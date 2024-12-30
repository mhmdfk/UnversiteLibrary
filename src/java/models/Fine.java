
package models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Fine {
    private Long fineId;
    private BorrowTransaction transaction;
    private BigDecimal amount;
    private FineStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime paidAt;

    public enum FineStatus {
        PENDING, PAID
    }

    
    public Long getFineId() {
        return fineId;
    }
    
    public void setFineId(Long fineId) {
        this.fineId = fineId;
    }
    

    public BorrowTransaction getTransaction() {
        return transaction;
    }
    
    public void setTransaction(BorrowTransaction transaction) {
        this.transaction = transaction;
    }
    

    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    

    public FineStatus getStatus() {
        return status;
    }
    
    public void setStatus(FineStatus status) {
        this.status = status;
    }
    

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    

    public LocalDateTime getPaidAt() {
        return paidAt;
    }
    
    public void setPaidAt(LocalDateTime paidAt) {
        this.paidAt = paidAt;
    }
    
}
