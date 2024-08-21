package com.messerli.balmburren.util;

import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


@Slf4j
public class Http {
    private QRInvoice qrInvoice;

    public Http(){}
    public Http(QRInvoice qrInvoice){this.qrInvoice = qrInvoice;}

    public String getHTML() throws Exception {
        String urlToRead = this.qrInvoice.getBaseUrl() + this.qrInvoice.getCreateUrl() + "?" + this.qrInvoice.getParams();
        StringBuilder result = new StringBuilder();
        URL url = new URL(urlToRead);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("APIKEY", qrInvoice.getApiKey());
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream()))) {
            for (String line; (line = reader.readLine()) != null; ) {
                result.append(line);
            }
        }
        return result.toString();
    }
}
