package com.messerli.balmburren.controllers;

import com.messerli.balmburren.services.FlywayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = {"http://localhost:4200","http://localhost:8006"}, exposedHeaders = {"Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"})
@RequestMapping("/re/")
@RestController
public class ResetController {

    private final FlywayService flywayService;

    public ResetController(FlywayService flywayService) {
        this.flywayService = flywayService;
    }

    @CrossOrigin( allowCredentials = "true")
    @PreAuthorize("hasAnyAuthority('ADMIN','SUPER_ADMIN')")
    @GetMapping("reset/")
    ResponseEntity<?> resetDB() {
        flywayService.resetDatabase();
        return ResponseEntity.ok().body("Migrating database with flyway...");}




}
