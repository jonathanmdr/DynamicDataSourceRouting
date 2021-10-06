package br.com.multidatasources.multidatasources.config.flyway;

import br.com.multidatasources.multidatasources.config.properties.flyway.FlywayProperties;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

import static br.com.multidatasources.multidatasources.config.datasource.MasterDataSourceConfiguration.MASTER_DATA_SOURCE_QUALIFIER;

@Configuration
public class FlywayConfiguration {

    private final DataSource dataSource;
    private final FlywayProperties flywayProperties;

    public FlywayConfiguration(@Qualifier(MASTER_DATA_SOURCE_QUALIFIER) DataSource dataSource,
                               FlywayProperties flywayProperties
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
