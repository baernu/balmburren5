package com.messerli.balmburren.controllers;

import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.services.UserService;
import com.messerli.balmburren.entities.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/admins")
@RestController
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }
    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/create_admin")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<?> createAdministrator(@RequestBody String username) {
        boolean var = userService.createAdministrator(username);

        return ResponseEntity.ok(var);
    }

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/create/driver")
    @PreAuthorize("hasAnyAuthority('DRIVER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<?> createDriver(@RequestBody String username) {
        boolean var = userService.createDriver(username);

        return ResponseEntity.ok(var);
    }

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/create/user")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createUser(@RequestBody String username) {
        boolean var = userService.createUser(username);
        return ResponseEntity.ok(var);
    }

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/update/user")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
//    @PreAuthorize("isAuthenticated()")'SUPER_ADMIN'
    public ResponseEntity<Optional<User>> updateUser(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.updateUser(user));
    }

    @CrossOrigin( allowCredentials = "true")
    @PutMapping("/update1/user")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
//    @PreAuthorize("isAuthenticated()")'SUPER_ADMIN'
    public ResponseEntity<Optional<User>> update1User(@RequestBody User user) {
        return ResponseEntity.ok().body(userService.newPassword(user));
    }

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/is_admin/{username}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    public ResponseEntity<Boolean> isAdmin(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.isAdmin(username));
    }

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('USER','ADMIN','SUPER_ADMIN')")
    @GetMapping("/is_basic/{username}")
    public ResponseEntity<Boolean> isBasic(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.isBasic(username));
    }

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('USER_KATHY','ADMIN','SUPER_ADMIN')")
    @GetMapping("/is_user_kathy/{username}")
    public ResponseEntity<Boolean> isUserKathy(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.isUserKathy(username));
    }

    @CrossOrigin( allowCredentials = "true")

    @GetMapping("/is_driver/{username}")
    @PreAuthorize("hasAnyAuthority('DRIVER','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Boolean> isDriver(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.isDriver(username));
    }

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("is_kathy/{username}")
    @PreAuthorize("hasAnyAuthority('KATHY','ADMIN','SUPER_ADMIN')")
    public ResponseEntity<Boolean> isKathy(@PathVariable("username") String username) {
        return ResponseEntity.ok().body(userService.isKathy(username));
    }
}
