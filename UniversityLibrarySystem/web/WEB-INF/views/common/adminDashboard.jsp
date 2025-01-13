<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Patron" %>
<%@ page import="models.Librarian" %>
<%@ page import="models.Book" %> 

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f9f9f9;
            color: #333;
            margin: 0;
            padding: 0;
        }

        h1 {
            background-color: #4CAF50;
            color: white;
            margin: 0;
            padding: 20px;
            text-align: center;
        }

        h2 {
            color: #555;
            margin: 30px 0 10px 20px;
        }

        div {
            text-align: center;
            margin: 20px 0;
        }

        button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 10px 20px;
            margin: 5px;
            border-radius: 5px;
            cursor: pointer;
            font-size: 16px;
        }

        button:hover {
            background-color: #45a049;
        }

        table {
            width: 90%;
            margin: 0 auto 20px;
            border-collapse: collapse;
            background: white;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
        }

        table thead tr {
            background-color: #4CAF50;
            color: white;
        }

        table th, table td {
            padding: 12px 15px;
            text-align: left;
            border: 1px solid #ddd;
        }

        table tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        table tr:hover {
            background-color: #f1f1f1;
        }

        .actions a {
            margin-right: 10px;
            text-decoration: none;
            color: #007BFF;
            font-weight: bold;
        }

        .actions a:hover {
            text-decoration: underline;
        }

        a[id^="add"] {
            display: block;
            margin: 0 auto 20px;
            text-align: center;
            padding: 10px 20px;
            background-color: #4CAF50;
            color: white;
            font-weight: bold;
            border-radius: 5px;
            text-decoration: none;
            width: fit-content;
        }

        a[id^="add"]:hover {
            background-color: #45a049;
        }

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

            button {
                font-size: 14px;
                padding: 8px 16px;
            }
        }
    </style>
    <script>
        function toggleView(view) {
            // Update the URL without reloading the page
            history.pushState(null, '', `?view=${view}`);

            // Show the corresponding table and link
            document.getElementById("patronsTable").style.display = view === "patrons" ? "table" : "none";
            document.getElementById("librariansTable").style.display = view === "librarians" ? "table" : "none";
            document.getElementById("booksTable").style.display = view === "books" ? "table" : "none";

            document.getElementById("addPatronLink").style.display = view === "patrons" ? "block" : "none";
            document.getElementById("addLibrarianLink").style.display = view === "librarians" ? "block" : "none";
            document.getElementById("addBookLink").style.display = view === "books" ? "block" : "none";
        }

        document.addEventListener("DOMContentLoaded", () => {
            // Hide all tables and links by default
            document.getElementById("patronsTable").style.display = "none";
            document.getElementById("librariansTable").style.display = "none";
            document.getElementById("booksTable").style.display = "none";

            document.getElementById("addPatronLink").style.display = "none";
            document.getElementById("addLibrarianLink").style.display = "none";
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

        function confirmDelete(url) {
            if (confirm("Are you sure you want to delete this item?")) {
                window.location.href = url;
            }
        }
    </script>
</head>
<body>
    <h1>Admin Dashboard</h1>
    
    <div style="text-align: right; margin: 10px;">
        <button onclick="window.location.href='<%= request.getContextPath()%>/auth/logout'" style="background-color: #f44336; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer;">
            Logout
        </button>
    </div>
            
    <div>
        <button onclick="toggleView('patrons')">Show Patrons</button>
        <button onclick="toggleView('librarians')">Show Librarians</button>
        <button onclick="toggleView('books')">Show Books</button>
        <button onclick="window.location.href='<%= request.getContextPath()%>/admin/fineReports'">Fine Reports</button>
        <button onclick="window.location.href='<%= request.getContextPath()%>/admin/libraryUsage'">Library Usage</button>
    </div>

<!--    <h2>Manage Patrons</h2>-->
    <table id="patronsTable">
        <thead>
            <tr>
                <th>Patron ID</th>
                <th>Name</th>
                <th>Username</th>
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
                        <td><%= patron.getId() %></td>
                        <td><%= patron.getFullName() %></td>
                        <td><%= patron.getUsername() %></td>
                        <td class="actions">
                            <a href="<%= request.getContextPath() %>/admin/editPatron?id=<%= patron.getId() %>">Edit</a>
                            <a href="javascript:void(0);" onclick="confirmDelete('<%= request.getContextPath() %>/admin/deletePatron?id=<%= patron.getId() %>')">Delete</a>
                            <a href="<%= request.getContextPath() %>/admin/viewBorrowingHistory?patronId=<%= patron.getId() %>">View History</a>
                        </td>
                    </tr>
            <% 
                    } 
                } 
            %>
        </tbody>
    </table>
    <a id="addPatronLink" href="<%= request.getContextPath() %>/admin/addPatron">Add New Patron</a>

<!--    <h2>Manage Librarians</h2>-->
    <table id="librariansTable">
        <thead>
            <tr>
                <th>Librarian ID</th>
                <th>Name</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Librarian> librarians = (List<Librarian>) request.getAttribute("librarians");
                if (librarians != null) {
                    for (Librarian librarian : librarians) { 
            %>
                    <tr>
                        <td><%= librarian.getId() %></td>
                        <td><%= librarian.getUsername() %></td>
                        <td class="actions">
                            <a href="<%= request.getContextPath() %>/admin/editLibrarian?id=<%= librarian.getId() %>">Edit</a>
                            <a href="javascript:void(0);" onclick="confirmDelete('<%= request.getContextPath() %>/admin/deleteLibrarian?id=<%= librarian.getId() %>')">Delete</a>
                        </td>
                    </tr>
            <% 
                    } 
                } 
            %>
        </tbody>
    </table>
    <a id="addLibrarianLink" href="<%= request.getContextPath() %>/admin/addLibrarian">Add New Librarian</a>

<!--    <h2>Manage Books</h2>-->
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
                        <td><%= book.getId() %></td>
                        <td><%= book.getTitle() %></td>
                        <td><%= book.getAuthor() %></td>
                        <td><%= book.getGenre() %></td>
                        <td><%= book.getPublicationYear() %></td>
                        <td><%= book.getStatus() %></td>
                        <td class="actions">
                            <a href="<%= request.getContextPath() %>/admin/editBook?id=<%= book.getId() %>">Edit</a>
                            <a href="javascript:void(0);" onclick="confirmDelete('<%= request.getContextPath() %>/admin/deleteBook?id=<%= book.getId() %>')">Delete</a>
                        </td>
                    </tr>
            <% 
                    } 
                } 
            %>
        </tbody>
    </table>
    <a id="addBookLink" href="<%= request.getContextPath() %>/admin/addBook">Add New Book</a>
</body>
</html>
