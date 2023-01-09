package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.user.MailOption;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.SimpleMessageDto;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("users")
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

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON})
    public Response listUsers(@QueryParam("page") @DefaultValue("1") final int page, @QueryParam("search") @DefaultValue("") final String search) {
        final Page<User> userPage = userService.searchUsers(page, search);

        if(userPage.getContent().isEmpty()){
            return Response.noContent().build();
        }

        final List<UserDto> allUsers = userPage.getContent().stream().map(u -> UserDto.fromUser(uriInfo, u)).collect(Collectors.toList());

        final Response.ResponseBuilder responseBuilder = Response.ok(new GenericEntity<List<UserDto>>(allUsers) {})
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", userPage.getTotalPages()).build(), "last")
                .link(uriInfo.getAbsolutePathBuilder().queryParam("page", 1).build(), "first");

        if(page != 1){
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page-1).build(), "prev");
        }

        if(page != userPage.getTotalPages()){
            responseBuilder.link(uriInfo.getAbsolutePathBuilder().queryParam("page", page+1).build(), "next");
        }

        return responseBuilder.build();
    }

    @Consumes({MediaType.APPLICATION_JSON})
    @POST
    public Response createUser(@Valid final UserForm userForm){
        final User newUser = userService.create(new User.UserBuilder(userForm.getEmail()).pass(userForm.getPassword()));

        final URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build();
        return Response.created(location).build();
    }

    @GET
    @Path("/{userId:[0-9]+}")
    @Produces(value = { MediaType.APPLICATION_JSON})
    public Response getUser(@PathParam("userId") final long userId){
        User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);

        UserDto userDto = UserDto.fromUser(uriInfo, user, userService.getFollowersCount(userId), userService.getFollowingCount(userId));
        return Response.ok(userDto).build();
    }

    //TODO: CHECK THAT THE USER THAT IS BEING UPDATED IS THE ONE THAT IS LOGGED IN.
    @PUT
    @Path("/{userId:[0-9]+}")
    @Produces(value = { MediaType.APPLICATION_JSON})
    public Response editUser(@PathParam("userId") final long userId, @Valid final UserProfileForm userProfileForm) throws IOException {
        Optional<User> mayBeUser = userService.getUserById(userId);

        if(!mayBeUser.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        userService.updateProfile(userId, userProfileForm.getUsername(),
                userProfileForm.getImage().getBytes(), userProfileForm.getImage().getContentType(), userProfileForm.getDescription());
        userService.updateEmailSettings(mayBeUser.get(), MailOption.getEnumCollection(userProfileForm.getMailOptions()));

        return Response.ok(UserDto.fromUser(uriInfo, mayBeUser.get())).build();
    }

    @GET
    @Path("/{userId:[0-9]+}/image")
    public Response profileImage(@PathParam("userId") final long userId) {
        final Image image = userService.getUserById(userId).orElseThrow(UserNotFoundException::new).getImage();

        if (image.getBytes().length == 0)
            return Response.noContent().build();

        return Response
                .ok(new ByteArrayInputStream(image.getBytes()))
                .type(image.getDataType())
                .build();
    }


    @PUT
    @Path(value = "/{userId:[0-9]+}/pingNews/{newsId:[0-9]+}")
    public Response pingNews(@PathParam("userId") final long userId, @PathParam("newsId") final long newsId) {
        //final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotFoundException::new);

        final User user = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        final News news =  newsService.getById(user, newsId).orElseThrow(NewsNotFoundException::new);

        //TODO: check if the current user has the id userId and if he is the creator of the article.

        if(userService.pingNewsToggle(user,news)){
            return Response.ok(SimpleMessageDto.fromString(String.format("User %s pinged the news of id %d", user.getUsername(), news.getNewsId()))).build();
        }else{
            return Response.ok(SimpleMessageDto.fromString(String.format("User %s unpinged the news of id %d", user.getUsername(), news.getNewsId()))).build();
        }
    }

    @PUT
    @Path(value = "/{userId:[0-9]+}/followers}")
    public Response followUser(@PathParam("userId") final long userId) {
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotFoundException::new);
        if(userService.followUser(currentUser, userId)){
            return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] followed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] already followed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
    }

    @DELETE
    @Path(value = "/{userId:[0-9]+}/followers}")
    public Response unfollowUser(@PathParam("userId") final long userId) {
        final User currentUser = securityService.getCurrentUser().orElseThrow(UserNotFoundException::new);
        if(userService.unfollowUser(currentUser, userId)){
            return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] unfollowed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
        }
        return Response.ok(SimpleMessageDto.fromString(String.format("User %s [id %d] did not followed user of id %d", currentUser, currentUser.getUserId(), userId))).build();
    }

}

