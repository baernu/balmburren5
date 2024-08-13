package com.messerli.balmburren.services;

import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.Role;
import com.messerli.balmburren.entities.RoleEnum;
import com.messerli.balmburren.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> allUsers();
    public boolean createAdministrator(String username);

    public boolean existUser(String username);

    public Optional<User> findUser(String username);
    public boolean createDriver(String username);
    public boolean createUser(String username);

    public boolean isAdmin(String username);
    public boolean isBasic(String username);
    public boolean isDriver(String username);
    public boolean isKathy(String username);
    public boolean isUserKathy(String username);

    public List<Role> getAllRoles();
}
