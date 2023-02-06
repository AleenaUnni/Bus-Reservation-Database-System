package com.reservation.reservation;

import Initializer.ConnectionHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;

@WebServlet("/persiststation")
public class PersistStation extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String[] parameters = request.getReader().readLine().split("&");
            String stationName = parameters[0].split("=")[1];
            ConnectionHandler.createStation(stationName);
        } catch (Exception e) {
            response.setStatus(400,"Error has occured during station creation");
        }
    }
}
