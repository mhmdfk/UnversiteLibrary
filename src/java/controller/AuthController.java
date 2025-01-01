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

    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getPathInfo();
        System.out.println("action: " + action);
        
        if (action == null) {
            action = "/login";
        }

        switch (action) {
            case "/login":
                showLoginForm(request, response);
                break;
            case "/logout":
                logout(request, response);
                break;
            default:
                response.sendRedirect(request.getContextPath() + "/auth/login");
        }
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
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

    private void showLoginForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(request, response);
    }

    private void processLogin(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
   
   
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        System.out.println("username: " + username);
        System.out.println("password: " + password);

        try (Connection conn = DatabaseConnection.getConnection()) {
            
            System.out.println("Reach 1");
            String query = "SELECT username, role FROM Users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setString(1, username);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                
                
                String userRole = rs.getString("role");
                System.out.println("UserRole:  "+ userRole);
                // Store username and role in session
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                session.setAttribute("role", userRole);

                System.out.println("GOT here");
//                 Redirect based on role
                if ("Admin".equalsIgnoreCase(userRole)) {
                        request.getRequestDispatcher("/WEB-INF/views/common/adminDashboard.jsp").forward(request, response);
                } else if ("Librarian".equalsIgnoreCase(userRole)) {
                    response.sendRedirect(request.getContextPath() + "/librarian/dashboard");
                    
                } else if ("Patron".equalsIgnoreCase(userRole)) {
//                    request.getRequestDispatcher("/WEB-INF/views/common/bookList.jsp").forward(request, response);
                    response.sendRedirect(request.getContextPath() + "/book");
                }
            } else {
                // Login failed
                request.setAttribute("error", "Invalid username or password");
                request.getRequestDispatcher("/WEB-INF/views/common/login.jsp").forward(request, response);
            }
        } catch (Exception e) {
            System.out.println("SSS");
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

    
    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "AuthController for handling login and logout actions.";
    }
}
