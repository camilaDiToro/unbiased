package ar.edu.itba.paw.model.news;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

public class News {

    private final long newsId, creatorId;
    private final Long imageId;
    private final String body, title, subtitle;
    private final LocalDateTime creationDate;
//    private final Collection<Category> categories;


    public News(NewsBuilder builder) {
        this.newsId = builder.newsId;
        this.creatorId = builder.creatorId;
        this.imageId = builder.imageId;
        this.body = builder.body;
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.creationDate = builder.creationDate;
//        this.categories = builder.categories;
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

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof News))
            return false;

        News aux = (News) obj;

        if (aux.equals(this))
            return true;

        return newsId == aux.newsId;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(newsId);
    }

    //    public Collection<Category> getCategories() { return categories; }

    public boolean hasImage(){
        return imageId!=null ;
    }

    public static class NewsBuilder {
        private final long creatorId;
        private long newsId;
        private Long imageId;
        private final String body, title, subtitle;
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
