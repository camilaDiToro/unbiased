package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.News;

import java.util.List;

public interface NewsDao {
    List<News> getNews();
}
