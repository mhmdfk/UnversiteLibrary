package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Rating;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class RatingController extends HttpServlet {

    // Handle POST request to add a rating
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long bookId = Long.parseLong(request.getParameter("bookId"));


        Integer patronId = (Integer) request.getSession(false).getAttribute("id");
        int rating = Integer.parseInt(request.getParameter("rating"));
        LocalDateTime createdAt = LocalDateTime.now();

        Rating ratingObj = new Rating(null, bookId, patronId, rating, createdAt);
        try {
            Rating.addRating(ratingObj);
            response.sendRedirect(request.getContextPath() + "/books?action=view&id=" + bookId);
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }
}