
package controller;

import models.Patron;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/patrons")
public class PatronController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private List<Patron> patrons = new ArrayList<>();
    private Long nextId = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        if (action == null) {
            // List all patrons
            request.setAttribute("patrons", patrons);
            request.getRequestDispatcher("/views/patron/list.jsp").forward(request, response);
        } else {
            switch (action) {
                case "view":
                    // View a single patron
                    Long viewId = Long.parseLong(request.getParameter("id"));
                    Patron viewPatron = patrons.stream()
                            .filter(p -> p.getId().equals(viewId))
                            .findFirst()
                            .orElse(null);
                    request.setAttribute("patron", viewPatron);
                    request.getRequestDispatcher("/views/patron/view.jsp").forward(request, response);
                    break;
                case "edit":
                    // Show edit form for a patron
                    Long editId = Long.parseLong(request.getParameter("id"));
                    Patron editPatron = patrons.stream()
                            .filter(p -> p.getId().equals(editId))
                            .findFirst()
                            .orElse(null);
                    request.setAttribute("patron", editPatron);
                    request.getRequestDispatcher("/views/patron/edit.jsp").forward(request, response);
                    break;
                case "delete":
                    // Delete a patron
                    Long deleteId = Long.parseLong(request.getParameter("id"));
                    patrons.removeIf(p -> p.getId().equals(deleteId));
                    response.sendRedirect(request.getContextPath() + "/patrons");
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
                // Create a new patron
                Patron newPatron = new Patron(
                        nextId++,
                        request.getParameter("username"),
                        request.getParameter("password"),
                     
                        request.getParameter("fullName"));
                patrons.add(newPatron);
                response.sendRedirect(request.getContextPath() + "/patrons");
                break;
            case "update":
                // Update an existing patron
                Long updateId = Long.parseLong(request.getParameter("id"));
                Patron updatePatron = patrons.stream()
                        .filter(p -> p.getId().equals(updateId))
                        .findFirst()
                        .orElse(null);
                if (updatePatron != null) {
                    updatePatron.setUsername(request.getParameter("username"));
                    updatePatron.setPassword(request.getParameter("password"));
                    updatePatron.setFullName(request.getParameter("fullName"));
                    updatePatron.setPatronId(request.getParameter("patronId"));
                }
                response.sendRedirect(request.getContextPath() + "/patrons");
                break;
            default:
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Action not found");
                break;
        }
    }
}