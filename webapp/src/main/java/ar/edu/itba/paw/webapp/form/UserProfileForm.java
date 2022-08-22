package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

public class UserProfileForm {

    private CommonsMultipartFile image;

    public CommonsMultipartFile getImage() {
        return image;
    }

    public void setImage(CommonsMultipartFile image) {
        this.image = image;
    }
}