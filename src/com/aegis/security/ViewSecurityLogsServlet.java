package com.aegis.security;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ViewSecurityLogsServlet")
public class ViewSecurityLogsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        // Only admins can view security logs
        if (session == null || !"ADMIN".equalsIgnoreCase((String) session.getAttribute("role"))) {
            response.sendRedirect("dashboard.jsp");
            return;
        }

        List<SecurityLog> logs = new ArrayList<>();

        // Fetch logs from DB
        String sql = "SELECT id, username, action, timestamp FROM security_logs ORDER BY timestamp DESC";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn != null ? conn.prepareStatement(sql) : null;
             ResultSet rs = (ps != null) ? ps.executeQuery() : null) {

            if (conn == null) {
                System.out.println("DB connection failed while loading security logs.");
            } else if (rs != null) {
                while (rs.next()) {
                    SecurityLog log = new SecurityLog(
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getString("action"),
                            rs.getTimestamp("timestamp")
                    );
                    logs.add(log);
                }
            }

        } catch (Exception e) {
            System.out.println("Error loading security logs: " + e.getMessage());
        }

        // Attach logs to request and forward to JSP
        request.setAttribute("logs", logs);
        RequestDispatcher rd = request.getRequestDispatcher("viewSecurityLogs.jsp");
        rd.forward(request, response);
    }
}
