package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.webapp.form.CreateNewsForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class NewsController {

    private final NewsService newsService;

    @Autowired
    public NewsController(final NewsService newsService){
        this.newsService = newsService;
    }


    @RequestMapping(value = "/news/create", method = RequestMethod.GET)
    public ModelAndView createNewsForm(@ModelAttribute("createNewsForm") final CreateNewsForm createNewsForm){
        final ModelAndView mav = new ModelAndView("create_news");
        return mav;
    }

    @RequestMapping(value = "/news/create", method = RequestMethod.POST)
    public ModelAndView postNewsForm(@Valid @ModelAttribute("createNewsForm") final CreateNewsForm createNewsFrom, final BindingResult errors){
        if(errors.hasErrors()){
            return createNewsForm(createNewsFrom);
        }

        //TODO: find or create user with email createNewsFrom.getCreatorEmail()
        // 0 is just to start
        final News.NewsBuilder newsBuilder = new News.NewsBuilder(1, createNewsFrom.getBody(), createNewsFrom.getTitle(), createNewsFrom.getSubtitle());

        final News news = newsService.create(newsBuilder);
        return new ModelAndView("redirect:/news/successfullycreated");
    }

    @RequestMapping(value = "/news/successfullycreated", method = RequestMethod.GET)
    public ModelAndView newsSuccessfullyCreated(){
        final ModelAndView mav = new ModelAndView("news_successfully_created");
        return mav;
    }

}
