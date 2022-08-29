package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.News;
import ar.edu.itba.paw.model.User;

import java.util.Optional;

public interface NewsDao {

    News create(News.NewsBuilder newsBuilder);
    Optional<News> getById(long id);
}
