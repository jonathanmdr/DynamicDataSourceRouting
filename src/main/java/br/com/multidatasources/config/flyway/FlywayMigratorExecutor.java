package br.com.multidatasources.config.flyway;

import jakarta.annotation.PostConstruct;
import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

@Component
public class FlywayMigratorExecutor {

    private final Flyway flyway;

    public FlywayMigratorExecutor(final Flyway flyway) {
        this.flyway = flyway;
    }

    @PostConstruct
    public void migrate() {
        flyway.migrate();
    }

}
