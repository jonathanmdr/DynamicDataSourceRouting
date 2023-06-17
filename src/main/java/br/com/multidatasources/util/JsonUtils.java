package br.com.multidatasources.util;

import br.com.multidatasources.config.objectmapper.ObjectMapperConfig;

import java.util.concurrent.Callable;

public final class JsonUtils {

    private JsonUtils() { }

    public static String toJson(final Object object) {
        return execute(() -> ObjectMapperConfig.objectMapper().writeValueAsString(object));
    }

    public static <T> T fromJson(final String json, final Class<T> type) {
        return execute(() -> ObjectMapperConfig.objectMapper().readValue(json, type));
    }

    private static <T> T execute(final Callable<T> callable) {
        try {
            return callable.call();
        } catch (final Exception ex) {
            throw new RuntimeException("An unexpected error occurred");
        }
    }

}
