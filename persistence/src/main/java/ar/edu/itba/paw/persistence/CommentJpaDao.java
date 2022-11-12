package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
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
import javax.persistence.TypedQuery;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class CommentJpaDao implements CommentDao{

    @PersistenceContext
    private EntityManager entityManager;

    private static final int COMMENT_PAGE_SIZE = 5;
    private static final int PAGE_SIZE = 5;
    private static final Logger LOGGER = LoggerFactory.getLogger(CommentJpaDao.class);

    @Override
    public Optional<Comment> getCommentById(long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public void addComment(User user, News news, String comment) {
        Comment commentObj = new Comment(user, comment, news);
        entityManager.persist(commentObj);
    }

    @Override
    public void deleteComment(long commentId) {
        Optional<Comment> mayBeComment = entityManager.createQuery("FROM Comment c WHERE c.id = :id", Comment.class).setParameter("id", commentId).getResultList().stream().findFirst();
        mayBeComment.ifPresent(Comment::delete);
    }

    @Override
    public Page<Comment> getNewComments(long newsId, int page) {
        int totalPages = getTotalPagesComments(newsId);
        page = Math.min(page, totalPages);

        Query query = entityManager.createNativeQuery("SELECT f.id from comments AS f WHERE news_id = :newsId ORDER BY commented_date DESC LIMIT :pageSize OFFSET :offset")
                .setParameter("newsId", newsId);
        List<Comment> comments = getCommentsOfPage(query, page, COMMENT_PAGE_SIZE);

        return new Page<>(comments, page, totalPages);
    }

    @Override
    public Page<Comment> getTopComments(long newsId, int page) {
        int totalPages = getTotalPagesComments(newsId);
        page = Math.min(page, totalPages);

        Query query = entityManager.createNativeQuery("WITH net_upvotes_by_comment as (SELECT comments.id, SUM( CASE WHEN upvote IS NULL THEN 0\n" +
                        "WHEN upvote THEN 1 ELSE -1 END) upvotes FROM comment_upvotes RIGHT JOIN comments ON comments.id = comment_id AND news_id = :newsId \n" +
                        "GROUP BY comments.id) SELECT id from net_upvotes_by_comment ORDER BY upvotes DESC LIMIT :pageSize OFFSET :offset")
                .setParameter("newsId", newsId);
        List<Comment> comments = getCommentsOfPage(query, page, COMMENT_PAGE_SIZE);

        return new Page<>(comments, page, totalPages);
    }

    private int getTotalPagesComments(long newsId) {
        long count = entityManager.createQuery("SELECT COUNT(c) from Comment c WHERE c.news.newsId = :newsId", Long.class)
                .setParameter("newsId", newsId).getSingleResult();
        return Page.getPageCount(count, COMMENT_PAGE_SIZE);
    }

    private List<Comment> getCommentsOfPage(Query query,int page, int pageSize) {
        @SuppressWarnings("unchecked")
        List<Long> ids = (List<Long>) query.setParameter("pageSize", pageSize)
                .setParameter("offset", pageSize*(page-1))
                .getResultList().stream().map(o -> ((Number)o).longValue()).collect(Collectors.toList());

        if (ids.isEmpty())
            return new ArrayList<>();

        final TypedQuery<Comment> typedQuery = entityManager.createQuery("SELECT f from Comment f WHERE f.id IN :ids",Comment.class)
                .setParameter("ids", ids);

        List<Comment> comments = typedQuery.getResultList();
        Map<Long, Comment> map = new HashMap<>();

        for (Comment comment1 : comments) {
            map.put(comment1.getId(), comment1);
        }
        comments = ids.stream().map(id -> map.get(id)).collect(Collectors.toList());

        return comments;
    }

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
