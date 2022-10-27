package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.Page;
import ar.edu.itba.paw.model.user.User;


public interface OwnerService {
    void makeUserAdmin(String email);
    void deleteUserAdmin(long userId);
    Page<User> getAdmins(int page, String search);
}
