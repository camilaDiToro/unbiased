package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

public class UserProfileForm {

    private MultipartFile image;
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