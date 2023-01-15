package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.news.Comment;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.CommentDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.stream.Collectors;

@Path("/api/news/{newsId:[0-9]+}/comments")
@Component
public class CommentController {

    private final NewsService newsService;

    @Context
    private UriInfo uriInfo;

    public CommentController(NewsService newsService) {
        this.newsService = newsService;
    }


    @GET
    @Produces(value = {CustomMediaType.COMMENT_LIST_V1})
    public Response listUsers(
            @PathParam("newsId") final long newsId,
            @QueryParam("page") @DefaultValue("1") final int page,
            @QueryParam("reported") @DefaultValue("null") final Boolean reported,
            @QueryParam("order") @DefaultValue("TOP") String orderBy) {

        final Page<Comment> commentPage = newsService.getComments(newsId, page, NewsOrder.getByValue(orderBy));

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
}
