package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.CategoryStatistics;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Map;

public class CategoryStatisticsDto {

    private double proportion;
    private String category;
    private URI statistics;

    public static CategoryStatisticsDto fromCategoryStatistic(UriInfo uriInfo, Map.Entry<Category, CategoryStatistics.Statistic> entry, long userId) {
        CategoryStatisticsDto c = new CategoryStatisticsDto();
        c.category = entry.getKey().getInterCode();
        c.proportion = entry.getValue().getProportion();
        c.statistics = uriInfo.getBaseUriBuilder().path("users").path(String.valueOf(userId)).path("news-stats").build();
        return c;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getProportion() {
        return proportion;
    }

    public void setProportion(double proportion) {
        this.proportion = proportion;
    }

    public String getCategory() {
        return category;
    }

    public URI getStatistics() {
        return statistics;
    }

    public void setStatistics(URI statistics) {
        this.statistics = statistics;
    }
}
