package com.messerli.balmburren.util;

import javax.mail.*;
import java.io.*;
import java.util.Base64;
import java.util.Objects;
import java.util.Properties;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class SendingEmail {

    @Autowired
    private JavaMailSender mailSender;
//    final String username = "balmburren@gmail.com";
//    final String password = "anrq bwbp mxhq igzr";
//
//    @Autowired
//    private JavaMailSender mailSender;

    //    final String username = "admin@balmburren.net";
//final String username = "admin";
//    final String password1 = "123456";
//    public SendingEmail() {
//    }
//
    public void send(String type, String toEmail, String subject, String body, byte[] byteArray, String base64String, String filename) {

//        Properties prop = new Properties();
//        prop.put("mail.smtp.host", "smtp.gmail.com");
//        prop.put("mail.smtp.port", "587");
//
////        prop.put("mail.transport.protocol", "smtps");
////        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
//        prop.put("mail.smtp.socketFactory.port", "587");
//        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
//
//        prop.put("mail.smtp.auth", "true");
//        prop.put("mail.smtp.starttls.enable", "true"); //TLS

//        Session session = Session.getInstance(prop,
//                new Authenticator() {
//                    protected PasswordAuthentication getPasswordAuthentication() {
//                        return new PasswordAuthentication(username, password);
//                    }
//                });

//        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
//            Message message = new MimeMessage();
//            message.setFrom(new InternetAddress("balmburren@gmail.com"));
//            message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));
//            message.setRecipients(
//                    Message.RecipientType.TO,
//                    InternetAddress.parse(toEmail)
////                    InternetAddress.parse("to_username_a@gmail.com, to_username_b@yahoo.com")
//            );
//            message.setSubject(subject);
//            message.setText(body);


            if(Objects.equals(type, "normal"))
            {
                mailSender.send(message);
//                Transport.send(message);
//                System.out.println("EMail Sent Successfully!!");
                return;
            }
//            BodyPart messageBodyPart = new MimeBodyPart();
//            messageBodyPart.setText(body);
//            MimeBodyPart attachmentPart = new MimeBodyPart();
//
//            if(Objects.equals(type, "attachment"))
//            {
//
//                if(filename.endsWith(".txt")) {
//                    makeFile(byteArray);
//                    attachmentPart.attachFile("/tmp/burren/file.txt");
//                }
//                if(filename.endsWith(".pdf")) {
//                    makePDf(base64String);
//                    attachmentPart.attachFile("/tmp/burren/Balmburren.pdf");
//                }
//            }
//
//            // Create a multipart message for attachment
//            Multipart multipart = new MimeMultipart();
//
//            // Set text message part
//            multipart.addBodyPart(messageBodyPart);
//            multipart.addBodyPart(attachmentPart);
//
//
//            // Send the complete message parts
//            message.setContent(multipart);
//            Transport.send(message);
//            // Send message
////            Transport tr = session.getTransport("smtp");
////
////            tr.connect();
////            message.saveChanges();
////            tr.sendMessage(message, message.getAllRecipients());
////            tr.close();
//            System.out.println("EMail Sent Successfully with attachment!!");
//
//
//
//
////            Transport.send(message);
//
//
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

//        System.out.println("TLSEmail Start");
//        Properties props = new Properties();
//        props.put("mail.smtp.user", this.username);
//        props.put("mail.smtp.pwd", this.password1);
//        props.put("mail.smtp.host", "mail.balmburren.net"); //SMTP Host
//        props.put("mail.smtp.port", "25"); //TLS Port was 587
//        props.put("mail.smtp.auth", "true"); //enable authentication
//        props.put("mail.smtp.ssl.trust", "mail.balmburren.net");
//        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
//        props.put("mail.transport.protocol", "smtp");
//        props.put("mail.smtp.starttls.enable", "true"); //enable STARTTLS
//
//
//        //create Authenticator object to pass in Session.getInstance argument
//        Authenticator auth = new Authenticator() {
//            //override the getPasswordAuthentication method
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(username, password1 );
//            }
//        };
//        Session session = Session.getInstance(props, auth);
////        Session session = Session.getInstance(props, null);
//        if(Objects.equals(type, "normal"))
//            EmailUtil.sendEmail(session, toEmail,subject, body);
//        if(Objects.equals(type, "attachment"))
//            EmailUtil.sendAttachmentEmail(session, toEmail,subject, body, bytes, base64String, filename);
////        if(Objects.equals(type, "image"))
////            EmailUtil.sendImageEmail(session, toEmail,subject, body, filename);
//    }


//    public class SendEmailTLS {
//
//        public static void main(String[] args) {




    }

    private static void makeFile(byte[] bytes){
        try (FileOutputStream fos = new FileOutputStream("/tmp/burren/file.txt")) {
            fos.write(bytes);
            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void makePDf(String base64String) {
        System.out.println("Base64String: " + base64String);
        base64String = base64String.replaceFirst("^data:application/[^;]*;base64,?","");
        byte[] bytes = Base64.getDecoder().decode(base64String);
        try(OutputStream out = new FileOutputStream("/tmp/burren/Balmburren.pdf")){
            out.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
