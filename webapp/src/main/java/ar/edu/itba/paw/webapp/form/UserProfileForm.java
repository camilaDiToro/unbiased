package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.constraints.NotExistingUsername;
import org.springframework.web.multipart.MultipartFile;

public class UserProfileForm {

    private MultipartFile image;

    @NotExistingUsername
    private String username;


    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}