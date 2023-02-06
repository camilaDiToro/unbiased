package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.webapp.adapter.CreatorAdapter;
import ar.edu.itba.paw.webapp.adapter.PositivityAdapter;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class ReportDetailDto {
    private String user;

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    private String reason;


    private long id;



    public static ReportDetailDto fromReportDetail(final UriInfo uriInfo, final ReportDetail reportDetail) {
        ReportDetailDto n = new ReportDetailDto();

        n.user = reportDetail.getReporter().toString();
        n.reason = reportDetail.getReason().getInterCode();

        n.datetime = reportDetail.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME);
        return n;
    }

    public static ReportDetailDto fromReportedComment(final UriInfo uriInfo, final ReportedComment reportDetail) {
        ReportDetailDto n = new ReportDetailDto();

        n.user = reportDetail.getReporter().toString();
        n.reason = reportDetail.getReason().getInterCode();

        n.datetime = reportDetail.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME);
        return n;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }












    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }







    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }



    private String datetime;


}
