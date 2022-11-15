package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.*;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.model.user.*;
import ar.edu.itba.paw.persistence.NewsDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsDao newsDao;
    private final UserService userService;
    private final EmailService emailService;
    private final UserDao userDao;
    private final CommentDao commentDao;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, UserService userService, EmailService emailService, UserDao userDao, CommentDao commentDao) {
        this.newsDao = newsDao;
        this.userService = userService;
        this.emailService = emailService;
        this.userDao = userDao;
        this.commentDao = commentDao;
    }

    @Override
    @Transactional
    public News create(News.NewsBuilder newsBuilder, List<Category> categories) {
        if(!newsBuilder.getCreator().getRoles().contains(Role.ROLE_JOURNALIST)){
            userService.addRole(newsBuilder.getCreator().getId(),Role.ROLE_JOURNALIST);
        }

        for(Category c : categories){
            if(c == null || !c.isTrueCategory()){
                throw new InvalidCategoryException();
            }
            newsBuilder.addCategory(c);
        }

        News createdNews = newsDao.create(newsBuilder);
        List<User> userList = userDao.getFollowersWithEmailPublishNewsActive(createdNews.getCreator());

        for(User u : userList){
            emailService.sendNewPublishedNewsByFollowing(u,createdNews,u.getEmailSettings().getLocale());
        }
        return createdNews;
    }

//    private User getLoggedUser() {
//        return securityService.getCurrentUser().orElse(null);
//    }

