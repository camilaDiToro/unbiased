package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.constraints.FileSize;
import ar.edu.itba.paw.webapp.constraints.NotExistingMailOption;
import ar.edu.itba.paw.webapp.constraints.NotExistingUsername;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public class UserProfileForm {

//    @FileSize
//    private MultipartFile image;

    @NotExistingUsername
    @Length(max=50)
    private String username;

    private String description;

    @NotExistingMailOption
    private String[] mailOptions;


    public String[] getMailOptions() {
        return mailOptions;
    }

    public void setMailOptions(String[] mailOptions) {
        this.mailOptions = mailOptions;
    }

//    public MultipartFile getImage() {
//        return image;
//    }
//
//    public void setImage(MultipartFile image) {
//        this.image = image;
//    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}