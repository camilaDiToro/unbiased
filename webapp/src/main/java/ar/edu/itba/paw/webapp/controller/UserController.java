package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.dto.UserDto;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
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
    @Produces(value = { MediaType.APPLICATION_JSON, })
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

    @Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_FORM_URLENCODED})
    @POST
    public Response createUser(@Valid final UserForm userForm){
        final User newUser = userService.create(new User.UserBuilder(userForm.getEmail()).pass(userForm.getPassword()));

        final URI location = uriInfo.getAbsolutePathBuilder().path(String.valueOf(newUser.getId())).build();
        return Response.created(location).build();
    }

    @GET
    @Path("/{id}")
    public Response getUser(@PathParam("id") final long userId){
        Optional<UserDto> mayBeUser = userService.getUserById(userId).map(u -> UserDto.fromUser(uriInfo, u));

        if(!mayBeUser.isPresent()){
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.ok(mayBeUser.get()).build();
    }
}
