package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.CommentUpvoteAction;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TextType;
import ar.edu.itba.paw.model.news.TextUtils;
import ar.edu.itba.paw.model.news.UpvoteActionResponse;
import ar.edu.itba.paw.model.user.SavedResult;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.model.exeptions.ImageNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.service.AdminService;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.form.CommentNewsForm;
import ar.edu.itba.paw.webapp.form.CreateNewsForm;
import ar.edu.itba.paw.webapp.form.ReportNewsForm;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class NewsController{

    private final NewsService newsService;
    private final ImageService imageService;
    private final AdminService adminService;
    private final UserService userService;

    private final SecurityService securityService;


    @Autowired
    public NewsController(AdminService adminService, UserService userService, NewsService newsService, ImageService imageService, SecurityService ss){
        this.securityService = ss;
        this.newsService = newsService;
        this.imageService = imageService;
        this.adminService = adminService;
        this.userService = userService;
    }


    @PreAuthorize("@ownerCheck.checkNewsOwnership(#newsId)")
    @RequestMapping(value = "/news/{newsId:[0-9]+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNews(@PathVariable("newsId") long newsId) {
        final News news = newsService.getById(newsId).orElseThrow(NewsNotFoundException::new);
        newsService.deleteNews(news);
        return new ModelAndView("redirect:/profile/" + news.getCreatorId());
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}/report", method = RequestMethod.POST)
    public ModelAndView reportNews(@PathVariable("newsId") long newsId,@Valid @ModelAttribute("reportNewsForm") final ReportNewsForm reportNewsFrom,
                                   final BindingResult errors) {
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        if (errors.hasErrors()) {
            return showNews(newsId, reportNewsFrom,new CommentNewsForm(),true, 1, "TOP");
        }
        adminService.reportNews(currentUser, newsId, ReportReason.valueOf(reportNewsFrom.getReason()));
        return new ModelAndView("redirect:/news/" + newsId);
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}/comment/{commentId:[0-9]+}/report", method = RequestMethod.POST)
    public ModelAndView reportComment(@PathVariable("commentId") long commentId,@Valid @ModelAttribute("reportNewsForm") final ReportNewsForm reportNewsFrom,
                                      final BindingResult errors, @PathVariable("newsId") long newsId){
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);

        if (errors.hasErrors()) {
            return showNews(commentId, reportNewsFrom,new CommentNewsForm(),true, 1, "TOP");
        }

        adminService.reportComment(currentUser, commentId, ReportReason.valueOf(reportNewsFrom.getReason()));
        return new ModelAndView("redirect:/news/" + newsId + "#" + "comment-" + commentId);
    }

    @RequestMapping(value = "/news/{newsId:[0-9]+}/comment", method = RequestMethod.POST)
    public ModelAndView commentNews(@PathVariable("newsId") long newsId,@Valid @ModelAttribute("commentNewsForm")
                                            final CommentNewsForm commentNewsForm, final BindingResult errors,
                                    @RequestParam(name = "order") final String order) {
        final NewsOrder newsOrder = NewsOrder.getByValue(order);
        if (errors.hasErrors()) {
            return showNews(newsId, new ReportNewsForm(),commentNewsForm, false, 1, "TOP");
        }
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        newsService.addComment(currentUser, newsId, commentNewsForm.getComment());
        return new ModelAndView("redirect:/news/" + newsId + "?order=" + newsOrder.name());
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
    @RequestParam(name="page", defaultValue="1") int page,
                                 @RequestParam(name="order", defaultValue="TOP") String orderBy){
        final Optional<User> loggedUser = securityService.getCurrentUser();

        final News news = newsService.getById(loggedUser,newsId).orElseThrow(NewsNotFoundException::new);
        final Locale locale = LocaleContextHolder.getLocale();
        final NewsOrder orderByObj = NewsOrder.getByValue(orderBy);
        final long followers = userService.getFollowersCount(news.getCreatorId());

        final Page<Comment> comments = newsService.getComments(newsId, page, orderByObj);
        final Map<Long, Rating> commentRatings = newsService.getCommentsRating(comments.getContent(), loggedUser);
        final Map<Long, Long> commentCreatorsFollowers = new HashMap<>();
        comments.getContent().forEach(c -> commentCreatorsFollowers.put(c.getUser().getUserId(), userService.getFollowersCount(c.getUser().getUserId())));

        final MyModelAndView.Builder builder =  new MyModelAndView.Builder("show_news", news.getTitle(), TextType.LITERAL)
                .withObject("date", news.getFormattedDate(locale))
                .withObject("fullNews", news)
                .withObject("reportReasons", ReportReason.values())
                .withObject("reportNewsForm", reportNewsFrom)
                .withObject("commentNewsForm", commentNewsFrom)
                .withObject("hasErrors", hasErrors)
                .withObject("commentCreatorsFollowers", commentCreatorsFollowers)
                .withObject("creatorFollowers", followers)
                .withObject("locale", locale)
                .withObject("orders", NewsOrder.values())
                .withObject("orderBy", orderBy)
                .withObject("commentRatings", commentRatings)
                .withObject("commentsPage", newsService.getComments(newsId,page, orderByObj));



        final Map<Long, Boolean> hasReportedComment;
        if (loggedUser.isPresent()) {
            hasReportedComment = new HashMap<>();
            final User user = loggedUser.get();
            comments.getContent().forEach(c -> hasReportedComment.put(c.getId(), user.hasReportedComment(c)));
            builder.withObject("hasReportedCommentMap", hasReportedComment)
                    .withObject("myNews", news.getCreator().equals(user))
                    .withObject("pinned", news.equals(user.getPingedNews()))
                    .withObject("hasReported", adminService.hasReported(user.getId(), news.getNewsId()));
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
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        userService.pingNewsToggle(currentUser, newsService.getById(newsId).orElseThrow(NewsNotFoundException::new));

        return new ModelAndView("redirect:/news/" + newsId);
    }

    @RequestMapping(value = "/create_article", method = RequestMethod.GET)
    public ModelAndView createArticle(@ModelAttribute("createNewsForm") final CreateNewsForm createNewsForm){
        return new MyModelAndView.Builder("create_article", "pageTitle.createArticle", TextType.INTERCODE)
                .withObject("categories", Category.getTrueCategories())
                .withObject("validate", false).build();
    }


    @RequestMapping(value = "/news/{newsId:[0-9]+}/save", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<SavedResult> saveNews(@PathVariable("newsId") long newsId){
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        final SavedResult savedResult = new SavedResult(newsService.toggleSaveNews(currentUser, newsId));
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
        final News.NewsBuilder newsBuilder = new News.NewsBuilder(user, TextUtils.convertMarkdownToHTML(createNewsFrom.getBody()), createNewsFrom.getTitle(), createNewsFrom.getSubtitle());

        if(!createNewsFrom.getImage().isEmpty() && 0 != createNewsFrom.getImage().getBytes().length){
            newsBuilder.imageId(imageService.uploadImage(createNewsFrom.getImage().getBytes(), createNewsFrom.getImage().getContentType()));
        }

        final News news = newsService.create(newsBuilder, Arrays.stream(createNewsFrom.getCategories()).map(Category::getByCode).collect(Collectors.toList()));
        return new ModelAndView("redirect:/news/" + news.getNewsId());
    }

    private ResponseEntity<UpvoteActionResponse> toggleHandler(CommentUpvoteAction payload, Rating action) {
        final long commentId = payload.getCommentId();
        final boolean isActive = payload.isActive();
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        final Comment comment = newsService.getCommentById(commentId).orElseThrow(CommentNotFoundException::new);
        newsService.setCommentRating(currentUser, comment, isActive ? action : Rating.NO_RATING);

        return new ResponseEntity<>(new UpvoteActionResponse(comment.getPositivityStats().getNetUpvotes(), isActive), HttpStatus.OK);
    }

    @RequestMapping(value = "/change-comment-upvote", method = RequestMethod.POST, produces="application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<UpvoteActionResponse>  toggleUpvote(@RequestBody CommentUpvoteAction payload){
        return toggleHandler(payload, Rating.UPVOTE);
    }

    @RequestMapping(value = "/change-comment-downvote", method = RequestMethod.POST, produces="application/json", consumes = "application/json")
    @ResponseBody
    public ResponseEntity<UpvoteActionResponse>  toggleDownvote(@RequestBody CommentUpvoteAction payload){
        return toggleHandler(payload, Rating.DOWNVOTE);
    }
}
