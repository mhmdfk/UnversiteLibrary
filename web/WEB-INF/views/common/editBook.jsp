<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="models.Book" %>
<!DOCTYPE html>
<html>
<head>
    <title>Edit Book</title>
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
        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
            max-width: 500px;
            margin: 20px auto;
        }
        form label {
            display: block;
            margin-bottom: 8px;
            font-weight: bold;
        }
        form input[type="text"],
        form input[type="number"],
        form input[type="date"] {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }
        form button {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
        }
        form button:hover {
            background-color: #0056b3;
        }
    </style>
</head>
<body>
    <h1>Edit Book</h1>

    <%
        // Retrieve the book object from the request
        Book book = (Book) request.getAttribute("book");
    %>

    <form action="<%= request.getContextPath() %>/admin/updateBook" method="post">
        <!-- Hidden field for Book ID -->
        <input type="hidden" name="id" value="<%= book.getId() %>">

        <!-- ISBN -->
        <label for="isbn">ISBN:</label>
        <input type="text" id="isbn" name="isbn" value="<%= book.getIsbn() %>" required>

        <!-- Title -->
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" value="<%= book.getTitle() %>" required>

        <!-- Author -->
        <label for="author">Author:</label>
        <input type="text" id="author" name="author" value="<%= book.getAuthor() %>" required>

        <!-- Genre -->
        <label for="genre">Genre:</label>
        <input type="text" id="genre" name="genre" value="<%= book.getGenre() %>" required>

        <!-- Publication Year -->
        <label for="publicationYear">Publication Year:</label>
        <input type="number" id="publicationYear" name="publicationYear" value="<%= book.getPublicationYear() %>" required>

        <!-- Total Copies -->
        <label for="totalCopies">Total Copies:</label>
        <input type="number" id="totalCopies" name="totalCopies" value="<%= book.getTotalCopies() %>" required>

        <!-- Available Copies -->
        <label for="availableCopies">Available Copies:</label>
        <input type="number" id="availableCopies" name="availableCopies" value="<%= book.getAvailableCopies() %>" required>

        <!-- Submit Button -->
        <button type="submit">Save Changes</button>
    </form>
</body>
</html>
