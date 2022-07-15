package br.com.multidatasources.config.flyway;

import org.flywaydb.core.Flyway;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

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
