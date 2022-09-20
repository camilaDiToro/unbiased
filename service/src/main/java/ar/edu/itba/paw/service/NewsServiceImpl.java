package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
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
    public Optional<News> getById(long id) {
        return newsDao.getById(id);
    }

    @Override
    public Optional<FullNews> getFullNewsById(long id) {
        Optional<News> maybeNews = newsDao.getById(id);
        if (maybeNews.isPresent()) {
            return Optional.of(getFullNews(maybeNews.get()));
        }

        return Optional.empty();

    }

    private FullNews getFullNews(News news) {
        long newsId = news.getNewsId();
        NewsStats newsStats = newsDao.getNewsStats(newsId);
        NewsStats.Positivity positivity = newsStats.getPositivity();
        Optional<User> maybeUser = securityService.getCurrentUser();
        LoggedUserParameters loggedUserParameters = null;
        if (maybeUser.isPresent()) {
            User user = maybeUser.get();
            loggedUserParameters = new LoggedUserParameters(upvoteState(news, user), isSaved(news, user));

        }
        return new FullNews(news, userDao.getUserById(news.getCreatorId()).get(), newsDao.getNewsStats(newsId), loggedUserParameters);
    }

    @Override
    public Page<FullNews> getNews(int page, String category, String newsOrder, String query) {
        int totalPages;
        page = page <= 0 ? 1 : page;

        NewsOrder newsOrderObject = NewsOrder.valueOf(newsOrder);
        List<News> ln;
        if (category.equals("ALL")) {
            totalPages = newsDao.getTotalPagesAllNews(query);
            page = Math.min(page, totalPages);
            ln = newsDao.getNews(page, query, newsOrderObject);
        }
        else {
            Category catObject = Category.getByValue(category);
            totalPages = newsDao.getTotalPagesCategory(catObject);
            page = Math.min(page, totalPages);
            ln = newsDao.getNewsByCategory(page, catObject, newsOrderObject);
        }
        return new Page<>(ln.stream().map(this::getFullNews).collect(Collectors.toList()),page,totalPages);
    }

    @Override
    public Page<FullNews> getNewsFromUser(int page, String newsOrder, long userId) {
        return getNewsForUserProfile(page, newsOrder, userId, ProfileCategory.MY_POSTS.toString());
    }

    @Override
    public Page<FullNews> getNewsForUserProfile(int page, String newsOrder, long userId, String profileCategory) {
        page = page <= 0 ? 1 : page;
        NewsOrder newsOrderObject = NewsOrder.valueOf(newsOrder);
        int totalPages = 0;

        List<News> ln = null;
        ProfileCategory pc = ProfileCategory.valueOf(profileCategory);
        switch (pc) {
            case SAVED: ln = newsDao.getSavedNewsFromUser(page,userId,newsOrderObject);
                totalPages = newsDao.getTotalPagesNewsFromUserSaved(page, userId);

                break;

            case UPVOTED: ln = newsDao.getNewsUpvotedByUser(page,userId,newsOrderObject);
                totalPages = newsDao.getTotalPagesNewsFromUserUpvoted(page, userId);

                break;

            case DOWNVOTED: ln = newsDao.getNewsDownvotedByUser(page,userId,newsOrderObject);
                totalPages = newsDao.getTotalPagesNewsFromUserDownvoted(page, userId);

                break;

            case MY_POSTS: ln  = newsDao.getAllNewsFromUser(page,userId,newsOrderObject);
                totalPages = newsDao.getTotalPagesNewsFromUser(page, userId);

        };

        page = Math.min(page, totalPages);

        return new Page<>(ln.stream().map(this::getFullNews).collect(Collectors.toList()), page, totalPages);
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
    public List<Category> getNewsCategory(News news) {
        return newsDao.getNewsCategory(news);
    }

    @Override
    public int getUpvotes(Long newsId) {
        return newsDao.getUpvotes(newsId);
    }
    @Override

    public Rating upvoteState(News news, User user) {
        return newsDao.upvoteState(news, user);
    }

    @Override
    public void setRating(Long newsId, Long userId, Rating rating) {
        newsDao.setRating(newsId, userId, rating);
    }
    @Override
    public NewsStats getNewsStats(Long newsId) {
        return newsDao.getNewsStats(newsId);
    }

//    @Override
//    public Positivity getPositivityBracket(Long newsId) {
//        return Positivity.getPositivvity(getPositivityValue(newsId));
//    }

    @Override
    public List<FullNews> getSavedNews(int page, User user, NewsOrder ns) {
        List<News> news = newsDao.getSavedNews(page, user, ns);
        return news.stream().map(this::getFullNews).collect(Collectors.toList());
    }

    @Override
    public boolean toggleSaveNews(News news, User user) {
        if (newsDao.isSaved(news, user)) {
            newsDao.removeSaved(news, user);
            return false;
        }

        else
            newsDao.saveNews(news, user);
        return true;
    }
    @Override
    public boolean isSaved(News news, User user) {
        return newsDao.isSaved(news, user);
    }

    @Override
    public void deleteNews(long newsId) {
        News news = newsDao.getById(newsId).orElseThrow(NewsNotFoundException::new);
        if(news.getCreatorId() != securityService.getCurrentUser().orElseThrow(() -> new HTTPException(400)).getId())
            throw new HTTPException(400);
        newsDao.deleteNews(newsId);
    }
}
