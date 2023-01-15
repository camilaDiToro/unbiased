package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.Tier;
import ar.edu.itba.paw.model.user.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

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


    public String getPositivity() {
        return positivity;
    }

    public void setPositivity(String positivity) {
        this.positivity = positivity;
    }

    private String positivity;

    private int interactions;

    public int getInteractions() {
        return interactions;
    }

    public void setInteractions(int interactions) {
        this.interactions = interactions;
    }

    public double getUpvotedProportion() {
        return upvotedProportion;
    }

    public void setUpvotedProportion(double upvotedProportion) {
        this.upvotedProportion = upvotedProportion;
    }

    private double upvotedProportion;

    public boolean isJournalist() {
        return isJournalist;
    }

    public void setJournalist(boolean journalist) {
        isJournalist = journalist;
    }

    private boolean isJournalist;

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
        PositivityStats stats = user.getPositivityStats();
        dto.isJournalist = user.getRoles() != null && user.getRoles().contains(Role.ROLE_JOURNALIST);
        dto.description = user.getDescription();
        dto.hasPositivity = user.hasPositivityStats();
        if (dto.hasPositivity) {
            dto.positivity = stats.getPositivity().toString().toLowerCase();
            dto.interactions = stats.getInteractions();
            dto.upvotedProportion = stats.getProportionUpvoted();
        }
        dto.id = user.getUserId();
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
