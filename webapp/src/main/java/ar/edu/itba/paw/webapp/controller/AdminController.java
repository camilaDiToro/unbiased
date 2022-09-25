package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TextType;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.CreateNewsForm;
import ar.edu.itba.paw.webapp.form.ReportNewsForm;
import ar.edu.itba.paw.webapp.model.MAVBuilderSupplier;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

@Controller
public class AdminController {

    private final AdminService adminService;
    private final NewsService newsService;

    private final MAVBuilderSupplier mavBuilderSupplier;

    private final SecurityService securityService;



    @Autowired
    public AdminController(AdminService adminService, NewsService newsService, SecurityService ss) {
        this.adminService = adminService;
        this.newsService = newsService;
        this.securityService = ss;
        mavBuilderSupplier = (view, title, textType) -> new MyModelAndView.Builder(view, title, textType, securityService);

    }

    @RequestMapping(value = "/admin/reported_news/{newsOrder:TOP|NEW}", method = RequestMethod.GET)
    public ModelAndView reportedNews(@RequestParam(name = "page", defaultValue = "1") int page,
                                     @PathVariable("newsOrder") String newsOrder) {
//        adminService.reportNews(24,1, ReportReason.INAPP);

        Page<ReportedNews> reportedNewsPage = adminService.getReportedNews(page, NewsOrder.NEW);
        return mavBuilderSupplier.supply("moderation-panel", "Moderation Panel", TextType.LITERAL)
                .withObject("newsPage", reportedNewsPage)
                .withObject("orders", NewsOrder.values())
                .withObject("orderBy", NewsOrder.valueOf(newsOrder)).build();
    }

    @RequestMapping("/admin/reported_news")
    public ModelAndView reportedNewsRedirect() {

        return new ModelAndView("redirect:/admin/reported_news/TOP");
    }


    @RequestMapping("/admin/reported_news_detail/{newsId:[0-9]+}")
    public ModelAndView reportedNewsDetail(@PathVariable("newsId") long newsId,
                                           @RequestParam(name = "page", defaultValue = "1") int page) {
        Page<ReportDetail> reportedNewsPage = adminService.getReportedNewsDetail(page,newsId);
        return mavBuilderSupplier.supply("moderation-panel-detail", "Moderation View", TextType.LITERAL)
                .withObject("reportedNewsPage", reportedNewsPage)
                .withObject("locale", LocaleContextHolder.getLocale())
                .withObject("newsId", newsId)
                .build();

    }

    @RequestMapping(value = "/admin/report_news/{newsId:[0-9]+}", method = RequestMethod.POST)
    public ModelAndView reportNews(@PathVariable("newsId") long newsId,@Valid @ModelAttribute("reportNewsForm") final ReportNewsForm reportNewsFrom,
                                   final BindingResult errors) {
//        if (errors.hasErrors()) {
//            return
//        }
        adminService.reportNews(newsId, ReportReason.valueOf(reportNewsFrom.getReason()));
        return new ModelAndView("redirect:/news/" + newsId);
    }

    @RequestMapping(value = "/admin/reported_news/{newsId:[0-9]+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNews(@PathVariable("newsId") long newsId) {
        adminService.deleteNews(newsId);
        return new ModelAndView("redirect:/admin/reported_news");
    }



    @RequestMapping(value = "/admin/add_admin/{userId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView addAdmin(@PathVariable("userId") long userId) {
        adminService.makeUserAdmin(userId);
        return new ModelAndView("redirect:/admin/reported_news");
    }
}