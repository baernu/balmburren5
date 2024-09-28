package com.messerli.balmburren.util;

import com.messerli.balmburren.services.FlywayService;
import jakarta.transaction.Transactional;
import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class FlywayImpl implements FlywayService {
    @Override
    public void resetTables() {
//        Flyway flyway = null;
//        flyway.clean();
//        flyway.migrate();
    }

//    @Bean
//    public Flyway flyway() {
//        Flyway flyway = new Flyway();
//        flyway.setDataSource(dataSource);
//        flyway.setLocations("filesystem:/migrations");
//        flyway.setSchemas("my_schema");
//        return flyway;
//    }
//
//    @Autowired
//    private Flyway flyway;
//
//    public void migrateDatabase() {
//        flyway. Migrate();
//    }
}
