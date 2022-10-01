package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.*;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.NewsDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.ws.http.HTTPException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class NewsServiceImpl implements NewsService {

    private final NewsDao newsDao;
    private final SecurityService securityService;
    private final UserService userService;

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, SecurityService securityService, UserService userService) {
        this.newsDao = newsDao;
        this.userService = userService;
        this.securityService = securityService;
    }

    @Override
    @Transactional
    public News create(News.NewsBuilder newsBuilder, String[] categories) {
        if(!userService.getRoles(userService.getUserById(newsBuilder.getCreatorId()).orElseThrow(UserNotFoundException::new)).contains(Role.JOURNALIST)){
            userService.addRole(userService.getUserById(newsBuilder.getCreatorId()).orElseThrow(UserNotFoundException::new),Role.JOURNALIST);
        }

        for(String category : categories){
            newsBuilder.addCategory(Category.getByValue(category));
        }

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
        NewsOrder newsOrderObject  = NewsOrder.getByValue(newsOrder);
        Category catObject = Category.getByValue(category);
        List<FullNews> ln;

        if (catObject.equals(Category.ALL)) {
            totalPages = newsDao.getTotalPagesAllNews(query);
            page = Math.min(page, totalPages);
            ln = newsDao.getNews(page, query, newsOrderObject, loggedUser);
        } else if (catObject.equals(Category.FOR_ME)) {
            totalPages = newsDao.getTodayNewsPageCount();
            page = Math.min(page, totalPages);
            ln = newsDao.getRecommendation(page, getLoggedUserOrThrowException(), newsOrderObject);
        }
        else {
            totalPages = newsDao.getTotalPagesCategory(catObject);
            page = Math.min(page, totalPages);
            ln = newsDao.getNewsByCategory(page, catObject, newsOrderObject, loggedUser);
        }

        return new Page<>(ln, page, totalPages);
    }


    @Override
    public Page<FullNews> getNewsForUserProfile(int page, String newsOrder, User user, String profileCategory) {
        page = page <= 0 ? 1 : page;

        NewsOrder newsOrderObject = NewsOrder.getByValue(newsOrder);
        int totalPages = 0;
        Long loggedUserId = securityService.getCurrentUser().map(User::getId).orElse(null);
        long userId = user.getId();

        List<FullNews> ln = null;
        ProfileCategory pc = ProfileCategory.getByValue(profileCategory);
        switch (pc) {

            case SAVED:
                totalPages = newsDao.getTotalPagesNewsFromUserSaved(page, user);
                ln = newsDao.getSavedNews(page, userId, newsOrderObject, loggedUserId);


                break;

            case UPVOTED:
                totalPages = newsDao.getTotalPagesNewsFromUserUpvoted(page, user);
                ln = newsDao.getNewsUpvotedByUser(page, user, newsOrderObject, loggedUserId);


                break;

            case DOWNVOTED:
                totalPages = newsDao.getTotalPagesNewsFromUserUpvoted(page, user);
                ln = newsDao.getNewsDownvotedByUser(page, user, newsOrderObject, loggedUserId);

                break;

            case MY_POSTS:
                totalPages = newsDao.getTotalPagesNewsFromUser(page, user);
                ln = newsDao.getAllNewsFromUser(page, user, newsOrderObject, loggedUserId);

        }

        page = Math.min(page, totalPages);

        return new Page<>(ln, page, totalPages);
    }

    @Override
    public List<Category> getNewsCategory(News news) {
        return newsDao.getNewsCategory(news);
    }

    @Override
    public FullNews getOrThrowException(long newsId) {
        return getById(newsId).orElseThrow(NewsNotFoundException::new);
    }

    @Override
    @Transactional
    public void setRating(News news, Rating rating) {
        newsDao.setRating(news.getNewsId(), getLoggedUserId(), rating);
    }

    @Override
    @Transactional
    public boolean toggleSaveNews(FullNews news, User user) {

        if (news.getLoggedUserParameters().isSaved()) {
            newsDao.removeSaved(news.getNews(), user);
            return false;
        } else
            newsDao.saveNews(news.getNews(), user);
        return true;
    }

    private User getLoggedUserOrThrowException() {
        return securityService.getCurrentUser().orElseThrow(() -> new HTTPException(400));
    }

    @Override
    @Transactional
    public void deleteNews(News news) {
        newsDao.deleteNews(news);
    }

    @Override
    public Iterable<ProfileCategory> getProfileCategories(User user) {
        Optional<User> currentUser =  securityService.getCurrentUser();
        boolean isMyProfile = user.equals(currentUser.orElse(null));
        return Arrays.stream(ProfileCategory.values()).filter(c -> {
            if (!isMyProfile && c.equals(ProfileCategory.SAVED)) {
                return false;
            }
            else if (c.equals(ProfileCategory.MY_POSTS) && !userService.getRoles(user).contains(Role.JOURNALIST)) {
                return false;
            }
            return true;
        }).collect(Collectors.toList());
    }

    @Override
    public Iterable<Category> getHomeCategories() {
        return Arrays.stream(Category.values()).filter(c ->  c != Category.FOR_ME || securityService.getCurrentUser().isPresent()).collect(Collectors.toList());
    }

    @Override
    public Page<FullNews> getRecommendation(int page, User user, NewsOrder newsOrder) {
        int totalPages = newsDao.getTodayNewsPageCount();
        page = Math.min(Math.max(page, 1), totalPages);
        return new Page<>(newsDao.getRecommendation(page, user, newsOrder),page, totalPages);
    }

    @Override
    public Optional<News> getSimpleNewsById(long id) {
        return newsDao.getSimpleNewsById(id);
    }

    @Override
    @Transactional
    public void addComment(News news, String comment) {
        User user = getLoggedUserOrThrowException();
        newsDao.addComment(user, news, comment);
    }

    @Override
    public Page<Comment> getComments(News news, int page) {
        return newsDao.getComments(news.getNewsId(), page);
    }
}
