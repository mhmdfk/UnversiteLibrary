package controller;

import models.Book;
import models.Reservation;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;

@WebServlet("/reserve")
public class ReservationController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long bookId = Long.parseLong(request.getParameter("bookId"));
        HttpSession session = request.getSession();
        //Long patronId = 15L; //(Long) session.getAttribute("patronId");  Assuming patronId is stored in session
        Integer patronId = (Integer) request.getSession(false).getAttribute("id");
        try {
            Book book = Book.getBookById(bookId);
            if (book != null && book.getStatus() == Book.BookStatus.BORROWED) {
                // Reserve the book
                Reservation.reserveBook(bookId, patronId);
                book.setStatus(Book.BookStatus.RESERVED);
                book.update();
                response.sendRedirect("books?action=view&id=" + bookId);
            } else {
                response.sendRedirect("books?action=view&id=" + bookId + "&error=unavailable");
            }
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }
}