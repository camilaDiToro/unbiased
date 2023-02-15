package ar.edu.itba.paw.webapp.form;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;

public class CommentNewsForm {

    @NotNull(message = "commentnewsform.comment.notnull")
    @NotBlank(message = "commentnewsform.comment.notblank")
    @Length(max=400, message = "commentnewsform.comment.length")
    private String comment;

    public long getNewsId() {
        return newsId;
    }

    public void setNewsId(long newsId) {
        this.newsId = newsId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    private long newsId;
}
