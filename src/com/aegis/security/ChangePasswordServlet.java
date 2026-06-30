package com.aegis.security;

import java.io.IOException;
import java.sql.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ChangePasswordServlet")
public class ChangePasswordServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws ServletException, IOException {

        HttpSession session = req.getSession(false);

        // Ensure user is logged in
        if (session == null || session.getAttribute("username") == null) {
            res.sendRedirect("login.jsp");
            return;
        }

        String username = (String) session.getAttribute("username");
        String oldPwd = req.getParameter("oldPassword");
        String newPwd = req.getParameter("newPassword");
        String confirmPwd = req.getParameter("confirmPassword");

        // Check new password validity
        if (newPwd == null || !newPwd.equals(confirmPwd)) {
            req.setAttribute("errorMessage", "New passwords do not match.");
            req.getRequestDispatcher("changePassword.jsp").forward(req, res);
            return;
        }

        if (oldPwd.equals(newPwd)) {
            req.setAttribute("errorMessage", "New password cannot be the same as the old one.");
            req.getRequestDispatcher("changePassword.jsp").forward(req, res);
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                req.setAttribute("errorMessage", "Database connection failed. Please try again later.");
                req.getRequestDispatcher("changePassword.jsp").forward(req, res);
                return;
            }

            // Encrypt both old and new passwords
            String encOld = PasswordEncryption.encrypt(oldPwd);
            String encNew = PasswordEncryption.encrypt(newPwd);

            // Verify old password (either encrypted or plain)
            String checkSql = "SELECT username FROM users WHERE username=? AND (password=? OR plain_password=?)";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, username);
                checkStmt.setString(2, encOld);
                checkStmt.setString(3, oldPwd);
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (!rs.next()) {
                        req.setAttribute("errorMessage", "Your current password is incorrect.");
                        req.getRequestDispatcher("changePassword.jsp").forward(req, res);
                        return;
                    }
                }
            }

            // Update both encrypted and plain passwords
            String updateSql = "UPDATE users SET password=?, plain_password=? WHERE username=?";
            try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
                updateStmt.setString(1, encNew);
                updateStmt.setString(2, newPwd);
                updateStmt.setString(3, username);
                int updated = updateStmt.executeUpdate();

                if (updated == 0) {
                    req.setAttribute("errorMessage", "Password update failed. Try again.");
                    req.getRequestDispatcher("changePassword.jsp").forward(req, res);
                    return;
                }
            }

            // Log password change
            String logSql = "INSERT INTO security_logs (username, action) VALUES (?, ?)";
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, username);
                logStmt.setString(2, "Password changed successfully");
                logStmt.executeUpdate();
            }

            res.sendRedirect("password_success.jsp");

        } catch (Exception e) {
            req.setAttribute("errorMessage", "Error while updating password: " + e.getMessage());
            req.getRequestDispatcher("changePassword.jsp").forward(req, res);
        }
    }
}
