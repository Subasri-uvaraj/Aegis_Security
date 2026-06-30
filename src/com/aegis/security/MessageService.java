package com.aegis.security;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageService {

    // Store an encrypted message (one-to-one)
    public void sendMessage(String sender, String receiver, String message) {
        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                System.out.println("Database connection failed. Cannot send message.");
                return;
            }

            String encryptedMsg = PasswordEncryption.encrypt(message);

            String sql = "INSERT INTO messages (sender, receiver, encrypted_message, plaintext, timestamp, status) " +
                         "VALUES (?, ?, ?, ?, NOW(), ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, sender);
                ps.setString(2, receiver);
                ps.setString(3, encryptedMsg);
                ps.setString(4, message);
                ps.setString(5, "Sent");
                ps.executeUpdate();
            }

            // Log user activity
            String logSql = "INSERT INTO security_logs (username, action) VALUES (?, ?)";
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, sender);
                logStmt.setString(2, "Sent message to " + receiver);
                logStmt.executeUpdate();
            }

            System.out.println("Message saved successfully for: " + receiver);

        } catch (Exception e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    // Store a group message (visible to everyone)
    public void sendGroupMessage(String sender, String message) {
        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                System.out.println("Database connection failed. Cannot send group message.");
                return;
            }

            String encryptedMsg = PasswordEncryption.encrypt(message);

            String sql = "INSERT INTO messages (sender, receiver, encrypted_message, plaintext, timestamp, status) " +
                         "VALUES (?, ?, ?, ?, NOW(), ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, sender);
                ps.setString(2, "GROUP");
                ps.setString(3, encryptedMsg);
                ps.setString(4, message);
                ps.setString(5, "Sent");
                ps.executeUpdate();
            }

            String logSql = "INSERT INTO security_logs (username, action) VALUES (?, ?)";
            try (PreparedStatement logStmt = conn.prepareStatement(logSql)) {
                logStmt.setString(1, sender);
                logStmt.setString(2, "Sent group message");
                logStmt.executeUpdate();
            }

            System.out.println("Group message saved successfully.");

        } catch (Exception e) {
            System.out.println("Error sending group message: " + e.getMessage());
        }
    }

    // Fetch messages (personal + group)
    public List<Message> fetchMessages(String username) {
        List<Message> messages = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                System.out.println("Database connection failed. Cannot fetch messages.");
                return messages;
            }

            String sql = "SELECT * FROM messages WHERE receiver=? OR receiver='GROUP' OR sender=? ORDER BY timestamp DESC";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, username);
                ps.setString(2, username);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        String decryptedMsg = PasswordEncryption.decrypt(rs.getString("encrypted_message"));
                        messages.add(new Message(
                                rs.getInt("id"),
                                rs.getString("sender"),
                                rs.getString("receiver"),
                                rs.getString("encrypted_message"),
                                decryptedMsg,
                                rs.getTimestamp("timestamp"),
                                rs.getString("status")
                        ));
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error fetching messages: " + e.getMessage());
        }

        return messages;
    }
}
