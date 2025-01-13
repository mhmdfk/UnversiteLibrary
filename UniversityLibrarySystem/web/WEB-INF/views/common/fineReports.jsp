<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.List" %>
<%@ page import="models.Fine" %>
<html>
<head>
    <title>Fine Reports</title>
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
        .paid-button {
            background-color: #4CAF50;
            color: white;
            border: none;
            padding: 8px 12px;
            cursor: pointer;
            border-radius: 4px;
        }
        .paid-button:hover {
            background-color: #45a049;
        }
        .total {
            font-weight: bold;
            margin-top: 10px;
        }
    </style>
</head>
<body>
    <h1>Fine Reports</h1>

    <h2>All Fines</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Patron ID</th>
                <th>Transaction ID</th>
                <th>Amount</th>
                <th>Issued Date</th>
                <th>Paid Date</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Fine> allFines = (List<Fine>) request.getAttribute("allFines");
                double allFinesTotal = 0.0; 
                if (allFines != null) {
                    for (Fine fine : allFines) {
                        allFinesTotal += fine.getAmount(); 
            %>
                    <tr>
                        <td><%= fine.getId() %></td>
                        <td><%= fine.getPatronId() %></td>
                        <td><%= fine.getTransactionId() %></td>
                        <td><%= fine.getAmount() %></td>
                        <td><%= fine.getIssuedDate() %></td>
                        <td><%= fine.getPaidDate() != null ? fine.getPaidDate() : "N/A" %></td>
                        <td><%= fine.getStatus() %></td>
                        <td>
                            <% if (fine.getStatus() == Fine.FineStatus.UNPAID) { %>
                                <form action="<%= request.getContextPath() %>/<%=request.getParameter("origin")%>/payFine" method="post" style="display: inline;">
                                    <input type="hidden" name="fineId" value="<%= fine.getId() %>">
                                    <button type="submit" class="paid-button">Mark as Paid</button>
                                </form>
                            <% } %>
                        </td>
                    </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
    <div class="total">Total Amount: <%= allFinesTotal %> JOD</div>

    <h2>Outstanding Fines</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Patron ID</th>
                <th>Transaction ID</th>
                <th>Amount</th>
                <th>Issued Date</th>
                <th>Status</th>
                <th>Actions</th>
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
                        <td><%= fine.getPatronId() %></td>
                        <td><%= fine.getTransactionId() %></td>
                        <td><%= fine.getAmount() %></td>
                        <td><%= fine.getIssuedDate() %></td>
                        <td><%= fine.getStatus() %></td>
                        <td>
                            <% if (fine.getStatus() == Fine.FineStatus.UNPAID) { %>
                                <form action="<%= request.getContextPath() %>/<%=request.getParameter("origin")%>/payFine" method="post" style="display: inline;">
                                    <input type="hidden" name="fineId" value="<%= fine.getId() %>">
                                    <button type="submit" class="paid-button">Mark as Paid</button>
                                </form>
                            <% } %>
                        </td>
                    </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
    <div class="total">Total Amount: <%= outstandingFinesTotal %> JOD</div>

    <h2>Overdue Fines</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Patron ID</th>
                <th>Transaction ID</th>
                <th>Amount</th>
                <th>Issued Date</th>
                <th>Status</th>
                <th>Actions</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Fine> overdueFines = (List<Fine>) request.getAttribute("overdueFines");
                double overdueFinesTotal = 0.0; 
                if (overdueFines != null) {
                    for (Fine fine : overdueFines) {
                        overdueFinesTotal += fine.getAmount(); 
            %>
                    <tr>
                        <td><%= fine.getId() %></td>
                        <td><%= fine.getPatronId() %></td>
                        <td><%= fine.getTransactionId() %></td>
                        <td><%= fine.getAmount() %></td>
                        <td><%= fine.getIssuedDate() %></td>
                        <td><%= fine.getStatus() %></td>
                        <td>
                            <% if (fine.getStatus() == Fine.FineStatus.UNPAID) { %>
                                <form action="<%= request.getContextPath() %>/<%=request.getParameter("origin")%>/payFine" method="post" style="display: inline;">
                                    <input type="hidden" name="fineId" value="<%= fine.getId() %>">
                                    <button type="submit" class="paid-button">Mark as Paid</button>
                                </form>
                            <% } %>
                        </td>
                    </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
    <div class="total">Total Amount: <%= overdueFinesTotal %> JOD</div>

    <h2>Collected Fines</h2>
    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Patron ID</th>
                <th>Transaction ID</th>
                <th>Amount</th>
                <th>Issued Date</th>
                <th>Paid Date</th>
                <th>Status</th>
            </tr>
        </thead>
        <tbody>
            <% 
                List<Fine> collectedFines = (List<Fine>) request.getAttribute("collectedFines");
                double collectedFinesTotal = 0.0; 
                if (collectedFines != null) {
                    for (Fine fine : collectedFines) {
                        collectedFinesTotal += fine.getAmount(); 
            %>
                    <tr>
                        <td><%= fine.getId() %></td>
                        <td><%= fine.getPatronId() %></td>
                        <td><%= fine.getTransactionId() %></td>
                        <td><%= fine.getAmount() %></td>
                        <td><%= fine.getIssuedDate() %></td>
                        <td><%= fine.getPaidDate() != null ? fine.getPaidDate() : "N/A" %></td>
                        <td><%= fine.getStatus() %></td>
                    </tr>
            <%
                    }
                }
            %>
        </tbody>
    </table>
    <div class="total">Total Amount: <%= collectedFinesTotal %> JOD</div>
</body>
</html>