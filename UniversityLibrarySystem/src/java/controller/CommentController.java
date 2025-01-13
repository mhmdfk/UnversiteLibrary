package controller;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;
import models.Comment;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;


public class CommentController extends HttpServlet {

    // Handle POST request to add a comment
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long bookId = Long.parseLong(request.getParameter("bookId"));
        Integer patronId = (Integer) request.getSession(false).getAttribute("id");
        
        String commentText = request.getParameter("comment");
        LocalDateTime createdAt = LocalDateTime.now();

        Comment comment = new Comment(null, bookId, patronId, commentText, createdAt);
        try {
            Comment.addComment(comment);
            request.setAttribute("successMessage", "The comment has been sent successfully!");
//            RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/views/common/bookView.jsp");
//                dispatcher.forward(request, response);
            response.sendRedirect(request.getContextPath() + "/books?action=view&id=" + bookId);
        } catch (SQLException e) {
            throw new ServletException("Database connection error", e);
        }
    }
}