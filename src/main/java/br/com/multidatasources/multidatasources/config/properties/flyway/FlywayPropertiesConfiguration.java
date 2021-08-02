package br.com.multidatasources.multidatasources.config.properties.flyway;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayPropertiesConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "flyway")
    public FlywayProperties flywayProperties() {
        return new FlywayProperties();
    }

}
