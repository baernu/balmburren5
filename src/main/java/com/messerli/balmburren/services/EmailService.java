package com.messerli.balmburren.services;



import com.messerli.balmburren.android.Client;
import com.messerli.balmburren.util.QRInvoice;

import java.util.List;

public interface EmailService {
    void sendEmailAttachment(String type, String toEmail, String subject, String body, byte[] byteArray, String base64String, String filename);
    String getQRInvoice(QRInvoice qrInvoice);
    String sendTourData(Client[] clients);
    List<Client> retourTourData(String json);

    void sendEmailRegister(String email);
}
