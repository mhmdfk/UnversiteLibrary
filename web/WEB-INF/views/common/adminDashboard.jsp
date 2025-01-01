<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Patron" %>
<%@ page import="models.Librarian" %>
<%@ page import="models.Book" %> <!-- Import the Book model -->

<!DOCTYPE html>
<html>
<head>
    <title>Admin Dashboard</title>
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
        h2 {
            color: #555;
            margin-top: 30px;
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
        .actions a {
            margin-right: 10px;
            text-decoration: none;
            color: #007bff;
        }
        .actions a:hover {
            text-decoration: underline;
        }
        #librariansTable, #patronsTable, #booksTable, #addPatronLink, #addLibrarianLink, #addBookLink {
            display: none;
        }
    </style>
    <script>
        function toggleView(view) {
            // Update the URL without reloading the page
            history.pushState(null, '', `?view=${view}`);

            // Show the corresponding table and link
            if (view === "patrons") {
                document.getElementById("patronsTable").style.display = "table";
                document.getElementById("librariansTable").style.display = "none";
                document.getElementById("booksTable").style.display = "none";
                document.getElementById("addPatronLink").style.display = "block";
                document.getElementById("addLibrarianLink").style.display = "none";
                document.getElementById("addBookLink").style.display = "none";
            } else if (view === "librarians") {
                document.getElementById("librariansTable").style.display = "table";
                document.getElementById("patronsTable").style.display = "none";
                document.getElementById("booksTable").style.display = "none";
                document.getElementById("addPatronLink").style.display = "none";
                document.getElementById("addLibrarianLink").style.display = "block";
                document.getElementById("addBookLink").style.display = "none";
            } else if (view === "books") {
                document.getElementById("booksTable").style.display = "table";
                document.getElementById("patronsTable").style.display = "none";
                document.getElementById("librariansTable").style.display = "none";
                document.getElementById("addPatronLink").style.display = "none";
                document.getElementById("addLibrarianLink").style.display = "none";
                document.getElementById("addBookLink").style.display = "block";
            }
        }

        document.addEventListener("DOMContentLoaded", () => {
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

    <!-- Toggle View Buttons -->
    <div>
        <button onclick="toggleView('patrons')">Show Patrons</button>
        <button onclick="toggleView('librarians')">Show Librarians</button>
        <button onclick="toggleView('books')">Show Books</button>
    </div>

    <!-- Manage Patrons Section -->
    <h2>Manage Patrons</h2>
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
                        </td>
                    </tr>
            <% 
                    } 
                } 
            %>
        </tbody>
    </table>
    <a id="addPatronLink" href="<%= request.getContextPath() %>/admin/addPatron">Add New Patron</a>

    <!-- Manage Librarians Section -->
    <h2>Manage Librarians</h2>
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

    <!-- Manage Books Section -->
    <h2>Manage Books</h2>
    <table id="booksTable">
        <thead>
            <tr>
                <th>Book ID</th>
                <th>Title</th>
                <th>Author</th>
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
