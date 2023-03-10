package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.Tier;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.webapp.adapter.PositivityAdapter;
import ar.edu.itba.paw.webapp.controller.queryParamsValidators.GetNewsFilter;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@XmlRootElement
public class UserDto {

    private String email;
    private String username;
    private long followers;
    private long following;
    private URI self;
    private URI news;
    private URI image;
    private URI newsStats;
    private URI roles;
    private URI publishedArticles;
    private URI likedArticles;
    private URI pinnedNews;
    private String tier;
    private boolean hasImage;
    private boolean isJournalist;
    private String description;
    private long id;
    private boolean hasPositivity;
    private String[] mailOptions = new String[]{};

    public URI getRoles() {
        return roles;
    }

    public void setRoles(URI roles) {
        this.roles = roles;
    }

    public URI getLikedArticles() {
        return likedArticles;
    }

    public void setLikedArticles(URI likedArticles) {
        this.likedArticles = likedArticles;
    }

    public URI getDislikedArticles() {
        return dislikedArticles;
    }

    public void setDislikedArticles(URI dislikedArticles) {
        this.dislikedArticles = dislikedArticles;
    }

    public URI getReportedArticles() {
        return reportedArticles;
    }

    public void setReportedArticles(URI reportedArticles) {
        this.reportedArticles = reportedArticles;
    }

    public URI getSavedArticles() {
        return savedArticles;
    }

    public void setSavedArticles(URI savedArticles) {
        this.savedArticles = savedArticles;
    }

    private URI dislikedArticles;

    public URI getPublishedArticles() {
        return publishedArticles;
    }

    public void setPublishedArticles(URI publishedArticles) {
        this.publishedArticles = publishedArticles;
    }

    private URI reportedArticles;
    private URI savedArticles;

    public URI getPinnedNews() {
        return pinnedNews;
    }

    public void setPinnedNews(URI pinnedNews) {
        this.pinnedNews = pinnedNews;
    }

    public boolean isHasImage() {
        return hasImage;
    }

    public void setHasImage(boolean hasImage) {
        this.hasImage = hasImage;
    }

    public String getTier() {
        return tier;
    }

    public void setTier(String tier) {
        this.tier = tier;
    }

    public boolean isJournalist() {
        return isJournalist;
    }

    public void setJournalist(boolean journalist) {
        isJournalist = journalist;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isHasPositivity() {
        return hasPositivity;
    }

    public void setHasPositivity(boolean hasPositivity) {
        this.hasPositivity = hasPositivity;
    }

    public String[] getMailOptions() {
        return mailOptions;
    }

    public void setMailOptions(String[] mailOptions) {
        this.mailOptions = mailOptions;
    }

    public HashMap<String, String> getStats() {
        return stats;
    }

    public void setStats(HashMap<String, String> stats) {
        this.stats = stats;
    }

    @XmlJavaTypeAdapter(PositivityAdapter.class)
    private HashMap<String, String> stats = new HashMap<>();

    public URI getNewsStats() {
        return newsStats;
    }

    public void setNewsStats(URI newsStats) {
        this.newsStats = newsStats;
    }

    public static UserDto fromUser(final UriInfo uriInfo, final User user){
        final UserDto dto = new UserDto();
        dto.email = user.getEmail();
        dto.username = user.getUsername();
        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).build();
        dto.hasImage = user.hasImage();
        if (dto.hasImage) {
            dto.image = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).path("image").build();
        }
        long followers = user.getFollowers() == null ? 0 : user.getFollowers().size();
        long following = user.getFollowing() == null ? 0 : user.getFollowing().size();
        dto.following = following;
        dto.followers = followers;
        dto.tier = Tier.getTier(followers).toString();
        Collection<Role> roles = user.getRoles();
        dto.isJournalist = roles != null && roles.contains(Role.ROLE_JOURNALIST);
        if (dto.isJournalist) {
            dto.newsStats = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).path("news-stats").build();
            dto.publishedArticles = uriInfo.getBaseUriBuilder().path("news").queryParam("filter", GetNewsFilter.PUBLISHED_BY.toString()).queryParam("id",user.getId()).build();
        }
        PositivityStats stats = user.getPositivityStats();
        dto.description = user.getDescription();
        dto.hasPositivity = user.hasPositivityStats();
        if (dto.hasPositivity) {
            PositivityStats p = user.getPositivityStats();
            dto.stats.put("upvoted", Double.toString(p.getProportionUpvoted()));
            dto.stats.put("positivity", p.getPositivity().toString());
            dto.stats.put("interactions", Long.toString(p.getInteractions()));
        }
        dto.id = user.getUserId();
        dto.mailOptions = user.getEmailSettings().getOptionsArray();
        dto.roles = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).path("role").build();
        dto.likedArticles = uriInfo.getBaseUriBuilder().path("news").queryParam("filter", GetNewsFilter.LIKED_BY.toString()).queryParam("id",user.getId()).build();
        dto.dislikedArticles = uriInfo.getBaseUriBuilder().path("news").queryParam("filter", GetNewsFilter.DISLIKED_BY.toString()).queryParam("id",user.getId()).build();
        dto.reportedArticles = uriInfo.getBaseUriBuilder().path("news").queryParam("filter", GetNewsFilter.REPORTED_BY.toString()).queryParam("id",user.getId()).build();
        dto.savedArticles = uriInfo.getBaseUriBuilder().path("news").queryParam("filter", GetNewsFilter.SAVED_BY.toString()).queryParam("id",user.getId()).build();
        dto.pinnedNews = uriInfo.getBaseUriBuilder().path("news").queryParam("filter", GetNewsFilter.PINNED_BY.toString()).queryParam("id",user.getId()).build();
        return dto;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }

    public URI getNews() {
        return news;
    }

    public void setNews(URI news) {
        this.news = news;
    }

    public long getFollowers() {
        return followers;
    }

    public void setFollowers(long followers) {
        this.followers = followers;
    }

    public long getFollowing() {
        return following;
    }

    public void setFollowing(long following) {
        this.following = following;
    }

    public URI getImage() {
        return image;
    }

    public void setImage(URI image) {
        this.image = image;
    }
}
