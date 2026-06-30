package com.aegis.security;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/GroupChatServlet")
public class GroupChatServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Validate session
        if (session == null || session.getAttribute("username") == null) {
            response.sendRedirect("login.jsp");
            return;
        }

        String sender = (String) session.getAttribute("username");
        String message = request.getParameter("message");

        if (message == null || message.trim().isEmpty()) {
            response.sendRedirect("groupChat.jsp");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            if (conn == null) {
                System.out.println("Database connection failed.");
                response.sendRedirect("groupChat.jsp");
                return;
            }

            // Encrypt the message before storing
            String encrypted = PasswordEncryption.encrypt(message);

            // Insert a group message (receiver='ALL')
            String sql = "INSERT INTO messages (sender, receiver, encrypted_message, plaintext, timestamp, status) " +
                         "VALUES (?, 'ALL', ?, ?, NOW(), 'SENT')";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, sender);
                ps.setString(2, encrypted);
                ps.setString(3, message);
                ps.executeUpdate();
            }

            // Log message activity
            String logSql = "INSERT INTO security_logs (username, action) VALUES (?, ?)";
            try (PreparedStatement logPs = conn.prepareStatement(logSql)) {
                logPs.setString(1, sender);
                logPs.setString(2, "Sent a group message");
                logPs.executeUpdate();
            }

            response.sendRedirect("groupChat.jsp");

        } catch (Exception e) {
            System.out.println("Error in group chat: " + e.getMessage());
            response.sendRedirect("groupChat.jsp");
        }
    }
}
