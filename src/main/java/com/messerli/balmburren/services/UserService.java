package com.messerli.balmburren.services;

import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    public List<User> allUsers();
    public User createAdministrator(RegisterUserDto input);

    public boolean existUser(String username);

    public Optional<User> findUser(String username);
    public boolean createDriver(String username);
    public boolean createUser(String username);

    boolean isAdmin(String username);
    boolean isBasic(String username);
    boolean isDriver(String username);
    boolean isKathy(String username);
    boolean isUserKathy(String username);}
