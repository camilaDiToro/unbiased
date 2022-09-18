package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EmailService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
public class HomeController {

    private final UserService us;
    private final NewsService ns;
    private final SecurityService ss;
    private final EmailService es;

    @Autowired
    public HomeController(@Qualifier("userServiceImpl") final UserService us, final NewsService ns, SecurityService ss, EmailService es){
        this.us = us;
        this.ns = ns;
        this.ss = ss;
        this.es = es;
    }

    @RequestMapping("/")
    public ModelAndView homePage( @RequestParam(name = "userId", defaultValue = "1") final long userId){
        return new ModelAndView("redirect:/TOP");
    }

    @RequestMapping("/{orderBy:TOP|NEW}")
    public ModelAndView helloWorld(
            @PathVariable("orderBy") final String orderBy,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "query", defaultValue = "") final String query,
            @RequestParam(name = "category", defaultValue = "ALL") final String category){
        final ModelAndView mav = new ModelAndView("index");


        Map<Long, Rating> ratingMap = new HashMap<>();
        Map<Long, Boolean> savedMap = new HashMap<>();





        Optional<User> user = ss.getCurrentUser();



        mav.addObject("user", user.orElse(null));


        mav.addObject("orders", NewsOrder.values());
        mav.addObject("orderBy", orderBy);
        mav.addObject("query", query);
        mav.addObject("categories", Category.values());
        mav.addObject("pageTitle", query.equals("") ? "Home" : "Search");
        mav.addObject("category", category.equals("ALL")? category:Category.getByValue(category));

        Page<FullNews> newsPage = ns.getNews(page,category,orderBy,query);

        List<FullNews> newsContent = newsPage.getContent();

        for (FullNews news : newsContent) {
            News article = news.getNews();
            long newsId = article.getNewsId();
            ratingMap.put(newsId, user.map(u -> ns.upvoteState(article, u)).orElse(Rating.NO_RATING));
            savedMap.put(newsId, user.map(u -> ns.isSaved(article, u)).orElse(false));
        }


        mav.addObject("topCreators", us.getTopCreators(5));

        mav.addObject("ratingMap", ratingMap);
        mav.addObject("savedMap", savedMap);




        mav.addObject("newsPage", newsPage);
//        mav.addObject("page", newsPage.getCurrentPage());
//        mav.addObject("totalPages", newsPage.getTotalPages());
//        mav.addObject("minPage",newsPage.getMinPage());
//        mav.addObject("maxPage",newsPage.getMaxPage());

        return mav;
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

        return new ResponseEntity<>(new UpvoteActionResponse(ns.getUpvotes(newsId), active), HttpStatus.OK);
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
        return new ModelAndView("errors/userNotFound");
    }
}
