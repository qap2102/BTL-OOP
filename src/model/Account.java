package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public final class Account {
    private int accountID;
    private int customerID;
    private String accountNumber;
    private BigDecimal balance;
    private String pinSmart;
    private String accountStatus;
    private LocalDateTime createdAt;

    // --- [CONSTRUCTOR] Hàm khởi tạo mặc định ---
    public Account() {
        this.balance = BigDecimal.ZERO;
        this.accountStatus = "ACTIVE";
    }

    // --- [CONSTRUCTOR] Hàm khởi tạo đầy đủ (Mặc định trạng thái ACTIVE) ---
    public Account(int accountID, int customerID, String accountNumber, 
                   BigDecimal balance, String pinSmart, LocalDateTime createdAt) {
        this.accountID = accountID;
        this.customerID = customerID;
        this.accountNumber = accountNumber;
        this.balance = (balance != null) ? balance : BigDecimal.ZERO;
        this.pinSmart = pinSmart;
        this.accountStatus = "ACTIVE";
        this.createdAt = createdAt;
    }

    // --- [CONSTRUCTOR] Hàm khởi tạo đầy đủ mọi thông số ---
    public Account(int accountID, int customerID, String accountNumber, 
                   BigDecimal balance, String pinSmart, String accountStatus, 
                   LocalDateTime createdAt) {
        this.accountID = accountID;
        this.customerID = customerID;
        this.accountNumber = accountNumber;
        this.balance = (balance != null) ? balance : BigDecimal.ZERO;
        this.pinSmart = pinSmart;
        this.accountStatus = (accountStatus != null) ? accountStatus : "ACTIVE";
        this.createdAt = createdAt;
    }

    // --- Getter/Setter ---

    // [GETTER] Lấy ID tài khoản (Primary Key)
    public int getAccountID() { 
        return accountID; 
    }
    
    // [SETTER] Gán ID tài khoản
    public void setAccountID(int accountID) { 
        this.accountID = accountID; 
    }

    // [GETTER] Lấy ID khách hàng sở hữu tài khoản này
    public int getCustomerID() { 
        return customerID; 
    }
    
    // [SETTER] Gán ID khách hàng
    public void setCustomerID(int customerID) { 
        this.customerID = customerID; 
    }

    // [GETTER] Lấy Số tài khoản (String)
    public String getAccountNumber() { 
        return accountNumber; 
    }
    
    // [SETTER] Gán Số tài khoản
    public void setAccountNumber(String accountNumber) { 
        this.accountNumber = accountNumber; 
    }

    // [GETTER] Lấy Số dư hiện tại
    public BigDecimal getBalance() { 
        return balance; 
    }
    
    // [SETTER] Gán Số dư
    public void setBalance(BigDecimal balance) { 
        this.balance = (balance != null) ? balance : BigDecimal.ZERO; 
    }

    // [GETTER] Lấy Mã PIN (Lưu ý: Chỉ dùng trong nội bộ Service/DAO)
    public String getPinSmart() { 
        return pinSmart; 
    }
    
    // [SETTER] Gán Mã PIN
    public void setPinSmart(String pinSmart) { 
        this.pinSmart = pinSmart; 
    }

    // [GETTER] Lấy Trạng thái tài khoản (ACTIVE, LOCKED...)
    public String getAccountStatus() { 
        return accountStatus; 
    }
    
    // [SETTER] Gán Trạng thái tài khoản
    public void setAccountStatus(String accountStatus) { 
        this.accountStatus = accountStatus; 
    }
    
    // [GETTER] Lấy Ngày giờ tạo tài khoản
    public LocalDateTime getCreatedAt() { 
        return createdAt; 
    }
    
    // [SETTER] Gán Ngày giờ tạo tài khoản
    public void setCreatedAt(LocalDateTime createdAt) { 
        this.createdAt = createdAt; 
    }

    // --- [TO STRING] Hiển thị thông tin đối tượng (Đã ẩn mã PIN để bảo mật) ---
    @Override
    public String toString() {
        return "Account{" +
                "accountID=" + accountID +
                ", customerID=" + customerID +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", pinSmart='[HIDDEN]'" +
                ", accountStatus='" + accountStatus + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}