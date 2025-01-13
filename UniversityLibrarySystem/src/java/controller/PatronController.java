/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package controller;

import java.io.IOException;
import java.sql.*;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.util.List;
import models.BorrowTransaction;
import models.Fine;
import models.Patron;

/**
 *
 * @author mfaaa
 */
public class PatronController extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false); 
        if (session == null) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;
        }

        // Retrieve session attributes
        Integer patronId = (Integer) session.getAttribute("id");
        String role = (String) session.getAttribute("role");
        String username = (String) session.getAttribute("username");
        
         if (!"Patron".equals(role)) {
            response.sendRedirect(request.getContextPath() + "/auth/login");
            return;  
        }
        try {
            
            Patron patron = Patron.fetchPatronById(patronId);
            request.setAttribute("patron", patron);

            
            List<Fine> outstandingFines = Fine.getOutstandingFinesByPatronId(patronId);
            request.setAttribute("outstandingFines", outstandingFines);

            
            List<BorrowTransaction> borrowHistory = BorrowTransaction.getBorrowingHistory(patronId);
            request.setAttribute("borrowHistory", borrowHistory);

            
            request.getRequestDispatcher("/WEB-INF/views/common/patronAccount.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Error fetching outstanding fines", e);
        }
    }

}
