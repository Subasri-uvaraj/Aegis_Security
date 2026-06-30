package com.aegis.security;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ForgotPasswordServlet")
public class ForgotPasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String username = request.getParameter("username");
        String email = request.getParameter("email");

        // Quick validation
        if (username == null || email == null || username.isEmpty() || email.isEmpty()) {
            out.println("<h3 style='color:red;'>Please enter both username and email.</h3>");
            out.println("<a href='forgotPassword.jsp'>Back</a>");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                out.println("<h3 style='color:red;'>Database connection failed. Please try again later.</h3>");
                return;
            }

            String sql = "SELECT username FROM users WHERE username=? AND email=?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, email);

                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        // Generate a random 6-digit OTP
                        int otp = new Random().nextInt(900000) + 100000;
                        System.out.println("Generated OTP for " + username + ": " + otp);

                        // Store OTP and username in session
                        HttpSession session = request.getSession();
                        session.setAttribute("otp", otp);
                        session.setAttribute("username", username);

                        // Redirect to reset password page
                        response.sendRedirect("resetPassword.jsp");
                    } else {
                        out.println("<h3 style='color:red;'>Invalid username or email.</h3>");
                        out.println("<a href='forgotPassword.jsp'>Try Again</a>");
                    }
                }
            }

        } catch (Exception e) {
            out.println("<h3 style='color:red;'>Something went wrong: " + e.getMessage() + "</h3>");
            System.out.println("Error in ForgotPasswordServlet: " + e.getMessage());
        }
    }
}
