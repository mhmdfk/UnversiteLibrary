package controller;

import models.Admin;
import models.Patron;
import models.Librarian;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import static java.lang.Integer.parseInt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/admin")
public class AdminController extends HttpServlet {
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        
        String action = request.getPathInfo();
        String view = request.getParameter("view");
        System.out.println("view: " + view);

        System.out.println("GET action in the admin: " + action);

        if (action == null) {
            // Fetch data from the database
            List<Patron> patrons = fetchPatrons();
            List<Librarian> librarians = fetchLibrarians();

            // Set attributes and forward to JSP
            request.setAttribute("patrons", patrons);
            request.setAttribute("librarians", librarians);
            request.getRequestDispatcher("/WEB-INF/views/common/adminDashboard.jsp").forward(request, response);
        } else {
            switch (action) {
                case "/editPatron":
                    showEditPatronForm(request, response);
                    break;
                case "/deletePatron":
                    request.getParameter("id");
                    deletePatronById(request.getParameter("id"));
                    response.sendRedirect(request.getContextPath() + "/admin?view=patrons");
                    break;
                case "/editLibrarian":
                    showEditLibrarianForm(request, response);
                    break;
                case "/deleteLibrarian":
                    deleteLibrarianById(request.getParameter("id"));
                    response.sendRedirect(request.getContextPath() + "/admin?view=librarians");
                    break;
                case "/addPatron":
                    request.getRequestDispatcher("/WEB-INF/views/common/addPatron.jsp").forward(request, response);
                    break;
                case "/addLibrarian":
                    request.getRequestDispatcher("/WEB-INF/views/common/addLibrarian.jsp").forward(request, response);
                    break;
                default:
                    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not found");
                    break;
            }
        }
    }
        
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        String action = request.getPathInfo();  // This retrieves the 'action' parameter

        System.out.println("Post action :: " + action);

        switch (action) {
            case "/updatePatron":
                updatePatron(request, response);
                response.sendRedirect(request.getContextPath() + "/admin?view=patrons");
                break;

            case "/updateLibrarian":
                updateLibrarian(request, response);
                response.sendRedirect(request.getContextPath() + "/admin?view=librarians");
                break;
            case "/addPatron":
                createPatron(request, response);
                response.sendRedirect(request.getContextPath() + "/admin?view=patrons");
                break;

            case "/addLibrarian":
                createLibrarian(request, response);
                response.sendRedirect(request.getContextPath() + "/admin?view=librarians");
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                break;
        }
    }

    private List<Patron> fetchPatrons() {
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

    private List<Librarian> fetchLibrarians() {
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

    private boolean isUsernameExist(String username, String table) {
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

    private void createPatron(HttpServletRequest request, HttpServletResponse response) {
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

    private void createLibrarian(HttpServletRequest request, HttpServletResponse response) {
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

    private void updatePatron(HttpServletRequest request, HttpServletResponse response) {
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

    private void updateLibrarian(HttpServletRequest request, HttpServletResponse response) {
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

    private void showEditPatronForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String patronId = request.getParameter("id");
        long id = Long.parseLong(patronId);
        Patron patron = fetchPatronById(id);  // Fetch patron from DB
        request.setAttribute("patron", patron);
        request.getRequestDispatcher("/WEB-INF/views/common/editPatron.jsp").forward(request, response);
    }

    private void showEditLibrarianForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String librarianId = request.getParameter("id");
        long id = Long.parseLong(librarianId);
        Librarian librarian = fetchLibrarianById(id);  // Fetch librarian from DB
        request.setAttribute("librarian", librarian);
        request.getRequestDispatcher("/WEB-INF/views/common/editLibrarian.jsp").forward(request, response);
    }

    private Patron fetchPatronById(Long id) {
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

    private Librarian fetchLibrarianById(Long id) {
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

    private void deletePatronById(String id) {
        String sql = "DELETE FROM patrons WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, parseInt(id));
            stmt.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteLibrarianById(String id) {
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
