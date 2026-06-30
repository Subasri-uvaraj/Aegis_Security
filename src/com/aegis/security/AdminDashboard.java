package com.aegis.security;

import java.sql.*;
import java.util.Scanner;

public class AdminDashboard {

    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        Connection conn = null;

        try {
            // connect to database
            conn = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/aegis_security", "root", "root");

            System.out.println("--- ADMIN DASHBOARD ---");
            int choice = 0;

            while (choice != 4) {
                System.out.println("\n1. View All Users");
                System.out.println("2. Add New User");
                System.out.println("3. Delete User");
                System.out.println("4. Logout");
                System.out.print("Enter choice: ");

                while (!input.hasNextInt()) {
                    System.out.print("Enter a valid number: ");
                    input.next();
                }

                choice = input.nextInt();
                input.nextLine(); // clear buffer

                switch (choice) {
                    case 1:
                        listUsers(conn);
                        break;
                    case 2:
                        createUser(conn, input);
                        break;
                    case 3:
                        removeUser(conn, input);
                        break;
                    case 4:
                        System.out.println("Logging out...");
                        break;
                    default:
                        System.out.println("Invalid option, please try again.");
                }
            }

        } catch (SQLException e) {
            System.out.println("Error connecting to DB: " + e.getMessage());
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException e) {
                System.out.println("Error closing connection: " + e.getMessage());
            }
            input.close();
        }
    }

    // View all registered users
    private static void listUsers(Connection conn) {
        String sql = "SELECT * FROM users";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n--- Registered Users ---");
            boolean found = false;

            while (rs.next()) {
                found = true;
                int id = rs.getInt("user_id");
                String uname = rs.getString("username");
                String email = rs.getString("email");
                String role = rs.getString("role");

                System.out.println(id + " | " + uname + " | " + email + " | " + role);
            }

            if (!found) {
                System.out.println("(No users in the database yet)");
            }

        } catch (SQLException e) {
            System.out.println("Error fetching user data: " + e.getMessage());
        }
    }

    // Add new user details
    private static void createUser(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter username: ");
            String uname = sc.nextLine().trim();

            System.out.print("Enter password: ");
            String pwd = sc.nextLine().trim();

            System.out.print("Enter email: ");
            String mail = sc.nextLine().trim();

            System.out.print("Enter role (ADMIN/USER/VISITOR): ");
            String role = sc.nextLine().trim().toUpperCase();

            String sql = "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, uname);
                ps.setString(2, pwd);
                ps.setString(3, mail);
                ps.setString(4, role);

                int row = ps.executeUpdate();
                if (row > 0)
                    System.out.println("User added successfully.");
                else
                    System.out.println("Could not add user. Try again.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding user: " + e.getMessage());
        }
    }

    // Delete user from DB
    private static void removeUser(Connection conn, Scanner sc) {
        try {
            System.out.print("Enter username to delete: ");
            String uname = sc.nextLine().trim();

            String sql = "DELETE FROM users WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setString(1, uname);
                int rows = ps.executeUpdate();

                if (rows > 0)
                    System.out.println("User deleted successfully.");
                else
                    System.out.println("User not found.");
            }

        } catch (SQLException e) {
            System.out.println("Error deleting user: " + e.getMessage());
        }
    }
}
