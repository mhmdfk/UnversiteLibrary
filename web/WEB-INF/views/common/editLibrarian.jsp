<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="models.Librarian" %>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Librarian</title>
     <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            padding: 20px;
        }
        form {
            max-width: 500px;
            margin: auto;
            background: #fff;
            padding: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        label {
            display: block;
            margin-bottom: 10px;
            font-weight: bold;
        }
        input[type="text"], input[type="email"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 20px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        button {
            padding: 10px 15px;
            background-color: #007bff;
            color: #fff;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        button:hover {
            background-color: #0056b3;
        }
        .toggle-container {
            display: flex;
            align-items: center;
        }
        .toggle-container input {
            margin-right: 10px;
        }
    </style>
</head>
<body>
    <h1>Edit Librarian</h1>
    <% 
        Librarian librarian = (Librarian) request.getAttribute("librarian");
        if (librarian != null) {
    %>
   
    <form action="<%= request.getContextPath() %>/admin/updateLibrarian" method="post">
        <input type="hidden" name="id" value="<%= librarian.getId() %>">
        <label for="username">Username</label>
        <input type="text" id="username" name="username" value="<%= librarian.getUsername() %>" required>
        
        <label for="password">Password</label>
        <input type="password" id="password" name="password" value="<%= librarian.getPassword() %>" required>
        
        <button type="submit">Save Changes</button>
    </form>
    <% } else { %>
        <p>Librarian not found.</p>
    <% } %>
</body>
</html>
