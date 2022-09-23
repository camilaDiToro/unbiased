package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.news.Category;

public interface CategoryDao {
    boolean addCategoryToNews(long newsId, Category category);
}
