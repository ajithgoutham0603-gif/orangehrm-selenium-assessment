package com.assessment.orangehrm.utils;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class ResourceUtils {
    private ResourceUtils() {
    }

    public static Path path(String resourcePath) {
        URL resource = ResourceUtils.class.getClassLoader().getResource(resourcePath);
        if (resource == null) {
            throw new IllegalArgumentException("Resource not found: " + resourcePath);
        }
        try {
            return Paths.get(resource.toURI());
        } catch (URISyntaxException e) {
            throw new IllegalStateException("Invalid resource path: " + resourcePath, e);
        }
    }
}
