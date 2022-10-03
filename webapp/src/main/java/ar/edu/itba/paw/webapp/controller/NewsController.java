package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.SavedResult;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.exeptions.ImageNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.webapp.auth.OwnerCheck;
import ar.edu.itba.paw.webapp.form.CommentNewsForm;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.CreateNewsForm;
import ar.edu.itba.paw.webapp.form.ReportNewsForm;
import ar.edu.itba.paw.webapp.model.MAVBuilderSupplier;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
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
    private final AdminService adminService;
    private final OwnerCheck ownerCheck;
    private final MAVBuilderSupplier mavBuilderSupplier;


    @Autowired
    public NewsController(final AdminService adminService, final NewsService newsService, final UserService userService, ImageService imageService, SecurityService ss, OwnerCheck ownerCheck){
        this.newsService = newsService;
        this.userService = userService;
        this.imageService = imageService;
        this.securityService = ss;
        this.adminService = adminService;
        this.ownerCheck = ownerCheck;
        mavBuilderSupplier = (view, title, textType) -> new MyModelAndView.Builder(view, title, textType, securityService);
    }


    @PreAuthorize("@ownerCheck.checkNewsOwnership(#newsId)")
    @RequestMapping(value = "/news/{newsId:[0-9]+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNews(@PathVariable("newsId") long newsId) {
        News news = newsService.getSimpleNewsById(newsId).orElseThrow(NewsNotFoundException::new);
        newsService.deleteNews(news);
        return new ModelAndView("redirect:/profile/" + news.getCreatorId());
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}/report", method = RequestMethod.POST)
    public ModelAndView reportNews(@PathVariable("newsId") long newsId,@Valid @ModelAttribute("reportNewsForm") final ReportNewsForm reportNewsFrom,
                                   final BindingResult errors) {
        if (errors.hasErrors()) {
            return showNews(newsId, reportNewsFrom,new CommentNewsForm(),true, 1);
        }
        adminService.reportNews(newsService.getSimpleNewsById(newsId).orElseThrow(NewsNotFoundException::new), ReportReason.valueOf(reportNewsFrom.getReason()));
        return new ModelAndView("redirect:/news/" + newsId);
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}/comment", method = RequestMethod.POST)
    public ModelAndView commentNews(@PathVariable("newsId") long newsId,@Valid @ModelAttribute("commentNewsForm") final CommentNewsForm commentNewsForm,
                                    final BindingResult errors) {
        if (errors.hasErrors()) {
            return showNews(newsId, new ReportNewsForm(),commentNewsForm, false, 1);
        }
        newsService.addComment(newsService.getOrThrowException(newsId).getNews(), commentNewsForm.getComment());
        return new ModelAndView("redirect:/news/" + newsId);
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView showNews(@PathVariable("newsId") long newsId,@ModelAttribute("reportNewsForm") final ReportNewsForm reportNewsFrom,
                                 @ModelAttribute("commentNewsForm") final CommentNewsForm commentNewsFrom,
                                 @RequestParam(name="hasErrors", defaultValue="false") boolean hasErrors,
    @RequestParam(name="page", defaultValue="1") int page){

        FullNews fullNews = newsService.getById(newsId).orElseThrow(NewsNotFoundException::new);
        News news = fullNews.getNews();
        Locale locale = LocaleContextHolder.getLocale();

        return mavBuilderSupplier.supply("show_news", news.getTitle(), TextType.LITERAL)
                .withObject("date", news.getFormattedDate(locale))
                .withObject("fullNews", fullNews)
                .withObject("hasReported", adminService.hasReported(news))
                .withObject("reportReasons", ReportReason.values())
                .withObject("reportNewsForm", reportNewsFrom)
                .withObject("commentNewsForm", commentNewsFrom)
                .withObject("hasErrors", hasErrors)
                .withObject("locale", locale)
                .withObject("commentsPage", newsService.getComments(news,page))
                .withObject("categories", newsService.getNewsCategory(fullNews.getNews())).build();

    }

    @RequestMapping( value = "/news/{imageId:[0-9]+}/image", method = {RequestMethod.GET},
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] newsImage(@PathVariable(value = "imageId") long imageId) {
         return imageService.getImageById(imageId).orElseThrow(ImageNotFoundException::new).getBytes();
    }

    @RequestMapping(value = "/create_article", method = RequestMethod.GET)
    public ModelAndView createArticle(@ModelAttribute("createNewsForm") final CreateNewsForm createNewsForm){
        return mavBuilderSupplier.supply("create_article", "pageTitle.createArticle", TextType.INTERCODE)
                .withObject("categories", Category.getTrueCategories())
                .withObject("validate", false).build();
    }


    @RequestMapping(value = "/news/{newsId:[0-9]+}/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SavedResult> saveNews(@PathVariable(value = "newsId") long newsId){
        Optional<User> maybeUser = securityService.getCurrentUser();
        Optional<FullNews> maybeNews = newsService.getById(newsId);

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

        final User user = securityService.getCurrentUser().get();
        final News.NewsBuilder newsBuilder = new News.NewsBuilder(user.getId(), createNewsFrom.getBody(), createNewsFrom.getTitle(), createNewsFrom.getSubtitle());

        if(!createNewsFrom.getImage().isEmpty()){
            newsBuilder.imageId(imageService.uploadImage(createNewsFrom.getImage().getBytes(), createNewsFrom.getImage().getContentType()));
        }

        for(String s : createNewsFrom.getCategories()){
            System.out.println("%" + s);
        }

        final News news = newsService.create(newsBuilder, createNewsFrom.getCategories());
        return new ModelAndView("redirect:/news/" + news.getNewsId());
    }
}
