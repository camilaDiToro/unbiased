package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorizedException;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.controller.queryParamsValidators.GetNewsFilter;
import ar.edu.itba.paw.webapp.controller.queryParamsValidators.GetNewsParams;
import ar.edu.itba.paw.webapp.form.ReportNewsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OwnerCheck {

    private final SecurityService securityService;
    private final NewsService newsService;

    private final UserService userService;

    @Autowired
    public OwnerCheck(SecurityService securityService, NewsService newsService, UserService userService) {
        this.securityService = securityService;
        this.newsService = newsService;
        this.userService = userService;
    }

    public boolean newsOwnership(long newsId, long userId) {
        long creatorId = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId)).getCreatorId();
        Optional<User> mbUser = securityService.getCurrentUser();
        if(!mbUser.isPresent()){
            return false;
        }
        long currentUserId = mbUser.get().getId();
        return creatorId==currentUserId && currentUserId == userId;
    }

    public boolean canDeleteNews(long newsId) {
        return checkNewsOwnership(newsId) || isAdmin();
    }

    private boolean isAdmin() {
        return userService.isUserAdmin(securityService.getCurrentUser().orElseThrow(()->new UserNotAuthorizedException("User should be logged in")));
    }


    public boolean canDeleteComment(long commentId) {
        return checkCommentOwnership(commentId) || isAdmin();
    }

    public boolean checkCommentOwnership(long commentId) {
        Optional<User> mbUser = securityService.getCurrentUser();
        if(!mbUser.isPresent()){
            return false;
        }
        return newsService.getCommentById(commentId).orElseThrow(()-> new CommentNotFoundException(commentId))
                .getUser().getId()==mbUser.get().getId();
    }

    public boolean checkNewsOwnership(long newsId) {
        Optional<User> mbUser = securityService.getCurrentUser();
        if(!mbUser.isPresent()){
            return false;
        }
        return newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(newsId)).getUser().getId()==mbUser.get().getId();
    }

    public boolean checkSavedNewsAccess(String category, long userId){
        if(!category.equals(ProfileCategory.SAVED.getDescription())){
            return true;
        }
        Optional<User> mayBeUser = securityService.getCurrentUser();
        return mayBeUser.filter(user -> user.getId() == userId).isPresent();
    }



    public boolean userMatches(long userId){
        Optional<User> mayBeUser = securityService.getCurrentUser();
        return mayBeUser.filter(user -> user.getId() == userId).isPresent();
    }

    public boolean userMatches(ReportNewsForm reportNewsForm){
        if(reportNewsForm == null){
            return false;
        }
        Optional<User> mayBeUser = securityService.getCurrentUser();
        return mayBeUser.filter(user -> user.getId() == reportNewsForm.getUserId()).isPresent();
    }

    public boolean canGetSavedNews(String filter, Long userId){
        if(!GetNewsFilter.SAVED_BY.toString().equalsIgnoreCase(filter)){
            return true;
        }
        if(userId == null){
            return false;
        }
        Optional<User> mayBeUser = securityService.getCurrentUser();
        return mayBeUser.filter(user -> user.getId() == userId).isPresent();
    }
}