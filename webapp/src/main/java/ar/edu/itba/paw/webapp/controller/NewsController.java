package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.CreateNewsForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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

    @RequestMapping("/create_article")
    public ModelAndView createArticle(@ModelAttribute("createNewsForm") final CreateNewsForm createNewsForm){
        final ModelAndView mav = new ModelAndView("create_article");
        mav.addObject("pageTitle", "Create article");
        return mav;
    }

    @RequestMapping(value = "/news/create", method = RequestMethod.POST)
    public ModelAndView postNewsForm(@Valid @ModelAttribute("createNewsForm") final CreateNewsForm createNewsFrom,
                                     final BindingResult errors) throws IOException {
        if(errors.hasErrors()){
            return createArticle(createNewsFrom);
        }

        final User user = userService.createIfNotExists(createNewsFrom.getCreatorEmail());
        final News.NewsBuilder newsBuilder = new News.NewsBuilder(user, createNewsFrom.getBody(), createNewsFrom.getTitle(), createNewsFrom.getSubtitle());

        if(createNewsFrom.getImage()!=null)
            newsBuilder.image(createNewsFrom.getImage().getBytes());

        final News news = newsService.create(newsBuilder);
        return new ModelAndView("redirect:/news/successfullycreated");
    }

    @RequestMapping(value = "/news/successfullycreated", method = RequestMethod.GET)
    public ModelAndView newsSuccessfullyCreated(){
        final ModelAndView mav = new ModelAndView("news_successfully_created");
        return mav;
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView profile(@PathVariable("newsId") long newsId){
        final ModelAndView mav = new ModelAndView("show_news");
        News news = newsService.getById(newsId).orElseThrow(UserNotFoundException::new);
        mav.addObject("news",news);
        mav.addObject("pageTitle", news.getTitle());
        return mav;
    }

    @RequestMapping( value = "/news/{newsId:[0-9]+}/image", method = {RequestMethod.GET},
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] newsImage(@PathVariable(value = "newsId") long newsId) {
        return newsService.getById(newsId).orElseThrow(UserNotFoundException::new).getImage();
    }

}
