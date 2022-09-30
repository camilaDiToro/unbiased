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
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","400");
        return mav;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(value = "/400/invalid_category")
    public ModelAndView error400InvalidCategory() {
        ModelAndView mav = new ModelAndView("errors/invalid_category");
        return mav;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @RequestMapping(value = "/400/invalid_order")
    public ModelAndView error400InvalidOrder() {
        ModelAndView mav = new ModelAndView("errors/invalid_order");
        return mav;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @RequestMapping(value = "/401")
    public ModelAndView error401() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","401");
        return mav;
    }

    @ResponseStatus(HttpStatus.FORBIDDEN)
    @RequestMapping(value = "/403")
    public ModelAndView error403() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","403");
        return mav;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @RequestMapping(value = "/404")
    public ModelAndView error404() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","404");
        return mav;
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @RequestMapping(value = "/405")
    public ModelAndView error405() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","405");
        return mav;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @RequestMapping(value = "/500")
    public ModelAndView error500() {
        ModelAndView mav = new ModelAndView("errors/generic_numbered_error");
        mav.addObject("errorCode","500");
        return mav;
    }

}
