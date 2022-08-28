package ar.edu.itba.paw.webapp.form;

public class CreateNewsForm {

    private String title;
    private String subtitle;
    private String body;
    private String creatorEmail;


    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getBody() {
        return body;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }
}
