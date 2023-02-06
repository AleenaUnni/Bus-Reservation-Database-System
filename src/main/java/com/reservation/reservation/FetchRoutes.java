package com.reservation.reservation;

import Initializer.ConnectionHandler;
import com.reservation.beans.Bus;
import com.reservation.beans.Route;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/fetchroutes")
public class FetchRoutes extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray routesJA = new JSONArray();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        if (request.getParameter("search") == null) {
            for (Route route : ConnectionHandler.getRoutes()) {
                JSONObject routesJO = new JSONObject();
                routesJO.put("route_id", route.getRoute_id());
                routesJO.put("bus_id", route.getBus_id());
                routesJO.put("from_station", route.getFrom_station());
                routesJO.put("to_station", route.getTo_station());
                routesJA.put(routesJO);
            }
        } else {
            String[] parameters = request.getQueryString().split("&");
            Long fromStation = Long.parseLong((parameters[1].split("=")[1]));
            Long toStation = Long.parseLong((parameters[2].split("=")[1]));
            Date travel_date = Date.valueOf((parameters[3].split("=")[1]));

            List<String> listOfBusesRunningInThatRoute = new ArrayList<>();
            for (Route route : ConnectionHandler.getRoutes(fromStation, toStation)) {
                listOfBusesRunningInThatRoute.add(route.getBus_id().toString());
            }

            /*need to check if bus has seats available on that day*/
            if (!listOfBusesRunningInThatRoute.isEmpty()) {
                List<Bus> busList = ConnectionHandler.getBusesWithSeatingCapacity(listOfBusesRunningInThatRoute, travel_date);
                for (Bus bus : busList) {
                    JSONObject busJO = new JSONObject();
                    busJO.put("id", bus.getId());
                    busJO.put("name", bus.getName());
//                    busJO.put("capacity",bus.getCapacity());
                    busJO.put("cost", bus.getCost());
                    routesJA.put(busJO);
                }
            }

        }
        out.append(routesJA.toString());
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray routesJA = new JSONArray();
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();

        JSONObject data = new JSONObject(request.getReader().readLine());
        Long fromStation = Long.valueOf(data.get("source").toString());
        Long toStation = Long.valueOf(data.get("destination").toString());
        Date travel_date = Date.valueOf((data.get("date").toString()));

        List<String> listOfBusesRunningInThatRoute = new ArrayList<>();
        for (Route route : ConnectionHandler.getRoutes(fromStation, toStation)) {
            listOfBusesRunningInThatRoute.add(route.getBus_id().toString());
        }

        /*need to check if bus has seats available on that day*/
        if (!listOfBusesRunningInThatRoute.isEmpty()) {
            List<Bus> busList = ConnectionHandler.getBusesWithSeatingCapacity(listOfBusesRunningInThatRoute, travel_date);
            for (Bus bus : busList) {
                JSONObject busJO = new JSONObject();
                busJO.put("id", bus.getId());
                busJO.put("name", bus.getName());
//                    busJO.put("capacity",bus.getCapacity());
                busJO.put("cost", bus.getCost());
                routesJA.put(busJO);
            }
        }

        out.append(routesJA.toString());
        out.close();
    }
}
