package com.aegis.security;

import java.sql.*;
import java.util.Scanner;

public class OfficerDashboard {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/aegis_security", "root", "root")) {

            System.out.println("=== OFFICER DASHBOARD ===");

            int choice;
            do {
                System.out.println("\n1. Send Secure Message");
                System.out.println("2. View Received Messages");
                System.out.println("3. Logout");
                System.out.print("Enter your choice: ");

                while (!scan.hasNextInt()) {
                    System.out.print("Please enter a valid number: ");
                    scan.next();
                }

                choice = scan.nextInt();
                scan.nextLine(); // consume newline

                switch (choice) {
                    case 1: sendMessage(conn, scan);
                    case 2 :viewMessages(conn, scan);
                    case 3 :System.out.println("Logging out...");
                    default :System.out.println("Invalid choice! Try again.");
                }

            } while (choice != 3);

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }

        scan.close();
        System.out.println("Session ended.");
    }

    private static void sendMessage(Connection conn, Scanner scan) {
        try {
            System.out.print("Enter your username (sender): ");
            String sender = scan.nextLine().trim();

            System.out.print("Enter receiver username: ");
            String receiver = scan.nextLine().trim();

            System.out.print("Enter message to encrypt and send: ");
            String message = scan.nextLine().trim();

            if (sender.isEmpty() || receiver.isEmpty() || message.isEmpty()) {
                System.out.println("All fields are required.");
                return;
            }

           
            String encryptedMsg = PasswordEncryption.encrypt(message);

            String sql = "INSERT INTO messages (sender, receiver, encrypted_message, plaintext, timestamp, status) " +
                         "VALUES (?, ?, ?, ?, NOW(), 'Sent')";
            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, sender);
                pstmt.setString(2, receiver);
                pstmt.setString(3, encryptedMsg);
                pstmt.setString(4, message);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("\nSecure Message Sent Successfully!");
                    System.out.println("Encrypted Message: " + encryptedMsg);
                } else {
                    System.out.println("Failed to send message.");
                }
            }

        } catch (Exception e) {
            System.out.println("Error while sending message: " + e.getMessage());
        }
    }

    private static void viewMessages(Connection conn, Scanner scan) {
        try {
            System.out.print("Enter your username to view messages: ");
            String receiver = scan.nextLine().trim();

            String sql = "SELECT sender, encrypted_message, timestamp FROM messages " +
                         "WHERE receiver=? ORDER BY timestamp DESC";

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                pstmt.setString(1, receiver);

                try (ResultSet rs = pstmt.executeQuery()) {
                    System.out.println("\n--- Received Messages ---");
                    boolean hasMessages = false;

                    while (rs.next()) {
                        hasMessages = true;
                        String sender = rs.getString("sender");
                        String encrypted = rs.getString("encrypted_message");
                        String decrypted;
                        try {

                            decrypted = PasswordEncryption.decrypt(encrypted);
                        } catch (Exception e) {
                            decrypted = "[Error decrypting message]";
                        }

                        Timestamp time = rs.getTimestamp("timestamp");
                        System.out.println("\nFrom: " + sender);
                        System.out.println("Encrypted: " + encrypted);
                        System.out.println("Decrypted: " + decrypted);
                        System.out.println("Time: " + time);
                    }

                    if (!hasMessages) {
                        System.out.println("No messages found for this user.");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error fetching messages: " + e.getMessage());
        }
    }
}
