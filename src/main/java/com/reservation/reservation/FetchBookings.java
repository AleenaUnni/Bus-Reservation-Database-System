package com.reservation.reservation;

import Initializer.ConnectionHandler;
import com.reservation.beans.Booking;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "FetchBookings", value = "/FetchBookings")
public class FetchBookings extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long userId = Long.valueOf(request.getParameter("user_id"));

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONArray busesJA =  new JSONArray();
        for (Booking booking : ConnectionHandler.getBookings(userId)) {
            JSONObject busJO = new JSONObject();
            busJO.put("booking_id",booking.getBooking_id());
            busJO.put("bus_id",booking.getBus_id());
            busJO.put("date",booking.getTravelDate());
            busesJA.put(busJO);
        }
        out.append(busesJA.toString());
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
