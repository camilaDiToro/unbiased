package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.Optional;

public abstract class BaseController {

    protected final SecurityService securityService;
    protected final UserService userService;

    @Autowired
    public BaseController(SecurityService ss, UserService userService) {
        this.securityService = ss;
        this.userService = userService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        Optional<User> maybeUser = securityService.getCurrentUser();
        User user = maybeUser.orElse(null);

        model.addAttribute("loggedUser", user);
        model.addAttribute("isLoggedIn", user != null);
        model.addAttribute("isAdmin", userService.isUserAdmin(user));
    }

}
