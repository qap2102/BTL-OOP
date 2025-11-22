package DataAccessObject;

import database.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import model.Account;

public class AccountDAO {

    // [TẠO TÀI KHOẢN] Thêm mới tài khoản vào DB với số dư ban đầu và trạng thái ACTIVE
    public Account createAccount(int customerID, String accountNumber, String pinSmart, BigDecimal balance) throws SQLException {
        String sql = "INSERT INTO accounts(customer_id, account_number, balance, pin_smart, account_status) VALUES(?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, customerID);
            ps.setString(2, accountNumber);
            ps.setBigDecimal(3, balance);
            ps.setString(4, pinSmart);
            ps.setString(5, "ACTIVE");
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                Account a = new Account();
                if (rs.next()) a.setAccountID(rs.getInt(1));
                a.setCustomerID(customerID);
                a.setAccountNumber(accountNumber);
                a.setBalance(balance);
                a.setPinSmart(pinSmart);
                a.setAccountStatus("ACTIVE");
                return a;
            }
        }
    }

    // [TÌM KIẾM] Lấy thông tin tài khoản theo ID tài khoản
    public Account findByID(int id) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extract(rs);
            return null;
        }
    }
    
    // [TÌM KIẾM] Lấy thông tin tài khoản theo Số tài khoản
    public Account findByAccountNumber(String accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, accountNumber);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extract(rs);
            return null;
        }
    }
    
    // [TÌM KIẾM] Lấy tài khoản dựa trên Username khách hàng (Join bảng customers)
    public Account getAccountByUsername(String username) throws SQLException {
        String sql = "SELECT a.* FROM accounts a " +
                     "JOIN customers c ON a.customer_id = c.customer_id " +
                     "WHERE c.username = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return extract(rs);
            return null;
        }
    }

    // [DANH SÁCH] Lấy tất cả tài khoản của một khách hàng cụ thể
    public List<Account> findByCustomer(int customerID) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE customer_id=? ORDER BY account_id DESC";
        List<Account> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(extract(rs));
        }
        return list;
    }

    // [ADMIN] Lấy toàn bộ danh sách tài khoản trong hệ thống
    public List<Account> findAccountAll() throws SQLException {
        String sql = "SELECT * FROM accounts ORDER BY account_id DESC";
        List<Account> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             Statement s = conn.createStatement();
             ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) list.add(extract(rs));
        }
        return list;
    }

    // [CẬP NHẬT] Sửa toàn bộ thông tin tài khoản (Số dư, PIN, Trạng thái)
    public void update(Account a) throws SQLException {
        String sql = "UPDATE accounts SET balance=?, pin_smart=?, account_status=? WHERE account_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, a.getBalance());
            ps.setString(2, a.getPinSmart());
            ps.setString(3, a.getAccountStatus());
            ps.setInt(4, a.getAccountID());
            ps.executeUpdate();
        }
    }

    // [CẬP NHẬT] Chỉ sửa số dư theo ID (Dùng cho nạp/rút đơn lẻ)
    public void updateBalance(int accountID, BigDecimal balance) throws SQLException {
        String sql = "UPDATE accounts SET balance=? WHERE account_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setBigDecimal(1, balance);
            ps.setInt(2, accountID);
            ps.executeUpdate();
        }
    }
    
    // [SERVICE] Cập nhật số dư trong Transaction (Có truyền Connection để rollback nếu lỗi)
    public void updateBalance_service(Connection conn, int id, BigDecimal balance) throws SQLException{
        String sql = "UPDATE accounts SET balance=? WHERE account_id=?";
        try(PreparedStatement ps = conn.prepareStatement(sql)){
            ps.setBigDecimal(1, balance);
            ps.setInt(2, id);
            ps.executeUpdate();
        }
    }

    // [CẬP NHẬT] Đổi mã PIN Smart
    public void updatePIN(int accountID, String pin) throws SQLException {
        String sql = "UPDATE accounts SET pin_smart=? WHERE account_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, pin);
            ps.setInt(2, accountID);
            ps.executeUpdate();
        }
    }

    // [ADMIN] Cập nhật trạng thái tài khoản (Khóa/Mở khóa)
    public void updateStatus(int accountID, String status) throws SQLException {
        String sql = "UPDATE accounts SET account_status=? WHERE account_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, accountID);
            ps.executeUpdate();
        }
    }

    // [XÓA] Xóa tài khoản khỏi hệ thống
    public void delete(int accountID) throws SQLException {
        String sql = "DELETE FROM accounts WHERE account_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accountID);
            ps.executeUpdate();
        }
    }

    // [GIAO DỊCH] Helper để ghi lịch sử giao dịch
    public void insertTransaction(Integer fromAccountID, Integer toAccountID, BigDecimal amount, String type, String content, String status) throws SQLException {
        String sql = "INSERT INTO transactions(from_account_id, to_account_id, amount, transaction_type, transaction_content, transaction_status, created_at) VALUES(?,?,?,?,?,?,NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            if (fromAccountID == null) ps.setNull(1, Types.INTEGER);
            else ps.setInt(1, fromAccountID);

            if (toAccountID == null) ps.setNull(2, Types.INTEGER);
            else ps.setInt(2, toAccountID);

            ps.setBigDecimal(3, amount);
            ps.setString(4, type);
            ps.setString(5, content);
            ps.setString(6, status);
            ps.executeUpdate();
        }
    }

    // [HELPER] Chuyển đổi dữ liệu từ ResultSet SQL thành Object Account
    private Account extract(ResultSet rs) throws SQLException {
        Account a = new Account();
        a.setAccountID(rs.getInt("account_id"));
        a.setCustomerID(rs.getInt("customer_id"));
        a.setAccountNumber(rs.getString("account_number"));
        a.setBalance(rs.getBigDecimal("balance"));
        a.setPinSmart(rs.getString("pin_smart"));
        a.setAccountStatus(rs.getString("account_status"));
        
        // Bổ sung lấy ngày tạo (để hiển thị ở form Thông tin tài khoản)
        try {
            Timestamp ts = rs.getTimestamp("created_at");
            if (ts != null) {
                a.setCreatedAt(ts.toLocalDateTime());
            }
        } catch (Exception e) {
            // Bỏ qua nếu không có cột created_at
        }
        
        return a;
    }
}