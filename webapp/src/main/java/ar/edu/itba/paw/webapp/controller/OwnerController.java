package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.news.TextType;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.service.OwnerService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.webapp.form.CreateAdminForm;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class OwnerController {

    private final OwnerService ownerService;

    private final SecurityService securityService;


    @Autowired
    public OwnerController(OwnerService ownerService, SecurityService securityService) {
        this.securityService = securityService;
        this.ownerService = ownerService;

    }

    @RequestMapping(value = "/owner/add_admin", method = RequestMethod.POST)
    public ModelAndView addAdmin(@Valid @ModelAttribute("createAdminForm") CreateAdminForm form, final BindingResult errors) {
        if (errors.hasErrors()){
            return addAdminPanelBase(form, 1, "", true);
        }

        ownerService.makeUserAdmin(form.getEmail());
        return new ModelAndView("redirect:/owner/add_admin_page");
    }

    @RequestMapping(value = "/owner/delete_admin_page/{userId:[0-9]+}", method = RequestMethod.GET) // Pongo GET porque en html solo hay post y get
    public ModelAndView deleteAdmin(@PathVariable("userId") long userId) {

        ownerService.deleteUserAdmin(userId);
        return new ModelAndView("redirect:/owner/add_admin_page");

    }

    public ModelAndView addAdminPanelBase(CreateAdminForm form,  int page,
                                      final String query, boolean hasErrors) {

        return new MyModelAndView.Builder("moderation_panel_add_admin", "pageTitle.moderationPanel", TextType.INTERCODE)
                .withObject("isOwner", securityService.getCurrentUser().get().getRoles().contains(Role.ROLE_OWNER))
                .withObject("usersPage", ownerService.getAdmins(page, query))
                .withObject("item", "manageAdmins")
                .withObject("hasErrors", hasErrors)
                .build();
    }

    @RequestMapping(value = "/owner/add_admin_page", method = RequestMethod.GET)
    public ModelAndView addAdminPanel(@ModelAttribute("createAdminForm") CreateAdminForm form, @RequestParam(name = "page", defaultValue = "1") int page,
                                      @RequestParam(name = "query", defaultValue = "") final String query) {

        return addAdminPanelBase(form, page, query, false);
    }
}
