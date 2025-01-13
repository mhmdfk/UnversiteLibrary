package models;

import controller.DatabaseConnection;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Fine {

    private Long id;
    private int patronId;
    private String transactionId; // Changed to String
    private double amount;
    private LocalDateTime issuedDate;
    private LocalDateTime paidDate;
    private FineStatus status;

    public enum FineStatus {
        PAID, UNPAID
    }

    // Constructor
    public Fine(Long id, int patronId, String transactionId, double amount, LocalDateTime issuedDate, LocalDateTime paidDate, FineStatus status) {
        this.id = id;
        this.patronId = patronId;
        this.transactionId = transactionId;
        this.amount = amount;
        this.issuedDate = issuedDate;
        this.paidDate = paidDate;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public String getTransactionId() {
        return transactionId;
    } // Changed to String

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    } // Changed to String

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public LocalDateTime getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(LocalDateTime paidDate) {
        this.paidDate = paidDate;
    }

    public FineStatus getStatus() {
        return status;
    }

    public void setStatus(FineStatus status) {
        this.status = status;
    }

    public void save() throws SQLException {
        String query = "INSERT INTO fines (patron_id, transaction_id, amount, issued_date, paid_date, status) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            pstmt.setLong(1, this.patronId);
            pstmt.setString(2, this.transactionId);
            pstmt.setDouble(3, this.amount);
            pstmt.setTimestamp(4, Timestamp.valueOf(this.issuedDate));
            pstmt.setTimestamp(5, this.paidDate != null ? Timestamp.valueOf(this.paidDate) : null);
            pstmt.setString(6, this.status.toString());
            pstmt.executeUpdate();

            // Retrieve the generated ID
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    this.id = rs.getLong(1);
                }
            }
        }
    }

    // Update a fine (e.g., mark as paid)
    public void update() throws SQLException {
        String query = "UPDATE fines SET paid_date = ?, status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setTimestamp(1, this.paidDate != null ? Timestamp.valueOf(this.paidDate) : null);
            pstmt.setString(2, this.status.toString());
            pstmt.setLong(3, this.id);
            pstmt.executeUpdate();
        }
    }

    // Retrieve all fines for a patron
    public static List<Fine> getFinesByPatronId(int patronId) throws SQLException {
        List<Fine> fines = new ArrayList<>();
        String query = "SELECT * FROM fines WHERE patron_id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, patronId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Fine fine = new Fine(
                            rs.getLong("id"),
                            rs.getInt("patron_id"),
                            rs.getString("transaction_id"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("issued_date").toLocalDateTime(),
                            rs.getTimestamp("paid_date") != null ? rs.getTimestamp("paid_date").toLocalDateTime() : null,
                            FineStatus.valueOf(rs.getString("status"))
                    );
                    fines.add(fine);
                }
            }
        }
        return fines;
    }
    
    public static List<Fine> getOutstandingFinesByPatronId(int patronId) throws SQLException {
        List<Fine> fines = new ArrayList<>();
        String query = "SELECT * FROM fines WHERE patron_id = ? AND status = 'UNPAID'";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, patronId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Fine fine = new Fine(
                            rs.getLong("id"),
                            rs.getInt("patron_id"),
                            rs.getString("transaction_id"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("issued_date").toLocalDateTime(),
                            rs.getTimestamp("paid_date") != null ? rs.getTimestamp("paid_date").toLocalDateTime() : null,
                            FineStatus.valueOf(rs.getString("status"))
                    );
                    fines.add(fine);
                }
            }
        }
        return fines;
    }
    // Retrieve a fine by ID
    public static Fine getFineById(Long id) throws SQLException {
        String query = "SELECT * FROM fines WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setLong(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return new Fine(
                            rs.getLong("id"),
                            rs.getInt("patron_id"),
                            rs.getString("transaction_id"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("issued_date").toLocalDateTime(),
                            rs.getTimestamp("paid_date") != null ? rs.getTimestamp("paid_date").toLocalDateTime() : null,
                            FineStatus.valueOf(rs.getString("status"))
                    );
                }
            }
        }
        return null;
    }

    
    public static List<Fine> getPendingFinesByTransactionId(String transactionId) throws SQLException {
        List<Fine> fines = new ArrayList<>();
        String query = "SELECT * FROM fines WHERE transaction_id = ? AND status = 'UNPAID'";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, transactionId);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Fine fine = new Fine(
                            rs.getLong("id"),
                            rs.getInt("patron_id"),
                            rs.getString("transaction_id"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("issued_date").toLocalDateTime(),
                            rs.getTimestamp("paid_date") != null ? rs.getTimestamp("paid_date").toLocalDateTime() : null,
                            FineStatus.valueOf(rs.getString("status"))
                    );
                    fines.add(fine);
                }
            }
        }
        return fines;
    }

    public static List<Fine> getFinesByStatus(String status) throws SQLException {
        List<Fine> fines = new ArrayList<>();
        String query = "SELECT * FROM fines WHERE status = ?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Fine fine = new Fine(
                            rs.getLong("id"),
                            rs.getInt("patron_id"),
                            rs.getString("transaction_id"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("issued_date").toLocalDateTime(),
                            rs.getTimestamp("paid_date") != null ? rs.getTimestamp("paid_date").toLocalDateTime() : null,
                            FineStatus.valueOf(rs.getString("status"))
                    );
                    fines.add(fine);
                }
            }
        }
        return fines;
    }

    public static List<Fine> getAllFines() throws SQLException {
        List<Fine> fines = new ArrayList<>();
        String query = "SELECT * FROM fines";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query); ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Fine fine = new Fine(
                        rs.getLong("id"),
                        rs.getInt("patron_id"),
                        rs.getString("transaction_id"),
                        rs.getDouble("amount"),
                        rs.getTimestamp("issued_date").toLocalDateTime(),
                        rs.getTimestamp("paid_date") != null ? rs.getTimestamp("paid_date").toLocalDateTime() : null,
                        FineStatus.valueOf(rs.getString("status"))
                );
                fines.add(fine);
            }
        }
        return fines;
    }
    
