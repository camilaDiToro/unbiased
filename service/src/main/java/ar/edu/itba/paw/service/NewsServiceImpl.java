package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorizedException;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.CategoryStatistics;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.CommentUpvote;
import ar.edu.itba.paw.model.user.EmailSettings;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.Upvote;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.persistence.NewsDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsDao newsDao;
    private final UserService userService;
    private final EmailService emailService;
    private final UserDao userDao;
    private final CommentDao commentDao;
    private final ImageService imageService;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, UserService userService, EmailService emailService, UserDao userDao, CommentDao commentDao, ImageService imageService) {
        this.newsDao = newsDao;
        this.userService = userService;
        this.emailService = emailService;
        this.userDao = userDao;
        this.commentDao = commentDao;
        this.imageService = imageService;
    }

    @Override
    @Transactional
    public News create(News.NewsBuilder newsBuilder,final List<Category> categories) {
        if(!newsBuilder.getCreator().getRoles().contains(Role.ROLE_JOURNALIST)){
            userService.addRole(newsBuilder.getCreator().getId(),Role.ROLE_JOURNALIST);
        }

        for(Category c : categories){
            if(c == null || !c.isTrueCategory()){
                throw new InvalidCategoryException(c);
            }
            newsBuilder.addCategory(c);
        }

        final News createdNews = newsDao.create(newsBuilder);
        final List<User> userList = userDao.getFollowersWithEmailPublishNewsActive(createdNews.getCreator());

        for(User u : userList){
            emailService.sendNewPublishedNewsByFollowing(u,createdNews,u.getEmailSettings().getLocale());
        }
        return createdNews;
    }

    @Override
    public Optional<News> getPinnedByUserNews(User user) {
        Optional<News> maybePinned =  newsDao.getPinnedByUserNews(user.getUserId());
        return maybePinned;
    }

    @Override
    @Transactional
    public Page<News> getNews(Optional<User> maybeCurrentUser,int page, Category category, NewsOrder newsOrder, TimeConstraint timeConstraint, String query) {
        final int totalPages;

        page = page <= 0 ? 1 : page;

        final List<News> ln;

        if (category.equals(Category.ALL)) {
            if (newsOrder.equals(NewsOrder.NEW)) {
                totalPages = newsDao.getTotalPagesAllNewsNew(query);
                page = Math.min(page, totalPages);
                ln =  newsDao.getNewNews(page, query);
            } else {
                totalPages = newsDao.getTotalPagesAllNewsTop(query, timeConstraint);
                page = Math.min(page, totalPages);
                ln =  newsDao.getTopNews(page, query, timeConstraint);
            }
        } else if (category.equals(Category.FOR_ME)) {
            if (!maybeCurrentUser.isPresent())
                throw new UserNotAuthorizedException("User should be logged in to get the category \"For me\"");
            final User currentUser = maybeCurrentUser.get();
            if(newsOrder.equals(NewsOrder.NEW)) {
                totalPages = newsDao.getRecommendationNewsPageCountNew(currentUser);
                page = Math.min(page, totalPages);
                ln = newsDao.getRecommendationNew(page, maybeCurrentUser.get());
            } else {
                totalPages = newsDao.getRecommendationNewsPageCountTop(currentUser, timeConstraint);
                page = Math.min(page, totalPages);
                ln = newsDao.getRecommendationTop(page, currentUser, timeConstraint);
            }
        }
        else { // categoria estandar
            if (newsOrder.equals(NewsOrder.NEW)) {
                totalPages = newsDao.getTotalPagesCategoryNew(category);
                page = Math.min(page, totalPages);
                ln =  newsDao.getNewsByCategoryNew(page, category);
            } else {
                totalPages = newsDao.getTotalPagesCategoryTop(category, timeConstraint);
                page = Math.min(page, totalPages);
                ln =  newsDao.getNewsByCategoryTop(page, category, timeConstraint);
            }
        }

        return new Page<>(ln, page, totalPages);
    }

    @Override
    @Transactional
    public Page<News> getNewsForUserProfile(Optional<User> maybeCurrentUser, int page, NewsOrder newsOrder, final User user, ProfileCategory profileCategory) {
        final Page<News> pageObj =  newsDao.getNewsFromProfile(page, user, newsOrder, profileCategory);
        return pageObj;
    }

    @Override
    @Transactional
    public boolean setRating(long userId, long newsId, Rating rating) {

        News news = newsDao.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));

        final PositivityStats.Positivity oldp = news.getPositivityStats().getPositivity();
        final Map<Long, Upvote> upvoteMap = news.getUpvoteMap();

        if(!upvoteMap.containsKey(userId)){
            if(rating.equals(Rating.NO_RATING)){
                return false;
            }
            upvoteMap.put(userId, new Upvote(news, userId));
            upvoteMap.get(userId).setValue(rating.equals(Rating.UPVOTE));
        }
        else if(rating.equals(Rating.NO_RATING)){
            upvoteMap.remove(userId);
        }
        else if(upvoteMap.get(userId).isValue()){
            if(rating.equals(Rating.UPVOTE)){
                return false;
            }
            upvoteMap.get(userId).setValue(false);
        }
        else if(rating.equals(Rating.DOWNVOTE)){
            return false;
        }
        else {
            upvoteMap.get(userId).setValue(true);
        }


        final PositivityStats.Positivity newp = news.getPositivityStats().getPositivity();
        if(oldp != newp){
            final User creator = news.getCreator();
            if(creator.getEmailSettings() != null && creator.getEmailSettings().isPositivityChange()){
                emailService.sendNewsPositivityChanged(creator, news, creator.getEmailSettings().getLocale());
            }
        }
        return true;
    }

    @Override
    public boolean isSavedByUser(long newsId, long userId) {
        return newsDao.isSavedByUser(newsId, userId);
    }

    @Override
    @Transactional
    public void saveNews(long userId, long newsId) {

        final News news = newsDao.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        final User user = userService.getUserById(userId).orElseThrow(()->new UserNotFoundException(userId));
        newsDao.saveNews(news, user);

    }

    @Override
    @Transactional
    public void unsaveNews(long userId, long newsId) {
        final News news = newsDao.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        final User user = userService.getUserById(userId).orElseThrow(()->new UserNotFoundException(userId));
        newsDao.removeSaved(news, user);
    }

    @Override
    @Transactional
    public void deleteNews(News news) {
        newsDao.deleteNews(news);
    }

    @Override
    @Transactional
    public void setNewsImage(final long newsId, final byte[] image,String dataType) {
        final News news = newsDao.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        final long imageId = imageService.uploadImage(image, dataType);
        news.setImageId(imageId);
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

            final User user = maybeLoggedUser.get();

            final Map<Long, CommentUpvote> upvoteMap = comment.getUpvoteMap();

            if (!upvoteMap.containsKey(user.getId()))
                return Rating.NO_RATING;

            return comment.getUpvoteMap().get(user.getId()).isValue() ? Rating.UPVOTE : Rating.DOWNVOTE;

        }));
    }

    @Override
    @Transactional
    public Comment addComment(final User currentUser, long newsId, String comment) {
        final News news = getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        final Comment commentObj = commentDao.addComment(currentUser, news, comment);
        final User newsOwner = news.getCreator();
        final EmailSettings emailSettings = newsOwner.getEmailSettings();
        if(emailSettings!=null && emailSettings.isComment()){
            emailService.sendNewCommentEmail(newsOwner,news,emailSettings.getLocale());
        }
        return commentObj;
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
