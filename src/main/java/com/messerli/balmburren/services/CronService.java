package com.messerli.balmburren.services;

import com.messerli.balmburren.android.Client;
import com.messerli.balmburren.util.QRInvoice;

import java.util.List;

public interface CronService {
    void writeBackupToFile();
    void sendBackup();
    void importDatabase(String bytearray);
    void migrateDB();
}
