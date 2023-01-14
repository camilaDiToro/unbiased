package ar.edu.itba.paw.webapp.auth;

public class Credentials {

    private final String email;
    private final String password;


    public Credentials(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
