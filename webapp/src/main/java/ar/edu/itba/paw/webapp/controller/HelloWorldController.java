package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.exceptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;

@Controller
public class HelloWorldController {

    private final UserService us;

    @Autowired
    public HelloWorldController(@Qualifier("userServiceImpl") final UserService us){
        this.us = us;
    }

    @RequestMapping("/")
    public ModelAndView helloWorld( @RequestParam(name = "userId", defaultValue = "1") final long userId){
        final ModelAndView mav = new ModelAndView("index");
        mav.addObject("user",us.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createForm(@ModelAttribute("registerForm") final UserForm userForm){
        final ModelAndView mav = new ModelAndView("register");
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm userForm, final BindingResult errors){
        if(errors.hasErrors()){
            return createForm(userForm);
        }
        final User user = us.create(userForm.getUsername(), userForm.getPassword());
        return new ModelAndView("redirect:/profile/"+user.getId());
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView profile(@PathVariable("userId") long userId, @Valid @ModelAttribute("userProfileForm") final UserProfileForm userProfileForm){
        final ModelAndView mav = new ModelAndView("profile");
        mav.addObject("user",us.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}", method = RequestMethod.POST)
    public ModelAndView profilePicture(@PathVariable("userId") long userId, @Valid @ModelAttribute("userProfileForm") final UserProfileForm userProfileForm, final BindingResult errors) throws IOException {
        if(errors.hasErrors()){
            return profile(userId, userProfileForm);
        }
        System.out.println("aaaa" + userProfileForm.getImage().getBytes());
        return new ModelAndView("redirect:/profile/"+userId);
    }

    @RequestMapping("/chau")
    public ModelAndView goodbyeWorld(final long userId){
        final ModelAndView mav = new ModelAndView("byebye");
        mav.addObject("user",us.getUserById(userId).orElseThrow(UserNotFoundException::new));
        return mav;
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView userNotFound()    {
        return new ModelAndView("404");
    }
}
