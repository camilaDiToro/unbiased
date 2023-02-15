package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.constraints.NotExistingNewsCategory;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class CreateNewsForm {

    @NotNull(message = "createnewsform.title.notnull")
    @NotBlank(message = "createnewsform.title.notblank")
    @Length(max = 200, message = "createnewsform.title.length")
    private String title;

    @NotNull(message = "createnewsform.subtitle.notnull")
    @NotBlank(message = "createnewsform.subtitle.notblank")
    @Length(max = 400, message = "createnewsform.subtitle.length")
    private String subtitle;

    @NotNull(message = "createnewsform.body.notnull")
    @NotBlank(message = "createnewsform.body.notblank")
    @Length(max = 10000000, message = "createnewsform.body.length")
    private String body;

    @NotExistingNewsCategory(message = "createnewsform.categories.notfound")
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
