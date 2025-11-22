package view;

import database.DatabaseConnection;
import java.sql.Connection;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

// [CLASS] Lớp chính chứa hàm main để chạy chương trình
public class Main {

    public static void main(String[] args) {
        // [GIAO DIỆN] Thiết lập giao diện (Look and Feel) là Nimbus để ứng dụng đẹp hơn
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
            System.err.println("Không thể thiết lập giao diện: " + e.getMessage());
        }

        // [DATABASE] Kiểm tra kết nối CSDL trước khi khởi động
        // Nếu không kết nối được thì báo lỗi và không mở ứng dụng
        try (Connection conn = DatabaseConnection.getConnection()) {
            if (conn == null) {
                JOptionPane.showMessageDialog(null, 
                    "Kết nối Database thất bại!\nVui lòng kiểm tra lại cấu hình.", 
                    "Lỗi Khởi Động", 
                    JOptionPane.ERROR_MESSAGE);
                return; // Dừng chương trình
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, 
                "Lỗi kết nối: " + e.getMessage(), 
                "Lỗi Khởi Động", 
                JOptionPane.ERROR_MESSAGE);
            return; // Dừng chương trình
        }

        // [KHỞI CHẠY] Mở màn hình Đăng nhập để bắt đầu sử dụng
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
    }
}