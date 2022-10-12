package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedNews;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;


@Repository
public class AdminJpaDao implements AdminDao{

    @PersistenceContext
    private EntityManager entityManager;

    private static final Logger LOGGER = LoggerFactory.getLogger(AdminJpaDao.class);

    @Override
    public void reportNews(News news, User reporter, ReportReason reportReason) {
        ReportDetail reportDetail = new ReportDetail(news, reporter, LocalDateTime.now(), reportReason);
        entityManager.persist(reportDetail);
        LOGGER.debug("News {} with id {} reported. The reason is {}", news.getTitle(), news.getNewsId(), reportReason.getDescription());
    }

    @Override
    public Page<ReportedNews> getReportedNews(int page, ReportOrder reportOrder) {
        return null;
    }

    @Override
    public Page<ReportDetail> getReportedNewsDetail(int page, News news) {
        return null;
    }

    @Override
    public void makeUserAdmin(User user) {

    }

    @Override
    public boolean hasReported(News news, Long loggedUser) {
        if (loggedUser == null){
            return false;
        }

        final TypedQuery<ReportDetail> query = entityManager.createQuery("FROM ReportDetail r WHERE r.news.newsId = :news_id AND r.reporter.userId = :user_id",ReportDetail.class)
                .setParameter("news_id", news.getNewsId())
                .setParameter("user_id", loggedUser);
        return !query.getResultList().isEmpty();
    }
}






































