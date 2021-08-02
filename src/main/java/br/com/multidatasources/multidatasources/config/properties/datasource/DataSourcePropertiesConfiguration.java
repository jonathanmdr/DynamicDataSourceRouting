package br.com.multidatasources.multidatasources.config.properties.datasource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataSourcePropertiesConfiguration {

    public static final String MASTER_PROPERTIES_QUALIFIER = "masterProperties";
    public static final String SLAVE_PROPERTIES_QUALIFIER = "slaveProperties";

    @Bean
    @ConfigurationProperties(prefix = "master.datasource")
    public DatabaseConnectionProperties masterProperties() {
        return new DatabaseConnectionProperties();
    }

    @Bean
    @ConfigurationProperties(prefix = "slave.datasource")
    public DatabaseConnectionProperties slaveProperties() {
        return new DatabaseConnectionProperties();
    }

}
