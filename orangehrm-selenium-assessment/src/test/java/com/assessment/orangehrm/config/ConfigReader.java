package com.assessment.orangehrm.config;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

public final class ConfigReader {
    private static final Properties PROPERTIES = new Properties();

    static {
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new IllegalStateException("config.properties was not found on the classpath");
            }
            PROPERTIES.load(input);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    private ConfigReader() {
    }

    public static String get(String key) {
        String systemValue = System.getProperty(key);
        return systemValue != null && !systemValue.isBlank()
                ? systemValue
                : PROPERTIES.getProperty(key);
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String loginUrl() {
        return baseUrl() + get("login.path");
    }

    public static Duration explicitWait() {
        return Duration.ofSeconds(Long.parseLong(get("explicit.wait.seconds")));
    }
}
