package com.aegis.security;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBConnection {

    // Database configuration
    private static final String URL = "jdbc:mysql://localhost:3306/aegis_security";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public static Connection getConnection() {
        Connection conn = null;
        try {
            // Load JDBC driver (required for older MySQL versions)
            Class.forName("com.mysql.jdbc.Driver");

            // Establish connection
            conn = DriverManager.getConnection(URL, USER, PASSWORD);

            if (conn != null) {
                System.out.println("Database connection established.");
            } else {
                System.out.println("Database connection failed: returned null.");
            }

        } catch (ClassNotFoundException e) {
            System.out.println("MySQL JDBC driver not found. Please add it to your project.");
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        return conn;
    }
}
