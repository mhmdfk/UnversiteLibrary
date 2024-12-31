/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author faisal
 */

import controller.DatabaseConnection;
import java.sql.Connection;

public class TestDatabaseConnection {
    public static void main(String[] args) {
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn != null) {
                System.out.println("Connection successful!");
            } else {
                System.out.println("Connection failed!");
            }
        } catch (Exception e) {
            System.err.println("An error occurred while trying to connect to the database:");
            e.printStackTrace();
        }
    }
}
