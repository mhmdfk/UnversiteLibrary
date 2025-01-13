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

        h1 {
            color: #333;
            text-align: center;
        }

        form {
            max-width: 500px;
            margin: auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 5px;
            box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
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
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        button {
            background-color: #28a745; /* Green color */
            color: white;
            padding: 10px 15px;
            border: none;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }

        button:hover {
            background-color: #218838; /* Darker green on hover */
        }

        .back-link {
            display: block;
            text-align: center;
            margin-top: 20px;
        }

        .back-link a {
            color: #007bff;
            text-decoration: none;
        }

        .back-link a:hover {
            text-decoration: underline;
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
        <p>User Already exists.</p>
    <% } %>

</body>
</html>
