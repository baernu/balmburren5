package com.messerli.balmburren.services.serviceImpl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.messerli.balmburren.android.Client;
import com.messerli.balmburren.android.UserTour;
import com.messerli.balmburren.services.EmailService;
import com.messerli.balmburren.util.Http;
import com.messerli.balmburren.util.QRInvoice;
import com.messerli.balmburren.util.SendingEmail;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;


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

    @Override
    public String sendTourData(Client[] clients) {
        UserTour userTour = new UserTour();
        try {
            return userTour.generateJson(clients);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Client> retourTourData(String json) {
        UserTour userTour = new UserTour();
        try {
            return userTour.getFields(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
