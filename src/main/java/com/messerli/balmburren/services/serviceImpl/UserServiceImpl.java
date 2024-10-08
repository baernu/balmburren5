package com.messerli.balmburren.services.serviceImpl;

import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import com.messerli.balmburren.repositories.UsersRoleRepo;
import com.messerli.balmburren.services.MyUserDetails;
import com.messerli.balmburren.services.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Autowired
    private final UsersRoleRepo usersRoleRepo;
//    private final MyUserDetails myUserDetails;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, MyUserDetails myUserDetails, PasswordEncoder passwordEncoder, UsersRoleRepo usersRoleRepo, PasswordEncoder passwordEncoder1) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.usersRoleRepo = usersRoleRepo;
//        this.myUserDetails = myUserDetails;
//        this.passwordEncoder = passwordEncoder;
        this.passwordEncoder = passwordEncoder1;
    }
    @Override
    public List<User> allUsers() {
        List<User> users = new ArrayList<>();

        userRepository.findAll().forEach(users::add);

        return users;
    }
    @Transactional
    public boolean createAdministrator(String username) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);

        if (optionalRole.isEmpty()) {
            return false;
        }

        Optional<User> user = findUser(username);
        if (user.isEmpty()) return false;

        List<UsersRole> list = usersRoleRepo.findAllByUser(user.get());
        boolean bool = list.stream().anyMatch(e -> e.getRole().getName().name().equals("ADMIN"));
        if(bool) {
            return false;
        }

        UsersRole usersRole = new UsersRole();
        usersRole.setUser(user.get());
        usersRole.setRole(optionalRole.get());
        usersRoleRepo.save(usersRole);


        return true;
    }

    @Override
    public boolean existUser(String username) {
        return userRepository.existsByUsername(username);
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
    @Transactional
    @Override
    public boolean createDriver(String username) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.DRIVER);

        if (optionalRole.isEmpty()) {
            return false;
        }

        Optional<User> user = findUser(username);
        if (user.isEmpty()) return false;

        List<UsersRole> list = usersRoleRepo.findAllByUser(user.get());
        boolean bool = list.stream().anyMatch(e -> e.getRole().getName().name().equals(RoleEnum.DRIVER.name()));
        if(bool) {
            return false;
        }

        UsersRole usersRole = new UsersRole();
        usersRole.setUser(user.get());
        usersRole.setRole(optionalRole.get());
        usersRoleRepo.save(usersRole);


        return true;
    }
    @Transactional
    @Override
    public boolean createUser(String username) {
        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.USER);

        if (optionalRole.isEmpty()) {
            return false;
        }

        Optional<User> user = findUser(username);
        if (user.isEmpty()) return false;

        List<UsersRole> list = usersRoleRepo.findAllByUser(user.get());
        boolean bool = list.stream().anyMatch(e -> e.getRole().getName().name().equals(RoleEnum.USER.name()));
        if(bool) {
            return false;
        }

        UsersRole usersRole = new UsersRole();
        usersRole.setUser(user.get());
        usersRole.setRole(optionalRole.get());
        usersRoleRepo.save(usersRole);


        return true;
    }

    @Override
    public Optional<User> enableUser(User user) {
        user.setEnabled(true);
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> dissableUser(User user) {
        user.setEnabled(false);
        return Optional.of(userRepository.save(user));
    }

    @Override
    public Optional<User> updateUser(User user) {
        Optional<User> user1 = findUser(user.getUsername());
        user.setVersion(user1.get().getVersion());
            return Optional.of(userRepository.save(user));
    }

    public Optional<User> newPassword(User user) {
        Optional<User> user1 = findUser(user.getUsername());
        user.setVersion(user1.get().getVersion());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return Optional.of(userRepository.save(user));
    }


    public boolean isAdmin(String username) {
        Optional<User> optionalUser = findUser(username);
        List<UsersRole> roles= usersRoleRepo.findAllByUser(optionalUser.get());
        roles = roles.stream().filter(urole -> urole.getRole().getName().name().equals(RoleEnum.ADMIN.name())).collect(Collectors.toSet()).stream().toList();
        return !roles.isEmpty();
    }

    @Override
    public boolean isBasic(String username) {
        Optional<User> optionalUser = findUser(username);
        List<UsersRole> roles= usersRoleRepo.findAllByUser(optionalUser.get());
        roles = roles.stream().filter(role -> role.getRole().getName().name().equals(RoleEnum.USER.name())).collect(Collectors.toSet()).stream().toList();
        return !roles.isEmpty();
    }

    @Override
    public boolean isDriver(String username) {
        Optional<User> optionalUser = findUser(username);
        List<UsersRole> roles= usersRoleRepo.findAllByUser(optionalUser.get());
        roles = roles.stream().filter(urole -> urole.getRole().getName().name().equals(RoleEnum.DRIVER.name())).collect(Collectors.toSet()).stream().toList();
        return !roles.isEmpty();
    }

    @Override
    public boolean isKathy(String username) {
        Optional<User> optionalUser = findUser(username);
        List<UsersRole> roles= usersRoleRepo.findAllByUser(optionalUser.get());
        roles = roles.stream().filter(urole -> urole.getRole().getName().name().equals(RoleEnum.KATHY.name())).collect(Collectors.toSet()).stream().toList();
        return !roles.isEmpty();
    }

    @Override
    public boolean isUserKathy(String username) {
        Optional<User> optionalUser = findUser(username);
        List<UsersRole> roles= usersRoleRepo.findAllByUser(optionalUser.get());
        roles = roles.stream().filter(role -> role.getRole().getName().name().equals(RoleEnum.USER_KATHY.name())).collect(Collectors.toSet()).stream().toList();
        return !roles.isEmpty();
    }

    @Override
    public List<Role> getAllRoles() {
        List<Role> roles = new ArrayList<>();
         roles = (List<Role>) roleRepository.findAll();

        return roles;
    }


}
