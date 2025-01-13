package controller;

import models.Book;
import models.BorrowTransaction;
import models.Fine;
import models.Patron;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import models.Reservation;

@WebServlet("/librarian/*")
public class LibrarianController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Use false to avoid creating a new session
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Retrieve session attributes
        Integer id = (Integer) session.getAttribute("id");
        String role = (String) session.getAttribute("role");
        String username = (String) session.getAttribute("username");

        // Check if the user is an admin, if not, redirect to an unauthorized page or login page
        if (!"Librarian".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;  // Stop further execution
        }

        String action = request.getPathInfo();
        if (action == null || action.equals("/")) {
            try {
                List<Patron> patrons = Patron.fetchPatrons();
                List<Book> books = Book.getAllBooks();
                List<BorrowTransaction> overdueBooks = BorrowTransaction.getOverdueBooksByLibrarian();
                List<Fine> unpaidFines = Fine.getUnpaidFinesByLibrarian();

                request.setAttribute("overdueBooks", overdueBooks);
                request.setAttribute("unpaidFines", unpaidFines);
                request.setAttribute("patrons", patrons);
                request.setAttribute("books", books);
                request.getRequestDispatcher("/WEB-INF/views/common/librarianDashboard.jsp").forward(request, response);
            } catch (SQLException ex) {
                throw new ServletException("Database connection error", ex);
            }
        } else {
            switch (action) {
                case "/editPatron":
                    showEditPatronForm(request, response);
                    break;
                case "/deletePatron":
                    Patron.deletePatronById(request.getParameter("id"));
                    response.sendRedirect(request.getContextPath() + "/librarian?view=patrons");
                    break;
                case "/addPatron":
                    request.getRequestDispatcher("/WEB-INF/views/common/addPatron.jsp?origin=librarian").forward(request, response);
                    break;
                case "/addBook":
                    request.getRequestDispatcher("/WEB-INF/views/common/addBook.jsp?origin=librarian").forward(request, response);
                    break;
                case "/editBook":
                    try {
                        long bookId = Long.parseLong(request.getParameter("id"));
                        Book book = Book.getBookById(bookId);
                        if (book != null) {
                            request.setAttribute("book", book);
                            request.getRequestDispatcher("/WEB-INF/views/common/editBook.jsp?origin=librarian").forward(request, response);
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                        }
                    } catch (SQLException | NumberFormatException ex) {
                        throw new ServletException("Error fetching book", ex);
                    }
                    break;
                case "/deleteBook":
                    try {
                        long bookId = Long.parseLong(request.getParameter("id"));
                        Book book = Book.getBookById(bookId);
                        if (book != null) {
                            book.delete();
                            response.sendRedirect(request.getContextPath() + "/librarian?view=books");
                        } else {
                            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Book not found");
                        }
                    } catch (SQLException | NumberFormatException ex) {
                        throw new ServletException("Error deleting book", ex);
                    }
                    break;
                case "/returnBook":
                    request.getRequestDispatcher("/WEB-INF/views/common/returnForm.jsp").forward(request, response);
                    break;
                case "/viewBorrowingHistory":
                    viewBorrowingHistory(request, response);
                    break;
                case "/fineReports":
                    showFineReports(request, response);
                    break;
                case "/libraryUsage":
                    showLibraryUsage(request, response);
                    break;
                case "/editBorrowStatus":
                    showEditBorrowStatusForm(request, response);
                    break;
                case "/deleteReservation":
                try {
                    Long reservationId = Long.parseLong(request.getParameter("reservationId"));
                    Reservation.deleteReservation(reservationId);
                    response.sendRedirect(request.getContextPath() + "/librarian/libraryUsage");
                } catch (SQLException ex) {
                    Logger.getLogger(LibrarianController.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                }
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
        String action = request.getPathInfo();
        if (action == null || action.equals("/")) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
            return;
        }

        switch (action) {
            case "/updatePatron":
                try {
                    Patron.updatePatron(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(LibrarianController.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.sendRedirect(request.getContextPath() + "/librarian?view=patrons");
                break;
            case "/addPatron":
                try {
                    Patron.createPatron(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(LibrarianController.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.sendRedirect(request.getContextPath() + "/librarian?view=patrons");
                break;
            case "/addBook":
                try {
                    Book.addBook(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(LibrarianController.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.sendRedirect(request.getContextPath() + "/librarian?view=books");
                break;
            case "/updateBook":
                try {
                    Book.updateBook(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(LibrarianController.class.getName()).log(Level.SEVERE, null, ex);
                }
                response.sendRedirect(request.getContextPath() + "/librarian?view=books");
                break;

            case "/updateBorrowStatus":
                updateBorrowStatus(request, response);
                break;
            case "/payFine":
                payFine(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                break;
        }
    }

    private void showEditPatronForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int patronId = Integer.parseInt(request.getParameter("id"));
            Patron patron = Patron.fetchPatronById(patronId);
            if (patron != null) {
                request.setAttribute("patron", patron);
                request.getRequestDispatcher("/WEB-INF/views/common/editPatron.jsp?origin=librarian").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Patron not found");
            }
        } catch (SQLException | NumberFormatException ex) {
            throw new ServletException("Error fetching patron", ex);
        }
    }

    private void viewBorrowingHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            System.out.println("I'm heeeee");
            int patronId = Integer.parseInt(request.getParameter("patronId"));
            List<BorrowTransaction> history = Patron.getBorrowingHistory(patronId);
            request.setAttribute("borrowingHistory", history);
            request.getRequestDispatcher("/WEB-INF/views/common/borrowingHistory.jsp?origin=librarian").forward(request, response);
        } catch (SQLException | NumberFormatException ex) {
            throw new ServletException("Error fetching borrowing history", ex);
        }
    }

    // Add this method to handle fine reports
    private void showFineReports(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<Fine> allFines = Fine.getAllFines();
            List<Fine> outstandingFines = Fine.getFinesByStatus("UNPAID");
            List<Fine> overdueFines = Fine.getFinesByStatus("OVERDUE");
            List<Fine> collectedFines = Fine.getFinesByStatus("PAID");

            request.setAttribute("allFines", allFines);
            request.setAttribute("outstandingFines", outstandingFines);
            request.setAttribute("overdueFines", overdueFines);
            request.setAttribute("collectedFines", collectedFines);

            request.getRequestDispatcher("/WEB-INF/views/common/fineReports.jsp?origin=librarian").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error fetching fine reports", e);
        }
    }

    // Add this method to handle library usage statistics
    private void showLibraryUsage(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int activeBorrowersCount = BorrowTransaction.getActiveBorrowersCount();
            List<BorrowTransaction> activeBorrowings = BorrowTransaction.getBorrowingsByStatus("ACTIVE");
            List<BorrowTransaction> overdueBorrowings = BorrowTransaction.getBorrowingsByStatus("OVERDUE");
            List<Reservation> activeReservations = Reservation.getActiveReservations();

            request.setAttribute("activeBorrowersCount", activeBorrowersCount);
            request.setAttribute("activeBorrowings", activeBorrowings);
            request.setAttribute("overdueBorrowings", overdueBorrowings);
            request.setAttribute("activeReservations", activeReservations);
            request.getRequestDispatcher("/WEB-INF/views/common/libraryUsage.jsp?origin=librarian").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error fetching library usage statistics", e);
        }
    }

    private void showEditBorrowStatusForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String transactionId = request.getParameter("transactionId");
            BorrowTransaction transaction = BorrowTransaction.getTransactionById(transactionId);
            System.out.println("1112I'm in here and can't");
            if (transaction != null) {
                request.setAttribute("transaction", transaction);
                System.out.println("I'm in here and can't");
                request.getRequestDispatcher("/WEB-INF/views/common/editBorrowStatus.jsp?origin=librarian").forward(request, response);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Transaction not found");
            }
        } catch (SQLException ex) {
            throw new ServletException("Error fetching transaction", ex);
        }
    }

    private void updateBorrowStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            String transactionId = request.getParameter("transactionId");
            String status = request.getParameter("status");
            BorrowTransaction transaction = BorrowTransaction.getTransactionById(transactionId);
            if (transaction != null) {
                transaction.setStatus(BorrowTransaction.TransactionStatus.valueOf(status));
                transaction.update();
                response.sendRedirect(request.getContextPath() + "/librarian/libraryUsage");
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Transaction not found");
            }
        } catch (SQLException ex) {
            throw new ServletException("Error updating transaction status", ex);
        }
    }

    private void payFine(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            Long fineId = Long.parseLong(request.getParameter("fineId"));
            Fine fine = Fine.getFineById(fineId);
            if (fine != null && fine.getStatus() == Fine.FineStatus.UNPAID) {
                fine.setPaidDate(LocalDateTime.now());
                fine.setStatus(Fine.FineStatus.PAID);
                fine.update();
            }
            response.sendRedirect(request.getContextPath() + "/librarian/fineReports");
        } catch (SQLException ex) {
            throw new ServletException("Error paying fine", ex);
        }
    }

}
