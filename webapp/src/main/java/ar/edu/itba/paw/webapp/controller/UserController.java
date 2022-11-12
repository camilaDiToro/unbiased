package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.model.user.*;
import ar.edu.itba.paw.service.*;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.auth.OwnerCheck;
import ar.edu.itba.paw.webapp.form.ResendVerificationEmail;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Locale;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final NewsService newsService;
    private final OwnerCheck ownerCheck;

    private final SecurityService securityService;

    @Autowired
    public UserController(UserService userService, SecurityService securityService, NewsService newsService, OwnerCheck ownerCheck) {
        this.securityService = securityService;
        this.userService = userService;
        this.newsService = newsService;
        this.ownerCheck = ownerCheck;
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createForm(@ModelAttribute("registerForm") final UserForm userForm) {
        return new MyModelAndView.Builder("register", "pageTitle.register", TextType.INTERCODE)
                .build();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm userForm, final BindingResult errors) {

        if (errors.hasErrors()) {
            return createForm(userForm);
        }

        User.UserBuilder userBuilder = new User.UserBuilder(userForm.getEmail()).pass(userForm.getPassword());

        userService.create(userBuilder);
        return new ModelAndView("email_verification_pending");
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}/pingNews/{newsId:[0-9]+}", method = RequestMethod.POST)
    public ModelAndView pingNews(@PathVariable("userId") final long userId,@PathVariable("newsId") final long newsId) {
        User currentUser = securityService.getCurrentUser().orElseThrow(UserNotFoundException::new);
        userService.pingNewsToggle(currentUser, newsService.getById(newsId).orElseThrow(NewsNotFoundException::new));

        return new ModelAndView("redirect:/profile/" + userId);
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView profileRedirect(@PathVariable("userId") long userId) {
        return new ModelAndView("redirect:/profile/" + userId + "/TOP");
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}/follow", method = RequestMethod.GET)
    public ModelAndView profileFollow(@PathVariable("userId") long userId) {
        User currentUser = securityService.getCurrentUser().orElseThrow(UserNotFoundException::new);
        userService.followUser(currentUser, userId);
        return new ModelAndView("redirect:/profile/" + userId + "/TOP");
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}/unfollow", method = RequestMethod.GET)
    public ModelAndView profileUnfollow(@PathVariable("userId") long userId) {
        User currentUser = securityService.getCurrentUser().orElseThrow(UserNotFoundException::new);
        userService.unfollowUser(currentUser, userId);
        return new ModelAndView("redirect:/profile/" + userId + "/TOP");
    }



    @PreAuthorize("@ownerCheck.checkSavedNewsAccess(#category, #userId)")
    @RequestMapping(value = "/profile/{userId:[0-9]+}/{newsOrder}", method = RequestMethod.GET)
    public ModelAndView profile(@PathVariable("userId") long userId,
                                @PathVariable("newsOrder") String newsOrder,
                                @ModelAttribute("userProfileForm") final UserProfileForm userProfileForm,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "category", defaultValue = "") String category,
                                @RequestParam(name = "hasErrors", defaultValue = "false") boolean hasErrors) {
        Optional<User> user =  securityService.getCurrentUser();
        User profileUser = userService.getRegisteredUserById(userId).orElseThrow(UserNotFoundException::new);

        CategoryStatistics categoryStatistics = newsService.getCategoryStatistics(userId);

        Iterable<ProfileCategory> profileCategoryList = newsService.getProfileCategories(profileUser);
        ProfileCategory catObject;
        if (category.equals("")){
            catObject = profileCategoryList.iterator().next();
        }
        else {
            catObject = userService.getProfileCategory(user, category, profileUser);
        }

//        userService.updateEmailSettings(user.get(),true, false, true, true);

        Page<News> fullNews = newsService.getNewsForUserProfile(page, NewsOrder.getByValue(newsOrder), profileUser, catObject);
        boolean isMyProfile = profileUser.equals(user.orElse(null));

        MyModelAndView.Builder mavBuilder = new MyModelAndView.Builder("profile", "pageTitle.profile", TextType.INTERCODE)
                .withObject("orders", NewsOrder.values())
                .withObject("orderBy", newsOrder)
                .withObject("categories", profileCategoryList)
                .withObject("newsPage", fullNews)
                .withObject("statisticsMap", categoryStatistics.getStatiscticsMap())
                .withObject("pingedNews", profileUser.getPingedNews())
                .withObject("isMyProfile", isMyProfile)
                .withObject("profileUser", profileUser)
                .withObject("newsCategories", Category.getTrueCategories())
                .withObject("userId", userId)
                .withObject("mailOptions", MailOption.values())
                .withObject("hasErrors", hasErrors)
                .withObject("following", userService.getFollowingCount(userId))
                .withObject("followers", userService.getFollowersCount(userId))
                .withObject("isJournalist", profileUser.getRoles().contains(Role.ROLE_JOURNALIST))
                .withStringParam(profileUser.toString());
        if(user.isPresent() && isMyProfile) {
            User loggedUser = user.get();
            mavBuilder.withObject("isFollowing", userService.isFollowing(loggedUser, userId));
            mavBuilder.withObject("getMailOptionByEnum", loggedUser.getEmailSettings().getValueByEnum());
        }

        mavBuilder.withObject("category", catObject);

        return mavBuilder.build();

    }

    @RequestMapping(value = "/profile", method = RequestMethod.POST)
    public ModelAndView profilePicture(@Valid @ModelAttribute("userProfileForm") final UserProfileForm userProfileForm, final BindingResult errors) throws IOException {
        User user = securityService.getCurrentUser().orElseThrow(UserNotFoundException::new);
        long userId = user.getId();
        if (errors.hasErrors()) {
            return profile(userId, "NEW",userProfileForm, 1, "MY_POSTS", true);
        }

        userService.updateProfile(userId, userProfileForm.getUsername(),
                userProfileForm.getImage().getBytes(), userProfileForm.getImage().getContentType(), userProfileForm.getDescription());
        userService.updateEmailSettings(user, MailOption.getEnumCollection(userProfileForm.getMailOptions()));
        return new ModelAndView("redirect:/profile/" + user.getUserId());
    }

    @RequestMapping("/verify_email")
    public ModelAndView verifyEmail(@RequestParam(name = "token") final String token) {
        VerificationToken.Status status = userService.verifyUserEmail(token);
        ModelAndView mav;
        if(status.equals(VerificationToken.Status.SUCCESFFULLY_VERIFIED)){
            mav = new MyModelAndView.Builder("email_verified", "pageTitle.emailVerified", TextType.INTERCODE)
                    .build();
        }else{
            mav = new ModelAndView("redirect:/email_not_verified/"+status.getStatus().toLowerCase(Locale.ROOT));
        }
        return mav;
    }

    @RequestMapping(value = "/email_not_verified/{status:expired|not_exists}", method = RequestMethod.GET)
    public ModelAndView resendVerificationEmail(@ModelAttribute("resendEmailForm") final ResendVerificationEmail userForm, @PathVariable("status") String status) {

        MyModelAndView.Builder mavBuilder = new MyModelAndView.Builder("email_not_verified", "pageTitle.emailVerified", TextType.INTERCODE)
                .withObject("status",status);

        if(status.equals("expired")){
            mavBuilder.withObject("errorMsg",VerificationToken.Status.EXPIRED.getCode());
        }else{
            mavBuilder.withObject("errorMsg",VerificationToken.Status.NOT_EXISTS.getCode());
        }
        return mavBuilder.build();
    }

    @RequestMapping(value = "/email_not_verified/{status:expired|not_exists}", method = RequestMethod.POST)
    public ModelAndView resendVerificationEmail(@Valid @ModelAttribute("resendEmailForm") final ResendVerificationEmail userForm, final BindingResult errors, @PathVariable("status") String status) {
        if (errors.hasErrors()) {
            return resendVerificationEmail(userForm, status);
        }

        VerificationToken.Status s = userService.resendEmailVerification(userForm.getEmail());
        if(s.equals(VerificationToken.Status.ALREADY_VERIFIED)){
            return new ModelAndView("email_already_verified");
        }
        return new ModelAndView("email_verification_pending");
    }



    @RequestMapping( value = "/profile/{userId:[0-9]+}/image", method = {RequestMethod.GET},
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] profileImage(@PathVariable("userId") long userId) {
        return userService.getUserById(userId).orElseThrow(UserNotFoundException::new).getImage().getBytes();
    }

}