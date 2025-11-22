package view;

import Service.CustomerService;
import Service.AdminService;
import Session.CustomerSession;
import Session.AdminSession;
import model.Customer;
import model.Admin;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.Border;

public class Login extends JFrame {

    // Màu sắc chủ đạo và Border dùng chung
    private final Color COL_PRIMARY = new Color(0, 102, 102);
    private final Color COL_SECONDARY = Color.WHITE;
    private final Border INPUT_BORDER = BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(5, 10, 5, 10)
    );

    // CardLayout để chuyển đổi giữa Đăng nhập và Quên mật khẩu trên cùng 1 cửa sổ
    private JPanel rightPanelContainer;
    private CardLayout cardLayout;

    // Components cho màn hình Login
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JCheckBox chkAdmin, chkShowPassword;

    // Components cho màn hình Quên Mật Khẩu
    private JTextField txtResetCccd, txtResetPhone;
    private JPasswordField txtResetNewPass, txtResetConfirmPass;
    private JCheckBox chkShowResetPass;

    // [CONSTRUCTOR] Khởi tạo giao diện chính
    public Login() {
        setTitle("Hệ thống Ngân hàng - Đăng nhập");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(850, 550); 
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Thêm Panel trái (Logo)
        add(createLeftPanel(), BorderLayout.WEST);

        // Khởi tạo container chứa các form bên phải (Login & Forgot Password)
        cardLayout = new CardLayout();
        rightPanelContainer = new JPanel(cardLayout);
        
        // Thêm 2 màn hình vào CardLayout với tên định danh
        rightPanelContainer.add(createLoginPanel(), "LOGIN");
        rightPanelContainer.add(createForgotPasswordPanel(), "FORGOT_PASSWORD");

        add(rightPanelContainer, BorderLayout.CENTER);
    }

    // [UI] Tạo Panel bên trái chứa Logo và Slogan
    private JPanel createLeftPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(COL_PRIMARY);
        panel.setPreferredSize(new Dimension(350, 550));

        JLabel lblLogo = new JLabel("E-BANKING");
        lblLogo.setFont(new Font("Segoe UI", Font.BOLD, 36));
        lblLogo.setForeground(Color.WHITE);

        JLabel lblSlogan = new JLabel("An toàn - Tiện lợi - Nhanh chóng");
        lblSlogan.setFont(new Font("Segoe UI", Font.ITALIC, 14));
        lblSlogan.setForeground(new Color(200, 200, 200));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0; gbc.gridy = 0;
        gbc.insets = new Insets(10, 0, 10, 0);
        panel.add(lblLogo, gbc);
        
        gbc.gridy = 1;
        panel.add(lblSlogan, gbc);

        return panel;
    }

    // [UI] Tạo màn hình Đăng nhập (Mặc định)
    private JPanel createLoginPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COL_SECONDARY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));

        // Tiêu đề
        JLabel lblTitle = new JLabel("ĐĂNG NHẬP");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(COL_PRIMARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Input fields
        panel.add(createLabeledField("Tên đăng nhập", txtUsername = new JTextField()));
        panel.add(createLabeledField("Mật khẩu", txtPassword = new JPasswordField()));

        // Checkboxes & Link Quên MK
        JPanel optionPanel = new JPanel(new BorderLayout());
        optionPanel.setBackground(COL_SECONDARY);
        optionPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

        JPanel chkPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        chkPanel.setBackground(COL_SECONDARY);
        
        chkAdmin = new JCheckBox("Admin");
        chkAdmin.setBackground(COL_SECONDARY);
        chkAdmin.setFocusPainted(false);
        
        chkShowPassword = new JCheckBox("Hiện MK");
        chkShowPassword.setBackground(COL_SECONDARY);
        chkShowPassword.setFocusPainted(false);
        chkShowPassword.addActionListener(e -> txtPassword.setEchoChar(chkShowPassword.isSelected() ? (char) 0 : '•'));

        chkPanel.add(chkAdmin);
        chkPanel.add(Box.createHorizontalStrut(10));
        chkPanel.add(chkShowPassword);

        // Nút Quên mật khẩu (Chuyển sang màn hình kia)
        JButton btnForgot = new JButton("Quên mật khẩu?");
        btnForgot.setBorderPainted(false);
        btnForgot.setContentAreaFilled(false);
        btnForgot.setForeground(COL_PRIMARY);
        btnForgot.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnForgot.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnForgot.addActionListener(e -> {
            // Reset form quên mk trước khi hiện để xóa dữ liệu cũ
            txtResetCccd.setText("");
            txtResetPhone.setText("");
            txtResetNewPass.setText("");
            txtResetConfirmPass.setText("");
            cardLayout.show(rightPanelContainer, "FORGOT_PASSWORD");
        });

        optionPanel.add(chkPanel, BorderLayout.WEST);
        optionPanel.add(btnForgot, BorderLayout.EAST);
        
        panel.add(optionPanel);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Button Login
        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        styleButton(btnLogin, COL_PRIMARY);
        btnLogin.addActionListener(this::handleLogin);
        panel.add(btnLogin);
        panel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Link Đăng ký
        JLabel lblRegister = new JLabel("Chưa có tài khoản? Đăng ký ngay");
        lblRegister.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblRegister.setForeground(COL_PRIMARY);
        lblRegister.setCursor(new Cursor(Cursor.HAND_CURSOR));
        lblRegister.setAlignmentX(Component.CENTER_ALIGNMENT);
        lblRegister.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                new Register().setVisible(true); // Mở form đăng ký (file Register.java có sẵn)
                // dispose(); // Tùy chọn: Đóng form login hoặc giữ nguyên
            }
        });
        panel.add(lblRegister);

        // Nút Thoát
        panel.add(Box.createVerticalGlue());
        JPanel exitPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        exitPanel.setBackground(COL_SECONDARY);
        JButton btnExit = new JButton("Thoát");
        btnExit.setBackground(new Color(220, 53, 69));
        btnExit.setForeground(Color.WHITE);
        btnExit.setBorderPainted(false);
        btnExit.setFocusPainted(false);
        btnExit.addActionListener(e -> System.exit(0));
        exitPanel.add(btnExit);
        panel.add(exitPanel);

        return panel;
    }

    // [UI] Tạo màn hình Quên mật khẩu
    private JPanel createForgotPasswordPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COL_SECONDARY);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel lblTitle = new JLabel("ĐẶT LẠI MẬT KHẨU");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(COL_PRIMARY);
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        panel.add(lblTitle);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        panel.add(createLabeledField("CCCD/CMND (*)", txtResetCccd = new JTextField()));
        panel.add(createLabeledField("Số điện thoại (*)", txtResetPhone = new JTextField()));
        panel.add(createLabeledField("Mật khẩu mới (*)", txtResetNewPass = new JPasswordField()));
        panel.add(createLabeledField("Xác nhận mật khẩu (*)", txtResetConfirmPass = new JPasswordField()));

        // Checkbox hiện mật khẩu
        chkShowResetPass = new JCheckBox("Hiện mật khẩu");
        chkShowResetPass.setBackground(COL_SECONDARY);
        chkShowResetPass.setAlignmentX(Component.LEFT_ALIGNMENT);
        chkShowResetPass.addActionListener(e -> {
            char echo = chkShowResetPass.isSelected() ? (char) 0 : '•';
            txtResetNewPass.setEchoChar(echo);
            txtResetConfirmPass.setEchoChar(echo);
        });
        panel.add(chkShowResetPass);
        panel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Nút Xác nhận
        JButton btnConfirm = new JButton("XÁC NHẬN ĐỔI MK");
        styleButton(btnConfirm, COL_PRIMARY);
        btnConfirm.addActionListener(this::handleResetPassword);
        panel.add(btnConfirm);
        
        panel.add(Box.createRigidArea(new Dimension(0, 10)));

        // Nút Quay lại
        JButton btnBack = new JButton("Quay lại Đăng nhập");
        styleButton(btnBack, new Color(108, 117, 125));
        btnBack.addActionListener(e -> cardLayout.show(rightPanelContainer, "LOGIN"));
        panel.add(btnBack);

        return panel;
    }

    // [HELPER] Tạo Panel chứa Label và InputField đẹp
    private JPanel createLabeledField(String labelText, JTextField field) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(COL_SECONDARY);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 65)); // Chiều cao cố định cho đẹp

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        panel.add(label, BorderLayout.NORTH);

        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setBorder(INPUT_BORDER);
        field.setPreferredSize(new Dimension(0, 35));
        panel.add(field, BorderLayout.CENTER);
        
        // Spacer bottom
        panel.add(Box.createRigidArea(new Dimension(0, 10)), BorderLayout.SOUTH);

        return panel;
    }

    // [HELPER] Style cho các nút bấm
    private void styleButton(JButton btn, Color bg) {
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    // --- LOGIC XỬ LÝ ---

    // [LOGIC] Xử lý sự kiện bấm nút Đăng nhập
    private void handleLogin(ActionEvent evt) {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ tên đăng nhập và mật khẩu!");
            return;
        }

        try {
            if (chkAdmin.isSelected()) {
                // Đăng nhập quyền Admin
                AdminService adminService = new AdminService();
                Admin admin = adminService.login(username, password);
                
                AdminSession.setLoggedInAdmin(admin); // Lưu session
                JOptionPane.showMessageDialog(this, "Xin chào Admin " + admin.getFullName() + "!");
                new Menu_Admin().setVisible(true);
                this.dispose();

            } else {
                // Đăng nhập quyền Khách hàng
                CustomerService customerService = new CustomerService();
                Customer customer = customerService.login(username, password);
                
                CustomerSession.setLoggedInCustomer(customer); // Lưu session
                JOptionPane.showMessageDialog(this, "Đăng nhập thành công!"); // [FIX] Đã bật lại thông báo
                
                new Menu_KH().setVisible(true);
                this.dispose();
            }
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Lỗi đăng nhập", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi hệ thống: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // [LOGIC] Xử lý sự kiện bấm nút Quên mật khẩu
    private void handleResetPassword(ActionEvent evt) {
        String cccd = txtResetCccd.getText().trim();
        String phone = txtResetPhone.getText().trim();
        String newPass = new String(txtResetNewPass.getPassword());
        String confirmPass = new String(txtResetConfirmPass.getPassword());

        // Validate cơ bản
        if (cccd.isEmpty() || phone.isEmpty() || newPass.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền đầy đủ thông tin (*)");
            return;
        }

        if (!newPass.equals(confirmPass)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!");
            return;
        }

        try {
            CustomerService customerService = new CustomerService();
            // Gọi hàm resetPassword đã thêm trong Service (có check trùng pass cũ)
            customerService.resetPassword(cccd, phone, newPass);
            
            JOptionPane.showMessageDialog(this, "Đổi mật khẩu thành công! Vui lòng đăng nhập lại.");
            // Chuyển về màn hình login
            cardLayout.show(rightPanelContainer, "LOGIN");
            
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(this, e.getMessage(), "Sai thông tin", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        // Set Look and Feel cho đẹp nếu muốn
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); } catch (Exception ex) {}
        
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}