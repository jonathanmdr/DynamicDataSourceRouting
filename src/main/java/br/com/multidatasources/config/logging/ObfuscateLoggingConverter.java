package br.com.multidatasources.config.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.pattern.CompositeConverter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ObfuscateLoggingConverter extends CompositeConverter<ILoggingEvent> {

    private static final Map<String, Pattern> sensitiveFieldPatterns = new HashMap<>();
    private static final int GROUP_FIELD_IN_MESSAGE_INDEX = 1;
    private static final int GROUP_FIELD_IN_JSON_INDEX = 2;

    static {
        final List<String> fieldNames = getAllSensitiveFieldNames();
        for (final String fieldName : fieldNames) {
            final String regex = "(?i)\\b" + Pattern.quote(fieldName) + "\\b\\s*[:=]\\s*\"?([^\",\\s]*)\"?|\"" + Pattern.quote(fieldName) + "\":\"([^\"]*)\"";
            sensitiveFieldPatterns.put(fieldName, Pattern.compile(regex));
        }
    }

    @Override
    protected String transform(final ILoggingEvent event, final String in) {
        String message = in;
        for (Map.Entry<String, Pattern> entry : sensitiveFieldPatterns.entrySet()) {
            message = obfuscateFieldValue(message, entry.getValue());
        }
        message = obfuscateArguments(event.getArgumentArray(), message);
        message = obfuscateMDC(event.getMDCPropertyMap(), message);
        return message.concat("\n");
    }

    private static String obfuscateFieldValue(final String message, final Pattern pattern) {
        final Matcher matcher = pattern.matcher(message);
        final StringBuilder sb = new StringBuilder();
        while (matcher.find()) {
            if (matcher.group(GROUP_FIELD_IN_MESSAGE_INDEX) != null) {
                matcher.appendReplacement(sb, matcher.group().replace(matcher.group(GROUP_FIELD_IN_MESSAGE_INDEX), "****"));
            }
            if (matcher.group(GROUP_FIELD_IN_JSON_INDEX) != null) {
                matcher.appendReplacement(sb, matcher.group().replace(matcher.group(GROUP_FIELD_IN_JSON_INDEX), "****"));
            }
        }
        matcher.appendTail(sb);
        return sb.toString();
    }

    private static String obfuscateArguments(final Object[] arguments, String message) {
        if (arguments != null) {
            for (final Object arg : arguments) {
                if (arg != null) {
                    for (final Pattern pattern : sensitiveFieldPatterns.values()) {
                        message = obfuscateFieldValue(message, pattern);
                    }
                }
            }
        }
        return message;
    }

    private static String obfuscateMDC(final Map<String, String> mdcPropertyMap, String message) {
        if (mdcPropertyMap != null) {
            for (final Map.Entry<String, String> entry : mdcPropertyMap.entrySet()) {
                if (sensitiveFieldPatterns.containsKey(entry.getKey())) {
                    message = obfuscateFieldValue(message, sensitiveFieldPatterns.get(entry.getKey()));
                }
            }
        }
        return message;
    }

    private static List<String> getAllSensitiveFieldNames() {
        try (final InputStream inputStream = ObfuscateLoggingConverter.class.getResourceAsStream("/obfuscate")) {
            if (inputStream == null) {
                return Collections.emptyList();
            }
            return new BufferedReader(new InputStreamReader(inputStream)).lines()
                .map(String::trim)
                .toList();
        } catch (final IOException ex) {
            return Collections.emptyList();
        }
    }

}