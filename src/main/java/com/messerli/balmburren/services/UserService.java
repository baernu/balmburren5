package com.messerli.balmburren.services;

import com.messerli.balmburren.entities.Role;
import com.messerli.balmburren.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> allUsers();
    boolean createAdministrator(String username);

    boolean existUser(String username);

    Optional<User> findUser(String username);
    Optional<User> getUserById(Long id);
    boolean createDriver(String username);
    boolean createUser(String username);

    Optional<User> enableUser(User user);

    Optional<User> dissableUser(User user);

    Optional<User> updateUser(User user);
    Optional<User> newPassword(User user);

    boolean isAdmin(String username);
    boolean isBasic(String username);
    boolean isDriver(String username);
    boolean isKathy(String username);
    boolean isUserKathy(String username);

    List<Role> getAllRoles();
}
