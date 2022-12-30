package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.user.User;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class UserDto {

    private String email;
    private String username;
    private URI self;
    private URI news;

    public static UserDto fromUser(final UriInfo uriInfo, final User user){
        final UserDto dto = new UserDto();

        dto.email = user.getEmail();
        dto.username = user.getUsername();
        dto.self = uriInfo.getAbsolutePathBuilder().replacePath("users").path(String.valueOf(user.getId())).build();

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
}
