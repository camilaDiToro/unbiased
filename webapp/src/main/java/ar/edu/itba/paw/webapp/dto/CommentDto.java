package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.user.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.sql.Timestamp;

public class CommentDto {

    private long id;
    private URI self;
    private String commentOwner;
    private URI userUri;
    private long reportCount;
    private URI reportsUri;
    private boolean deleted;
    private Timestamp commentedDate;

    public static CommentDto fromComment(final UriInfo uriInfo, final Comment comment, final long newsId){
        final CommentDto dto = new CommentDto();
        dto.id = comment.getId();
        dto.self = uriInfo.getBaseUriBuilder().path("api/news").path(String.valueOf(newsId)).path("comments").path(String.valueOf(comment.getId())).build();
        User commentOwner = comment.getUser();
        dto.commentOwner = commentOwner.toString();
        dto.userUri = uriInfo.getBaseUriBuilder().path("api/users").path(String.valueOf(commentOwner.getId())).build();
        dto.reportCount = comment.getReports() == null ? 0 : comment.getReports().size();
        //TODO
        dto.reportsUri = uriInfo.getBaseUriBuilder().path("TODO").build();
        dto.deleted = comment.getDeleted();
        dto.commentedDate = comment.getCommentedDate();
        return dto;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public String getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(String commentOwner) {
        this.commentOwner = commentOwner;
    }

    public URI getUserUri() {
        return userUri;
    }

    public void setUserUri(URI userUri) {
        this.userUri = userUri;
    }

    public long getReportCount() {
        return reportCount;
    }

    public void setReportCount(long reportCount) {
        this.reportCount = reportCount;
    }

    public URI getReportsUri() {
        return reportsUri;
    }

    public void setReportsUri(URI reportsUri) {
        this.reportsUri = reportsUri;
    }

    public Timestamp getCommentedDate() {
        return commentedDate;
    }

    public void setCommentedDate(Timestamp commentedDate) {
        this.commentedDate = commentedDate;
    }
}
