package models;

import controller.DatabaseConnection;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private BookStatus status;

    public enum BookStatus {
        AVAILABLE, RESERVED, BORROWED
    }

    // Constructor
    public Book(Long id, String isbn, String title, String author, String genre, Integer publicationYear,
                LocalDateTime createdAt, LocalDateTime updatedAt, BookStatus status) {
        this.id = id;
        this.isbn = isbn;
        this.title = title;
        this.author = author;
        this.genre = genre;
        this.publicationYear = publicationYear;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.status = status;
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

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public BookStatus getStatus() { return status; }
    public void setStatus(BookStatus status) { this.status = status; }

    public boolean isAvailable() {
        return status == BookStatus.AVAILABLE;
    }

    // DAO Methods

    // Add a new book to the database
    public void save() throws SQLException {
        String query = "INSERT INTO books (isbn, title, author, genre, publication_year, created_at, updated_at, status) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setString(1, this.isbn);
            pstmt.setString(2, this.title);
            pstmt.setString(3, this.author);
            pstmt.setString(4, this.genre);
            pstmt.setInt(5, this.publicationYear);
            pstmt.setTimestamp(6, Timestamp.valueOf(this.createdAt));
            pstmt.setTimestamp(7, Timestamp.valueOf(this.updatedAt));
            pstmt.setString(8, this.status.toString());
            pstmt.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getLong(1);
                }
            }
        }
    }

    public static void addBook(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        // Extract book details from the request
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String genre = request.getParameter("genre");
        int publicationYear = Integer.parseInt(request.getParameter("publicationYear"));
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        BookStatus status = BookStatus.valueOf(request.getParameter("status"));

        // SQL query to insert a new book
        String query = "INSERT INTO books (isbn, title, author, genre, publication_year, created_at, updated_at, status) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            // Set parameters for the query
            pstmt.setString(1, isbn);
            pstmt.setString(2, title);
            pstmt.setString(3, author);
            pstmt.setString(4, genre);
            pstmt.setInt(5, publicationYear);
            pstmt.setTimestamp(6, Timestamp.valueOf(createdAt));
            pstmt.setTimestamp(7, Timestamp.valueOf(updatedAt));
            pstmt.setString(8, status.toString());

            // Execute the query
            pstmt.executeUpdate();
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
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getTimestamp("updated_at").toLocalDateTime(),
                        BookStatus.valueOf(rs.getString("status"))
                );
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
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime(),
                            BookStatus.valueOf(rs.getString("status"))
                    );
                }
            }
        }
        return null;
    }

    // Update a book in the database
    public void update() throws SQLException {
        String query = "UPDATE books SET isbn = ?, title = ?, author = ?, genre = ?, publication_year = ?, " +
                       "updated_at = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, this.isbn);
            pstmt.setString(2, this.title);
            pstmt.setString(3, this.author);
            pstmt.setString(4, this.genre);
            pstmt.setInt(5, this.publicationYear);
            pstmt.setTimestamp(6, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.setString(7, this.status.toString());
            pstmt.setLong(8, this.id);
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

    public static void updateBook(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String sql = "UPDATE books SET isbn = ?, title = ?, author = ?, genre = ?, publication_year = ?, status = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, request.getParameter("isbn"));
            stmt.setString(2, request.getParameter("title"));
            stmt.setString(3, request.getParameter("author"));
            stmt.setString(4, request.getParameter("genre"));
            stmt.setInt(5, Integer.parseInt(request.getParameter("publicationYear")));
            stmt.setString(6, request.getParameter("status"));
            stmt.setLong(7, Long.parseLong(request.getParameter("id")));

            stmt.executeUpdate();
        }
    }
    
    public void updateStatus() throws SQLException {
        String query = "UPDATE books SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, this.status.toString());
            pstmt.setLong(2, this.id);
            pstmt.executeUpdate();
        }
    }

    public static List<Book> searchBooks(String searchQuery) throws SQLException {
        List<Book> books = new ArrayList<>();
        String query = "SELECT * FROM books WHERE title ILIKE ? OR author ILIKE ? ORDER BY publication_year DESC";

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
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getTimestamp("updated_at").toLocalDateTime(),
                            BookStatus.valueOf(rs.getString("status"))
                    );
                    books.add(book);
                }
            }
        }
        return books;
    }
    
    public static void shareBook(Long bookId, Integer sharedByUserId, Integer sharedWithUserId) throws SQLException {
        System.out.println("bookId: " + bookId);
        System.out.println("sharedByUserId: " + sharedByUserId);
        System.out.println("sharedWithUserId: " + sharedWithUserId);
    String query = "INSERT INTO shared_books (book_id, shared_by_user_id, shared_with_user_id) VALUES (?, ?, ?)";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setLong(1, bookId);
        pstmt.setInt(2, sharedByUserId);
        pstmt.setInt(3, sharedWithUserId);
        pstmt.executeUpdate();
    }
}

    // In Book.java
public static List<Book> getSharedBooks(Integer sharedWithUserId) throws SQLException {
    List<Book> sharedBooks = new ArrayList<>();
//    String query = "SELECT b.* FROM books b JOIN shared_books sb ON b.id = sb.book_id WHERE sb.shared_with_user_id = ?";
    String query = "SELECT DISTINCT b.*, p.full_name AS shared_by_name " +
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
                Book book = new Book(
                    rs.getLong("id"),
                    rs.getString("isbn"),
                    rs.getString("title"),
                    rs.getString("author"),
                    rs.getString("genre"),
                    rs.getInt("publication_year"),
                    rs.getTimestamp("created_at").toLocalDateTime(),
                    rs.getTimestamp("updated_at").toLocalDateTime(),
                    BookStatus.valueOf(rs.getString("status"))
                );
                sharedBooks.add(book);
            }
        }
    }
    return sharedBooks;
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
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", status=" + status +
                '}';
    }
}