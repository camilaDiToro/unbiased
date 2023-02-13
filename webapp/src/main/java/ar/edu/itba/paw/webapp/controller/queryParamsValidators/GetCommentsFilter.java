package ar.edu.itba.paw.webapp.controller.queryParamsValidators;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.admin.ReportOrder;
import ar.edu.itba.paw.model.exeptions.UserNotFoundException;
import ar.edu.itba.paw.model.news.*;
import ar.edu.itba.paw.service.AdminService;
import ar.edu.itba.paw.service.CommentService;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.api.exceptions.InvalidGetCommentsFilter;
import ar.edu.itba.paw.webapp.api.exceptions.InvalidRequestParamsException;

public enum GetCommentsFilter {


    REPORTED_BY() {
        @Override
        public GetCommentsParams validateParams(UserService userService, String order, Long id) {
            return GetCommentsFilter.validateIdParam(userService, id);
        }

        @Override
        public Page<Comment> getComments(CommentService commentService, AdminService adminService, int page, GetCommentsParams params) {
            return adminService.getReportedByUserComments(page, params.getUser());
        }
    },
    LIKED_BY() {
        @Override
        public GetCommentsParams validateParams(UserService userService, String order, Long id) {
            return GetCommentsFilter.validateIdParam(userService, id);
        }

        @Override
        public Page<Comment> getComments(CommentService commentService, AdminService adminService, int page, GetCommentsParams params) {
            return new Page<>(commentService.getCommentsUpvotedByUser(params.getUser()), 1, 1);
        }
    },
    DISLIKED_BY() {
        @Override
        public GetCommentsParams validateParams(UserService userService, String order, Long id) {
            return GetCommentsFilter.validateIdParam(userService, id);
        }

        @Override
        public Page<Comment> getComments(CommentService commentService, AdminService adminService, int page, GetCommentsParams params) {
            return new Page<>(commentService.getCommentsDownvotedByUser(params.getUser()), 1, 1);
        }
    },
    REPORTED() {
        @Override
        public GetCommentsParams validateParams(UserService userService, String order, Long id) {
            ReportOrder reportOrder = (order == null || order.equals("")) ? ReportOrder.REP_COUNT_DESC : ReportOrder.getByValue(order);
            return new GetCommentsParams(null, reportOrder, null, 0);
        }

        @Override
        public Page<Comment> getComments(CommentService commentService, AdminService adminService, int page, GetCommentsParams params) {
            return commentService.getComments(0,page,null,true,params.getReportOrder());
        }
    },
    NEWS_COMMENTS() {
        @Override
        public GetCommentsParams validateParams(UserService userService, String order, Long id) {
            if(id == null){
                throw new InvalidRequestParamsException("The requested filter \"news_comments\"requires an \"id\" query param");
            }
            else if(id <= 0){
                throw new InvalidRequestParamsException("id param must be greater than 0");
            }
            NewsOrder newsOrder = (order == null || order.equals("")) ? NewsOrder.TOP : NewsOrder.getByValue(order);
            return new GetCommentsParams(newsOrder, null, null, id);
        }

        @Override
        public Page<Comment> getComments(CommentService commentService, AdminService adminService, int page, GetCommentsParams params) {
            return commentService.getComments(params.getNewsId() ,page, params.getNewsOrder(),false,params.getReportOrder());
        }
    };


    public abstract GetCommentsParams validateParams(UserService userService, String order, Long id);

    public abstract Page<Comment> getComments(CommentService commentService, AdminService adminService, int page, GetCommentsParams params);

    public static GetCommentsFilter fromString(String param) {
        try {
            return valueOf(param.toUpperCase());
        } catch (Exception e) {
            throw new InvalidGetCommentsFilter(param);
        }
    }

    private static GetCommentsParams validateIdParam(UserService userService, Long id) {
        if(id == null){
            throw new InvalidRequestParamsException("The requested filter requires an \"id\" query param");
        }
        else if(id <= 0){
            throw new InvalidRequestParamsException("id param must be greater than 0");
        }

        return new GetCommentsParams(null, null,
                userService.getUserById(id).orElseThrow(()-> new UserNotFoundException(id)), 0);
    }

    GetCommentsFilter() {
    }

}
