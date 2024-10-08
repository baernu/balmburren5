package com.messerli.balmburren.services.serviceImpl;

import com.messerli.balmburren.services.FlywayService;
import jakarta.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
public class FlywayServiceImpl implements FlywayService {

    @Autowired
    private Flyway flyway;

    @Transactional
    @Override
    public void migrateDatabase() {
//        flyway.clean();
        flyway.repair();
        flyway.migrate();

    }

    @Transactional
    @Override
    public void clearDatabase() {
        flyway.clean();
//        flyway.migrate();
    }



}
