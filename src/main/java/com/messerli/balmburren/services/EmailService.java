package com.messerli.balmburren.services;



import com.messerli.balmburren.util.QRInvoice;


public interface EmailService {
    void sendEmailAttachment(String type, String toEmail, String subject, String body, byte[] byteArray, String base64String, String filename);
    String getQRInvoice(QRInvoice qrInvoice);
//    String sendTourData(Client[] clients);
//    List<Client> retourTourData(String json);

    void sendEmailRegister(String email);
}
