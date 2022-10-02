package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.constraints.ExistingEmail;
import ar.edu.itba.paw.webapp.constraints.NotExistingAdmin;
import org.hibernate.validator.constraints.NotBlank;

public class CreateAdminForm {

    @ExistingEmail
    @NotBlank
    @NotExistingAdmin
    String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