//    private Long getLoggedUserId() {
//        return securityService.getCurrentUser().map(User::getId).orElse(null);
//    }

    @Override
    @Transactional
    public Page<News> getNews(Optional<User> maybeCurrentUser, int page, Category category, NewsOrder newsOrder, TimeConstraint timeConstraint, String query) {
        int totalPages;
        boolean isPresent = maybeCurrentUser.isPresent();

        page = page <= 0 ? 1 : page;

        List<News> ln;

        if (category.equals(Category.ALL)) {
            if (newsOrder.equals(NewsOrder.NEW)) {
                totalPages = newsDao.getTotalPagesAllNews(query,timeConstraint);
                ln = isPresent ? newsDao.getNewNews(page, query, maybeCurrentUser.get().getUserId()) :  newsDao.getNewNews(page, query);
            } else {
                totalPages = newsDao.getTotalPagesAllNews(query, timeConstraint);
                ln = isPresent ? newsDao.getTopNews(page, query, timeConstraint, maybeCurrentUser.get().getUserId()) : newsDao.getTopNews(page, query, timeConstraint);
            }
        } else if (category.equals(Category.FOR_ME)) {
            if (!isPresent)
                throw new UserNotAuthorized();
            User currentUser = maybeCurrentUser.get();
            if(newsOrder.equals(NewsOrder.NEW)) {
                totalPages = newsDao.getRecommendationNewsPageCountNew(currentUser);
                ln = newsDao.getRecommendationNew(page, maybeCurrentUser.get());
            } else {
                totalPages = newsDao.getRecommendationNewsPageCountTop(currentUser, timeConstraint);
                ln = newsDao.getRecommendationTop(page, currentUser, timeConstraint);
            }
        }
        else { // categoria estandar
            if (newsOrder.equals(NewsOrder.NEW)) {
                totalPages = newsDao.getTotalPagesCategoryNew(category);
                ln = isPresent ? newsDao.getNewsByCategoryNew(page, category, maybeCurrentUser.get().getUserId()) : newsDao.getNewsByCategoryNew(page, category);
            } else {
                totalPages = newsDao.getTotalPagesCategoryTop(category, timeConstraint);
                ln = isPresent ? newsDao.getNewsByCategoryTop(page, category,maybeCurrentUser.get().getUserId(), timeConstraint) : newsDao.getNewsByCategoryTop(page, category, timeConstraint);
            }
        }

        page = Math.min(page, totalPages);
        return new Page<>(ln, page, totalPages);
    }




    @Override
    @Transactional
    public Page<News> getNewsForUserProfile(Optional<User> maybeCurrentUser, int page, NewsOrder newsOrder, User user, ProfileCategory profileCategory) {

        Page<News> pageObj =  newsDao.getNewsFromProfile(page, user, newsOrder, maybeCurrentUser, profileCategory);
        maybeCurrentUser.ifPresent(value -> pageObj.getContent().remove(value.getPingedNews()));
        return pageObj;
    }

    @Override
    @Transactional
    public void setRating(User currentUser, News news, Rating rating) {

        PositivityStats.Positivity oldp = news.getPositivityStats().getPositivity();
//        newsDao.setRating(news, user, Rating.NO_RATING);
        Map<Long, Upvote> upvoteMap = news.getUpvoteMap();
        if (rating.equals(Rating.NO_RATING)) {
            upvoteMap.remove(currentUser.getId());
            return;
        }
        long userId = currentUser.getId();

        upvoteMap.putIfAbsent(userId, new Upvote(news, currentUser.getId()));
        upvoteMap.get(userId).setValue(rating.equals(Rating.UPVOTE));

        //newsDao.setRating(news, user, rating);
        PositivityStats.Positivity newp = news.getPositivityStats().getPositivity();
        if(oldp != newp){
            User creator = news.getCreator();
            if(creator.getEmailSettings() != null && creator.getEmailSettings().isPositivityChange()){
                emailService.sendNewsPositivityChanged(creator, news, creator.getEmailSettings().getLocale());
            }
        }
    }

    @Override
    @Transactional
    public void setCommentRating(User currentUser, Comment comment, Rating rating) {
        Map<Long, CommentUpvote> upvoteMap = comment.getUpvoteMap();
        if (rating.equals(Rating.NO_RATING)) {
            upvoteMap.remove(currentUser.getId());
            return;
        }
        long userId = currentUser.getId();

        upvoteMap.putIfAbsent(userId, new CommentUpvote(comment, currentUser.getId()));
        upvoteMap.get(userId).setValue(rating.equals(Rating.UPVOTE));
    }

    @Override
    @Transactional
    public boolean toggleSaveNews(User currentUser, long newsId) {
//        User user = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);

        News news = newsDao.getById(newsId, currentUser.getId()).orElseThrow(NewsNotFoundException::new);

        boolean returnValue;
        if (news.getLoggedUserParameters().isSaved()) {
            newsDao.removeSaved(news, currentUser);
            returnValue =  false;
        } else {
            newsDao.saveNews(news, currentUser);
            returnValue = true;
        }
        news.setUserSpecificVariables(currentUser.getId());
        return returnValue;
    }

    @Override
    @Transactional
    public void deleteNews(News news) {
        newsDao.deleteNews(news);
    }


    @Override
    @Transactional
    public Iterable<ProfileCategory> getProfileCategories(Optional<User> maybeCurrentUser, User user) {

//        Optional<String> email = securityService.getCurrentUserEmail();
        boolean isMyProfile =  maybeCurrentUser.isPresent() && maybeCurrentUser.get().equals(user);
        return Arrays.stream(ProfileCategory.values()).filter(c -> {
            if (!isMyProfile && c.equals(ProfileCategory.SAVED)) {
                return false;
            }
            else return !c.equals(ProfileCategory.MY_POSTS) || user.getRoles().contains(Role.ROLE_JOURNALIST);
        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Iterable<Category> getHomeCategories(Optional<User> maybeCurrentUser) {
        return Category.categoriesInOrder().stream().filter(c ->  c != Category.FOR_ME || maybeCurrentUser.isPresent()).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public Optional<News> getById(Optional<User> maybeCurrentUser, long id) {
        return maybeCurrentUser.isPresent() ? newsDao.getById(id, maybeCurrentUser.get().getUserId()) : newsDao.getById(id);
    }

    @Override
    @Transactional
    public Optional<News> getById(User currentUser, long id) {
        return newsDao.getById(id, currentUser.getUserId());
    }

    @Override
    @Transactional
    public Optional<News> getById(long id) {
        return newsDao.getById(id);
    }

    @Override
    @Transactional
    public Optional<Comment> getCommentById(long id) {
        return commentDao.getCommentById(id);
    }

    @Override
    @Transactional
    public Map<Long, Rating> getCommentsRating(List<Comment> comments, Optional<User> maybeLoggedUser) {
        return comments.stream().collect(Collectors.toMap(Comment::getId, comment -> {
            if (!maybeLoggedUser.isPresent())
                return Rating.NO_RATING;

            User user = maybeLoggedUser.get();

            Map<Long, CommentUpvote> upvoteMap = comment.getUpvoteMap();

            if (!upvoteMap.containsKey(user.getId()))
                return Rating.NO_RATING;

            return comment.getUpvoteMap().get(user.getId()).isValue() ? Rating.UPVOTE : Rating.DOWNVOTE;

        }));
    }

    @Override
    @Transactional
    public void addComment(User currentUser, long newsId, String comment) {
//        User user = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        News news = getById(currentUser, newsId).orElseThrow(NewsNotFoundException::new);
        commentDao.addComment(currentUser, news, comment);
        User newsOwner = news.getCreator();
        EmailSettings emailSettings = newsOwner.getEmailSettings();
        if(emailSettings!=null && emailSettings.isComment()){
            emailService.sendNewCommentEmail(newsOwner,news,emailSettings.getLocale());
        }
    }

    @Override
    @Transactional
    public Page<Comment> getComments(long newsId, int page, NewsOrder orderByObj) {
        if (orderByObj.equals(NewsOrder.NEW)) {
            return commentDao.getNewComments(newsId, page);
        }
        return commentDao.getTopComments(newsId, page);
    }

    @Override
    @Transactional
    public void deleteComment(long commentId) {
        commentDao.deleteComment(commentId);
    }

    @Override
    @Transactional
    public CategoryStatistics getCategoryStatistics(long userId) {
        return newsDao.getCategoryStatistics(userId);
    }

}
