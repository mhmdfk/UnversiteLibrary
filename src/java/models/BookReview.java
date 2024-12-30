
package models;

import java.time.LocalDateTime;

public class BookReview {
    private Long reviewId;
    private Book book;
    //private Patron patron;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;

    
    public Long getReviewId() {
        return reviewId;
    }
    
    public void setReviewId(Long reviewId) {
        this.reviewId = reviewId;
    }
    

    public Book getBook() {
        return book;
    }
    
    public void setBook(Book book) {
        this.book = book;
    }
    

   // public Patron getPatron() {
       // return patron;
    //}
    
    //public void setPatron(Patron patron) {
        //this.patron = patron;
   // }
    

    public Integer getRating() {
        return rating;
    }
    
    public void setRating(Integer rating) {
        this.rating = rating;
    }
    

    public String getComment() {
        return comment;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}
