/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author faisal
 */


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://db.agczxedanserlhnwvqft.supabase.co:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "tlysxjEwxLSAlJjS";

    static {
        try {
            Class.forName("org.postgresql.Driver"); // Explicitly register the driver
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Failed to load PostgreSQL JDBC driver", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        System.out.println("Attempting to connect to database...");
        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
        System.out.println("Connected to database successfully!");
        return conn;
    }
}