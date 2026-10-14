package com.assessment.orangehrm.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;

public final class JsonDataReader {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private JsonDataReader() {
    }

    public static <T> T read(String resourcePath, Class<T> type) {
        try (InputStream input = JsonDataReader.class.getClassLoader().getResourceAsStream(resourcePath)) {
            if (input == null) {
                throw new IllegalArgumentException("Test data not found: " + resourcePath);
            }
            return OBJECT_MAPPER.readValue(input, type);
        } catch (IOException e) {
            throw new IllegalStateException("Unable to read JSON test data: " + resourcePath, e);
        }
    }
}
