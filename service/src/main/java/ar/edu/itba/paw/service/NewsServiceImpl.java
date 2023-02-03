package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorizedException;
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


import java.util.Arrays;
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
                throw new InvalidCategoryException(String.format(InvalidCategoryException.STRING_MSG,c));
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
    public Page<News> getNews(Optional<User> maybeCurrentUser, int page, Category category, NewsOrder newsOrder, TimeConstraint timeConstraint, String query) {
        final int totalPages;
        final boolean isPresent = maybeCurrentUser.isPresent();

        page = page <= 0 ? 1 : page;

        final List<News> ln;

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
                throw new UserNotAuthorizedException();
            final User currentUser = maybeCurrentUser.get();
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
    public Optional<News> getPingedNews(Optional<User> maybeCurrentUser, final User profileUser) {
        News pinnedNews = profileUser.getPingedNews();
        if (pinnedNews == null)
            return Optional.empty();
        maybeCurrentUser.ifPresent(user -> pinnedNews.setUserSpecificVariables(user.getUserId()));

        return Optional.of(pinnedNews);
    }

    @Override
    @Transactional
    public Page<News> getNewsForUserProfile(Optional<User> maybeCurrentUser, int page, NewsOrder newsOrder, final User user, ProfileCategory profileCategory) {
        final Page<News> pageObj =  newsDao.getNewsFromProfile(page, user, newsOrder, maybeCurrentUser, profileCategory);
        return pageObj;
    }

    @Override
    @Transactional
    public void setRating(final User currentUser, News news, Rating rating) {

        final PositivityStats.Positivity oldp = news.getPositivityStats().getPositivity();
        final Map<Long, Upvote> upvoteMap = news.getUpvoteMap();
        if (rating.equals(Rating.NO_RATING)) {
            upvoteMap.remove(currentUser.getId());
            return;
        }
        final long userId = currentUser.getId();

        upvoteMap.putIfAbsent(userId, new Upvote(news, currentUser.getId()));
        upvoteMap.get(userId).setValue(rating.equals(Rating.UPVOTE));

        final PositivityStats.Positivity newp = news.getPositivityStats().getPositivity();
        if(oldp != newp){
            final User creator = news.getCreator();
            if(creator.getEmailSettings() != null && creator.getEmailSettings().isPositivityChange()){
                emailService.sendNewsPositivityChanged(creator, news, creator.getEmailSettings().getLocale());
            }
        }
    }



    @Override
    @Transactional
    public void saveNews(final User currentUser, long newsId) {

        final News news = newsDao.getById(newsId, currentUser.getId()).orElseThrow(()-> new NewsNotFoundException(String.format(NewsNotFoundException.ID_MSG, newsId)));

        newsDao.saveNews(news, currentUser);
        news.setUserSpecificVariables(currentUser.getId());

    }

    @Override
    @Transactional
    public void unsaveNews(final User currentUser, long newsId) {

        final News news = newsDao.getById(newsId, currentUser.getId()).orElseThrow(()-> new NewsNotFoundException(String.format(NewsNotFoundException.ID_MSG, newsId)));

        newsDao.removeSaved(news, currentUser);
        news.setUserSpecificVariables(currentUser.getId());

    }

    @Override
    @Transactional
    public boolean toggleSaveNews(final User currentUser, long newsId) {

        final News news = newsDao.getById(newsId, currentUser.getId()).orElseThrow(()-> new NewsNotFoundException(String.format(NewsNotFoundException.ID_MSG, newsId)));

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
    public Iterable<ProfileCategory> getProfileCategories(Optional<User> maybeCurrentUser, final User user) {

        final boolean isMyProfile =  maybeCurrentUser.isPresent() && maybeCurrentUser.get().equals(user);
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
    public Optional<News> getById(final User currentUser, long id) {
        return newsDao.getById(id, currentUser.getUserId());
    }

    @Override
    @Transactional
    public void setNewsImage(final long newsId, final byte[] image,String dataType) {
        final News news = newsDao.getById(newsId).orElseThrow(()-> new NewsNotFoundException(String.format(NewsNotFoundException.ID_MSG, newsId)));
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
        final News news = getById(currentUser, newsId).orElseThrow(()-> new NewsNotFoundException(String.format(NewsNotFoundException.ID_MSG, newsId)));
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
    public Page<Comment> getComments(long newsId, int page, NewsOrder orderByObj, final boolean reported, ReportOrder reportOrder) {
        if(!reported){
            if (orderByObj.equals(NewsOrder.NEW)) {
                return commentDao.getNewComments(newsId, page);
            }
            return commentDao.getTopComments(newsId, page);
        }

        return commentDao.getReportedComment(page, reportOrder);
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
