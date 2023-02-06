package com.reservation.beans;

public class Route {

    Long route_id;
    Long bus_id;
    Long from_station;
    Long to_station;

    public Long getRoute_id() {
        return route_id;
    }

    public void setRoute_id(Long route_id) {
        this.route_id = route_id;
    }

    public Long getBus_id() {
        return bus_id;
    }

    public void setBus_id(Long bus_id) {
        this.bus_id = bus_id;
    }

    public Long getFrom_station() {
        return from_station;
    }

    public void setFrom_station(Long from_station) {
        this.from_station = from_station;
    }

    public Long getTo_station() {
        return to_station;
    }

    public void setTo_station(Long to_station) {
        this.to_station = to_station;
    }
}
