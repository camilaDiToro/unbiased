package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.exeptions.CommentNotFoundException;
import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class OwnerCheck {

    private final SecurityService securityService;
    private final NewsService newsService;

    @Autowired
    public OwnerCheck(SecurityService securityService, NewsService newsService) {
        this.securityService = securityService;
        this.newsService = newsService;
    }

    public boolean newsOwnership(long newsId, long userId) {
        long creatorId = newsService.getById(newsId).orElseThrow(()-> new NewsNotFoundException(String.format(NewsNotFoundException.ID_MSG, newsId))).getCreatorId();
        long currentUserId = securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new).getId();
        return creatorId==currentUserId && currentUserId == userId;
    }

    public boolean checkCommentOwnership(long commentId) {
        return newsService.getCommentById(commentId).orElseThrow(CommentNotFoundException::new).getUser().getId()==securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new).getId();
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
}