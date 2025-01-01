package controller;

import models.Librarian;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/librarians")
public class LibrarianController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private List<Librarian> librarians = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            // List all librarians
            request.setAttribute("librarians", librarians);
            request.getRequestDispatcher("/views/librarian/list.jsp").forward(request, response);
        } else {
            switch (action) {
                case "view":
                    // View a single librarian
                    Long viewId = Long.parseLong(request.getParameter("id"));
                    Librarian viewLibrarian = librarians.stream()
                            .filter(l -> l.getId().equals(viewId))
                            .findFirst()
                            .orElse(null);
                    request.setAttribute("librarian", viewLibrarian);
                    request.getRequestDispatcher("/views/librarian/view.jsp").forward(request, response);
                    break;
                case "edit":
                    // Show edit form for a librarian
                    Long editId = Long.parseLong(request.getParameter("id"));
                    Librarian editLibrarian = librarians.stream()
                            .filter(l -> l.getId().equals(editId))
                            .findFirst()
                            .orElse(null);
                    request.setAttribute("librarian", editLibrarian);
                    request.getRequestDispatcher("/views/librarian/edit.jsp").forward(request, response);
                    break;
                case "delete":
                    // Delete a librarian
                    Long deleteId = Long.parseLong(request.getParameter("id"));
                    librarians.removeIf(l -> l.getId().equals(deleteId));
                    response.sendRedirect(request.getContextPath() + "/librarians");
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
        String action = request.getParameter("action");

        if (action == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Action parameter is missing");
            return;
        }

        switch (action) {
            case "create":
                // Create a new librarian
                Librarian newLibrarian = new Librarian(
                        nextId++,
                        request.getParameter("username"),
                        request.getParameter("password"));
                librarians.add(newLibrarian);
                response.sendRedirect(request.getContextPath() + "/librarians");
                break;
            case "update":
                // Update an existing librarian
                Long updateId = Long.parseLong(request.getParameter("id"));
                Librarian updateLibrarian = librarians.stream()
                        .filter(l -> l.getId().equals(updateId))
                        .findFirst()
                        .orElse(null);
                if (updateLibrarian != null) {
                    updateLibrarian.setUsername(request.getParameter("username"));
                    updateLibrarian.setPassword(request.getParameter("password"));
                
                }
                response.sendRedirect(request.getContextPath() + "/librarians");
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not found");
                break;
        }
    }
}