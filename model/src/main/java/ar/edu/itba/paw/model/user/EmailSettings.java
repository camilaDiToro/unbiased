package ar.edu.itba.paw.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Consumer;

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

    @Transient
    private final Map<MailOption, Consumer<Boolean>> setterByEnum = new EnumMap<>(MailOption.class);
    {
        setterByEnum.put(MailOption.COMMENT, this::setComment);
        setterByEnum.put(MailOption.FOLLOW, this::setFollow);
        setterByEnum.put(MailOption.FOLLOWING_PUBLISHED, this::setFollowingPublished);
        setterByEnum.put(MailOption.POSITIVITY_CHANGED, this::setPositivityChange);
    }

    public String[] getOptionsArray() {
        Map<MailOption, Boolean> map = getValueByEnum();
        List<String> list = new ArrayList<>();
        for (Map.Entry<MailOption, Boolean> entry : map.entrySet()) {
            if (entry.getValue()) {
                list.add(entry.getKey().getInterCode());
            }
        }
        return list.toArray(new String[]{});
    }

    public EmailSettings(boolean follow, boolean comment, boolean followingPublished, boolean positivityChange, Locale locale, User user) {
        this.follow = follow;
        this.comment = comment;
        this.followingPublished = followingPublished;
        this.positivityChange = positivityChange;
        this.user = user;
        this.locale=locale;
    }

    public EmailSettings(Collection<MailOption> options, Locale locale, User user) {
        setSettings(options);
        this.user = user;
        this.locale=locale;
    }

    public void setSettings(Collection<MailOption> options) {
        for (MailOption option : MailOption.values())
            setterByEnum.get(option).accept(false);
        options.forEach(o -> setterByEnum.get(o).accept(true));
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

    public void setUser(User user) {
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


    public Map<MailOption, Boolean> getValueByEnum() {
        Map<MailOption, Boolean> getterByEnum = new EnumMap<>(MailOption.class);
        getterByEnum.put(MailOption.COMMENT, comment);
        getterByEnum.put(MailOption.FOLLOW, follow);
        getterByEnum.put(MailOption.FOLLOWING_PUBLISHED, followingPublished);
        getterByEnum.put(MailOption.POSITIVITY_CHANGED, positivityChange);
        return getterByEnum;
    }
}
