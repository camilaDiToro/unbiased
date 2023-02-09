package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorizedException;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.AdminService;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.CommentDto;
import ar.edu.itba.paw.webapp.dto.NewsReportDetailDto;
import ar.edu.itba.paw.webapp.dto.SimpleMessageDto;
import ar.edu.itba.paw.webapp.form.CommentNewsForm;
import ar.edu.itba.paw.webapp.form.ReportNewsForm;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api/comments")
@Component
public class CommentController {

    private final NewsService newsService;
    private final SecurityService securityService;
    private final CommentService commentService;
    private final UserService userService;
    private final AdminService adminService;

    @Context
    private UriInfo uriInfo;

    public CommentController(AdminService adminService, UserService userService, NewsService newsService, SecurityService securityService, CommentService commentService) {
        this.newsService = newsService;
        this.commentService = commentService;
        this.securityService = securityService;
        this.userService = userService;
        this.adminService = adminService;
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
            @QueryParam("reportedBy") @DefaultValue("-1") Long reportedBy,
            @QueryParam("reportOrder") @DefaultValue("REP_COUNT_DESC") String reportOrder) {
        if (reportedBy>0) {
            final User user = userService.getUserById(reportedBy).orElseThrow(()-> new UserNotFoundException(reportedBy));
            List<Comment> comments = adminService.getReportedByUserComments(user);
            if (comments.isEmpty())
                return Response.noContent().build();
            return Response.ok(new GenericEntity<List<CommentDto>>(comments.stream().map(n -> {
                return CommentDto.fromComment(uriInfo, n);
            }).collect(Collectors.toList())) {
            }).build();
        } else
        if (likedBy > 0 || dislikedBy > 0) {
            long userId = likedBy > 0 ? likedBy : dislikedBy;
            User user = userService.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
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
        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<CommentDto>>(comments) {});
        return PagingUtils.pagedResponse(commentPage, responseBuilder, uriInfo);
    }

    @GET
    @Path("/{commentId:[0-9]+}/reports")
    @Produces(value = {CustomMediaType.COMMENT_REPORT_DETAIL_LIST_V1})
    public Response getNewsReportDetail(@PathParam("commentId") final long commentId){
        Comment comment = commentService.getById(commentId).orElseThrow(()-> new CommentNotFoundException(commentId));
        List<NewsReportDetailDto> reportList = comment.getReports().stream().map(d -> NewsReportDetailDto.fromReportedComment(uriInfo, d)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<NewsReportDetailDto>>(reportList) {}).build();
    }

    @POST
    @Consumes(value = {CustomMediaType.COMMENT_V1})
    @Produces(value = {CustomMediaType.COMMENT_V1})
    public Response postComment(
            @QueryParam("newsId") final long newsId,
            @Valid CommentNewsForm form){
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorizedException::new);
        final CommentDto commentDto = CommentDto.fromComment(uriInfo, newsService.addComment(currentUser, newsId, form.getComment()));
        return Response.created(commentDto.getSelf()).entity(commentDto).build();
    }

    @PUT
    @Path("/{commentId:[0-9]+}/reports/{userId:[0-9]+}")
    @Consumes(value = {CustomMediaType.COMMENT_REPORT_DETAIL_V1})
    public Response report(@PathParam("userId") final long userId, @PathParam("commentId") final long commentId, @Valid final ReportNewsForm reportForm){
        User user = userService.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        //TODO: Check if getting this object here is necessary
        Comment comment = commentService.getById(commentId).orElseThrow(()-> new CommentNotFoundException(commentId));
        adminService.reportComment(user, commentId, ReportReason.getByValue(reportForm.getReason()));
        return Response.ok().build();
    }







    @PUT
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @Path("/{commentId:[0-9]+}/likes")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response like(@PathParam("commentId") long commentId, @QueryParam("userId") long userId){
        commentService.setCommentRating(userId, commentId, Rating.UPVOTE);
        return Response.ok(SimpleMessageDto.fromString(String.format("The user of id %d liked the comment of id %d",
                userId, commentId))).build();
    }

    @DELETE
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @Path("/{commentId:[0-9]+}/likes")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response removeLike(@PathParam("commentId") long commentId, @QueryParam("userId") long userId){
        if(commentService.setCommentRating(userId, commentId, Rating.NO_RATING)) {
            return Response.ok(SimpleMessageDto.fromString(String.format("The like of the user of id %d to the comment of id %d has been removed",
                    userId, commentId))).build();
        }
        return Response.noContent().build();
    }

    @PUT
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @Path("/{commentId:[0-9]+}/dislikes")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response dislike(@PathParam("commentId") long commentId, @QueryParam("userId") long userId){
        commentService.setCommentRating(userId, commentId, Rating.DOWNVOTE);
        return Response.ok(SimpleMessageDto.fromString(String.format("The user of id %d disliked the comment of id %d",
                userId, commentId))).build();
    }

    @DELETE
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @Path("/{commentId:[0-9]+}/dislikes")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response removeDislike(@PathParam("commentId") long commentId, @QueryParam("userId") long userId){
        if(commentService.setCommentRating(userId, commentId, Rating.NO_RATING)) {
            return Response.ok(SimpleMessageDto.fromString(String.format("The dislike of the user of id %d to the comment of id %d has been removed",
                    userId, commentId))).build();
        }
        return Response.noContent().build();
    }

    @PUT //Logical deletion
    @Path("/{commentId:[0-9]+}")
    @PreAuthorize("@ownerCheck.canDeleteComment(#commentId)")
    public Response delete(@PathParam("commentId") final long commentId){
        Comment comment = commentService.getById(commentId).orElseThrow(()-> new CommentNotFoundException(commentId));
        if(comment.getDeleted()){
            return Response.ok(SimpleMessageDto.fromString(String.format("The comment of id %d had already been deleted",
                    commentId))).build();
        }
        newsService.deleteComment(commentId);
        return Response.ok(SimpleMessageDto.fromString(String.format("Comment of id %d successfully deleted",
                commentId))).build();
    }
}
