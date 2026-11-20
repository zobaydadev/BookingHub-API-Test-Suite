package com.qa.api.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Booking {

    public String firstname;
    public String lastname;
    public int totalprice;
    public boolean depositpaid;
    public BookingDates bookingdates;
    public String additionalneeds;

    public Booking() {
    }

    public Booking(String firstname, String lastname, int totalprice, boolean depositpaid,
                   BookingDates bookingdates, String additionalneeds) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.totalprice = totalprice;
        this.depositpaid = depositpaid;
        this.bookingdates = bookingdates;
        this.additionalneeds = additionalneeds;
    }

    public static Booking sample() {
        return new Booking("Jim", "Brown", 111, true, new BookingDates("2026-01-01", "2026-01-05"), "Breakfast");
    }
}
