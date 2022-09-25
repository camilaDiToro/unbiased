package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.LoggedUserParameters;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.NewsDao;
import ar.edu.itba.paw.persistence.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.ws.http.HTTPException;
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

//    @Override
//    public Optional<FullNews> getNewsById(long id) {
//        Optional<News> maybeNews = newsDao.getById(id);
//        if (maybeNews.isPresent()) {
//            return Optional.of(getFullNews(maybeNews.get()));
//        }
//
//        return Optional.empty();
//
//    }

//    private FullNews getFullNews(News news) {
//        long newsId = news.getNewsId();
////        NewsStats newsStats = newsDao.getNewsStats(newsId);
//        Optional<User> maybeUser = securityService.getCurrentUser();
//        LoggedUserParameters loggedUserParameters = null;
//        if (maybeUser.isPresent()) {
//            User user = maybeUser.get();
//            loggedUserParameters = new LoggedUserParameters(upvoteState(news, user), isSaved(news, user));
//
//        }
//        return new FullNews(news, userDao.getUserById(news.getCreatorId()).get(), newsDao.getNewsStats(newsId), loggedUserParameters);
//    }

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

//    @Override
//    public Page<FullNews> getNewsFromUser(int page, String newsOrder, long userId) {
//        return getNewsForUserProfile(page, newsOrder, userId, ProfileCategory.MY_POSTS.toString());
//    }

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

    /*@Override
    public Page<News> getNewsFromUser(int page, String newsOrder, long userId) {
        page = page <= 0 ? 1 : page;
        NewsOrder newsOrderObject = NewsOrder.valueOf(newsOrder);
        int totalPages = newsDao.getTotalPagesNewsFromUser(page, userId, newsOrderObject);
        page = Math.min(page, totalPages);
        List<News> ln = newsDao.getAllNewsFromUser(page,userId,newsOrderObject);
        return new Page<>(ln, page, totalPages);
    }*/

    @Override
    public List<Category> getNewsCategory(FullNews news) {
        return newsDao.getNewsCategory(news.getNews());
    }

//    @Override
//    public int getUpvotes(Long newsId) {
//        return newsDao.getUpvotes(newsId);
//    }
//    @Override
//
//    public Rating upvoteState(News news, User user) {
//        return newsDao.upvoteState(news, user);
//    }
    @Override
    public FullNews getOrThrowException(long newsId) {
        Optional<FullNews> maybeNews = getById(newsId);

        if (!maybeNews.isPresent())
            throw new NewsNotFoundException();
        return maybeNews.get();
    }

    @Override
    public void setRating(Long newsId, Long userId, Rating rating) {
        newsDao.setRating(newsId, userId, rating);
    }
//

//    @Override
//    public Positivity getPositivityBracket(Long newsId) {
//        return Positivity.getPositivvity(getPositivityValue(newsId));
//    }

//    @Override
//    public List<FullNews> getSavedNews(int page, User user, NewsOrder ns) {
////        List<FullNews> news = newsDao.getSavedNews(page, user.getId(), ns);
//        return null;
////        return news.stream().map(this::getFullNews).collect(Collectors.toList());
//    }

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

    private User getLoggedUserOrThrowException() {
        return securityService.getCurrentUser().orElseThrow(() -> new HTTPException(400));
    }
//    @Override
//    public boolean isSaved(News news, User user) {
//        return newsDao.isSaved(news, user);
//    }

    @Override
    public void deleteNews(long newsId) {
        FullNews news = getById(newsId).orElseThrow(NewsNotFoundException::new);
        User current = getLoggedUserOrThrowException();
        if(!securityService.isCurrentUserAdmin() && news.getNews().getCreatorId() != current.getId())
            throw new UserNotAuthorized();
        newsDao.deleteNews(newsId);
    }

    @Override
    public void addComment(News news, String comment) {
        User user = getLoggedUserOrThrowException();
        newsDao.addComment(user, news, comment);
    }
@Override
    public Page<Comment> getComments(long newsId, int page) {
        return newsDao.getComments(newsId, page);
    }
}
