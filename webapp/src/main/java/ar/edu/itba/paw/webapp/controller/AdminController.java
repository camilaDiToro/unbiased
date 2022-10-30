package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.TextType;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class AdminController extends BaseController{

    private final AdminService adminService;
    private final NewsService newsService;
    private final UserService userService;

    @Autowired
    public AdminController(AdminService adminService, NewsService newsService, UserService userService, SecurityService ss) {
        super(ss);
        this.adminService = adminService;
        this.newsService = newsService;
        this.userService = userService;
    }



    @RequestMapping(value = "/admin/reported_news/{newsOrder}", method = RequestMethod.GET)
    public ModelAndView reportedNews(@RequestParam(name = "page", defaultValue = "1") int page,
                                     @PathVariable("newsOrder") String newsOrder) {


        Page<News> reportedNewsPage = adminService.getReportedNews(page, newsOrder);
        return new MyModelAndView.Builder("moderation_panel_reported_news", "pageTitle.moderationPanel", TextType.INTERCODE)
                .withObject("newsPage", reportedNewsPage)
                .withObject("orders", ReportOrder.values())
                .withObject("item", "news")
                .withObject("isOwner", securityService.getCurrentUser().get().getRoles().contains(Role.ROLE_OWNER))
                .withObject("orderBy", ReportOrder.valueOf(newsOrder)).build();
    }

    @RequestMapping(value = "/admin/reported_comments/{commentOrder}", method = RequestMethod.GET)
    public ModelAndView reportedComments(@RequestParam(name = "page", defaultValue = "1") int page,
                                     @PathVariable("commentOrder") String commentOrder) {


        Page<Comment> reportedCommentsPage = adminService.getReportedComments(page, commentOrder);
        return new MyModelAndView.Builder("moderation_panel_reported_comments", "pageTitle.moderationPanel", TextType.INTERCODE)
                .withObject("commentsPage", reportedCommentsPage)
                .withObject("orders", ReportOrder.values())
                .withObject("item", "comments")
                .withObject("isOwner", securityService.getCurrentUser().get().getRoles().contains(Role.ROLE_OWNER))
                .withObject("orderBy", ReportOrder.valueOf(commentOrder)).build();
    }

    @RequestMapping("/admin/reported_news")
    public ModelAndView reportedNewsRedirect() {
        return new ModelAndView("redirect:/admin/reported_news/" + ReportOrder.values()[0].name());
    }

    @RequestMapping("/admin/reported_comments")
    public ModelAndView reportedCommentsRedirect() {
        return new ModelAndView("redirect:/admin/reported_comments/" + ReportOrder.values()[0].name());
    }


    @RequestMapping("/admin/reported_news_detail/{newsId:[0-9]+}")
    public ModelAndView reportedNewsDetail(@PathVariable("newsId") long newsId,
                                           @RequestParam(name = "page", defaultValue = "1") int page) {
        Page<ReportDetail> reportedNewsPage = adminService.getReportedNewsDetail(page,newsService.getById(newsId).orElseThrow(NewsNotFoundException::new));
        return new MyModelAndView.Builder("moderation_panel_reported_news_detail", "pageTitle.moderationPanel", TextType.INTERCODE)
                .withObject("reportedNewsPage", reportedNewsPage)
                .withObject("locale", LocaleContextHolder.getLocale())
                .withObject("newsId", newsId)
                .withObject("isOwner", securityService.getCurrentUser().get().getRoles().contains(Role.ROLE_OWNER))
                .withObject("item", "news")
                .build();
    }

    @RequestMapping("/admin/reported_comment_detail/{commentId:[0-9]+}")
    public ModelAndView reportedCommentDetail(@PathVariable("commentId") long commentId,
                                           @RequestParam(name = "page", defaultValue = "1") int page) {
        Page<ReportedComment> reportedCommentPage = adminService.getReportedCommentDetail(page,newsService.getCommentById(commentId).orElseThrow(CommentNotFoundException::new));
        return new MyModelAndView.Builder("moderation_panel_reported_comment_detail", "pageTitle.moderationPanel", TextType.INTERCODE)
                .withObject("reportedCommentPage", reportedCommentPage)
                .withObject("locale", LocaleContextHolder.getLocale())
                .withObject("commentId", commentId)
                .withObject("isOwner", securityService.getCurrentUser().get().getRoles().contains(Role.ROLE_OWNER))
                .withObject("item", "comments")
                .build();
    }

    @RequestMapping(value = "/admin/reported_news/{newsId:[0-9]+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNews(@PathVariable("newsId") long newsId) {
        adminService.deleteNews(newsService.getById(newsId).orElseThrow(NewsNotFoundException::new));
        return new ModelAndView("redirect:/admin/reported_news");
    }

    @RequestMapping(value = "/admin/reported_comments/{commentId:[0-9]+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteComment(@PathVariable("commentId") long commentId) {
        adminService.deleteComment(newsService.getCommentById(commentId).orElseThrow(CommentNotFoundException::new));
        return new ModelAndView("redirect:/admin/reported_comments");
    }


}