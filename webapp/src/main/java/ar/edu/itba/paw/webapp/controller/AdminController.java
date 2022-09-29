package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TextType;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.CreateAdminForm;
import ar.edu.itba.paw.webapp.model.MAVBuilderSupplier;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AdminController {

    private final AdminService adminService;
    private final NewsService newsService;
    private final UserService userService;
    private final MAVBuilderSupplier mavBuilderSupplier;
    private final SecurityService securityService;

    @Autowired
    public AdminController(AdminService adminService, NewsService newsService, UserService userService, SecurityService ss) {
        this.adminService = adminService;
        this.newsService = newsService;
        this.userService = userService;
        this.securityService = ss;
        mavBuilderSupplier = (view, title, textType) -> new MyModelAndView.Builder(view, title, textType, securityService);
    }

    @RequestMapping(value = "/admin/add_admin", method = RequestMethod.GET)
    public ModelAndView addAdmin(@Valid @ModelAttribute("createAdminForm") CreateAdminForm form, final BindingResult errors) {
        if (errors.hasErrors())
            return addAdminPanel(form);

        adminService.makeUserAdmin(userService.findByEmail(form.getEmail()).get());
        return mavBuilderSupplier.supply("moderation_panel_add_admin", "Moderation Panel", TextType.LITERAL)
                .withObject("addedAdmin", true)
                .build();
    }

    @RequestMapping(value = "/admin/reported_news/{newsOrder:TOP|NEW}", method = RequestMethod.GET)
    public ModelAndView reportedNews(@RequestParam(name = "page", defaultValue = "1") int page,
                                     @PathVariable("newsOrder") String newsOrder) {

        Page<ReportedNews> reportedNewsPage = adminService.getReportedNews(page, NewsOrder.NEW);
        return mavBuilderSupplier.supply("moderation_panel", "Moderation Panel", TextType.LITERAL)
                .withObject("newsPage", reportedNewsPage)
                .withObject("orders", NewsOrder.values())
                .withObject("orderBy", NewsOrder.valueOf(newsOrder)).build();
    }

    @RequestMapping(value = "/admin/add_admin_page", method = RequestMethod.GET)
    public ModelAndView addAdminPanel(@ModelAttribute("createAdminForm") CreateAdminForm form) {

        return mavBuilderSupplier.supply("moderation_panel_add_admin", "Moderation Panel", TextType.LITERAL)
                .build();
    }

    @RequestMapping("/admin/reported_news")
    public ModelAndView reportedNewsRedirect() {
        return new ModelAndView("redirect:/admin/reported_news/TOP");
    }


    @RequestMapping("/admin/reported_news_detail/{newsId:[0-9]+}")
    public ModelAndView reportedNewsDetail(@PathVariable("newsId") long newsId,
                                           @RequestParam(name = "page", defaultValue = "1") int page) {
        Page<ReportDetail> reportedNewsPage = adminService.getReportedNewsDetail(page,newsService.getSimpleNewsById(newsId).orElseThrow(NewsNotFoundException::new));
        return mavBuilderSupplier.supply("moderation_panel_detail", "Moderation View", TextType.LITERAL)
                .withObject("reportedNewsPage", reportedNewsPage)
                .withObject("locale", LocaleContextHolder.getLocale())
                .withObject("newsId", newsId)
                .build();
    }

    @RequestMapping(value = "/admin/reported_news/{newsId:[0-9]+}/delete", method = RequestMethod.POST)
    public ModelAndView deleteNews(@PathVariable("newsId") long newsId) {
        adminService.deleteNews(newsService.getSimpleNewsById(newsId).orElseThrow(NewsNotFoundException::new));
        return new ModelAndView("redirect:/admin/reported_news");
    }


}