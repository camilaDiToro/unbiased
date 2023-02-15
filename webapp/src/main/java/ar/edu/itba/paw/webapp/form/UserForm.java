package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.constraints.NotExistingEmail;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;

public class UserForm {

    @NotExistingEmail(message = "userform.email.repeated")
    @Email(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "userform.email.invalid")
    // source: https://www.w3resource.com/javascript/form/email-validation.php
    private String email;

    @NotBlank(message = "userform.pass.notblank")
    private String password;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
