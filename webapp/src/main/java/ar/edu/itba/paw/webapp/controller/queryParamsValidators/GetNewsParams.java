package ar.edu.itba.paw.webapp.controller.queryParamsValidators;

import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.User;

public class GetNewsParams {

    private final Category categoryObj;
    private final NewsOrder orderObj;
    private final TimeConstraint timeObj;

    private final ReportOrder reportOrder;
    private final  String search;
    private final User user;

    public GetNewsParams(Category categoryObj, NewsOrder orderObj, ReportOrder reportOrder, TimeConstraint timeObj, String search, User user) {
        this.categoryObj = categoryObj;
        this.orderObj = orderObj;
        this.reportOrder = reportOrder;
        this.timeObj = timeObj;
        this.search = search;
        this.user = user;
    }

    public Category getCategoryObj() {
        return categoryObj;
    }

    public NewsOrder getOrderObj() {
        return orderObj;
    }

    public TimeConstraint getTimeObj() {
        return timeObj;
    }

    public String getSearch() {
        return search;
    }

    public User getUser() {
        return user;
    }

    public ReportOrder getReportOrder() {
        return reportOrder;
    }
}
