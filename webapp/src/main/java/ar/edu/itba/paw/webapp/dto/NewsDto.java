package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.webapp.adapter.CreatorAdapter;
import ar.edu.itba.paw.webapp.adapter.PositivityAdapter;
import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;

public class NewsDto {
    private String title;
    private String subtitle;
    private String body;
    private int readTime;
    private Boolean saved;
    private boolean hasImage;

    @XmlJavaTypeAdapter(CreatorAdapter.class)
    private HashMap<String, String> creator = new HashMap<>();
    private long id;
    private Boolean pinned;
    private long upvotes;

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }

    private URI image;

    public static NewsDto fromNews(final UriInfo uriInfo, final News news) {
        NewsDto n = new NewsDto();
        n.id = news.getNewsId();
        PositivityStats p = news.getPositivityStats();
        n.upvotes = p.getNetUpvotes();
        n.hasImage = n.isHasImage();
        if (n.hasImage) {
            n.image = uriInfo.getBaseUriBuilder().path("api").path("news").path(String.valueOf(news.getNewsId())).path("image").build();
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
            n.creator.put("image",uriInfo.getBaseUriBuilder().path("api").path("users").path(String.valueOf(news.getCreatorId())).path("image").build().toString());
        }
        n.creator.put("self",uriInfo.getBaseUriBuilder().path("api").path("users").path(String.valueOf(creator.getId())).build().toString());
        n.creator.put("nameOrEmail", creator.toString());
        n.creator.put("tier", creator.getTier().toString());
        n.creator.put("id", Long.toString(news.getCreatorId()));
        n.datetime = news.getCreationDate().format(DateTimeFormatter.ISO_DATE_TIME);
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

    @XmlJavaTypeAdapter(PositivityAdapter.class)
    private HashMap<String, String> stats = new HashMap<>();
}
