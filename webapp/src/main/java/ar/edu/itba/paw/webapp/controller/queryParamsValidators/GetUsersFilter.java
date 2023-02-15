package ar.edu.itba.paw.webapp.controller.queryParamsValidators;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.api.exceptions.InvalidGetUsersFilter;
import ar.edu.itba.paw.webapp.api.exceptions.InvalidRequestParamsException;
import org.springframework.stereotype.Component;

public enum GetUsersFilter {

    SEARCH() {
        @Override
        public Page<User> getUsers(UserService userService, int page, String search, Long id) {
            return userService.searchUsers(page,search);
        }
    },
    TOP_CREATORS() {
        @Override
        public Page<User> getUsers(UserService userService, int page, String search, Long id) {
            return userService.getTopCreators();
        }
    },
    ADMINS() {
        @Override
        public Page<User> getUsers(UserService userService, int page, String search, Long id) {
            return userService.getAdmins(page,search);
        }
    },
    NOT_ADMINS() {
        @Override
        public Page<User> getUsers(UserService userService, int page, String search, Long id) {
            return userService.getNotAdmins(page,search);
        }
    },
    FOLLOWING(){
        @Override
        public void validateParams(String search, Long id) {
            if(id == null){
                throw new InvalidRequestParamsException("The requested filter requires an \"id\" query param");
            }
            else if(id <= 0){
                throw new InvalidRequestParamsException("id param must be greater than 0");
            }
        }

        @Override
        public Page<User> getUsers(UserService userService, int page, String search, Long id) {
            return userService.getFollowing(page,id);
        }
    },
    NO_FILTER() {
        @Override
        public Page<User> getUsers(UserService userService, int page, String search, Long id) {
            return userService.searchUsers(page,"");
        }
    };

    public void validateParams(String search, Long id) {
    }

    public abstract Page<User> getUsers(UserService userService,int page, String search, Long id);

    public static GetUsersFilter fromString(String param) {
        try {
            return valueOf(param.toUpperCase());
        } catch (Exception e) {
            throw new InvalidGetUsersFilter(param);
        }
    }

    GetUsersFilter() {
    }
}
