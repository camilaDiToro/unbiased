package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.News;

public interface NewsDao {

    News create(News.NewsBuilder newsBuilder);

}
