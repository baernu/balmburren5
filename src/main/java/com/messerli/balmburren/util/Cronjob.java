package com.messerli.balmburren.util;

import com.smattme.MysqlExportService;
import com.smattme.MysqlImportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Properties;


@Component
@Slf4j
public class Cronjob {

    @Autowired
    private SendingEmail sendingEmail;

//    @Scheduled(cron = "0 0 * * * ?")
@Scheduled(cron = "0 54 15 * * *")
    public void backup() {
    try {
        setupBackup();
    } catch (SQLException | IOException e) {
        log.info("in the process of backup: RuntimeException...");
        throw new RuntimeException(e);
    } catch (ClassNotFoundException e) {
        log.info("in the process of backup: ClassNotFoundException...");
        throw new RuntimeException(e);
    }
    log.info("in the process of backup after exceptions...");
    }


    private void setupBackup() throws SQLException, IOException, ClassNotFoundException {
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
//
////set the outputs temp dir
        properties.setProperty(MysqlExportService.TEMP_DIR, new File("external").getPath());

        MysqlExportService mysqlExportService = new MysqlExportService(properties);
        //If we want to get the raw exported SQL dump as a String we only need to call this method:
        String generatedSql = mysqlExportService.getGeneratedSql();
        byte[] byteArray = generatedSql.getBytes(StandardCharsets.UTF_8);
        log.info("byteArray: " + Arrays.toString(byteArray));

        sendingEmail.send("attachment", "balmburren@gmail.com", "Backup", "Neues Backup ist bereit", byteArray, "", "backup.txt");


//        mysqlExportService.export();
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

    private void importDatabase() throws SQLException, ClassNotFoundException, IOException {
        String sql = new String(Files.readAllBytes(Paths.get("path/to/sql/dump/file.sql")));

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

        assertTrue(res);
    }

    private void assertTrue(boolean res) {
    }
}
