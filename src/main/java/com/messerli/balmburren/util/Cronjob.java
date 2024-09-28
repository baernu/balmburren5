package com.messerli.balmburren.util;

import com.messerli.balmburren.entities.RoleSeeder;
import com.messerli.balmburren.services.CronService;
import com.messerli.balmburren.services.FlywayService;
import com.smattme.MysqlExportService;
import com.smattme.MysqlImportService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


@Service
@Slf4j
public class Cronjob implements CronService {

    @Autowired
    private SendingEmail sendingEmail;
    @Autowired
    private FlywayService flywayService;

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

    @Transactional
    public void writeBackupToFile() {

        Properties properties = new Properties();
        properties.setProperty(MysqlExportService.DB_NAME, "balmburren_db");
        properties.setProperty(MysqlExportService.DB_USERNAME, "root");
        properties.setProperty(MysqlExportService.DB_PASSWORD, "secret");
        properties.setProperty(MysqlExportService.DB_HOST, "localhost");
        properties.setProperty(MysqlExportService.DB_PORT, "3307");
        properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, "jdbc:mysql://localhost:3307/balmburren_db");


        properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");

        properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());

        try {
            mysqlExportService = new MysqlExportService(properties);
            mysqlExportService.export();
            file = mysqlExportService.getGeneratedZipFile();

            if (file == null) {
                log.info("No SQL generated. Check your database connection or export service.");
            } else {

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
    @Transactional
    public void sendBackup(){
        try {
            byteArray = Files.readAllBytes(file.toPath());
        } catch (IOException e) {
            log.info("ReadallBytes from File throw exceptioo");
            throw new RuntimeException(e);
        }
        sendingEmail.send("attachment", "balmburren@gmail.com", "Backup", "Neues Backup ist bereit", byteArray, "", "backup.zip");

    }
    @Transactional
    public void importDatabase(byte[] bytearray ){
        flywayService.resetDatabase();

        String sql;
        sql = Arrays.toString(bytearray);

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

//    public byte[] extractSqlFileFromZip(byte[] zipByteArray) throws IOException {
//        byte[] sqlFileBytes = null;
//        try{
//            InputStream byteArrayInputStream = new ByteArrayInputStream(zipByteArray);
//            ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);
//
//            ZipEntry zipEntry;
//
//
//            // Iterate through the entries in the ZIP file
//            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
//                // Check if the entry is a .sql file
//                if (zipEntry.getName().endsWith(".sql")) {
//                    // Read the content of the .sql file into a byte array
//                    sqlFileBytes = extractFileFromZip(zipInputStream);
//                    break; // Exit the loop after finding the .sql file
//                }
//            }
//
//            // Close the streams
//            zipInputStream.close();
//            byteArrayInputStream.close();
//
//        }catch(Exception e) {
//            log.info("Decompressing of zip file not working: " + e.getMessage());
//        }
//
//        return sqlFileBytes; // Return the byte array of the .sql file content
//    }

//    private byte[] extractFileFromZip(InputStream inputStream) throws IOException {
//        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//        byte[] buffer = new byte[1024];
//        int length;
//
//        // Read the file content into the ByteArrayOutputStream
//        while ((length = inputStream.read(buffer)) > 0) {
//            byteArrayOutputStream.write(buffer, 0, length);
//        }
//
//        return byteArrayOutputStream.toByteArray(); // Convert to byte array
//    }



}
