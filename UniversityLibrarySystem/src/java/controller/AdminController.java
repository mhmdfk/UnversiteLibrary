package controller;

import models.Patron;
import models.Librarian;
import models.Book;
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
import models.BorrowTransaction;
import models.Fine;
import models.Reservation;

@WebServlet("/admin")
public class AdminController extends HttpServlet {

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
        if (!"Admin".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;  // Stop further execution
        }

        String action = request.getPathInfo();
        String view = request.getParameter("view");

        if (action == null) {
            try {
                List<Patron> patrons = Patron.fetchPatrons();
                List<Librarian> librarians = Librarian.fetchLibrarians();
                List<Book> books = Book.getAllBooks();

                request.setAttribute("patrons", patrons);
                request.setAttribute("librarians", librarians);
                request.setAttribute("books", books);

                request.getRequestDispatcher("/WEB-INF/views/common/adminDashboard.jsp").forward(request, response);
            } catch (SQLException ex) {
                Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
            }
        } else {
            switch (action) {
                case "/editPatron":
                    showEditPatronForm(request, response);
                    break;
                case "/deletePatron":
                    Patron.deletePatronById(request.getParameter("id"));
                    response.sendRedirect(request.getContextPath() + "/admin?view=patrons");
                    break;
                case "/editLibrarian":
                    showEditLibrarianForm(request, response);
                    break;
                case "/deleteLibrarian":
                    try {
                        Librarian.deleteLibrarianById(Long.parseLong(request.getParameter("id")));
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                    }
                    response.sendRedirect(request.getContextPath() + "/admin?view=librarians");
                    break;
                case "/addPatron":
                    request.getRequestDispatcher("/WEB-INF/views/common/addPatron.jsp?origin=admin").forward(request, response);
                    break;
                case "/addLibrarian":
                    request.getRequestDispatcher("/WEB-INF/views/common/addLibrarian.jsp").forward(request, response);
                    break;
                case "/addBook":
                    request.getRequestDispatcher("/WEB-INF/views/common/addBook.jsp?origin=admin").forward(request, response);
                    break;
                case "/editBook":
                    try {
                        Book book = Book.getBookById(Long.parseLong(request.getParameter("id")));
                        request.setAttribute("book", book);
                        request.getRequestDispatcher("/WEB-INF/views/common/editBook.jsp?origin=admin").forward(request, response);
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                    }
                    break;
                case "/deleteBook":
                    try {
                        Book.getBookById(Long.parseLong(request.getParameter("id"))).delete();
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                    }
                    response.sendRedirect(request.getContextPath() + "/admin?view=books");
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
                case "/payFine":
                    payFine(request, response);
                    break;
                case "/deleteReservation":
                    try {
                        Long reservationId = Long.parseLong(request.getParameter("reservationId"));
                        Reservation.deleteReservation(reservationId);
                        response.sendRedirect(request.getContextPath() + "/admin/libraryUsage");
                    } catch (SQLException ex) {
                        Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
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

        switch (action) {
            case "/updatePatron":
                try {
                    Patron.updatePatron(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                }
                response.sendRedirect(request.getContextPath() + "/admin?view=patrons");
                break;
            case "/updateLibrarian":
                try {
                    Librarian.updateLibrarian(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                }
                response.sendRedirect(request.getContextPath() + "/admin?view=librarians");
                break;
            case "/addPatron":
                try {
                    Patron.createPatron(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                }
                response.sendRedirect(request.getContextPath() + "/admin?view=patrons");
                break;
            case "/addLibrarian":
                try {
                    Librarian.createLibrarian(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                }
                response.sendRedirect(request.getContextPath() + "/admin?view=librarians");
                break;
            case "/addBook":
                try {
                    Book.addBook(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                }
                response.sendRedirect(request.getContextPath() + "/admin?view=books");
                break;
            case "/updateBook":
                try {
                    Book.updateBook(request, response);
                } catch (SQLException ex) {
                    Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
                }
                response.sendRedirect(request.getContextPath() + "/admin?view=books");
                break;
            case "/updateBorrowStatus":
                updateBorrowStatus(request, response);
                break;
            default:
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid action.");
                break;
        }
    }

    private void showEditPatronForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Patron patron = Patron.fetchPatronById(Integer.parseInt(request.getParameter("id")));
            request.setAttribute("patron", patron);
            request.getRequestDispatcher("/WEB-INF/views/common/editPatron.jsp?origin=admin").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private void showEditLibrarianForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            Librarian librarian = Librarian.fetchLibrarianById(Long.parseLong(request.getParameter("id")));
            request.setAttribute("librarian", librarian);
            request.getRequestDispatcher("/WEB-INF/views/common/editLibrarian.jsp").forward(request, response);
        } catch (SQLException ex) {
            Logger.getLogger(AdminController.class.getName()).log(Level.SEVERE, null, ex);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        }
    }

    private void viewBorrowingHistory(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int patronId = Integer.parseInt(request.getParameter("patronId"));
            List<BorrowTransaction> history = Patron.getBorrowingHistory(patronId);
            request.setAttribute("borrowingHistory", history);
            request.getRequestDispatcher("/WEB-INF/views/common/borrowingHistory.jsp?origin=admin").forward(request, response);
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
            request.setAttribute("activeReservations", activeReservations); // Add active reservations

            request.getRequestDispatcher("/WEB-INF/views/common/libraryUsage.jsp?origin=admin").forward(request, response);
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
                request.getRequestDispatcher("/WEB-INF/views/common/editBorrowStatus.jsp?origin=admin").forward(request, response);
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
                response.sendRedirect(request.getContextPath() + "/admin/libraryUsage");
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
