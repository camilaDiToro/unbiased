package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.CommentDto;
import ar.edu.itba.paw.webapp.form.CommentNewsForm;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/comments")
@Component
public class CommentController {

    private final NewsService newsService;
    private final SecurityService securityService;

    private final CommentService commentService;

    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    public CommentController(UserService userService, NewsService newsService, SecurityService securityService, CommentService commentService) {
        this.newsService = newsService;
        this.commentService = commentService;
        this.securityService = securityService;
        this.userService = userService;
    }

    @GET
    @Produces(value = {CustomMediaType.COMMENT_LIST_V1})
    public Response listComments(
            @QueryParam("newsId") @DefaultValue("-1") final Long newsId,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("reported") final boolean reported,
            @QueryParam("order") @DefaultValue("TOP") String orderBy,
            @QueryParam("likedBy") @DefaultValue("-1") Long likedBy,
            @QueryParam("dislikedBy") @DefaultValue("-1") Long dislikedBy,
            @QueryParam("reportOrder") @DefaultValue("REP_COUNT_DESC") String reportOrder) {

        if (likedBy > 0 || dislikedBy > 0) {
            long userId = likedBy > 0 ? likedBy : dislikedBy;
            User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
            List<Comment> comments = likedBy > 0 ? commentService.getCommentsUpvotedByUser(user) : commentService.getCommentsDownvotedByUser(user);
            final List<CommentDto> dtoComments = comments.stream().map(c -> CommentDto.fromComment(uriInfo, c)).collect(Collectors.toList());
            if (comments.isEmpty()) {
                return Response.noContent().build();
            }
            return Response.ok(new GenericEntity<List<CommentDto>>(dtoComments) {
            }).build();
        }


        final Page<Comment> commentPage = newsService.getComments(newsId, page, NewsOrder.getByValue(orderBy), reported, ReportOrder.getByValue(reportOrder));

        if(commentPage.getContent().isEmpty()){
            return Response.noContent().build();
        }

        final List<CommentDto> comments = commentPage.getContent().stream().map(c -> CommentDto.fromComment(uriInfo, c)).collect(Collectors.toList());

        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<CommentDto>>(comments) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", commentPage.getTotalPages()).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");

        if(page != 1){
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
        }

        if(page != commentPage.getTotalPages()){
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
        }

        return responseBuilder.build();

    }

    @POST
    @Consumes(value = {MediaType.APPLICATION_JSON})
    @Produces(value = {CustomMediaType.COMMENT_V1})
    public Response postComment(
            @QueryParam("newsId") final long newsId,
            @Valid CommentNewsForm form){
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        final CommentDto commentDto = CommentDto.fromComment(uriInfo, newsService.addComment(currentUser, newsId, form.getComment()));
        return Response.created(commentDto.getSelf()).entity(commentDto).build();
    }

    @PUT
    @Path("/{commentId:[0-9]+}/likes/{userId:[0-9]+}")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response like(@PathParam("userId") final long userId, @PathParam("commentId") final long commentId){
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentService.getById(commentId).orElseThrow(CommentNotFoundException::new);
        commentService.setCommentRating(user, comment, Rating.UPVOTE);
        return Response.ok().build();
    }

    @PUT
    @Path("/{commentId:[0-9]+}/dislikes/{userId:[0-9]+}")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response dislike(@PathParam("userId") final long userId, @PathParam("commentId") final long commentId){
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentService.getById(commentId).orElseThrow(CommentNotFoundException::new);
        commentService.setCommentRating(user, comment, Rating.DOWNVOTE);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{commentId:[0-9]+}/likes/{userId:[0-9]+}")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response removeLike(@PathParam("userId") final long userId, @PathParam("commentId") final long commentId){
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentService.getById(commentId).orElseThrow(CommentNotFoundException::new);
        commentService.setCommentRating(user, comment, Rating.NO_RATING);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{commentId:[0-9]+}/dislikes/{userId:[0-9]+}")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response removeDislike(@PathParam("userId") final long userId, @PathParam("commentId") final long commentId){
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        Comment comment = commentService.getById(commentId).orElseThrow(CommentNotFoundException::new);
        commentService.setCommentRating(user, comment, Rating.NO_RATING);
        return Response.ok().build();
    }

    /*@DELETE
    @Path("/{commentId:[0-9]+}")
    public Response deleteComment(@PathParam("newsId") final long newsId, @PathParam("commentId") final long commentId) {
        newsService.deleteComment(commentId);
    }*/

}
