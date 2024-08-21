package com.messerli.balmburren.util;

import javax.mail.*;
import java.util.Objects;
import java.util.Properties;

/**
 Outgoing Mail (SMTP) Server
 requires TLS or SSL: smtp.gmail.com (use authentication)
 Use Authentication: Yes
 Port for TLS/STARTTLS: 587
 */
public class SendingEmail {
//    final String username = "admin@balmburren.net";
final String username = "admin";
    final String password1 = "123456";
    public SendingEmail() {
    }

    public void send(String type, String fromEmail, String toEmail, String subject, String password, String body, byte[] bytes, String base64String, String filename) {

        System.out.println("TLSEmail Start");
        Properties props = new Properties();
        props.put("mail.smtp.user", this.username);
        props.put("mail.smtp.pwd", this.password1);
        props.put("mail.smtp.host", "mail.balmburren.net"); //SMTP Host
        props.put("mail.smtp.port", "25"); //TLS Port was 587
        props.put("mail.smtp.auth", "true"); //enable authentication
        props.put("mail.smtp.ssl.trust", "mail.balmburren.net");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS


        //create Authenticator object to pass in Session.getInstance argument
        Authenticator auth = new Authenticator() {
            //override the getPasswordAuthentication method
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password1 );
            }
        };
        Session session = Session.getInstance(props, auth);
//        Session session = Session.getInstance(props, null);
        if(Objects.equals(type, "normal"))
            EmailUtil.sendEmail(session, toEmail,subject, body);
        if(Objects.equals(type, "attachment"))
            EmailUtil.sendAttachmentEmail(session, toEmail,subject, body, bytes, base64String, filename);
//        if(Objects.equals(type, "image"))
//            EmailUtil.sendImageEmail(session, toEmail,subject, body, filename);
    }
}
