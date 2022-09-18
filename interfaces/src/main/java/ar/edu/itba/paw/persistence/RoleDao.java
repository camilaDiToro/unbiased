package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.model.Role;

import java.util.List;

public interface RoleDao {

    void addRole(long userId, Role role);
    List<String> getRoles(long userId);
}
