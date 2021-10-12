package br.com.multidatasources.controller.utils;

import java.net.URI;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

public class ResourceUriHelper {

    private ResourceUriHelper() { }

    public static URI getUri(Object resourceId) {
        return ServletUriComponentsBuilder.fromCurrentRequestUri()
                .path("/{id}")
                .buildAndExpand(resourceId)
                .toUri();
    }

}
