package com.aegis.security;

import java.sql.*;
import java.util.Scanner;

public class ChangePassword {

    public static void main(String[] args) {

        Scanner scan = new Scanner(System.in);
        System.out.println("--- CHANGE PASSWORD ---");

        System.out.print("Enter username: ");
        String username = scan.nextLine().trim();

        System.out.print("Enter old password: ");
        String oldPwd = scan.nextLine().trim();

        System.out.print("Enter new password: ");
        String newPwd = scan.nextLine().trim();

        System.out.print("Confirm new password: ");
        String confirmPwd = scan.nextLine().trim();

        // check before talking to DB
        if (!newPwd.equals(confirmPwd)) {
            System.out.println("Passwords do not match. Try again.");
            scan.close();
            return;
        }

        Connection conn = null;
        PreparedStatement checkStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;

        try {
            conn = DBConnection.getConnection();

            if (conn == null) {
                System.out.println("Database connection failed. Please check settings.");
                return;
            }

            // encrypt passwords
            String oldEnc, newEnc;
            try {
                oldEnc = PasswordEncryption.encrypt(oldPwd);
                newEnc = PasswordEncryption.encrypt(newPwd);
            } catch (Exception e) {
                System.out.println("Error while encrypting password: " + e.getMessage());
                return;
            }

            // verify old credentials
            String sqlCheck = "SELECT username FROM users WHERE username=? AND password=?";
            checkStmt = conn.prepareStatement(sqlCheck);
            checkStmt.setString(1, username);
            checkStmt.setString(2, oldEnc);
            rs = checkStmt.executeQuery();

            if (!rs.next()) {
                System.out.println("Invalid username or password.");
                try {
                    SecurityLogServices.addLog(username, "Password change failed (wrong old password)");
                } catch (Exception ignore) {
                    // logging failed - not fatal
                }
                return;
            }

            // update to new password
            String sqlUpdate = "UPDATE users SET password=? WHERE username=?";
            updateStmt = conn.prepareStatement(sqlUpdate);
            updateStmt.setString(1, newEnc);
            updateStmt.setString(2, username);

            int rows = updateStmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Password updated successfully!");
                try {
                    SecurityLogServices.addLog(username, "Password updated successfully");
                } catch (Exception logEx) {
                    System.out.println("(Couldn’t write to log — not critical)");
                }
            } else {
                System.out.println("No changes made. User may not exist.");
            }

        } catch (SQLException e) {
            System.out.println("SQL Error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        } finally {
            // clean up resources
            try {
                if (rs != null) rs.close();
                if (checkStmt != null) checkStmt.close();
                if (updateStmt != null) updateStmt.close();
                if (conn != null) conn.close();
            } catch (SQLException ignored) {}
            scan.close();
        }
    }
}
