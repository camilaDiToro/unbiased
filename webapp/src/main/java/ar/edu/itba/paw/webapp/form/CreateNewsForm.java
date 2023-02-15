package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.constraints.NotExistingNewsCategory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class CreateNewsForm {

    @NotNull
    @NotBlank
    @Length(max = 200)
    private String title;
    @NotNull
    @NotBlank
    @Length(max = 400)
    private String subtitle;
    @NotNull
    @NotBlank
    @Length(max = 10000000)
    private String body;
    @NotExistingNewsCategory
    private String[] categories;


    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getBody() {
        return body;
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

}
