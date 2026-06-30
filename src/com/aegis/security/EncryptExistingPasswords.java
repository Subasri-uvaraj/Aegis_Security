package com.aegis.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class EncryptExistingPasswords {

    public static void main(String[] args) {
        System.out.println("=== Password Encryption Utility ===");

        int updatedCount = 0;

        try (Connection conn = DBConnection.getConnection()) {

            if (conn == null) {
                System.out.println("Database connection failed. Please check your settings.");
                return;
            }

            String selectSQL = "SELECT username, password FROM users";
            try (PreparedStatement selectStmt = conn.prepareStatement(selectSQL);
                 ResultSet rs = selectStmt.executeQuery()) {

                while (rs.next()) {
                    String username = rs.getString("username");
                    String currentPwd = rs.getString("password");

                    // skip null or already encrypted passwords (simple check)
                    if (currentPwd == null || !currentPwd.matches("[a-zA-Z0-9]+")) {
                        System.out.println("Skipping " + username + " (possibly already encrypted)");
                        continue;
                    }

                    try {
                        String newEnc = PasswordEncryption.encrypt(currentPwd);

                        // update user with encrypted password
                        String updateSQL = "UPDATE users SET password = ? WHERE username = ?";
                        try (PreparedStatement updateStmt = conn.prepareStatement(updateSQL)) {
                            updateStmt.setString(1, newEnc);
                            updateStmt.setString(2, username);
                            updateStmt.executeUpdate();
                            updatedCount++;
                            System.out.println("Encrypted password for user: " + username);
                        }

                    } catch (Exception encErr) {
                        System.out.println("Failed to update " + username + ": " + encErr.getMessage());
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error during password encryption: " + e.getMessage());
        }

        System.out.println("\nProcess completed. Total users updated: " + updatedCount);
        System.out.println("====================================");
    }
}
