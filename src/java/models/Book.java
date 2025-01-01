package models;

import controller.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Book {
    private Long id;
    private String isbn;
    private String title;
    private String author;
    private String genre;
    private Integer publicationYear;
    private Integer totalCopies;
    private Integer availableCopies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor
    public Book(Long id, String isbn, String title, String author, String genre, Integer publicationYear,
                Integer totalCopies, Integer availableCopies, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.totalCopies = totalCopies;
        this.availableCopies = availableCopies;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getGenre() { return genre; }
    public void setGenre(String genre) { this.genre = genre; }

    public Integer getPublicationYear() { return publicationYear; }
    public void setPublicationYear(Integer publicationYear) { this.publicationYear = publicationYear; }

    public Integer getTotalCopies() { return totalCopies; }
    public void setTotalCopies(Integer totalCopies) { this.totalCopies = totalCopies; }

    public Integer getAvailableCopies() { return availableCopies; }
    public void setAvailableCopies(Integer availableCopies) { this.availableCopies = availableCopies; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    // DAO Methods

    // Add a new book to the database
    public void save() throws SQLException {
        String query = "INSERT INTO books (isbn, title, author, genre, publication_year, total_copies, available_copies, created_at, updated_at) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, this.isbn);
            pstmt.setString(2, this.title);
            pstmt.setString(3, this.author);
            pstmt.setString(4, this.genre);
            pstmt.setInt(5, this.publicationYear);
            pstmt.setInt(6, this.totalCopies);
            pstmt.setInt(7, this.availableCopies);
            pstmt.setTimestamp(8, Timestamp.valueOf(this.createdAt));
            pstmt.setTimestamp(9, Timestamp.valueOf(this.updatedAt));
            pstmt.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getLong(1);
                }
            }
        }
    }

    // Retrieve all books from the database
    public static List<Book> getAllBooks() throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getLong("id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("publication_year"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
                
//                System.out.println(book);
                books.add(book);
            }
        }
        return books;
    }

    // Retrieve a book by ID
    public static Book getBookById(Long id) throws SQLException {
        String query = "SELECT * FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Book(
                            rs.getLong("id"),
                            rs.getString("isbn"),
                            rs.getString("title"),
                            rs.getString("author"),
                            rs.getString("genre"),
                            rs.getInt("publication_year"),
                            rs.getInt("total_copies"),
                            rs.getInt("available_copies"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime()
                    );
                }
            }
        }
        return null;
    }

    // Update a book in the database
    public void update() throws SQLException {
        String query = "UPDATE books SET isbn = ?, title = ?, author = ?, genre = ?, publication_year = ?, " +
                       "total_copies = ?, available_copies = ?, updated_at = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, this.isbn);
            pstmt.setString(2, this.title);
            pstmt.setString(3, this.author);
            pstmt.setString(4, this.genre);
            pstmt.setInt(5, this.publicationYear);
            pstmt.setInt(6, this.totalCopies);
            pstmt.setInt(7, this.availableCopies);
            pstmt.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setLong(9, this.id);
            pstmt.executeUpdate();
        }
    }

    // Delete a book from the database
    public void delete() throws SQLException {
        String query = "DELETE FROM books WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, this.id);
            pstmt.executeUpdate();
        }
    }

    public static List<Book> searchBooks(String searchQuery) throws SQLException {
    List<Book> books = new ArrayList<>();
    String query = "SELECT * FROM books WHERE title ILIKE ? OR author ILIKE ?";
    
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        
        // Search for books by title or author using LIKE operator
        String searchPattern = "%" + searchQuery + "%";
        pstmt.setString(1, searchPattern);
        pstmt.setString(2, searchPattern);

        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Book book = new Book(
                        rs.getLong("id"),
                        rs.getString("isbn"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("publication_year"),
                        rs.getInt("total_copies"),
                        rs.getInt("available_copies"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime()
                );
                books.add(book);
            }
        }
    }
    return books;
}

    // Override toString() for debugging
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", isbn='" + isbn + '\'' +
                ", title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", genre='" + genre + '\'' +
                ", publicationYear=" + publicationYear +
                ", totalCopies=" + totalCopies +
                ", availableCopies=" + availableCopies +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}