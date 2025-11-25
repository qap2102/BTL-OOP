package DataAccessObject;

import database.DatabaseConnection;
import java.sql.*;
import model.Admin;

public class AdminDAO {

    // [ĐĂNG NHẬP ADMIN] Kiểm tra xác thực tài khoản quản trị viên
    public Admin findByLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM admins WHERE username=? AND password=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return extract(rs);
            return null;
        }
    }

    // [TÌM KIẾM] Lấy thông tin Admin theo ID
    public Admin findByID(int id) throws SQLException {
        String sql = "SELECT * FROM admins WHERE admin_id=?";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return extract(rs);
            return null;
        }
    }

    // [HELPER] Chuyển đổi dữ liệu từ ResultSet sang Object Admin
    private Admin extract(ResultSet rs) throws SQLException {
        Admin admin = new Admin();
        admin.setAdminID(rs.getInt("admin_id"));
        admin.setUsername(rs.getString("username"));
        admin.setFullName(rs.getString("full_name"));
        admin.setEmail(rs.getString("email"));
        admin.setRole(rs.getString("role"));
        return admin;
    }
}