package com.messerli.balmburren.services;

import jakarta.transaction.Transactional;

public interface FlywayService {
    @Transactional
    void migrateDatabase();

}
