<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.BorrowTransaction" %>
<html>
<head>
    <title>Edit Borrow Status</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        h1 {
            color: #333;
            text-align: center;
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

        form select {
            width: 100%;
            padding: 8px;
            margin-bottom: 15px;
            border: 1px solid #ccc;
            border-radius: 4px;
        }

        form button {
            background-color: #28a745; /* Green color */
            color: white;
            border: none;
            padding: 10px 15px;
            border-radius: 4px;
            cursor: pointer;
            width: 100%;
        }

        form button:hover {
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
    <h1>Edit Borrow Status</h1>

    <%
        // Retrieve the BorrowTransaction object from the request
        BorrowTransaction transaction = (BorrowTransaction) request.getAttribute("transaction");
        String origin = request.getParameter("origin");
    %>

    <form action="<%= request.getContextPath() %>/<%= origin %>/updateBorrowStatus" method="post">
        <input type="hidden" name="transactionId" value="<%= transaction.getTransactionId() %>">
        
        <label for="status">Status:</label>
        <select id="status" name="status">
            <option value="ACTIVE" <%= transaction.getStatus().equals("ACTIVE") ? "selected" : "" %>>Active</option>
            <option value="RETURNED" <%= transaction.getStatus().equals("RETURNED") ? "selected" : "" %>>Returned</option>
            <option value="OVERDUE" <%= transaction.getStatus().equals("OVERDUE") ? "selected" : "" %>>Overdue</option>
        </select>
        
        <br><br>
        <button type="submit">Update Status</button>
    </form>

    
</body>
</html>
