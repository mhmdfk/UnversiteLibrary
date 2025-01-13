<%@ page contentType="text/html" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add New Patron</title>
    <style>
        /* General Styles */
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
            background-color: #f8f9fa;
        }

        h1 {
            text-align: center;
            color: #343a40;
        }

        form {
            width: 400px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        }

        /* Form Label Styles */
        label {
            font-size: 14px;
            color: #555;
            margin-bottom: 8px;
            display: block;
        }

        /* Input Fields and Select Box */
        input[type="text"],
        input[type="password"],
        input[type="date"],
        select {
            width: 100%;
            padding: 10px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 5px;
            font-size: 14px;
        }

        /* Button Styles */
        button {
            width: 100%;
            padding: 10px;
            background-color: #28a745; /* Green color */
            color: white;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            cursor: pointer;
            transition: background-color 0.3s ease;
        }

        button:hover {
            background-color: #218838; /* Darker green on hover */
        }

        /* Error Message Styles */
        .error-message {
            color: red;
            font-weight: bold;
            text-align: center;
            margin-bottom: 15px;
        }

        /* Back Link */
        .back-link {
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

        /* Responsive Design for Smaller Screens */
        @media (max-width: 768px) {
            form {
                width: 90%;
                padding: 15px;
            }
        }

    </style>
</head>
<body>
    <h1>Add New Patron</h1>

    <%-- Display the error message if the username exists --%>
    <% if (request.getAttribute("errorMessage") != null) { %>
        <div class="error-message">
            <%= request.getAttribute("errorMessage") %>
        </div>
    <% } %>
    
    <form action="<%= request.getContextPath() %>/<%= request.getParameter("origin") %>/addPatron" method="post">
        <!-- Username -->
        <label for="username">Username:</label>
        <input type="text" id="username" name="username" required placeholder="Enter username">

        <!-- Password -->
        <label for="password">Password:</label>
        <input type="password" id="password" name="password" required placeholder="Enter password">

        <!-- Full Name -->
        <label for="fullName">Full Name:</label>
        <input type="text" id="fullName" name="fullName" required placeholder="Enter full name">

        <!-- Date of Birth -->
        <label for="dob">Date of Birth:</label>
        <input type="date" id="dob" name="dob" required>

        <!-- Type (Student or Employee) -->
        <label for="type">Type:</label>
        <select id="type" name="type" required>
            <option value="STUDENT">Student</option>
            <option value="EMPLOYEE">Employee</option>
        </select>

        <!-- Submit Button -->
        <button type="submit">Add Patron</button>
    </form>

    <div class="back-link">
        <a href="<%= request.getContextPath() %>/<%= request.getParameter("origin") %>?view=patrons">Back to Patrons List</a>
    </div>
</body>
</html>
