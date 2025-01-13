<%@page import="models.Patron" %>
<%@page import="models.Rating"%>
<%@page import="models.Comment"%>
<%@page import="models.Book" %>
<%@page import="models.BorrowTransaction" %>
<%@page import="java.util.List" %>
<%@page import="java.sql.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <head>
        <title>Book Details</title>
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

            /* Paragraph Styles */
            p {
                margin: 10px 0;
                font-size: 16px;
                line-height: 1.6;
            }

            /* Container Styles */
            .container {
                background-color: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
                margin-bottom: 40px;
            }

            /* Button Styles */
            .rent-button, .reserve-button {
                background-color: #28a745; /* Green background */
                color: white;
                padding: 8px 16px;
                border: none;
                cursor: pointer;
                font-size: 14px;
                border-radius: 5px;
                transition: background-color 0.3s ease;
                margin-right: 10px;
            }

            .rent-button:hover, .reserve-button:hover {
                background-color: #218838; /* Darker green on hover */
            }

            /* Error and Success Message Styles */
            .error-message {
                color: #dc3545;
                font-weight: bold;
                margin-top: 20px;
                text-align: center;
            }

            .success-message {
                background-color: #d4edda;
                color: #155724;
                padding: 10px;
                border-radius: 5px;
                margin-bottom: 20px;
                text-align: center;
            }

            /* Comment Section Styles */
            ul {
                list-style-type: none;
            }

            ul li {
                background-color: #f1f1f1;
                margin-bottom: 10px;
                padding: 15px;
                border-radius: 5px;
            }

            ul li p {
                margin: 5px 0;
                font-size: 14px;
            }

            /* Form Styles */
            form {
                margin-top: 20px;
            }

            textarea {
                width: 100%;
                padding: 10px;
                border-radius: 5px;
                border: 1px solid #ccc;
                resize: vertical;
            }

            select {
                width: 100%;
                padding: 10px;
                border-radius: 5px;
                border: 1px solid #ccc;
                margin-bottom: 10px;
            }

            /* Input Fields */
            input[type="hidden"] {
                display: none;
            }

            /* Responsive Design */
            @media (max-width: 768px) {
                body {
                    padding: 15px;
                }

                .container {
                    padding: 15px;
                }

                h1 {
                    font-size: 28px;
                }

                .rent-button, .reserve-button {
                    padding: 8px 16px;
                    font-size: 14px;
                }

                textarea {
                    width: 100%;
                }
            }
        </style>
    </head>

    <body>
        <h1>Book Details</h1>
        <div class="container">
            <%
                Book book = (Book) request.getAttribute("book");
                Integer patronId = (Integer) session.getAttribute("id");
                if (book != null) {
                    boolean isBorrowedByUser = false;
                    try {
                        List<BorrowTransaction> borrowings = BorrowTransaction.getBorrowingHistory(patronId);
                        for (BorrowTransaction transaction : borrowings) {
                            if (transaction.getBookId().equals(book.getId()) && 
                                (transaction.getStatus() == BorrowTransaction.TransactionStatus.ACTIVE || 
                                 transaction.getStatus() == BorrowTransaction.TransactionStatus.OVERDUE)) {
                                isBorrowedByUser = true;
                                break;
                            }
                        }
                    } catch (SQLException e) {
                        throw new ServletException("Database connection error", e);
                    }
            %>
            <p><strong>ID:</strong> <%= book.getId() %></p>
            <p><strong>Title:</strong> <%= book.getTitle() %></p>
            <p><strong>Author:</strong> <%= book.getAuthor() %></p>
            <p><strong>Genre:</strong> <%= book.getGenre() %></p>
            <p><strong>Publication Year:</strong> <%= book.getPublicationYear() %></p>
            <p><strong>Status:</strong> <%= book.getStatus() %></p>
            <p><strong>Created At:</strong> <%= book.getCreatedAt() %></p>
            <p><strong>Updated At:</strong> <%= book.getUpdatedAt() %></p>
            
            <% if (book.isAvailable()) { %>
            <form action="<%= request.getContextPath() %>/borrow" method="post">
                <input type="hidden" name="bookId" value="<%= book.getId() %>">
                <button type="submit" class="rent-button">Borrow Book</button>
            </form>
            <% } else if (book.getStatus() == Book.BookStatus.BORROWED && !isBorrowedByUser) { %>
            <form action="<%= request.getContextPath() %>/reserve" method="post">
                <input type="hidden" name="bookId" value="<%= book.getId() %>">
                <button type="submit" class="reserve-button">Reserve Book</button>
            </form>
            <% } else if (isBorrowedByUser) { %>
            <p class="error-message">You have already borrowed this book. You cannot reserve it.</p>
            <% } else { %>
            <p class="error-message">This book is currently unavailable for rent.</p>
            <% } %>
            
            <!-- Display Average Rating -->
            <p><strong>Average Rating:</strong> <%= Rating.getAverageRatingByBookId(book.getId()) %></p>

            <!-- Display Comments -->
            <h2>Comments</h2>
            <ul>
                <% for (Comment comment : Comment.getCommentsByBookId(book.getId())) { %>
                <li>
                    <p><%= comment.getComment() %></p>
                    <p><strong>By Patron ID:</strong> <%= comment.getPatronId() %> on <%= comment.getCreatedAt() %></p>
                </li>
                <% } %>
            </ul>

            <!-- Add Comment Form -->
            <h2>Leave a Comment</h2>
            <form action="<%= request.getContextPath() %>/comment" method="post">
                <input type="hidden" name="bookId" value="<%= book.getId() %>">
                <input type="hidden" name="patronId" value="<%= session.getAttribute("patronId") %>">
                <textarea name="comment" rows="4" cols="50" required></textarea><br>
                <button type="submit" class="rent-button">Submit Comment</button>
            </form>

            <!-- Add Rating Form -->
            <h2>Rate this Book</h2>
            <form action="<%= request.getContextPath() %>/rating" method="post">
                <input type="hidden" name="bookId" value="<%= book.getId() %>">
                <input type="hidden" name="patronId" value="<%= session.getAttribute("patronId") %>">
                <select name="rating" required>
                    <option value="1">1</option>
                    <option value="2">2</option>
                    <option value="3">3</option>
                    <option value="4">4</option>
                    <option value="5">5</option>
                </select><br>
                <button type="submit" class="rent-button">Submit Rating</button>
            </form>

            <!-- Share Book Form -->
            <h2>Share this Book</h2>
            <form action="<%= request.getContextPath() %>/shared-books" method="post">
                <input type="hidden" name="action" value="share">
                <input type="hidden" name="bookId" value="<%= book.getId() %>">
                <input type="hidden" name="sharedByUserId" value="<%= session.getAttribute("id") %>">
                <label for="sharedWithUserId">Share with:</label>
                <select id="sharedWithUserId" name="sharedWithUserId" required>
                    <option value="">Select a patron</option>
                    <% for (Patron patron : (List<Patron>) request.getAttribute("patrons")) { %>
                    <option value="<%= patron.getId() %>"><%= patron.getFullName() %></option>
                    <% } %>
                </select><br>
                <button type="submit" class="rent-button">Share Book</button>
            </form>
            <% 
                String successMessage = (String) request.getAttribute("successMessage");
                if (successMessage != null) {
            %>
            <div class="success-message">
                <%= successMessage %>
            </div>
            <% } %>

            <% if (request.getParameter("error") != null) { %>
            <p class="error-message">
                <%
                    String error = request.getParameter("error");
                    switch (error) {
                        case "unavailable":
                            out.println("This book is currently unavailable for borrow.");
                            break;
                        case "reserved":
                            out.println("This book is already reserved.");
                            break;
                        case "maxBooksReached":
                            out.println("You can't Borrow more than three books.");
                            break;
                        default:
                            out.println("An error occurred. Please try again.");
                    }
                %>
            </p>
            <% } %>
            <% } else { %>
            <p class="error-message">Book details not found.</p>
            <% } %>
        </div>
    </body>
</html>