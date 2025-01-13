package controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import models.Book;
import models.BorrowTransaction;
import models.Patron;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;


public class BorrowController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long bookId = Long.parseLong(request.getParameter("bookId"));
        HttpSession session = request.getSession();
        //Patron patron = (Patron) session.getAttribute("patron");
        Integer patronId = (Integer) session.getAttribute("id"); 
        try {
            Book book = Book.getBookById(bookId);

            
            if (book != null && book.isAvailable()  ) {
                ServletContext context = getServletContext();
                int maxBooksPerPatron = Integer.parseInt(context.getInitParameter("maxBooksPerPatron"));
                int currBooksCount = Patron.getActiveBorrowCount(patronId);

                if (currBooksCount >= maxBooksPerPatron) {
                    response.sendRedirect("books?action=view&id=" + bookId + "&error=maxBooksReached");
                    return;
                }
                
                int defaultBorrowingPeriodInDays = Integer.parseInt(context.getInitParameter("defaultBorrowingPeriodInDays"));
                
                LocalDateTime borrowDate = LocalDateTime.now();
                LocalDateTime dueDate = borrowDate.plus(defaultBorrowingPeriodInDays, ChronoUnit.DAYS);
                BorrowTransaction transaction = new BorrowTransaction(patronId, bookId, borrowDate, dueDate);
                 
               
                book.setStatus(Book.BookStatus.BORROWED);
                book.update();
                transaction.save();

                response.sendRedirect("books?action=view&id=" + bookId);
            } else {
                response.sendRedirect("books?action=view&id=" + bookId + "&error=unavailable");
            }
            
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }
}