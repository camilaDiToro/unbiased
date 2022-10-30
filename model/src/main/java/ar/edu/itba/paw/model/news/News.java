package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.user.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.*;
@Entity
@Table(name = "news")
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "news_news_id_seq")
    @SequenceGenerator(name="news_news_id_seq", sequenceName = "news_news_id_seq", allocationSize = 1)
    @Column(name = "news_id")
    private Long newsId;

    @Column(name = "image_id")
    private Long imageId;

    @Column(name = "body")
    private String body;

    @Column(name = "title")
    private String title;

    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "accesses")
    private int accesses;

    @Column(name = "creation_date")
    private Timestamp date;

    @Transient
    private LocalDateTime creationDate;

    public Collection<Category> getCategories() {
        return categories;
    }

    public void setCategories(Collection<Category> categories) {
        this.categories = categories;
    }

    @ElementCollection(targetClass = Category.class)
    @JoinTable(name = "news_category", joinColumns = @JoinColumn(name = "news_id"))
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "category_id")
    private Collection<Category> categories;

    @Transient
    private int readTime;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator", referencedColumnName = "user_id")
    private User creator;

    @Transient
    private Integer upvotes = 0;

    @Transient
    private Integer downvotes = 0;

    @Transient
    private LoggedUserParameters loggedUserParameters;

    @OneToMany(mappedBy="news",fetch = FetchType.EAGER, orphanRemoval = true, cascade = CascadeType.ALL)
    @MapKey(name = "userId")
    private Map<Long,Upvote> upvoteMap;

    @ManyToMany
    @JoinTable(name = "saved_news",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    @MapKey(name = "userId")
    private Map<Long,User> usersSaved;


    @OneToMany(mappedBy = "news")
    @OrderBy("creationDate DESC")
    private List<ReportDetail> reports;

    @PreRemove
    private void removeIfPinged() {
        if (this.equals(creator.getPingedNews()))
            creator.setPingedNews(null);
    }


    @Override
    public String toString() {
        return "News{" +
                "newsId=" + newsId +
                ", creatorId=" + creator.getId() +
                ", imageId=" + imageId +
                ", body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }

    public Map<Long, Upvote> getUpvoteMap() {
        return upvoteMap;
    }

    public void setUpvoteMap(Map<Long, Upvote> upvoteMap) {
        this.upvoteMap = upvoteMap;
    }

    News() {

    }


    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    @PostLoad
    private void postLoad() {
        readTime = TextUtils.estimatedMinutesToRead(TextUtils.extractTextFromHTML(body));
        creationDate = date.toLocalDateTime();
    }

    public void setUserSpecificVariables(long userId) {
        Upvote upvote = upvoteMap.get(userId); // TODO fix
        Rating rating = upvote != null ? (upvote.isValue() ? Rating.UPVOTE : Rating.DOWNVOTE) : Rating.NO_RATING;
        loggedUserParameters = new LoggedUserParameters(rating, usersSaved.containsKey(userId));
    }

    public boolean hasLoggedUserParameters() {
        return loggedUserParameters != null;
    }

    public LoggedUserParameters getLoggedUserParameters() {


        return loggedUserParameters;
    }

    public News(NewsBuilder builder) {
        this.creator = builder.creator;
        this.imageId = builder.imageId;
        this.body = builder.body;
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.creationDate = builder.creationDate;
        this.date = Timestamp.valueOf(creationDate);
    }

    public TimeUtils.Amount getAmountAgo() {
        return TimeUtils.calculateTimeAgoWithPeriodAndDuration(creationDate);
    }

    public long getNewsId() {
        return newsId;
    }

    public long getCreatorId() {
        return creator.getId();
    }

    public Long getImageId() {
        return imageId;
    }

    public String getBody() {
        return body;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public String getFormattedDate(Locale locale) {
        return creationDate.format(DateTimeFormatter.ofLocalizedDate( FormatStyle.FULL ).withLocale( locale)) +
                " - " + creationDate.format(DateTimeFormatter.ofPattern("HH:mm"));
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this){
            return true;
        }
        if (!(obj instanceof News)){
            return false;
        }

        News aux = (News) obj;

        return newsId == aux.newsId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(newsId);
    }

    public boolean hasImage(){
        return imageId!=null ;
    }

    public int getReadTime() {
        return readTime;
    }

    public User getUser() {
        return creator;
    }

    public PositivityStats getPositivityStats() {
        Collection<Upvote> set = upvoteMap.values();
        int total = set.size();
        upvotes =
                set.stream().map(u -> u.isValue() ? 1 : 0)
                        .reduce(0, Integer::sum);
        downvotes = total - upvotes;
        return new PositivityStats(upvotes, downvotes);
    }

    public List<ReportDetail> getReports() {
        return reports;
    }

    public void setReports(List<ReportDetail> reports) {
        this.reports = reports;
    }

    public static class NewsBuilder {
        private User creator;
        private long newsId;
        private Long imageId;
        private String body;
        private String title;
        private String subtitle;
        private LocalDateTime creationDate;
        private Collection<Category> categories;


        public NewsBuilder(User creator, String body, String title, String subtitle) {
            this.creator = creator;
            this.body = body;
            this.title = title;
            this.subtitle = subtitle;
            this.creationDate = LocalDateTime.now();
            this.categories = new ArrayList<>();
        }

        private NewsBuilder() {

        }

        public NewsBuilder imageId(Long imageId){
            this.imageId = imageId;
            return this;
        }

        public NewsBuilder body(String body){
            this.body = body;
            return this;
        }

        public NewsBuilder creationDate(LocalDateTime creationDate){
            this.creationDate = creationDate;
            return this;
        }

        public NewsBuilder newsId(long newsId){
            this.newsId = newsId;
            return this;
        }

        public NewsBuilder addCategory(Category category){
            this.categories.add(category);
            return this;
        }

        public News build(){
            if(this.body == null){
                throw new NullPointerException("The property body must not be null");
            }
            if(this.title == null){
                throw new NullPointerException("The property title must not be null");
            }
            if(this.subtitle == null){
                throw new NullPointerException("The property subtitle must not be null");
            }
            return new News(this);
        }

        public User getCreator() {
            return creator;
        }

        public long getNewsId() {
            return newsId;
        }

        public String getBody() {
            return body;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public Long getImageId() {
            return imageId;
        }

        public LocalDateTime getCreationDate() {
            return creationDate;
        }

        public Collection<Category> getCategories() {
            return categories;
        }

    }
}
