<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add New Patron</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f4f4f4;
        }
        form {
            width: 400px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        }
        h1 {
            text-align: center;
            color: #333;
        }
        label {
            font-size: 14px;
            color: #555;
            margin-bottom: 5px;
            display: block;
        }
        input[type="text"], input[type="password"] {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ddd;
            border-radius: 4px;
        }
        button {
            width: 100%;
            padding: 10px;
            background-color: #4CAF50;
            color: white;
            border: none;
            border-radius: 4px;
            cursor: pointer;
        }
        button:hover {
            background-color: #45a049;
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
    <h1>Add New Patron</h1>
    
            
        <%-- Display the error message if the username exists --%>
        <% if (request.getAttribute("errorMessage") != null) { %>
            <div style="color: red; font-weight: bold;">
                <%= request.getAttribute("errorMessage") %>
            </div>
        

    <form action="<%= request.getContextPath() %>/admin/addPatron" method="post">
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required placeholder="Enter username">

        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required placeholder="Enter password">

        <label for="fullName">Full Name:</label>
        <input type="text" id="fullName" name="fullName" required placeholder="Enter full name">

        <button type="submit">Add Patron</button>
    </form>

        
    <div class="back-link">
        <a href="<%= request.getContextPath() %>/admin?view=patrons">Back to Patrons List</a>
    </div>
</body>
</html>
