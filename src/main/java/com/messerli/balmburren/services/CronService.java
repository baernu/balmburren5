package com.messerli.balmburren.services;

import com.messerli.balmburren.android.Client;
import com.messerli.balmburren.util.QRInvoice;

import java.util.List;

public interface CronService {
    public void writeBackupToFile();
    public void sendBackup();
    public void importDatabase(String bytearray);
}
