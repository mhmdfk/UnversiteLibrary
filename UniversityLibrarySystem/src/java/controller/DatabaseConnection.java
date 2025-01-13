package controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // Updated URL and user for Session Pooler
    private static final String URL = "jdbc:postgresql://aws-0-eu-central-1.pooler.supabase.com:5432/postgres";
    private static final String USER = "postgres.agczxedanserlhnwvqft"; // Updated user format
    private static final String PASSWORD = "tlysxjEwxLSAlJjS"; // Replace with your actual password

    static {
        try {
            Class.forName("org.postgresql.Driver"); // Explicitly register the driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load PostgreSQL JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("Attempting to connect to database with URL: " + URL);
        System.out.println("Username: " + USER);
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connected to database successfully!");
        return conn;
    }
}