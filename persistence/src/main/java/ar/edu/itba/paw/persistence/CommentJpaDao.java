package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.User;
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
}
