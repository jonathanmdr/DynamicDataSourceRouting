package br.com.multidatasources.config.flyway;

import br.com.multidatasources.config.datasource.master.MasterDataSource;
import br.com.multidatasources.config.properties.flyway.FlywayProperties;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration {

    private final DataSource dataSource;
    private final FlywayProperties flywayProperties;

    public FlywayConfiguration(
        @MasterDataSource final DataSource dataSource,
        final FlywayProperties flywayProperties
    ) {
        this.dataSource = dataSource;
        this.flywayProperties = flywayProperties;
    }

    @Bean
    public Flyway flyway(){
        return Flyway.configure()
            .baselineOnMigrate(flywayProperties.isBaselineOnMigrate())
            .locations(flywayProperties.getLocations())
            .dataSource(dataSource)
            .schemas(flywayProperties.getSchemaName())
            .target(MigrationVersion.LATEST)
            .load();
    }

}
