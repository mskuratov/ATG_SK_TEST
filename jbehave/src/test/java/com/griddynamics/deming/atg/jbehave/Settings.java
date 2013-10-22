package com.griddynamics.deming.atg.jbehave;

import org.apache.commons.lang.Validate;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Settings {

    private static final String CRS_URL = "crs.url";

    public static final String crsUrl;

    static {
        Properties properties = new Properties();

        InputStream inputStream = Settings.class.getClassLoader().getResourceAsStream("jbehave.properties");
        Validate.notNull(inputStream, "jbehave.properties file is not found in classpath.");

        try {
            try {
                properties.load(inputStream);
            } finally {
                inputStream.close();
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load settings", e);
        }

        crsUrl = getString(properties, CRS_URL);
    }

    private static String getString(Properties properties, String property) {
        String value = properties.getProperty(property);

        if (value == null) {
            throw new IllegalStateException(String.format("Property %s is not found", property));
        }

        return value;
    }
}
