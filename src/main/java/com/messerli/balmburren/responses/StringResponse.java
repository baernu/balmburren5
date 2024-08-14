package com.messerli.balmburren.responses;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class StringResponse extends ResponseEntity<String> {
    private static String string;

    public StringResponse(String string, int i) {
        super(string, HttpStatus.valueOf(i));
        StringResponse.string = string;
    }

    public String getString() {
        return string;
    }
}
