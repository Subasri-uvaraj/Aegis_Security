package com.aegis.security;
import java.sql.Connection;
import java.sql.PreparedStatement;
public class SecurityLogServices {
    public static void addLog(String username, String action) {
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "INSERT INTO security_logs (username, action) VALUES (?, ?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, action);
            stmt.executeUpdate();
            System.out.println("Log Added: [" + username + "] - " + action);
        } catch (Exception e) {
            System.out.println("Error inserting log: " + e.getMessage());
        }
    }
}
