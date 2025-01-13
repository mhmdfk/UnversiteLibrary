package models;

import controller.DatabaseConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BorrowTransaction {

    private String transactionId;  // Format: BT######
    private int patronId;         // Patron ID instead of Patron object
    private Long bookId;           // Book ID instead of Book object
    private LocalDateTime borrowDate;
    private LocalDateTime dueDate;
    private LocalDateTime returnDate;
    private TransactionStatus status;

    public enum TransactionStatus {
        ACTIVE, RETURNED, OVERDUE
    }

    // Constructor for creating a new transaction
    public BorrowTransaction(int patronId, Long bookId, LocalDateTime borrowDate, LocalDateTime dueDate) {
        this.patronId = patronId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.status = TransactionStatus.ACTIVE; // Default status is ACTIVE
    }

    // Constructor for retrieving an existing transaction
    public BorrowTransaction(String transactionId, int patronId, Long bookId, LocalDateTime borrowDate, LocalDateTime dueDate, LocalDateTime returnDate, TransactionStatus status) {
        this.transactionId = transactionId;
        this.patronId = patronId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.dueDate = dueDate;
        this.returnDate = returnDate;
        this.status = status;
    }

    // Getters and Setters
    public String getTransactionId() {
        return transactionId;
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public TransactionStatus getStatus() {
        return status;
    }

    public void setStatus(TransactionStatus status) {
        this.status = status;
    }

    
    public void save() throws SQLException {
        String query = "INSERT INTO borrow_transactions (patron_id, book_id, borrow_date, due_date, return_date, status) "
                + "VALUES (?, ?, ?, ?, ?, ?) RETURNING transaction_id";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setLong(1, this.patronId);
            pstmt.setLong(2, this.bookId);
            pstmt.setTimestamp(3, Timestamp.valueOf(this.borrowDate));
            pstmt.setTimestamp(4, Timestamp.valueOf(this.dueDate));
            pstmt.setTimestamp(5, this.returnDate != null ? Timestamp.valueOf(this.returnDate) : null);
            pstmt.setString(6, this.status.toString());

            try (var rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    this.transactionId = rs.getString("transaction_id"); // Retrieve the generated transaction ID
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to save borrow transaction: " + e.getMessage(), e);
        }
    }

    
    public void update() throws SQLException {
        String query = "UPDATE borrow_transactions SET return_date = ?, status = ? WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setTimestamp(1, this.returnDate != null ? Timestamp.valueOf(this.returnDate) : null);
            pstmt.setString(2, this.status.toString());
            pstmt.setString(3, this.transactionId);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new SQLException("Failed to update borrow transaction: " + e.getMessage(), e);
        }
    }

    
    public static BorrowTransaction getTransactionById(String transactionId) throws SQLException {
        String query = "SELECT * FROM borrow_transactions WHERE transaction_id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, transactionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int patronId = rs.getInt("patron_id");
                    Long bookId = rs.getLong("book_id");
                    LocalDateTime borrowDate = rs.getTimestamp("borrow_date").toLocalDateTime();
                    LocalDateTime dueDate = rs.getTimestamp("due_date").toLocalDateTime();
                    LocalDateTime returnDate = rs.getTimestamp("return_date") != null ? rs.getTimestamp("return_date").toLocalDateTime() : null;
                    TransactionStatus status = TransactionStatus.valueOf(rs.getString("status"));

                    return new BorrowTransaction(transactionId, patronId, bookId, borrowDate, dueDate, returnDate, status);
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Failed to retrieve borrow transaction: " + e.getMessage(), e);
        }
        return null;
    }

    
   

    public static int getActiveBorrowersCount() throws SQLException {
        String query = "SELECT COUNT(DISTINCT patron_id) FROM borrow_transactions WHERE status = 'ACTIVE' ";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt(1);
            }
        }
        return 0;
    }

    public static List<BorrowTransaction> getBorrowingsByStatus(String status) throws SQLException {
        String query = "SELECT * FROM borrow_transactions WHERE status = ?";
        List<BorrowTransaction> borrowings = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String transactionId = rs.getString("transaction_id");
                    int patronId = rs.getInt("patron_id");
                    Long bookId = rs.getLong("book_id");
                    LocalDateTime borrowDate = rs.getTimestamp("borrow_date").toLocalDateTime();
                    LocalDateTime dueDate = rs.getTimestamp("due_date").toLocalDateTime();
                    LocalDateTime returnDate = rs.getTimestamp("return_date") != null ? rs.getTimestamp("return_date").toLocalDateTime() : null;
                    TransactionStatus transactionStatus = TransactionStatus.valueOf(rs.getString("status"));

                    borrowings.add(new BorrowTransaction(transactionId, patronId, bookId, borrowDate, dueDate, returnDate, transactionStatus));
                }
            }
        }
        return borrowings;
    }

    public static List<BorrowTransaction> getBorrowingHistory(int patronId) throws SQLException {
        List<BorrowTransaction> history = new ArrayList<>();
        String query = "SELECT * FROM borrow_transactions WHERE patron_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, patronId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    BorrowTransaction transaction = new BorrowTransaction(
                            rs.getString("transaction_id"),
                            rs.getInt("patron_id"),
                            rs.getLong("book_id"),
                            rs.getTimestamp("borrow_date").toLocalDateTime(),
                            rs.getTimestamp("due_date").toLocalDateTime(),
                            rs.getTimestamp("return_date") != null ? rs.getTimestamp("return_date").toLocalDateTime() : null,
                            BorrowTransaction.TransactionStatus.valueOf(rs.getString("status"))
                    );
                    history.add(transaction);
                }
            }
        }
        return history;
    }

    public static List<BorrowTransaction> getOverdueBooksByPatron(int patronId) throws SQLException {
        List<BorrowTransaction> overdueBooks = new ArrayList<>();
        String query = "SELECT * FROM borrow_transactions WHERE patron_id = ? AND ((due_date < ? AND status = 'ACTIVE') or (status = 'OVERDUE'))";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, patronId);
            pstmt.setTimestamp(2, Timestamp.valueOf(LocalDateTime.now()));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String transactionId = rs.getString("transaction_id");
                    Long bookId = rs.getLong("book_id");
                    LocalDateTime borrowDate = rs.getTimestamp("borrow_date").toLocalDateTime();
                    LocalDateTime dueDate = rs.getTimestamp("due_date").toLocalDateTime();
                    LocalDateTime returnDate = rs.getTimestamp("return_date") != null ? rs.getTimestamp("return_date").toLocalDateTime() : null;
                    TransactionStatus status = TransactionStatus.valueOf(rs.getString("status"));

                    BorrowTransaction transaction = new BorrowTransaction(transactionId, patronId, bookId, borrowDate, dueDate, returnDate, status);
                    overdueBooks.add(transaction);
                }
            }
        }
        return overdueBooks;
    }

    public static List<BorrowTransaction> getOverdueBooksByLibrarian() throws SQLException {
        List<BorrowTransaction> overdueBooks = new ArrayList<>();
        String query = "SELECT * FROM borrow_transactions WHERE (due_date < ? AND status = 'ACTIVE') OR (status = 'OVERDUE')";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String transactionId = rs.getString("transaction_id");
                    Integer patronId = rs.getInt("patron_id");
                    Long bookId = rs.getLong("book_id");
                    LocalDateTime borrowDate = rs.getTimestamp("borrow_date").toLocalDateTime();
                    LocalDateTime dueDate = rs.getTimestamp("due_date").toLocalDateTime();
                    LocalDateTime returnDate = rs.getTimestamp("return_date") != null ? rs.getTimestamp("return_date").toLocalDateTime() : null;
                    TransactionStatus status = TransactionStatus.valueOf(rs.getString("status"));

                    BorrowTransaction transaction = new BorrowTransaction(transactionId, patronId, bookId, borrowDate, dueDate, returnDate, status);
                    overdueBooks.add(transaction);
                }
            }
        }

        return overdueBooks;
    }

    @Override
    public String toString() {
        return "BorrowTransaction{"
                + "transactionId='" + transactionId + '\''
                + ", patronId=" + patronId
                + ", bookId=" + bookId
                + ", borrowDate=" + borrowDate
                + ", dueDate=" + dueDate
                + ", returnDate=" + returnDate
                + ", status=" + status
                + '}';
    }
}
