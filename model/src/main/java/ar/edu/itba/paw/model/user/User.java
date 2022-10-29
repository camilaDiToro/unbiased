package ar.edu.itba.paw.model.user;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;

import javax.persistence.*;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_user_id_seq")
    @SequenceGenerator(name="users_user_id_seq", sequenceName = "users_user_id_seq", allocationSize = 1)
    @Column(name = "user_id")
    private Long userId;

    public News getPingedNews() {
        return pingedNews;
    }

    public void setPingedNews(News pingedNews) {
        this.pingedNews = pingedNews;
    }

    @OneToOne
    @JoinColumn(name="pinged_news", referencedColumnName="news_id", nullable=true)

    private News pingedNews;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "image_id")
    private Image image;

    @Column(unique = true, length = 100, nullable = false)
    private String email;

    @Column(unique = true, length = 50)
    private String username;

    @Column(length = 200, nullable = false)
    private String pass;

    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @Transient
    private PositivityStats positivityStats;

    @OneToMany(mappedBy="userId",fetch = FetchType.LAZY)
    private Set<Upvote> upvoteSet;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "user_role", joinColumns = @JoinColumn(name = "user_id"))
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private Collection<Role> roles;

    @ElementCollection(fetch = FetchType.LAZY)
    @JoinTable(name = "comment_report",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(
                    name = "comment_id",
                    referencedColumnName = "id"
            ))
    private Collection<Comment> reports;

    public Collection<Comment> getReports() {
        return reports;
    }

    public void setReports(Collection<Comment> reports) {
        this.reports = reports;
    }



    @OneToMany(mappedBy="userId",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Saved> savedNews;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy="userId",fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Follow> following;


    /* package */ User() {
        //Just for Hibernate
    }

    public User(User.UserBuilder userBuilder) {
        this();
        this.email = userBuilder.email;
        this.username = userBuilder.username;
        this.pass = userBuilder.pass;
        this.status = userBuilder.status;
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

    public void removeAdminRole(){
        roles.forEach(System.out::println);
        System.out.println("Aaaaaaaaaaaaaaaaaaaaaaaaaa");
        roles.remove(Role.ROLE_ADMIN);
        roles.forEach(System.out::println);
    }

    public Set<Upvote> getUpvoteSet() {
        return upvoteSet;
    }

    public void setUpvoteSet(Set<Upvote> upvoteSet) {
        this.upvoteSet = upvoteSet;
    }

    public boolean hasReportedComment(Comment comment) {
        return reports.contains(comment);
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public boolean hasImage() {
        return image != null;
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

    public void setFollowing(Set<Follow> following) {
        this.following = following;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public void addRole(Role role){
        if(!roles.contains(role)){
            roles.add(role);
        }
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public static class UserBuilder{
        //private Long userId;
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
