package ar.edu.itba.paw.model;

import java.time.LocalDateTime;

public class News {

    private final long newsId, creatorId;
    private final byte[] image;
    private final String body, title, subtitle;
    private final LocalDateTime creationDate;


    public News(NewsBuilder builder) {
        this.newsId = builder.newsId;
        this.creatorId = builder.creatorId;
        this.image = builder.image;
        this.body = builder.body;
        this.title = builder.title;
        this.subtitle = builder.subtitle;
        this.creationDate = builder.creationDate;
    }

    public long getNewsId() {
        return newsId;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public byte[] getImage() {
        return image;
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

    public boolean hasImage(){
        System.out.println(image);
        return image!=null && image.length != 0;
    }

    public static class NewsBuilder {
        private final long creatorId;
        private long newsId;
        private final String body, title, subtitle;
        private byte[] image;
        private LocalDateTime creationDate;

        public NewsBuilder(long creatorId, String body, String title, String subtitle) {
            this.creatorId = creatorId;
            this.body = body;
            this.title = title;
            this.subtitle = subtitle;
            this.creationDate = LocalDateTime.now();
        }

        public NewsBuilder image(byte[] image){
            this.image = image;
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

        public byte[] getImage() {
            return image;
        }

        public LocalDateTime getCreationDate() {
            return creationDate;
        }
    }
}
