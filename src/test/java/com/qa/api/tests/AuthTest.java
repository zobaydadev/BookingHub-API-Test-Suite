package com.qa.api.tests;

import com.qa.api.client.BookingClient;
import com.qa.api.config.ApiConfig;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class AuthTest {

    private final BookingClient client = new BookingClient();

    @Test(description = "Valid credentials return a token")
    public void validCredentialsReturnToken() {
        client.createToken(ApiConfig.USERNAME, ApiConfig.PASSWORD)
                .then().statusCode(200)
                .body("token", notNullValue());
    }

    @Test(description = "Invalid credentials are rejected without a token")
    public void invalidCredentialsAreRejected() {
        Response response = client.createToken(ApiConfig.USERNAME, "not-the-password");
        response.then().statusCode(200).body("reason", equalTo("Bad credentials"));
    }
}
