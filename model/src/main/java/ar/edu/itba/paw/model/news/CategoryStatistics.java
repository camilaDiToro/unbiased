package ar.edu.itba.paw.model.news;

import java.util.HashMap;
import java.util.Map;

public class CategoryStatistics {

    private final long tourismCount;
    private final long showCount;
    private final long politicsCount;
    private final long economicsCount;
    private final long sportsCount;
    private final long techCount;
    private final long allCount;
    private final long noCategoryCount;

    public CategoryStatistics(final long tourismCount, final long showCount, final long politicsCount, final long economicsCount, final long sportsCount, final long techCount, final long allCount, final long noCategoryCount) {
        this.tourismCount = tourismCount;
        this.showCount = showCount;
        this.politicsCount = politicsCount;
        this.economicsCount = economicsCount;
        this.sportsCount = sportsCount;
        this.techCount = techCount;
        this.allCount = allCount;
        this.noCategoryCount = noCategoryCount;
    }

    public long getTourismCount() {
        return tourismCount;
    }

    public long getShowCount() {
        return showCount;
    }

    public long getPoliticsCount() {
        return politicsCount;
    }

    public long getEconomicsCount() {
        return economicsCount;
    }

    public long getSportsCount() {
        return sportsCount;
    }

    public long getTechCount() {
        return techCount;
    }

    public long getAllCount() {
        return allCount;
    }

    public long getNoCategoryCount() {
        return noCategoryCount;
    }

    public Map<Category, Statistic> getStatiscticsMap() {
        final Map<Category, Statistic> statisticsMap = new HashMap<>();
        statisticsMap.put(Category.ECONOMICS, new Statistic(economicsCount, allCount));
        statisticsMap.put(Category.SHOW, new Statistic(showCount, allCount));
        statisticsMap.put(Category.POLITICS, new Statistic(politicsCount, allCount));
        statisticsMap.put(Category.SPORTS, new Statistic(sportsCount, allCount));
        statisticsMap.put(Category.TECHNOLOGY, new Statistic(techCount, allCount));
        statisticsMap.put(Category.TOURISM, new Statistic(tourismCount, allCount));
        return statisticsMap;
    }

    public static class Statistic {
        private final long qty;

        public final long getQty() {
            return qty;
        }

        public int getPercentage() {
            return percentage;
        }

        public double getProportion() {
            return ((double) qty) /total;
        }

        private final int percentage;
        private final double total;
        private Statistic(long qty, long total) {
            this.qty = qty;
            this.total = total;
            this.percentage = (int) Math.round((((double) qty) /total)*100);
        }
    }
}
