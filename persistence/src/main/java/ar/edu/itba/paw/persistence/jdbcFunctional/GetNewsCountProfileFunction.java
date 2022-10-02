package ar.edu.itba.paw.persistence.jdbcFunctional;

import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;

import java.util.List;

@FunctionalInterface
public interface GetNewsCountProfileFunction {
    List<FullNews> getNewsCount(int page, User user, NewsOrder ns, Long loggedUser);
}