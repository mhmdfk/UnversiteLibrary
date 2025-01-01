package controller;

import models.Book;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/books")
public class BookController extends HttpServlet {

    // Handle GET requests (e.g., list all books or get a specific book)
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        String action = request.getParameter("action");
//        System.out.print("action "+action);
//        try {
//            if (action == null) {
//                // List all books
//                List<Book> books = Book.getAllBooks();
//                request.setAttribute("books", books);
//                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/bookList.jsp");
//                dispatcher.forward(request, response);
//            } else if (action.equals("view")) {
//                // View a specific book
//                Long id = Long.parseLong(request.getParameter("id"));
//                Book book = Book.getBookById(id);
//                request.setAttribute("book", book);
//                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/bookView.jsp");
//                dispatcher.forward(request, response);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String searchQuery = request.getParameter("searchQuery"); // Get the search query

        try {
            if (action == null) {
                // List all books or search for books
                List<Book> books;
                if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                    // Filter books by title or author
                    books = Book.searchBooks(searchQuery);
                } else {
                    // No search query, get all books
                    books = Book.getAllBooks();
                }
                request.setAttribute("books", books);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/bookList.jsp");
                dispatcher.forward(request, response);
            } else if (action.equals("view")) {
                // View a specific book
                Long id = Long.parseLong(request.getParameter("id"));
                Book book = Book.getBookById(id);
                request.setAttribute("book", book);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/bookView.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle POST requests (e.g., add a new book)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String genre = request.getParameter("genre");
        int publicationYear = Integer.parseInt(request.getParameter("publicationYear"));
        int totalCopies = Integer.parseInt(request.getParameter("totalCopies"));
        int availableCopies = Integer.parseInt(request.getParameter("availableCopies"));

        Book book = new Book(null, isbn, title, author, genre, publicationYear, totalCopies, availableCopies, LocalDateTime.now(), LocalDateTime.now());

        try {
            book.save();
            response.sendRedirect("books");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle PUT requests (e.g., update a book)
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String isbn = request.getParameter("isbn");
        String title = request.getParameter("title");
        String author = request.getParameter("author");
        String genre = request.getParameter("genre");
        int publicationYear = Integer.parseInt(request.getParameter("publicationYear"));
        int totalCopies = Integer.parseInt(request.getParameter("totalCopies"));
        int availableCopies = Integer.parseInt(request.getParameter("availableCopies"));

        try {
            Book book = Book.getBookById(id);
            if (book != null) {
                book.setIsbn(isbn);
                book.setTitle(title);
                book.setAuthor(author);
                book.setGenre(genre);
                book.setPublicationYear(publicationYear);
                book.setTotalCopies(totalCopies);
                book.setAvailableCopies(availableCopies);
                book.update();
            }
            response.sendRedirect("books");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Handle DELETE requests (e.g., delete a book)
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));

        try {
            Book book = Book.getBookById(id);
            if (book != null) {
                book.delete();
            }
            response.sendRedirect("books");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}