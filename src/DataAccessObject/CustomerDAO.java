package DataAccessObject;

import database.DatabaseConnection;
import java.sql.*;
import java.util.*;
import model.Customer;

public class CustomerDAO {

    // --- [ADMIN] Cập nhật thông tin khách hàng (Không cho sửa CCCD) ---
    // Hàm này dùng trong chức năng "Quản lý khách hàng" của Admin
    public void updateCustomerInfo(Customer c) throws SQLException {
        String sql = "UPDATE customers SET full_name=?, date_of_birth=?, sex=?, place_of_residence=?, email=?, phone=? WHERE customer_id=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, c.getFullName());
            ps.setString(2, c.getDateOfBirth());
            ps.setString(3, c.getSex());
            ps.setString(4, c.getPlaceOfResidence());
            ps.setString(5, c.getEmail());
            ps.setString(6, c.getPhone());
            ps.setInt(7, c.getCustomerID());
            ps.executeUpdate();
        }
    }

    // --- Đăng ký khách hàng mới ---
    // Tạo record trong bảng customers và trả về đối tượng vừa tạo (có ID)
    public Customer registerCustomer(String username, String passwordHash, String citizenID, String fullName,
            String dateOfBirth, String sex, String nationality, String placeOfOrigin, String placeOfResidence,
            String email, String phoneNumber) throws SQLException {
        if (findByRegister(username, citizenID, email, phoneNumber) != null)
            throw new SQLException("Username already exists.");
        String sql = "INSERT INTO customers(username, password_hash, citizen_id, full_name, date_of_birth, sex, nationality, place_of_origin, place_of_residence, email, phone) VALUES(?,?,?,?,?,?,?,?,?,?,?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setString(3, citizenID);
            ps.setString(4, fullName);
            ps.setString(5, dateOfBirth);
            ps.setString(6, sex);
            ps.setString(7, nationality);
            ps.setString(8, placeOfOrigin);
            ps.setString(9, placeOfResidence);
            ps.setString(10, email);
            ps.setString(11, phoneNumber);
            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            Customer k = new Customer();
            if (rs.next()) {
                k.setCustomerID(rs.getInt(1));
            }
            k.setUsername(username);
            k.setPasswordHash(passwordHash);
            k.setCitizenID(citizenID);
            k.setFullName(fullName);
            k.setDateOfBirth(dateOfBirth);
            k.setSex(sex);
            k.setNationality(nationality);
            k.setPlaceOfOrigin(placeOfOrigin);
            k.setPlaceOfResidence(placeOfResidence);
            k.setEmail(email);
            k.setPhone(phoneNumber);
            return k;
        }
    }

    // --- Kiểm tra thông tin đăng ký trùng lặp ---
    // Check xem username, CCCD, email hoặc SĐT đã tồn tại chưa
    public Customer findByRegister(String username, String citizenID, String email, String phone) throws SQLException {
        String sql = "SELECT * FROM customers WHERE username=? OR citizen_id=? OR email=? OR phone=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, citizenID);
            ps.setString(3, email);
            ps.setString(4, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extract(rs);
            }
            return null;
        }
    }

    // --- Đăng nhập ---
    // Tìm khách hàng khớp Username và PasswordHash
    public Customer findByLogin(String username, String passwordHash) throws SQLException {
        String sql = "SELECT * FROM customers WHERE username=? AND password_hash=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extract(rs);
            }
            return null;
        }
    }

    // --- Tìm khách hàng theo ID ---
    public Customer findByID(int id) throws SQLException {
        String sql = "SELECT * FROM customers WHERE customer_id=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extract(rs);
            }
            return null;
        }
    }

    // --- Lấy danh sách tất cả khách hàng ---
    // Dùng cho Admin xem danh sách
    public List<Customer> getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers ORDER BY customer_id DESC";
        List<Customer> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
                Statement s = conn.createStatement();
                ResultSet rs = s.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extract(rs));
            }
        }
        return list;
    }

    // --- [CUSTOMER] Cập nhật thông tin cá nhân ---
    // Hàm này dùng cho khách hàng tự sửa thông tin của mình (Menu KH)
    public void update(Customer k) throws SQLException {
        String sql = "UPDATE customers SET full_name=?, date_of_birth=?, sex=?, place_of_residence=?, email=?, phone=? WHERE customer_id=?";
        try (Connection conn = DatabaseConnection.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, k.getFullName());
            ps.setString(2, k.getDateOfBirth());
            ps.setString(3, k.getSex());
            ps.setString(4, k.getPlaceOfResidence());
            ps.setString(5, k.getEmail());
            ps.setString(6, k.getPhone());
            ps.setInt(7, k.getCustomerID());
            ps.executeUpdate();
        }
    }

    // --- Đổi mật khẩu ---
    public boolean updatePassword(int customerID, String newPasswordHash) throws SQLException {
        String sql = "UPDATE customers SET password_hash=? WHERE customer_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newPasswordHash);
            ps.setInt(2, customerID);
            int affectedRows = ps.executeUpdate();
            return affectedRows > 0;
        }
    }

    // --- [QUÊN MẬT KHẨU] Tìm khách hàng theo CCCD và SĐT ---
    public Customer findByCccdAndPhone(String cccd, String phone) throws SQLException {
        String sql = "SELECT * FROM customers WHERE citizen_id=? AND phone=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cccd);
            ps.setString(2, phone);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extract(rs);
            }
            return null;
        }
    }

    // --- Helper: Chuyển ResultSet thành Object Customer ---
    private Customer extract(ResultSet rs) throws SQLException {
        Customer k = new Customer();
        k.setCustomerID(rs.getInt("customer_id"));
        k.setUsername(rs.getString("username"));
        k.setPasswordHash(rs.getString("password_hash"));
        k.setCitizenID(rs.getString("citizen_id"));
        k.setFullName(rs.getString("full_name"));
        k.setDateOfBirth(rs.getString("date_of_birth"));
        k.setSex(rs.getString("sex"));
        k.setNationality(rs.getString("nationality"));
        k.setPlaceOfOrigin(rs.getString("place_of_origin"));
        k.setPlaceOfResidence(rs.getString("place_of_residence"));
        k.setEmail(rs.getString("email"));
        k.setPhone(rs.getString("phone"));
        // k.setUserStatus(rs.getString("user_status"));
        return k;
    }
}