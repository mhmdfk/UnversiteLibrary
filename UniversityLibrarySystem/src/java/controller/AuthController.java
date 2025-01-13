package controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        String action = request.getPathInfo();
        
        System.out.println("actoin: " + action);
        if (action == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
        } else {
            switch (action) {
    case "/login-patron":
        showLoginForm(request, response, "patron");
        break;
    case "/login":
        showLoginForm(request, response, "patron");  
        break;
    case "/login-admin":
        showLoginForm(request, response, "admin");
        break;
    case "/login-librarian":
        showLoginForm(request, response, "librarian");
        break;
    case "/logout":
        logout(request, response);
        break;
    default:
        response.sendRedirect(request.getContextPath() + "/auth/login-patron");
}

        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        if ("/login".equals(action)) {
            processLogin(request, response);
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unsupported action: " + action);
        }
    }

    private void showLoginForm(HttpServletRequest request, HttpServletResponse response , String role)
            throws ServletException, IOException {
//        String  = request.getParameter("role");

        if ("admin".equals(role)) {
            request.getRequestDispatcher("/WEB-INF/views/common/adminLogin.jsp").forward(request, response);
        } else if ("librarian".equals(role)) {
            request.getRequestDispatcher("/WEB-INF/views/common/librarianLogin.jsp").forward(request, response);
        } else {
            request.getRequestDispatcher("/WEB-INF/views/common/patronLogin.jsp").forward(request, response);
        }
    }

    private void processLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String role = request.getParameter("role");

        try (Connection conn = DatabaseConnection.getConnection()) {
            String query = "";
            if ("admin".equals(role)) {
                query = "SELECT id, 'Admin' AS role FROM admins WHERE username = ? AND password = ?";
            } else if ("librarian".equals(role)) {
                query = "SELECT id, 'Librarian' AS role FROM librarians WHERE username = ? AND password = ?";
            } else {
                query = "SELECT id, 'Patron' AS role, full_name FROM patrons WHERE username = ? AND password = ?";
            }

            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                HttpSession session = request.getSession();
                int id = rs.getInt("id");
                session.setAttribute("id", id);
                session.setAttribute("username", username);
                session.setAttribute("role", rs.getString("role"));

                // Redirect based on the role
                if ("Admin".equals(rs.getString("role"))) {
                    response.sendRedirect(request.getContextPath() + "/admin");
                } else if ("Librarian".equals(rs.getString("role"))) {
                    response.sendRedirect(request.getContextPath() + "/librarian");
                } else if ("Patron".equals(rs.getString("role"))) {
                    session.setAttribute("fullName", rs.getString("full_name"));
                    response.sendRedirect(request.getContextPath() + "/book");
                }
            } else {
                request.setAttribute("error", "Invalid username or password");
                showLoginForm(request, response , role);
            }
        } catch (Exception e) {
            throw new ServletException("Database connection error", e);
        }
    }

    private void logout(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        response.sendRedirect(request.getContextPath() + "/auth/login");
    }

    @Override
    public String getServletInfo() {
        return "AuthController for handling login and logout actions.";
    }
}
