package model;

import java.time.LocalDateTime;

public final class Admin { 
    private int adminID;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String role;
    private LocalDateTime createdAt;
    
    // --- [CONSTRUCTOR] Hàm khởi tạo mặc định ---
    public Admin() {}
    
    // --- [CONSTRUCTOR] Hàm khởi tạo đầy đủ tham số ---
    public Admin(int adminID, String username, String passwordHash, String fullName, 
                 String email, String role, LocalDateTime createdAt) {
        this.adminID = adminID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
    }

    // --- Getter/Setter ---

    // [GETTER] Lấy ID quản trị viên
    public int getAdminID() {
        return adminID;
    }

    // [SETTER] Gán ID quản trị viên
    public void setAdminID(int adminID) {
        this.adminID = adminID;
    }

    // [GETTER] Lấy tên đăng nhập
    public String getUsername() {
        return username;
    }

    // [SETTER] Gán tên đăng nhập
    public void setUsername(String username) {
        this.username = username;
    }

    // [GETTER] Lấy mật khẩu đã mã hóa
    // [FIX BẢO MẬT] Giữ lại hàm cần thiết cho DAO/Service nhưng không dùng setter/getter công khai
    public String getPasswordHash() {
        return passwordHash;
    }

    // [SETTER] Gán mật khẩu đã mã hóa
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    // [GETTER] Lấy họ và tên đầy đủ
    public String getFullName() {
        return fullName;
    }

    // [SETTER] Gán họ và tên đầy đủ
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    // [GETTER] Lấy địa chỉ Email
    public String getEmail() {
        return email;
    }

    // [SETTER] Gán địa chỉ Email
    public void setEmail(String email) {
        this.email = email;
    }

    // [GETTER] Lấy vai trò (Ví dụ: SUPER_ADMIN, MANAGER...)
    public String getRole() {
        return role;
    }

    // [SETTER] Gán vai trò
    public void setRole(String role) {
        this.role = role;
    }

    // [GETTER] Lấy ngày giờ tạo tài khoản
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // [SETTER] Gán ngày giờ tạo tài khoản
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
}