package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class Transaction{
    private int transactionID;
    private Integer fromAccountID; // Dùng Integer để có thể null (trường hợp Nạp tiền)
    private Integer toAccountID;   // Dùng Integer để có thể null (trường hợp Rút tiền)
    private BigDecimal amount;
    private String transactionType;
    private String transactionContent;
    private String transactionStatus;
    private LocalDateTime createdAt;

    // --- [CONSTRUCTOR] Hàm khởi tạo mặc định ---
    public Transaction(){}

    // --- [CONSTRUCTOR] Hàm khởi tạo đầy đủ tham số ---
    public Transaction(int transactionID, Integer fromAccountID, Integer toAccountID, BigDecimal amount, String transactionType, String transactionContent, String transactionStatus, LocalDateTime createdAt){
        this.transactionID = transactionID;
        this.fromAccountID = fromAccountID;
        this.toAccountID = toAccountID;
        setAmount(amount);
        setTransactionType(transactionType);
        this.transactionContent = transactionContent;
        setTransactionStatus(transactionStatus);
        this.createdAt = createdAt;
    }

    // --- Getter/Setter ---

    // [GETTER] Lấy ID giao dịch
    public int getTransactionID(){
        return transactionID; 
    }
    
    // [SETTER] Gán ID giao dịch
    public void setTransactionID(int transactionID){ 
        this.transactionID = transactionID; 
    }

    // [GETTER] Lấy ID tài khoản gửi (Có thể null nếu là Nạp tiền)
    public Integer getFromAccountID(){ 
        return fromAccountID; 
    }
    
    // [SETTER] Gán ID tài khoản gửi
    public void setFromAccountID(Integer fromAccountID){ 
        this.fromAccountID = fromAccountID; 
    }

    // [GETTER] Lấy ID tài khoản nhận (Có thể null nếu là Rút tiền)
    public Integer getToAccountID(){ 
        return toAccountID; 
    }
    
    // [SETTER] Gán ID tài khoản nhận
    public void setToAccountID(Integer toAccountID){ 
        this.toAccountID = toAccountID; 
    }

    // [GETTER] Lấy số tiền giao dịch
    public BigDecimal getAmount(){ 
        return amount; 
    }
    
    // [SETTER] Gán số tiền giao dịch
    public void setAmount(BigDecimal amount){ 
        this.amount = amount; 
    }

    // [GETTER] Lấy loại giao dịch (DEPOSIT, WITHDRAW, TRANSFER...)
    public String getTransactionType(){ 
        return transactionType; 
    }
    
    // [SETTER] Gán loại giao dịch
    public void setTransactionType(String transactionType){ 
        this.transactionType = transactionType; 
    }

    // [GETTER] Lấy nội dung/ghi chú giao dịch
    public String getTransactionContent(){ 
        return transactionContent; 
    }
    
    // [SETTER] Gán nội dung giao dịch
    public void setTransactionContent(String transactionContent){ 
        this.transactionContent = transactionContent; 
    }

    // [GETTER] Lấy trạng thái giao dịch (SUCCESS, FAILED...)
    public String getTransactionStatus(){ 
        return transactionStatus; 
    }
    
    // [SETTER] Gán trạng thái giao dịch
    public void setTransactionStatus(String transactionStatus){ 
        this.transactionStatus = transactionStatus; 
    }

    // [GETTER] Lấy thời gian tạo giao dịch
    public LocalDateTime getCreatedAt(){ 
        return createdAt; 
    }
    
    // [SETTER] Gán thời gian tạo giao dịch
    public void setCreatedAt(LocalDateTime createdAt){ 
        this.createdAt = createdAt; 
    }
    
    // [TO STRING] Hiển thị thông tin tóm tắt giao dịch (Dùng để log/debug)
    @Override
    public String toString(){
        return String.format("[%s] %s | %.2f | %s | %s -> %s", createdAt, transactionType, amount, transactionContent, fromAccountID == null ? "-" : fromAccountID, toAccountID == null ? "-" : toAccountID);
    }
}