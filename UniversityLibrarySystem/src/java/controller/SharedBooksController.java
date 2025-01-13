
package controller;

import models.SharedBooks;
import models.Book;
import models.Patron;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;


public class SharedBooksController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);

        // Check if the user is logged in
        if (session == null || session.getAttribute("id") == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Get the user ID from the session
        Integer sharedWithUserId = (Integer) session.getAttribute("id");

        try {
            // Fetch the shared books using the SharedBook module
            List<SharedBooks> sharedBooks = SharedBooks.getSharedBooks(sharedWithUserId);
            request.setAttribute("sharedBooks", sharedBooks);

            // Forward to the sharedBooks.jsp page
            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/sharedBooks.jsp");
            dispatcher.forward(request, response);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new ServletException("Database error while fetching shared books", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        if ("share".equals(action)) {
            Long bookId = Long.parseLong(request.getParameter("bookId"));
            Integer sharedByUserId = (Integer) request.getSession(false).getAttribute("id");
            Integer sharedWithUserId = Integer.parseInt(request.getParameter("sharedWithUserId"));

            try {
                // Share the book using the Book module
                Book.shareBook(bookId, sharedByUserId, sharedWithUserId);

                // Set the success message
                request.setAttribute("successMessage", "The book has been shared successfully!");

                // Fetch the book and patrons again to display on the same page
                Book book = Book.getBookById(bookId);
                List<Patron> patrons = Patron.fetchPatrons();

                request.setAttribute("book", book);
                request.setAttribute("patrons", patrons);

                // Forward the request to the bookView.jsp page
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/bookView.jsp");
                dispatcher.forward(request, response);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new ServletException("Database error while sharing the book", e);
            }
        }
    }
}