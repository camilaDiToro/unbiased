package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorizedException;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.CategoryStatistics;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.MailOption;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.OwnerService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.api.exceptions.ApiErrorCode;
import ar.edu.itba.paw.webapp.api.exceptions.CustomBadRequestException;
import ar.edu.itba.paw.webapp.api.exceptions.InvalidRequestParamsException;
import ar.edu.itba.paw.webapp.controller.queryParamsValidators.GetUsersFilter;
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

import javax.persistence.Enumerated;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Path("/api/users")
@Component
public class UserController {

    private final UserService userService;
    private final NewsService newsService;
    private final SecurityService securityService;
    private final OwnerService ownerService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(UserService userService, NewsService newsService, SecurityService securityService, OwnerService ownerService) {
        this.userService = userService;
        this.newsService = newsService;
        this.securityService = securityService;
        this.ownerService = ownerService;
    }

    @GET
    @Produces(value = {CustomMediaType.USER_LIST_V1})
    public Response listUsers(@QueryParam("page") @DefaultValue("1") final int page,
                              @QueryParam("filter") @DefaultValue("NO_FILTER") final String filter,
                              @QueryParam("search") final String search,
                              @QueryParam("id") final Long id) {

        final GetUsersFilter objFilter = GetUsersFilter.fromString(filter);
        if(!objFilter.areParamsValid(search,id)){
            throw new InvalidRequestParamsException(objFilter.getInvalidParamsMsg(search, id));
        }

        Page<User> userPage = objFilter.getUsers(userService,page,search,id);

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
        User user = userService.getUserById(userId).orElseThrow( () -> new UserNotFoundException(userId));

        UserDto userDto = UserDto.fromUser(uriInfo, user);
        return Response.ok(userDto).build();
    }

    @GET
    @Path("/{userId:[0-9]+}/news-stats")
    @Produces(value = { CustomMediaType.CATEGORY_STATISTICS_V1})
    public Response getUserNewsStats(@PathParam("userId") final long userId){
        User user = userService.getUserById(userId).orElseThrow( () -> new UserNotFoundException(userId));
        if (!user.getRoles().contains(Role.ROLE_JOURNALIST)) {
            return Response.noContent().build();
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
        User user = userService.getUserById(userId).orElseThrow(()-> new UserNotFoundException(userId));

        userService.updateProfile(userId, userProfileForm.getUsername(),
                null, null, userProfileForm.getDescription());
        if(userProfileForm.getMailOptions()!=null){
            userService.updateEmailSettings(user, MailOption.getEnumCollection(userProfileForm.getMailOptions()));
        }

        return Response.ok(UserDto.fromUser(uriInfo, user)).build();
    }

    @PUT
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Path("/{userId:[0-9]+}/image")
    public Response updateUserImage(@PathParam("userId") long userId,
                                    @FormDataParam("image") final FormDataBodyPart imageBodyPart,
                                    @FormDataParam("image") byte[] bytes) {
        final String imageType = imageBodyPart.getMediaType().toString();
        userService.setUserImage(userId, bytes, imageType);
        final URI location = uriInfo.getAbsolutePathBuilder().build();
        return Response.created(location).build();
    }

    @GET
    @Path("/{userId:[0-9]+}/image")
    public Response profileImage(@PathParam("userId") final long userId) {
        final Image image = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId)).getImage();

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
    @Path(value = "/{userId:[0-9]+}/pinnedNews")
    public Response pinNews(@PathParam("userId") final long userId, @QueryParam("newsId") final long newsId) {

        final User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        final News news =  newsService.getById(user, newsId).orElseThrow(()-> new NewsNotFoundException(newsId));
        userService.pinNews(user, news);
        return Response.ok(SimpleMessageDto.fromString(String.format("User %s pinned the news of id %d", user.getUsername(), news.getNewsId()))).build();

    }

    @DELETE
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#userId)")
    @Path(value = "/{userId:[0-9]+}/pinnedNews")
    public Response unpinNews(@PathParam("userId") final long userId) {

        final User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        userService.unpinNews(user);
        return Response.ok(SimpleMessageDto.fromString(String.format("User %s unpinned the news", user.getUsername()))).build();

    }

    /*@GET
    @Path(value = "/{userId:[0-9]+}/following")
    @Produces(value = {CustomMediaType.USER_LIST_V1})
    public Response following(@PathParam("userId")  final long userId) {

        final User user = userService.getUserById(userId).orElseThrow(() -> new UserNotFoundException(userId));
        List<UserDto> users = userService.getFollowing(user).stream().map(u -> UserDto.fromUser(uriInfo, u)).collect(Collectors.toList());

        if (users.isEmpty()) {
            return Response.noContent().build();
        }
        return Response.ok(new GenericEntity<List<UserDto>>(users){}).build();
    }*/

    @PUT
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.userMatches(#followerId)")
    @Path(value = "/{userId:[0-9]+}/followers/{followerId:[0-9]+}")
    public Response followUser(@PathParam("userId") final long userId, @PathParam("followerId") final long followerId) {
        final User currentUser = securityService.getCurrentUser().orElseThrow(() -> new UserNotFoundException(userId));
        if(userService.followUser(currentUser, userId)){
            return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] followed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] already followed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
    }

    @DELETE
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @Path(value = "/{userId:[0-9]+}/followers/{followerId:[0-9]+}")
    public Response unfollowUser(@PathParam("userId") final long userId, @PathParam("followerId") final long followerId) {
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotAuthorizedException::new);
        if(userService.unfollowUser(currentUser, userId)){
            return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] unfollowed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] did not follow user of id %d", currentUser, currentUser.getUserId(), userId))).build();
    }


    /// ------------------------------------------------------------------------------------------
    /// -------------------------------- OWNER ---------------------------------------------------
    /// ------------------------------------------------------------------------------------------

    @PUT
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.isAdmin()")
    @Path(value = "/{userId:[0-9]+}/role")
    public Response addRole(@PathParam("userId") final long userId, @QueryParam("role") final String role) {
        if(!role.equals(Role.ROLE_ADMIN.getRole())){
            throw new CustomBadRequestException(
                    ApiErrorCode.INVALID_ROLE,
                    "Trying to add an invalid role to an user",
                    String.format("The role %s can not be manually added to the user of id %d", role, userId));
        }
        if(ownerService.makeUserAdmin(userId)){
            return Response.ok(SimpleMessageDto.fromString(String.format("User of id %d is now admin", userId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("User of id %d was already admin", userId))).build();
    }

    @DELETE
    @Produces(value = {CustomMediaType.SIMPLE_MESSAGE_V1})
    @PreAuthorize("@ownerCheck.isAdmin()")
    @Path(value = "/{userId:[0-9]+}/role")
    public Response deleteRole(@PathParam("userId") final long userId, @QueryParam("role") final String role) {
        if(!role.equals(Role.ROLE_ADMIN.getRole())){
            throw new CustomBadRequestException(
                    ApiErrorCode.INVALID_ROLE,
                    "Trying to delete an invalid role from an user",
                    String.format("The role %s can not be manually deleted from the user of id %d", role, userId));
        }
        if(ownerService.deleteUserAdmin(userId)){
            return Response.ok(SimpleMessageDto.fromString(String.format("User of id %d is not admin anymore", userId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("User of id %d was not an admin", userId))).build();
    }


}

