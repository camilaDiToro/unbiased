package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Image;
import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.user.MailOption;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.UserService;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("users")
@Component
public class UserController {

    private final UserService userService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
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
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON})
    public Response getUser(@PathParam("id") final long userId){
        Optional<User> mayBeUser = userService.getUserById(userId);

        if(!mayBeUser.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        UserDto userDto = UserDto.fromUser(uriInfo, mayBeUser.get(), userService.getFollowersCount(userId), userService.getFollowingCount(userId));

        return Response.ok(userDto).build();
    }

    //TODO: CHECK THAT THE USER THAT IS BEING UPDATED IS THE ONE THAT IS LOGGED IN.
    @PUT
    @Path("/{id}")
    @Produces(value = { MediaType.APPLICATION_JSON})
    public Response editUser(@PathParam("id") final long userId, @Valid final UserProfileForm userProfileForm) throws IOException {
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
    @Path("/{id}/image")
    public Response profileImage(@PathParam("id") final long userId) {
        final Image image = userService.getUserById(userId).orElseThrow(UserNotFoundException::new).getImage();

        if (image.getBytes().length == 0)
            return Response.noContent().build();

        return Response
                .ok(new ByteArrayInputStream(image.getBytes()))
                .type(image.getDataType())
                .build();
    }

}

