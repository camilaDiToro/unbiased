package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.model.*;
import ar.edu.itba.paw.model.exeptions.ImageNotFoundException;
import ar.edu.itba.paw.model.exeptions.InvalidCategoryException;
import ar.edu.itba.paw.service.ImageService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.UserForm;
import ar.edu.itba.paw.webapp.form.UserProfileForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

@Controller
public class UserController {

    private final UserService userService;

    private final NewsService newsService;
    private final ImageService imageService;

    private final SecurityService securityService;

    private final MAVSupplier mavSupplier;


    @Autowired
    public UserController(UserService userService, ImageService imageService, SecurityService securityService, NewsService newsService) {
        this.userService = userService;
        this.imageService = imageService;
        this.securityService = securityService;
        this.newsService = newsService;

         mavSupplier = (view, title, textType) -> new MyModelAndView(view, title, textType, securityService.getCurrentUser());

//        this.
    }


    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("login");
    }

    @RequestMapping(value = "/create", method = RequestMethod.GET)
    public ModelAndView createForm(@ModelAttribute("registerForm") final UserForm userForm) {
        final ModelAndView mav = mavSupplier.supply("registerForm", "pageTitle.create", TextType.INTERCODE);
        return mav;
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ModelAndView create(@Valid @ModelAttribute("registerForm") final UserForm userForm, final BindingResult errors) {

        if (errors.hasErrors()) {
            return createForm(userForm);
        }

        User.UserBuilder userBuilder = new User.UserBuilder(userForm.getEmail()).pass(userForm.getPassword());

        final User user = userService.create(userBuilder);
        return new ModelAndView("redirect:/profile/" + user.getId());
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}", method = RequestMethod.GET)
    public ModelAndView profileRedirect(@PathVariable("userId") long userId) {
        final ModelAndView mav = new ModelAndView("redirect:/profile/" + userId + "/TOP");


        return mav;
    }


    @RequestMapping(value = "/profile/{userId:[0-9]+}/{newsOrder:TOP|NEW}", method = RequestMethod.GET)
    public ModelAndView profile(@PathVariable("userId") long userId,
                                @PathVariable("newsOrder") String newsOrder,
                                @Valid @ModelAttribute("userProfileForm") final UserProfileForm userProfileForm,
                                @RequestParam(name = "page", defaultValue = "1") int page,
                                @RequestParam(name = "category", defaultValue = "MY_POSTS") String category) {
        final ModelAndView mav = mavSupplier.supply("profile", "pageTitle.profile", TextType.INTERCODE);
        Optional<User> user =  securityService.getCurrentUser();
        User profileUser = userService.getUserById(userId).orElseThrow(UserNotFoundException::new);
        Page<FullNews> fullNews = newsService.getNewsForUserProfile(page, "TOP", profileUser.getId(), category);
        List<FullNews> newsContent = fullNews.getContent();
        mav.addObject("user",user.orElse(null));

        mav.addObject("profileUser", profileUser);

        mav.addObject("isMyProfile", profileUser.equals(user.orElse(null)));

        mav.addObject("newsPage", fullNews);

        mav.addObject("orderBy", newsOrder);

        try {
            mav.addObject("category", ProfileCategory.valueOf(category));
        } catch(IllegalArgumentException e) {
            throw new InvalidCategoryException();
        }

        mav.addObject("orders", NewsOrder.values());

        mav.addObject("categories", ProfileCategory.values());


        Map<Long, Rating> ratingMap = new HashMap<>();
        Map<Long, Boolean> savedMap = new HashMap<>();

        for (FullNews news : newsContent) {
            News article = news.getNews();
            long newsId = article.getNewsId();
            ratingMap.put(newsId, user.map(u -> newsService.upvoteState(article, u)).orElse(Rating.NO_RATING));
            savedMap.put(newsId, user.map(u -> newsService.isSaved(article, u)).orElse(false));
        }

        mav.addObject("ratingMap", ratingMap);
        mav.addObject("savedMap", savedMap);

        return mav;
    }

    @RequestMapping(value = "/profile/{userId:[0-9]+}", method = RequestMethod.POST)
    public ModelAndView profilePicture(@PathVariable("userId") long userId, @Valid @ModelAttribute("userProfileForm") final UserProfileForm userProfileForm, final BindingResult errors) throws IOException {
        if (errors.hasErrors()) {
            return profile(userId, "NEW",userProfileForm, 1, "MY_POSTS");
        }
        Long imageId = null;
        if(!userProfileForm.getImage().isEmpty() && userProfileForm.getImage().getBytes().length != 0){
            imageId = imageService.uploadImage(userProfileForm.getImage().getBytes(), userProfileForm.getImage().getContentType());
        }
        userService.updateProfile(userId, userProfileForm.getUsername(), imageId);
        return new ModelAndView("redirect:/profile/" + userId);
    }

    @RequestMapping("/verify_email")
    public ModelAndView verifyEmail(@RequestParam(name = "token") final String token) {
        userService.verifyUserEmail(token);
        return mavSupplier.supply("email_verified", "pageTitle.emailVerified", TextType.LITERAL);
    }

    @RequestMapping( value = "/profile/{imageId:[0-9]+}/image", method = {RequestMethod.GET},
            produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] profileImage(@PathVariable(value = "imageId") long imageId) {
        return imageService.getImageById(imageId).orElseThrow(ImageNotFoundException::new).getBytes();
    }

}