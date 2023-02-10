package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.webapp.adapter.CreatorAdapter;
import ar.edu.itba.paw.webapp.adapter.PositivityAdapter;
import ar.edu.itba.paw.webapp.controller.queryParamsValidators.GetCommentsFilter;
import ar.edu.itba.paw.webapp.controller.queryParamsValidators.GetNewsFilter;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class NewsDto {

    private String title;
    private String subtitle;
    private String body;
    private int readTime;
    private Boolean saved;
    private boolean hasImage;
    private URI self;
    private URI articleReports;
    private URI userLink;
    private URI userImage;
    private URI upvotesLink;
    private URI downvotesLink;
    private URI bookmarks;
    private URI comments;
    private String[] categories;
    private long id;
    private Boolean pinned;
    private long upvotes;

    @XmlJavaTypeAdapter(PositivityAdapter.class)
    private HashMap<String, String> stats = new HashMap<>();

    @XmlJavaTypeAdapter(CreatorAdapter.class)
    private HashMap<String, String> creator = new HashMap<>();

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getArticleReports() {
        return articleReports;
    }

    public void setArticleReports(URI articleReports) {
        this.articleReports = articleReports;
    }

    public URI getUserLink() {
        return userLink;
    }

    public void setUserLink(URI userLink) {
        this.userLink = userLink;
    }

    public URI getUserImage() {
        return userImage;
    }

    public void setUserImage(URI userImage) {
        this.userImage = userImage;
    }

    public URI getLikes() {
        return upvotesLink;
    }

    public void setLikes(URI upvotesLink) {
        this.upvotesLink = upvotesLink;
    }

    public URI getDislikes() {
        return downvotesLink;
    }

    public void setDislikes(URI downvotesLink) {
        this.downvotesLink = downvotesLink;
    }

    public URI getBookmarks() {
        return bookmarks;
    }

    public void setBookmarks(URI bookmarks) {
        this.bookmarks = bookmarks;
    }

    public URI getComments() {
        return comments;
    }

    public void setComments(URI comments) {
        this.comments = comments;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }

    private URI image;

    public long getReportCount() {
        return reportCount;
    }

    public void setReportCount(long reportCount) {
        this.reportCount = reportCount;
    }

    private long reportCount;

    public static NewsDto fromNews(final UriInfo uriInfo, final News news) {
        NewsDto n = new NewsDto();
        n.id = news.getNewsId();
        PositivityStats p = news.getPositivityStats();
        n.upvotes = p.getNetUpvotes();
        n.hasImage = news.hasImage();
        List<ReportDetail> reportedNewsList = news.getReports();
        n.reportCount = reportedNewsList == null ? 0 : reportedNewsList.size();
        n.categories = news.getCategories().stream().map(Category::getInterCode).collect(Collectors.toList()).toArray(new String[]{});
        if (n.hasImage) {
            n.image = uriInfo.getBaseUriBuilder().path("news").path(String.valueOf(news.getNewsId())).path("image").build();
        }
        n.title = news.getTitle();
        n.subtitle = news.getSubtitle();
        n.body = news.getBody();
        n.stats.put("upvoted", Double.toString(p.getProportionUpvoted()));
        n.stats.put("positivity", p.getPositivity().toString());
        n.stats.put("interactions", Long.toString(p.getInteractions()));

        User creator = news.getCreator();
        n.creator.put("hasImage", Boolean.toString(creator.hasImage()));
        if (creator.hasImage()) {
            n.creator.put("image",uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(news.getCreatorId())).path("image").build().toString());
        }
        n.creator.put("self",uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(creator.getId())).build().toString());
        n.creator.put("nameOrEmail", creator.toString());
        n.creator.put("tier", creator.getTier().toString());
        n.creator.put("id", Long.toString(news.getCreatorId()));
        n.datetime = news.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME);

        n.self = uriInfo.getBaseUriBuilder().path("article").path(String.valueOf(news.getNewsId())).build();
        n.articleReports = uriInfo.getBaseUriBuilder().path("article").path(String.valueOf(news.getNewsId())).path("reports").build();

        User user = news.getCreator();
        n.userLink = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).build();
        if(user.hasImage()){
            n.userImage = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).path("image").build();
        }
        n.upvotesLink = uriInfo.getBaseUriBuilder().path("article").path(String.valueOf(news.getNewsId())).path("likes").build();
        n.downvotesLink = uriInfo.getBaseUriBuilder().path("article").path(String.valueOf(news.getNewsId())).path("dislikes").build();
        n.bookmarks = uriInfo.getBaseUriBuilder().path("article").path(String.valueOf(news.getNewsId())).path("bookmarks").build();;
        n.comments  = uriInfo.getBaseUriBuilder().path("comments").queryParam("filter", GetCommentsFilter.NEWS_COMMENTS.toString()).queryParam("id",news.getNewsId()).build();
        return n;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getReadTime() {
        return readTime;
    }

    public void setReadTime(int readTime) {
        this.readTime = readTime;
    }

    public Boolean getSaved() {
        return saved;
    }

    public void setSaved(Boolean saved) {
        this.saved = saved;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public HashMap<String, String> getCreator() {
        return creator;
    }

    public void setCreator(HashMap<String, String> creator) {
        this.creator = creator;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Boolean getPinned() {
        return pinned;
    }

    public void setPinned(Boolean pinned) {
        this.pinned = pinned;
    }

    public long getUpvotes() {
        return upvotes;
    }

    public void setUpvotes(long upvotes) {
        this.upvotes = upvotes;
    }

    public Long getRating() {
        return rating;
    }

    public void setRating(Long rating) {
        this.rating = rating;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public HashMap<String, String> getStats() {
        return stats;
    }

    public void setStats(HashMap<String, String> stats) {
        this.stats = stats;
    }

    private Long rating;
    private String datetime;
}
