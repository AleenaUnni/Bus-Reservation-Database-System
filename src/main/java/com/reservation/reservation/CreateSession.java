package com.reservation.reservation;

import Initializer.ConnectionHandler;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;

@WebServlet("/createsession")
public class CreateSession extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("login.jsp").forward(request,response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long userId = ConnectionHandler.loginUser(request.getParameter("email"), request.getParameter("psw"));
            if (userId != null) {
                HashMap<Long,String> sessionMap = (HashMap<Long, String>) request.getServletContext().getAttribute("sessionMap");
                sessionMap.put(userId,request.getSession().getId());
                if(request.getParameter("email").equals("admin")){
                    request.getRequestDispatcher("admin.jsp").forward(request,response);
                }else {
//                    BookingServlet bookingServlet = new BookingServlet();
//                    bookingServlet.doGet(request,response);
//                    response.sendRedirect(request.getContextPath()+"/booking");
                    request.setAttribute("user_id",userId);
                    request.getRequestDispatcher("booking.jsp").forward(request,response);
                }
            }else{
                request.getRequestDispatcher("users.jsp").forward(request,response);
            }
        } catch (SQLException e) {
            request.getRequestDispatcher("users.jsp").forward(request,response);
        }
    }
}
