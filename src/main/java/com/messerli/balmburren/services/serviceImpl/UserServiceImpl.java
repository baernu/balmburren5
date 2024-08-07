package com.messerli.balmburren.services.serviceImpl;

import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.RoleEnum;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import com.messerli.balmburren.services.UserService;
import com.messerli.balmburren.entities.Role;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }
    @Override
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }

    public boolean createAdministrator(String username) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) {
            return false;
        }

        Optional<User> user = findUser(username);
        user.ifPresent(value -> value
                .setRole(optionalRole.get()));

        if (user.isPresent()) {
            userRepository.save(user.get());
            return true;
        }
        else
            return false;
    }

    @Override
    public boolean existUser(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);

        return optionalUser.isPresent();
    }

    @Override
    public Optional<User> findUser(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean createDriver(String username) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.DRIVER);

        if (optionalRole.isEmpty()) {
            return false;
        }

        Optional<User> user = findUser(username);
        user.ifPresent(value -> value
                .setRole(optionalRole.get()));

        if (user.isPresent()) {
            userRepository.save(user.get());
            return true;
        }
        else
            return false;

    }

    @Override
    public boolean createUser(String username) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return false;
        }

        Optional<User> user = findUser(username);
        user.ifPresent(value -> value
                .setRole(optionalRole.get()));

        if (user.isPresent()) {
            userRepository.save(user.get());
            return true;
        }
        else
            return false;
    }

    public boolean isAdmin(String username) {
        Optional<User> optionalUser = findUser(username);
        return optionalUser.map(user -> user.getRole().getName().equals(RoleEnum.ADMIN)).orElse(false);
    }

    @Override
    public boolean isBasic(String username) {
        Optional<User> optionalUser = findUser(username);
        return optionalUser.map(user -> user.getRole().getName().equals(RoleEnum.USER)).orElse(false);
    }

    @Override
    public boolean isDriver(String username) {
        Optional<User> optionalUser = findUser(username);
        return optionalUser.map(user -> user.getRole().getName().equals(RoleEnum.DRIVER)).orElse(false);
    }

    @Override
    public boolean isKathy(String username) {
        Optional<User> optionalUser = findUser(username);
        return optionalUser.map(user -> user.getRole().getName().equals(RoleEnum.KATHY)).orElse(false);
    }

    @Override
    public boolean isUserKathy(String username) {
        Optional<User> optionalUser = findUser(username);
        return optionalUser.map(user -> user.getRole().getName().equals(RoleEnum.USER_KATHY)).orElse(false);
    }


}
