package ar.edu.itba.paw.webapp.controller.queryParamsValidators;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.user.User;
import ar.edu.itba.paw.service.UserService;
import ar.edu.itba.paw.webapp.api.exceptions.InvalidGetUsersFilter;
import org.springframework.stereotype.Component;

@Component
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
    FOLLOWING(){
        @Override
        public boolean areParamsValid(String search, Long id) {
            return id!=null && id > 0;
        }

        @Override
        public Page<User> getUsers(UserService userService, int page, String search, Long id) {
            return userService.getFollowing(page,id);
        }

        @Override
        public String getInvalidParamsMsg(String search, Long id) {
            if(id == null){
                return "\"following\" filter needs an \"id\" query param. " +
                        "Try sending the query params ?filter=following&id=2 to get the followers of the user of id 2";
            }
            return "id param must be greater than 0";
        }
    },
    NO_FILTER() {
        @Override
        public Page<User> getUsers(UserService userService, int page, String search, Long id) {
            return userService.searchUsers(page,"");
        }
    };

    public String getInvalidParamsMsg(String search, Long id){
        return String.format("Invalid params %s, %d", search, id);
    }

    public boolean areParamsValid(String search, Long id) {
        return true;
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
