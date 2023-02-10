package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.admin.ReportedComment;
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
import ar.edu.itba.paw.webapp.api.exceptions.InvalidRequestParamsException;
import ar.edu.itba.paw.webapp.api.exceptions.MissingArgumentException;
import ar.edu.itba.paw.webapp.controller.queryParamsValidators.GetCommentsFilter;
import ar.edu.itba.paw.webapp.controller.queryParamsValidators.GetCommentsParams;
import ar.edu.itba.paw.webapp.dto.*;
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
            @QueryParam("filter") @DefaultValue("NEWS_COMMENTS") String filter,
            @QueryParam("page") @DefaultValue("1") int page,
            @QueryParam("id") Long id,
            @QueryParam("order") String order) {

        final GetCommentsFilter commentsFilter = GetCommentsFilter.fromString(filter);
        final GetCommentsParams params = commentsFilter.validateParams(userService, order, id);
        final Page<Comment> commentPage = commentsFilter.getComments(commentService, adminService, page, params);

        if(commentPage.getContent().isEmpty()){
            return Response.noContent().build();
        }

        final List<CommentDto> comments = commentPage.getContent().stream().map(c -> CommentDto.fromComment(uriInfo, c)).collect(Collectors.toList());
        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<CommentDto>>(comments) {});
        return PagingUtils.pagedResponse(commentPage, responseBuilder, uriInfo);
    }

    @POST
    @Consumes(value = {CustomMediaType.COMMENT_V1})
    @Produces(value = {CustomMediaType.COMMENT_V1})
    public Response postComment(
            @Valid CommentNewsForm form){
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorizedException::new);
        final CommentDto commentDto = CommentDto.fromComment(uriInfo, newsService.addComment(currentUser, form.getNewsId(), form.getComment()));
        return Response.created(commentDto.getSelf()).entity(commentDto).build();
    }

    @DELETE
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

    @POST
    @Path("/{commentId:[0-9]+}/reports")
    @PreAuthorize("@ownerCheck.userMatches(#reportForm)")
    @Consumes(value = {CustomMediaType.COMMENT_REPORT_DETAIL_V1})
    public Response report( @PathParam("commentId") final long commentId, @Valid final ReportNewsForm reportForm){
        ReportedComment reportedComment = adminService.reportComment(reportForm.getUserId(), commentId, ReportReason.getByValue(reportForm.getReason()));
        if(reportedComment == null){
            throw new InvalidRequestParamsException("Each user can report a comment just once");
        }
        return Response.ok(CommentReportDetailsDto.fromReportedComment(uriInfo,reportedComment)).build();
    }

    @GET
    @Path("/{commentId:[0-9]+}/reports")
    @Produces(value = {CustomMediaType.COMMENT_REPORT_DETAIL_LIST_V1})
    public Response getNewsReportDetail(@PathParam("commentId") long commentId, @QueryParam("page") @DefaultValue("1") int page){
        Page<ReportedComment> reportedCommentPage = adminService.getReportedCommentDetail(page, commentId);
        if(reportedCommentPage.getContent().isEmpty()){
            return Response.noContent().build();
        }
        List<CommentReportDetailsDto> reportList = reportedCommentPage.getContent().stream().map(n -> CommentReportDetailsDto.fromReportedComment(uriInfo, n)).collect(Collectors.toList());
        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<CommentReportDetailsDto>>(reportList) {});
        return PagingUtils.pagedResponse(reportedCommentPage, responseBuilder, uriInfo);
    }

    @POST
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
}
