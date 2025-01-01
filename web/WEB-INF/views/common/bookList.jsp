<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Book" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Book List</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                background-color: #f4f4f4;
                margin: 0;
                padding: 20px;
            }
            h1 {
                color: #333;
            }
            table {
                width: 100%;
                border-collapse: collapse;
                margin-top: 20px;
            }
            table, th, td {
                border: 1px solid #ddd;
            }
            th, td {
                padding: 10px;
                text-align: left;
            }
            th {
                background-color: #f8f8f8;
            }
            tr:nth-child(even) {
                background-color: #f9f9f9;
            }
            tr:hover {
                background-color: #f1f1f1;
            }
            a {
                text-decoration: none;
                color: #007bff;
            }
            a:hover {
                text-decoration: underline;
            }
            .search-form {
                margin-bottom: 20px;
            }
            .search-form input[type="text"] {
                padding: 8px;
                width: 200px;
            }
            .search-form button {
                padding: 8px 12px;
                background-color: #007bff;
                color: white;
                border: none;
                cursor: pointer;
            }
            .search-form button:hover {
                background-color: #0056b3;
            }
        </style>
    </head>
    <body>
        <h1>Book List</h1>
        
        <!-- Search Form -->
        <form action="books" method="get" class="search-form">
            <input type="text" name="searchQuery" placeholder="Search by author or title" value="<%= request.getParameter("searchQuery") != null ? request.getParameter("searchQuery") : "" %>">
            <button type="submit">Search</button>
        </form>

        <table>
            <thead>
                <tr>
                    <th>Title</th>
                    <th>Author</th>
                    <th>ISBN</th>
                    <th>Genre</th>
                    <th>Publication Year</th>
                    <th>Total Copies</th>
                    <th>Available Copies</th>
                </tr>
            </thead>
            <tbody>
                <% 
                    // Retrieve the list of books from the request
                    List<Book> books = (List<Book>) request.getAttribute("books");
                    if (books != null) {
                        for (Book book : books) {
                %>
                    <tr>
                        <td><a href="books?action=view&id=<%= book.getId() %>"><%= book.getTitle() %></a></td>
                        <td><%= book.getAuthor() %></td>
                        <td><%= book.getIsbn() %></td>
                        <td><%= book.getGenre() %></td>
                        <td><%= book.getPublicationYear() %></td>
                        <td><%= book.getTotalCopies() %></td>
                        <td><%= book.getAvailableCopies() %></td>
                    </tr>
                <% 
                        }
                    }
                %>
            </tbody>
        </table>
    </body>
</html>
