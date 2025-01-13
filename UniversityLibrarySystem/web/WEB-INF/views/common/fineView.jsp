<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Fine" %>

<html>
<head>
    <title>Fine Details</title>
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
        p {
            margin: 10px 0;
        }
        .actions a {
            margin-right: 10px;
            text-decoration: none;
            color: #007BFF;
            font-weight: bold;
        }
        .actions a:hover {
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <h1>Fine Details</h1>
    <%
        Fine fine = (Fine) request.getAttribute("fine");
        if (fine != null) {
    %>
        <p>Fine ID: <%= fine.getId() %></p>
        <p>Amount: <%= fine.getAmount() %></p>
        <p>Issued Date: <%= fine.getIssuedDate() %></p>
        <p>Paid Date: <%= fine.getPaidDate() != null ? fine.getPaidDate() : "Not Paid" %></p>
        <p>Status: <%= fine.getStatus() %></p>
        <div class="actions">
            <% if (fine.getStatus() == Fine.FineStatus.UNPAID) { %>
                <a href="fines?action=pay&id=<%= fine.getId() %>&patronId=<%= fine.getPatronId() %>">Pay</a>
            <% } %>
            <a href="fines?patronId=<%= fine.getPatronId() %>">Back to List</a>
        </div>
    <% } else { %>
        <p class="error-message">Fine details not found.</p>
    <% } %>
</body>
</html>