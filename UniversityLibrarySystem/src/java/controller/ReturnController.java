package controller;

import models.Book;
import models.BorrowTransaction;
import models.Fine;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import models.Reservation;

public class ReturnController extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String transactionId = request.getParameter("transactionId");
        LocalDateTime returnDate = LocalDateTime.now();

        try {
            // Retrieve the borrow transaction
            BorrowTransaction transaction = BorrowTransaction.getTransactionById(transactionId);
            if (transaction != null) {

                transaction.setReturnDate(returnDate);
                transaction.setStatus(BorrowTransaction.TransactionStatus.RETURNED);
                transaction.update();

                if (returnDate.isAfter(transaction.getDueDate())) {
                    ServletContext context = getServletContext();
                    double fineRatePerDay = Double.parseDouble(context.getInitParameter("fineRatePerDay"));
                    long daysLate = java.time.Duration.between(transaction.getDueDate(), returnDate).toDays();
                    double fineAmount = daysLate * fineRatePerDay;

                    Fine fine = new Fine(
                            null,
                            transaction.getPatronId(),
                            transaction.getTransactionId(),
                            fineAmount,
                            returnDate,
                            null,
                            Fine.FineStatus.UNPAID
                    );
                    fine.save();
                }

                // Update the book's status
                Book book = Book.getBookById(transaction.getBookId());
                if (book != null) {
                    // Check if the book has been reserved
                    if (book.getStatus() == Book.BookStatus.RESERVED) {
                        // Get the active reservation for this book
                        List<Reservation> reservations = Reservation.getReservationsByBookId(book.getId());
                        if (!reservations.isEmpty()) {
                            
                            Reservation reservation = reservations.get(0);

                            
                            LocalDateTime borrowDate = LocalDateTime.now();
                            LocalDateTime dueDate = borrowDate.plusDays(Integer.parseInt(getServletContext().getInitParameter("defaultBorrowingPeriodInDays")));
                            BorrowTransaction newTransaction = new BorrowTransaction(reservation.getPatronId(), book.getId(), borrowDate, dueDate);
                            newTransaction.save();

                            
                            book.setStatus(Book.BookStatus.BORROWED);
                            book.updateStatus();

                            
                            Reservation.deleteReservation(reservation.getReservationId());
                        } else {
                            
                            book.setStatus(Book.BookStatus.AVAILABLE);
                            book.updateStatus();
                        }
                    } else {
                        
                        book.setStatus(Book.BookStatus.AVAILABLE);
                        book.updateStatus();
                    }
                }

                // Redirect to the librarian dashboard with a success message
                response.sendRedirect("librarian?message=Book returned successfully.");
            } else {
                // Redirect with an error message if the transaction is not found
                response.sendRedirect("librarian?error=Transaction not found.");
            }
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }
}
