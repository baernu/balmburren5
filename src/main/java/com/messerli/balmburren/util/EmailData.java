package com.messerli.balmburren.util;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailData {
    private String fromEmail;
    private String toEmail;
    private String subject;
    private String password;
    private String body;
    private String filename;
    private byte[] byteArray;
    private String base64String;
    private String type;
}
