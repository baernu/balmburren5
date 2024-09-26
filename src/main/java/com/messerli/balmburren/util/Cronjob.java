package com.messerli.balmburren.util;

import com.messerli.balmburren.services.CronService;
import com.smattme.MysqlExportService;
import com.smattme.MysqlImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
@Slf4j
public class Cronjob implements CronService {

    @Autowired
    private SendingEmail sendingEmail;

    private static byte[] byteArray;
    private static MysqlExportService mysqlExportService;
    private static File file;

    @Scheduled(cron = "0 40 23 * * *")
    public void backupAutoWriteToFile() {
        writeBackupToFile();
        log.info("in the process of backupAutoWriteToFile");
    }

    @Scheduled(cron = "59 59 23 * * *")
    public void backupAutoSend() {
        sendBackup();
        log.info("in the process of backupAutoSend");
    }


    public void writeBackupToFile() {
        Properties properties = new Properties();
        properties.setProperty(MysqlExportService.DB_NAME, "balmburren_db");
        properties.setProperty(MysqlExportService.DB_USERNAME, "root");
        properties.setProperty(MysqlExportService.DB_PASSWORD, "secret");
        properties.setProperty(MysqlExportService.DB_HOST, "localhost");
        properties.setProperty(MysqlExportService.DB_PORT, "3307");
        properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, "jdbc:mysql://localhost:3307/balmburren_db");

//        MysqlExportService mysqlExportService = new MysqlExportService(properties);
////properties relating to email config
//        properties.setProperty(MysqlExportService.EMAIL_HOST, "smtp.gmail.com");
//        properties.setProperty(MysqlExportService.EMAIL_PORT, "587");
//        properties.setProperty(MysqlExportService.EMAIL_USERNAME, "balmburren@gmail.com");
//        properties.setProperty(MysqlExportService.EMAIL_PASSWORD, "anrq bwbp mxhq igzr");
//        properties.setProperty(MysqlExportService.EMAIL_FROM, "balmburren@gmail.com");
//        properties.setProperty(MysqlExportService.EMAIL_TO, "balmburren@gmail.com");
//        properties.setProperty(MysqlExportService.EMAIL_SSL_PROTOCOLS, "TLSv1.2");
//        properties.setProperty(MysqlExportService.EMAIL_SMTP_AUTH_ENABLED, "true");
//        properties.setProperty(MysqlExportService.EMAIL_START_TLS_ENABLED, "true");
        properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");
//
////set the outputs temp dir
//        properties.setProperty(MysqlExportService.TEMP_DIR, "/tmp/mysql_dump");
//        writeToFile(null);
//        properties.setProperty(MysqlExportService.TEMP_DIR, Paths.get("backup.txt").toString());
        properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());

        try {
            mysqlExportService = new MysqlExportService(properties);
            mysqlExportService.export();
            file = mysqlExportService.getGeneratedZipFile();
//            String generatedSql = mysqlExportService.getGeneratedSql();
            if (file == null) {
                log.info("No SQL generated. Check your database connection or export service.");
            } else {
//                byteArray = generatedSql.getBytes(StandardCharsets.UTF_8);
//                writeToFile(byteArray);
                log.info("Writing ByteArray for Backup...");
                mysqlExportService.clearTempFiles();
                log.info("Clearing TempFiles...");
            }

        } catch (Exception e) {
            log.info("Error occurred during SQL export: " + e.getMessage());
        }

//        MysqlExportService mysqlExportService = new MysqlExportService(properties);
//        //If we want to get the raw exported SQL dump as a String we only need to call this method:
//        String generatedSql = mysqlExportService.getGeneratedSql();
//        byte[] byteArray = generatedSql.getBytes(StandardCharsets.UTF_8);
//        log.info("byteArray: " + Arrays.toString(byteArray));

//        sendingEmail.send("attachment", "balmburren@gmail.com", "Backup", "Neues Backup ist bereit", byteArray, "", "backup.txt");



