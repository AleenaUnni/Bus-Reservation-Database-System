package com.reservation.reservation;

import Initializer.ConnectionHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/persistbus")
public class PersistBus extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String[] parameters = request.getReader().readLine().split("&");
            String bus_name = parameters[0].split("=")[1];
            int bus_capacity = Integer.parseInt(parameters[1].split("=")[1]);
            int bus_cost = Integer.parseInt(parameters[2].split("=")[1]);

            ConnectionHandler.createBus(bus_name,bus_capacity,bus_cost);
        } catch (Exception e) {
            response.setStatus(400,"Error has occured during bus creation");
        }
    }
}
