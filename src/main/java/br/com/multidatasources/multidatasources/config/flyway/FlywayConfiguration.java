package br.com.multidatasources.multidatasources.config.flyway;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayConfiguration {

    private final DataSource dataSource;

    public FlywayConfiguration(@Qualifier("masterDataSource") DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public Flyway flyway(){
        return Flyway.configure()
                .baselineOnMigrate(true)
                .locations("classpath:db/migration", "classpath:db/testdata")
                .dataSource(dataSource)
                .schemas("billionaires")
                .target(MigrationVersion.LATEST)
                .load();
    }

    @PostConstruct
    public void migrate() {
        Flyway flyway = flyway();
        flyway.migrate();
    }

}
