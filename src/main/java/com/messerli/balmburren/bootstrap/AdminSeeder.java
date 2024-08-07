package com.messerli.balmburren.bootstrap;


import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.Role;
import com.messerli.balmburren.entities.RoleEnum;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Slf4j
@Component
public class AdminSeeder implements ApplicationListener<ContextRefreshedEvent> {
    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

    }
//    private final RoleRepository roleRepository;
//    private final UserRepository userRepository;
//
//    private final PasswordEncoder passwordEncoder;
//
//
//    public AdminSeeder(
//            RoleRepository roleRepository,
//            UserRepository  userRepository,
//            PasswordEncoder passwordEncoder
//    ) {
//        this.roleRepository = roleRepository;
//        this.userRepository = userRepository;
//        this.passwordEncoder = passwordEncoder;
//    }
//    @Override
//    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
//        this.createSuperAdministrator();
//    }
//
//    private void createSuperAdministrator() {
//        RegisterUserDto userDto = new RegisterUserDto();
////        userDto.setFirstname("Super").setLastname( "Admin").setUsername("super.admin@email.com").setPassword("123456");
//        userDto.setFirstname("Normal").setLastname( "Admin").setUsername("admin").setPassword("adminadmin");
////        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.SUPER_ADMIN);
////        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
//        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
//        Optional<User> optionalUser = userRepository.findByUsername(userDto.getUsername());
//        if (optionalRole.isEmpty() || optionalUser.isPresent()) {
//            return;
//        }
////
//        var user = new User()
//                .setFirstname(userDto.getFirstname())
//                .setLastname(userDto.getLastname())
//                .setUsername(userDto.getUsername())
//                .setPassword(passwordEncoder.encode(userDto.getPassword()))
//                .setRole(optionalRole.get());
//
//        User user1 = userRepository.save(user);
////        User registeredUser = authenticationService.signup(userDto);
////        log.info("AuthenticationService is over..");
////        boolean message = userService.createAdministrator(registeredUser.getUsername());
//        log.info("Create User is : " + user1);
//    }
}
