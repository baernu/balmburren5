package com.messerli.balmburren.controllers;

import com.messerli.balmburren.entities.Role;
import com.messerli.balmburren.entities.RoleEnum;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.services.MyUserDetails;
import com.messerli.balmburren.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/users/")
@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }
    @CrossOrigin( allowCredentials = "true")
    @GetMapping("me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> authenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        MyUserDetails currentUser = (MyUserDetails) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("{username}")
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> findUser(@PathVariable("username") String username) {

        return ResponseEntity.ok(userService.findUser(username));
    }
    @CrossOrigin( allowCredentials = "true")
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<List<User>> allUsers() {
        List <User> users = userService.allUsers();

        return ResponseEntity.ok(users);
    }


    @CrossOrigin( allowCredentials = "true")
    @GetMapping("role")
    ResponseEntity<List<Role>> getAllRoles() {
        return ResponseEntity.ok().body(userService.getAllRoles());}


}
