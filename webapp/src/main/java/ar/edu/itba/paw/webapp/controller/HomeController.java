package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.model.MAVBuilderSupplier;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;


@Controller
public class HomeController {

    private final UserService userService;
    private final NewsService newsService;
    private final SecurityService securityService;
    private final MAVBuilderSupplier mavBuilderSupplier;

    @Autowired
    public HomeController(final UserService userService, final NewsService newsService, SecurityService securityService){
        this.userService = userService;
        this.newsService = newsService;
        this.securityService = securityService;
        mavBuilderSupplier = (view, title, textType) -> new MyModelAndView.Builder(view, title, textType, securityService);
    }

    @RequestMapping("/")
    public ModelAndView homePage( @RequestParam(name = "userId", defaultValue = "1") final long userId){
        return new ModelAndView("redirect:/" + NewsOrder.values()[0]);
    }

    @RequestMapping("/{orderBy}")
    public ModelAndView helloWorld(
            @PathVariable("orderBy") final String orderBy,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "query", defaultValue = "") final String query,
            @RequestParam(name = "category", defaultValue = "ALL") final String category,
            @RequestParam(name="type", defaultValue="article") String type){


        Page<News> newsPage = newsService.getNews(page,category,orderBy,query);

        MyModelAndView.Builder builder= mavBuilderSupplier.supply("index", "pageTitle.home", TextType.INTERCODE)
                .withObject("orders", NewsOrder.values())
                .withObject("orderBy", orderBy)
                .withObject("query", query)
                .withObject("type", type)
                .withObject("categories", newsService.getHomeCategories())
                .withObject("topCreators", userService.getTopCreators(5))
                .withObject("category", category.equals("ALL")? category:Category.getByValue(category));

        if (type.equals("creator")) {
            builder.withObject("usersPage", userService.searchUsers(page, query));
        }
        else if (type.equals("article")) {
            builder.withObject("newsPage", newsPage);
        }

        return builder.build();
    }

    private ResponseEntity<UpvoteActionResponse> toggleHandler(UpvoteAction payload, Rating action) {
        final Long newsId = payload.getNewsId();
        final boolean isActive = payload.isActive();

        News news = newsService.getOrThrowException(newsId);

        newsService.setRating(news, isActive ? action : Rating.NO_RATING);


        return new ResponseEntity<>(new UpvoteActionResponse(news.getPositivityStats().getNetUpvotes(), isActive), HttpStatus.OK);
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
}
