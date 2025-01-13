package controller;

import models.Book;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import models.BorrowTransaction;
import models.Fine;
import models.Patron;

@WebServlet("/books")
public class BookController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String searchQuery = request.getParameter("searchQuery");
        String error = request.getParameter("error");

        HttpSession session = request.getSession(false); 
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        
        Integer id = (Integer) session.getAttribute("id");
        String role = (String) session.getAttribute("role");
        String username = (String) session.getAttribute("username");

        
        if (!"Patron".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;  
        }

        try {
            if (action == null) {
                
                List<Book> books;
                if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                    books = Book.searchBooks(searchQuery);
                } else {
                    books = Book.getAllBooks();
                }

                List<BorrowTransaction> overdueBooks = BorrowTransaction.getOverdueBooksByPatron(id);
                List<Fine> unpaidFines = Fine.getOutstandingFinesByPatronId(id);
                request.setAttribute("overdueBooks", overdueBooks);
                request.setAttribute("unpaidFines", unpaidFines);

                request.setAttribute("books", books);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/bookList.jsp");
                dispatcher.forward(request, response);

            } else if (action.equals("view")) {
                
                if (session == null || session.getAttribute("id") == null || !"Patron".equals(session.getAttribute("role"))) {
                    
                    response.sendRedirect(request.getContextPath() + "/auth/login");
                    return; 
                }

                
                Long bookId = Long.parseLong(request.getParameter("id"));
                Book book = Book.getBookById(bookId);
                request.setAttribute("book", book);

                
                List<Patron> patrons = Patron.fetchPatrons();
                request.setAttribute("patrons", patrons);

                if (error != null && error.equals("unavailable")) {
                    request.setAttribute("error", "The book is not available for borrowing.");
                }

                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/bookView.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            // Handle adding a new book
            String isbn = request.getParameter("isbn");
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String genre = request.getParameter("genre");
            int publicationYear = Integer.parseInt(request.getParameter("publicationYear"));
            LocalDateTime createdAt = LocalDateTime.now();
            LocalDateTime updatedAt = LocalDateTime.now();
            Book.BookStatus status = Book.BookStatus.valueOf(request.getParameter("status"));

            Book book = new Book(null, isbn, title, author, genre, publicationYear, createdAt, updatedAt, status);

            try {
                book.save();
                response.sendRedirect("books");
            } catch (SQLException e) {
                throw new ServletException("Error adding book", e);
            }
        } else if (action.equals("update")) {
            // Handle updating an existing book
            Long id = Long.parseLong(request.getParameter("id"));
            String isbn = request.getParameter("isbn");
            String title = request.getParameter("title");
            String author = request.getParameter("author");
            String genre = request.getParameter("genre");
            int publicationYear = Integer.parseInt(request.getParameter("publicationYear"));
            Book.BookStatus status = Book.BookStatus.valueOf(request.getParameter("status"));

            try {
                Book book = Book.getBookById(id);
                if (book != null) {
                    book.setIsbn(isbn);
                    book.setTitle(title);
                    book.setAuthor(author);
                    book.setGenre(genre);
                    book.setPublicationYear(publicationYear);
                    book.setStatus(status);
                    book.update();
                }
                response.sendRedirect("books");
            } catch (SQLException e) {
                throw new ServletException("Error updating book", e);
            }
        } else if (action.equals("delete")) {
            
            Long id = Long.parseLong(request.getParameter("id"));

            try {
                Book book = Book.getBookById(id);
                if (book != null) {
                    book.delete();
                }
                response.sendRedirect("books");
            } catch (SQLException e) {
                throw new ServletException("Error deleting book", e);
            }
        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
        }
    }
}
