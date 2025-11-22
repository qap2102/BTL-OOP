package view;

import java.awt.*;
import javax.swing.*;
import Service.CustomerService;
import Session.CustomerSession;
import model.Customer;

// [CLASS] Màn hình Đổi mật khẩu dành cho Khách hàng
public class DoiMatKhau extends JFrame {

    private JPasswordField txtOld, txtNew;

    // [CONSTRUCTOR] Khởi tạo giao diện nhập liệu
    public DoiMatKhau() {
        setTitle("Đổi mật khẩu");
        setSize(400, 300);
        setLocationRelativeTo(null);
        setLayout(null);

        // Tiêu đề
        JLabel lbl = new JLabel("ĐỔI MẬT KHẨU", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setBounds(100, 20, 200, 40);
        add(lbl);

        // Input Mật khẩu cũ
        JLabel lblOld = new JLabel("Mật khẩu cũ:");
        lblOld.setBounds(50, 90, 120, 30);
        add(lblOld);

        txtOld = new JPasswordField();
        txtOld.setBounds(170, 90, 160, 30);
        add(txtOld);

        // Input Mật khẩu mới
        JLabel lblNew = new JLabel("Mật khẩu mới:");
        lblNew.setBounds(50, 130, 120, 30);
        add(lblNew);

        txtNew = new JPasswordField();
        txtNew.setBounds(170, 130, 160, 30);
        add(txtNew);

        // Nút xác nhận
        JButton btn = new JButton("Đổi");
        btn.setBounds(150, 180, 120, 35);
        btn.addActionListener(e -> doiMatKhau());
        add(btn);
    }

    // [ACTION] Xử lý logic đổi mật khẩu (Đã chỉnh sửa)
    private void doiMatKhau() {
        String oldPass = new String(txtOld.getPassword());
        String newPass = new String(txtNew.getPassword());

        // 1. Kiểm tra bỏ trống (Validation cơ bản)
        if (oldPass.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ mật khẩu cũ và mới!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 2. [LOGIC MỚI] Kiểm tra mật khẩu mới có trùng mật khẩu cũ không
        if (oldPass.equals(newPass)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu mới không được trùng với mật khẩu cũ!", "Lỗi logic", JOptionPane.ERROR_MESSAGE);
            return; // Dừng lại, không gọi Service
        }

        try {
            // Lấy thông tin khách hàng hiện tại từ Session
            Customer current = CustomerSession.getLoggedInCustomer(); 
            if (current == null) {
                JOptionPane.showMessageDialog(this, "Lỗi: Không có khách hàng đăng nhập!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Gọi Service để thực hiện đổi mật khẩu
            CustomerService cs = new CustomerService();
            cs.changePassword(current, oldPass, newPass); 

            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công!");
            this.dispose(); // Đóng cửa sổ sau khi thành công
            
        } catch (IllegalArgumentException e) {
            // Bắt lỗi logic từ Service (Ví dụ: Mật khẩu cũ nhập sai)
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi thông tin", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            // Bắt lỗi hệ thống
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra: " + e.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }
}