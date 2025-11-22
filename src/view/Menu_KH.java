package view;

import Service.CustomerService;
import Session.CustomerSession;
import model.Customer;
import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

// [CLASS] Màn hình chính (Dashboard) dành cho Khách hàng
// Tích hợp Sidebar menu và các chức năng: Tài khoản, Chuyển tiền, Nạp/Rút, Lịch sử, Sửa thông tin...
public class Menu_KH extends javax.swing.JFrame {

    // Màu sắc chủ đạo của giao diện
    private final Color COL_SIDEBAR = new Color(44, 62, 80);     // Xám đen
    private final Color COL_ACTIVE = new Color(52, 152, 219);    // Xanh dương (Highlight)
    private final Color COL_CONTENT = new Color(236, 240, 241);  // Xám nhạt (Nền nội dung)
    
    // Khai báo Service
    private final CustomerService customerService = new CustomerService();
    
    // Panel chứa nội dung chính (để thay đổi giao diện động)
    private JPanel pnlContent; 

    // [CONSTRUCTOR] Khởi tạo giao diện
    public Menu_KH() {
        initComponents();
        setLocationRelativeTo(null);
    }

    // [INIT] Thiết lập cấu trúc giao diện (Sidebar + Content)
    private void initComponents() {
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Ngân hàng Trực tuyến - Khách hàng");
        setSize(1000, 600);
        setLayout(new BorderLayout());

        // --- 1. SIDEBAR (MENU TRÁI) ---
        JPanel pnlSidebar = new JPanel();
        pnlSidebar.setBackground(COL_SIDEBAR);
        pnlSidebar.setPreferredSize(new Dimension(250, 600));
        pnlSidebar.setLayout(new GridLayout(9, 1, 0, 10)); 
        pnlSidebar.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));

        JLabel lblBrand = new JLabel("E-BANKING", SwingConstants.CENTER);
        lblBrand.setFont(new Font("Segoe UI", Font.BOLD, 24));
        lblBrand.setForeground(Color.WHITE);
        pnlSidebar.add(lblBrand);
        
        // Các nút chức năng
        JButton btnTaiKhoan = createSidebarButton("Tài khoản của tôi");
        JButton btnChuyenTien = createSidebarButton("Chuyển tiền");
        JButton btnNapTien = createSidebarButton("Nạp tiền");
        JButton btnRutTien = createSidebarButton("Rút tiền");
        JButton btnLichSu = createSidebarButton("Lịch sử giao dịch");
        JButton btnSuaThongTin = createSidebarButton("Sửa thông tin"); 
        JButton btnDoiMatKhau = createSidebarButton("Đổi mật khẩu");
        JButton btnDangXuat = createSidebarButton("Đăng xuất");
        btnDangXuat.setBackground(new Color(192, 57, 43)); // Màu đỏ cho nút thoát

        // --- [FIX] SỰ KIỆN CÁC NÚT ---
        
        // 1. Tài khoản
        btnTaiKhoan.addActionListener(e -> {
            try { openForm(new TaiKhoanKhachHang()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Chưa có file TaiKhoanKhachHang"); }
        });

        // 2. Chuyển tiền
        btnChuyenTien.addActionListener(e -> {
             try { openForm(new ChuyenTien()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Chưa có file ChuyenTien"); }
        });

        // 3. Nạp tiền
        btnNapTien.addActionListener(e -> {
             try { openForm(new NapTien()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Chưa có file NapTien"); }
        });

        // 4. Rút tiền
        btnRutTien.addActionListener(e -> {
             try { openForm(new RutTien()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Chưa có file RutTien"); }
        });

        // 5. Lịch sử
        btnLichSu.addActionListener(e -> {
             try { openForm(new BienLaiGiaoDich()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Chưa có file BienLaiGiaoDich"); }
        });
        
        // 6. Sửa thông tin (Cái này dùng hàm nội bộ showEditForm, không mở form mới)
        btnSuaThongTin.addActionListener(e -> showEditForm());

        // 7. Đổi mật khẩu
        btnDoiMatKhau.addActionListener(e -> {
             try { openForm(new DoiMatKhau()); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Chưa có file DoiMatKhau"); }
        });

        // 8. Đăng xuất
        btnDangXuat.addActionListener(e -> {
            CustomerSession.clear(); // Xóa session
            this.dispose();
            new Login().setVisible(true); // Mở lại form Login
        });

        // Thêm nút vào Sidebar
        pnlSidebar.add(btnTaiKhoan);
        pnlSidebar.add(btnChuyenTien);
        pnlSidebar.add(btnNapTien);
        pnlSidebar.add(btnRutTien);
        pnlSidebar.add(btnLichSu);
        pnlSidebar.add(btnSuaThongTin);
        pnlSidebar.add(btnDoiMatKhau);
        pnlSidebar.add(btnDangXuat);

        add(pnlSidebar, BorderLayout.WEST);

        // --- 2. CONTENT (NỘI DUNG CHÍNH BAN ĐẦU) ---
        pnlContent = new JPanel();
        pnlContent.setBackground(COL_CONTENT);
        pnlContent.setLayout(new BorderLayout()); 
        
        // Mặc định hiển thị màn hình chào mừng
        showWelcomeScreen();

        add(pnlContent, BorderLayout.CENTER);
    }

    // [UI] Hiển thị màn hình chào mừng (Default)
    private void showWelcomeScreen() {
        pnlContent.removeAll();
        JLabel lblWelcome = new JLabel("Xin chào quý khách!", SwingConstants.CENTER);
        lblWelcome.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblWelcome.setForeground(new Color(127, 140, 141));
        pnlContent.add(lblWelcome, BorderLayout.CENTER);
        refreshContent();
    }

    // [UI] Hiển thị Form Sửa thông tin ngay tại panel chính (Không popup)
    private void showEditForm() {
        pnlContent.removeAll(); 

        Customer c = CustomerSession.getLoggedInCustomer();
        if (c == null) return;

        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(new EmptyBorder(20, 50, 20, 50));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề form
        JLabel lblTitle = new JLabel("CẬP NHẬT THÔNG TIN");
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lblTitle.setForeground(COL_SIDEBAR);
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        formPanel.add(lblTitle, gbc);

        gbc.gridwidth = 1;

        // CCCD (Không cho sửa - Read Only)
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("CCCD (Không thể sửa):"), gbc);
        
        JTextField txtCccd = new JTextField(c.getCitizenID());
        txtCccd.setEditable(false);
        txtCccd.setBackground(new Color(230, 230, 230)); 
        gbc.gridx = 1;
        formPanel.add(txtCccd, gbc);

        // Họ tên
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Họ và tên:"), gbc);
        JTextField txtName = new JTextField(c.getFullName());
        gbc.gridx = 1;
        formPanel.add(txtName, gbc);

        // Ngày sinh
        gbc.gridx = 0; gbc.gridy = 3;
        formPanel.add(new JLabel("Ngày sinh:"), gbc);
        JTextField txtDob = new JTextField(c.getDateOfBirth());
        gbc.gridx = 1;
        formPanel.add(txtDob, gbc);
        
        // Email
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Email:"), gbc);
        JTextField txtEmail = new JTextField(c.getEmail());
        gbc.gridx = 1;
        formPanel.add(txtEmail, gbc);

        // SĐT
        gbc.gridx = 0; gbc.gridy = 5;
        formPanel.add(new JLabel("Số điện thoại:"), gbc);
        JTextField txtPhone = new JTextField(c.getPhone());
        gbc.gridx = 1;
        formPanel.add(txtPhone, gbc);

        // Địa chỉ
        gbc.gridx = 0; gbc.gridy = 6;
        formPanel.add(new JLabel("Địa chỉ:"), gbc);
        JTextField txtAddress = new JTextField(c.getPlaceOfResidence());
        gbc.gridx = 1;
        formPanel.add(txtAddress, gbc);

        // Nút Lưu
        JButton btnSave = new JButton("LƯU THAY ĐỔI");
        btnSave.setBackground(new Color(46, 204, 113));
        btnSave.setForeground(Color.WHITE);
        btnSave.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridx = 1; gbc.gridy = 7;
        formPanel.add(btnSave, gbc);

        // Xử lý sự kiện Lưu thông tin mới
        btnSave.addActionListener(evt -> {
            try {
                c.setFullName(txtName.getText());
                c.setDateOfBirth(txtDob.getText());
                c.setEmail(txtEmail.getText());
                c.setPhone(txtPhone.getText());
                c.setPlaceOfResidence(txtAddress.getText());

                customerService.updateInfor(c); // Update DB
                CustomerSession.setLoggedInCustomer(c); // Update Session
                
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + ex.getMessage());
            }
        });

        pnlContent.add(formPanel, BorderLayout.CENTER);
        refreshContent();
    }

    // [HELPER] Vẽ lại Panel Content sau khi thay đổi
    private void refreshContent() {
        pnlContent.revalidate();
        pnlContent.repaint();
    }

    // [HELPER] Tạo nút Sidebar đẹp (Hover effect)
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btn.setForeground(Color.WHITE);
        btn.setBackground(COL_SIDEBAR);
        btn.setBorderPainted(false);
        btn.setFocusPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) { btn.setBackground(COL_ACTIVE); }
            public void mouseExited(java.awt.event.MouseEvent evt) { 
                if(!btn.getText().equals("Đăng xuất")) btn.setBackground(COL_SIDEBAR); 
            }
        });
        return btn;
    }

    // [HELPER] Mở form con (JFrame) ở cửa sổ mới
    private void openForm(javax.swing.JFrame frame) {
        frame.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new Menu_KH().setVisible(true));
    }
}