package ar.edu.itba.paw.persistence.functional;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;

@FunctionalInterface
public interface GetNewsProfileFunction {
    Page<News> getNews(int page, User user, NewsOrder ns);
}
