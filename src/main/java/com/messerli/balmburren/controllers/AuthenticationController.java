package com.messerli.balmburren.controllers;

import com.messerli.balmburren.dtos.LoginUserDto;
import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.User;
import com.messerli.balmburren.responses.CookieResponse;
import com.messerli.balmburren.responses.LoginResponse;
import com.messerli.balmburren.services.AuthenticationService;
import com.messerli.balmburren.services.JwtService;
import com.messerli.balmburren.services.MyUserDetails;
import com.messerli.balmburren.services.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006","https://www.balmburren.net:4200"}
        , exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
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
        MyUserDetails authenticatedUser = authenticationService.authenticate(loginUserDto);

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
//        JSONObject jsonObj = new JSONObject(tok);
//        tok = jsonObj.getString("token");
//        Cookie cookie = new Cookie("jwt", tok);
//        cookie.setMaxAge(60 * 60 * 24);
//        ///////////////////////////////////////////////////
//        cookie.setSecure(true);
//        ////////////////////////////////////////////////////7
//        cookie.setHttpOnly(true);
//        cookie.setPath("/");
//        cookie.setDomain("balmburren.net");
//
//        // Set explicit Expires header (for better compatibility)
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        String expires = sdf.format(new Date(System.currentTimeMillis() + (60 * 60 * 24 * 1000))); // 24 hours
//        response.addHeader("Set-Cookie", "jwt=" + tok + "; Path=/; HttpOnly; Secure; Max-Age=86400;SameSite=Strict; Expires=" + expires);
//
//
//        response.addCookie(cookie);
//        CookieResponse cookieResponse = new CookieResponse("Cookie is set: " + "HTTP: ", 200);
//
//        return ResponseEntity.ok(cookieResponse);

//        JSONObject jsonObj = new JSONObject(tok);
//        tok = jsonObj.getString("token");
//
//        // Create a cookie and set Max-Age
//        Cookie cookie = new Cookie("jwt", tok);
//        cookie.setMaxAge(60 * 60 * 24); // 24 hours in seconds
//        cookie.setSecure(true); // Ensure the cookie is sent over HTTPS
//        cookie.setHttpOnly(true); // Prevent client-side access
//        cookie.setPath("/"); // Make it available across the entire domain
//
//        // Add cookie to response
//        response.addCookie(cookie);
//
//        // Add explicit Expires header for better browser compatibility
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        String expires = sdf.format(new Date(System.currentTimeMillis() + (60 * 60 * 24 * 1000))); // 24 hours
//
//        response.addHeader("Set-Cookie", String.format(
//                "jwt=%s; Path=/; HttpOnly; Secure; Max-Age=86400; Expires=%s",
//                tok, expires
//        ));
//
//        // Create response object
//        CookieResponse cookieResponse = new CookieResponse("Cookie is set: " + "HTTP: ", 200);
//
//        return ResponseEntity.ok(cookieResponse);

//        JSONObject jsonObj = new JSONObject(tok);
//        tok = jsonObj.getString("token");
//
//// Create a formatted Set-Cookie header manually
//        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
//        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
//        String expires = sdf.format(new Date(System.currentTimeMillis() + (60 * 60 * 24 * 1000))); // 24 hours
//
//// Manually add the Set-Cookie header with SameSite=Strict
//        response.addHeader("Set-Cookie", String.format(
//                "jwt=%s; Path=/; HttpOnly; Secure; Max-Age=86400; Domain=balmburren.net; SameSite=Strict",
//                tok
//        ));
//
//// Create response object
//        CookieResponse cookieResponse = new CookieResponse("Cookie is set: " + "HTTP: ", 200);
//
//// Return response
//        return ResponseEntity.ok(cookieResponse);

        JSONObject jsonObj = new JSONObject(tok);
        tok = jsonObj.getString("token");

// Manually construct the Set-Cookie header with all required attributes
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss z");
        sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
        String expires = sdf.format(new Date(System.currentTimeMillis() + (60 * 60 * 24 * 1000))); // 24 hours

        response.addHeader("Set-Cookie", String.format(
                "jwt=%s; Path=/; HttpOnly; Secure; Max-Age=86400; SameSite=Strict; Expires=%s; Domain=balmburren.net",
                tok, expires
        ));

// Create response object
        CookieResponse cookieResponse = new CookieResponse("Cookie is set: " + "HTTP: ", 200);

// Return response
        return ResponseEntity.ok(cookieResponse);
    }


    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/delete-cookie")
    public ResponseEntity<?> deleteCookie(HttpServletResponse response) throws UnsupportedEncodingException {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        ///////////////////////////////////////////////////
        cookie.setSecure(true);
        ///////////////////////////////////////////////////
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        CookieResponse cookieResponse = new CookieResponse("Cookie is deleted: "  + "HTTP: ", 200);

        return ResponseEntity.ok(cookieResponse);

    }
}