package Service;

import DataAccessObject.*;
import java.math.BigDecimal;
import model.*;
import java.sql.*;
import java.util.List;

public class AccountService{
    
    private final AccountDAO a = new AccountDAO();
    
    // [TẠO TÀI KHOẢN] Tạo mới tài khoản ngân hàng (Kiểm tra trùng số TK trước khi tạo)
    public Account createAccount(int customerID, String accountNumber, String pinSmart, BigDecimal balance) throws SQLException{
        Account existing = a.findByAccountNumber(accountNumber);
        if(existing != null){
            throw new IllegalArgumentException("Account number already exists.");
        }
        return a.createAccount(customerID, accountNumber, pinSmart, balance);
    }
    
    // [TÌM KIẾM] Lấy thông tin tài khoản theo ID
    public Account getAccountByID(int id) throws SQLException{
        Account acc = a.findByID(id);
        if(acc == null){
            throw new IllegalArgumentException("Account nout found.");
        }
        return acc;
    }
    
    // [TÌM KIẾM] Lấy thông tin tài khoản theo Số tài khoản
    public Account getAccountByNumber(String num) throws SQLException{
        Account acc = a.findByAccountNumber(num);
        if(acc == null){
            throw new IllegalArgumentException("Account nout found.");
        }
        return acc;
    }
    
    // [CẬP NHẬT] Cập nhật thông tin chung của tài khoản
    public void updateInfor(Account acctomer) throws SQLException{
        Account acc = a.findByID(acctomer.getAccountID());
        if(acc == null){
            throw new IllegalArgumentException("Account does not exist.");
        }
        a.update(acctomer);
    }
    
    // [CẬP NHẬT] Cập nhật số dư tài khoản
    public void updateBalance(int id, BigDecimal amount) throws SQLException{
        Account acc = a.findByID(id);
        if(acc == null){
            throw new IllegalArgumentException("Account not found.");
        }
        a.updateBalance(id, amount);
    }
    
    // [BẢO MẬT] Đổi mã PIN (Smart PIN)
    public void updatePIN(int id, String pin) throws SQLException{
        Account acc = a.findByID(id);
        if(acc == null){
            throw new IllegalArgumentException("Account not found.");
        }
        a.updatePIN(id, pin);
    }
    
    // [QUẢN TRỊ] Cập nhật trạng thái (Khóa/Mở khóa tài khoản)
    public void updateStatus(int id, String status) throws SQLException{
        Account acc = a.findByID(id);
        if(acc == null){
            throw new IllegalArgumentException("Account not found.");
        }
        a.updateStatus(id, status);
    }
    
    // [XÓA] Xóa tài khoản khỏi hệ thống
    public void delete(int id) throws SQLException{
        Account acc = a.findByID(id);
        if(acc == null){
            throw new IllegalArgumentException("Account does not exist.");
        }
        a.delete(id);
    }
    
    // [DANH SÁCH] Lấy danh sách tài khoản của một khách hàng cụ thể
    public List<Account> getAccountByCustomer(int id) throws SQLException{
        return a.findByCustomer(id);
    }
    
    // [ADMIN] Lấy danh sách toàn bộ tài khoản trong hệ thống
    public List<Account> getAccountsAll() throws SQLException{
        return a.findAccountAll();
    }
}