package view;

import java.awt.*;
import javax.swing.*;
import model.Customer;
import model.Account;
import Service.AccountService;
import Session.CustomerSession;
import java.util.List;

// [CLASS] Màn hình hiển thị thông tin tài khoản khách hàng
public class TaiKhoanKhachHang extends JFrame {

    // Màu sắc giao diện
    private final Color COL_BG = new Color(240, 245, 249);
    private final Color COL_HEADER = new Color(0, 102, 102);
    private final Color COL_CARD = Color.WHITE;

    // [CONSTRUCTOR] Khởi tạo giao diện
    public TaiKhoanKhachHang() {
        setTitle("Thông tin tài khoản");
        setSize(700, 550);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(COL_BG);

        // Header (Tiêu đề trang)
        JPanel pnlHeader = createHeaderPanel();
        add(pnlHeader, BorderLayout.NORTH);

        // Content (Nội dung chính)
        JPanel pnlContent = createContentPanel();
        add(pnlContent, BorderLayout.CENTER);

        // Buttons (Nút chức năng)
        JPanel pnlButtons = createButtonPanel();
        add(pnlButtons, BorderLayout.SOUTH);
    }

    // [UI] Tạo Panel Header
    private JPanel createHeaderPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COL_HEADER);
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel lblTitle = new JLabel("THÔNG TIN TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblTitle.setForeground(Color.WHITE);

        panel.add(lblTitle, BorderLayout.CENTER);
        return panel;
    }

    // [UI] Tạo Panel Nội dung chính (Chia 4 ô thông tin)
    private JPanel createContentPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 2, 15, 15));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        panel.setBackground(COL_BG);

        // Lấy thông tin khách hàng từ Session
        Customer customer = CustomerSession.getLoggedInCustomer();
        if (customer == null) {
            showError("Lỗi phiên đăng nhập!");
            return panel;
        }

        // Lấy thông tin tài khoản ngân hàng từ DB
        AccountService as = new AccountService();
        Account acc = null;
        try {
            List<Account> list = as.getAccountByCustomer(customer.getCustomerID());
            if (!list.isEmpty()) {
                acc = list.get(0); // Lấy tài khoản đầu tiên
            }
        } catch (Exception e) {
            e.printStackTrace();
            showError("Lỗi tải thông tin tài khoản!");
            return panel;
        }

        // Panel 1: Thông tin cá nhân
        panel.add(createInfoPanel("THÔNG TIN CÁ NHÂN", createPersonalInfo(customer)));

        // Panel 2: Thông tin tài khoản
        panel.add(createInfoPanel("THÔNG TIN TÀI KHOẢN", createAccountInfo(acc)));

        // Panel 3: Thông tin liên hệ
        panel.add(createInfoPanel("THÔNG TIN LIÊN HỆ", createContactInfo(customer)));

        // Panel 4: Trạng thái tài khoản
        panel.add(createInfoPanel("TRẠNG THÁI", createStatusInfo(customer, acc)));

        return panel;
    }

    // [HELPER] Tạo khung Panel con có viền và tiêu đề
    private JPanel createInfoPanel(String title, JPanel contentPanel) {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBackground(COL_CARD);
        panel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));

        // Title
        JLabel lblTitle = new JLabel(title);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 16));
        lblTitle.setForeground(COL_HEADER);
        lblTitle.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

        panel.add(lblTitle, BorderLayout.NORTH);
        panel.add(contentPanel, BorderLayout.CENTER);

        return panel;
    }

    // [UI] Tạo nội dung Thông tin cá nhân
    private JPanel createPersonalInfo(Customer customer) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        panel.setBackground(COL_CARD);

        addInfoRow(panel, "Họ và tên:", customer.getFullName());
        addInfoRow(panel, "CCCD/CMND:", customer.getCitizenID());
        addInfoRow(panel, "Giới tính:", formatGender(customer.getSex()));
        addInfoRow(panel, "Quốc tịch:", customer.getNationality());
        addInfoRow(panel, "Mã khách hàng:", String.valueOf(customer.getCustomerID()));

        return panel;
    }

    // [UI] Tạo nội dung Thông tin tài khoản
    private JPanel createAccountInfo(Account acc) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        panel.setBackground(COL_CARD);

        if (acc != null) {
            addInfoRow(panel, "Số tài khoản:", acc.getAccountNumber());
            addInfoRow(panel, "Số dư:", formatCurrency(acc.getBalance()));
            addInfoRow(panel, "Loại tài khoản:", "Tài khoản thanh toán");
            addInfoRow(panel, "Ngày mở TK:", formatDate(acc.getCreatedAt()));
        } else {
            addInfoRow(panel, "Số tài khoản:", "Chưa có tài khoản");
            addInfoRow(panel, "Số dư:", "0 VND");
            addInfoRow(panel, "Loại tài khoản:", "N/A");
            addInfoRow(panel, "Ngày mở TK:", "N/A");
        }

        return panel;
    }

    // [UI] Tạo nội dung Thông tin liên hệ
    private JPanel createContactInfo(Customer customer) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        panel.setBackground(COL_CARD);

        addInfoRow(panel, "Email:", customer.getEmail());
        addInfoRow(panel, "Số điện thoại:", customer.getPhone());
        addInfoRow(panel, "Địa chỉ:", customer.getPlaceOfResidence());
        addInfoRow(panel, "Tên đăng nhập:", customer.getUsername());
        addInfoRow(panel, "Quê quán:", customer.getPlaceOfOrigin());

        return panel;
    }

    // [UI] Tạo nội dung Trạng thái
    private JPanel createStatusInfo(Customer customer, Account acc) {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(0, 1, 5, 5));
        panel.setBackground(COL_CARD);

        String trangThaiKhachHang = "🟢 Đang hoạt động";
        if (customer.getUserStatus() != null && !customer.getUserStatus().isEmpty()) {
            if (customer.getUserStatus().equalsIgnoreCase("INACTIVE")) {
                trangThaiKhachHang = "🔴 Ngừng hoạt động";
            } else if (customer.getUserStatus().equalsIgnoreCase("LOCKED")) {
                trangThaiKhachHang = "🔒 Đã khóa";
            }
        }

        addInfoRow(panel, "Trạng thái KH:", trangThaiKhachHang);
        
        // [LOGIC] Hiển thị trạng thái tài khoản ngân hàng
        if (acc != null) {
            addInfoRow(panel, "Trạng thái TK:", "🟢 Đang hoạt động");
            addInfoRow(panel, "Số dư khả dụng:", formatCurrency(acc.getBalance()));
        } else {
            addInfoRow(panel, "Trạng thái TK:", "🔴 Chưa kích hoạt");
            addInfoRow(panel, "Số dư khả dụng:", "0 VND");
        }

        return panel;
    }

    // [HELPER] Thêm một dòng thông tin (Nhãn : Giá trị)
    private void addInfoRow(JPanel panel, String label, String value) {
        JPanel rowPanel = new JPanel(new BorderLayout());
        rowPanel.setBackground(COL_CARD);

        JLabel lblLabel = new JLabel(label);
        lblLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblLabel.setForeground(Color.DARK_GRAY);

        JLabel lblValue = new JLabel(value);
        lblValue.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        
        // Đổi màu chữ nếu là trạng thái đặc biệt
        if (value.contains("🟢")) {
            lblValue.setForeground(new Color(0, 128, 0));
        } else if (value.contains("🔴") || value.contains("🔒")) {
            lblValue.setForeground(Color.RED);
        } else {
            lblValue.setForeground(Color.BLACK);
        }

        rowPanel.add(lblLabel, BorderLayout.WEST);
        rowPanel.add(lblValue, BorderLayout.EAST);
        panel.add(rowPanel);
    }

    // [UI] Tạo Panel chứa các nút bấm cuối trang
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBackground(COL_BG);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 20, 20));

        JButton btnLamMoi = new JButton("Làm mới thông tin");
        btnLamMoi.setBackground(COL_HEADER);
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLamMoi.addActionListener(e -> refreshData());

        JButton btnDong = new JButton("Đóng");
        btnDong.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnDong.addActionListener(e -> dispose());

        panel.add(btnLamMoi);
        panel.add(btnDong);

        return panel;
    }

    // [HELPER] Định dạng tiền tệ
    private String formatCurrency(java.math.BigDecimal amount) {
        if (amount == null) return "0 VND";
        return String.format("%,d VND", amount.intValue());
    }

    // [HELPER] Định dạng ngày tháng
    private String formatDate(java.time.LocalDateTime dateTime) {
        if (dateTime == null) return "N/A";
        try {
            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy");
            return dateTime.format(formatter);
        } catch (Exception e) {
            return "N/A";
        }
    }

    // [HELPER] Định dạng giới tính
    private String formatGender(String gender) {
        if (gender == null) return "N/A";
        switch (gender.toUpperCase()) {
            case "MALE": return "Nam";
            case "FEMALE": return "Nữ";
            case "OTHER": return "Khác";
            default: return gender;
        }
    }

    // [LOGIC] Làm mới dữ liệu bằng cách đóng và mở lại form
    private void refreshData() {
        this.dispose();
        new TaiKhoanKhachHang().setVisible(true);
    }

    // [LOGIC] Hiển thị thông báo lỗi
    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Lỗi", JOptionPane.ERROR_MESSAGE);
        this.dispose();
    }
}