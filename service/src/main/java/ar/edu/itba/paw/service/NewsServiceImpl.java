package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.LoggedUserParameters;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.NewsDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService{

    private final NewsDao newsDao;
    private final SecurityService securityService;
    private final UserDao userDao;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, SecurityService securityService, UserDao userDao) {
        this.newsDao = newsDao;
        this.userDao=userDao;
        this.securityService = securityService;
    }

    @Override
    public News create(News.NewsBuilder newsBuilder) {
        return this.newsDao.create(newsBuilder);
    }

    @Override
    public Optional<FullNews> getById(long id) {
        return newsDao.getById(id, getLoggedUserId());
    }

    private Long getLoggedUserId() {
        return securityService.getCurrentUser().map(User::getId).orElse(null);
    }

    @Override
    public Page<FullNews> getNews(int page, String category, String newsOrder, String query) {
        int totalPages;
        page = page <= 0 ? 1 : page;

        Long loggedUser = getLoggedUserId();

        NewsOrder newsOrderObject = NewsOrder.valueOf(newsOrder);
        List<FullNews> ln;
        if (category.equals("ALL")) {
            totalPages = newsDao.getTotalPagesAllNews(query);
            page = Math.min(page, totalPages);
            ln = newsDao.getNews(page, query, newsOrderObject, loggedUser);
        }
        else {
            Category catObject = Category.getByValue(category);
            totalPages = newsDao.getTotalPagesCategory(catObject);
            page = Math.min(page, totalPages);
            ln = newsDao.getNewsByCategory(page, catObject, newsOrderObject, loggedUser);
        }
        return new Page<>(ln,page,totalPages);
    }

    @Override
    public Page<FullNews> getNewsForUserProfile(int page, String newsOrder, long userId, String profileCategory) {
        page = page <= 0 ? 1 : page;
        NewsOrder newsOrderObject = NewsOrder.valueOf(newsOrder);
        int totalPages = 0;

        Optional<User> maybeUser = securityService.getCurrentUser();

        Long loggedUserId = maybeUser.map(User::getId).orElse(null);

        List<FullNews> ln = null;
        ProfileCategory pc = ProfileCategory.valueOf(profileCategory);
        switch (pc) {
            case SAVED:
                totalPages = newsDao.getTotalPagesNewsFromUserSaved(page, userId);
                ln = newsDao.getSavedNews(page,userId,newsOrderObject, loggedUserId);


                break;

            case UPVOTED:
                totalPages = newsDao.getTotalPagesNewsFromUserUpvoted(page, userId);
                ln = newsDao.getNewsUpvotedByUser(page,userId,newsOrderObject, loggedUserId);


                break;

            case DOWNVOTED:
                totalPages = newsDao.getTotalPagesNewsFromUserUpvoted(page, userId);
                ln = newsDao.getNewsDownvotedByUser(page,userId,newsOrderObject, loggedUserId);

                break;

            case MY_POSTS:
                totalPages = newsDao.getTotalPagesNewsFromUser(page, userId);
                ln = newsDao.getAllNewsFromUser(page,userId,newsOrderObject, loggedUserId);

        };

        page = Math.min(page, totalPages);

        return new Page<>(ln, page, totalPages);
    }

    @Override
    public List<Category> getNewsCategory(FullNews news) {
        return newsDao.getNewsCategory(news.getNews());
    }

    @Override
    public void setRating(Long newsId, Long userId, Rating rating) {
        newsDao.setRating(newsId, userId, rating);
    }

    @Override
    public boolean toggleSaveNews(FullNews news, User user) {

        if (news.getLoggedUserParameters().isSaved()) {
            newsDao.removeSaved(news.getNews(), user);
            return false;
        }

        else
            newsDao.saveNews(news.getNews(), user);
        return true;
    }

    @Override
    public void deleteNews(News news) {
        if(news.getCreatorId() != securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new).getId())
            throw new UserNotAuthorized();
        newsDao.deleteNews(news.getNewsId());
    }

    @Override
    public Page<FullNews> getRecommendation(int page, User user) {
        int totalPages = newsDao.getTodayNewsPageCount();
        page = Math.min(Math.max(page, 1), totalPages);
        return new Page<>(newsDao.getRecommendation(page, user),page, totalPages);
    }

    @Override
    public Optional<News> getSimpleNewsById(long id) {
        return newsDao.getSimpleNewsById(id);
    }
}
