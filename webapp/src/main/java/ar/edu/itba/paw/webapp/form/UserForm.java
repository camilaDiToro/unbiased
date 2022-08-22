package ar.edu.itba.paw.webapp.form;

import org.springframework.web.multipart.commons.CommonsMultipartFile;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserForm {

    @Size(min=6, max=100)
    @Pattern(regexp="[a-zA-Z][a-zA-Z0-9]*")
    private String username;

    @Size(min=8)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
