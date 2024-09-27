package com.messerli.balmburren.services;


import com.messerli.balmburren.entities.Role;
import com.messerli.balmburren.dtos.LoginUserDto;
import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.RoleEnum;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.entities.UsersRole;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import com.messerli.balmburren.repositories.UsersRoleRepo;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    @Autowired
    private final RoleRepository roleRepository;
    @Autowired
    private final UsersRoleRepo usersRoleRepo;

    public AuthenticationService(
            UserRepository userRepository,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder,
            RoleRepository roleRepository, UsersRoleRepo usersRoleRepo) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.usersRoleRepo = usersRoleRepo;
    }
    @Transactional
    public User signup(RegisterUserDto input) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return null;
        }

//        Set<UsersRole> roles = new HashSet<>();
//        roles.add(optionalRole.get());
        var user = new User();
        user.setFirstname(input.getFirstname());
        user.setLastname(input.getLastname());
        user.setUsername(input.getUsername());
        user.setPassword(passwordEncoder.encode(input.getPassword()));
        user.setEnabled(true);

        user = userRepository.save(user);
        UsersRole usersRole = new UsersRole();
        usersRole.setRole(optionalRole.get());
        usersRole.setUser(user);
        usersRoleRepo.save(usersRole);

        return userRepository.save(user);
    }
    @Transactional
    public MyUserDetails authenticate(LoginUserDto input) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        input.getUsername(),
                        input.getPassword()
                )
        );
        MyUserDetails myUserDetails = new MyUserDetails(userRepository.findByUsername(input.getUsername()).orElseThrow(), usersRoleRepo);

//        return userRepository.findByUsername(input.getUsername()).orElseThrow();
        return myUserDetails;
    }

//    public List<User> allUsers() {
//        List<User> users = new ArrayList<>();
//
//        userRepository.findAll().forEach(users::add);
//
//        return users;
//    }
}
