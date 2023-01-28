package ar.edu.itba.paw.webapp.auth.email;

public class EmailCredentials {
    private final String email;
    private final String token;


    public EmailCredentials(String email, String token) {
        this.email = email;
        this.token = token;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
