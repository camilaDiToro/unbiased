package ar.edu.itba.paw.model.old;

import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.TextUtils;
import ar.edu.itba.paw.model.user.*;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "full_news")
public class FullNews {

    FullNews() {

    }
    @Id
    @Column(name = "news_id")
    private long newsId;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "news_id", referencedColumnName = "news_id")
    private News news;



    @Transient
    private int readTime;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "creator", referencedColumnName = "user_id")
    private User creator;

    @Transient
    private PositivityStats positivityStats;

    @Column(name = "upvotes")
    private Integer upvotes = 0;

    @Column(name = "downvotes")
    private Integer downvotes = 0;


    @Transient
    private LoggedUserParameters loggedUserParameters;

    @OneToMany(mappedBy="newsId",fetch = FetchType.EAGER)
    private Set<Upvote> upvoteSet;

    @Transient
    private Map<Upvote, Upvote> upvoteMap;


    @ManyToMany
    @JoinTable(name = "saved_news",
            joinColumns = @JoinColumn(name = "news_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> usersSaved;




    public FullNews(News news, User creator, PositivityStats positivityStats, LoggedUserParameters loggedUserParameters) {
        this.news = news;

        this.creator = creator;
        this.positivityStats = positivityStats;
        this.loggedUserParameters = loggedUserParameters;

        readTime = TextUtils.estimatedMinutesToRead(TextUtils.extractTextFromHTML(news.getBody()));
    }



    public FullNews(News news, User creator,  PositivityStats positivityStats) {
        this(news, creator, positivityStats, null);
    }

    @PostLoad
    private void postLoad() {
        upvoteMap = new HashMap<>();
        upvoteSet.forEach(u -> upvoteMap.put(u,u));
        readTime = TextUtils.estimatedMinutesToRead(TextUtils.extractTextFromHTML(news.getBody()));
        upvotes = upvotes == null ? 0 : upvotes;
        downvotes = downvotes == null ? 0 : downvotes; // TODO: ver clase, esto no parece estar del todo bien pero por ahora anda
        positivityStats = new PositivityStats(upvotes, downvotes);
    }

    public void setUserSpecificVariables(long userId) {
        long newsId = news.getNewsId();
        Upvote upvote = upvoteMap.get(
                new Upvote(newsId, userId));
        Rating rating = upvote != null ? (upvote.isValue() ? Rating.UPVOTE : Rating.DOWNVOTE) : Rating.NO_RATING;
        loggedUserParameters = new LoggedUserParameters(rating, usersSaved.contains(new Saved(news, userId)));
        Set<Upvote> set = upvoteMap.keySet();
        int total = set.size();
        int upvotes =
                set.stream().map(u -> u.isValue() ? 1 : 0)
                        .reduce(0, Integer::sum);
        int downvotes = total - upvotes;
        positivityStats = new PositivityStats(upvotes, downvotes);
    }

    public boolean hasLoggedUserParameters() {
        return loggedUserParameters != null;
    }

    public LoggedUserParameters getLoggedUserParameters() {
//        if (!hasLoggedUserParameters())
//            throw new NoSuchElementException();

        return loggedUserParameters;
    }

    public News getNews() {
        return news;
    }

    public int getReadTime() {
        return readTime;
    }



    public User getUser() {
        return creator;
    }




    public PositivityStats getPositivityStats() {
        return positivityStats;
    }
}
