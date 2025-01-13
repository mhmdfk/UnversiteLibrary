package models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import controller.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Reservation {

    private Long reservationId;
    private Long bookId;
    private int patronId;
    private LocalDateTime reservationDate;
    private LocalDateTime expirationDate;

    // Constructor
    public Reservation(Long reservationId, Long bookId, int patronId, LocalDateTime reservationDate, LocalDateTime expirationDate) {
        this.reservationId = reservationId;
        this.bookId = bookId;
        this.patronId = patronId;
        this.reservationDate = reservationDate;
        this.expirationDate = expirationDate;
    }

    // Getters and Setters
    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public LocalDateTime getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(LocalDateTime reservationDate) {
        this.reservationDate = reservationDate;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
    }

    public static void reserveBook(Long bookId, int patronId) throws SQLException {
        String query = "INSERT INTO reservations (book_id, patron_id, reservation_date) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, bookId);
            pstmt.setLong(2, patronId);
            pstmt.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));
            pstmt.executeUpdate();
        }
    }

    public static List<Reservation> getReservationsByBookId(Long bookId) throws SQLException {
        List<Reservation> reservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE book_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, bookId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation(
                            rs.getLong("reservation_id"),
                            rs.getLong("book_id"),
                            rs.getInt("patron_id"),
                            rs.getTimestamp("reservation_date").toLocalDateTime(),
                            rs.getTimestamp("expiration_date") != null ? rs.getTimestamp("expiration_date").toLocalDateTime() : null
                    );
                    reservations.add(reservation);
                }
            }
        }
        return reservations;
    }

    public static List<Reservation> getActiveReservations() throws SQLException {
        List<Reservation> activeReservations = new ArrayList<>();
        String query = "SELECT * FROM reservations WHERE expiration_date > ? OR expiration_date IS NULL"; // Active reservations
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Reservation reservation = new Reservation(
                            rs.getLong("reservation_id"),
                            rs.getLong("book_id"),
                            rs.getInt("patron_id"),
                            rs.getTimestamp("reservation_date").toLocalDateTime(),
                            rs.getTimestamp("expiration_date") != null ? rs.getTimestamp("expiration_date").toLocalDateTime() : null
                    );
                    activeReservations.add(reservation);
                }
            }
        }
        return activeReservations;
    }
    
    public static void deleteReservation(Long reservationId) throws SQLException {
        // First, retrieve the reservation to get the book ID
        String selectQuery = "SELECT book_id FROM reservations WHERE reservation_id = ?";
        Long bookId = null;

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement selectStmt = conn.prepareStatement(selectQuery)) {
            selectStmt.setLong(1, reservationId);
            try (ResultSet rs = selectStmt.executeQuery()) {
                if (rs.next()) {
                    bookId = rs.getLong("book_id");
                }
            }
        }

        // If the book ID is found, update the book status to BORROWED
        if (bookId != null) {
            String updateQuery = "UPDATE books SET status = ? WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement updateStmt = conn.prepareStatement(updateQuery)) {
                updateStmt.setString(1, Book.BookStatus.BORROWED.toString());
                updateStmt.setLong(2, bookId);
                updateStmt.executeUpdate();
            }
        }

        // Finally, delete the reservation
        String deleteQuery = "DELETE FROM reservations WHERE reservation_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement deleteStmt = conn.prepareStatement(deleteQuery)) {
            deleteStmt.setLong(1, reservationId);
            deleteStmt.executeUpdate();
        }
    }
}
