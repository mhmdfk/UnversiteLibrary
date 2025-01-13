package models;

import controller.DatabaseConnection;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Librarian {
    private Long id;
    private String username;
    private String password;

    // Constructor
    public Librarian(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    // Fetch all librarians
    public static List<Librarian> fetchLibrarians() throws SQLException {
        List<Librarian> librarians = new ArrayList<>();
        String sql = "SELECT id, username, password FROM librarians ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                librarians.add(new Librarian(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password")
                ));
            }
        }
        return librarians;
    }

    // Fetch a librarian by ID
    public static Librarian fetchLibrarianById(Long id) throws SQLException {
        String sql = "SELECT id, username, password FROM librarians WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Librarian(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        }
        return null;
    }

    // Create a new librarian
    public static void createLibrarian(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String sql = "INSERT INTO librarians (username, password) VALUES (?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.executeUpdate();
        }
    }

    // Update a librarian
    public static void updateLibrarian(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        Long id = Long.parseLong(request.getParameter("id"));
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        String sql = "UPDATE librarians SET username = ?, password = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setLong(3, id);
            stmt.executeUpdate();
        }
    }

    // Delete a librarian by ID
    public static void deleteLibrarianById(Long id) throws SQLException {
        String sql = "DELETE FROM librarians WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            stmt.executeUpdate();
        }
    }
}