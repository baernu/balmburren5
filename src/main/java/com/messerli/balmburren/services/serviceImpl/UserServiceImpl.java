package com.messerli.balmburren.services.serviceImpl;

import com.messerli.balmburren.entities.RoleEnum;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import com.messerli.balmburren.services.MyUserDetails;
import com.messerli.balmburren.services.UserService;
import com.messerli.balmburren.entities.Role;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
//    private final MyUserDetails myUserDetails;
//    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, MyUserDetails myUserDetails, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
//        this.myUserDetails = myUserDetails;
//        this.passwordEncoder = passwordEncoder;
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
//        user.ifPresent(value -> value
//                .setRole(optionalRole.get()));

        if (user.isPresent()) {
            Set<Role> roles = user.get().getRoles();
            if (roles == null) roles = new HashSet<>();
            Stream<Role> roles1 =  roles.stream().filter(role -> role.getName().equals(optionalRole.get().getName()));
            if (roles1.toList().isEmpty()) {
                roles.add(optionalRole.get());
                user.get().setRoles(roles);
                userRepository.save(user.get());
                return true;
            } else return false;
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
    public Optional<User> getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);
        return user;
    }

    @Override
    public boolean createDriver(String username) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.DRIVER);

        if (optionalRole.isEmpty()) {
            return false;
        }

        Optional<User> user = findUser(username);
//        user.ifPresent(value -> value
//                .setRole(optionalRole.get()));

        if (user.isPresent()) {
            Set<Role> roles = user.get().getRoles();
            if (roles == null) roles = new HashSet<>();
            Stream<Role> roles1 =  roles.stream().filter(role -> role.getName().equals(optionalRole.get().getName()));
            if (roles1.toList().isEmpty()) {
                roles.add(optionalRole.get());
                user.get().setRoles(roles);
                userRepository.save(user.get());
                return true;
            } else return false;

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
//        user.ifPresent(value -> value
//                .setRole(optionalRole.get()));

        if (user.isPresent()) {
            Set<Role> roles = user.get().getRoles();
            if (roles == null) roles = new HashSet<>();
            Stream<Role> roles1 = roles.stream().filter(role -> role.getName().equals(optionalRole.get().getName()));
            if (roles1.toList().isEmpty()) {
                roles.add(optionalRole.get());
                user.get().setRoles(roles);
                userRepository.save(user.get());
                return true;
            } else return false;
        }
        else
            return false;
    }

    @Override
    public Optional<User> updateUser(User user) {
        Optional<User> user1 = findUser(user.getUsername());
        user.setVersion(user1.get().getVersion());
            return Optional.of(userRepository.save(user));
    }

    public boolean isAdmin(String username) {
        Optional<User> optionalUser = findUser(username);
        Set<Role> roles =  optionalUser.get().getRoles();
        roles = roles.stream().filter(role -> role.getName().name().equals(RoleEnum.ADMIN.name())).collect(Collectors.toSet());
        return !roles.isEmpty();
    }

    @Override
    public boolean isBasic(String username) {
        Optional<User> optionalUser = findUser(username);
        Set<Role> roles =  optionalUser.get().getRoles();
        roles = roles.stream().filter(role -> role.getName().name().equals(RoleEnum.USER.name())).collect(Collectors.toSet());
        return !roles.isEmpty();
    }

    @Override
    public boolean isDriver(String username) {
        Optional<User> optionalUser = findUser(username);
        Set<Role> roles =  optionalUser.get().getRoles();
        roles = roles.stream().filter(role -> role.getName().name().equals(RoleEnum.DRIVER.name())).collect(Collectors.toSet());
        return !roles.isEmpty();
    }

    @Override
    public boolean isKathy(String username) {
        Optional<User> optionalUser = findUser(username);
        Set<Role> roles =  optionalUser.get().getRoles();
        roles = roles.stream().filter(role -> role.getName().name().equals(RoleEnum.KATHY.name())).collect(Collectors.toSet());
        return !roles.isEmpty();
    }

    @Override
    public boolean isUserKathy(String username) {
        Optional<User> optionalUser = findUser(username);
        Set<Role> roles =  optionalUser.get().getRoles();
        roles = roles.stream().filter(role -> role.getName().name().equals(RoleEnum.USER_KATHY.name())).collect(Collectors.toSet());
        return !roles.isEmpty();
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
         roles = (List<Role>) roleRepository.findAll();

        return roles;
    }


}
