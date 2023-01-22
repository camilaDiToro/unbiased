package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.webapp.adapter.CreatorAdapter;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class CommentDto {

    private long id;
    private URI self;
    private String commentOwner;
    private URI userUri;
    private long reportCount;

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    private String body;

    public long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }


    private long upvotes;

    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    private long newsId;

    private URI reportsUri;
    private boolean deleted;
    private String datetime;

    public HashMap<String, String> getCreator() {
        return creator;
    }

    public void setCreator(HashMap<String, String> creator) {
        this.creator = creator;
    }

    @XmlJavaTypeAdapter(CreatorAdapter.class)
    private HashMap<String, String> creator = new HashMap<>();

    public static CommentDto fromComment(final UriInfo uriInfo, final Comment comment){
        final CommentDto dto = new CommentDto();
        dto.id = comment.getId();
        dto.self = uriInfo.getBaseUriBuilder().path("api").path("comments").path(String.valueOf(comment.getId())).build();
        User creator = comment.getUser();
        dto.creator.put("hasImage", Boolean.toString(creator.hasImage()));
        if (creator.hasImage()) {
            dto.creator.put("image",uriInfo.getBaseUriBuilder().path("api").path("users").path(String.valueOf(creator.getId())).path("image").build().toString());
        }
        dto.creator.put("self",uriInfo.getBaseUriBuilder().path("api").path("users").path(String.valueOf(creator.getId())).build().toString());
        dto.creator.put("nameOrEmail", creator.toString());
        dto.creator.put("tier", creator.getTier().toString());
        dto.creator.put("id", Long.toString(creator.getUserId()));
        dto.reportCount = comment.getReports() == null ? 0 : comment.getReports().size();
        dto.upvotes = comment.getPositivityStats() == null ? 0: comment.getPositivityStats().getNetUpvotes();
        dto.reportsUri = uriInfo.getBaseUriBuilder().path("TODO").build();
        dto.deleted = comment.getDeleted();
        dto.newsId = comment.getNews().getNewsId();
        if (!dto.deleted) {
            dto.body = comment.getComment();
        }
        dto.datetime = comment.getCommentedDate().toLocalDateTime().format(DateTimeFormatter.ISO_DATE_TIME);
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
