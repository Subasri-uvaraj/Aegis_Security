package com.aegis.security;

import java.sql.*;
import java.util.Scanner;

public class Login {

    public static void main(String[] args) {
        System.out.println("=== AEGIS GATEWAY SECURITY SYSTEM ===");
        Scanner scan = new Scanner(System.in);

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                System.out.println("Database connection failed. Please check settings.");
                return;
            }

            System.out.print("Enter Username: ");
            String username = scan.nextLine().trim();

            System.out.print("Enter Password: ");
            String password = scan.nextLine().trim();

            // encrypt password before verifying
            String encPwd = PasswordEncryption.encrypt(password);

            String sql = "SELECT role FROM users WHERE username=? AND password=?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, username);
                stmt.setString(2, encPwd);

                try (ResultSet rs = stmt.executeQuery()) {

                    SecurityLogServices logService = new SecurityLogServices();

                    if (rs.next()) {
                        String role = rs.getString("role");
                        System.out.println("\nLogin successful!");
                        System.out.println("Role: " + role);
                        logService.addLog(username, "Login successful");

                        // redirect based on user role
                        if ("ADMIN".equalsIgnoreCase(role)) {
                            System.out.println("Welcome Admin!");
                            AdminDashboard.main(null);
                        } else if ("USER".equalsIgnoreCase(role)) {
                            System.out.println("Welcome User!");
                            UserDashBoard.main(null);
                        } else {
                            System.out.println("Access Denied! Unknown Role.");
                        }

                    } else {
                        System.out.println("\nInvalid username or password.");
                        logService.addLog(username, "Failed login attempt");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error during login: " + e.getMessage());
        } finally {
            scan.close();
        }
    }
}
