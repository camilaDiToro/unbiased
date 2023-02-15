package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.constraints.FileSize;
import ar.edu.itba.paw.webapp.constraints.NotExistingMailOption;
import ar.edu.itba.paw.webapp.constraints.NotExistingUsername;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

public class UserProfileForm {

    @NotExistingUsername(message = "userprofileform.username.repeated")
    @Length(max=50, message = "userprofileform.username.length")
    private String username;

    private String description;

    @NotExistingMailOption(message = "userprofileform.mailoptions.notfound")
    private String[] mailOptions;


    public String[] getMailOptions() {
        return mailOptions;
    }

    public void setMailOptions(String[] mailOptions) {
        this.mailOptions = mailOptions;
    }

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