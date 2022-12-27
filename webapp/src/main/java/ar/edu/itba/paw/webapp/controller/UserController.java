package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("users")
@Component
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GET
    @Produces(value = { MediaType.APPLICATION_JSON, })
    public Response listUsers(@QueryParam("page") @DefaultValue("1") final int page) {
        //final List<User> allUsers = userService.getAll();
        final List<User> allUsers = new ArrayList<>();

        if(allUsers.isEmpty()){
            return Response.noContent().build();
        }

        return Response.ok(new GenericEntity<List<User>>(allUsers) {})
                .link("", "prev")
                .link("", "next")
                .link("", "last")
                .link("", "first")
                .build();
    }
}
