package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.constraints.ExistingEmail;
import ar.edu.itba.paw.webapp.constraints.NotExistingEmail;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class ResendVerificationEmail {

    @ExistingEmail
    @Email(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$")
    // source: https://www.w3resource.com/javascript/form/email-validation.php
    private String email;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
