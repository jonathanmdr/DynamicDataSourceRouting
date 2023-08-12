package br.com.multidatasources.config.micrometer;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class MeterMetricSender implements MetricSender {

    private final MeterRegistry meterRegistry;

    public MeterMetricSender(final MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void increment(final String metricName, final Map<String, String> tags) {
        final var appendedTags = appendDefaultTags(tags);
        this.meterRegistry.counter(metricName, toTags(appendedTags)).increment();
    }

    @Override
    public void increment(final String metricName, final Map<String, String> tags, final double incrementValue) {
        final var appendedTags = appendDefaultTags(tags);
        this.meterRegistry.counter(metricName, toTags(appendedTags)).increment(incrementValue);
    }

    @Override
    public void gauge(final String metricName, final Map<String, String> tags, final Number currentValue) {
        final var appendedTags = appendDefaultTags(tags);
        Gauge.builder(metricName, () -> currentValue)
            .tags(toTags(appendedTags))
            .register(this.meterRegistry);
    }

    private Map<String, String> appendDefaultTags(final Map<String, String> tags) {
        final var tagsAppender = new HashMap<>(tags);

        appendTagIfExists("trace_id", tagsAppender);
        appendTagIfExists("span_id", tagsAppender);

        return tagsAppender;
    }

    private void appendTagIfExists(final String tagName, final Map<String, String> appender) {
        Optional.ofNullable(MDC.get(tagName))
            .ifPresent(tagValue -> appender.put(tagName, tagValue));
    }

    private Tags toTags(final Map<String, String> tagsToConvert) {
        final var listTags = tagsToConvert.entrySet()
            .stream()
            .map(tag -> Tag.of(tag.getKey(), tag.getValue()))
            .toList();

        return Tags.of(listTags);
    }

}
