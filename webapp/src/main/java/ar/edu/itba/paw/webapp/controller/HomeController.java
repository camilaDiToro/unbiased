package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.NewsOrder;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class HomeController {

    private final UserService us;
    private final NewsService ns;

    @Autowired
    public HomeController(@Qualifier("userServiceImpl") final UserService us, final NewsService ns){
        this.us = us;
        this.ns = ns;
    }

    @RequestMapping("/")
    public ModelAndView homePage( @RequestParam(name = "userId", defaultValue = "1") final long userId){
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

        mav.addObject("orders", NewsOrder.values());
        mav.addObject("orderBy", orderBy);
        mav.addObject("query", query);
        mav.addObject("categories", Category.values());
        mav.addObject("pageTitle", query.equals("") ? "Home" : "Search");
        int totalPages;
        page = page <= 0 ? 1 : page;

        if (category.equals("ALL")) {
            mav.addObject("category", category);
            totalPages = ns.getTotalPagesAllNews(query);
            page = page > totalPages ? totalPages : page;
            mav.addObject("news", ns.getNews(page, query, NewsOrder.valueOf(orderBy)));
        }
        else {
            Category catObject = Category.valueOf(category);
            mav.addObject("category", catObject);
            totalPages = ns.getTotalPagesCategory(catObject);
            page = page > totalPages ? totalPages : page;
            mav.addObject("news", ns.getNewsByCategory(page, catObject, NewsOrder.valueOf(orderBy)));
        }

        mav.addObject("page", page);
        mav.addObject("totalPages", totalPages);



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




    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound()    {
        return new ModelAndView("errors/userNotFound");
    }
}
