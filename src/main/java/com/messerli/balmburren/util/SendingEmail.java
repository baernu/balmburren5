package com.messerli.balmburren.util;


import java.io.*;
import java.util.Base64;
import java.util.Objects;


import jakarta.mail.Message;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
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
            MimeMessagePreparator preparator = new MimeMessagePreparator() {
                public void prepare(MimeMessage mimeMessage) throws Exception {
                    mimeMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(toEmail));
                    mimeMessage.setFrom(new InternetAddress("balmburren@gmail.com"));
                    mimeMessage.setSubject(subject);
                    mimeMessage.setText(body);

//                    if (filename.endsWith(".txt")) {
//                        makeFile(byteArray);
//                        FileSystemResource file = new FileSystemResource(new File("src/main/resources/file.txt"));
//                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//                        helper.addAttachment("Balmburren-Text", file);
//                    }
//                    if (filename.endsWith(".pdf")) {
//                        makePDf(base64String);
//                        FileSystemResource file = new FileSystemResource(new File("src/main/resources/balmburren.pdf"));
//                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
//                        helper.addAttachment("Balmburren-PDF", file);
//                    }

                    if (filename.endsWith(".txt")) {
                        String txtFilePath = makeFile(byteArray);  // Return the path where the file is created
                        FileSystemResource file = new FileSystemResource(new File(txtFilePath));
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                        helper.addAttachment("Balmburren-Text.txt", file);
                    }

                    if (filename.endsWith(".pdf")) {
                        String pdfFilePath = makePDf(base64String);  // Return the path where the file is created
                        FileSystemResource file = new FileSystemResource(new File(pdfFilePath));
                        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
                        helper.addAttachment("Balmburren-PDF.pdf", file);
                    }

                }
            };

            try

            {
                mailSender.send(preparator);
            }
            catch(
                    MailException ex)

            {
                System.err.println(ex.getMessage());
            }


        }
    }

//    private static void makeFile(byte[] bytes) {
//        try (FileOutputStream fos = new FileOutputStream(new ClassPathResource("src/main/resources/file.txt").getFile())) {
//            fos.write(bytes);
//            //fos.close(); There is no more need for this line since you had created the instance of "fos" inside the try. And this will automatically close the OutputStream
//        } catch (FileNotFoundException fileNotFoundException){
//            log.info("TXT File not found!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static void makePDf(String base64String) {
//        System.out.println("Base64String: " + base64String);
//        base64String = base64String.replaceFirst("^data:application/[^;]*;base64,?","");
//        byte[] bytes = Base64.getDecoder().decode(base64String);
//        try(OutputStream out = new FileOutputStream(new ClassPathResource("src/main/resources/balmburren.pdf").getFile())){
//            out.write(bytes);
//        } catch (FileNotFoundException fileNotFoundException){
//            log.info("PDF File not found!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }

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

