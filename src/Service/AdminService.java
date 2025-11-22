package Service;

import DataAccessObject.*;
import model.Admin;
import model.Account;
import model.Customer;
import model.Transaction;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class AdminService {

    private final AdminDAO adminDAO = new AdminDAO();
    private final AccountDAO accountDAO = new AccountDAO();
    private final CustomerDAO customerDAO = new CustomerDAO();
    private final TransactionDAO transactionDAO = new TransactionDAO();
    
    // === CÁC METHOD CƠ BẢN ===

    // [ĐĂNG NHẬP] Xác thực Admin
    public Admin login(String username, String password) throws SQLException {
        Admin admin = adminDAO.findByLogin(username, password);
        if (admin == null) {
            throw new IllegalArgumentException("Tên đăng nhập hoặc mật khẩu không đúng.");
        }
        return admin;
    }
    
    // [QUẢN TRỊ] Khóa/Mở khóa tài khoản ngân hàng
    public void toggleAccountStatus(int accountId, boolean isLock) throws SQLException {
        String status = isLock ? "LOCKED" : "ACTIVE";
        if (accountDAO.findByID(accountId) == null) {
            throw new IllegalArgumentException("Tài khoản không tồn tại.");
        }
        accountDAO.updateStatus(accountId, status);
    }
    
    // [QUẢN TRỊ] Khóa/Mở khóa người dùng (User)
    public void toggleCustomerStatus(int customerId, boolean isLock) throws SQLException {
        String status = isLock ? "LOCKED" : "ACTIVE";
        if (customerDAO.findByID(customerId) == null) {
             throw new IllegalArgumentException("Khách hàng không tồn tại.");
        }
        customerDAO.updateStatus(customerId, status);
    }
    
    // [THỐNG KÊ] Lấy tổng số lượng khách hàng
    public int getTotalCustomers() throws SQLException {
        return customerDAO.getAllCustomers().size();
    }
    
    // [THỐNG KÊ] Lấy tổng số dư toàn hệ thống (Tổng tiền trong ngân hàng)
    public BigDecimal getTotalSystemBalance() throws SQLException {
        List<Account> accounts = accountDAO.findAccountAll();
        BigDecimal total = BigDecimal.ZERO;
        for (Account acc : accounts) {
            total = total.add(acc.getBalance());
        }
        return total;
    }
    
    // [THỐNG KÊ] Lấy danh sách giao dịch gần đây (Cho Dashboard)
    public List<Transaction> getRecentTransactions() throws SQLException {
        return transactionDAO.getAllTransactions();
    }

    // === QUẢN LÝ KHÁCH HÀNG ===

    // [DANH SÁCH] Lấy tất cả khách hàng
    public List<Customer> getAllCustomers() throws SQLException {
        return customerDAO.getAllCustomers();
    }
    
    // [TÌM KIẾM] Tìm khách hàng theo từ khóa (Username, Tên, Email, SĐT, CCCD)
    public List<Customer> searchCustomers(String keyword) throws SQLException {
        List<Customer> allCustomers = customerDAO.getAllCustomers();
        List<Customer> result = new ArrayList<>();
        String key = keyword.toLowerCase();
        for (Customer customer : allCustomers) {
            if (customer.getUsername().toLowerCase().contains(key) ||
                customer.getFullName().toLowerCase().contains(key) ||
                customer.getEmail().toLowerCase().contains(key) ||
                customer.getPhone().toLowerCase().contains(key) ||
                customer.getCitizenID().toLowerCase().contains(key)) {
                result.add(customer);
            }
        }
        return result;
    }
    
    // [KHÓA TÀI KHOẢN] Khóa tài khoản khách hàng
    public void lockCustomerAccount(int customerId) throws SQLException {
        customerDAO.updateStatus(customerId, "LOCKED");
    }
    
    // [MỞ KHÓA] Mở khóa tài khoản khách hàng
    public void unlockCustomerAccount(int customerId) throws SQLException {
        customerDAO.updateStatus(customerId, "ACTIVE");
    }
    
    // [CHI TIẾT] Lấy thông tin khách hàng theo ID
    public Customer getCustomerById(int customerId) throws SQLException {
        return customerDAO.findByID(customerId);
    }
    
    // [CHI TIẾT] Lấy danh sách tài khoản ngân hàng của khách
    public List<Account> getCustomerAccounts(int customerId) throws SQLException {
        return accountDAO.findByCustomer(customerId);
    }
    
    // [CHI TIẾT] Lấy lịch sử giao dịch của khách hàng
    public List<Transaction> getTransactionHistory(int customerId) throws SQLException {
        return transactionDAO.getTransactionsByCustomerId(customerId);
    }
    
    // [CẬP NHẬT] Admin sửa thông tin khách hàng
    public void updateCustomerInfo(Customer customer) throws SQLException {
        customerDAO.updateCustomerInfo(customer);
    }
    
    // [GIAO DỊCH] Admin nạp tiền cho khách
    public void depositForCustomer(int customerId, long amountVal, String description) throws SQLException {
        List<Account> accounts = accountDAO.findByCustomer(customerId);
        if (accounts.isEmpty()) throw new SQLException("Khách hàng không có tài khoản nào");
        
        Account mainAccount = accounts.get(0); 
        BigDecimal amount = BigDecimal.valueOf(amountVal);
        
        BigDecimal newBalance = mainAccount.getBalance().add(amount);
        accountDAO.updateBalance(mainAccount.getAccountID(), newBalance);
        
        Transaction transaction = new Transaction();
        transaction.setFromAccountID(null); 
        transaction.setToAccountID(mainAccount.getAccountID());
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(amount);
        transaction.setTransactionContent(description); 
        transaction.setTransactionStatus("SUCCESS");
        
        transactionDAO.createTransaction(transaction);
    }
    
    // [GIAO DỊCH] Admin rút tiền hộ khách
    public void withdrawForCustomer(int customerId, long amountVal, String description) throws SQLException {
        List<Account> accounts = accountDAO.findByCustomer(customerId);
        if (accounts.isEmpty()) throw new SQLException("Khách hàng không có tài khoản nào");
        
        Account mainAccount = accounts.get(0);
        BigDecimal amount = BigDecimal.valueOf(amountVal);
        
        if (mainAccount.getBalance().compareTo(amount) < 0) {
            throw new SQLException("Số dư không đủ để rút tiền");
        }
        
        BigDecimal newBalance = mainAccount.getBalance().subtract(amount);
        accountDAO.updateBalance(mainAccount.getAccountID(), newBalance);
        
        Transaction transaction = new Transaction();
        transaction.setFromAccountID(mainAccount.getAccountID());
        transaction.setToAccountID(null);
        transaction.setTransactionType("WITHDRAW");
        transaction.setAmount(amount);
        transaction.setTransactionContent(description);
        transaction.setTransactionStatus("SUCCESS");
        
        transactionDAO.createTransaction(transaction);
    }
    
    // [GIAO DỊCH] Admin chuyển tiền hộ khách
    public void transferForCustomer(int customerId, String targetAccountNumber, long amountVal, String description) throws SQLException {
        List<Account> senderAccounts = accountDAO.findByCustomer(customerId);
        if (senderAccounts.isEmpty()) throw new SQLException("Khách hàng không có tài khoản nào");
        
        Account senderAccount = senderAccounts.get(0);
        Account targetAccount = accountDAO.findByAccountNumber(targetAccountNumber);
        BigDecimal amount = BigDecimal.valueOf(amountVal);
        
        if (targetAccount == null) throw new SQLException("Tài khoản đích không tồn tại");
        if (senderAccount.getAccountID() == targetAccount.getAccountID()) throw new SQLException("Không thể chuyển tiền đến cùng tài khoản");
        if (senderAccount.getBalance().compareTo(amount) < 0) throw new SQLException("Số dư không đủ");
        
        try {
            // Trừ tiền gửi
            BigDecimal senderNewBalance = senderAccount.getBalance().subtract(amount);
            accountDAO.updateBalance(senderAccount.getAccountID(), senderNewBalance);
            
            // Cộng tiền nhận
            BigDecimal receiverNewBalance = targetAccount.getBalance().add(amount);
            accountDAO.updateBalance(targetAccount.getAccountID(), receiverNewBalance);
            
            // Tạo giao dịch
            Transaction transaction = new Transaction();
            transaction.setFromAccountID(senderAccount.getAccountID());
            transaction.setToAccountID(targetAccount.getAccountID());
            transaction.setTransactionType("TRANSFER");
            transaction.setAmount(amount);
            transaction.setTransactionContent("Chuyển tiền đến " + targetAccountNumber + " - " + description);
            transaction.setTransactionStatus("SUCCESS");
            
            transactionDAO.createTransaction(transaction);
            
        } catch (SQLException e) {
            throw new SQLException("Lỗi khi chuyển tiền: " + e.getMessage());
        }
    }
    
    // --- HELPER METHODS ---

    // [HELPER] Lấy tổng số dư của 1 khách hàng
    public BigDecimal getCustomerTotalBalance(int customerId) throws SQLException {
        List<Account> accounts = getCustomerAccounts(customerId);
        BigDecimal total = BigDecimal.ZERO;
        for (Account acc : accounts) total = total.add(acc.getBalance());
        return total;
    }
    
    // [HELPER] Đếm số giao dịch của 1 khách hàng
    public int getCustomerTransactionCount(int customerId) throws SQLException {
        return transactionDAO.getTransactionCountByCustomerId(customerId);
    }
    
    // [XÓA] Xóa một giao dịch (Cẩn thận khi dùng)
    public void deleteTransaction(int transactionId) throws SQLException {
        transactionDAO.deleteTransaction(transactionId);
    }
}