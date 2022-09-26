package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.ImageNotFoundException;
import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.model.news.FullNews;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TextType;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.ResendVerificationEmail;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import ar.edu.itba.paw.webapp.model.MAVBuilderSupplier;
import ar.edu.itba.paw.webapp.model.MyModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final NewsService newsService;
    private final ImageService imageService;
    private final SecurityService securityService;
    private final MAVBuilderSupplier mavBuilderSupplier;

    @Autowired
    public UserController(UserService userService, ImageService imageService, SecurityService securityService, NewsService newsService) {
        this.userService = userService;
        this.imageService = imageService;
        this.securityService = securityService;
        this.newsService = newsService;
        mavBuilderSupplier = (view, title, textType) -> new MyModelAndView.Builder(view, title, textType, securityService);
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createForm(@ModelAttribute("registerForm") final UserForm userForm) {
        return mavBuilderSupplier.supply("register", "pageTitle.register", TextType.INTERCODE)
                .build();
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm userForm, final BindingResult errors) {

        if (errors.hasErrors()) {
            return createForm(userForm);
        }

        User.UserBuilder userBuilder = new User.UserBuilder(userForm.getEmail()).pass(userForm.getPassword());

        final User user = userService.create(userBuilder);
        return new ModelAndView("email_verification_pending");
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView profileRedirect(@PathVariable("userId") long userId) {
        final ModelAndView mav = new ModelAndView("redirect:/profile/" + userId + "/TOP");
        Page<FullNews> ln = newsService.getRecommendation(1,userService.getUserById(userId).get());
        return mav;
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}/follow", method = RequestMethod.GET)
    public ModelAndView profileFollow(@PathVariable("userId") long userId) {
        userService.followUser(userId);
        final ModelAndView mav = new ModelAndView("redirect:/profile/" + userId + "/TOP");
        return mav;
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}/unfollow", method = RequestMethod.GET)
    public ModelAndView profileUnfollow(@PathVariable("userId") long userId) {
        userService.unfollowUser(userId);
        final ModelAndView mav = new ModelAndView("redirect:/profile/" + userId + "/TOP");
        return mav;
    }


    @RequestMapping(value = "/profile/{userId:[0-9]+}/{newsOrder:TOP|NEW}", method = RequestMethod.GET)
    public ModelAndView profile(@PathVariable("userId") long userId,
                                @PathVariable("newsOrder") String newsOrder,
                                @Valid @ModelAttribute("userProfileForm") final UserProfileForm userProfileForm,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "category", defaultValue = "MY_POSTS") String category,
                                @RequestParam(name = "hasErrors", defaultValue = "false") boolean hasErrors) {
        Optional<User> user =  securityService.getCurrentUser();
        User profileUser = userService.getRegisteredUserById(userId).orElseThrow(UserNotFoundException::new);
        Page<FullNews> fullNews = newsService.getNewsForUserProfile(page, newsOrder, profileUser, category);
        boolean isMyProfile = profileUser.equals(user.orElse(null));


        MyModelAndView.Builder mavBuilder = mavBuilderSupplier.supply("profile", "pageTitle.profile", TextType.INTERCODE)
                .withObject("orders", NewsOrder.values())
                .withObject("orderBy", newsOrder)
                .withObject("categories", Arrays.stream(ProfileCategory.values()).filter(c -> isMyProfile || !c.equals(ProfileCategory.SAVED)).toArray())
                .withObject("newsPage", fullNews)
                .withObject("isMyProfile", isMyProfile)
                .withObject("profileUser", profileUser)
                .withObject("userId", userId)
                .withObject("hasErrors", hasErrors)
                .withStringParam(profileUser.toString());
        if(securityService.getCurrentUser().isPresent()) {
                   mavBuilder.withObject("isFollowing", userService.isFollowing(userId));
        }

        try {
            mavBuilder.withObject("category", ProfileCategory.valueOf(category));
        } catch(IllegalArgumentException e) {
            throw new InvalidCategoryException();
        }
        return mavBuilder.build();
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}", method = RequestMethod.POST)
    public ModelAndView profilePicture(@PathVariable("userId") long userId, @Valid @ModelAttribute("userProfileForm") final UserProfileForm userProfileForm, final BindingResult errors) throws IOException {
        if (errors.hasErrors()) {
            return profile(userId, "NEW",userProfileForm, 1, "MY_POSTS", true);
        }
        Long imageId = imageService.uploadImage(userProfileForm.getImage().getBytes(), userProfileForm.getImage().getContentType());

        userService.updateProfile(userId, userProfileForm.getUsername(), imageId);
        return new ModelAndView("redirect:/profile/" + userId);
    }

    @RequestMapping("/verify_email")
    public ModelAndView verifyEmail(@RequestParam(name = "token") final String token) {
        VerificationToken.Status status = userService.verifyUserEmail(token);
        ModelAndView mav;
        if(status.equals(VerificationToken.Status.SUCCESFFULLY_VERIFIED)){
            mav = mavBuilderSupplier.supply("email_verified", "pageTitle.emailVerified", TextType.LITERAL)
                    .build();
        }else{
            mav = new ModelAndView("redirect:/email_not_verified/"+status.getStatus().toLowerCase(Locale.ROOT));
        }
        return mav;
    }

    @RequestMapping(value = "/email_not_verified/{status:expired|not_exists}", method = RequestMethod.GET)
    public ModelAndView resendVerificationEmail(@ModelAttribute("resendEmailForm") final ResendVerificationEmail userForm, @PathVariable("status") String status) {

        MyModelAndView.Builder mavBuilder = mavBuilderSupplier.supply("email_not_verified", "pageTitle.emailVerified", TextType.INTERCODE)
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

        userService.resendEmailVerification(userForm.getEmail());
        return new ModelAndView("email_verification_pending");
    }



    @RequestMapping( value = "/profile/{imageId:[0-9]+}/image", method = {RequestMethod.GET},
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] profileImage(@PathVariable(value = "imageId") long imageId) {
        return imageService.getImageById(imageId).orElseThrow(ImageNotFoundException::new).getBytes();
    }

}