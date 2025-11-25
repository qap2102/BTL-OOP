package Service;

import DataAccessObject.*;
import java.math.BigDecimal;
import model.*;
import java.sql.*;
import java.util.List;

public class AccountService {

    private final AccountDAO a = new AccountDAO();

    // [TẠO TÀI KHOẢN] Tạo mới tài khoản ngân hàng (Kiểm tra trùng số TK trước khi
    // tạo)
    public Account createAccount(int customerID, String accountNumber, String pinSmart, BigDecimal balance)
            throws SQLException {
        Account existing = a.findByAccountNumber(accountNumber);
        if (existing != null) {
            throw new IllegalArgumentException("Tài khoản đã tồn tại.");
        }
        return a.createAccount(customerID, accountNumber, pinSmart, balance);
    }

    // [TÌM KIẾM] Lấy thông tin tài khoản theo ID
    public Account getAccountByID(int id) throws SQLException {
        Account acc = a.findByID(id);
        if (acc == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản.");
        }
        return acc;
    }

    // [TÌM KIẾM] Lấy thông tin tài khoản theo Số tài khoản
    public Account getAccountByNumber(String num) throws SQLException {
        Account acc = a.findByAccountNumber(num);
        if (acc == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản.");
        }
        return acc;
    }

    // [CẬP NHẬT] Cập nhật thông tin chung của tài khoản
    public void updateInfor(Account acctomer) throws SQLException {
        Account acc = a.findByID(acctomer.getAccountID());
        if (acc == null) {
            throw new IllegalArgumentException("Tài khoản không tồn tại.");
        }
        a.update(acctomer);
    }

    // [CẬP NHẬT] Cập nhật số dư tài khoản
    public void updateBalance(int id, BigDecimal amount) throws SQLException {
        Account acc = a.findByID(id);
        if (acc == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản.");
        }
        a.updateBalance(id, amount);
    }

    // [QUẢN TRỊ] Cập nhật trạng thái (Khóa/Mở khóa tài khoản)
    public void updateStatus(int id, String status) throws SQLException {
        Account acc = a.findByID(id);
        if (acc == null) {
            throw new IllegalArgumentException("Không tìm thấy tài khoản.");
        }
        a.updateStatus(id, status);
    }

    // [DANH SÁCH] Lấy danh sách tài khoản của một khách hàng cụ thể
    public List<Account> getAccountByCustomer(int id) throws SQLException {
        return a.findByCustomer(id);
    }

    // [ADMIN] Lấy danh sách toàn bộ tài khoản trong hệ thống
    public List<Account> getAccountsAll() throws SQLException {
        return a.findAccountAll();
    }
}