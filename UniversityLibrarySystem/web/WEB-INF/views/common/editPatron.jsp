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

        input[type="text"], input[type="password"], input[type="date"], select {
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
        String origin = request.getParameter("origin");
        Patron patron = (Patron) request.getAttribute("patron");
        if (patron != null) {
    %>
        <form action="<%= request.getContextPath() %>/<%=origin%>/updatePatron" method="post">
            <input type="hidden" name="id" value="<%= patron.getId() %>">
            
            <!-- Full Name -->
            <label for="fullName">Full Name:</label>
            <input type="text" id="fullName" name="fullName" value="<%= patron.getFullName() %>" required>
            
            <!-- Username -->
            <label for="username">Username:</label>
            <input type="text" id="username" name="username" value="<%= patron.getUsername() %>" required>
            
            <!-- Password -->
            <label for="password">Password:</label>
            <input type="password" id="password" name="password" value="<%= patron.getPassword() %>" required>
            <div class="toggle-container">
                <input type="checkbox" id="togglePasswordCheckbox" onclick="togglePassword()">
                <label for="togglePasswordCheckbox" id="togglePasswordIcon">Show</label>
            </div>
            
            <!-- Date of Birth -->
            <label for="dob">Date of Birth:</label>
            <input type="date" id="dob" name="dob" value="<%= patron.getDob() %>" required>
            
            <!-- Type (Student or Employee) -->
            <label for="type">Type:</label>
            <select id="type" name="type" required>
                <option value="STUDENT" <%= patron.getType() == Patron.PatronType.STUDENT ? "selected" : "" %>>Student</option>
                <option value="EMPLOYEE" <%= patron.getType() == Patron.PatronType.EMPLOYEE ? "selected" : "" %>>Employee</option>
            </select>
            
            <!-- Submit Button -->
            <button type="submit">Save Changes</button>
        </form>
    <% } else { %>
        <p>User Already exists.</p>
    <% } %>

    
</body>
</html>
