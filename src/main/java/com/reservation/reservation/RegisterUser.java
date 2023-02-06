package com.reservation.reservation;

import Initializer.ConnectionHandler;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/registeruser")
public class RegisterUser extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {


            ConnectionHandler.createUser(request.getParameter("email"),1,1,request.getParameter("psw"),request.getParameter("email"));




            request.getRequestDispatcher("booking.jsp").forward(request,response);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
