package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.exeptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.ws.http.HTTPException;

/*https://www.baeldung.com/exception-handling-for-rest-with-spring*/
@ControllerAdvice
public class AdviceErrorController {

    @ExceptionHandler(InvalidCategoryException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView restaurantNotFound() {
        return new ModelAndView("forward:/400/invalid_category");
    }

    @ExceptionHandler(InvalidOrderException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView orderNotFound() {
        return new ModelAndView("forward:/400/invalid_order");
    }

    @ExceptionHandler(InvalidFilterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView filterNotFound() {
        return new ModelAndView("forward:/400/invalid_filter");
    }

    @ExceptionHandler(HTTPException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView numberedError(HTTPException exception) {
        return new ModelAndView("forward:/" + exception.getStatusCode());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound(UserNotFoundException ex) {
        return new ModelAndView("errors/userNotFound");
    }

    @ExceptionHandler(UserNotAuthorized.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ModelAndView invalidUser(UserNotAuthorized ex) {
        return new ModelAndView("errors/invalid_user");
    }

    @ExceptionHandler(NewsNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView newsNotFound()    {
        return new ModelAndView("errors/newsNotFound");
    }
}
