package ar.edu.itba.paw.webapp.dto;

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

    public static UserDto fromUser(final UriInfo uriInfo, final User user, final long followers, final long following){
        final UserDto dto = fromUser(uriInfo,user);
        dto.followers = followers;
        dto.following = following;

        return dto;
    }

    public static UserDto fromUser(final UriInfo uriInfo, final User user){
        final UserDto dto = new UserDto();

        dto.email = user.getEmail();
        dto.username = user.getUsername();
        dto.self = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).build();
        dto.image = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(user.getId())).path("image").build();
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
