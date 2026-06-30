package com.aegis.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ResetPasswordServlet")
public class ResetPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // collect form inputs
        String username = request.getParameter("username");
        String newPassword = request.getParameter("newPassword");
        String confirmPassword = request.getParameter("confirmPassword");

        // quick input validation
        if (username == null || newPassword == null || confirmPassword == null ||
                username.isBlank() || newPassword.isBlank() || confirmPassword.isBlank()) {
            out.println("<h3 style='color:red;'>All fields are required.</h3>");
            out.println("<a href='resetPassword.jsp'>Back</a>");
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            out.println("<h3 style='color:red;'>Passwords do not match. Please try again.</h3>");
            out.println("<a href='resetPassword.jsp'>Back</a>");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                out.println("<h3 style='color:red;'>Couldn’t connect to the database. Try again later.</h3>");
                return;
            }

            // encrypt password before updating
            String encryptedPwd = PasswordEncryption.encrypt(newPassword);

            String updateSql = "UPDATE users SET password=? WHERE username=?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setString(1, encryptedPwd);
                ps.setString(2, username);

                int updated = ps.executeUpdate();

                if (updated > 0) {
                    // log the reset action
                    String logSql = "INSERT INTO security_logs (username, action) VALUES (?, ?)";
                    try (PreparedStatement logPs = conn.prepareStatement(logSql)) {
                        logPs.setString(1, username);
                        logPs.setString(2, "Password reset successfully");
                        logPs.executeUpdate();
                    }

                    out.println("<h3 style='color:green;'>Password reset successfully!</h3>");
                    out.println("<a href='login.jsp'>Login Now</a>");
                } else {
                    out.println("<h3 style='color:red;'>No account found with that username.</h3>");
                    out.println("<a href='resetPassword.jsp'>Back</a>");
                }
            }

        } catch (Exception e) {
            System.out.println("Error during password reset: " + e.getMessage());
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
