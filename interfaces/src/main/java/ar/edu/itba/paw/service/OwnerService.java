package ar.edu.itba.paw.service;

import ar.edu.itba.paw.model.user.User;


public interface OwnerService {
    void makeUserAdmin(User user);
    void deleteUserAdmin(User user);
}
