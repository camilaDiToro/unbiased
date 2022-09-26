package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Role;
import ar.edu.itba.paw.model.user.User;

import java.util.List;

public interface RoleDao {

    void addRole(User user, Role role);
    List<String> getRoles(User user);
}
