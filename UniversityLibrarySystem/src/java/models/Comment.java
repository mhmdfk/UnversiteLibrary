package models;

import java.time.LocalDateTime;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import controller.DatabaseConnection;

public class Comment {
    private Long id;
    private Long bookId;
    private int patronId;
    private String comment;
    private LocalDateTime createdAt;

    // Constructor, Getters, and Setters
    public Comment(Long id, Long bookId, int patronId, String comment, LocalDateTime createdAt) {
        this.id = id;
        this.bookId = bookId;
        this.patronId = patronId;
        this.comment = comment;
        this.createdAt = createdAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getBookId() { return bookId; }
    public void setBookId(Long bookId) { this.bookId = bookId; }

    public int getPatronId() { return patronId; }
    public void setPatronId(int patronId) { this.patronId = patronId; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
     public static void addComment(Comment comment) throws SQLException {
        String query = "INSERT INTO comments (book_id, patron_id, comment, created_at) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, comment.getBookId());
            pstmt.setLong(2, comment.getPatronId());
            pstmt.setString(3, comment.getComment());
            pstmt.setTimestamp(4, Timestamp.valueOf(comment.getCreatedAt()));
            pstmt.executeUpdate();
        }
    }

    public static List<Comment> getCommentsByBookId(Long bookId) throws SQLException {
        List<Comment> comments = new ArrayList<>();
        String query = "SELECT * FROM comments WHERE book_id = ? ORDER BY created_at DESC";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Comment comment = new Comment(
                            rs.getLong("id"),
                            rs.getLong("book_id"),
                            rs.getInt("patron_id"),
                            rs.getString("comment"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                    comments.add(comment);
                }
            }
        }
        return comments;
    }
}