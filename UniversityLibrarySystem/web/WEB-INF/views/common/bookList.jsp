<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Book" %>
<%@ page import="models.BorrowTransaction" %>
<%@ page import="models.Fine" %>
<%@ page import="java.time.LocalDateTime" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Book List</title>
        <style>
            body {
                font-family: 'Arial', sans-serif;
                background-color: #ffffff; /* White background */
                margin: 0;
                padding: 0;
            }

            header {
                background-color: #28a745; /* Green header */
                color: #fff;
                padding: 20px;
                text-align: center;
                font-size: 24px;
                font-weight: bold;
            }

            .container {
                width: 90%;
                max-width: 1200px;
                margin: 20px auto;
                background: #ffffff;
                padding: 20px;
                box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                border-radius: 8px;
            }

            h1 {
                margin-top: 0;
                color: #28a745;
                font-size: 28px;
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
                padding: 12px;
                text-align: left;
            }

            th {
                background-color: #e6f9ea; /* Light green for headers */
                color: #333;
            }

            tr:nth-child(even) {
                background-color: #f9fff9; /* Very light green for alternating rows */
            }

            tr:hover {
                background-color: #e2f7e6; /* Slightly darker green on hover */
            }

            .top-right {
                position: absolute;
                top: 10px;
                right: 20px;
            }

            .search-form {
                margin: 20px 0;
                display: flex;
                justify-content: flex-end;
            }

            .search-form input {
                padding: 8px;
                width: 250px;
                border: 1px solid #ddd;
                border-radius: 4px;
                margin-right: 10px;
            }

            .search-form button {
                background-color: #28a745;
                color: white;
                padding: 8px 16px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
            }

            .search-form button:hover {
                background-color: #218838; /* Darker green on hover */
            }

            .notification-container {
                background-color: #e8f5e9; /* Very light green */
                border: 1px solid #c8e6c9;
                padding: 15px;
                border-radius: 8px;
                margin-bottom: 20px;
            }

            .notification-header {
                font-size: 18px;
                color: #2e7d32;
                margin-bottom: 10px;
                font-weight: bold;
            }

            .notification-item {
                padding: 10px;
                margin-bottom: 10px;
                border: 1px solid #ddd;
                border-radius: 4px;
            }

            .notification-item.overdue {
                background-color: #ffebee; /* Light red */
                border-color: #ffcdd2;
            }

            .notification-item.fine {
                background-color: #fff3e0; /* Light orange */
                border-color: #ffe0b2;
            }

            footer {
                text-align: center;
                padding: 10px 0;
                color: #777;
                font-size: 14px;
                margin-top: 20px;
            }

            /* Status-specific styles */
            .status-borrowed {
                color: blue;
                font-weight: bold;
            }

            .status-reserved {
                color: red;
                font-weight: bold;
            }

            .status-available {
                color: green;
                font-weight: bold;
            }
        </style>
    </head>
    <body>
        <header>Library Book List</header>

        <div class="top-right">
            <a href="<%= request.getContextPath() %>/patron" style="text-decoration: none; color: #ffffff;">My Account</a>
        </div>

        <div class="container">
            <h1>Books</h1>

            <!-- Notification Section -->
            <%
                List<BorrowTransaction> overdueBooks = (List<BorrowTransaction>) request.getAttribute("overdueBooks");
                List<Fine> unpaidFines = (List<Fine>) request.getAttribute("unpaidFines");
            %>
            <div class="notification-container">
                <div class="notification-header">Notifications</div>

                <% if (overdueBooks != null && !overdueBooks.isEmpty()) { %>
                    <div class="notification-header">Overdue Books</div>
                    <% for (BorrowTransaction transaction : overdueBooks) { 
                        Book book = Book.getBookById(transaction.getBookId());
                        String bookTitle = book != null ? book.getTitle() : "Unknown Book";
                    %>
                        <div class="notification-item overdue">
                            <strong>Book Title:</strong> <%= bookTitle %><br>
                            <strong>Due Date:</strong> <%= transaction.getDueDate() %><br>
                        </div>
                    <% } %>
                <% } %>

                <% if (unpaidFines != null && !unpaidFines.isEmpty()) { %>
                    <div class="notification-header">Unpaid Fines</div>
                    <% for (Fine fine : unpaidFines) { %>
                        <div class="notification-item fine">
                            <strong>Fine Amount:</strong> <%= fine.getAmount() %> JD<br>
                            <strong>Issued Date:</strong> <%= fine.getIssuedDate() %><br>
                        </div>
                    <% } %>
                <% } %>

                <% if ((overdueBooks == null || overdueBooks.isEmpty()) && (unpaidFines == null || unpaidFines.isEmpty())) { %>
                    <div class="notification-item">No new notifications.</div>
                <% } %>
            </div>

            <!-- Book Table -->
            <table>
                <thead>
                    <tr>
                        <th>Title</th>
                        <th>Author</th>
                        <th>ISBN</th>
                        <th>Genre</th>
                        <th>Publication Year</th>
                        <th>Status</th>
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
                        <td>
                            <span class="
                                <% if (book.getStatus() == Book.BookStatus.BORROWED) { %>
                                    status-borrowed
                                <% } else if (book.getStatus() == Book.BookStatus.RESERVED) { %>
                                    status-reserved
                                <% } else { %>
                                    status-available
                                <% } %>
                            ">
                                <%= book.getStatus() %>
                            </span>
                        </td>
                    </tr>
                    <% 
                            }
                        }
                    %>
                </tbody>
            </table>
        </div>
    </body>
</html>