package com.griddynamics.deming.atg.jbehave

import org.apache.commons.lang.Validate

class Settings {

    // property keys
    private static final def CRS_URL = "crs.url"

    // settings
    static final def crsUrl

    static {
        def properties = new Properties();

        def inputStream = Settings.class.classLoader.getResourceAsStream("jbehave.properties");
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

    private static def getString(Properties properties, String property) {
        def value = properties.getProperty(property);

        if (value == null) {
            throw new IllegalStateException(String.format("Property %s is not found", property));
        }

        return value;
    }
}
