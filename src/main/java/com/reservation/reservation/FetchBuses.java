package com.reservation.reservation;

import Initializer.ConnectionHandler;
import com.reservation.beans.Bus;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/fetchbuses")
public class FetchBuses extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONArray busesJA =  new JSONArray();
        for (Bus bus : ConnectionHandler.getBuses()) {
            JSONObject busJO = new JSONObject();
            busJO.put("id",bus.getId());
            busJO.put("name",bus.getName());
            busJO.put("capacity",bus.getCapacity());
            busJO.put("cost",bus.getCost());
            busesJA.put(busJO);
        }
        out.append(busesJA.toString());
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
