package com.messerli.balmburren.services.serviceImpl;


import com.messerli.balmburren.services.EmailService;
import com.messerli.balmburren.util.Http;
import com.messerli.balmburren.util.QRInvoice;
import com.messerli.balmburren.util.SendingEmail;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;



@Service
@Transactional
@Slf4j
public class EmailServiceImpl implements EmailService {

    private final SendingEmail sendingEmail;

    public EmailServiceImpl(SendingEmail sendingEmail) {
        this.sendingEmail = sendingEmail;
    }

    @Override
    public void sendEmailAttachment(String type, String toEmail, String subject, String body, byte[] byteArray, String base64String, String filename) {
        sendingEmail.send(type, toEmail, subject, body, byteArray, base64String, filename);
    }

    @Override
    public String getQRInvoice(QRInvoice qrInvoice) {
        Http http = new Http(qrInvoice);
        try {
            return http.getHTML();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

//    @Override
//    public String sendTourData(Client[] clients) {
//        UserTour userTour = new UserTour();
//        try {
//            return userTour.generateJson(clients);
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    @Override
//    public List<Client> retourTourData(String json) {
//        UserTour userTour = new UserTour();
//        try {
//            return userTour.getFields(json);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Override
    public void sendEmailRegister(String email) {
        String subject = "Registrierung Balmburren";
        String body = "Guten Tag\nSie haben sich neu bei Balmburren registriert. Bitte antworten Sie auf diese Mail und bestätigen kurz,\nbei Balmburren als Kunde * in online bestellen zu wollen.\nVielen Dank.";
        sendingEmail.send("normal",email, subject, body, null, null, "");
    }

}
