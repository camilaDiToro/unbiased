package ar.edu.itba.paw.webapp.controller.queryParamsValidators;

import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;

public class GetCommentsParams {

    private final NewsOrder newsOrder;
    private final ReportOrder reportOrder;
    private final User user;
    private final long newsId;

    public long getNewsId() {
        return newsId;
    }

    public GetCommentsParams(NewsOrder newsOrder, ReportOrder reportOrder, User user, long newsId) {
        this.newsOrder = newsOrder;
        this.reportOrder = reportOrder;
        this.user = user;
        this.newsId = newsId;
    }

    public NewsOrder getNewsOrder() {
        return newsOrder;
    }

    public ReportOrder getReportOrder() {
        return reportOrder;
    }

    public User getUser() {
        return user;
    }
}
