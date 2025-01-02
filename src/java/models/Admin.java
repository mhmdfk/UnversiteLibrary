package models;

import controller.DatabaseConnection;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Admin {
    private Long id;
    private String username;
    private String password;
    

    public Admin(Long id, String username, String password, String email, String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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
    
public static void updateBook(HttpServletRequest request, HttpServletResponse response) throws SQLException {
    // Define the SQL query
        String sql = "UPDATE books SET isbn = ?, title = ?, author = ?, genre = ?, publication_year = ?, total_copies = ?, available_copies = ? WHERE id = ?";


    // Logging for debugging
    System.out.println("Updating book with ID: " + request.getParameter("id"));

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        // Set parameters in the correct order
              stmt.setString(1, request.getParameter("isbn")); // ISBN
        stmt.setString(2, request.getParameter("title")); // Title
        stmt.setString(3, request.getParameter("author")); // Author
        stmt.setString(4, request.getParameter("genre")); // Genre
        stmt.setInt(5, Integer.parseInt(request.getParameter("publicationYear"))); // Publication Year
        stmt.setInt(6, Integer.parseInt(request.getParameter("totalCopies"))); // Total Copies
        stmt.setInt(7, Integer.parseInt(request.getParameter("availableCopies"))); // Available Copies
        stmt.setLong(8, Long.parseLong(request.getParameter("id"))); // Book ID

        // Execute the update
        int rowsAffected = stmt.executeUpdate();

        // Logging success
        System.out.println("Book updated successfully. Rows affected: " + rowsAffected);
    } catch (NumberFormatException e) {
        // Log and handle number format issues
        System.err.println("Invalid input format: " + e.getMessage());
        throw e;
    } catch (SQLException e) {
        // Log and handle SQL issues
        System.err.println("SQL error while updating book: " + e.getMessage());
        throw e;
    }
}
        public static List<Patron> fetchPatrons() {
        List<Patron> patrons = new ArrayList<>();
        String sql = "SELECT id, username, password, full_name FROM patrons order by id;";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                patrons.add(new Patron(
                    rs.getLong("id"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("full_name")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return patrons;
    }

    public static List<Librarian> fetchLibrarians() {
        List<Librarian> librarians = new ArrayList<>();
        String sql = "SELECT id, username, password FROM librarians order by id";

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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return librarians;
    }

    public static boolean isUsernameExist(String username, String table) {
        String sql = "SELECT COUNT(*) FROM " + table + " WHERE username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;  // If count > 0, username exists
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;  // Username does not exist
    }

    public static void createPatron(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        // Check if username already exists
        if (isUsernameExist(username, "patrons")) {
            request.setAttribute("errorMessage", "Username already exists.");
            try {
                request.getRequestDispatcher("/WEB-INF/views/common/addPatron.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        String sql = "INSERT INTO patrons (username, password, full_name) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, request.getParameter("password"));
            stmt.setString(3, request.getParameter("fullName"));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void createLibrarian(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        // Check if username already exists
        if (isUsernameExist(username, "librarians")) {
            request.setAttribute("errorMessage", "Username already exists.");
            try {
                request.getRequestDispatcher("/WEB-INF/views/common/addLibrarian.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        String sql = "INSERT INTO librarians (username, password) VALUES (?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, request.getParameter("password"));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updatePatron(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        // Check if username already exists
        if (isUsernameExist(username, "patrons")) {
            request.setAttribute("errorMessage", "Username already exists.");
            try {
                request.getRequestDispatcher("/WEB-INF/views/common/editPatron.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        String sql = "UPDATE patrons SET full_name = ?, username= ?, password = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, request.getParameter("fullName"));
            stmt.setString(2, username);
            stmt.setString(3, request.getParameter("password"));
            stmt.setLong(4, Long.parseLong(request.getParameter("id")));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateLibrarian(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        // Check if username already exists
        if (isUsernameExist(username, "librarians")) {
            request.setAttribute("errorMessage", "Username already exists.");
            try {
                request.getRequestDispatcher("/WEB-INF/views/common/editLibrarian.jsp").forward(request, response);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        String sql = "UPDATE librarians SET username = ?, password = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, request.getParameter("password"));
            stmt.setLong(3, Long.parseLong(request.getParameter("id")));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    

    public static Patron fetchPatronById(Long id) {
        Patron patron = null;
        String sql = "SELECT id, username, password, full_name FROM patrons WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id); // Set the ID parameter
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    patron = new Patron(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("full_name")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return patron;
    }

    public static Librarian fetchLibrarianById(Long id) {
        Librarian librarian = null;
        String sql = "SELECT id, username, password FROM librarians WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id); // Set the ID parameter
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    librarian = new Librarian(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return librarian;
    }

    
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

    public static void deleteLibrarianById(String id) {
        String sql = "DELETE FROM librarians WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, parseInt(id));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}