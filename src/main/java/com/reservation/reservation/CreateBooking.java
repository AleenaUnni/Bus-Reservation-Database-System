package com.reservation.reservation;

import Initializer.ConnectionHandler;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.sql.SQLException;

@WebServlet("/persistbooking")
public class CreateBooking extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        try {
            JSONObject data = new JSONObject(request.getReader().readLine());
            Long userId = Long.valueOf(data.get("user_id").toString());
            Long busId = Long.valueOf(data.get("bus_id").toString());
            Date travel_date = Date.valueOf((data.get("date").toString()));

            ConnectionHandler.createBooking(userId,busId,travel_date);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        out.write("Successfully Booked");
        out.close();
    }
}
