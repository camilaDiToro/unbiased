package ar.edu.itba.paw.model.user;

import javax.persistence.*;
import java.util.Locale;

@Entity
@Table(name="email_settings")
public class EmailSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "email_settings_id_seq")
    @SequenceGenerator(name="email_settings_id_seq", sequenceName = "email_settings_id_seq", allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name= "follow", nullable = false)
    private boolean follow;

    @Column(name= "comment", nullable = false)
    private boolean comment;

    @Column(name= "following_published", nullable = false)
    private boolean followingPublished;

    @Column(name= "positivity_change", nullable = false)
    private boolean positivityChange;

    private Locale locale;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    EmailSettings() {
        //Just for hibernate
    }

    public EmailSettings(boolean follow, boolean comment, boolean followingPublished, boolean positivityChange, Locale locale, final User user) {
        this.follow = follow;
        this.comment = comment;
        this.followingPublished = followingPublished;
        this.positivityChange = positivityChange;
        this.user = user;
        this.locale=locale;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isFollow() {
        return follow;
    }

    public void setFollow(boolean follow) {
        this.follow = follow;
    }

    public boolean isComment() {
        return comment;
    }

    public void setComment(boolean comment) {
        this.comment = comment;
    }

    public User getUser() {
        return user;
    }

    public void setUser(final User user) {
        this.user = user;
    }

    public boolean isFollowingPublished() {
        return followingPublished;
    }

    public void setFollowingPublished(boolean followingPublished) {
        this.followingPublished = followingPublished;
    }

    public boolean isPositivityChange() {
        return positivityChange;
    }

    public void setPositivityChange(boolean positivityChange) {
        this.positivityChange = positivityChange;
    }

    public Locale getLocale() {
        return locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}