//
//
//
//        //get the generated file as a Java File object
//        properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");
//
//        //This property instructs mysql-backup4j to preserve the generated zip file so that we can access it:
//        File file = mysqlExportService.getGeneratedZipFile();
//
//        //Once we’re done, we have to manually clear the zip file from the TEMP_DIR by calling:
//        //mysqlExportService.clearTempFiles(false);
//
//        //If we want to get the raw exported SQL dump as a String we only need to call this method:
//        String generatedSql = mysqlExportService.getGeneratedSql();
//
//        properties.setProperty(MysqlExportService.DELETE_EXISTING_DATA, "true");
//        properties.setProperty(MysqlExportService.DROP_TABLES, "true");
//        properties.setProperty(MysqlExportService.ADD_IF_NOT_EXISTS, "true");
//        properties.setProperty(MysqlExportService.JDBC_DRIVER_NAME, "root.ss");
//        properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, "jdbc:mysql://localhost:3306/database-name");
//        properties.setProperty(MysqlExportService.PRESERVE_GENERATED_SQL_FILE, "true");
    }

    public void sendBackup(){
        try {
            byteArray = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.info("ReadallBytes from File throw exceptioo");
            throw new RuntimeException(e);
        }
        sendingEmail.send("attachment", "balmburren@gmail.com", "Backup", "Neues Backup ist bereit", byteArray, "", "backup.zip");
//        String generatedSql = mysqlExportService.getGeneratedSql();
//        if (generatedSql == null) {
//            log.info("No SQL generated. Check your database connection or export service.");
//        } else {
////            byteArray = generatedSql.getBytes(StandardCharsets.UTF_8);
////            String fileName = writeToFile(byteArray);
//            sendingEmail.send("attachment", "balmburren@gmail.com", "Backup", "Neues Backup ist bereit", byteArray, "", "backup.txt");
//        }
    }

    public void importDatabase(byte[] bytearray ){
        byte[] bytearrayDecompressed;
        try {
            bytearrayDecompressed = toUnzippedByteArray(byteArray);
        } catch (IOException e) {
            log.info("Decompressing of zip file is not working: " + e.getMessage());
            throw new RuntimeException(e);
        }

        String sql;
        //            sql = new String(Files.readAllBytes(Paths.get("path/to/sql/dump/file.sql")));
        sql = bytearrayDecompressed.toString();

        try {
            boolean res = MysqlImportService.builder()
                    .setDatabase("balmburren_db")
                    .setSqlString(sql)
                    .setUsername("root")
                    .setPassword("secret")
                    .setHost("localhost")
                    .setPort("3307")
                    .setDeleteExisting(true)
                    .setDropExisting(true)
                    .importDatabase();
            if (!res)log.info("SQLImport not working!");
        } catch (SQLException e) {
            log.info("SQLException building import" + e.getMessage());
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            log.info("ClassNotFoundException building import" + e.getMessage());
            throw new RuntimeException(e);
        }
        log.info("Importing database is successful.");
    }

//    private  String writeToFile(byte[] bytes) {
//        String fileName = "backup.txt";  // Save to a local path
//        try (FileOutputStream fos = new FileOutputStream(fileName)) {
//            fos.write(bytes);
//        } catch (FileNotFoundException fileNotFoundException) {
//            log.info("TXT File not found!");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return fileName;  // Return the path to the created file
//    }

    public  byte[] toUnzippedByteArray(byte[] zippedBytes) throws IOException {
        var zipInputStream = new ZipInputStream(new ByteArrayInputStream(zippedBytes));
        var buff = new byte[1024];
        if (zipInputStream.getNextEntry() != null) {
            var outputStream = new ByteArrayOutputStream();
            int l;
            while ((l = zipInputStream.read(buff)) > 0) {
                outputStream.write(buff, 0, l);
            }
            return outputStream.toByteArray();
        }
        return new byte[0];
    }



}
