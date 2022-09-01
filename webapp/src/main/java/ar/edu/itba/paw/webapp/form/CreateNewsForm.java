package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class CreateNewsForm {

    @Size(min=1, max=100)
    @Pattern(regexp="[a-zA-Z][a-zA-Z0-9]*")
    private String title;
    @Pattern(regexp="[a-zA-Z][a-zA-Z0-9]*")
    private String subtitle;
    @Size(min=1)
    @Pattern(regexp="[a-zA-Z][a-zA-Z0-9]*")
    private String body;
    @Size(min=6, max=100)
    @Pattern(regexp="[a-zA-Z][a-zA-Z0-9]*")
    private String creatorEmail;
    private CommonsMultipartFile image;


    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getBody() {
        return body;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(CommonsMultipartFile image) {
        this.image = image;
    }
}
