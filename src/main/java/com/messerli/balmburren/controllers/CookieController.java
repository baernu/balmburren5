package com.messerli.balmburren.controllers;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import com.messerli.balmburren.responses.CookieResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, allowedHeaders = "*")
@RestController
public class CookieController {


    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/set-cookie/{tok}")
//    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> setCookie(HttpServletResponse response, @PathVariable("tok") String tok) {

        log.info("token is: {}", tok);
        Cookie cookie = new Cookie("jwt", tok);
        cookie.setMaxAge(60 * 60);
        ///////////////////////////////////////////////////
        cookie.setSecure(false);
        ////////////////////////////////////////////////////7
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);
        CookieResponse cookieResponse = new CookieResponse("Cookie: " + cookie.toString() + "HTTP: ", 200);

        return ResponseEntity.ok(cookieResponse);
    }

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("/delete-cookie")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteCookie(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", null);
        cookie.setMaxAge(0);
        ///////////////////////////////////////////////////
        cookie.setSecure(true);
        ///////////////////////////////////////////////////
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        response.addCookie(cookie);

        CookieResponse cookieResponse = new CookieResponse("Cookie: " + cookie.toString() + "HTTP: ", 200);

        return ResponseEntity.ok(cookieResponse);

    }



}
