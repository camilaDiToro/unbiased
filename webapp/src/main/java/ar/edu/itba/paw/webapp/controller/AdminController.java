package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@Controller
public class AdminController {

    private final AdminService adminService;
    private final NewsService newsService;

    @Autowired
    public AdminController(AdminService adminService, NewsService newsService) {
        this.adminService = adminService;
        this.newsService = newsService;
    }

    @RequestMapping("/admin/reported_news")
    public ModelAndView reportedNews(@RequestParam(name = "page", defaultValue = "1") int page) {
        Page<ReportedNews> reportedNewsPage = adminService.getReportedNews(page);
        return new ModelAndView("admin_panel");
    }


    @RequestMapping("/admin/reported_news_detail/{newsId:[0-9]+}")
    public ModelAndView reportedNewsDetail(@PathVariable("newsId") long newsId,
                                           @RequestParam(name = "page", defaultValue = "1") int page) {
        Page<ReportDetail> reportedNewsPage = adminService.getReportedNewsDetail(page,newsId);
        return new ModelAndView("admin_panel");
    }

    @RequestMapping(value = "/admin/reported_news/{newsId:[0-9]+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNews(@PathVariable("newsId") long newsId) {
        newsService.deleteNews(newsId);
        return new ModelAndView("redirect:/admin/reported_news");
    }
}