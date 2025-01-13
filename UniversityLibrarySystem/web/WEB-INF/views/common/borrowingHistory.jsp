<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.BorrowTransaction" %>
<%@ page import="models.Fine" %>
<%@ page import="java.util.List" %>

<!DOCTYPE html>
<html>
<head>
    <title>Borrowing History</title>
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
        tr:hover {
            background-color: #f1f1f1;
        }
    </style>
</head>
<body>
    <h1>Borrowing History</h1>
    <table>
        <thead>
            <tr>
                <th>Transaction ID</th>
                <th>Book ID</th>
                <th>Borrow Date</th>
                <th>Due Date</th>
                <th>Return Date</th>
                <th>Status</th>
                <th>Pending Fines</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<BorrowTransaction> history = (List<BorrowTransaction>) request.getAttribute("borrowingHistory");
                if (history != null) {
                    for (BorrowTransaction transaction : history) { 
                        List<Fine> pendingFines = Fine.getPendingFinesByTransactionId(transaction.getTransactionId());
                        double totalPendingFines = pendingFines.stream().mapToDouble(Fine::getAmount).sum();
            %>
                    <tr>
                        <td><%= transaction.getTransactionId() %></td>
                        <td><%= transaction.getBookId() %></td>
                        <td><%= transaction.getBorrowDate() %></td>
                        <td><%= transaction.getDueDate() %></td>
                        <td><%= transaction.getReturnDate() != null ? transaction.getReturnDate() : "Not Returned" %></td>
                        <td><%= transaction.getStatus() %></td>
                        <td><%= totalPendingFines > 0 ? totalPendingFines + " JD" : "No Pending Fines" %></td>
                    </tr>
            <% 
                    } 
                }else{%>
                <h1>There's no history yet</h1>
                <%} 
            %>
        </tbody>
    </table>
    <a href="<%= request.getContextPath() %>/<%=request.getParameter("origin")%>">Back to Dashboard</a>
</body>
</html>