<%@page import="models.SharedBooks" %>
<%@page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <title>Shared Books</title>
    <style>
        /* General Reset */
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        /* Body Styles */
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background-color: #f8f9fa;
            color: #333;
            padding: 20px;
            max-width: 1200px;
            margin: 0 auto;
        }

        h1 {
            color: #343a40;
            font-size: 36px;
            margin-bottom: 20px;
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        th, td {
            padding: 10px;
            border: 1px solid #ddd;
            text-align: left;
        }

        th {
            background-color: #007bff;
            color: white;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        .no-books {
            text-align: center;
            color: #dc3545;
            font-weight: bold;
            margin-top: 20px;
        }
    </style>
</head>
<body>
    <h1>Books Shared with You</h1>

    <% 
        List<SharedBooks> sharedBooks = (List<SharedBooks>) request.getAttribute("sharedBooks");
        if (sharedBooks != null && !sharedBooks.isEmpty()) {
    %>
    <table>
        <thead>
            <tr>
                <th>Title</th>
                <th>Author</th>
                <th>Genre</th>
                <th>Publication Year</th>
                <th>Status</th>
                <th>Shared By</th>
                <th>Shared At</th>
            </tr>
        </thead>
        <tbody>
            <% for (SharedBooks sharedBook : sharedBooks) { %>
            <tr>
                <td><%= sharedBook.getBook().getTitle() %></td>
                <td><%= sharedBook.getBook().getAuthor() %></td>
                <td><%= sharedBook.getBook().getGenre() %></td>
                <td><%= sharedBook.getBook().getPublicationYear() %></td>
                <td><%= sharedBook.getBook().getStatus() %></td>
                <td><%= sharedBook.getSharedByName() %></td>
                <td><%= sharedBook.getSharedAt() %></td>
            </tr>
            <% } %>
        </tbody>
    </table>
    <% } else { %>
    <p class="no-books">No books have been shared with you.</p>
    <% } %>
</body>
</html>