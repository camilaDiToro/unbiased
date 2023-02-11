package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;

import ar.edu.itba.paw.model.admin.ReportDetail;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.exeptions.ImageNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.TextUtils;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.AdminService;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.api.exceptions.InvalidRequestParamsException;
import ar.edu.itba.paw.webapp.api.exceptions.MissingArgumentException;
import ar.edu.itba.paw.webapp.controller.queryParamsValidators.GetNewsFilter;
import ar.edu.itba.paw.webapp.controller.queryParamsValidators.GetNewsParams;
import ar.edu.itba.paw.webapp.dto.*;
import ar.edu.itba.paw.webapp.form.CreateNewsForm;
import ar.edu.itba.paw.webapp.form.ReportNewsForm;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/news")
@Component
public class NewsController {

    private final UserService userService;
    private final NewsService newsService;
    private final SecurityService securityService;


    private final ImageService imageService;
    private final AdminService adminService;


    @Context
    private UriInfo uriInfo;

    @Autowired
    public NewsController(AdminService adminService, UserService userService, NewsService newsService, SecurityService securityService, ImageService imageService) {
        this.userService = userService;
        this.newsService = newsService;
        this.securityService = securityService;
        this.imageService = imageService;
        this.adminService = adminService;
    }

    @GET
    @PreAuthorize("@ownerCheck.canGetSavedNews(#filter, #id)")
    @Produces(value = {CustomMediaType.NEWS_LIST_V1})
    public Response listNews(@QueryParam("page") @DefaultValue("1") final int page,
                             @QueryParam("filter") @DefaultValue("NO_FILTER") final String filter,
                             @QueryParam("search") @DefaultValue("") final String search,
                             @QueryParam("cat") @DefaultValue("ALL") final String category,
                             @QueryParam("order") final String order,
                             @QueryParam("time") @DefaultValue("WEEK") final String time,
                             @QueryParam("id") final Long id) {
        final GetNewsFilter objFilter = GetNewsFilter.fromString(filter);
        final GetNewsParams params = objFilter.validateParams(userService, category, order, time, search, id);
        final Page<News> newsPage = objFilter.getNews(newsService, adminService, page, params);

        if(newsPage.getContent().isEmpty()){
            return Response.noContent().build();
        }

        final List<NewsDto> allNews = newsPage.getContent().stream().map(n -> NewsDto.fromNews(uriInfo, n)).collect(Collectors.toList());
        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<NewsDto>>(allNews) {});
        return ResponseHeadersUtils.pagedResponse(newsPage, responseBuilder, uriInfo);
    }

    @GET
    @Path("/{newsId:[0-9]+}")
    @Produces(value = {CustomMediaType.NEWS_V1})
    public Response getNews(@PathParam("newsId") final long newsId){
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        NewsDto newsDto = NewsDto.fromNews(uriInfo, news);
        return Response.ok(newsDto).build();
    }

    @GET
    @Path("/{newsId:[0-9]+}/reports")
    @Produces(value = {CustomMediaType.NEWS_REPORT_LIST_V1})
    public Response getNewsReportDetail(@PathParam("newsId") final long newsId, @QueryParam("page") @DefaultValue("1") int page){
        Page<ReportDetail> reportedNewsDetail = adminService.getReportedNewsDetail(page, newsId);
        if(reportedNewsDetail.getContent().isEmpty()){
            return Response.noContent().build();
        }
        List<NewsReportDetailDto> reportList = reportedNewsDetail.getContent().stream().map(n -> NewsReportDetailDto.fromReportDetail(uriInfo, n)).collect(Collectors.toList());
        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<NewsReportDetailDto>>(reportList) {});
        return ResponseHeadersUtils.pagedResponse(reportedNewsDetail, responseBuilder, uriInfo);
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @PreAuthorize("@ownerCheck.checkNewsOwnership(#newsId)")
    @Path("/{newsId:[0-9]+}/image")
    public Response updateNewsImage(@PathParam("newsId") long newsId,
                                 @FormDataParam("image") final FormDataBodyPart newsBodyPart,
                                 @FormDataParam("image") byte[] bytes) {
        final String imageType = newsBodyPart.getMediaType().toString();
        newsService.setNewsImage(newsId, bytes, imageType);
        final URI location = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(location).build();
    }

    @POST
    @Path("/{newsId:[0-9]+}/likes")
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response like(@QueryParam("userId") long userId, @PathParam("newsId") long newsId){
        if(newsService.setRating(userId, newsId, Rating.UPVOTE)){
            return Response.ok(SimpleMessageDto.fromString(String.format("The user of id %d liked the article of id %d",
                    userId, newsId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("The user of id %d already liked the article of id %d",
                userId, newsId))).build();
    }

    @POST
    @Path("/{newsId:[0-9]+}/dislikes")
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response dislike(@QueryParam("userId") long userId, @PathParam("newsId") long newsId){
        if(newsService.setRating(userId, newsId, Rating.DOWNVOTE)){
            return Response.ok(SimpleMessageDto.fromString(String.format("The user of id %d disliked the article of id %d",
                    userId, newsId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("The user of id %d already disliked the article of id %d",
                userId, newsId))).build();
    }

    @DELETE
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @Path("/{newsId:[0-9]+}")
    @PreAuthorize("@ownerCheck.canDeleteNews(#newsId)")
    public Response delete(@PathParam("newsId")  long newsId){
        Optional<News> news = newsService.getById(newsId);
        if(!news.isPresent()){
            return Response.noContent().build();
        }
        newsService.deleteNews(news.get());
        return Response.ok(SimpleMessageDto.fromString(String.format("The article of id %d has been deleted", newsId))).build();
    }

    @POST
    @Produces({CustomMediaType.NEWS_REPORT_V1})
    @Path("/{newsId:[0-9]+}/reports")
    @PreAuthorize("@ownerCheck.userMatches(#reportForm.userId)")
    public Response report(@PathParam("newsId")  long newsId, @Valid final ReportNewsForm reportForm){
        if(reportForm == null){
            throw new MissingArgumentException("Missing body, userId and reporting reason required");
        }
        ReportDetail reportDetail = adminService.reportNews(reportForm.getUserId(), newsId, ReportReason.getByValue(reportForm.getReason()));
        if(reportDetail == null){
            throw new InvalidRequestParamsException("Each user can report an article just once");
        }
        return Response.ok(NewsReportDetailDto.fromReportDetail(uriInfo, reportDetail)).build();
    }

    @DELETE
    @Path("/{newsId:[0-9]+}/likes")
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response removeLike(@QueryParam("userId")  long userId, @PathParam("newsId")  long newsId){
        if(newsService.setRating(userId, newsId, Rating.NO_RATING)){
            return Response.ok(SimpleMessageDto.fromString(String.format("The like of the user of id %d to the article of id %d has been removed",
                    userId, newsId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("The user of id %d did not have an interaction with the article of id %d",
                userId, newsId))).build();
    }

    @DELETE
    @Path("/{newsId:[0-9]+}/dislikes")
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response removeDislike(@QueryParam("userId")  long userId, @PathParam("newsId")  long newsId){
        if(newsService.setRating(userId, newsId, Rating.NO_RATING)){
            return Response.ok(SimpleMessageDto.fromString(String.format("The dislike of the user of id %d to the article of id %d has been removed",
                    userId, newsId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("The user of id %d did not have an interaction with the article of id %d",
                userId, newsId))).build();
    }

    @DELETE
    @Path("/{newsId:[0-9]+}/bookmarks")
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response removeBookmark(@QueryParam("userId") long userId, @PathParam("newsId") long newsId){
        if (!newsService.isSavedByUser(newsId, userId)) {
            return Response.ok(String.format("The user of id %d did not have the article of id %d saved",userId, newsId)).build();
        }
        newsService.unsaveNews(userId, newsId);
        return Response.ok(String.format("The user of id %d unsaved the article of id %d",userId, newsId)).build();
    }

    @POST
    @Path("/{newsId:[0-9]+}/bookmarks")
    @Produces({CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response save(@QueryParam("userId") long userId, @PathParam("newsId") long newsId){
        if (newsService.isSavedByUser(newsId, userId)) {
            return Response.ok(String.format("The user of id %d had already saved the article of id %d",userId, newsId)).build();
        }
        newsService.saveNews(userId, newsId);
        return Response.ok(String.format("The user of id %d saved the article of id %d",userId, newsId)).build();
    }

    @Consumes({CustomMediaType.NEWS_V1})
    @POST
    public Response createNews(@Valid final CreateNewsForm createNewsFrom) {
        final User user = securityService.getCurrentUser().get();
        final News.NewsBuilder newsBuilder = new News.NewsBuilder(user, TextUtils.convertMarkdownToHTML(createNewsFrom.getBody()), createNewsFrom.getTitle(), createNewsFrom.getSubtitle());
        List<Category> categories;
        if (createNewsFrom.getCategories() == null) {
            categories = new ArrayList<>();
        } else {
            categories = Arrays.stream(createNewsFrom.getCategories()).map(Category::getByCode).collect(Collectors.toList());
        }
        final News news = newsService.create(newsBuilder, categories);

        final URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(news.getNewsId())).build();
        return Response.created(location).build();
    }

    @GET
    @Path("/{newsId:[0-9]+}/image")
    public Response profileImage(@PathParam("newsId") final long newsId,
                                 @Context javax.ws.rs.core.Request request) {
        final News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        final Optional<Long> maybeImageId = news.getImageId();

        if (!maybeImageId.isPresent()){
            return Response.noContent().build();
        }

        final Image image = imageService.getImageById(maybeImageId.get()).orElseThrow(()-> new ImageNotFoundException(maybeImageId.get()));
        return ResponseHeadersUtils.conditionalCacheImageResponse(image, request);
    }

}

