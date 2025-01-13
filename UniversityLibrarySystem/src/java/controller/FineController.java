package controller;

import models.Fine;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

@WebServlet("/fines")
public class FineController extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        int patronId = Integer.parseInt(request.getParameter("patronId"));

        try {
            if (action == null) {
                // List all fines for the patron
                List<Fine> fines = Fine.getFinesByPatronId(patronId);
                request.setAttribute("fines", fines);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/fineList.jsp");
                dispatcher.forward(request, response);
            } else if (action.equals("view")) {
                // View a specific fine
                Long fineId = Long.parseLong(request.getParameter("id"));
                Fine fine = Fine.getFineById(fineId);
                request.setAttribute("fine", fine);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/fineView.jsp");
                dispatcher.forward(request, response);
            }
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long fineId = Long.parseLong(request.getParameter("id"));
        String action = request.getParameter("action");

        try {
            Fine fine = Fine.getFineById(fineId);
            if (fine != null && action.equals("pay")) {
                // Mark the fine as paid
                fine.setPaidDate(LocalDateTime.now());
                fine.setStatus(Fine.FineStatus.PAID);
                fine.update();
                response.sendRedirect("fines?patronId=" + fine.getPatronId());
            }
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }
}