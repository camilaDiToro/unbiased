package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
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
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView restaurantNotFound() {
        return new ModelAndView("forward:/400/invalid_category");
    }

    @ExceptionHandler(HTTPException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ModelAndView numberedError(HTTPException exception) {
        return new ModelAndView("forward:/" + exception.getStatusCode());
    }

}
