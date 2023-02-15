package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportedComment;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.format.DateTimeFormatter;


public class CommentReportDetailsDto {

    private String user;
    private String datetime;
    private String reason;
    private URI userLink;
    private URI commentLink;
    private long commentId;
    private long id;

    public static CommentReportDetailsDto fromReportedComment(final UriInfo uriInfo, final ReportedComment reportDetail) {
        CommentReportDetailsDto n = new CommentReportDetailsDto();

        n.user = reportDetail.getReporter().toString();
        n.reason = reportDetail.getReason().getInterCode();
        n.commentId = reportDetail.getComment().getId();
        n.datetime = reportDetail.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME);
        n.id = reportDetail.getId();
        n.userLink = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(reportDetail.getReporter().getId())).build();
        n.commentLink = uriInfo.getBaseUriBuilder().path("comments").path(String.valueOf(reportDetail.getComment().getId())).build();
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

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
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

    public URI getCommentLink() {
        return commentLink;
    }

    public void setCommentLink(URI commentLink) {
        this.commentLink = commentLink;
    }

}