//     public static List<Fine> getUnpaidFinesByPatron(int patronId) throws SQLException {
//        System.out.println("####");
//        List<Fine> unpaidFines = new ArrayList<>();
//        String query = "SELECT * FROM fines WHERE patron_id = ? AND status = 'UNPAID'";
//
//        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
//            pstmt.setLong(1, patronId);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                while (rs.next()) {
//                    Long id = rs.getLong("id");
//                    String transactionId = rs.getString("transaction_id");
//                    double amount = rs.getDouble("amount");
//                    LocalDateTime issuedDate = rs.getTimestamp("issued_date").toLocalDateTime();
//                    LocalDateTime paidDate = rs.getTimestamp("paid_date") != null ? rs.getTimestamp("paid_date").toLocalDateTime() : null;
//                    FineStatus status = FineStatus.valueOf(rs.getString("status")); // Convert string to enum
//                    
//                    System.out.println("####");
//                    System.out.println("amount: "+ amount);
//
//                    Fine fine = new Fine(id, patronId, transactionId, amount, issuedDate, paidDate, status);
//                    unpaidFines.add(fine);
//                }
//            }
//        }
//        return unpaidFines;
//    }

    public static List<Fine> getUnpaidFinesByLibrarian() throws SQLException {
        System.out.println("Fetching unpaid fines for librarian...");
        List<Fine> unpaidFines = new ArrayList<>();
        String query = "SELECT * FROM fines WHERE status = 'UNPAID'";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Long id = rs.getLong("id");
                    Integer patronId = rs.getInt("patron_id");
                    String transactionId = rs.getString("transaction_id");
                    double amount = rs.getDouble("amount");
                    LocalDateTime issuedDate = rs.getTimestamp("issued_date").toLocalDateTime();
                    LocalDateTime paidDate = rs.getTimestamp("paid_date") != null ? rs.getTimestamp("paid_date").toLocalDateTime() : null;
                    FineStatus status = FineStatus.valueOf(rs.getString("status"));

                    System.out.println("Fine ID: " + id);
                    System.out.println("Patron ID: " + patronId);
                    System.out.println("Amount: " + amount);
                    System.out.println("Issued Date: " + issuedDate);
                    System.out.println("Status: " + status);

                    Fine fine = new Fine(id, patronId, transactionId, amount, issuedDate, paidDate, status);
                    unpaidFines.add(fine);
                }
            }
    }
    
    return unpaidFines;
}     
    

}
