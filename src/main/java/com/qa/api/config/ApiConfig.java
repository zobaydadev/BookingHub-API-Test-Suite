package com.qa.api.config;

/** Central place for the API under test. Values can be overridden with -D system properties. */
public final class ApiConfig {

    public static final String BASE_URL = System.getProperty("api.base.url", "https://restful-booker.herokuapp.com");
    public static final String USERNAME = System.getProperty("api.username", "admin");
    public static final String PASSWORD = System.getProperty("api.password", "password123");

    private ApiConfig() {
    }
}
