package com.messerli.balmburren.controllers;

import com.messerli.balmburren.dtos.LoginUserDto;
import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.responses.CookieResponse;
import com.messerli.balmburren.responses.LoginResponse;
import com.messerli.balmburren.services.AuthenticationService;
import com.messerli.balmburren.services.JwtService;
import com.messerli.balmburren.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;


@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
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

    @CrossOrigin(allowCredentials = "true")
    @PostMapping("/set-cookie")
    public ResponseEntity<?> setCookie(HttpServletResponse response, @RequestBody String tok) throws UnsupportedEncodingException {
        JSONObject jsonObj = new JSONObject(tok);
        tok = jsonObj.getString("token");
        Cookie cookie = new Cookie("jwt", tok);
        cookie.setMaxAge(60 * 60);
        ///////////////////////////////////////////////////
        cookie.setSecure(false);
        ////////////////////////////////////////////////////7
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        CookieResponse cookieResponse = new CookieResponse("Cookie is set: " + "HTTP: ", 200);

        return ResponseEntity.ok(cookieResponse);
    }


    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/delete-cookie")
    public ResponseEntity<?> deleteCookie(HttpServletResponse response) throws UnsupportedEncodingException {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        ///////////////////////////////////////////////////
        cookie.setSecure(false);
        ///////////////////////////////////////////////////
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        CookieResponse cookieResponse = new CookieResponse("Cookie is deleted: "  + "HTTP: ", 200);

        return ResponseEntity.ok(cookieResponse);

    }
}