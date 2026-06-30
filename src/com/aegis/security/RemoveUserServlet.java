package com.aegis.security;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/RemoveUserServlet")
public class RemoveUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // make sure only ADMIN can remove users
        if (session == null ||
            session.getAttribute("username") == null ||
            !"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendRedirect("login.jsp");
            return;
        }

        String admin = (String) session.getAttribute("username");
        String usernameToRemove = request.getParameter("username");

        if (usernameToRemove == null || usernameToRemove.trim().isEmpty()) {
            response.getWriter().println("<h3 style='color:red;'>Username cannot be empty.</h3>");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                response.getWriter().println("<h3 style='color:red;'>Database connection failed.</h3>");
                return;
            }

            // delete the user
            String sql = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, usernameToRemove);
                int rows = ps.executeUpdate();

                if (rows > 0) {
                    // log the action
                    String logSql = "INSERT INTO security_logs (username, action) VALUES (?, ?)";
                    try (PreparedStatement logPs = conn.prepareStatement(logSql)) {
                        logPs.setString(1, admin);
                        logPs.setString(2, "Removed user: " + usernameToRemove);
                        logPs.executeUpdate();
                    }

                    response.sendRedirect("removeUser.jsp?msg=User removed successfully");

                } else {
                    response.getWriter().println("<h3 style='color:red;'>User not found.</h3>");
                }
            }

        } catch (Exception e) {
            System.out.println("Error removing user: " + e.getMessage());
            response.getWriter().println("<h3 style='color:red;'>Error removing user: " + e.getMessage() + "</h3>");
        }
    }
}
