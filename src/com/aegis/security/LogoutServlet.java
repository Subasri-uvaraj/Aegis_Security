package com.aegis.security;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            String username = (String) session.getAttribute("username");

            // log the logout action if username is available
            if (username != null && !username.isEmpty()) {
                try (Connection conn = DBConnection.getConnection();
                     PreparedStatement ps = conn.prepareStatement(
                             "INSERT INTO security_logs (username, action) VALUES (?, ?)")) {

                    if (conn != null) {
                        ps.setString(1, username);
                        ps.setString(2, "Logout successful");
                        ps.executeUpdate();
                    }

                } catch (Exception e) {
                    System.out.println("Error logging logout action: " + e.getMessage());
                }
            }

            // destroy the user session
            session.invalidate();
        }

        // redirect user to logout confirmation page
        response.sendRedirect("logout.jsp");
    }
}
