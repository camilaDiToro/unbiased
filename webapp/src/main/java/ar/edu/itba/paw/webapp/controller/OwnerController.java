package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.news.TextType;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.CreateAdminForm;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class OwnerController extends BaseController{

    private final OwnerService ownerService;
    private final UserService userService;


    @Autowired
    public OwnerController(SecurityService securityService, OwnerService ownerService, UserService userService) {
        super(securityService);
        this.ownerService = ownerService;
        this.userService = userService;
    }

    @RequestMapping(value = "/owner/add_admin", method = RequestMethod.POST)
    public ModelAndView addAdmin(@Valid @ModelAttribute("createAdminForm") CreateAdminForm form, final BindingResult errors) {
        if (errors.hasErrors()){
            return addAdminPanel(form, 1, "");
        }

        ownerService.makeUserAdmin(form.getEmail());
        return new ModelAndView("redirect:/owner/add_admin_page");
    }

    @RequestMapping(value = "/owner/delete_admin_page/{userId:[0-9]+}", method = RequestMethod.GET) // Pongo GET porque en html solo hay post y get
    public ModelAndView deleteAdmin(@PathVariable("userId") long userId) {

        ownerService.deleteUserAdmin(userId);
        return new ModelAndView("redirect:/owner/add_admin_page");

    }

    @RequestMapping(value = "/owner/add_admin_page", method = RequestMethod.GET)
    public ModelAndView addAdminPanel(@ModelAttribute("createAdminForm") CreateAdminForm form, @RequestParam(name = "page", defaultValue = "1") int page,
                                      @RequestParam(name = "query", defaultValue = "") final String query) {

        return new MyModelAndView.Builder("moderation_panel_add_admin", "pageTitle.moderationPanel", TextType.INTERCODE)
                .withObject("isOwner", securityService.getCurrentUser().get().getRoles().contains(Role.ROLE_OWNER))
                .withObject("usersPage", ownerService.getAdmins(page, query))
                .withObject("item", "manageAdmins")
                .build();
    }
}
