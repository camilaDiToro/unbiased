package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.service.EmailService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
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
import java.util.List;
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


        mav.addObject("orders", NewsOrder.values());
        mav.addObject("orderBy", orderBy);
        mav.addObject("query", query);
        mav.addObject("categories", Category.values());
        mav.addObject("pageTitle", query.equals("") ? "Home" : "Search");
        mav.addObject("category", category.equals("ALL")? category:Category.getByValue(category));

        Page<News> newsPage = ns.getNews(page,category,orderBy,query);
        mav.addObject("news", newsPage.getContent());
        mav.addObject("page", newsPage.getCurrentPage());
        mav.addObject("totalPages", newsPage.getTotalPages());
        mav.addObject("minPage",newsPage.getMinPage());
        mav.addObject("maxPage",newsPage.getMaxPage());
        return mav;
    }

    @RequestMapping("/chau")
    public ModelAndView goodbyeWorld(final long userId){
        final ModelAndView mav = new ModelAndView("byebye");
        mav.addObject("user",us.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

}
