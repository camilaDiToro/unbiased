package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportedComment;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.format.DateTimeFormatter;

public class NewsReportDetailDto {

    private String user;
    private String datetime;
    private String reason;
    private URI userLink;
    private URI articleLink;
    private long articleId;
    private long id;


    public static NewsReportDetailDto fromReportDetail(final UriInfo uriInfo, final ReportDetail reportDetail) {
        NewsReportDetailDto n = new NewsReportDetailDto();

        n.user = reportDetail.getReporter().toString();
        n.userLink = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(reportDetail.getReporter().getId())).build();
        n.reason = reportDetail.getReason().getInterCode();
        n.articleId = reportDetail.getNews().getNewsId();
        n.articleLink = uriInfo.getBaseUriBuilder().path("article").path(String.valueOf(reportDetail.getNews().getNewsId())).build();
        n.datetime = reportDetail.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME);
        n.id = reportDetail.getId();
        return n;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public long getArticleId() {
        return articleId;
    }

    public void setArticleId(long articleId) {
        this.articleId = articleId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public URI getUserLink() {
        return userLink;
    }

    public void setUserLink(URI userLink) {
        this.userLink = userLink;
    }

    public URI getArticleLink() {
        return articleLink;
    }

    public void setArticleLink(URI articleLink) {
        this.articleLink = articleLink;
    }

}
