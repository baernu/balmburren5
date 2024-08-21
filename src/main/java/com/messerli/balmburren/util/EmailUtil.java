package com.messerli.balmburren.util;

import lombok.extern.slf4j.Slf4j;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Base64;
import java.util.Date;

@Slf4j
public class EmailUtil {

    /**
     * Utility method to send simple HTML email
     *
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     */
    public static void sendEmail(Session session, String toEmail, String subject, String body) {
        try {
            MimeMessage msg = new MimeMessage(session);
            //set message headers
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("admin@balmburren.net"));

            msg.setReplyTo(InternetAddress.parse("admin@balmburren.net", false));

            msg.setSubject(subject, "UTF-8");

            msg.setText(body, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
//            Transport.send(msg);
            Transport tr = session.getTransport("smtp");
//            tr.connect("mail.balmburren.net",25,"admin", "123456");
            tr.connect();
            msg.saveChanges();
            tr.sendMessage(msg, msg.getAllRecipients());
            tr.close();

            System.out.println("EMail Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Utility method to send email with attachment
     * @param session
     * @param toEmail
     * @param subject
     * @param body
     * @param byteArray
     * @param filename
     */
    public static void sendAttachmentEmail(Session session, String toEmail, String subject, String body, byte[] byteArray, String base64String, String filename) {
        try{
            MimeMessage msg = new MimeMessage( session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress("admin@balmburren.net"));

            msg.setReplyTo(InternetAddress.parse("admin@balmburren.net", false));

            msg.setSubject(subject, "UTF-8");

            msg.setSentDate(new Date());

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            System.out.println("Message is ready");
            // Create the message body part
            BodyPart messageBodyPart = new MimeBodyPart();

            // Fill the message
            messageBodyPart.setText(body);
            MimeBodyPart attachmentPart = new MimeBodyPart();
            if(filename.endsWith(".txt")) {
                makeFile(byteArray);
                attachmentPart.attachFile("/tmp/burren/file.txt");
            }
            if(filename.endsWith(".pdf")) {
                makePDf(base64String);
                attachmentPart.attachFile("/tmp/burren/Balmburren.pdf");
            }



            // Create a multipart message for attachment
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);
            multipart.addBodyPart(attachmentPart);


            // Send the complete message parts
            msg.setContent(multipart);

            // Send message
            Transport tr = session.getTransport("smtp");

            tr.connect();
            msg.saveChanges();
            tr.sendMessage(msg, msg.getAllRecipients());
            tr.close();
            System.out.println("EMail Sent Successfully with attachment!!");
        }catch (MessagingException | IOException e) {
            e.printStackTrace();
            System.out.println("Messaging Exception, sorry not going on...");
        }
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