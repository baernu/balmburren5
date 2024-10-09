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
    public void importDatabase(String bytearray ){

        byte[] decodedBytes = Base64.getDecoder().decode(bytearray);

        // Step 2: Convert byte[] to a String (assuming UTF-8 encoding)
        String decodedString = new String(decodedBytes, StandardCharsets.UTF_8);



        String sql;
        sql = decodedString;
        writeSQLDataForFlyway(sql);


        flywayService.migrateDatabase();


    }

    private void writeSQLDataForFlyway(String sqlString) {
        String dirPath = "src/main/resources/db/migration/data";
        String fileName = "V100_INSERT_UPDATE.SQL";
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


    private void loadRoles() {
        RoleEnum[] roleNames = new RoleEnum[] { RoleEnum.USER, RoleEnum.ADMIN, RoleEnum.SUPER_ADMIN, RoleEnum.DRIVER, RoleEnum.KATHY, RoleEnum.USER_KATHY };
        Map<RoleEnum, String> roleDescriptionMap = Map.of(
                RoleEnum.USER, "Default user role",
                RoleEnum.DRIVER, "Driver role",
                RoleEnum.KATHY, "Helper role",
                RoleEnum.USER_KATHY, "User administrated by Kathy",
                RoleEnum.ADMIN, "Administrator role",
                RoleEnum.SUPER_ADMIN, "Super Administrator role"
        );

        Arrays.stream(roleNames).forEach((roleName) -> {
            Optional<Role> optionalRole = roleRepository.findByName(roleName);

            optionalRole.ifPresentOrElse(System.out::println, () -> {
                Role roleToCreate = new Role();

                roleToCreate.setName(roleName);
                roleToCreate.setDescription(roleDescriptionMap.get(roleName));

                roleRepository.save(roleToCreate);
            });
        });
    }

    private void createAdmin(){
        RegisterUserDto userDto = new RegisterUserDto();
        userDto.setFirstname("Normal").setLastname("Admin").setUsername("admin").setPassword("adminadmin");

        var user = new User();
        user.setFirstname(userDto.getFirstname());
        user.setLastname(userDto.getLastname());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEnabled(true);
        if(userRepository.existsByUsername(userDto.getUsername()))
            return;
        User user1 = userRepository.save(user);


        Optional<Role> optionalRole = roleRepository.findByName(RoleEnum.ADMIN);
        Optional<Role> optionalRole1 = roleRepository.findByName(RoleEnum.USER);
        if (optionalRole.isEmpty() || optionalRole1.isEmpty() || user1.getUsername().isEmpty()) {
            return;
        }

        Optional<UsersRole> usersRole = Optional.of(new UsersRole());
        usersRole.get().setUser(user1);
        usersRole.get().setRole(optionalRole.get());
        usersRoleRepo.save(usersRole.get());
        userRepository.save(user1);
    }




}
