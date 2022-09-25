package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.user.User;

import java.util.List;
import java.util.Optional;

public interface NewsService {
    News create(News.NewsBuilder newsBuilder);

    Optional<FullNews> getById(long id);

    Page<FullNews> getNews(int page, String category, String newsOrder, String query);

//    Page<FullNews> getNewsFromUser(int page, String newsOrder, long userId);

    List<Category> getNewsCategory(FullNews news);

//    int getUpvotes(Long newsId);

//    Rating upvoteState(News news, User user);

    void setRating(Long newsId, Long userId, Rating rating);



    boolean toggleSaveNews(FullNews news, User user);

    FullNews getOrThrowException(long newsId);

//    Optional<FullNews> getNewsById(long id);

//    boolean isSaved(News news, User user);

    void deleteNews(long newsId);

    Page<FullNews> getNewsForUserProfile(int page, String newsOrder, long userId, String pc);

    void addComment(News news, String comment);

    Page<Comment> getComments(long newsId, int page);
}
