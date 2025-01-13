<%@page import="models.Book"%>
<%@page import="models.Patron"%>
<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.BorrowTransaction" %>
<%@ page import="models.Fine" %>
<%@ page import="java.time.LocalDateTime" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Librarian Dashboard</title>
        <style>
            body {
                font-family: Arial, sans-serif;
                margin: 0;
                padding: 0;
                background-color: #f9f9f9;
                color: #333;
            }

            h1 {
                background-color: #4CAF50;
                color: white;
                margin: 0;
                padding: 20px;
                text-align: center;
            }

            h2 {
                margin: 20px;
                text-align: center;
            }

            /* Buttons */
            .toggle-buttons, .add-buttons {
                display: flex;
                justify-content: center;
                margin: 20px 0;
            }

            .toggle-buttons button, .add-buttons a {
                margin: 0 10px;
                padding: 10px 20px;
                background-color: #4CAF50;
                color: white;
                font-weight: bold;
                border: none;
                border-radius: 5px;
                text-decoration: none;
                cursor: pointer;
            }

            .toggle-buttons button:hover, .add-buttons a:hover {
                background-color: #45a049;
            }

            /* Table Styles */
            table {
                width: 80%;
                margin: 20px auto;
                border-collapse: collapse;
                background: white;
                box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
                display: none; /* Hidden by default */
            }

            table.active {
                display: table; /* Display active table */
            }

            table thead tr {
                background-color: #4CAF50;
                color: white;
                text-align: left;
            }

            table th, table td {
                padding: 12px 15px;
                border: 1px solid #ddd;
            }

            table tr:nth-child(even) {
                background-color: #f2f2f2;
            }

            table tr:hover {
                background-color: #f1f1f1;
                cursor: pointer;
            }

            /* Action Links */
            a {
                margin-right: 10px;
                text-decoration: none;
                color: #007BFF;
                font-weight: bold;
            }

            a:hover {
                text-decoration: underline;
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

            /* Responsive Design */
            @media (max-width: 768px) {
                table {
                    width: 100%;
                }

                th, td {
                    font-size: 14px;
                }

                h1, h2 {
                    font-size: 18px;
                }
            }
        </style>
        <script>
            function toggleView(view) {
                // Update the URL without reloading the page
                history.pushState(null, '', `?view=${view}`);

                // Show the corresponding table and link
                document.getElementById("patronsTable").style.display = view === "patrons" ? "table" : "none";
                document.getElementById("booksTable").style.display = view === "books" ? "table" : "none";

                document.getElementById("addPatronLink").style.display = view === "patrons" ? "block" : "none";
                document.getElementById("addBookLink").style.display = view === "books" ? "block" : "none";
            }


            function confirmDelete(deleteUrl) {
                if (confirm("Are you sure you want to delete this item?")) {
                    window.location.href = deleteUrl;
                }
            }

            document.addEventListener("DOMContentLoaded", () => {
                // Hide all tables and links by default
                document.getElementById("patronsTable").style.display = "none";
                document.getElementById("booksTable").style.display = "none";

                document.getElementById("addPatronLink").style.display = "none";
                document.getElementById("addBookLink").style.display = "none";

                const params = new URLSearchParams(window.location.search);
                const view = params.get("view");

                if (view === "patrons") {
                    toggleView('patrons');
                } else if (view === "librarians") {
                    toggleView('librarians');
                } else if (view === "books") {
                    toggleView('books');
                }
            });
        </script>
    </head>
    <body>
        <h1>Librarian Dashboard</h1>

        <div style="text-align: right; margin: 10px;">
            <button onclick="window.location.href = '<%= request.getContextPath()%>/auth/logout'" style="background-color: #f44336; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer;">
                Logout
            </button>
        </div>

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
                <strong>Patron ID:</strong> <%= transaction.getPatronId()%><br>
                <strong>Book Title:</strong> <%= bookTitle%><br>
                <strong>Due Date:</strong> <%= transaction.getDueDate()%><br>
            </div>
            <% } %>
            <% } %>

            <% if (unpaidFines != null && !unpaidFines.isEmpty()) { %>
            <div class="notification-header">Unpaid Fines</div>
            <% for (Fine fine : unpaidFines) {%>
            <div class="notification-item fine">
                <strong>Patron ID:</strong> <%= fine.getPatronId()%><br>
                <strong>Fine Amount:</strong> <%= fine.getAmount()%> JD<br>
                <strong>Issued Date:</strong> <%= fine.getIssuedDate()%><br>
            </div>
            <% } %>
            <% } %>

            <% if ((overdueBooks == null || overdueBooks.isEmpty()) && (unpaidFines == null || unpaidFines.isEmpty())) { %>
            <div class="notification-item">No new notifications.</div>
            <% }%>
        </div>

        <div class="toggle-buttons">
            <button onclick="toggleView('patrons')">Show Patrons</button>
            <button onclick="toggleView('books')">Show Books</button>
            <button onclick="window.location.href = '<%= request.getContextPath()%>/librarian/returnBook'" class="return-button">
                Return Book
            </button>
            <button onclick="window.location.href = '<%= request.getContextPath()%>/librarian/fineReports'">Fine Reports</button>
            <button onclick="window.location.href = '<%= request.getContextPath()%>/librarian/libraryUsage'">Library Usage</button>
        </div>


        <!-- Patrons Table -->
        <table id="patronsTable">
            <thead>
                <tr>
                    <th>Patron ID</th>
                    <th>Name</th>
                    <th>Username</th>
                    <th>Date of Birth</th>
                    <th>Type</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Patron> patrons = (List<Patron>) request.getAttribute("patrons");
                    if (patrons != null) {
                        for (Patron patron : patrons) {
                %>
                <tr>
                    <td><%= patron.getId()%></td>
                    <td><%= patron.getFullName()%></td>
                    <td><%= patron.getUsername()%></td>
                    <td><%= patron.getDob()%></td>
                    <td><%= patron.getType()%></td>
                    <td class="actions">
                        <a href="<%= request.getContextPath()%>/librarian/editPatron?id=<%= patron.getId()%>">Edit</a>
                        <a href="javascript:void(0);" onclick="confirmDelete('<%= request.getContextPath()%>/librarian/deletePatron?id=<%= patron.getId()%>')">Delete</a>
                        <a href="<%= request.getContextPath()%>/librarian/viewBorrowingHistory?patronId=<%= patron.getId()%>">View History</a>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>

        <!-- Books Table -->
        <table id="booksTable">
            <thead>
                <tr>
                    <th>Book ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>Genre</th>
                    <th>Publication Year</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<Book> books = (List<Book>) request.getAttribute("books");
                    if (books != null) {
                        for (Book book : books) {
                %>
                <tr>
                    <td><%= book.getId()%></td>
                    <td><%= book.getTitle()%></td>
                    <td><%= book.getAuthor()%></td>
                    <td><%= book.getGenre()%></td>
                    <td><%= book.getPublicationYear()%></td>
                    <td><%= book.getStatus()%></td>
                    <td class="actions">
                        <a href="<%= request.getContextPath()%>/librarian/editBook?id=<%= book.getId()%>">Edit</a>
                        <a href="javascript:void(0);" onclick="confirmDelete('<%= request.getContextPath()%>/librarian/deleteBook?id=<%= book.getId()%>')">Delete</a>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>

        <!-- Add Buttons -->
        <div class="add-buttons">
            <a id="addPatronLink" href="<%= request.getContextPath()%>/librarian/addPatron" style="display: none;">Add New Patron</a>
            <a id="addBookLink" href="<%= request.getContextPath()%>/librarian/addBook" style="display: none;">Add New Book</a>
        </div>   
    </body>
</html>