package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.CategoryStatistics;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.MailOption;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.CategoryStatisticsDto;
import ar.edu.itba.paw.webapp.api.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.SimpleMessageDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import org.glassfish.jersey.media.multipart.FormDataBodyPart;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("/api/users")
@Component
public class UserController {

    private final UserService userService;
    private final NewsService newsService;
    private final SecurityService securityService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(UserService userService, NewsService newsService, SecurityService securityService) {
        this.userService = userService;
        this.newsService = newsService;
        this.securityService = securityService;
    }

    @PUT
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.newsOwnership(#newsId, #userId)")
    @Path(value = "/{userId:[0-9]+}/pinnedNews")
    public Response pinNews(@PathParam("userId") final long userId, @QueryParam("newsId") final long newsId) {

        final User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format(UserNotFoundException.ID_MSG, userId)));
        final News news =  newsService.getById(user, newsId).orElseThrow(()-> new NewsNotFoundException(String.format(NewsNotFoundException.ID_MSG, newsId)));
        userService.pinNews(user, news);
        return Response.ok(SimpleMessageDto.fromString(String.format("User %s pinned the news of id %d", user.getUsername(), news.getNewsId()))).build();

    }

    @DELETE
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    @Path(value = "/{userId:[0-9]+}/pinnedNews")
    public Response unpinNews(@PathParam("userId") final long userId) {

        final User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format(UserNotFoundException.ID_MSG, userId)));
        userService.unpinNews(user);
        return Response.ok(SimpleMessageDto.fromString(String.format("User %s unpinned the news", user.getUsername()))).build();

    }

    @GET
    @Path(value = "/{userId:[0-9]+}/following")
    @Produces(value = {CustomMediaType.USER_LIST_V1})
    public Response following(@PathParam("userId")  final long userId) {

        final User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format(UserNotFoundException.ID_MSG, userId)));
        List<UserDto> users = userService.getFollowing(user).stream().map(u -> UserDto.fromUser(uriInfo, u)).collect(Collectors.toList());

        if (users.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(new GenericEntity<List<UserDto>>(users){}).build();
    }

    @GET
    @Produces(value = {CustomMediaType.USER_LIST_V1})
    public Response listUsers(@QueryParam("page") @DefaultValue("1") final int page, @QueryParam("search") @DefaultValue("") final String search,
                              @QueryParam("topCreators") final boolean topCreators) {

        if (topCreators) {
            List<UserDto> creatorList =  userService.getTopCreators(5).stream().map(u -> UserDto.fromUser(uriInfo, u)).collect(Collectors.toList());
            return Response.ok(new GenericEntity<List<UserDto>>(creatorList) {}).build();
        }
        final Page<User> userPage = userService.searchUsers(page, search);

        if(userPage.getContent().isEmpty()){
            return Response.noContent().build();
        }

        final List<UserDto> allUsers = userPage.getContent().stream().map(u -> UserDto.fromUser(uriInfo, u)).collect(Collectors.toList());

        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<UserDto>>(allUsers) {});
        return PagingUtils.pagedResponse(userPage, responseBuilder, uriInfo);
    }

    @Consumes({CustomMediaType.USER_V1})
    @POST
    public Response createUser(@Valid final UserForm userForm){
        final User newUser = userService.create(new User.UserBuilder(userForm.getEmail()).pass(userForm.getPassword()));

        final URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build();
        return Response.created(location).build();
    }

    @GET
    @Path("/{userId:[0-9]+}")
    @Produces(value = {CustomMediaType.USER_V1})
    public Response getUser(@PathParam("userId") final long userId){
        User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format(UserNotFoundException.ID_MSG, userId)));

        UserDto userDto = UserDto.fromUser(uriInfo, user);
        return Response.ok(userDto).build();
    }

    @GET
    @Path("/{userId:[0-9]+}/news-stats")
    @Produces(value = { CustomMediaType.CATEGORY_STATISTICS_V1})
    public Response getUserNewsStats(@PathParam("userId") final long userId){
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        if (!user.getRoles().contains(Role.ROLE_JOURNALIST)) {
            return Response.status(404).build();
        }

        Map<Category, CategoryStatistics.Statistic> newsCategoryMap = newsService.getCategoryStatistics(user.getUserId()).getStatiscticsMap();
        List<CategoryStatisticsDto> newsStats = newsCategoryMap.entrySet().stream().map(CategoryStatisticsDto::fromCategoryStatistic).collect(Collectors.toList());
        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<CategoryStatisticsDto>>(newsStats) {});
        return responseBuilder.build();
    }


    @PUT
    @Path("/{userId:[0-9]+}")
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    @Produces(value = { CustomMediaType.USER_V1})
    public Response editUser(@PathParam("userId") final long userId, @Valid final UserProfileForm userProfileForm) throws IOException {
        Optional<User> mayBeUser = userService.getUserById(userId);

        if(!mayBeUser.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        userService.updateProfile(userId, userProfileForm.getUsername(),
                null, null, userProfileForm.getDescription());
        if(userProfileForm.getMailOptions()!=null){
            userService.updateEmailSettings(mayBeUser.get(), MailOption.getEnumCollection(userProfileForm.getMailOptions()));
        }

        return Response.ok(UserDto.fromUser(uriInfo, mayBeUser.get())).build();
    }

    @PUT
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/{userId:[0-9]+}/image")
    public Response updateUserImage(@PathParam("userId") long userId,
                                    @FormDataParam("image") final FormDataBodyPart imageBodyPart,
                                    @FormDataParam("image") byte[] bytes) {
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        final String imageType = imageBodyPart.getMediaType().toString();
        userService.setUserImage(userId, bytes, imageType);
        final URI location = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(location).build();
    }

    @GET
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @Path("/{userId:[0-9]+}/image")
    public Response profileImage(@PathParam("userId") final long userId) {
        final Image image = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format(UserNotFoundException.ID_MSG, userId))).getImage();

        if (image.getBytes().length == 0)
            return Response.noContent().build();

        return Response
                .ok(new ByteArrayInputStream(image.getBytes()))
                .type(image.getDataType())
                .build();
    }


    @PUT
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.newsOwnership(#newsId, #userId)")
    @Path(value = "/{userId:[0-9]+}/pingNews/{newsId:[0-9]+}")
    public Response pingNews(@PathParam("userId") final long userId, @PathParam("newsId") final long newsId) {

        final User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(String.format(UserNotFoundException.ID_MSG, userId)));
        final News news =  newsService.getById(user, newsId).orElseThrow(()-> new NewsNotFoundException(String.format(NewsNotFoundException.ID_MSG, newsId)));

        if(userService.pingNewsToggle(user,news)){
            return Response.ok(SimpleMessageDto.fromString(String.format("User %s pinged the news of id %d", user.getUsername(), news.getNewsId()))).build();
        }else{
            return Response.ok(SimpleMessageDto.fromString(String.format("User %s unpinged the news of id %d", user.getUsername(), news.getNewsId()))).build();
        }
    }

    @PUT
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#followerId)")
    @Path(value = "/{userId:[0-9]+}/followers/{followerId:[0-9]+}")
    public Response followUser(@PathParam("userId") final long userId, @PathParam("followerId") final long followerId) {
        final User currentUser = securityService.getCurrentUser().orElseThrow(() -> new UserNotFoundException(String.format(UserNotFoundException.ID_MSG, userId)));
        if(userService.followUser(currentUser, userId)){
            return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] followed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] already followed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
    }

    @DELETE
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#followerId)")
    @Path(value = "/{userId:[0-9]+}/followers/{followerId:[0-9]+}")
    public Response unfollowUser(@PathParam("userId") final long userId, @PathParam("followerId") final long followerId) {
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new);
        if(userService.unfollowUser(currentUser, userId)){
            return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] unfollowed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] did not follow user of id %d", currentUser, currentUser.getUserId(), userId))).build();
    }

}

