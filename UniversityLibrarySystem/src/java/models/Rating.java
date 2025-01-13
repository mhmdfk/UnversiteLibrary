package models;

import controller.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Rating {
    private Long id;
    private Long bookId;
    private int patronId;
    private int rating;
    private LocalDateTime createdAt;

    // Constructor, Getters, and Setters
    public Rating(Long id, Long bookId, int patronId, int rating, LocalDateTime createdAt) {
        this.id = id;
        this.bookId = bookId;
        this.patronId = patronId;
        this.rating = rating;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public int getPatronId() { return patronId; }
    public void setPatronId(int patronId) { this.patronId = patronId; }

    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public static void addRating(Rating rating) throws SQLException {
        String query = "INSERT INTO ratings (book_id, patron_id, rating, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, rating.getBookId());
            pstmt.setLong(2, rating.getPatronId());
            pstmt.setInt(3, rating.getRating());
            pstmt.setTimestamp(4, Timestamp.valueOf(rating.getCreatedAt()));
            pstmt.executeUpdate();
        }
    }

    public static double getAverageRatingByBookId(Long bookId) throws SQLException {
        String query = "SELECT AVG(rating) AS average_rating FROM ratings WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("average_rating");
                }
            }
        }
        return 0.0;
    }

    public static List<Rating> getRatingsByBookId(Long bookId) throws SQLException {
        List<Rating> ratings = new ArrayList<>();
        String query = "SELECT * FROM ratings WHERE book_id = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Rating rating = new Rating(
                            rs.getLong("id"),
                            rs.getLong("book_id"),
                            rs.getInt("patron_id"),
                            rs.getInt("rating"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    ratings.add(rating);
                }
            }
        }
        return ratings;
    }
}