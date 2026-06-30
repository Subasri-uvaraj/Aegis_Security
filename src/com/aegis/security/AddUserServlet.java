package com.aegis.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/AddUserServlet")
public class AddUserServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        HttpSession session = request.getSession(false);

        String currentRole = (session != null) ? (String) session.getAttribute("role") : null;

        // Only ADMIN users are allowed to add new accounts
        if (currentRole == null || !"ADMIN".equalsIgnoreCase(currentRole)) {
            response.sendRedirect("dashboard.jsp");
            return;
        }

        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement userStmt = conn.prepareStatement(
                     "INSERT INTO users (username, password, plain_password, email, role) VALUES (?, ?, ?, ?, ?)");
             PreparedStatement logStmt = conn.prepareStatement(
                     "INSERT INTO security_logs (username, action) VALUES (?, ?)")) {

            if (conn == null) {
                out.println("<h3 style='color:red;'>Unable to connect to the database.</h3>");
                return;
            }

            // Encrypt password before storing
            String encryptedPassword = PasswordEncryption.encrypt(password);

            userStmt.setString(1, username);
            userStmt.setString(2, encryptedPassword);
            userStmt.setString(3, password); // store plain version automatically
            userStmt.setString(4, email);
            userStmt.setString(5, role);
            userStmt.executeUpdate();

            // Log the admin action
            String adminUser = (String) session.getAttribute("username");
            String action = "Added new user: " + username + " (Role: " + role + ")";
            logStmt.setString(1, adminUser);
            logStmt.setString(2, action);
            logStmt.executeUpdate();

            out.println("<h3 style='color:green;'>User added successfully.</h3>");
            out.println("<a href='dashboard.jsp'>Back to Dashboard</a>");

        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
            System.err.println("Error in AddUserServlet: " + e.getMessage());
        }
    }
}
