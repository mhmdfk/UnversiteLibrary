<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.Fine" %>
<%@ page import="java.util.List" %>

<html>
<head>
    <title>Fine List</title>
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
    <h1>Fine List</h1>
    <table>
        <thead>
            <tr>
                <th>Fine ID</th>
                <th>Amount</th>
                <th>Issued Date</th>
                <th>Paid Date</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Fine> fines = (List<Fine>) request.getAttribute("fines");
                if (fines != null) {
                    for (Fine fine : fines) { 
            %>
                    <tr>
                        <td><%= fine.getId() %></td>
                        <td><%= fine.getAmount() %></td>
                        <td><%= fine.getIssuedDate() %></td>
                        <td><%= fine.getPaidDate() != null ? fine.getPaidDate() : "Not Paid" %></td>
                        <td><%= fine.getStatus() %></td>
                        <td class="actions">
                            <% if (fine.getStatus() == Fine.FineStatus.UNPAID) { %>
                                <a href="fines?action=pay&id=<%= fine.getId() %>&patronId=<%= fine.getPatronId() %>">Pay</a>
                            <% } %>
                            <a href="fines?action=view&id=<%= fine.getId() %>&patronId=<%= fine.getPatronId() %>">View</a>
                        </td>
                    </tr>
            <% 
                    } 
                } 
            %>
        </tbody>
    </table>
</body>
</html>