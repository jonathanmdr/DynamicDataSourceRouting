package br.com.multidatasources.config.objectmapper;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

@Configuration
public class ObjectMapperConfig {

    private static final ObjectMapper objectMapper = new Jackson2ObjectMapperBuilder()
        .dateFormat(new StdDateFormat())
        .featuresToDisable(
            DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
            DeserializationFeature.FAIL_ON_NULL_FOR_PRIMITIVES,
            DeserializationFeature.FAIL_ON_NULL_CREATOR_PROPERTIES,
            SerializationFeature.WRITE_DATES_AS_TIMESTAMPS
        )
        .modules(
            new JavaTimeModule(),
            new Jdk8Module()
        )
        .propertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE)
        .build()
        .setSerializationInclusion(JsonInclude.Include.NON_NULL);

    @Bean
    public static ObjectMapper objectMapper() {
        return objectMapper.copy();
    }

}
