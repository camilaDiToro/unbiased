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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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
    public Comment addComment(User user, News news, String comment) {
        final Comment commentObj = new Comment(user, comment, news);
        entityManager.persist(commentObj);
        return commentObj;
    }

    @Override
    public void deleteComment(long commentId) {
        final Optional<Comment> mayBeComment = entityManager.createQuery("FROM Comment c WHERE c.id = :id", Comment.class).setParameter("id", commentId).getResultList().stream().findFirst();
        mayBeComment.ifPresent(Comment::delete);
    }

    @Override
    public Page<Comment> getNewComments(long newsId, int page) {
        final int totalPages = getTotalPagesComments(newsId);
        page = Math.min(page, totalPages);

        final Query query = entityManager.createNativeQuery("SELECT id from comments AS f WHERE news_id = :newsId ORDER BY commented_date DESC LIMIT :pageSize OFFSET :offset")
                .setParameter("newsId", newsId);
        return getCommentsOfPage(query, page, COMMENT_PAGE_SIZE, totalPages);
    }

    @Override
    public Page<Comment> getTopComments(long newsId, int page) {
        page = Math.max(1,page);
        final int totalPages = getTotalPagesComments(newsId);
        page = Math.min(page, totalPages);

        final Query query = entityManager.createNativeQuery("WITH net_upvotes_by_comment as (SELECT comments.id, SUM( CASE WHEN upvote IS NULL THEN 0\n" +
                        "WHEN upvote THEN 1 ELSE -1 END) upvotes FROM comment_upvotes RIGHT JOIN comments ON comments.id = comment_id WHERE news_id = :newsId \n" +
                        "GROUP BY comments.id) SELECT id from net_upvotes_by_comment ORDER BY upvotes DESC LIMIT :pageSize OFFSET :offset")
                .setParameter("newsId", newsId);
        return getCommentsOfPage(query, page, COMMENT_PAGE_SIZE, totalPages);
    }

    @Override
    public Page<Comment> getReportedByUserComments(int page, long userId) {

        page = Math.max(1,page);
        final int totalPages = getTotalPagesReportedByUserComments(userId);
        page = Math.min(page, totalPages);

        final Query query = entityManager.createNativeQuery(
                "SELECT comment_id FROM comment_report JOIN comments n ON n.id = comment_report.comment_id WHERE deleted = false and comment_report.user_id = :userId LIMIT :pageSize OFFSET :offset")
                .setParameter("userId", userId);
        return getCommentsOfPage(query, page, COMMENT_PAGE_SIZE, totalPages);
    }

    @Override
    public Page<Comment> getCommentsUpvotedByUser(int page, User user) {
        page = Math.max(1,page);
        final int totalPages = getTotalPagesUpvotedByUserComments(user.getId());
        page = Math.min(page, totalPages);

        final Query query = entityManager.createNativeQuery(
                "SELECT comment_id FROM comment_upvotes WHERE user_id = :userId AND upvote = true LIMIT :pageSize OFFSET :offset")
                .setParameter("userId", user.getUserId());

        return getCommentsOfPage(query, page, COMMENT_PAGE_SIZE, totalPages);
    }

    @Override
    public Page<Comment> getCommentsDownvotedByUser(int page, User user) {

        page = Math.max(1,page);
        final int totalPages = getTotalPagesDownvotedByUserComments(user.getId());
        page = Math.min(page, totalPages);

        final Query query = entityManager.createNativeQuery(
                "SELECT comment_id FROM comment_upvotes WHERE user_id = :userId AND upvote = false LIMIT :pageSize OFFSET :offset")
                .setParameter("userId", user.getUserId());
        return getCommentsOfPage(query, page, COMMENT_PAGE_SIZE, totalPages);
    }

    private int getTotalPagesDownvotedByUserComments(long userId) {
        final int count =  ((Number) entityManager.createNativeQuery(
                        "SELECT count(distinct comment_id) FROM comment_upvotes WHERE user_id = :userId AND upvote = false")
                .setParameter("userId", userId)
                .getSingleResult()).intValue();
        return Page.getPageCount(count, COMMENT_PAGE_SIZE);
    }

    private int getTotalPagesUpvotedByUserComments(long userId) {
        final int count =  ((Number) entityManager.createNativeQuery(
                        "SELECT count(distinct comment_id) FROM comment_upvotes WHERE user_id = :userId AND upvote = true")
                .setParameter("userId", userId)
                .getSingleResult()).intValue();
        return Page.getPageCount(count, COMMENT_PAGE_SIZE);
    }

    private int getTotalPagesReportedByUserComments(long userId) {
        final int count =  ((Number) entityManager.createNativeQuery(
                "SELECT count(distinct comment_id) FROM comment_report JOIN comments n ON n.id = comment_report.comment_id WHERE deleted = false and comment_report.user_id = :userId")
                .setParameter("userId", userId)
                .getSingleResult()).intValue();
        return Page.getPageCount(count, COMMENT_PAGE_SIZE);
    }

    private int getTotalPagesComments(long newsId) {
        final long count = entityManager.createQuery("SELECT COUNT(c) from Comment c WHERE c.news.newsId = :newsId", Long.class)
                .setParameter("newsId", newsId).getSingleResult();
        return Page.getPageCount(count, COMMENT_PAGE_SIZE);
    }

    private Page<Comment> getCommentsOfPage(Query query,int page, int pageSize, int totalPages) {
        query =  query.setParameter("pageSize", pageSize)
                .setParameter("offset", pageSize*(page-1));

        final TypedQuery<Comment> typedQuery = entityManager.createQuery("SELECT f from Comment f WHERE f.id IN :ids",Comment.class);

        return JpaUtils.getPage(page, totalPages, query, typedQuery, Comment::getId);
    }

    @Override
    public ReportedComment reportComment(Comment comment, User reporter, ReportReason reportReason) {
        final ReportedComment reportedComment = new ReportedComment(comment, reporter, reportReason);
        entityManager.persist(reportedComment);
        LOGGER.debug("Comment from {} with id {} reported. The reason is {}", comment.getUser(), comment.getId(), reportReason.getDescription());
        return reportedComment;
    }

    @Override
    public Page<Comment> getReportedComment(int page, ReportOrder reportOrder) {
        page = Math.min(page,1);
        final int totalPages = getReportedCommentPageCount();
        page = Math.min(page, totalPages);

        final Query idsQuery = entityManager.createNativeQuery(
                        "SELECT comment_id FROM comment_report JOIN comments n ON n.id = comment_report.comment_id WHERE deleted = false GROUP BY comment_id ORDER BY "
                                + reportOrder.getQuery() +" LIMIT :pageSize OFFSET :offset");

       return getCommentsOfPage(idsQuery, page, PAGE_SIZE, totalPages);
    }

    private int getReportedCommentPageCount() {
        final int count =  ((Number) entityManager.createNativeQuery(
                "SELECT count(distinct comment_id) FROM comment_report JOIN comments n ON n.id = comment_report.comment_id WHERE deleted = false").getSingleResult()).intValue();
        return Page.getPageCount(count, PAGE_SIZE);
    }

    private int getReportedCommentDetailPageCount(long commentId) {
        final int count =  ((Number) entityManager.createNativeQuery(
                "SELECT count(distinct comment_id) FROM comment_report JOIN comments n ON n.id = comment_report.comment_id WHERE deleted = false and comment_report.comment_id = :id")
                .setParameter("id",commentId)
                .getSingleResult()).intValue();
        return Page.getPageCount(count, PAGE_SIZE);
    }


    @Override
    public Page<ReportedComment> getReportedCommentDetail(int page, long commentId) {
        page = Math.max(page,1);
        final int totalPages = getReportedCommentDetailPageCount(commentId);
        page = Math.min(page, totalPages);
        final Query idsQuery = entityManager.createNativeQuery(
                        "SELECT comment_report.id FROM comment_report JOIN comments n ON n.id = comment_report.comment_id WHERE deleted = false and comment_report.comment_id=:id" +
                                " LIMIT :pageSize OFFSET :offset")
                .setParameter("pageSize", PAGE_SIZE)
                .setParameter("offset", PAGE_SIZE*(page-1))
                .setParameter("id",commentId);

        final TypedQuery<ReportedComment> objectQuery = entityManager.createQuery("SELECT n from ReportedComment n WHERE n.id IN :ids " , ReportedComment.class);

        return JpaUtils.getPage(page, totalPages, idsQuery, objectQuery, ReportedComment::getId);
    }

    @Override
    public boolean isReportedByUser(long commentId, long userId) {
        int value = ((Number)entityManager.createNativeQuery("SELECT count(*) FROM comment_report WHERE user_id = :userId and comment_id = :commentId")
                .setParameter("userId", userId).setParameter("commentId", commentId)
                .getSingleResult()).intValue();
        return value >= 1;
    }

}
