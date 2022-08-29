package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.UserService;
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
import java.io.IOException;

@Controller
public class NewsController {

    private final NewsService newsService;
    private final UserService userService;

    @Autowired
    public NewsController(final NewsService newsService, final UserService userService){
        this.newsService = newsService;
        this.userService = userService;
    }


    @RequestMapping(value = "/news/create", method = RequestMethod.GET)
    public ModelAndView createNewsForm(@ModelAttribute("createNewsForm") final CreateNewsForm createNewsForm){
        final ModelAndView mav = new ModelAndView("create_news");
        return mav;
    }

    @RequestMapping(value = "/news/create", method = RequestMethod.POST)
    public ModelAndView postNewsForm(@Valid @ModelAttribute("createNewsForm") final CreateNewsForm createNewsFrom,
                                     final BindingResult errors) throws IOException {
        if(errors.hasErrors()){
            return createNewsForm(createNewsFrom);
        }

        final User user = userService.createIfNotExists(createNewsFrom.getCreatorEmail());
        final News.NewsBuilder newsBuilder = new News.NewsBuilder(user.getId(), createNewsFrom.getBody(), createNewsFrom.getTitle(), createNewsFrom.getSubtitle())
                .image(createNewsFrom.getImage().getBytes());

        final News news = newsService.create(newsBuilder);
        return new ModelAndView("redirect:/news/successfullycreated");
    }

    @RequestMapping(value = "/news/successfullycreated", method = RequestMethod.GET)
    public ModelAndView newsSuccessfullyCreated(){
        final ModelAndView mav = new ModelAndView("news_successfully_created");
        return mav;
    }

}
