package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public class News {

    private final long newsId, creator;
    private final Long imageId;
    private final String body, title, subtitle;
    private final LocalDateTime creationDate;


    public News(NewsBuilder builder) {
        this.newsId = builder.newsId;
        this.creator = builder.creator;
        this.imageId = builder.imageId;
        this.body = builder.body;
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.creationDate = builder.creationDate;
    }

    public long getNewsId() {
        return newsId;
    }

    public long getCreator() {
        return creator;
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

    public static class NewsBuilder {
        private final long creator;
        private long newsId;
        private final String body, title, subtitle;
        private Long imageId;
        private LocalDateTime creationDate;

        public NewsBuilder(long creator, String body, String title, String subtitle) {
            this.creator = creator;
            this.body = body;
            this.title = title;
            this.subtitle = subtitle;
            this.creationDate = LocalDateTime.now();
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

        public long getCreator() {
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
    }
}
