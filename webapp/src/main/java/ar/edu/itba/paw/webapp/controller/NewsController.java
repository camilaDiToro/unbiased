package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.NewsDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api/news")
@Component
public class NewsController {

    private final UserService userService;
    private final NewsService newsService;
    private final SecurityService securityService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public NewsController(UserService userService, NewsService newsService, SecurityService securityService) {
        this.userService = userService;
        this.newsService = newsService;
        this.securityService = securityService;
    }



    @GET
    @Produces(value = {MediaType.APPLICATION_JSON})
    public Response listNews(@QueryParam("page") @DefaultValue("1") final int page, @QueryParam("search") @DefaultValue("") final String search,
                             @QueryParam("cat") @DefaultValue("ALL") final String category,
                             @QueryParam("order") @DefaultValue("TOP") final String order,
                             @QueryParam("time") @DefaultValue("WEEK") final String time,
                             @QueryParam("likedBy") @DefaultValue("") final String likedBy,
                             @QueryParam("dislikedBy") @DefaultValue("") final String dislikedBy,
                             @QueryParam("savedBy") @DefaultValue("") final String savedBy,
                             @QueryParam("publishedBy") @DefaultValue("") final String publishedBy) {
        Category categoryObj = Category.getByValue(category);
        NewsOrder orderObj = NewsOrder.getByValue(order);
        TimeConstraint timeObj = TimeConstraint.getByValue(time);
        final Page<News> newsPage;
        final int uniqueParamCount = Arrays.stream(new String[]{likedBy, dislikedBy, savedBy, publishedBy}).map(s -> s.equals("") ?0 : 1).reduce(Integer::sum).get();

        if (uniqueParamCount > 1) {
            // excepcion de request invalido
        }

        // SE PUEDE HACER MEJOR
        if (!likedBy.equals("")) {
            final ProfileCategory catObject = ProfileCategory.UPVOTED;
            final User profileUser = userService.getUserById(Long.parseLong(likedBy)).orElseThrow(UserNotFoundException::new);
            newsPage = newsService.getNewsForUserProfile(Optional.empty(), page, orderObj, profileUser, catObject);
        } else if (!dislikedBy.equals(""))  {
            final ProfileCategory catObject = ProfileCategory.DOWNVOTED;
            final User profileUser = userService.getUserById(Long.parseLong(dislikedBy)).orElseThrow(UserNotFoundException::new);
            newsPage = newsService.getNewsForUserProfile(Optional.empty(), page, orderObj, profileUser, catObject);
        } else if (!publishedBy.equals("")) {
            final ProfileCategory catObject = ProfileCategory.MY_POSTS;
            final User profileUser = userService.getUserById(Long.parseLong(publishedBy)).orElseThrow(UserNotFoundException::new);
            newsPage = newsService.getNewsForUserProfile(Optional.empty(), page, orderObj, profileUser, catObject);
        } else if (!savedBy.equals("")) {
            final ProfileCategory catObject = ProfileCategory.SAVED;
            final User profileUser = userService.getUserById(Long.parseLong(savedBy)).orElseThrow(UserNotFoundException::new);
            final User user = securityService.getCurrentUser().get();
            if (!user.equals(profileUser)) {
                throw new UserNotAuthorized();
            }
            newsPage = newsService.getNewsForUserProfile(Optional.empty(), page, orderObj, profileUser, catObject);
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

        queryParams.entrySet().forEach(entry -> pathBuilder.queryParam(entry.getKey(), entry.getValue().get(0)));

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
//
//    @Consumes({MediaType.APPLICATION_JSON})
//    @POST
//    public Response createUser(@Valid final UserForm userForm){
//        final User newUser = userService.create(new User.UserBuilder(userForm.getEmail()).pass(userForm.getPassword()));
//
//        final URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build();
//        return Response.created(location).build();
//    }
//
//    @GET
//    @Path("/{userId:[0-9]+}")
//    @Produces(value = { MediaType.APPLICATION_JSON})
//    public Response getUser(@PathParam("userId") final long userId){
//        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
//        UserDto userDto = UserDto.fromUser(uriInfo, user, userService.getFollowersCount(userId), userService.getFollowingCount(userId));
//        return Response.ok(userDto).build();
//    }
//
//    @GET
//    @Path("/{userId:[0-9]+}/news-stats")
//    @Produces(value = { MediaType.APPLICATION_JSON})
//    public Response getUserNewsStats(@PathParam("userId") final long userId){
//        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
//        if (!user.getRoles().contains(Role.ROLE_JOURNALIST)) {
//            return Response.status(404).build();
//        }
//
//        Map<Category, CategoryStatistics.Statistic> newsCategoryMap = newsService.getCategoryStatistics(user.getUserId()).getStatiscticsMap();
//        List<CategoryStatisticsDto> newsStats = newsCategoryMap.entrySet().stream().map(CategoryStatisticsDto::fromCategoryStatistic).collect(Collectors.toList());
//        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<CategoryStatisticsDto>>(newsStats) {});
//        return responseBuilder.build();
//    }
//
//
//    @PUT
//    @Path("/{userId:[0-9]+}")
//    @PreAuthorize("@ownerCheck.userMatches(#userId)")
//    @Produces(value = { MediaType.APPLICATION_JSON})
//    @Consumes({MediaType.APPLICATION_JSON})
//    public Response editUser(@PathParam("userId") final long userId, @Valid final UserProfileForm userProfileForm) throws IOException {
//        Optional<User> mayBeUser = userService.getUserById(userId);
//
//        if(!mayBeUser.isPresent()){
//            return Response.status(Response.Status.NOT_FOUND).build();
//        }
//
//        if(userProfileForm.getImage() != null){
//            userService.updateProfile(userId, userProfileForm.getUsername(),
//                    userProfileForm.getImage().getBytes(), userProfileForm.getImage().getContentType(), userProfileForm.getDescription());
//        }else{
//            userService.updateProfile(userId, userProfileForm.getUsername(),
//                    null, null, userProfileForm.getDescription());
//        }
//        if(userProfileForm.getMailOptions()!=null){
//            userService.updateEmailSettings(mayBeUser.get(), MailOption.getEnumCollection(userProfileForm.getMailOptions()));
//        }
//
//        return Response.ok(UserDto.fromUser(uriInfo, mayBeUser.get(), userService.getFollowersCount(userId), userService.getFollowingCount(userId))).build();
//    }
//
//    @GET
//    @Path("/{userId:[0-9]+}/image")
//    public Response profileImage(@PathParam("userId") final long userId) {
//        final Image image = userService.getUserById(userId).orElseThrow(UserNotFoundException::new).getImage();
//
//        if (image.getBytes().length == 0)
//            return Response.noContent().build();
//
//        return Response
//                .ok(new ByteArrayInputStream(image.getBytes()))
//                .type(image.getDataType())
//                .build();
//    }
//
//
//    @PUT
//    @PreAuthorize("@ownerCheck.newsOwnership(#newsId, #userId)")
//    @Path(value = "/{userId:[0-9]+}/pingNews/{newsId:[0-9]+}")
//    public Response pingNews(@PathParam("userId") final long userId, @PathParam("newsId") final long newsId) {
//
//        final User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
//        final News news =  newsService.getById(user, newsId).orElseThrow(NewsNotFoundException::new);
//
//        if(userService.pingNewsToggle(user,news)){
//            return Response.ok(SimpleMessageDto.fromString(String.format("User %s pinged the news of id %d", user.getUsername(), news.getNewsId()))).build();
//        }else{
//            return Response.ok(SimpleMessageDto.fromString(String.format("User %s unpinged the news of id %d", user.getUsername(), news.getNewsId()))).build();
//        }
//    }
//
//    @PUT
//    @PreAuthorize("@ownerCheck.userMatches(#followerId)")
//    @Path(value = "/{userId:[0-9]+}/followers/{followerId:[0-9]+}")
//    public Response followUser(@PathParam("userId") final long userId, @PathParam("followerId") final long followerId) {
//        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotFoundException::new);
//        if(userService.followUser(currentUser, userId)){
//            return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] followed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
//        }
//        return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] already followed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
//    }
//
//    @DELETE
//    @PreAuthorize("@ownerCheck.userMatches(#followerId)")
//    @Path(value = "/{userId:[0-9]+}/followers/{followerId:[0-9]+}")
//    public Response unfollowUser(@PathParam("userId") final long userId, @PathParam("followerId") final long followerId) {
//        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotFoundException::new);
//        if(userService.unfollowUser(currentUser, userId)){
//            return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] unfollowed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
//        }
//        return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] did not followed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
//    }

}

