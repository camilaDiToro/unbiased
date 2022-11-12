package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedComment;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public void reportComment(Comment comment, User reporter, ReportReason reportReason) {
        ReportedComment reportedComment = new ReportedComment(comment, reporter, reportReason);
        entityManager.persist(reportedComment);
        LOGGER.debug("Comment from {} with id {} reported. The reason is {}", comment.getUser(), comment.getId(), reportReason.getDescription());
    }



    @Override
    public Page<Comment> getReportedComment(int page, ReportOrder reportOrder) {
        Query query = entityManager.createNativeQuery(
                        "SELECT comment_id FROM comment_report JOIN comments n ON n.id = comment_report.comment_id WHERE deleted = false GROUP BY comment_id ORDER BY "
                                + reportOrder.getQuery() +" LIMIT :limit OFFSET :offset")
                .setParameter("limit",PAGE_SIZE)
                .setParameter("offset",(page-1)*PAGE_SIZE);

        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) query.getResultList().stream()
                .map(o -> ((Number)o).longValue()).collect(Collectors.toList());

        if(ids.isEmpty()){
            return new Page<>(new ArrayList<>(),page,1);
        }

        List<Comment> reportedComments = entityManager.createQuery("SELECT n from Comment n WHERE n.id IN :ids " , Comment.class)
                .setParameter("ids", ids).getResultList();

        Map<Long, Comment> map = new HashMap<>();
        for (Comment reportedComment : reportedComments) {
            map.put(reportedComment.getId(), reportedComment);
         }
        // map id -> news
        reportedComments =  ids.stream().map(id -> map.get(id)).collect(Collectors.toList());

        return new Page<>(reportedComments,page,Page.getPageCount(getReportedCommentTotal(), PAGE_SIZE));
    }

    private int getReportedCommentTotal() {
        int total =  ((Number) entityManager.createNativeQuery(
                "SELECT count(comment_id) FROM comment_report JOIN comments n ON n.id = comment_report.comment_id WHERE deleted = false").getSingleResult()).intValue();
        return total;
    }



    @Override
    public Page<ReportedComment> getReportedCommentDetail(int page, Comment comment) {
        List<ReportedComment> rd = comment.getReports();
        int totalPages = Page.getPageCount(rd.size(), PAGE_SIZE);
        page = Math.min(Math.max(page, 1), totalPages);
        return new Page<>(rd.subList((page-1)*PAGE_SIZE, Math.min(rd.size(), page*PAGE_SIZE)), page, totalPages);
    }


}






































