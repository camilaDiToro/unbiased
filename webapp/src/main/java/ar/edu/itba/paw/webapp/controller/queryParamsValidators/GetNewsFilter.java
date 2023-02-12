package ar.edu.itba.paw.webapp.controller.queryParamsValidators;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.Category;
import ar.edu.itba.paw.model.news.News;
import ar.edu.itba.paw.model.news.NewsOrder;
import ar.edu.itba.paw.model.news.TimeConstraint;
import ar.edu.itba.paw.model.user.ProfileCategory;
import ar.edu.itba.paw.service.AdminService;
import ar.edu.itba.paw.service.NewsService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.api.exceptions.InvalidGetNewsFilter;
import ar.edu.itba.paw.webapp.api.exceptions.InvalidRequestParamsException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public enum GetNewsFilter {

    NO_FILTER() {
        @Override
        public GetNewsParams validateParams(UserService userService, String category, String order, String time, String search, Long id) {
            NewsOrder newsOrder = (order == null || order.equals("")) ? NewsOrder.TOP : NewsOrder.getByValue(order);
            return new GetNewsParams(   Category.getByValue(category), newsOrder, null,
                                        TimeConstraint.getByValue(time), search, null);
        }

        @Override
        public Page<News> getNews(NewsService newsService, AdminService adminService, int page, GetNewsParams params) {
            return newsService.getNews(Optional.empty(), page, params.getCategoryObj(),
                    params.getOrderObj(), params.getTimeObj(), params.getSearch());
        }
    },

    PINNED_BY(){
        @Override
        public GetNewsParams validateParams(UserService userService, String category, String order, String time, String search, Long id) {
            return validateIdParam(userService, category, order, time, search, id);
        }

        @Override
        public Page<News> getNews(NewsService newsService, AdminService adminService, int page, GetNewsParams params) {
            Optional<News> news = newsService.getPinnedByUserNews(params.getUser());
            List<News> list = Collections.emptyList();
            if(news.isPresent()){
                 list = Collections.singletonList(news.get());
            }
            return new Page<>(list, 1, 1);
        }
    },
    REPORTED_BY(){
        @Override
        public GetNewsParams validateParams(UserService userService, String category, String order, String time, String search, Long id) {
            return validateIdParam(userService, category, order, time, search, id);
        }

        @Override
        public Page<News> getNews(NewsService newsService, AdminService adminService, int page, GetNewsParams params) {
            return new Page<>(adminService.getReportedByUserNews(params.getUser()), 1, 1);
        }
    },
    REPORTED(){
        @Override
        public GetNewsParams validateParams(UserService userService, String category, String order, String time, String search, Long id) {
            ReportOrder reportOrder = (order == null || order.equals("")) ? ReportOrder.REP_COUNT_DESC : ReportOrder.getByValue(order);
            return new GetNewsParams(null, null, reportOrder, null, null, null);
        }

        @Override
        public Page<News> getNews(NewsService newsService, AdminService adminService, int page, GetNewsParams params) {
            return adminService.getReportedNews(page, params.getReportOrder());
        }
    },

    LIKED_BY() {
        @Override
        public GetNewsParams validateParams(UserService userService, String category, String order, String time, String search, Long id) {
            return validateIdParam(userService, category, order, time, search, id);
        }

        @Override
        public Page<News> getNews(NewsService newsService, AdminService adminService, int page, GetNewsParams params) {
            return newsService.getNewsForUserProfile(Optional.empty(), page, params.getOrderObj(), params.getUser(), ProfileCategory.UPVOTED);
        }

    },

    DISLIKED_BY() {
        @Override
        public GetNewsParams validateParams(UserService userService, String category, String order, String time, String search, Long id) {
            return validateIdParam(userService, category, order, time, search, id);
        }

        @Override
        public Page<News> getNews(NewsService newsService, AdminService adminService, int page, GetNewsParams params) {
            return newsService.getNewsForUserProfile(Optional.empty(), page, params.getOrderObj(), params.getUser(), ProfileCategory.DOWNVOTED);
        }
    },

    PUBLISHED_BY() {
        @Override
        public GetNewsParams validateParams(UserService userService, String category, String order, String time, String search, Long id) {
            return validateIdParam(userService, category, order, time, search, id);
        }

        @Override
        public Page<News> getNews(NewsService newsService, AdminService adminService, int page, GetNewsParams params) {
            return newsService.getNewsForUserProfile(Optional.empty(), page, params.getOrderObj(), params.getUser(), ProfileCategory.MY_POSTS);
        }
    },

    SAVED_BY() {
        @Override
        public GetNewsParams validateParams(UserService userService, String category, String order, String time, String search, Long id) {
            return validateIdParam(userService, category, order, time, search, id);
        }

        @Override
        public Page<News> getNews(NewsService newsService, AdminService adminService, int page, GetNewsParams params) {
            return newsService.getNewsForUserProfile(Optional.empty(), page, params.getOrderObj(), params.getUser(), ProfileCategory.SAVED);
        }
    };

    public abstract GetNewsParams validateParams(UserService userService, String category, String order, String time, String search, Long id);

    public abstract Page<News> getNews(NewsService newsService, AdminService adminService, int page, GetNewsParams params);

    public static GetNewsFilter fromString(String param) {
        try {
            return valueOf(param.toUpperCase());
        } catch (Exception e) {
            throw new InvalidGetNewsFilter(param);
        }
    }

    private static GetNewsParams validateIdParam(UserService userService, String category, String order, String time, String search, Long id) {
        NewsOrder newsOrder = (order == null || order.equals("")) ? NewsOrder.TOP : NewsOrder.getByValue(order);
        if(id == null){
            throw new InvalidRequestParamsException("The requested filter requires an \"id\" query param");
        }
        else if(id <= 0){
            throw new InvalidRequestParamsException("id param must be greater than 0");
        }

        return new GetNewsParams(null, newsOrder, null,null, null,
                userService.getUserById(id).orElseThrow(()-> new UserNotFoundException(id)));
    }

    GetNewsFilter() {
    }
}