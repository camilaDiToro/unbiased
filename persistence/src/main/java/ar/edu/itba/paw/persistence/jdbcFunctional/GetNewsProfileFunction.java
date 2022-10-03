package ar.edu.itba.paw.persistence.jdbcFunctional;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;

import java.util.List;
import java.util.Objects;

@FunctionalInterface
public interface GetNewsProfileFunction {
    Page<FullNews> getNews(int page, User user, NewsOrder ns, Long loggedUser);
}
