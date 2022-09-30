package ar.edu.itba.paw.webapp.auth;

import ar.edu.itba.paw.model.exeptions.NewsNotFoundException;
import ar.edu.itba.paw.model.exeptions.UserNotAuthorized;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OwnerCheck {

    private final SecurityService securityService;
    private final NewsService newsService;

    @Autowired
    public OwnerCheck(SecurityService securityService, NewsService newsService) {
        this.securityService = securityService;
        this.newsService = newsService;
    }

    public boolean checkIfCurrentUserIsOwner(long newsId) {
        System.out.println("%Holaaaaaaa" + newsId);
        return newsService.getSimpleNewsById(newsId).orElseThrow(NewsNotFoundException::new).getCreatorId()==securityService.getCurrentUser().orElseThrow(UserNotAuthorized::new).getId();
    }
}