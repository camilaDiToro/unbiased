package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.*;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.PositivityStats;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
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

    @Autowired
    public NewsServiceImpl(NewsDao newsDao, SecurityService securityService, UserService userService, EmailService emailService, UserDao userDao) {
        this.newsDao = newsDao;
        this.userService = userService;
        this.securityService = securityService;
        this.emailService = emailService;
        this.userDao = userDao;
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
    public Page<News> getNews(int page, Category category, NewsOrder newsOrder, String query) {
        int totalPages;
        page = page <= 0 ? 1 : page;

        Long loggedUser = getLoggedUserId();

        List<News> ln;

        if (category.equals(Category.ALL)) {
            totalPages = newsDao.getTotalPagesAllNews(query);
            page = Math.min(page, totalPages);
            ln = newsDao.getNews(page, query, newsOrder, loggedUser);
        } else if (category.equals(Category.FOR_ME)) {
            totalPages = newsDao.getTodayNewsPageCount(getLoggedUser());
            page = Math.min(page, totalPages);
            ln = newsDao.getRecommendation(page, securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new), newsOrder);
        }
        else {
            totalPages = newsDao.getTotalPagesCategory(category);
            page = Math.min(page, totalPages);
            ln = newsDao.getNewsByCategory(page, category, newsOrder, loggedUser);
        }

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
        newsDao.setRating(news, user, rating);
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
        return newsDao.getCommentById(id);
    }

    @Override
    @Transactional
    public void addComment(long newsId, String comment) {
        User user = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        News news = getById(newsId).orElseThrow(NewsNotFoundException::new);
        newsDao.addComment(user, news, comment);
        User newsOwner = news.getCreator();
        if(newsOwner.getEmailSettings()!=null && newsOwner.getEmailSettings().isComment()){
            Locale locale = LocaleContextHolder.getLocale();
            emailService.sendNewCommentEmail(newsOwner,news,locale);
        }
    }

    @Override
    public Page<Comment> getComments(long newsId, int page) {
        return newsDao.getComments(newsId, page);
    }

    @Override
    @Transactional
    public void deleteComment(long commentId) {
        newsDao.deleteComment(commentId);
    }

}
