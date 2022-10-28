package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
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
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.io.IOException;
import java.util.*;

@Controller
public class NewsController {

    private final NewsService newsService;
    private final ImageService imageService;
    private final SecurityService securityService;
    private final AdminService adminService;

    private final UserService userService;
    private final OwnerCheck ownerCheck;
    private final MAVBuilderSupplier mavBuilderSupplier;


    @Autowired
    public NewsController(AdminService adminService, UserService userService, NewsService newsService, ImageService imageService, SecurityService ss, OwnerCheck ownerCheck){
        this.newsService = newsService;
        this.imageService = imageService;
        this.securityService = ss;
        this.adminService = adminService;
        this.userService = userService;
        this.ownerCheck = ownerCheck;
        mavBuilderSupplier = (view, title, textType) -> new MyModelAndView.Builder(view, title, textType, securityService);
    }


    @PreAuthorize("@ownerCheck.checkNewsOwnership(#newsId)")
    @RequestMapping(value = "/news/{newsId:[0-9]+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNews(@PathVariable("newsId") long newsId) {
        News news = newsService.getById(newsId).orElseThrow(NewsNotFoundException::new);
        newsService.deleteNews(news);
        return new ModelAndView("redirect:/profile/" + news.getCreatorId());
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}/report", method = RequestMethod.POST)
    public ModelAndView reportNews(@PathVariable("newsId") long newsId,@Valid @ModelAttribute("reportNewsForm") final ReportNewsForm reportNewsFrom,
                                   final BindingResult errors) {
        if (errors.hasErrors()) {
            return showNews(newsId, reportNewsFrom,new CommentNewsForm(),true, 1);
        }
        adminService.reportNews(newsService.getById(newsId).orElseThrow(NewsNotFoundException::new), ReportReason.valueOf(reportNewsFrom.getReason()));
        return new ModelAndView("redirect:/news/" + newsId);
    }

    @RequestMapping(value = "/news/comment/{commentId:[0-9]+}/report", method = RequestMethod.POST)
    public ModelAndView reportComment(@PathVariable("commentId") long commentId,@Valid @ModelAttribute("reportNewsForm") final ReportNewsForm reportNewsFrom,
                                   final BindingResult errors) {
        if (errors.hasErrors()) {
            return showNews(commentId, reportNewsFrom,new CommentNewsForm(),true, 1);
        }
        Comment comment = newsService.getCommentById(commentId).orElseThrow(CommentNotFoundException::new);
        adminService.reportComment(comment, ReportReason.valueOf(reportNewsFrom.getReason()));
        return new ModelAndView("redirect:/news/" + comment.getNews().getNewsId() + "#" + "comment-" + commentId);
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}/comment", method = RequestMethod.POST)
    public ModelAndView commentNews(@PathVariable("newsId") long newsId,@Valid @ModelAttribute("commentNewsForm")
                                            final CommentNewsForm commentNewsForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return showNews(newsId, new ReportNewsForm(),commentNewsForm, false, 1);
        }
        newsService.addComment(newsService.getOrThrowException(newsId), commentNewsForm.getComment());
        return new ModelAndView("redirect:/news/" + newsId);
    }

    @PreAuthorize("@ownerCheck.checkCommentOwnership(#commentId)")
    @RequestMapping(value = "/news/{newsId:[0-9]+}/comment/{commentId:[0-9]+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteComment(@PathVariable("newsId") long newsId, @PathVariable("commentId") long commentId) {
        newsService.deleteComment(commentId);
        return new ModelAndView("redirect:/news/" + newsId);
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView showNews(@PathVariable("newsId") long newsId,@ModelAttribute("reportNewsForm") final ReportNewsForm reportNewsFrom,
                                 @ModelAttribute("commentNewsForm") final CommentNewsForm commentNewsFrom,
                                 @RequestParam(name="hasErrors", defaultValue="false") boolean hasErrors,
    @RequestParam(name="page", defaultValue="1") int page){

        News news = newsService.getById(newsId).orElseThrow(NewsNotFoundException::new);
        Locale locale = LocaleContextHolder.getLocale();

//        adminService.reportComment(newsService.getComments(news, 1).getContent().get(0), ReportReason.INAP);

        Page<Comment> comments = newsService.getComments(news,page);


        MyModelAndView.Builder builder =  mavBuilderSupplier.supply("show_news", news.getTitle(), TextType.LITERAL)
                .withObject("date", news.getFormattedDate(locale))
                .withObject("fullNews", news)
                .withObject("hasReported", adminService.hasReported(news))
                .withObject("reportReasons", ReportReason.values())
                .withObject("reportNewsForm", reportNewsFrom)
                .withObject("commentNewsForm", commentNewsFrom)
                .withObject("hasErrors", hasErrors)
                .withObject("locale", locale)
                .withObject("commentsPage", newsService.getComments(news,page))
                .withObject("categories", newsService.getNewsCategory(news));

        Optional<User> loggedUser = securityService.getCurrentUser();

        Map<Long, Boolean> hasReportedComment;
        if (loggedUser.isPresent()) {
            hasReportedComment = new HashMap<>();
            User user = loggedUser.get();
            comments.getContent().forEach(c -> hasReportedComment.put(c.getId(), user.hasReportedComment(c)));
            builder.withObject("hasReportedCommentMap", hasReportedComment)
                    .withObject("myNews", news.getCreator().equals(user))
                    .withObject("pinned", news.equals(user.getPingedNews()));
        }

        return builder.build();

    }

    @RequestMapping( value = "/news/{imageId:[0-9]+}/image", method = {RequestMethod.GET},
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] newsImage(@PathVariable("imageId") long imageId) {
         return imageService.getImageById(imageId).orElseThrow(ImageNotFoundException::new).getBytes();
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}/pingNews", method = RequestMethod.POST)
    public ModelAndView pingNews(@PathVariable("newsId") final long newsId) {

        userService.pingNewsToggle(newsService.getById(newsId).orElseThrow(NewsNotFoundException::new));

        return new ModelAndView("redirect:/news/" + newsId);
    }

    @RequestMapping(value = "/create_article", method = RequestMethod.GET)
    public ModelAndView createArticle(@ModelAttribute("createNewsForm") final CreateNewsForm createNewsForm){
        return mavBuilderSupplier.supply("create_article", "pageTitle.createArticle", TextType.INTERCODE)
                .withObject("categories", Category.getTrueCategories())
                .withObject("validate", false).build();
    }


    @RequestMapping(value = "/news/{newsId:[0-9]+}/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SavedResult> saveNews(@PathVariable("newsId") long newsId){
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

        final User user = securityService.getCurrentUser().get();
        final News.NewsBuilder newsBuilder = new News.NewsBuilder(user, createNewsFrom.getBody(), createNewsFrom.getTitle(), createNewsFrom.getSubtitle());

        if(!createNewsFrom.getImage().isEmpty()){
            newsBuilder.imageId(imageService.uploadImage(createNewsFrom.getImage().getBytes(), createNewsFrom.getImage().getContentType()));
        }

        final News news = newsService.create(newsBuilder, createNewsFrom.getCategories());
        return new ModelAndView("redirect:/news/" + news.getNewsId());
    }
}
