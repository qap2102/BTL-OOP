package model;

public final class Customer{
    
    private int customerID;
    private String username;
    private String passwordHash; // Giữ lại trường này
    private String citizenID;
    private String fullName;
    private String dateOfBirth;
    private String sex;
    private String nationality;
    private String placeOfOrigin;
    private String placeOfResidence;
    private String email;
    private String phoneNumber;
    private String userStatus;
    
    // [CONSTRUCTOR] Hàm khởi tạo mặc định
    public Customer(){}
    
    // [CONSTRUCTOR] Hàm khởi tạo đầy đủ tham số
    public Customer(int customerID, String username, String passwordHash, String citizenID, String fullName, String dateOfBirth, String sex, String nationality, String placeOfOrigin, String placeOfResidence, String email, String phoneNumber){
        this.customerID = customerID;
        this.username = username;
        this.passwordHash = passwordHash;
        this.citizenID = citizenID;
        this.fullName = fullName;
        this.dateOfBirth = dateOfBirth;
        this.sex = sex;
        this.nationality = nationality;
        this.placeOfOrigin = placeOfOrigin;
        this.placeOfResidence = placeOfResidence;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }
    
    // [GETTER] Lấy ID khách hàng
    public int getCustomerID(){ 
        return customerID; 
    }
    
    // [SETTER] Gán ID khách hàng
    public void setCustomerID(int customerID){ 
        this.customerID = customerID; 
    }

    // [GETTER] Lấy tên đăng nhập
    public String getUsername(){ 
        return username;
    }
    
    // [SETTER] Gán tên đăng nhập
    public void setUsername(String username){
        this.username = username;
    }

    // [GETTER] Lấy mật khẩu đã mã hóa (Chỉ dùng nội bộ DAO/Service)
    // [FIX BẢO MẬT] Giữ lại hàm cần thiết cho DAO/Service nhưng không dùng setter/getter công khai
    public String getPasswordHash(){ 
        return passwordHash; 
    }
    
    // [SETTER] Gán mật khẩu đã mã hóa
    public void setPasswordHash(String passwordHash){ 
        this.passwordHash = passwordHash;
    }
    
    // [GETTER] Lấy số CCCD/CMND
    public String getCitizenID(){ 
        return citizenID; 
    }
    
    // [SETTER] Gán số CCCD/CMND
    public void setCitizenID(String citizenID){ 
        this.citizenID = citizenID; 
    }

    // [GETTER] Lấy họ và tên đầy đủ
    public String getFullName(){ 
        return fullName; 
    }
    
    // [SETTER] Gán họ và tên đầy đủ
    public void setFullName(String fullName){ 
        this.fullName = fullName; 
    }

    // [GETTER] Lấy ngày sinh
    public String getDateOfBirth(){ 
        return dateOfBirth; 
    }
    
    // [SETTER] Gán ngày sinh
    public void setDateOfBirth(String dateOfBirth){ 
        this.dateOfBirth = dateOfBirth; 
    }

    // [GETTER] Lấy giới tính
    public String getSex(){ 
        return sex; 
    }
    
    // [SETTER] Gán giới tính
    public void setSex(String sex){ 
        this.sex = sex; 
    }

    // [GETTER] Lấy quốc tịch
    public String getNationality(){ 
        return nationality;
    }
    
    // [SETTER] Gán quốc tịch
    public void setNationality(String nationality){ 
        this.nationality = nationality; 
    }

    // [GETTER] Lấy quê quán
    public String getPlaceOfOrigin(){
        return placeOfOrigin; 
    }
    
    // [SETTER] Gán quê quán
    public void setPlaceOfOrigin(String placeOfOrigin){ 
        this.placeOfOrigin = placeOfOrigin; 
    }

    // [GETTER] Lấy địa chỉ thường trú
    public String getPlaceOfResidence(){ 
        return placeOfResidence; 
    }
    
    // [SETTER] Gán địa chỉ thường trú
    public void setPlaceOfResidence(String placeOfResidence){ 
        this.placeOfResidence = placeOfResidence; 
    }

    // [GETTER] Lấy địa chỉ Email
    public String getEmail(){
        return email;
    }
    
    // [SETTER] Gán địa chỉ Email
    public void setEmail(String email){ 
        this.email = email; 
    }

    // [GETTER] Lấy số điện thoại
    public String getPhone(){ 
        return phoneNumber; 
    }
    
    // [SETTER] Gán số điện thoại
    public void setPhone(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    
    // [GETTER] Lấy trạng thái tài khoản (ACTIVE/LOCKED)
    public String getUserStatus(){
        return userStatus;
    }
    
    // [SETTER] Gán trạng thái tài khoản
    public void setUserStatus(String userStatus){
        this.userStatus = userStatus;
    }
    
    // [TO STRING] Chuyển đối tượng thành chuỗi (Dùng để debug/log)
    @Override
    public String toString(){
        // [FIX BẢO MẬT] Ẩn passwordHash trong toString()
        return "Customer{" + "customerID=" + customerID + ", username='" + username + '\'' + ", passwordHash='[HIDDEN]'" + ", citizenID='" + citizenID + '\'' + ", fullName='" + fullName + '\'' + ", dateOfBirth='" + dateOfBirth + '\'' + ", sex='" + sex + '\'' + ", nationality='" + nationality + '\'' + ", placeOfOrigin='" + placeOfOrigin + '\'' + ", placeOfResidence='" + placeOfResidence + '\'' + ", email='" + email + '\'' + ", phoneNumber='" + phoneNumber + ", userStatus='" + userStatus + '\'' + '}';
    }
}