package com.messerli.balmburren.util;

import com.messerli.balmburren.entities.RoleEnum;
import com.messerli.balmburren.dtos.RegisterUserDto;
import com.messerli.balmburren.entities.*;
import com.messerli.balmburren.repositories.RoleRepository;
import com.messerli.balmburren.repositories.UserRepository;
import com.messerli.balmburren.repositories.UsersRoleRepo;
import com.messerli.balmburren.services.CronService;
import com.messerli.balmburren.services.FlywayService;
import com.smattme.MysqlExportService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


@Service
@Slf4j
public class Cronjob implements CronService {

    @Autowired
    private SendingEmail sendingEmail;
    @Autowired
    private FlywayService flywayService;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UsersRoleRepo usersRoleRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static byte[] byteArray;
    private static MysqlExportService mysqlExportService;
    private static String zipFilePath;
    private static String filename;


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
//        // Database connection details
//        String dbUser = "root";          // Your MySQL username
//        String dbPassword = "secret";  // Your MySQL password
//        String dbName = "balmburren_db";   // The database you want to dump
//        String dumpFilePath = "/tmp/backup.sql";  // Temporary file for dump
//        zipFilePath = "/backup.zip";       // Output ZIP file in root folder
//        String podName = "mysqldb-5476f6c675-pl6nb";
//        String namespace = "default";
//
//        // Path to kubeconfig file
//        String kubeconfigPath = System.getProperty("user.home") + "/exoscale/balmburren-cluster.kubeconfig";
//
//        // Command to exec mysqldump within the pod using kubectl, namespace, and kubeconfig
//
//        String dumpCommand = String.format(
//                "kubectl --kubeconfig=%s exec  %s -- mysqldump -u %s -p%s %s > %s",
//                kubeconfigPath, podName,  dbUser, dbPassword, dbName, dumpFilePath
//        );
//        // mysqldump command
////        String dumpCommand = "mysqldump -u " + dbUser + " -p" + dbPassword + " " + dbName + " > " + dumpFilePath;
//
//        // Execute the dump command
//        try {
//            Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", dumpCommand});
//
//            // Wait for the process to complete
//            int exitCode = process.waitFor();
//            if (exitCode == 0) {
//                log.info("Database dump successful. File saved at " + dumpFilePath);
//
//                // Now zip the dump file
//                zipFile(dumpFilePath, zipFilePath);
//
//                // Optionally, delete the original dump file after zipping
//                File dumpFile = new File(dumpFilePath);
//                if (dumpFile.delete()) {
//                    log.info("Temporary dump file deleted.");
//                } else {
//                    log.info("Failed to delete temporary dump file.");
//                }
//
//            } else {
//                log.info("Database dump failed with exit code: " + exitCode);
//            }
//
//        } catch (IOException | InterruptedException e) {
//            e.printStackTrace();
//        }
//    }




//        Properties properties = new Properties();
//        properties.setProperty(MysqlExportService.DB_NAME, "balmburren_db");
//        properties.setProperty(MysqlExportService.DB_USERNAME, "root");
//        properties.setProperty(MysqlExportService.DB_PASSWORD, "secret");
//        properties.setProperty(MysqlExportService.DB_HOST, "localhost");
//        properties.setProperty(MysqlExportService.DB_PORT, "3307");
//        properties.setProperty(MysqlExportService.JDBC_CONNECTION_STRING, "jdbc:mysql://localhost:3307/balmburren_db");
//
//
//        properties.setProperty(MysqlExportService.PRESERVE_GENERATED_ZIP, "true");
//
//        properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());
//
//        try {
//            mysqlExportService = new MysqlExportService(properties);
//            mysqlExportService.export();
//            file = mysqlExportService.getGeneratedZipFile();
//
//            if (file == null) {
//                log.info("No SQL generated. Check your database connection or export service.");
//            } else {
//
//                log.info("Writing ByteArray for Backup...");
//                mysqlExportService.clearTempFiles();
//                log.info("Clearing TempFiles...");
//            }
//
//        } catch (Exception e) {
//            log.info("Error occurred during SQL export: " + e.getMessage());
        }







    private static void zipFile(String sourceFilePath, String zipFilePath) {
        try (
                FileOutputStream fos = new FileOutputStream(zipFilePath);
                ZipOutputStream zos = new ZipOutputStream(fos);
                FileInputStream fis = new FileInputStream(sourceFilePath)
        ) {
            ZipEntry zipEntry = new ZipEntry("backup.sql");  // Name of file inside the zip
            zos.putNextEntry(zipEntry);

            byte[] buffer = new byte[1024];
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }

            zos.closeEntry();
            log.info("ZIP file created at: " + zipFilePath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Transactional
    public void sendBackup(){
        try {
            byteArray = Files.readAllBytes(Path.of(zipFilePath));
        } catch (IOException e) {
            log.info("ReadallBytes from File throw exceptioo");
            throw new RuntimeException(e);
        }
        sendingEmail.send("attachment", "balmburren@gmail.com", "Backup", "Neues Backup ist bereit", byteArray, "", "backup.zip");

    }
    @Transactional
    public void importDatabase(String bytearray ){

        byte[] decodedBytes = Base64.getDecoder().decode(bytearray);

        // Step 2: Convert byte[] to a String (assuming UTF-8 encoding)
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);



        String sql;
        sql = decodedString;
        writeSQLDataForFlyway(sql);


//        flywayService.migrateDatabase();


    }
    @Transactional
    public void migrateDB(){
        flywayService.migrateDatabase();
    }

    private void writeSQLDataForFlyway(String sqlString) {
        String dirPath = "src/main/resources/db/migration/data";
        String fileName = "V40__INSERT_BACKUP.SQL";
        Path path = Paths.get(dirPath);
        if (!Files.exists(path)) {
            try {
                Files.createDirectories(path);
            } catch (IOException e) {
                log.info("Failed to create directory: " + e.getMessage());
                return;
            }
        }
        String filePath = dirPath + "/" + fileName;
        try (FileWriter writer = new FileWriter(new File(filePath), false)) {
            writer.write(sqlString);
            log.info("SQL file written to: " + filePath);
        } catch (IOException e) {
            log.info("Error writing file: " + e.getMessage());
        }
    }

}
