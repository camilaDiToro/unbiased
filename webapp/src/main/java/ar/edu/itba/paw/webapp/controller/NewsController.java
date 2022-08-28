package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.User;
import ar.edu.itba.paw.webapp.form.CreateNewsForm;
import ar.edu.itba.paw.webapp.form.UserForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class NewsController {


    @RequestMapping(value = "/news/create", method = RequestMethod.GET)
    public ModelAndView createNewsForm(@ModelAttribute("createNewsForm") final CreateNewsForm createNewsForm){
        final ModelAndView mav = new ModelAndView("create_news");
        return mav;
    }

    @RequestMapping(value = "/news/create", method = RequestMethod.POST)
    public ModelAndView postNewsForm(@Valid @ModelAttribute("createNewsForm") final CreateNewsForm createNewsFrom, final BindingResult errors){
        if(errors.hasErrors()){
            return createNewsForm(createNewsFrom);
        }

        //printing parameters to check they are working properly
        System.out.println(createNewsFrom.getTitle());
        System.out.println(createNewsFrom.getSubtitle());
        System.out.println(createNewsFrom.getCreatorEmail());
        System.out.println(createNewsFrom.getBody());

        //Create news instad of user, need to implement news service.
        //final User user = us.create(userForm.getEmail());
        return new ModelAndView("redirect:/news/successfullycreated");
    }

    @RequestMapping(value = "/news/successfullycreated", method = RequestMethod.GET)
    public ModelAndView newsSuccessfullyCreated(){
        final ModelAndView mav = new ModelAndView("news_successfully_created");
        return mav;
    }

}
