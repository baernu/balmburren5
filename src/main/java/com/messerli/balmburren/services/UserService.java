package com.messerli.balmburren.services;

import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.User;

import java.util.List;

public interface UserService {

    public List<User> allUsers();
    public User createAdministrator(RegisterUserDto input);

    public boolean existUser(String username);
}
