package ar.edu.itba.paw.webapp.model;

import ar.edu.itba.paw.model.news.TextType;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.SecurityService;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

public class MyModelAndView extends ModelAndView {

    private MyModelAndView(String viewName, String pageTitle, TextType textType, Optional<User> loggedUser, boolean isAdmin) {
        super(viewName);
        addObject("pageTitle", pageTitle);
        User user = loggedUser.orElse(null);

        addObject("loggedUser", user);
        addObject("isLoggedIn", user != null);
        addObject("textType", textType);
        addObject("isAdmin", isAdmin);
    }

    public static class Builder {
        private final MyModelAndView mav;
        private final TextType textType;
        private final StringBuilder params = new StringBuilder();
        public Builder(String viewName, String pageTitle, TextType textType, SecurityService ss) {
            mav = new MyModelAndView(viewName, pageTitle, textType, ss.getCurrentUser(), ss.isCurrentUserAdmin());
            this.textType = textType;
        }

        public MyModelAndView.Builder withStringParam(String param) {
            if (TextType.INTERCODE != textType)
                throw new IllegalStateException();

            this.params.append(',').append(param);
            return this;
        }

        public MyModelAndView.Builder withObject(String name, Object o) {
            mav.addObject(name, o);
            return this;
        }

        public MyModelAndView.Builder withObject(Object o) {
            mav.addObject(o);
            return this;
        }

        public ModelAndView build() {
            mav.addObject("stringParams", params.toString());
            return mav;
        }
    }
}
