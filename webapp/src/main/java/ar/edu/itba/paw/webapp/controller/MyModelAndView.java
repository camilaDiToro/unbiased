package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.TextType;
import ar.edu.itba.paw.model.User;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

public class MyModelAndView extends ModelAndView {

    public MyModelAndView(String viewName, String pageTitle, TextType textType, Optional<User> loggedUser) {
        super(viewName);
        addObject("pageTitle", pageTitle);

        User user = loggedUser.orElse(null);

        addObject("loggedUser", user);
        addObject("isLoggedIn", user != null);
        addObject("textType", textType);
    }
}
