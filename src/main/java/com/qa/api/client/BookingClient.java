package com.qa.api.client;

import com.qa.api.config.ApiConfig;
import com.qa.api.models.Booking;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

/** Thin wrapper around the booking endpoints so tests read as intent, not HTTP plumbing. */
public class BookingClient {

    private final RequestSpecification spec = new RequestSpecBuilder()
            .setBaseUri(ApiConfig.BASE_URL)
            .setContentType(ContentType.JSON)
            .addHeader("Accept", "application/json")
            .log(LogDetail.URI)
            .build();

    public Response createToken(String username, String password) {
        return given().spec(spec)
                .body(Map.of("username", username, "password", password))
                .post("/auth");
    }

    public String validToken() {
        return createToken(ApiConfig.USERNAME, ApiConfig.PASSWORD).jsonPath().getString("token");
    }

    public Response listIds() {
        return given().spec(spec).get("/booking");
    }

    public Response listIdsByLastName(String lastname) {
        return given().spec(spec).queryParam("lastname", lastname).get("/booking");
    }

    public Response create(Booking booking) {
        return given().spec(spec).body(booking).post("/booking");
    }

    public Response get(int id) {
        return given().spec(spec).get("/booking/{id}", id);
    }

    public Response update(int id, Booking booking, String token) {
        return given().spec(spec).cookie("token", token).body(booking).put("/booking/{id}", id);
    }

    public Response partialUpdate(int id, Map<String, Object> fields, String token) {
        return given().spec(spec).cookie("token", token).body(fields).patch("/booking/{id}", id);
    }

    public Response delete(int id, String token) {
        return given().spec(spec).cookie("token", token).delete("/booking/{id}", id);
    }

    public Response deleteWithoutToken(int id) {
        return given().spec(spec).delete("/booking/{id}", id);
    }
}
