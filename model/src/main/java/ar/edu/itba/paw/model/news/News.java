package ar.edu.itba.paw.model.news;

import ar.edu.itba.paw.model.Rating;
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

    @Column(name = "creator")
    private Long creatorId;
    @Column(name = "image_id")
    private Long imageId;
    @Column(name = "body")
    private String body;
    @Column(name = "title")
    private String title;
    @Column(name = "subtitle")
    private String subtitle;

    @Column(name = "creation_date")
    private Timestamp date;

    @Transient
    private LocalDateTime creationDate;




    @Override
    public String toString() {
        return "News{" +
                "newsId=" + newsId +
                ", creatorId=" + creatorId +
                ", imageId=" + imageId +
                ", body='" + body + '\'' +
                ", title='" + title + '\'' +
                ", subtitle='" + subtitle + '\'' +
                ", creationDate=" + creationDate +
                '}';
    }


    News() {

    }

    @PostLoad
    private void postLoad() {
        creationDate = date.toLocalDateTime();
    }

    public News(NewsBuilder builder) {
        this.newsId = builder.newsId;
        this.creatorId = builder.creatorId;
        this.imageId = builder.imageId;
        this.body = TextUtils.convertMarkdownToHTML(builder.body);
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
        return creatorId;
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
        if (!(obj instanceof News)){
            return false;
        }

        News aux = (News) obj;

        if (aux.equals(this)){
            return true;
        }

        return newsId == aux.newsId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(newsId);
    }

    public boolean hasImage(){
        return imageId!=null ;
    }

    public static class NewsBuilder {
        private final long creatorId;
        private long newsId;
        private Long imageId;
        private final String body;
        private final String title;
        private final String subtitle;
        private LocalDateTime creationDate;
        private final Collection<Category> categories;


        public NewsBuilder(long creatorId, String body, String title, String subtitle) {
            this.creatorId = creatorId;
            this.body = body;
            this.title = title;
            this.subtitle = subtitle;
            this.creationDate = LocalDateTime.now();
            this.categories = new ArrayList<>();
        }

        public NewsBuilder imageId(Long imageId){
            this.imageId = imageId;
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

        public long getCreatorId() {
            return creatorId;
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
