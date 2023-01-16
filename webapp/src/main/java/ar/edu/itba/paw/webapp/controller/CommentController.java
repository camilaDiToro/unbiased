package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.persistence.CommentDao;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.CommentDto;
import ar.edu.itba.paw.webapp.form.CommentNewsForm;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/news/{newsId:[0-9]+}/comments")
@Component
public class CommentController {

    private final NewsService newsService;
    private final SecurityService securityService;

    @Context
    private UriInfo uriInfo;

    public CommentController(NewsService newsService, SecurityService securityService) {
        this.newsService = newsService;
        this.securityService = securityService;
    }

    @GET
    @Produces(value = {CustomMediaType.COMMENT_LIST_V1})
    public Response listComments(
            @PathParam("newsId") final long newsId,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("reported") @DefaultValue("null") final Boolean reported,
            @QueryParam("order") @DefaultValue("TOP") String orderBy,
            @QueryParam("reportOrder") @DefaultValue("REP_COUNT_DESC") String reportOrder) {

        final Page<Comment> commentPage = newsService.getComments(newsId, page, NewsOrder.getByValue(orderBy), reported, ReportOrder.getByValue(reportOrder));

        if(commentPage.getContent().isEmpty()){
            return Response.noContent().build();
        }

        final List<CommentDto> comments = commentPage.getContent().stream().map(c -> CommentDto.fromComment(uriInfo, c, newsId)).collect(Collectors.toList());

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
    public Response listUsers(
            @PathParam("newsId") final long newsId,
            @Valid CommentNewsForm form){
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        final CommentDto commentDto = CommentDto.fromComment(uriInfo, newsService.addComment(currentUser, newsId, form.getComment()), newsId);
        return Response.created(commentDto.getSelf()).entity(commentDto).build();
    }

    /*@DELETE
    @Path("/{commentId:[0-9]+}")
    public Response deleteComment(@PathParam("newsId") final long newsId, @PathParam("commentId") final long commentId) {
        newsService.deleteComment(commentId);
    }*/

}
