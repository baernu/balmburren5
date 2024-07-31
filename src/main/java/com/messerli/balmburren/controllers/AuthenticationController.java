package com.messerli.balmburren.controllers;

import com.messerli.balmburren.dtos.LoginUserDto;
import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.responses.LoginResponse;
import com.messerli.balmburren.services.AuthenticationService;
import com.messerli.balmburren.services.JwtService;
import com.messerli.balmburren.services.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, allowedHeaders = "*")
@RequestMapping("/auth")
@RestController
public class AuthenticationController {
    private final JwtService jwtService;
    private final AuthenticationService authenticationService;

    private final UserService userService;

    public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }
    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterUserDto registerUserDto) {
        User registeredUser = authenticationService.signup(registerUserDto);

        return ResponseEntity.ok(registeredUser);
    }
    @CrossOrigin( allowCredentials = "true")
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticate(@RequestBody LoginUserDto loginUserDto) {
        User authenticatedUser = authenticationService.authenticate(loginUserDto);

        String jwtToken = jwtService.generateToken(authenticatedUser);

        LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());

        return ResponseEntity.ok(loginResponse);
    }
    @CrossOrigin( allowCredentials = "true")
    @GetMapping ("/exist/{username}")
    ResponseEntity<Boolean> existUser(@PathVariable("username") String username) {
        boolean bool = userService.existUser(username);
        return ResponseEntity.ok().body(bool);}
}