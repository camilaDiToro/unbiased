package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.MultipartFile;

public class UserProfileForm {

    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }
}