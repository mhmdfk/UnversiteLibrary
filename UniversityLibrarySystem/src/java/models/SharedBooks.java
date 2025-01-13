package models;

import controller.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class SharedBooks {
    private Book book; // Composition: Use the existing Book module
    private Integer sharedByUserId;
    private LocalDateTime sharedAt;
    private String sharedByName;

    // Constructor
    public SharedBooks(Book book, Integer sharedByUserId, LocalDateTime sharedAt, String sharedByName) {
        this.book = book;
        this.sharedByUserId = sharedByUserId;
        this.sharedAt = sharedAt;
        this.sharedByName = sharedByName;
    }

    // Getters and Setters
    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public Integer getSharedByUserId() {
        return sharedByUserId;
    }

    public void setSharedByUserId(Integer sharedByUserId) {
        this.sharedByUserId = sharedByUserId;
    }

    public LocalDateTime getSharedAt() {
        return sharedAt;
    }

//    public String getSharedAt() {
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    return sharedAt.format(formatter);
//}

    public void setSharedAt(LocalDateTime sharedAt) {
        this.sharedAt = sharedAt;
    }

    public String getSharedByName() {
        return sharedByName;
    }

    public void setSharedByName(String sharedByName) {
        this.sharedByName = sharedByName;
    }

    // Fetch shared books for a specific user
    public static List<SharedBooks> getSharedBooks(Integer sharedWithUserId) throws SQLException {
        List<SharedBooks> SharedBooks = new ArrayList<>();
        String query = "SELECT b.*, sb.shared_by_user_id, sb.shared_at, p.full_name AS shared_by_name " +
                       "FROM books b " +
                       "JOIN shared_books sb ON b.id = sb.book_id " +
                       "JOIN patrons p ON sb.shared_by_user_id = p.id " +
                       "WHERE sb.shared_with_user_id = ? " +
                       "ORDER BY sb.shared_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, sharedWithUserId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    // Create a Book object
                    Book book = new Book(
                        rs.getLong("id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("publication_year"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        Book.BookStatus.valueOf(rs.getString("status"))
                    );

                    // Create a SharedBook object
                    SharedBooks sharedBook = new SharedBooks(
                        book,
                        rs.getInt("shared_by_user_id"),
                        rs.getTimestamp("shared_at").toLocalDateTime(),
                        rs.getString("shared_by_name")
                    );

                    SharedBooks.add(sharedBook);
                }
            }
        }
        return SharedBooks;
    }
}