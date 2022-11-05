package ar.edu.itba.paw.model.user;

import javax.persistence.*;

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
    private boolean following_published;

    @Column(name= "positivity_change", nullable = false)
    private boolean positivity_change;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    EmailSettings() {
        //Just for hibernate
    }

    public EmailSettings(boolean follow, boolean comment, boolean following_published, boolean positivity_change, User user) {
        this.follow = follow;
        this.comment = comment;
        this.following_published = following_published;
        this.positivity_change = positivity_change;
        this.user = user;
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

    public boolean isFollowing_published() {
        return following_published;
    }

    public void setFollowing_published(boolean following_published) {
        this.following_published = following_published;
    }

    public boolean isPositivity_change() {
        return positivity_change;
    }

    public void setPositivity_change(boolean positivity_change) {
        this.positivity_change = positivity_change;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
