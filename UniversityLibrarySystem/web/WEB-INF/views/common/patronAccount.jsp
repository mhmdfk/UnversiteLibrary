<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Fine" %>
<%@ page import="models.Patron" %>
<%@ page import="models.BorrowTransaction" %>
<html>
<head>
    <title>Patron Account</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 20px;
        }
        h1 {
            color: #4CAF50;
        }
        h2 {
            color: #555;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-top: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 12px;
            text-align: left;
        }
        th {
            background-color: #4CAF50;
            color: white;
        }
        tr:nth-child(even) {
            background-color: #f2f2f2;
        }
        .total {
            font-weight: bold;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <h1>Patron Account</h1>
    
    <div style="text-align: right; margin: 10px;">
        <button onclick="window.location.href='<%= request.getContextPath()%>/auth/logout'" style="background-color: #f44336; color: white; border: none; padding: 10px 20px; border-radius: 5px; cursor: pointer;">
            Logout
        </button>
    </div>
    
    <h2>Patron Information</h2>
    <%
        Patron patron = (Patron) request.getAttribute("patron");
        if (patron != null) {
    %>
    <table>
        <tr>
            <th>ID</th>
            <th>Full Name</th>
            <th>Username</th>
            <th>Date of Birth</th>
            <th>Type</th>
        </tr>
        <tr>
            <td><%= patron.getId() %></td>
            <td><%= patron.getFullName() %></td>
            <td><%= patron.getUsername() %></td>
            <td><%= patron.getDob() %></td>
            <td><%= patron.getType() %></td>
        </tr>
    </table>
    <%
        }
    %>

    
    <h2>Outstanding Fines</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Transaction ID</th>
                <th>Amount</th>
                <th>Issued Date</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Fine> outstandingFines = (List<Fine>) request.getAttribute("outstandingFines");
                double outstandingFinesTotal = 0.0;
                if (outstandingFines != null) {
                    for (Fine fine : outstandingFines) {
                        outstandingFinesTotal += fine.getAmount();
            %>
                    <tr>
                        <td><%= fine.getId() %></td>
                        <td><%= fine.getTransactionId() %></td>
                        <td><%= fine.getAmount() %></td>
                        <td><%= fine.getIssuedDate() %></td>
                        <td><%= fine.getStatus() %></td>
                    </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
    <div class="total">Total Outstanding Fines: <%= outstandingFinesTotal %></div>

    
    <h2>Borrow History</h2>
    <table>
        <thead>
            <tr>
                <th>Transaction ID</th>
                <th>Book ID</th>
                <th>Borrow Date</th>
                <th>Due Date</th>
                <th>Return Date</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<BorrowTransaction> borrowHistory = (List<BorrowTransaction>) request.getAttribute("borrowHistory");
                if (borrowHistory != null) {
                    for (BorrowTransaction transaction : borrowHistory) {
            %>
                    <tr>
                        <td><%= transaction.getTransactionId() %></td>
                        <td><%= transaction.getBookId() %></td>
                        <td><%= transaction.getBorrowDate() %></td>
                        <td><%= transaction.getDueDate() %></td>
                        <td><%= transaction.getReturnDate() != null ? transaction.getReturnDate() : "N/A" %></td>
                        <td><%= transaction.getStatus() %></td>
                    </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
</body>
</html>