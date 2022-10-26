package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.TextType;
import ar.edu.itba.paw.model.user.Role;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.webapp.form.CreateAdminForm;
import ar.edu.itba.paw.webapp.model.MAVBuilderSupplier;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class OwnerController {

    private final MAVBuilderSupplier mavBuilderSupplier;
    private final OwnerService ownerService;
    private final UserService userService;

    private final SecurityService securityService;

    @Autowired
    public OwnerController(SecurityService securityService, OwnerService ownerService, UserService userService) {
        mavBuilderSupplier = (view, title, textType) -> new MyModelAndView.Builder(view, title, textType, securityService);
        this.ownerService = ownerService;
        this.userService = userService;
        this.securityService = securityService;
    }

    @RequestMapping(value = "/owner/add_admin", method = RequestMethod.POST)
    public ModelAndView addAdmin(@Valid @ModelAttribute("createAdminForm") CreateAdminForm form, final BindingResult errors) {
        if (errors.hasErrors()){
            return addAdminPanel(form);
        }

        ownerService.makeUserAdmin(userService.findByEmail(form.getEmail()).get());
        return mavBuilderSupplier.supply("moderation_panel_add_admin", "pageTitle.moderationPanel", TextType.INTERCODE)
                .withObject("addedAdmin", true)
                .withObject("item", "manageAdmins")
                .withObject("isOwner", securityService.getCurrentUser().get().getRoles().contains(Role.ROLE_OWNER))
                .build();
    }

    @RequestMapping(value = "/owner/delete_admin_page/{userId:[0-9]+}", method = RequestMethod.DELETE)
    public ModelAndView deleteAdmin(@PathVariable("userId") long userId) {

        ownerService.deleteUserAdmin(userService.getUserById(userId).orElseThrow(UserNotFoundException::new));
        //TODO: return the correct view
        return mavBuilderSupplier.supply("moderation_panel_add_admin", "pageTitle.moderationPanel", TextType.INTERCODE)
                .build();
    }

    @RequestMapping(value = "/owner/add_admin_page", method = RequestMethod.GET)
    public ModelAndView addAdminPanel(@ModelAttribute("createAdminForm") CreateAdminForm form) {

        return mavBuilderSupplier.supply("moderation_panel_add_admin", "pageTitle.moderationPanel", TextType.INTERCODE)
                .build();
    }
}
