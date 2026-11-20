package com.qa.api.tests;

import com.qa.api.client.BookingClient;
import com.qa.api.models.Booking;
import com.qa.api.models.BookingDates;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;

/** One booking is carried through create, read, update, patch and delete. Methods run in order via dependsOnMethods. */
public class BookingCrudTest {

    private final BookingClient client = new BookingClient();
    private String token;
    private int bookingId;

    @BeforeClass
    public void authenticate() {
        token = client.validToken();
        Assert.assertNotNull(token, "Could not obtain an auth token");
    }

    @Test(description = "Create a booking")
    public void createBooking() {
        Booking booking = Booking.sample();
        Response response = client.create(booking);
        response.then().statusCode(200);

        bookingId = response.jsonPath().getInt("bookingid");
        Assert.assertTrue(bookingId > 0, "Expected a booking id");
        Assert.assertEquals(response.jsonPath().getString("booking.firstname"), booking.firstname);
        Assert.assertEquals(response.jsonPath().getInt("booking.totalprice"), booking.totalprice);
    }

    @Test(description = "Read the booking and validate its JSON schema", dependsOnMethods = "createBooking")
    public void getBooking() {
        client.get(bookingId)
                .then().statusCode(200)
                .body(JsonSchemaValidator.matchesJsonSchemaInClasspath("schemas/booking-schema.json"))
                .body("lastname", equalTo("Brown"));
    }

    @Test(description = "The booking id appears in a filtered list", dependsOnMethods = "createBooking")
    public void bookingAppearsInSearch() {
        Response response = client.listIdsByLastName("Brown");
        response.then().statusCode(200);
        Assert.assertTrue(response.jsonPath().getList("bookingid").contains(bookingId),
                "Booking " + bookingId + " not found in search results");
    }

    @Test(description = "Full update replaces the booking", dependsOnMethods = "getBooking")
    public void updateBooking() {
        Booking updated = new Booking("Sally", "Brown", 222, false,
                new BookingDates("2026-02-01", "2026-02-07"), "Lunch");
        client.update(bookingId, updated, token)
                .then().statusCode(200)
                .body("firstname", equalTo("Sally"))
                .body("totalprice", equalTo(222))
                .body("depositpaid", equalTo(false));
    }

    @Test(description = "Partial update changes only the sent fields", dependsOnMethods = "updateBooking")
    public void partialUpdateBooking() {
        client.partialUpdate(bookingId, Map.of("firstname", "James"), token)
                .then().statusCode(200)
                .body("firstname", equalTo("James"))
                .body("lastname", equalTo("Brown"));
    }

    @Test(description = "Delete the booking", dependsOnMethods = "partialUpdateBooking")
    public void deleteBooking() {
        client.delete(bookingId, token).then().statusCode(201);
    }

    @Test(description = "A deleted booking is gone", dependsOnMethods = "deleteBooking")
    public void deletedBookingReturns404() {
        client.get(bookingId).then().statusCode(404);
    }
}
