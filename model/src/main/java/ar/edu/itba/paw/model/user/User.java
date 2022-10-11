package ar.edu.itba.paw.model.user;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
    @SequenceGenerator(name="users_user_id_seq", sequenceName = "users_user_id_seq", allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;

    @Column(name="image_id")
    private Long imageId;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(unique = true, length = 50)
    private String username;

    @Column(length = 200, nullable = false)
    private String pass;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    // TODO: Check if its posible to generate a custom fetchType.
    // in jdbc we just retrived the positivity stats if the user was a journalist
    @Transient
    private PositivityStats positivityStats;

    @OneToMany(mappedBy="userId",fetch = FetchType.LAZY)
    private Set<Upvote> upvoteSet;

//    @ElementCollection(targetClass = News.class)
//    @JoinTable(name = "saved_news", joinColumns = @JoinColumn(name = "user_id"))
//    @Column(name = "news_id")
//    private Collection<News> savedNews;

    @OneToMany(mappedBy="userId",fetch = FetchType.LAZY)
    private Set<Saved> savedNews;


    public void setFollowing(Set<Follow> following) {
        this.following = following;
    }

    @OneToMany(mappedBy="userId",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Follow> following;


    /* package */ User() {
        //Just for Hibernate
    }

    public User(User.UserBuilder userBuilder) {
        this();
        this.userId = userBuilder.userId;
        this.email = userBuilder.email;
        this.imageId = userBuilder.imageId;
        this.username = userBuilder.username;
        this.pass = userBuilder.pass;
        this.status = userBuilder.status;
//        this.positivityStats = userBuilder.positivity;
    }

    @PostLoad
    private void setPositivity() {
        int upvotes = upvoteSet
                .stream().map(upvote -> upvote.isValue() ? 1 : 0)
                .reduce(0, Integer::sum);
        int downvotes = upvoteSet
                .stream().map(upvote -> upvote.isValue() ? 0 : 1)
                .reduce(0, Integer::sum);
        positivityStats = new PositivityStats(upvotes, downvotes);
    }

    public Set<Upvote> getUpvoteSet() {
        return upvoteSet;
    }

    public void setUpvoteSet(Set<Upvote> upvoteSet) {
        this.upvoteSet = upvoteSet;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageId(Long imageId) {
        this.imageId = imageId;
    }

    public Set<Follow> getFollowing() {
        return following;
    }

    @Override
    public String toString() {
        return username != null ? username : email;
    }

    public long getId() {
        return userId;
    }

    public Long getImageId() {
        return imageId;
    }

    public boolean hasImage() {
        return imageId != null;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public PositivityStats getPositivityStats() {
        return positivityStats;
    }

    public String getPass() {
        return pass;
    }

    public UserStatus getStatus() {
        return status;
    }

    public boolean hasPositivityStats() {
        return positivityStats != null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;

        if (!(o instanceof User))
            return false;

        User aux = (User) o;

        return aux.userId == userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public void setPositivityStats(PositivityStats positivityStats) {
        this.positivityStats = positivityStats;
    }

    public Set<Saved> getSavedNews() {
        return savedNews;
    }

    public void setSavedNews(Set<Saved> savedNews) {
        this.savedNews = savedNews;
    }

    public static class UserBuilder{
        private Long userId;
        private Long imageId;
        private final String email;
        private String username, pass;
        private UserStatus status;

        private PositivityStats positivity;

        public UserBuilder(String email){
            if(email == null){
                throw new NullPointerException("User email must not be null");
            }
            this.email = email;
            status = UserStatus.UNREGISTERED;
        }

        public UserBuilder userId(Long userId){
            this.userId = userId;
            return this;
        }

        public UserBuilder imageId(Long imageId){
            this.imageId = imageId;
            return this;
        }

        public UserBuilder username(String username){
            this.username = username;
            return this;
        }

        public UserBuilder pass(String pass){
            this.pass = pass;
            status = UserStatus.UNABLE;
            return this;
        }

        public UserBuilder status(String status){
            this.status = UserStatus.valueOf(status);
            return this;
        }

        public UserBuilder positivity(PositivityStats positivity){
            this.positivity = positivity;
            return this;
        }

        public User build(){
            return new User(this);
        }

        public Long getUserId() {
            return userId;
        }

        public Long getImageId() {
            return imageId;
        }

        public String getEmail() {
            return email;
        }

        public String getUsername() {
            return username;
        }

        public String getPass() {
            return pass;
        }

        public UserStatus getStatus() {
            return status;
        }
    }

}
