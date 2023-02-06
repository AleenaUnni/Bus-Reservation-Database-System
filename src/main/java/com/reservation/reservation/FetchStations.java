package com.reservation.reservation;

import Initializer.ConnectionHandler;
import com.reservation.beans.Station;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/fetchstations")
public class FetchStations extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        JSONArray stationsJA =  new JSONArray();
        for (Station station : ConnectionHandler.getStations()) {
            JSONObject stationJO = new JSONObject();
            stationJO.put("id",station.getId());
            stationJO.put("name",station.getName());
            stationsJA.put(stationJO);
        }
        out.append(stationsJA.toString());
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
