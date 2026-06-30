package com.aegis.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Scanner;

public class UserDashBoard {

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Enter your username: ");
        String username = scan.nextLine().trim();

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                System.out.println("Database connection failed. Please check your settings.");
                scan.close();
                return;
            }

            if (!userExists(conn, username)) {
                System.out.println("User not found!");
                scan.close();
                return;
            }

            System.out.println("\n=== USER DASHBOARD ===");

            int choice;
            do {
                System.out.println("\n1. View Profile");
                System.out.println("2. Change Password");
                System.out.println("3. Logout");
                System.out.print("Enter your choice: ");

                while (!scan.hasNextInt()) {
                    System.out.print("Please enter a valid number: ");
                    scan.next();
                }

                choice = scan.nextInt();
                scan.nextLine(); // consume newline

                switch (choice) {
                    case 1:
                        viewProfile(conn, username);
                        break;

                    case 2:
                        changePassword(conn, scan, username);
                        break;

                    case 3:
                        System.out.println("Logging out...");
                        break;

                    default:
                        System.out.println("Invalid choice! Try again.");
                        break;
                }

            } while (choice != 3);

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scan.close();
            System.out.println("Session ended.");
        }
    }

    // Check if the user exists in the database
    private static boolean userExists(Connection conn, String username) throws Exception {
        String query = "SELECT COUNT(*) FROM users WHERE username = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();

        rs.next();
        boolean exists = rs.getInt(1) > 0;

        rs.close();
        pstmt.close();
        return exists;
    }

    // Display user profile information
    private static void viewProfile(Connection conn, String username) throws Exception {
        String query = "SELECT username, email, role FROM users WHERE username = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, username);
        ResultSet rs = pstmt.executeQuery();

        if (rs.next()) {
            System.out.println("\n--- User Profile ---");
            System.out.println("Username: " + rs.getString("username"));
            System.out.println("Email: " + rs.getString("email"));
            System.out.println("Role: " + rs.getString("role"));
        } else {
            System.out.println("User not found.");
        }

        rs.close();
        pstmt.close();
    }

    // Allow user to change password (with encryption)
    private static void changePassword(Connection conn, Scanner scan, String username) {
        System.out.print("Enter new password: ");
        String newPassword = scan.nextLine().trim();

        if (newPassword.isEmpty()) {
            System.out.println("Password cannot be empty.");
            return;
        }

        try {
            String encryptedPassword = PasswordEncryption.encrypt(newPassword);
            String updateQuery = "UPDATE users SET password = ? WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(updateQuery);
            pstmt.setString(1, encryptedPassword);
            pstmt.setString(2, username);

            int rows = pstmt.executeUpdate();
            if (rows > 0) {
                System.out.println("Password updated successfully!");
                logActivity(conn, username, "Changed account password");
            } else {
                System.out.println("Failed to update password.");
            }

            pstmt.close();
        } catch (Exception e) {
            System.out.println("Error changing password: " + e.getMessage());
        }
    }

    // Log user actions in the security logs
    private static void logActivity(Connection conn, String username, String action) {
        String logQuery = "INSERT INTO security_logs (username, action) VALUES (?, ?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(logQuery);
            pstmt.setString(1, username);
            pstmt.setString(2, action);
            pstmt.executeUpdate();
            pstmt.close();
        } catch (Exception e) {
            System.out.println("Failed to log activity: " + e.getMessage());
        }
    }
}
