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
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsDao newsDao;
    private final SecurityService securityService;
    private final UserService userService;
    private final EmailService emailService;
    private final UserDao userDao;
    private final CommentDao commentDao;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, SecurityService securityService, UserService userService, EmailService emailService, UserDao userDao, CommentDao commentDao) {
        this.newsDao = newsDao;
        this.userService = userService;
        this.securityService = securityService;
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

    private User getLoggedUser() {
        return securityService.getCurrentUser().orElse(null);
    }

    private Long getLoggedUserId() {
        return securityService.getCurrentUser().map(User::getId).orElse(null);
    }

    @Override
    public Page<News> getNews(int page, Category category, NewsOrder newsOrder, TimeConstraint timeConstraint, String query) {
        int totalPages;
        page = page <= 0 ? 1 : page;

        Long loggedUser = getLoggedUserId();

        List<News> ln;

        if (category.equals(Category.ALL)) {
            if (newsOrder.equals(NewsOrder.NEW)) {
                totalPages = newsDao.getTotalPagesAllNews(query,timeConstraint);
                ln = newsDao.getNewNews(page, query, loggedUser);
            } else {
                totalPages = newsDao.getTotalPagesAllNews(query, timeConstraint);
                ln = newsDao.getTopNews(page, query, timeConstraint, loggedUser);
            }
        } else if (category.equals(Category.FOR_ME)) {
            if(newsOrder.equals(NewsOrder.NEW)) {
                totalPages = newsDao.getRecommendationNewsPageCountNew(getLoggedUser());
                ln = newsDao.getRecommendationNew(page, securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new));
            } else {
                totalPages = newsDao.getRecommendationNewsPageCountTop(getLoggedUser(), timeConstraint);
                ln = newsDao.getRecommendationTop(page, securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new), timeConstraint);

            }
        }
        else {
            if (category.equals(NewsOrder.NEW)) {
                totalPages = newsDao.getTotalPagesCategoryNew(category);
                ln = newsDao.getNewsByCategoryNew(page, category, loggedUser);
            } else {
                totalPages = newsDao.getTotalPagesCategoryTop(category, timeConstraint);
                ln = newsDao.getNewsByCategoryTop(page, category,loggedUser, timeConstraint);
            }
        }

        page = Math.min(page, totalPages);
        return new Page<>(ln, page, totalPages);
    }




    @Override
    public Page<News> getNewsForUserProfile(int page, NewsOrder newsOrder, User user, String profileCategory) {
        Optional<User> maybeLoggedUser = securityService.getCurrentUser();
        ProfileCategory pc = ProfileCategory.getByValue(profileCategory);
        Page<News> pageObj =  newsDao.getNewsFromProfile(page, user, newsOrder, maybeLoggedUser.map(User::getId).orElse(null), pc);
        maybeLoggedUser.ifPresent(value -> pageObj.getContent().remove(value.getPingedNews()));
        return pageObj;
    }

    @Override
    @Transactional
    public void setRating(News news, Rating rating) {
        User user  = getLoggedUser();

        PositivityStats.Positivity oldp = news.getPositivityStats().getPositivity();
//        newsDao.setRating(news, user, Rating.NO_RATING);
        Map<Long, Upvote> upvoteMap = news.getUpvoteMap();
        if (rating.equals(Rating.NO_RATING)) {
            upvoteMap.remove(user.getId());
            return;
        }
        long userId = user.getId();

        upvoteMap.putIfAbsent(userId, new Upvote(news, user.getId()));
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
    public void setCommentRating(Comment comment, Rating rating) {
        User user  = getLoggedUser();
        Map<Long, CommentUpvote> upvoteMap = comment.getUpvoteMap();
        if (rating.equals(Rating.NO_RATING)) {
            upvoteMap.remove(user.getId());
            return;
        }
        long userId = user.getId();

        upvoteMap.putIfAbsent(userId, new CommentUpvote(comment, user.getId()));
        upvoteMap.get(userId).setValue(rating.equals(Rating.UPVOTE));
    }

    @Override
    @Transactional
    public boolean toggleSaveNews(long newsId) {
        User user = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);

        News news = newsDao.getById(newsId, user.getId()).orElseThrow(NewsNotFoundException::new);

        boolean returnValue;
        if (news.getLoggedUserParameters().isSaved()) {
            newsDao.removeSaved(news, user);
            returnValue =  false;
        } else {
            newsDao.saveNews(news, user);
            returnValue = true;
        }
        news.setUserSpecificVariables(user.getId());
        return returnValue;
    }

    @Override
    @Transactional
    public void deleteNews(News news) {
        newsDao.deleteNews(news);
    }


    @Override
    public Iterable<ProfileCategory> getProfileCategories(User user) {

        Optional<String> email = securityService.getCurrentUserEmail();
        boolean isMyProfile =  email.isPresent() && user.getEmail().equals(email.get());
        return Arrays.stream(ProfileCategory.values()).filter(c -> {
            if (!isMyProfile && c.equals(ProfileCategory.SAVED)) {
                return false;
            }
            else if (c.equals(ProfileCategory.MY_POSTS) && !user.getRoles().contains(Role.ROLE_JOURNALIST)) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Category> getHomeCategories() {
        return Category.categoriesInOrder().stream().filter(c ->  c != Category.FOR_ME || securityService.getCurrentUser().isPresent()).collect(Collectors.toList());
    }

    @Override
    public Optional<News> getById(long id) {
        return newsDao.getById(id, getLoggedUserId());
    }

    @Override
    public Optional<Comment> getCommentById(long id) {
        return commentDao.getCommentById(id);
    }

    @Override
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
    public void addComment(long newsId, String comment) {
        User user = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        News news = getById(newsId).orElseThrow(NewsNotFoundException::new);
        commentDao.addComment(user, news, comment);
        User newsOwner = news.getCreator();
        EmailSettings emailSettings = newsOwner.getEmailSettings();
        if(emailSettings!=null && emailSettings.isComment()){
            emailService.sendNewCommentEmail(newsOwner,news,emailSettings.getLocale());
        }
    }

    @Override
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
    public CategoryStatistics getCategoryStatistics(long userId) {
        return newsDao.getCategoryStatistics(userId);
    }

}
