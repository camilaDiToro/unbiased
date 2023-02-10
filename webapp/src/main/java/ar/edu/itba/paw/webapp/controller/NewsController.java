package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.Rating;

import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.admin.ReportReason;
import ar.edu.itba.paw.model.exeptions.ImageNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TextUtils;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.AdminService;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.NewsDto;
import ar.edu.itba.paw.webapp.dto.ReportDetailDto;
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
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.io.ByteArrayInputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Path("/api/news")
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
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response listNews(@QueryParam("page") @DefaultValue("1") final int page, @QueryParam("search") @DefaultValue("") final String search,
                             @QueryParam("cat") @DefaultValue("ALL") final String category,
                             @QueryParam("order") @DefaultValue("TOP") final String order,
                             @QueryParam("time") @DefaultValue("WEEK") final String time,
                             @QueryParam("likedBy") @DefaultValue("-1") final long  likedBy,
                             @QueryParam("dislikedBy") @DefaultValue("-1") final long  dislikedBy,
                             @QueryParam("pinnedBy") @DefaultValue("-1") final long pinnedBy,
                             @QueryParam("savedBy") @DefaultValue("-1") final long  savedBy,
                             @QueryParam("reportedBy") @DefaultValue("-1") final long reportedBy,
                             @QueryParam("reported") final boolean reported,
                             @QueryParam("reportOrder") @DefaultValue("REP_COUNT_DESC") String reportOrder,
                             @QueryParam("publishedBy") @DefaultValue("-1") final long  publishedBy) {
        Category categoryObj = Category.getByValue(category);
        NewsOrder orderObj = NewsOrder.getByValue(order);
        TimeConstraint timeObj = TimeConstraint.getByValue(time);
        final Page<News> newsPage;
        final long uniqueParamCount = Arrays.stream(new long[]{likedBy, dislikedBy, savedBy, publishedBy}).map(s -> s > 0 ? 1 : 0).reduce(Long::sum).getAsLong();

        if (uniqueParamCount > 1) {
            // throw new ...
        }
        if (pinnedBy>0) {
            final User user = userService.getUserById(pinnedBy).orElseThrow(()-> new UserNotFoundException(pinnedBy));
            Optional<News> news = newsService.getPinnedByUserNews(user);
            if (!news.isPresent())
                return Response.noContent().build();
            return Response.ok(new GenericEntity<List<NewsDto>>(Stream.of(news.get()).map(n -> {
                return NewsDto.fromNews(uriInfo, n);
            }).collect(Collectors.toList())) {}).build();
        } else
        if (reportedBy>0) {
            final User user = userService.getUserById(reportedBy).orElseThrow(()-> new UserNotFoundException(reportedBy));
            List<News> news = adminService.getReportedByUserNews(user);
            if (news.isEmpty())
                return Response.noContent().build();
            return Response.ok(new GenericEntity<List<NewsDto>>(news.stream().map(n -> {
                return NewsDto.fromNews(uriInfo, n);
            }).collect(Collectors.toList())) {}).build();
        } else if (reported) {
            final ReportOrder reportOrderObj = ReportOrder.getByValue(reportOrder);
            newsPage = adminService.getReportedNews(page, reportOrderObj);
        } else if (likedBy > 0) {
            final ProfileCategory catObject = ProfileCategory.UPVOTED;
            final User profileUser = userService.getUserById(likedBy).orElseThrow(()-> new UserNotFoundException(likedBy));
            newsPage = newsService.getNewsForUserProfile(Optional.empty(), page, orderObj, profileUser, catObject);
        } else if (dislikedBy > 0)  {
            final ProfileCategory catObject = ProfileCategory.DOWNVOTED;
            final User profileUser = userService.getUserById(dislikedBy).orElseThrow(()-> new UserNotFoundException(dislikedBy));
            newsPage = newsService.getNewsForUserProfile(Optional.empty(), page, orderObj, profileUser, catObject);
        } else if (publishedBy > 0) {
            final ProfileCategory catObject = ProfileCategory.MY_POSTS;
            final User profileUser = userService.getUserById(publishedBy).orElseThrow(()-> new UserNotFoundException(publishedBy));
            newsPage = newsService.getNewsForUserProfile(Optional.empty(), page, orderObj, profileUser, catObject);
        } else if (savedBy > 0) {
            final ProfileCategory catObject = ProfileCategory.SAVED;
            final User profileUser = userService.getUserById(savedBy).orElseThrow(()-> new UserNotFoundException(savedBy));
            final Optional<User> user = securityService.getCurrentUser();

            newsPage = newsService.getNewsForUserProfile(user, page, orderObj, profileUser, catObject);
        }
        else {
            newsPage = newsService.getNews(Optional.empty(), page,categoryObj, orderObj, timeObj, search);
        }


        if(newsPage.getContent().isEmpty()){
            return Response.noContent().build();
        }

        final List<NewsDto> allNews = newsPage.getContent().stream().map(n -> {

            return NewsDto.fromNews(uriInfo, n);
        }).collect(Collectors.toList());

        final MultivaluedMap<String, String> queryParams = uriInfo.getQueryParameters();
        final UriBuilder pathBuilder = uriInfo.getAbsolutePathBuilder();

        queryParams.forEach((key, value) -> pathBuilder.queryParam(key, value.get(0)));

        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<NewsDto>>(allNews) {})
                .link(pathBuilder.clone().replaceQueryParam("page", newsPage.getTotalPages()).build(), "last")
                .link(pathBuilder.clone().replaceQueryParam("page", 1).build(), "first");

        if(page != 1){
            responseBuilder.link(pathBuilder.clone().replaceQueryParam("page", page-1).build(), "prev");
        }


        if(page != newsPage.getTotalPages()){
            responseBuilder.link(pathBuilder.clone().replaceQueryParam("page", page+1).build(), "next");
        }
        return responseBuilder.build();
    }

    @GET
    @Path("/{newsId:[0-9]+}")
    @Produces(value = {CustomMediaType.USER_V1})
    public Response getNews(@PathParam("newsId") final long newsId){
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));

        NewsDto newsDto = NewsDto.fromNews(uriInfo, news);
        return Response.ok(newsDto).build();
    }

    @GET
    @Path("/{newsId:[0-9]+}/reports")
    @Produces(value = {CustomMediaType.USER_V1})
    public Response getNewsReportDetail(@PathParam("newsId") final long newsId){
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        List<ReportDetailDto> reportList = news.getReports().stream().map(d -> ReportDetailDto.fromReportDetail(uriInfo, d)).collect(Collectors.toList());
        return Response.ok(new GenericEntity<List<ReportDetailDto>>(reportList) {}).build();
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/{newsId:[0-9]+}/image")
    public Response updateNewsImage(@PathParam("newsId") long newsId,
                                 @FormDataParam("image") final FormDataBodyPart newsBodyPart,
                                 @FormDataParam("image") byte[] bytes) {
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        final String imageType = newsBodyPart.getMediaType().toString();
        newsService.setNewsImage(news.getNewsId(), bytes, imageType);
        final URI location = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(location).build();
    }

    @PUT
    @Path("/{newsId:[0-9]+}/likes/{userId:[0-9]+}")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response like(@PathParam("userId") final long userId, @PathParam("newsId") final long newsId){
        User user = userService.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        newsService.setRating(user, news, Rating.UPVOTE);
        return Response.ok().build();
    }

    @PUT
    @Path("/{newsId:[0-9]+}/dislikes/{userId:[0-9]+}")
  @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response dislike(@PathParam("userId") final long userId, @PathParam("newsId") final long newsId){
        User user = userService.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        newsService.setRating(user, news, Rating.DOWNVOTE);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{newsId:[0-9]+}")
    @PreAuthorize("@ownerCheck.canDeleteNews(#newsId)")
    public Response delete(@PathParam("newsId") final long newsId){
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        newsService.deleteNews(news);
        return Response.ok().build();
    }

    @PUT
    @Path("/{newsId:[0-9]+}/reports/{userId:[0-9]+}")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response report(@PathParam("userId") final long userId, @PathParam("newsId") final long newsId, @Valid final ReportNewsForm reportForm){
        User user = userService.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        adminService.reportNews(user, newsId, ReportReason.getByValue(reportForm.getReason()));
        return Response.ok().build();
    }

    @DELETE
    @Path("/{newsId:[0-9]+}/likes/{userId:[0-9]+}")
        @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response removeLike(@PathParam("userId") final long userId, @PathParam("newsId") final long newsId){
        User user = userService.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        newsService.setRating(user, news, Rating.NO_RATING);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{newsId:[0-9]+}/dislikes/{userId:[0-9]+}")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response removeDislike(@PathParam("userId") final long userId, @PathParam("newsId") final long newsId){
        User user = userService.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        newsService.setRating(user, news, Rating.NO_RATING);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{newsId:[0-9]+}/bookmarks/{userId:[0-9]+}")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response removeBookmark(@PathParam("userId") final long userId, @PathParam("newsId") final long newsId){
        User user = userService.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        newsService.unsaveNews(user, newsId);
        return Response.ok().build();
    }

    @PUT
    @Path("/{newsId:[0-9]+}/bookmarks/{userId:[0-9]+}")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    public Response save(@PathParam("userId") final long userId, @PathParam("newsId") final long newsId){
        User user = userService.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));
        News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        newsService.saveNews(user, newsId);
        return Response.ok().build();
    }



    @Consumes({MediaType.APPLICATION_JSON})
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
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @Path("/{newsId:[0-9]+}/image")
    public Response profileImage(@PathParam("newsId") final long newsId) {
        final News news = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        final Optional<Long> maybeImageId = news.getImageId();

        if (!maybeImageId.isPresent()){
            return Response.noContent().build();
        }

        final Image image = imageService.getImageById(maybeImageId.get()).orElseThrow(()-> new ImageNotFoundException(maybeImageId.get()));
        return Response
                .ok(new ByteArrayInputStream(image.getBytes()))
                .type(image.getDataType())
                .build();
    }

}

