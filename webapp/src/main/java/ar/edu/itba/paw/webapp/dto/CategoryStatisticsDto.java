package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.CategoryStatistics;

import java.util.Map;

public class CategoryStatisticsDto {
    private double proportion;

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public String getCategory() {
        return category;
    }

    public static CategoryStatisticsDto fromCategoryStatistic(Map.Entry<Category, CategoryStatistics.Statistic> entry) {
        CategoryStatisticsDto c = new CategoryStatisticsDto();
        c.category = entry.getKey().getInterCode();
        c.proportion = entry.getValue().getProportion();
        return c;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    private String category;
}
