
package controller;

import com.sun.jdi.connect.spi.Connection;
import models.Patron;
import models.Librarian;
import java.io.IOException;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Admin;
import models.Book;
import controller.DatabaseConnection;


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
            List<Patron> patrons = Admin.fetchPatrons();
            List<Librarian> librarians = Admin.fetchLibrarians();
            
            try {
                List<Book> books;
                books = Book.getAllBooks();
                request.setAttribute("books", books);
            } catch (SQLException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
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
                    
                    
                    
                    Admin.deletePatronById(request.getParameter("id"));
                    response.sendRedirect(request.getContextPath() + "/admin?view=patrons");
                    break;
                case "/editLibrarian":
                    showEditLibrarianForm(request, response);
                    break;
                case "/deleteLibrarian":
                    Admin.deleteLibrarianById(request.getParameter("id"));
                    response.sendRedirect(request.getContextPath() + "/admin?view=librarians");
                    break;
                case "/addPatron":
                    request.getRequestDispatcher("/WEB-INF/views/common/addPatron.jsp").forward(request, response);
                    break;
                case "/addLibrarian":
                    request.getRequestDispatcher("/WEB-INF/views/common/addLibrarian.jsp").forward(request, response);
                    break;
                    
                case "/editBook":
                    
                        String bookId = request.getParameter("id");
                if (bookId != null) {
                    try {
                            long id = Long.parseLong(bookId);
                            Book book = Book.getBookById(id); // Fetch book from database
                            System.out.println("BBBBB: " + book);
                            request.setAttribute("book", book);
                            request.getRequestDispatcher("/WEB-INF/views/common/editBook.jsp").forward(request, response);
                            
                    } catch (NumberFormatException | SQLException ex) {
                            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid book ID.");
                    }
                }
                else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Book ID is required.");
                    }
                    break;

                    
                    
                case "/deleteBook":
                    
                    
                {
                    try {
                        long id = Long.parseLong(request.getParameter("id"));
                        Book.getBookById(id).delete();
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                    
                    response.sendRedirect(request.getContextPath() + "/admin?view=books");
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

        System.out.println("Post action ::" + action);

        switch (action) {
            case "/updatePatron":
                Admin.updatePatron(request, response);
                response.sendRedirect(request.getContextPath() + "/admin?view=patrons");
                break;

            case "/updateLibrarian":
                Admin.updateLibrarian(request, response);
                response.sendRedirect(request.getContextPath() + "/admin?view=librarians");
                break;
            case "/addPatron":
                Admin.createPatron(request, response);
                response.sendRedirect(request.getContextPath() + "/admin?view=patrons");
                break;

            case "/addLibrarian":
                Admin.createLibrarian(request, response);
                response.sendRedirect(request.getContextPath() + "/admin?view=librarians");
                break;
            
            case "/updateBook":
                try {
                                       
                    Admin.updateBook(request , response); // Update the book in the database

                    response.sendRedirect(request.getContextPath() + "/admin?view=books");
                } catch (Exception e) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, e);
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Failed to update the book.");
                }

                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                break;
        }
    }
    
  

    private void showEditPatronForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String patronId = request.getParameter("id");
        long id = Long.parseLong(patronId);
        Patron patron = Admin.fetchPatronById(id);  // Fetch patron from DB
        request.setAttribute("patron", patron);
        request.getRequestDispatcher("/WEB-INF/views/common/editPatron.jsp").forward(request, response);
    }

    private void showEditLibrarianForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String librarianId = request.getParameter("id");
        long id = Long.parseLong(librarianId);
        Librarian librarian = Admin.fetchLibrarianById(id);  // Fetch librarian from DB
        request.setAttribute("librarian", librarian);
        request.getRequestDispatcher("/WEB-INF/views/common/editLibrarian.jsp").forward(request, response);
    }
}
