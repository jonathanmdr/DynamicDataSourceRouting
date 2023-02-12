package br.com.multidatasources.config.properties.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourceConnectionPropertiesConfiguration {

    @Bean
    @MasterProperties
    @ConfigurationProperties(prefix = "master.datasource")
    public DatabaseConnectionProperties masterProperties() {
        return new DatabaseConnectionProperties();
    }

    @Bean
    @ReplicaProperties
    @ConfigurationProperties(prefix = "replica.datasource")
    public DatabaseConnectionProperties replicaProperties() {
        return new DatabaseConnectionProperties();
    }

}
