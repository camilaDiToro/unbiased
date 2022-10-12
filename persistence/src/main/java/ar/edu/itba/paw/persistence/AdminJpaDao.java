package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Repository
public class AdminJpaDao implements AdminDao{

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminJpaDao.class);
    private static final int PAGE_SIZE = 5;

    @Override
    public void reportNews(News news, User reporter, ReportReason reportReason) {
        ReportDetail reportDetail = new ReportDetail(news, reporter, LocalDateTime.now(), reportReason);
        entityManager.persist(reportDetail);
        LOGGER.debug("News {} with id {} reported. The reason is {}", news.getTitle(), news.getNewsId(), reportReason.getDescription());
    }

    @Override
    public Page<News> getReportedNews(int page, ReportOrder reportOrder) {
        Query query = entityManager.createNativeQuery(
                "SELECT news_id FROM (report NATURAL JOIN news) GROUP BY news_id ORDER BY "
                        + reportOrder.getQuery() +" LIMIT :limit OFFSET :offset")
                .setParameter("limit",PAGE_SIZE)
                .setParameter("offset",(page-1)*PAGE_SIZE);

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) query.getResultList().stream()
                .map(o -> ((Integer)o).longValue()).collect(Collectors.toList());

        if(ids.isEmpty()){
            return new Page<>(new ArrayList<>(),page,getTotalReportedNews());
        }

        Map<ReportOrder, String> map = new HashMap<>();
        map.put(ReportOrder.REP_COUNT_DESC, "n.reports.size desc");
        map.put(ReportOrder.REP_COUNT_ASC, "n.reports.size asc"); // TODO es feo pero por ahora no se me ocurre otra cosa
        map.put(ReportOrder.REP_DATE_DESC, "n.getFirstReportDate() asc");
        map.put(ReportOrder.REP_DATE_ASC, "n.getLastReportDate() desc");

        List<News> news = entityManager.createQuery("SELECT n from News n WHERE n.newsId IN :ids ORDER BY " + map.get(reportOrder), News.class)
                .setParameter("ids", ids).getResultList();


        return new Page<>(news,page,getTotalReportedNews());
    }

    private int getTotalReportedNews() {
        long count = entityManager.createQuery("SELECT COUNT(distinct r.news) FROM ReportDetail r", Long.class)
                .getSingleResult();
        return Page.getPageCount(count, PAGE_SIZE);
    }

    @Override
    public Page<ReportDetail> getReportedNewsDetail(int page, News news) {
        return null;
    }

    @Override
    public void makeUserAdmin(User user) {
        /* Implement after role dao */
    }

    @Override
    public boolean hasReported(News news, Long loggedUser) {
        if (loggedUser == null){
            return false;
        }
        long elemCount = entityManager.createQuery("SELECT COUNT(r) FROM ReportDetail r WHERE r.news.newsId = :news_id AND r.reporter.userId = :user_id",Long.class)
                .setParameter("news_id", news.getNewsId())
                .setParameter("user_id", loggedUser).getSingleResult();
        return elemCount > 0;
    }
}






































