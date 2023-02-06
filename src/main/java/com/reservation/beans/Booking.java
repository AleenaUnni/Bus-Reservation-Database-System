package com.reservation.beans;

import java.sql.Date;

public class Booking {
    public Long getBooking_id() {
        return booking_id;
    }

    public void setBooking_id(Long booking_id) {
        this.booking_id = booking_id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Date getTravelDate() {
        return travelDate;
    }

    public void setTravelDate(Date travelDate) {
        this.travelDate = travelDate;
    }

    Long booking_id;
    Long bus_id;
    Long userId;
    Date travelDate;

    public Long getBus_id() {
        return bus_id;
    }

    public void setBus_id(Long bus_id) {
        this.bus_id = bus_id;
    }
}
