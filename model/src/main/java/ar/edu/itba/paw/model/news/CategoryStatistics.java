package ar.edu.itba.paw.model.news;

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
}
