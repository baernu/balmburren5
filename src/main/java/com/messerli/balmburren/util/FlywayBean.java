package com.messerli.balmburren.util;

import jakarta.transaction.Transactional;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FlywayBean {

//    @Autowired
//    private Flyway flyway;
//    @Override
//    public void resetTables() {
//        flyway.clean();
//        flyway.migrate();
//    }


    @Bean
    public FlywayMigrationStrategy clean() {
        return flyway -> {
            flyway.clean();
            flyway.migrate();
        };
    }
}
