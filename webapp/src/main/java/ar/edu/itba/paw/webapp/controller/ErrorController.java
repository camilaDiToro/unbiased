package ar.edu.itba.paw.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {



    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping("/400")
    public ModelAndView error400() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","400");
        return mav;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping("/400/invalid_category")
    public ModelAndView error400InvalidCategory() {
        return new ModelAndView("errors/invalid_category");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping("/400/invalid_filter")
    public ModelAndView error400InvalidFilter() {
        return new ModelAndView("errors/invalid_filter");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping("/400/invalid_order")
    public ModelAndView error400InvalidOrder() {
        return new ModelAndView("errors/invalid_order");
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping("/401")
    public ModelAndView error401() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","401");
        return mav;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @RequestMapping( "/403")
    public ModelAndView error403() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","403");
        return mav;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping( "/404")
    public ModelAndView error404() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","404");
        return mav;
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @RequestMapping( "/405")
    public ModelAndView error405() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","405");
        return mav;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping( "/500")
    public ModelAndView error500() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","500");
        return mav;
    }

}
