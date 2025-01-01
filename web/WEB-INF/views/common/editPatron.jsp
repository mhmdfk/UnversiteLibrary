<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<%@ page import="models.Patron" %>

<!DOCTYPE html>
<html>
<head>
    <title>Edit Patron</title>
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
    <script>
        // Function to toggle password visibility
        function togglePassword() {
            var passwordField = document.getElementById("password");
            var toggleIcon = document.getElementById("togglePasswordIcon");
            if (passwordField.type === "password") {
                passwordField.type = "text"; // Show password
                toggleIcon.textContent = "Hide"; // Change icon text to 'Hide'
            } else {
                passwordField.type = "password"; // Hide password
                toggleIcon.textContent = "Show"; // Change icon text to 'Show'
            }
        }
        
    
    </script>
</head>
<body>
    <h1>Edit Patron</h1>
    <% 
        Patron patron = (Patron) request.getAttribute("patron");
        if (patron != null) {
    %>
        <form action="<%= request.getContextPath() %>/admin/updatePatron" method="post">
        
        
        <input type="hidden" name="id" value="<%= patron.getId() %>">
        <label for="fullName">Full Name</label>
        <input type="text" id="fullName" name="fullName" value="<%= patron.getFullName() %>" required>
        
        <label for="fullName">Username</label>
        <input type="text" id="username" name="username" value="<%= patron.getUsername() %>" required>
        
        <label for="password">Password</label>
        <input type="password" id="password" name="password" value="<%= patron.getPassword() %>" required>

        <div class="toggle-container">
            <input type="checkbox" id="togglePasswordCheckbox" onclick="togglePassword()">
            <label for="togglePasswordCheckbox" id="togglePasswordIcon">Show</label>
        </div>
        
        <button type="submit">Save Changes</button>
    </form>
    <% } else { %>
        <p>Patron not found.</p>
    <% } %>
</body>
</html>
