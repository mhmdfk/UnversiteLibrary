<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Return Book</title>
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
form input[type="text"] {
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
    <h1>Return Book</h1>
    <form action="<%= request.getContextPath() %>/return" method="post">
        <!-- Transaction ID -->
        <label for="transactionId">Transaction ID:</label>
        <input type="text" id="transactionId" name="transactionId" placeholder="Enter Transaction ID" required>

        <!-- Submit Button -->
        <button type="submit">Return Book</button>
    </form>
</body>
</html>
