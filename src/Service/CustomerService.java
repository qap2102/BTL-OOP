package Service;

import DataAccessObject.*;
import database.DatabaseConnection;
import model.*;
import java.sql.*;
import java.math.BigDecimal;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class CustomerService {
    
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    
    // [ĐĂNG KÝ] Xử lý đầy đủ quy trình: Validate -> Tạo Customer -> Tạo Account mặc định
    public Customer registerNewUserFullProcess(String username, String fullName, String password, String citizenID, 
                                           String email, String phone, String address, 
                                           String dateOfBirth, String pin) throws SQLException {
        
        // Chuyển đổi định dạng ngày sinh
        try {
            if (dateOfBirth.contains("/")) {
                SimpleDateFormat userFormat = new SimpleDateFormat("dd/MM/yyyy");
                SimpleDateFormat mySQLFormat = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date date = userFormat.parse(dateOfBirth);
                dateOfBirth = mySQLFormat.format(date);
            }
        } catch (Exception e) {
            System.out.println("Lỗi convert ngày (giữ nguyên giá trị cũ): " + e.getMessage());
        }

        // 1. Kiểm tra trùng lặp thông tin
        if(customerDAO.findByRegister(username, citizenID, email, phone) != null){
            throw new IllegalArgumentException("Tên đăng nhập, CCCD, Email hoặc SĐT đã tồn tại.");
        }
        
        // 2. Thiết lập dữ liệu mặc định
        String sex = "OTHER";
        String nationality = "Việt Nam";
        String placeOfOrigin = address;
        String placeOfResidence = address;
        
        // 3. Gọi DAO để tạo khách hàng
        Customer newCus = customerDAO.registerCustomer(username, password, citizenID, fullName, 
                                             dateOfBirth, sex, nationality, placeOfOrigin, 
                                             placeOfResidence, email, phone);
        
        // 4. Tạo tài khoản ngân hàng mặc định (Tặng 50k)
        if (newCus != null) {
            // Sử dụng SĐT làm số tài khoản (hoặc random nếu SĐT lỗi)
            String accountNumber = (phone == null || phone.isEmpty()) ? 
                    String.valueOf(System.currentTimeMillis()).substring(5) : phone;
            
            BigDecimal initialBalance = new BigDecimal("50000");
            
            accountDAO.createAccount(
                newCus.getCustomerID(), 
                accountNumber, 
                pin, 
                initialBalance
            );
        }
        return newCus;
    }

    // [ĐĂNG KÝ] Hàm cơ bản chỉ tạo Customer (Ít dùng, chủ yếu dùng hàm FullProcess ở trên)
    public Customer registerCustomer(String username, String passwordHash, String citizenID, String fullName, String dateOfBirth, String sex, String nationality, String placeOfOrigin, String placeOfResidence, String email, String phone) throws SQLException{
        if(customerDAO.findByRegister(username, citizenID, email, phone) != null){
            throw new IllegalArgumentException("Thông tin đã tồn tại trong hệ thống.");
        }
        return customerDAO.registerCustomer(username, passwordHash, citizenID, fullName, dateOfBirth, sex, nationality, placeOfOrigin, placeOfResidence, email, phone);
    }
    
    // [ĐĂNG NHẬP] Kiểm tra user/pass và trạng thái khóa
    public Customer login(String username, String password) throws SQLException{
        Customer cus = customerDAO.findByLogin(username, password);
        if(cus == null){
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng.");
        }
        if("LOCKED".equalsIgnoreCase(cus.getUserStatus())){
            throw new IllegalArgumentException("Tài khoản đã bị khóa.");
        }
        return cus;
    }
    
    // [CẬP NHẬT] Sửa thông tin cá nhân
    public void updateInfor(Customer customer) throws SQLException{
        if(customerDAO.findByID(customer.getCustomerID()) == null){
            throw new IllegalArgumentException("Tài khoản không tồn tại.");
        }
        customerDAO.update(customer);
    }
    
    // [ADMIN] Cập nhật trạng thái (Khóa/Mở khóa)
    public void updateStatus(int id, String status) throws SQLException{
        if(customerDAO.findByID(id) == null){
            throw new IllegalArgumentException("Tài khoản không tồn tại.");
        }
        customerDAO.updateStatus(id, status);
    }
    
    // [ADMIN] Xóa tài khoản
    public void delete(int id) throws SQLException{
        if(customerDAO.findByID(id) == null){
            throw new IllegalArgumentException("Tài khoản không tồn tại.");
        }
        customerDAO.delete(id);
    }
    
    // [ADMIN] Lấy danh sách toàn bộ khách hàng
    public List<Customer> getAllCustomer() throws SQLException{
        return customerDAO.getAllCustomers();
    }

    // [ĐỔI MẬT KHẨU] Đổi pass khi đã đăng nhập (Cần mật khẩu cũ)
    public boolean changePassword(Customer customer, String oldPass, String newPass) throws SQLException{
        if(!customer.getPasswordHash().equals(oldPass)){
            throw new IllegalArgumentException("Mật khẩu cũ không đúng!");
        }
        return customerDAO.updatePassword(customer.getCustomerID(), newPass); 
    }

    // [GIAO DỊCH] Lấy lịch sử theo Username
    public List<Transaction> getTransactionHistory(String username) throws SQLException {
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("Username không hợp lệ!");
        }
        return transactionDAO.getTransactionsByUsername(username);
    }
    
    // [GIAO DỊCH] Rút tiền mặt
    public void withdraw(String username, long amountVal) throws Exception {
        if (amountVal <= 0) {
            throw new IllegalArgumentException("Số tiền rút phải lớn hơn 0!");
        }
        
        BigDecimal amount = BigDecimal.valueOf(amountVal); 

        Account account = accountDAO.getAccountByUsername(username);
        if (account == null) {
            throw new IllegalArgumentException("Tài khoản không tồn tại!");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Số dư không đủ!");
        }

        BigDecimal newBalance = account.getBalance().subtract(amount);
        account.setBalance(newBalance);

        accountDAO.updateBalance(account.getAccountID(), account.getBalance()); 

        accountDAO.insertTransaction(account.getAccountID(), null, amount, "Rút tiền", "Rút tiền mặt", "SUCCESS");
    }

    // [QUÊN MẬT KHẨU] Reset pass bằng CCCD + SĐT (Không cần pass cũ, nhưng check trùng pass cũ)
    public void resetPassword(String cccd, String phone, String newPass) throws SQLException {
        // Bước 1: Tìm khách hàng xác thực
        Customer c = customerDAO.findByCccdAndPhone(cccd, phone);
        if (c == null) {
            throw new IllegalArgumentException("Thông tin CCCD hoặc Số điện thoại không chính xác!");
        }

        // Bước 2: Kiểm tra mật khẩu mới không được trùng mật khẩu cũ
        if (c.getPasswordHash().equals(newPass)) {
            throw new IllegalArgumentException("Mật khẩu mới không được trùng với mật khẩu cũ!");
        }
        
        // Bước 3: Cập nhật
        boolean success = customerDAO.updatePassword(c.getCustomerID(), newPass);
        if (!success) {
            throw new SQLException("Lỗi hệ thống: Không thể cập nhật mật khẩu.");
        }
    }

    // [GIAO DỊCH] Lấy lịch sử theo CustomerID (Dùng cho Admin)
    public List<Transaction> getTransactionHistoryByCustomerId(int customerId) {
        List<Transaction> transactions = new ArrayList<>();
        
        String sql = """
            SELECT t.* FROM transactions t
            WHERE t.from_account_id IN (SELECT account_id FROM accounts WHERE customer_id = ?)
               OR t.to_account_id IN (SELECT account_id FROM accounts WHERE customer_id = ?)
            ORDER BY t.created_at DESC
            LIMIT 100
            """;
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, customerId);
            pstmt.setInt(2, customerId);
            
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setTransactionID(rs.getInt("transaction_id"));
                transaction.setTransactionType(rs.getString("transaction_type"));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setFromAccountID(rs.getInt("from_account_id"));
                transaction.setToAccountID(rs.getInt("to_account_id"));
                transaction.setTransactionContent(rs.getString("transaction_content"));
                
                Timestamp timestamp = rs.getTimestamp("created_at");
                if (timestamp != null) {
                    transaction.setCreatedAt(timestamp.toLocalDateTime());
                }
                
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi lấy lịch sử giao dịch: " + e.getMessage());
        }
        
        return transactions;
    }
}