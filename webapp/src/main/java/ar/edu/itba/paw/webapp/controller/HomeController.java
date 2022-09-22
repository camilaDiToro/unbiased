package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.service.EmailService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.model.MAVBuilderSupplier;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class HomeController {

    private final UserService us;
    private final NewsService ns;
    private final SecurityService ss;
    private final EmailService es;

    private final MAVBuilderSupplier mavBuilderSupplier;
;

    @Autowired
    public HomeController(@Qualifier("userServiceImpl") final UserService us, final NewsService ns, SecurityService ss, EmailService es){
        this.us = us;
        this.ns = ns;
        this.ss = ss;
        this.es = es;
        mavBuilderSupplier = (view, title, textType) -> new MyModelAndView.Builder(view, title, textType, ss.getCurrentUser());

    }

    @RequestMapping("/")
    public ModelAndView homePage( @RequestParam(name = "userId", defaultValue = "1") final long userId){
        return new ModelAndView("redirect:/TOP");
//        return new ModelAndView("verify_email");

    }

    @RequestMapping("/{orderBy:TOP|NEW}")
    public ModelAndView helloWorld(
            @PathVariable("orderBy") final String orderBy,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "query", defaultValue = "") final String query,
            @RequestParam(name = "category", defaultValue = "ALL") final String category){



        Page<FullNews> newsPage = ns.getNews(page,category,orderBy,query);


        return mavBuilderSupplier.supply("index", "pageTitle.home", TextType.INTERCODE)
                .withObject("topCreators", us.getTopCreators(5))
                .withObject("orders", NewsOrder.values())
                .withObject("orderBy", orderBy)
                .withObject("query", query)
                .withObject("categories", Category.values())
                .withObject("category", category.equals("ALL")? category:Category.getByValue(category))
                .withObject("newsPage", newsPage).build();
    }

    private ResponseEntity<UpvoteActionResponse> toggleHandler(UpvoteAction payload, Rating action) {
//        ObjectMapper mapper = new ObjectMapper();
//        Map<String, Object> map = mapper.readValue(payload, Map.class);
//        final String fmt = "{ \"upvotes\": %d, \"active\": %b }";

//        final Long newsId = (Long.valueOf((String)map.get("newsId")));
        final Long newsId = payload.getNewsId();
        final boolean isActive = payload.isActive();

        Optional<User> maybeUser = ss.getCurrentUser();
        boolean active;

        if (maybeUser.isPresent()) {
//            active = (boolean)map.get("active");
            active = isActive;

            User user = maybeUser.get();
            ns.setRating(newsId,  user.getId(), active ? action : Rating.NO_RATING);
        }
        else {
//            active = !(boolean)map.get("active");
            active = !isActive;
        }
        final FullNews news = ns.getById(newsId).orElseThrow(NewsNotFoundException::new);


        return new ResponseEntity<>(new UpvoteActionResponse(news.getNewsStats().getNetUpvotes(), active), HttpStatus.OK);
//        return String.format(fmt, ns.getUpvotes(newsId), active);
    }

    @RequestMapping(value = "/change-upvote", method = RequestMethod.POST, produces="application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<UpvoteActionResponse>  toggleUpvote(@RequestBody UpvoteAction payload){
        return toggleHandler(payload, Rating.UPVOTE);
    }

    @RequestMapping(value = "/change-downvote", method = RequestMethod.POST, produces="application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<UpvoteActionResponse>  toggleDownvote(@RequestBody UpvoteAction payload){
        return toggleHandler(payload, Rating.DOWNVOTE);
    }


    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound()    {
        return mavBuilderSupplier.supply("errors/userNotFound", "pageTitle.userNotFound", TextType.INTERCODE).build();
    }
}
