package com.messerli.balmburren.util;


import java.io.*;
import java.util.Base64;
import java.util.Objects;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
@Slf4j
@Service
public class SendingEmail {

    @Autowired
    private JavaMailSender mailSender;
    public void send(String type, String toEmail, String subject, String body, byte[] byteArray, String base64String, String filename) {
        if (Objects.equals(type, "normal")) {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);

            mailSender.send(message);
        }
        if (Objects.equals(type, "attachment")) {
            MimeMessage message = mailSender.createMimeMessage();

            try {
                MimeMessageHelper  helper = new MimeMessageHelper(message, true);

                helper.setSubject(subject);
                helper.setFrom("balmburren@gmail.com");
                helper.setTo(toEmail);
                helper.setReplyTo("balmburren@gmail.com");
                helper.setText(body, false);
                if (filename.endsWith(".txt")) {
                    String txtFilePath = makeFile(byteArray);  // Return the path where the file is created
                    FileSystemResource file = new FileSystemResource(new File(txtFilePath));
                    helper.addAttachment("Balmburren-Text.txt", file);
                }
                if (filename.endsWith(".pdf")) {
                    String pdfFilePath = makePDf(base64String);  // Return the path where the file is created
                    FileSystemResource file = new FileSystemResource(new File(pdfFilePath));
                    helper.addAttachment("Balmburren-PDF.pdf", file);
                }
                mailSender.send(message);

            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static String makeFile(byte[] bytes) {
        String filePath = "file.txt";  // Save to a local path
        try (FileOutputStream fos = new FileOutputStream(filePath)) {
            fos.write(bytes);
        } catch (FileNotFoundException fileNotFoundException) {
            log.info("TXT File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;  // Return the path to the created file
    }

    private static String makePDf(String base64String) {
        String filePath = "balmburren.pdf";  // Save to a local path
        System.out.println("Base64String: " + base64String);
        base64String = base64String.replaceFirst("^data:application/[^;]*;base64,?", "");
        byte[] bytes = Base64.getDecoder().decode(base64String);
        try (OutputStream out = new FileOutputStream(filePath)) {
            out.write(bytes);
        } catch (FileNotFoundException fileNotFoundException) {
            log.info("PDF File not found!");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;  // Return the path to the created file
    }
}

