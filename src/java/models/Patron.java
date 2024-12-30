
package models;

import java.time.LocalDate;

public class Patron {
    private Long patronId;
    //private User user;
    private PatronType patronType;
    private LocalDate dateOfBirth;
    private PatronStatus status;

    public enum PatronType {
        STUDENT, EMPLOYEE
    }

    public enum PatronStatus {
        ACTIVE, SUSPENDED, INACTIVE
    }

    
    public Long getPatronId() {
        return patronId;
    }
    
    public void setPatronId(Long patronId) {
        this.patronId = patronId;
    }
    

    //public User getUser() {
       // return user;
   // }
    
    //public void setUser(User user) {
        //this.user = user;
   // }
    

    public PatronType getPatronType() {
        return patronType;
    }
    
    public void setPatronType(PatronType patronType) {
        this.patronType = patronType;
    }
    

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    

    public PatronStatus getStatus() {
        return status;
    }
    
    public void setStatus(PatronStatus status) {
        this.status = status;
    }
    
}
