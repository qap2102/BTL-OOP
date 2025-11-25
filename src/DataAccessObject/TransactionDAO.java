package DataAccessObject;

import database.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.*;
import java.util.*;
import model.Transaction;

public class TransactionDAO {

    // --- 1. CÁC HÀM CƠ BẢN (Dùng cho Customer App) ---

    // [TẠO GIAO DỊCH] Hàm cơ bản, tự tạo kết nối mới
    public Transaction createTransaction(Integer fromAccountID, Integer toAccountID, BigDecimal amount,
            String transactionType, String transactionContent) throws SQLException {
        Transaction t = new Transaction();
        t.setFromAccountID(fromAccountID);
        t.setToAccountID(toAccountID);
        t.setAmount(amount);
        t.setTransactionType(transactionType);
        t.setTransactionContent(transactionContent);
        t.setTransactionStatus("SUCCESS");

        createTransaction(t); // Gọi hàm overload bên dưới
        return t;
    }

    // [TẠO GIAO DỊCH - TRANSACTION] Hàm này dùng chung kết nối (Connection) để đảm
    // bảo tính toàn vẹn dữ liệu (Rollback nếu lỗi)
    public Transaction createTransaction_service(Connection conn, Integer fromAccountID, Integer toAccountID,
            BigDecimal amount, String transactionType, String transactionContent) throws SQLException {
        String sql = "INSERT INTO transactions(from_account_id, to_account_id, amount, transaction_type, transaction_content, transaction_status, created_at) VALUES(?,?,?,?,?,?,NOW())";
        try (PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setObject(1, fromAccountID);
            ps.setObject(2, toAccountID);
            ps.setBigDecimal(3, amount);
            ps.setString(4, transactionType);
            ps.setString(5, transactionContent);
            ps.setString(6, "SUCCESS");
            ps.executeUpdate();

            Transaction t = new Transaction();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    t.setTransactionID(rs.getInt(1));
                }
            }
            t.setFromAccountID(fromAccountID);
            t.setToAccountID(toAccountID);
            t.setAmount(amount);
            t.setTransactionType(transactionType);
            t.setTransactionContent(transactionContent);
            t.setTransactionStatus("SUCCESS");
            return t;
        }
    }

    // [LỊCH SỬ THEO TÀI KHOẢN] Lấy giao dịch liên quan đến một Account ID cụ thể
    public List<Transaction> getTransactionsByAccount(int accID) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE from_account_id = ? OR to_account_id = ? ORDER BY created_at DESC";
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, accID);
            ps.setInt(2, accID);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extract(rs));
            }
        }
        return list;
    }

    // [LỊCH SỬ THEO USERNAME] Lấy tất cả giao dịch của một người dùng (dựa vào
    // Username)
    public List<Transaction> getTransactionsByUsername(String username) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        // Sử dụng Subquery để tìm tất cả account_id thuộc về username đó, tránh trùng
        // lặp
        String sql = "SELECT t.* FROM transactions t " +
                "WHERE t.from_account_id IN (SELECT account_id FROM accounts a JOIN customers c ON a.customer_id = c.customer_id WHERE c.username = ?) "
                +
                "OR t.to_account_id IN (SELECT account_id FROM accounts a JOIN customers c ON a.customer_id = c.customer_id WHERE c.username = ?) "
                +
                "ORDER BY t.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, username);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extract(rs));
            }
        }
        return list;
    }

    // --- 2. CÁC HÀM MỞ RỘNG (Dùng cho Admin App & Quản lý khách hàng) ---

    // [TẠO GIAO DỊCH TỪ OBJECT] Helper method
    public void createTransaction(Transaction t) throws SQLException {
        String sql = "INSERT INTO transactions(from_account_id, to_account_id, amount, transaction_type, transaction_content, transaction_status, created_at) VALUES(?,?,?,?,?,?,NOW())";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setObject(1, t.getFromAccountID());
            ps.setObject(2, t.getToAccountID());
            ps.setBigDecimal(3, t.getAmount());
            ps.setString(4, t.getTransactionType());
            ps.setString(5, t.getTransactionContent());
            ps.setString(6, t.getTransactionStatus());

            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    t.setTransactionID(rs.getInt(1));
                }
            }
        }
    }

    // [DASHBOARD] Lấy 50 giao dịch gần nhất toàn hệ thống
    public List<Transaction> getAllTransactions() throws SQLException {
        String sql = "SELECT * FROM transactions ORDER BY created_at DESC LIMIT 50";
        List<Transaction> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                Statement s = conn.createStatement();
                ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extract(rs));
            }
        }
        return list;
    }

    // [QUẢN LÝ KHÁCH HÀNG] Lấy lịch sử giao dịch theo Customer ID
    public List<Transaction> getTransactionsByCustomerId(int customerId) throws SQLException {
        List<Transaction> list = new ArrayList<>();
        // Sử dụng Subquery để lấy các account_id liên quan đến customerId
        String sql = "SELECT t.* FROM transactions t " +
                "WHERE t.from_account_id IN (SELECT account_id FROM accounts WHERE customer_id = ?) " +
                "OR t.to_account_id IN (SELECT account_id FROM accounts WHERE customer_id = ?) " +
                "ORDER BY t.created_at DESC";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            ps.setInt(2, customerId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extract(rs));
            }
        }
        return list;
    }

    // [HELPER] Chuyển đổi ResultSet thành Object Transaction
    private Transaction extract(ResultSet rs) throws SQLException {
        Transaction t = new Transaction();
        t.setTransactionID(rs.getInt("transaction_id"));

        int fromId = rs.getInt("from_account_id");
        t.setFromAccountID(rs.wasNull() ? null : fromId);

        int toId = rs.getInt("to_account_id");
        t.setToAccountID(rs.wasNull() ? null : toId);

        t.setAmount(rs.getBigDecimal("amount"));
        t.setTransactionType(rs.getString("transaction_type"));
        t.setTransactionContent(rs.getString("transaction_content"));
        t.setTransactionStatus(rs.getString("transaction_status"));

        Timestamp ts = rs.getTimestamp("created_at");
        if (ts != null) {
            t.setCreatedAt(ts.toLocalDateTime());
        }
        return t;
    }
}