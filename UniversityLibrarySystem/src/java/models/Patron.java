package models;

import controller.DatabaseConnection;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Patron {

    public enum PatronType {
        STUDENT, EMPLOYEE
    }
    
    private int id; // Changed from Long to int
    private String username;
    private String password;
    private String fullName;
    private PatronType type; // New field to differentiate between student and employee
    private LocalDate dob; // New field for Date of Birth
    private List<Book> borrowedBooks;

    // Constructor
    public Patron(int id, String username, String password, String fullName, LocalDate dob, PatronType type) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.dob = dob;
        this.type = type;
        this.borrowedBooks = new ArrayList<>();
    }

    // Getters and Setters
    public int getId() { // Changed return type to int
        return id;
    }

    public void setId(int id) { // Changed parameter type to int
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public PatronType getType() {
        return type;
    }

    public void setType(PatronType type) {
        this.type = type;
    }

    public LocalDate getDob() {
        return dob;
    }

    public void setDob(LocalDate dob) {
        this.dob = dob;
    }

    // Fetch all patrons
    public static List<Patron> fetchPatrons() throws SQLException {
        List<Patron> patrons = new ArrayList<>();
        String sql = "SELECT id, username, password, full_name, dob, type FROM patrons ORDER BY id";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql); 
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patrons.add(new Patron(
                    rs.getInt("id"), // Changed to getInt
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name"),
                    rs.getDate("dob").toLocalDate(), // Convert SQL Date to LocalDate
                    Patron.PatronType.valueOf(rs.getString("type")) // Convert String to PatronType enum
                ));
            }
        }
        return patrons;
    }

    // Fetch a patron by ID
    public static Patron fetchPatronById(int id) throws SQLException { // Changed parameter type to int
        String sql = "SELECT id, username, password, full_name, dob, type FROM patrons WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id); // Changed to setInt
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Patron(
                        rs.getInt("id"), // Changed to getInt
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name"),
                        rs.getDate("dob").toLocalDate(), // Convert SQL Date to LocalDate
                        Patron.PatronType.valueOf(rs.getString("type")) // Convert String to PatronType enum
                    );
                }
            }
        }
        return null;
    }

    // Create a new patron
    public static void createPatron(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        LocalDate dob = LocalDate.parse(request.getParameter("dob")); // Get DOB from request
        PatronType type = PatronType.valueOf(request.getParameter("type")); // Get type from request

        String sql = "INSERT INTO patrons (username, password, full_name, dob, type) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setDate(4, java.sql.Date.valueOf(dob)); // Convert LocalDate to SQL Date
            stmt.setString(5, type.toString()); // Convert PatronType to String
            stmt.executeUpdate();
        }
    }

    // Update a patron
    public static void updatePatron(HttpServletRequest request, HttpServletResponse response) throws SQLException {
        int id = Integer.parseInt(request.getParameter("id")); // Changed to parseInt
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String fullName = request.getParameter("fullName");
        LocalDate dob = LocalDate.parse(request.getParameter("dob")); // Get DOB from request
        PatronType type = PatronType.valueOf(request.getParameter("type")); // Get type from request

        String sql = "UPDATE patrons SET username = ?, password = ?, full_name = ?, dob = ?, type = ? WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, fullName);
            stmt.setDate(4, java.sql.Date.valueOf(dob)); // Convert LocalDate to SQL Date
            stmt.setString(5, type.toString()); // Convert PatronType to String
            stmt.setInt(6, id); // Changed to setInt
            stmt.executeUpdate();
        }
    }

    // Delete a patron by ID
    public static void deletePatronById(String id) {
        String sql = "DELETE FROM patrons WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, parseInt(id));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Other methods (unchanged)
    public static List<BorrowTransaction> getBorrowingHistory(int patronId) throws SQLException { // Changed parameter type to int
        List<BorrowTransaction> history = new ArrayList<>();
        String query = "SELECT * FROM borrow_transactions WHERE patron_id = ?";
        System.out.println("I'm mer");
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
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
    
    public static int getActiveBorrowCount(int patronId) throws SQLException {
        String query = "SELECT COUNT(*) AS active_borrows FROM borrow_transactions WHERE patron_id = ? AND (status = 'ACTIVE' OR status = 'OVERDUE')";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, patronId);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("active_borrows"); 
                }
            }
        }
        return 0; 
    }
    
    public static List<Fine> getOutstandingFines(int patronId) throws SQLException { // Changed parameter type to int
        List<Fine> fines = new ArrayList<>();
        String query = "SELECT * FROM fines WHERE patron_id = ? AND status = 'UNPAID'";
        try (Connection conn = DatabaseConnection.getConnection(); 
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, patronId); // Changed to setInt
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Fine fine = new Fine(
                            rs.getLong("id"), // Changed to getInt
                            rs.getInt("patron_id"), 
                            rs.getString("transaction_id"),
                            rs.getDouble("amount"),
                            rs.getTimestamp("issued_date").toLocalDateTime(),
                            rs.getTimestamp("paid_date") != null ? rs.getTimestamp("paid_date").toLocalDateTime() : null,
                            Fine.FineStatus.valueOf(rs.getString("status"))
                    );
                    fines.add(fine);
                }
            }
        }
        return fines;
    }
}