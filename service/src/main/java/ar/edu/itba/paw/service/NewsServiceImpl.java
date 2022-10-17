package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.*;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.NewsDao;
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
        if(!userService.getRoles(userService.getUserById(newsBuilder.getCreator().getId()).orElseThrow(UserNotFoundException::new)).contains(Role.ROLE_JOURNALIST)){
            userService.addRole(userService.getUserById(newsBuilder.getCreator().getId()).orElseThrow(UserNotFoundException::new),Role.ROLE_JOURNALIST);
        }

        for(String category : categories){
            Category c = Category.getByCode(category);
            if(c == null || !c.isTrueCategory()){
                throw new InvalidCategoryException();
            }
            newsBuilder.addCategory(c);
        }

        return this.newsDao.create(newsBuilder);
    }

    private User getLoggedUser() {
        return securityService.getCurrentUser().orElse(null);
    }

    private Long getLoggedUserId() {
        return securityService.getCurrentUser().map(User::getId).orElse(null);
    }

    @Override
    public Page<News> getNews(int page, String category, String newsOrder, String query) {
        int totalPages;
        page = page <= 0 ? 1 : page;

        Long loggedUser = getLoggedUserId();
        NewsOrder newsOrderObject  = NewsOrder.getByValue(newsOrder);
        Category catObject = Category.getByValue(category);
        List<News> ln;

        if (catObject.equals(Category.ALL)) {
            totalPages = newsDao.getTotalPagesAllNews(query);
            page = Math.min(page, totalPages);
            ln = newsDao.getNews(page, query, newsOrderObject, loggedUser);
        } else if (catObject.equals(Category.FOR_ME)) {
            totalPages = newsDao.getTodayNewsPageCount(getLoggedUser());
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
    public Page<News> getNewsForUserProfile(int page, String newsOrder, User user, String profileCategory) {
        NewsOrder newsOrderObject = NewsOrder.getByValue(newsOrder);
        Long loggedUserId = securityService.getCurrentUser().map(User::getId).orElse(null);
        ProfileCategory pc = ProfileCategory.getByValue(profileCategory);
        return newsDao.getNewsFromProfile(page, user, newsOrderObject, loggedUserId, pc);
    }

    @Override
    public List<Category> getNewsCategory(News news) {
        return newsDao.getNewsCategory(news);
    }

    @Override
    public News getOrThrowException(long newsId) {
        return getById(newsId).orElseThrow(NewsNotFoundException::new);
    }

    @Override
    @Transactional
    public void setRating(News news, Rating rating) {
        newsDao.setRating(news, getLoggedUser(), rating);
    }

    @Override
    @Transactional
    public boolean toggleSaveNews(News news, User user) {

        if (news.getLoggedUserParameters().isSaved()) {
            newsDao.removeSaved(news, user);
            return false;
        } else {
            newsDao.saveNews(news, user);
        }
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
            else if (c.equals(ProfileCategory.MY_POSTS) && !userService.getRoles(user).contains(Role.ROLE_JOURNALIST)) {
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
    public Page<News> getRecommendation(int page, User user, NewsOrder newsOrder) {
        int totalPages = newsDao.getTodayNewsPageCount(user);
        page = Math.min(Math.max(page, 1), totalPages);
        return new Page<>(newsDao.getRecommendation(page, user, newsOrder),page, totalPages);
    }

    @Override
    public Optional<News> getById(long id) {
        return newsDao.getById(id, getLoggedUserId());
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
