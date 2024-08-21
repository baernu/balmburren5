package com.messerli.balmburren.util;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QRInvoice  {
    private String baseUrl;
    private String createUrl;
    private String apiKey;
    private String params;
}