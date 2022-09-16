package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.service.EmailService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

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
        //Optional<User> mayBeUser = ss.getCurrentUser();
        //es.sendSimpleMessage("cditoro@itba.edu.ar", "First email", "holi");
        return new ModelAndView("redirect:/TOP");
    }

//    @RequestMapping("/{orderBy}")
//    public ModelAndView helloWorld( @RequestParam(name = "userId", defaultValue = "1") final long userId){
//        final ModelAndView mav = new ModelAndView("index");
//        mav.addObject("user",us.getUserById(userId).orElseThrow(UserNotFoundException::new));
//        return mav;
//    }

    @RequestMapping("/{orderBy:TOP|NEW}")
    public ModelAndView helloWorld(
            @PathVariable("orderBy") final String orderBy,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "query", defaultValue = "") final String query,
            @RequestParam(name = "category", defaultValue = "ALL") final String category){
        final ModelAndView mav = new ModelAndView("index");

//        ns.setRating((long)1, (long)1, Rating.UPVOTE);
        mav.addObject("orders", NewsOrder.values());
        mav.addObject("orderBy", orderBy);
        mav.addObject("query", query);
        mav.addObject("categories", Category.values());
        mav.addObject("pageTitle", query.equals("") ? "Home" : "Search");
        int totalPages;
        page = page <= 0 ? 1 : page;

        List<News> news;

        if (category.equals("ALL")) {
            mav.addObject("category", category);
            totalPages = ns.getTotalPagesAllNews(query);
            page = page > totalPages ? totalPages : page;
            news =  ns.getNews(page, query, NewsOrder.valueOf(orderBy));
        }
        else {
            Category catObject = Category.valueOf(category);
            mav.addObject("category", catObject);
            totalPages = ns.getTotalPagesCategory(catObject);
            page = page > totalPages ? totalPages : page;
            news =  ns.getNewsByCategory(page, catObject, NewsOrder.valueOf(orderBy));
        }

        Map<Long, Integer> readTimeMap = new HashMap<>();
        Map<Long, Integer> upvotesMap = new HashMap<>();
        Map<Long, Rating> ratingMap = new HashMap<>();
        Map<Long, String> creatorMap = new HashMap<>();
        Map<Long, Positivity> positivityMap = new HashMap<>();

        Optional<User> user = ss.getCurrentUser();

        for (News article : news) {
            long newsId = article.getNewsId();
            readTimeMap.put(newsId, TextUtils.estimatedMinutesToRead(TextUtils.extractTextFromHTML(article.getBody())));
            upvotesMap.put(newsId, ns.getUpvotes(article.getNewsId()));
            ratingMap.put(newsId, user.map(u -> ns.upvoteState(article, u)).orElse(Rating.NO_RATING));
            User u = us.getUserById(article.getCreatorId()).get();
            String name = u.getUsername();
            if (name == null)
                name = u.getEmail();
            creatorMap.put(newsId,name);
            positivityMap.put(newsId, ns.getPositivityBracket(newsId));
        }

        mav.addObject("readTimeMap", readTimeMap);
        mav.addObject("upvotesMap", upvotesMap);
        mav.addObject("ratingMap", ratingMap);
        mav.addObject("creatorMap", creatorMap);
        mav.addObject("positivityMap", positivityMap);



        mav.addObject("news", news);
        mav.addObject("user", user.orElse(null));

        mav.addObject("page", page);
        mav.addObject("totalPages", totalPages);

        User userMessi = new User.UserBuilder("messi@messi.com").username("Leo Messi").userId(1).build();

//        List<User> topCreators = new ArrayList<>();

//        for (int i=0 ; i<5 ; i++) {
//            topCreators.add(userMessi);
//        }

        mav.addObject("topCreators", us.getTopCreators(5));



        int minPage = 1;
        if (page - 2 >= 1)
            minPage = page - 2;
        else if (page - 1 >= 1)
            minPage = page - 1;
        mav.addObject("minPage",minPage);


        int maxPage = page;
        if (page + 2 <= totalPages) {
            maxPage = page + 2;
        }
        else if (page + 1 <= totalPages)
            maxPage = page + 1;

        mav.addObject("maxPage",maxPage);
        return mav;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createForm(@ModelAttribute("registerForm") final UserForm userForm){
        final ModelAndView mav = new ModelAndView("register");
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm userForm, final BindingResult errors){
        if(errors.hasErrors()){
            return createForm(userForm);
        }
        User.UserBuilder userBuilder = new User.UserBuilder(userForm.getEmail()).pass(userForm.getPassword());
        final User user = us.create(userBuilder);
        return new ModelAndView("redirect:/profile/"+user.getId());
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView profile(@PathVariable("userId") long userId, @Valid @ModelAttribute("userProfileForm") final UserProfileForm userProfileForm){
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user",us.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}", method = RequestMethod.POST)
    public ModelAndView profilePicture(@PathVariable("userId") long userId, @Valid @ModelAttribute("userProfileForm") final UserProfileForm userProfileForm, final BindingResult errors) throws IOException {
        if(errors.hasErrors()){
            return profile(userId, userProfileForm);
        }
        return new ModelAndView("redirect:/profile/"+userId);
    }

    @RequestMapping("/chau")
    public ModelAndView goodbyeWorld(final long userId){
        final ModelAndView mav = new ModelAndView("byebye");
        mav.addObject("user",us.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    private String toggleHandler(String payload, Rating action) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, Object> map = mapper.readValue(payload, Map.class);
        final String fmt = "{ \"upvotes\": %d, \"active\": %b }";
        final Long newsId = (Long.valueOf((String)map.get("newsId")));
        Optional<User> maybeUser = ss.getCurrentUser();
        boolean active;

        if (maybeUser.isPresent()) {
            active = (boolean)map.get("active");
            User user = maybeUser.get();
            ns.setRating(newsId,  user.getId(), active ? action : Rating.NO_RATING);
        }
        else {
            active = !(boolean)map.get("active");
        }
        return String.format(fmt, ns.getUpvotes(newsId), active);
    }

    @RequestMapping(value = "/change-upvote", method = RequestMethod.POST, produces="application/json", consumes = "application/json")
    @ResponseBody
    public String toggleUpvote(@RequestBody String payload) throws JsonProcessingException {
        return toggleHandler(payload, Rating.UPVOTE);
    }

    @RequestMapping(value = "/change-downvote", method = RequestMethod.POST, produces="application/json", consumes = "application/json")
    @ResponseBody
    public String toggleDownvote(@RequestBody String payload) throws JsonProcessingException {
        return toggleHandler(payload, Rating.DOWNVOTE);

    }
    @RequestMapping("/verify_email")
    public ModelAndView verifyEmail(@RequestParam(name = "token") final String token) {
        us.verifyUserEmail(token);
        return new ModelAndView("email_verified");
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound()    {
        return new ModelAndView("errors/userNotFound");
    }
}
