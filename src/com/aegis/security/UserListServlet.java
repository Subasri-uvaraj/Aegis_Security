package com.aegis.security;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/UserListServlet")
public class UserListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Ensure only logged-in users can access this page
        String sender = (session != null) ? (String) session.getAttribute("username") : null;
        if (sender == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        List<String> userList = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                response.getWriter().println("<h3 style='color:red;'>Database connection failed.</h3>");
                return;
            }

            // Fetch all usernames from the users table
            String sql = "SELECT username FROM users";
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {
                    userList.add(rs.getString("username"));
                }
            }

            // Attach list and sender name to the request
            request.setAttribute("userList", userList);
            request.setAttribute("sender", sender);

            // Forward to message sending page
            RequestDispatcher rd = request.getRequestDispatcher("sendMessage.jsp");
            rd.forward(request, response);

        } catch (Exception e) {
            System.out.println("Error loading user list: " + e.getMessage());
            response.getWriter().println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
