package br.com.multidatasources.api;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public final class ResourceUriHelper {

    private ResourceUriHelper() { }

    public static URI uriFrom(final Object resourceId) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(resourceId)
                .toUri();
    }

}
