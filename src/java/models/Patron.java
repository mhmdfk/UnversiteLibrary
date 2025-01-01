package models;

import java.util.ArrayList;
import java.util.List;

public class Patron {
    private Long id;
    private String username;
    private String password;
//    private String email;
    private String fullName;
    private String patronId;
    private List<Book> borrowedBooks;
    private List<Fine> fines;

    public Patron(long id, String username, String password,  String fullName) {
        this.id = id;
        this.username = username;
        this.password = password;
//        this.email = email;
        this.fullName = fullName;
        this.patronId = patronId;
        this.borrowedBooks = new ArrayList<>();
        this.fines = new ArrayList<>();
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
//
//    public String getEmail() {
//        return email;
////    }

//    public void setEmail(String email) {
//        this.email = email;
////    }
//
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPatronId() {
        return patronId;
    }

    public void setPatronId(String patronId) {
        this.patronId = patronId;
    }

    public List<Book> getBorrowedBooks() {
        return borrowedBooks;
    }

    public void setBorrowedBooks(List<Book> borrowedBooks) {
        this.borrowedBooks = borrowedBooks;
    }

    public List<Fine> getFines() {
        return fines;
    }

    public void setFines(List<Fine> fines) {
        this.fines = fines;
    }

    // Patron-specific methods
    public void borrowBook(Book book) {
        borrowedBooks.add(book);
    }

    public void returnBook(Book book) {
        borrowedBooks.remove(book);
    }
    @Override
    public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("Patron{id=").append(id)
      .append(", username='").append(username).append('\'')
      .append(", password='").append(password).append('\'') // You may choose to exclude this for security reasons
      .append(", fullName='").append(fullName).append('\'')
      .append(", patronId='").append(patronId).append('\'')
      .append(", borrowedBooks=").append(borrowedBooks)
      .append(", fines=").append(fines)
      .append('}');
    return sb.toString();
}

    
}