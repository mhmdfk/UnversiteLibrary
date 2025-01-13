<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="models.BorrowTransaction" %>
<%@ page import="models.Reservation" %>
<%@ page import="java.util.List" %>
<html>
    <head>
        <title>Library Usage Statistics</title>
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
            .edit-button {
                background-color: #4CAF50;
                color: white;
                border: none;
                padding: 8px 12px;
                cursor: pointer;
                border-radius: 4px;
            }
            .edit-button:hover {
                background-color: #45a049;
            }
            .delete-button {
                background-color: #f44336; /* Red color */
                color: white;
                border: none;
                padding: 8px 12px;
                cursor: pointer;
                border-radius: 4px;
            }
            .delete-button:hover {
                background-color: #d32f2f; /* Darker red on hover */
            }
        </style>
    </head>
    <body>
        <h1>Library Usage Statistics</h1>
        <p>Active Borrowers: ${activeBorrowersCount}</p>

        <h2>Active Borrowing Transactions</h2>
        <table>
            <thead>
                <tr>
                    <th>Transaction ID</th>
                    <th>Patron ID</th>
                    <th>Book ID</th>
                    <th>Borrow Date</th>
                    <th>Due Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<BorrowTransaction> activeBorrowings = (List<BorrowTransaction>) request.getAttribute("activeBorrowings");
                    if (activeBorrowings != null) {
                        for (BorrowTransaction transaction : activeBorrowings) {
                %>
                <tr>
                    <td><%= transaction.getTransactionId()%></td>
                    <td><%= transaction.getPatronId()%></td>
                    <td><%= transaction.getBookId()%></td>
                    <td><%= transaction.getBorrowDate()%></td>
                    <td><%= transaction.getDueDate()%></td>
                    <td><%= transaction.getStatus()%></td>
                    <td>
                        <button class="edit-button" onclick="window.location.href = '<%= request.getContextPath()%>/<%=request.getParameter("origin")%>/editBorrowStatus?transactionId=<%= transaction.getTransactionId()%>'">Edit</button>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>

        <h2>Overdue Borrowing Transactions</h2>
        <table>
            <thead>
                <tr>
                    <th>Transaction ID</th>
                    <th>Patron ID</th>
                    <th>Book ID</th>
                    <th>Borrow Date</th>
                    <th>Due Date</th>
                    <th>Status</th>
                    <th>Actions</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<BorrowTransaction> overdueBorrowings = (List<BorrowTransaction>) request.getAttribute("overdueBorrowings");
                    if (overdueBorrowings != null) {
                        for (BorrowTransaction transaction : overdueBorrowings) {
                %>
                <tr>
                    <td><%= transaction.getTransactionId()%></td>
                    <td><%= transaction.getPatronId()%></td>
                    <td><%= transaction.getBookId()%></td>
                    <td><%= transaction.getBorrowDate()%></td>
                    <td><%= transaction.getDueDate()%></td>
                    <td><%= transaction.getStatus()%></td>
                    <td>
                        <button class="edit-button" onclick="window.location.href = '<%= request.getContextPath()%>/<%=request.getParameter("origin")%>/editBorrowStatus?transactionId=<%= transaction.getTransactionId()%>'">Edit</button>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>

        <h2>Active Reservations</h2>
        <table>
            <thead>
                <tr>
                    <th>Reservation ID</th>
                    <th>Patron ID</th>
                    <th>Book ID</th>
                    <th>Reservation Date</th>
                    <th>Expiration Date</th>
                    <th>Actions</th> 
                </tr>
            </thead>
            <tbody>
                <%
                    List<Reservation> activeReservations = (List<Reservation>) request.getAttribute("activeReservations");
                    if (activeReservations != null) {
                        for (Reservation reservation : activeReservations) {
                %>
                <tr>
                    <td><%= reservation.getReservationId()%></td>
                    <td><%= reservation.getPatronId()%></td>
                    <td><%= reservation.getBookId()%></td>
                    <td><%= reservation.getReservationDate()%></td>
                    <td><%= reservation.getExpirationDate() != null ? reservation.getExpirationDate() : "N/A"%></td>
                    <td>
                        
                        <button class="delete-button" 
                                onclick="confirmDelete('<%= request.getContextPath()%>/<%=request.getParameter("origin")%>/deleteReservation?reservationId=<%= reservation.getReservationId()%>')">
                            Delete
                        </button>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>

        <script>
            function confirmDelete(url) {
                if (confirm("Are you sure you want to delete this reservation?")) {
                    window.location.href = url;
                }
            }
        </script>
    </body>
</html>