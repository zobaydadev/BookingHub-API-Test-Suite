package com.qa.api.tests;

import com.qa.api.client.BookingClient;
import com.qa.api.models.Booking;
import org.testng.annotations.Test;

public class BookingNegativeTest {

    private final BookingClient client = new BookingClient();

    @Test(description = "Getting an id that does not exist returns 404")
    public void unknownBookingReturns404() {
        client.get(999999999).then().statusCode(404);
    }

    @Test(description = "Deleting without a token is forbidden")
    public void deleteWithoutTokenIsForbidden() {
        int id = client.create(Booking.sample()).jsonPath().getInt("bookingid");
        client.deleteWithoutToken(id).then().statusCode(403);
    }

    @Test(description = "Updating with an invalid token is forbidden")
    public void updateWithInvalidTokenIsForbidden() {
        int id = client.create(Booking.sample()).jsonPath().getInt("bookingid");
        client.update(id, Booking.sample(), "invalid-token").then().statusCode(403);
    }
}
