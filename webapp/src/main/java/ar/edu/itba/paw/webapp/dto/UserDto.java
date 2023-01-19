package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.Tier;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.webapp.adapter.PositivityAdapter;

import javax.ws.rs.core.UriInfo;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.net.URI;
import java.util.HashMap;

@XmlRootElement
public class UserDto {

    private String email;
    private String username;
    private long followers;
    private long following;
    private URI self;
    private URI news;
    private URI image;

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

    private String tier;

    private boolean hasImage;






    public boolean isJournalist() {
        return isJournalist;
    }

    public void setJournalist(boolean journalist) {
        isJournalist = journalist;
    }

    private boolean isJournalist;

    public static UserDto fromUser(final UriInfo uriInfo, final User user, final long followers, final long following){
        final UserDto dto = fromUser(uriInfo,user);
        dto.followers = followers;
        dto.following = following;

        return dto;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    private long id;

    public boolean isHasPositivity() {
        return hasPositivity;
    }

    public void setHasPositivity(boolean hasPositivity) {
        this.hasPositivity = hasPositivity;
    }

    private boolean hasPositivity;

    public String[] getMailOptions() {
        return mailOptions;
    }

    public void setMailOptions(String[] mailOptions) {
        this.mailOptions = mailOptions;
    }

    private String[] mailOptions = new String[]{};


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

    private URI newsStats;




    public static UserDto fromUser(final UriInfo uriInfo, final User user){
        final UserDto dto = new UserDto();
        dto.email = user.getEmail();
        dto.username = user.getUsername();
        dto.self = uriInfo.getBaseUriBuilder().path("api").path("users").path(String.valueOf(user.getId())).build();
        dto.hasImage = user.hasImage();
        if (dto.hasImage) {
            dto.image = uriInfo.getBaseUriBuilder().path("api").path("users").path(String.valueOf(user.getId())).path("image").build();
        }
        long followers = user.getFollowers().size();
        long following = user.getFollowing().size();
        dto.following = following;
        dto.followers = followers;
        dto.tier = Tier.getTier(followers).toString();
        dto.isJournalist = user.getRoles().contains(Role.ROLE_JOURNALIST);
        if (dto.isJournalist) {
            dto.newsStats = uriInfo.getBaseUriBuilder().path("api").path("users").path(String.valueOf(user.getId())).path("news-stats").build();
        }
        dto.description = user.getDescription();
        dto.hasPositivity = user.hasPositivityStats();
        if (dto.hasPositivity) {
            PositivityStats p = user.getPositivityStats();
            dto.stats.put("upvoted", Double.toString(p.getProportionUpvoted()));
            dto.stats.put("positivity", p.getPositivity().toString());
            dto.stats.put("interactions", Long.toString(p.getInteractions()));
        }
        dto.id = user.getUserId();
        // TODO if user == loggedInUser
            dto.mailOptions = user.getEmailSettings().getOptionsArray();



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
