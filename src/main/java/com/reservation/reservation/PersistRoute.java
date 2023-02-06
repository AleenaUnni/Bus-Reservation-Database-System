package com.reservation.reservation;

import Initializer.ConnectionHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/persistroute")
public class PersistRoute extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String[] parameters = request.getReader().readLine().split("&");
            Long bus_id = Long.parseLong(parameters[0].split("=")[1]);
            Long from_station = Long.parseLong(parameters[1].split("=")[1]);
            Long to_station = Long.parseLong(parameters[2].split("=")[1]);

            ConnectionHandler.createRoute(bus_id,from_station,to_station);
        } catch (Exception e) {
            response.setStatus(400,"Error has occured during route creation");
        }
    }
}
