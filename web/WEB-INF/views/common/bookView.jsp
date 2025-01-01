<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Book Details</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 20px;
        }

        h1 {
            color: #333;
            font-size: 2em;
            margin-bottom: 20px;
        }

        h2 {
            color: #444;
            font-size: 1.5em;
            margin-top: 40px;
        }

        p {
            font-size: 1.1em;
            line-height: 1.5;
            margin: 10px 0;
        }

        table {
            width: 100%;
            margin-top: 20px;
            border-collapse: collapse;
        }

        table th, table td {
            padding: 8px;
            text-align: left;
            border: 1px solid #ddd;
        }

        table th {
            background-color: #f8f8f8;
        }

        table tr:nth-child(even) {
            background-color: #f9f9f9;
        }

        table tr:hover {
            background-color: #f1f1f1;
        }

        form {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin-top: 20px;
        }

        form input[type="text"], form input[type="number"] {
            width: 100%;
            padding: 10px;
            margin: 10px 0;
            border: 1px solid #ccc;
            border-radius: 4px;
            font-size: 1em;
        }

        form input[type="submit"] {
            background-color: #007bff;
            color: white;
            border: none;
            padding: 12px 20px;
            font-size: 1.1em;
            cursor: pointer;
            border-radius: 4px;
        }

        form input[type="submit"]:hover {
            background-color: #0056b3;
        }

        .book-details {
            background-color: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            margin-bottom: 40px;
        }

        .book-details p {
            font-size: 1.1em;
            margin: 10px 0;
        }

    </style>
</head>
<body>
    <h1>Book Details</h1>

    <div class="book-details">
        <p>ID: ${book.id}</p>
        <p>Title: ${book.title}</p>
        <p>Author: ${book.author}</p>
        <p>Genre: ${book.genre}</p>
        <p>Publication Year: ${book.publicationYear}</p>
        <p>Total Copies: ${book.totalCopies}</p>
        <p>Available Copies: ${book.availableCopies}</p>
        <p>Created At: ${book.createdAt}</p>
        <p>Updated At: ${book.updatedAt}</p>
    </div>

<!--    <h2>Update Book</h2>
    <form action="books" method="post">
        <input type="hidden" name="_method" value="PUT">
        <input type="hidden" name="id" value="${book.id}">
        ISBN: <input type="text" name="isbn" value="${book.isbn}" required><br>
        Title: <input type="text" name="title" value="${book.title}" required><br>
        Author: <input type="text" name="author" value="${book.author}" required><br>
        Genre: <input type="text" name="genre" value="${book.genre}" required><br>
        Publication Year: <input type="number" name="publicationYear" value="${book.publicationYear}" required><br>
        Total Copies: <input type="number" name="totalCopies" value="${book.totalCopies}" required><br>
        Available Copies: <input type="number" name="availableCopies" value="${book.availableCopies}" required><br>
        <input type="submit" value="Update Book">
    </form>-->
</body>
</html>
