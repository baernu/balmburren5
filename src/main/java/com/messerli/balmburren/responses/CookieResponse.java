package com.messerli.balmburren.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import jakarta.servlet.http.Cookie;


public class CookieResponse extends ResponseEntity<String> {
    private Cookie cookie;
    private static String string;
    public CookieResponse(String string, int i) {
        super(string, HttpStatus.valueOf(i));
        CookieResponse.string = string;
    }

    public Cookie getCookie() {
        return cookie;
    }

    public String getString() {
        return string;
    }
}
