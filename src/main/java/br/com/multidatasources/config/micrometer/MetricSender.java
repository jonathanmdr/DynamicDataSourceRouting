package br.com.multidatasources.config.micrometer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.validation.annotation.Validated;

import java.util.Map;

@Validated
public interface MetricSender {

    void increment(@NotBlank final String metricName, @NotNull final Map<String, String> tags);
    void increment(@NotBlank final String metricName, @NotNull final Map<String, String> tags, final double incrementValue);
    void gauge(@NotBlank final String metricName, @NotNull final Map<String, String> tags, @NotNull final Number value);

}
