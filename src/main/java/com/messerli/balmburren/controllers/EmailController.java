package com.messerli.balmburren.controllers;

import com.messerli.balmburren.services.CronService;
import com.messerli.balmburren.util.EmailData;
import com.messerli.balmburren.util.QRInvoice;
import com.messerli.balmburren.services.EmailService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006","https://www.balmburren.net:4200"}
        , exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/em/")
@RestController
public class EmailController {

    private final EmailService emailService;
//    private final CronService cronService;

    public EmailController(EmailService emailService, CronService cronService) {
        this.emailService = emailService;
//        this.cronService = cronService;
    }


    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @PostMapping("send/email/")
    public ResponseEntity<?> sendEmailAttachment(@RequestBody EmailData emailData) {
        emailService.sendEmailAttachment(emailData.getType(), emailData.getToEmail(), emailData.getSubject(), emailData.getBody(), emailData.getByteArray(), emailData.getBase64String(), emailData.getFilename());
//        log.info("Sending email with attachment: {}", emailData);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/send/email/attachment").toUriString());
        return ResponseEntity.created(uri).body(emailData);
    }

    @CrossOrigin( allowCredentials = "true")
    @GetMapping("send/email/register/{email}")
    public ResponseEntity<?> sendEmailRegister(@PathVariable("email") String email) {
        emailService.sendEmailRegister(email);
        return ResponseEntity.ok().body("Sending Register Email.");
    }

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN')")
    @PostMapping("qrcode")
    public ResponseEntity<?> getQRInvoice(@RequestBody QRInvoice qrInvoice) {
        String string = emailService.getQRInvoice(qrInvoice);
        log.info("Get QRInvoice: {}", string);
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/qrcode").toUriString());
        return ResponseEntity.created(uri).body(string);
    }

//    @CrossOrigin( allowCredentials = "true")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER')")
//    @PostMapping("send/email/tourdata")
//    public ResponseEntity<?> sendTourData(@RequestBody Client[] clients) {
//        String string = emailService.sendTourData(clients);
//        log.info("Tour data ss Json {}", string);
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/send/email/tourdata").toUriString());
//        return ResponseEntity.created(uri).body(string);
//    }

//    @CrossOrigin( allowCredentials = "true")
//    @PreAuthorize("hasAnyAuthority('ADMIN', 'SUPER_ADMIN', 'DRIVER')")
//    @PostMapping("send/retour/tourdata")
//    public ResponseEntity<?> retourTourData(@RequestBody String json) {
//        List<Client> list = emailService.retourTourData(json);
//        log.info("Tour data retour {}", list);
//        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/send/retour/tourdata").toUriString());
//        return ResponseEntity.created(uri).body(list);
//    }

//    @CrossOrigin( allowCredentials = "true")
//    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
//    @GetMapping("backup/send/")
//    public ResponseEntity<?> backupSend() {
//        cronService.sendBackup();
//        return ResponseEntity.ok().body("Backup sending...");
//    }

//    @CrossOrigin( allowCredentials = "true")
//    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
//    @GetMapping("backup/tofile/")
//    public ResponseEntity<?> backupToFile() {
//        cronService.writeBackupToFile();
//        return ResponseEntity.ok().body("Backup write to file...");
//    }
//    @CrossOrigin( allowCredentials = "true")
//    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
//    @PatchMapping("backup/import/")
//    public ResponseEntity<ByteDTO> backupUImport(@RequestBody ByteDTO byteDTO) {
//        cronService.importDatabase(byteDTO.getBytearray());
//        return ResponseEntity.ok().body(byteDTO);
//    }

//    @CrossOrigin( allowCredentials = "true")
//    @PreAuthorize("hasAuthority('SUPER_ADMIN')")
//    @GetMapping("backup/import/migrate/")
//    public ResponseEntity<?> backupUImportMigrate() {
//        cronService.migrateDB();
//        return ResponseEntity.ok().body("Migrating DB.");
//    }

}
