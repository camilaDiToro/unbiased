package ar.edu.itba.paw.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(value = "/400")
    public ModelAndView error400() {
        return new ModelAndView("errors/400");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping(value = "/401")
    public ModelAndView error401() {
        return new ModelAndView("errors/401");
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @RequestMapping(value = "/403")
    public ModelAndView error403() {
        return new ModelAndView("errors/403");
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(value = "/404")
    public ModelAndView error404() {
        return new ModelAndView("errors/404");
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @RequestMapping(value = "/405")
    public ModelAndView error405() {
        return new ModelAndView("errors/405");
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(value = "/500")
    public ModelAndView error500() {
        return new ModelAndView("errors/500");
    }

}
