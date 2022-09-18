package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.Category;
import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.model.exeptions.ImageNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.webapp.form.CreateNewsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.Optional;

@Controller
public class NewsController {

    private final NewsService newsService;
    private final UserService userService;
    private final ImageService imageService;

    private final SecurityService securityService;

    @Autowired
    public NewsController(final NewsService newsService, final UserService userService, ImageService imageService, SecurityService ss){
        this.newsService = newsService;
        this.userService = userService;
        this.imageService = imageService;
        this.securityService = ss;
    }


//    @RequestMapping(value = "/news/create", method = RequestMethod.POST)
//    public ModelAndView postNewsForm(@Valid @ModelAttribute("createNewsForm") final CreateNewsForm createNewsFrom,
//                                     final BindingResult errors) throws IOException {
//        if(errors.hasErrors()){
//            return createArticle(createNewsFrom);
//        }
//
//
//
//        final User.UserBuilder userBuilder = new User.UserBuilder(createNewsFrom.getCreatorEmail());
//        final User user = userService.createIfNotExists(userBuilder);
//        final News.NewsBuilder newsBuilder = new News.NewsBuilder(user.getId(), createNewsFrom.getBody(), createNewsFrom.getTitle(), createNewsFrom.getSubtitle());
//
//        if(createNewsFrom.getImage()!=null && createNewsFrom.getImage().getBytes().length!=0){
//            newsBuilder.imageId(imageService.uploadImage(createNewsFrom.getImage().getBytes(), createNewsFrom.getImage().getContentType()));
//        }
//
//
//        final News news = newsService.create(newsBuilder);
//        return new ModelAndView("redirect:/news/" + news.getNewsId());
//    }

//    @RequestMapping(value = "/news/successfullycreated", method = RequestMethod.GET)
//    public ModelAndView newsSuccessfullyCreated(){
//        final ModelAndView mav = new ModelAndView("news_successfully_created");
//        return mav;
//    }

    @RequestMapping(value = "/news/create", method = RequestMethod.POST)
    public ModelAndView postNewsForm(@Valid @ModelAttribute("createNewsForm") final CreateNewsForm createNewsFrom,
                                     final BindingResult errors) throws IOException {
        if(errors.hasErrors()){
            return createArticle(createNewsFrom);
        }

        final User.UserBuilder userBuilder = new User.UserBuilder(createNewsFrom.getCreatorEmail());
        final User user = userService.createIfNotExists(userBuilder);
        final News.NewsBuilder newsBuilder = new News.NewsBuilder(user.getId(), createNewsFrom.getBody(), createNewsFrom.getTitle(), createNewsFrom.getSubtitle());

        if(createNewsFrom.getImage()!=null && createNewsFrom.getImage().getBytes().length!=0){
            newsBuilder.imageId(imageService.uploadImage(createNewsFrom.getImage().getBytes(), createNewsFrom.getImage().getContentType()));
        }


        final News news = newsService.create(newsBuilder);
        return new ModelAndView("redirect:/news/" + news.getNewsId());
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView profile(@PathVariable("newsId") long newsId){
        final ModelAndView mav = new ModelAndView("show_news");

        Optional<User> maybeUser = securityService.getCurrentUser();


        //TODO: check if there is a better way of doing this.
        FullNews fullNews = newsService.getFullNewsById(newsId).orElseThrow(NewsNotFoundException::new);
        News news = fullNews.getNews();
        mav.addObject("rating",maybeUser.map(u -> newsService.upvoteState(fullNews.getNews(), u)).orElse(Rating.NO_RATING));

        Locale locale = LocaleContextHolder.getLocale();
        mav.addObject("date", LocalDate.now().format(DateTimeFormatter.ofLocalizedDate( FormatStyle.FULL )
                .withLocale( locale)));
        mav.addObject("fullNews", fullNews);
        mav.addObject("loggedUser", maybeUser.orElse(null));

        mav.addObject("categories", newsService.getNewsCategory(news));
        mav.addObject("user", userService.getUserById(news.getCreatorId()).orElseThrow(NewsNotFoundException::new));
        return mav;
    }

    @RequestMapping( value = "/news/{imageId:[0-9]+}/image", method = {RequestMethod.GET},
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] newsImage(@PathVariable(value = "imageId") long imageId) {
         return imageService.getImageById(imageId).orElseThrow(ImageNotFoundException::new).getBytes();
    }


    @RequestMapping(value = "/create_article", method = RequestMethod.GET)
    public ModelAndView createArticle(@ModelAttribute("createNewsForm") final CreateNewsForm createNewsForm){
        final ModelAndView mav = new ModelAndView("create_article");
        mav.addObject("categories", Category.values());
        mav.addObject("validate", false);
        mav.addObject("loggedUser", securityService.getCurrentUser().orElse(null));

        return mav;
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SavedResult> saveNews(@PathVariable(value = "newsId") long newsId){
        Optional<User> maybeUser = securityService.getCurrentUser();
        Optional<News> maybeNews = newsService.getById(newsId);

        if (!maybeUser.isPresent() || !maybeNews.isPresent()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        SavedResult savedResult = new SavedResult(newsService.toggleSaveNews(maybeNews.get(), maybeUser.get()));
        return new ResponseEntity<>(savedResult, HttpStatus.OK);
    }

    private ModelAndView createArticleAndValidate(@ModelAttribute("createNewsForm") final CreateNewsForm createNewsForm, final BindingResult errors){
        final ModelAndView mav = new ModelAndView("create_article");
        mav.addObject("categories", Category.values());
        mav.addObject("errors", errors);
        mav.addObject("validate", true);
        return mav;
    }

    @RequestMapping(value = "/create_article", method = RequestMethod.POST)
    public ModelAndView postArticle(@Valid @ModelAttribute("createNewsForm") final CreateNewsForm createNewsFrom,
                                     final BindingResult errors) throws IOException {
        if(errors.hasErrors()){
            return createArticleAndValidate(createNewsFrom, errors);
        }

        String htmlBody = TextUtils.convertMarkdownToHTML(createNewsFrom.getBody());

        final User user = securityService.getCurrentUser().get();
        final News.NewsBuilder newsBuilder = new News.NewsBuilder(user.getId(), createNewsFrom.getBody(), createNewsFrom.getTitle(), createNewsFrom.getSubtitle());


        for(String category : createNewsFrom.getCategories()){
            Category c = Category.getByInterCode(category);
            if(c==null)
                throw new InvalidCategoryException();
            newsBuilder.addCategory(c);
        }

        if(!createNewsFrom.getImage().isEmpty()){
            newsBuilder.imageId(imageService.uploadImage(createNewsFrom.getImage().getBytes(), createNewsFrom.getImage().getContentType()));
        }

        final News news = newsService.create(newsBuilder);
        return new ModelAndView("redirect:/news/" + news.getNewsId());
    }




}
