<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add Book</title>
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

/* Heading Styles */
h1 {
    color: #343a40;
    font-size: 36px;
    margin-bottom: 20px;
    text-align: center;
}

/* Form Styles */
form {
    background-color: #fff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
    max-width: 500px;
    margin: 20px auto;
}

/* Label Styles */
form label {
    display: block;
    margin-bottom: 8px;
    font-weight: bold;
}

/* Input Field Styles */
form input[type="text"],
form input[type="number"],
form select {
    width: 100%;
    padding: 10px;
    margin-bottom: 15px;
    border: 1px solid #ccc;
    border-radius: 5px;
    font-size: 14px;
}

/* Button Styles */
form button {
    background-color: #28a745; /* Green button background */
    color: white;
    border: none;
    padding: 10px 15px;
    border-radius: 5px;
    cursor: pointer;
    font-size: 16px;
    transition: background-color 0.3s ease;
}

form button:hover {
    background-color: #218838; /* Darker green on hover */
}

/* Responsive Design */
@media (max-width: 768px) {
    body {
        padding: 15px;
    }

    form {
        padding: 15px;
    }

    h1 {
        font-size: 28px;
    }

    form button {
        font-size: 14px;
    }
}

    </style>
</head>
<body>
    <h1>Add New Book</h1>

    <%
        // Retrieve the origin parameter to determine the redirect path
        String origin = request.getParameter("origin");
    %>

    <form action="<%= request.getContextPath() %>/<%= origin %>/addBook" method="post">
        <!-- ISBN -->
        <label for="isbn">ISBN:</label>
        <input type="text" id="isbn" name="isbn" placeholder="Enter ISBN" required>

        <!-- Title -->
        <label for="title">Title:</label>
        <input type="text" id="title" name="title" placeholder="Enter Title" required>

        <!-- Author -->
        <label for="author">Author:</label>
        <input type="text" id="author" name="author" placeholder="Enter Author" required>

        <!-- Genre -->
        <label for="genre">Genre:</label>
        <input type="text" id="genre" name="genre" placeholder="Enter Genre" required>

        <!-- Publication Year -->
        <label for="publicationYear">Publication Year:</label>
        <input type="number" id="publicationYear" name="publicationYear" placeholder="Enter Publication Year" required>

        <!-- Status -->
        <label for="status">Status:</label>
        <select id="status" name="status" required>
            <option value="AVAILABLE">Available</option>
            <option value="RESERVED">Reserved</option>
            <option value="BORROWED">Borrowed</option>
        </select>

        <!-- Submit Button -->
        <button type="submit">Add Book</button>
    </form>
</body>
</html>
