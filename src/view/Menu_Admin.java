package view;

import javax.swing.*;
import java.awt.*;
import Session.AdminSession;
import model.Admin;

// [CLASS] Màn hình chính (Dashboard) dành cho Quản trị viên (Admin)
// Tích hợp các chức năng quản trị hệ thống: Quản lý khách hàng, Thông tin Admin, Báo cáo thống kê
public class Menu_Admin extends javax.swing.JFrame {

    // [CONSTRUCTOR] Khởi tạo giao diện Admin
    public Menu_Admin() {
        initComponents();
        setLocationRelativeTo(null);
        styleButtons();
        setWelcomeMessage();
    }

    // [UI] Hiển thị lời chào Admin (Lấy tên từ Session)
    private void setWelcomeMessage() {
        try {
            Admin currentAdmin = AdminSession.getLoggedInAdmin();
            if (currentAdmin != null) {
                jLabel1.setText("Xin chào, " + currentAdmin.getFullName() + "!");
            } else {
                jLabel1.setText("Xin chào, Quản trị viên!");
            }
        } catch (Exception e) {
            jLabel1.setText("Xin chào, Quản trị viên!");
        }
    }

    // [INIT] Thiết lập cấu trúc giao diện
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        btnQuanLyKhachHang = new javax.swing.JButton();
        btnThongTinAdmin = new javax.swing.JButton();
        btnBaoCaoThongKe = new javax.swing.JButton();
        btnDangXuat = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hệ thống Ngân hàng - Admin");
        setPreferredSize(new Dimension(800, 600));

        // --- HEADER PANEL ---
        jPanel1.setBackground(new java.awt.Color(0, 102, 102));
        lblTitle.setFont(new java.awt.Font("Segoe UI", 1, 28));
        lblTitle.setForeground(new java.awt.Color(255, 255, 255));
        lblTitle.setHorizontalAlignment(SwingConstants.CENTER);
        lblTitle.setText("HỆ THỐNG QUẢN TRỊ NGÂN HÀNG");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle, javax.swing.GroupLayout.DEFAULT_SIZE, 788, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(lblTitle)
                .addGap(30, 30, 30))
        );

        // --- BODY PANEL ---
        jPanel2.setBackground(new java.awt.Color(240, 245, 249));
        
        // Các nút chức năng chính
        btnQuanLyKhachHang.setFont(new java.awt.Font("Segoe UI", 1, 18));
        btnQuanLyKhachHang.setText("QUẢN LÝ KHÁCH HÀNG");
        btnQuanLyKhachHang.setBackground(new java.awt.Color(52, 152, 219));
        btnQuanLyKhachHang.setForeground(Color.WHITE);
        btnQuanLyKhachHang.addActionListener(e -> openQLKH());

        btnThongTinAdmin.setFont(new java.awt.Font("Segoe UI", 1, 18));
        btnThongTinAdmin.setText("THÔNG TIN ADMIN");
        btnThongTinAdmin.setBackground(new java.awt.Color(155, 89, 182));
        btnThongTinAdmin.setForeground(Color.WHITE);
        btnThongTinAdmin.addActionListener(e -> openAdminInfo());

        btnBaoCaoThongKe.setFont(new java.awt.Font("Segoe UI", 1, 18));
        btnBaoCaoThongKe.setText("BÁO CÁO & THỐNG KÊ");
        btnBaoCaoThongKe.setBackground(new java.awt.Color(39, 174, 96));
        btnBaoCaoThongKe.setForeground(Color.WHITE);
        btnBaoCaoThongKe.addActionListener(e -> openBaoCaoThongKe());

        // Nút Đăng xuất (Góc phải dưới)
        btnDangXuat.setBackground(new java.awt.Color(231, 76, 60));
        btnDangXuat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // Font nhỏ hơn xíu cho tinh tế
        btnDangXuat.setForeground(new java.awt.Color(255, 255, 255));
        btnDangXuat.setText("Đăng xuất");
        btnDangXuat.addActionListener(e -> logout());

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 20));
        jLabel1.setForeground(new java.awt.Color(44, 62, 80));
        jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
        jLabel1.setText("Xin chào, Quản trị viên!");

        // --- CẤU HÌNH LAYOUT ---
        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(150, Short.MAX_VALUE)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnQuanLyKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThongTinAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnBaoCaoThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(150, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );
        
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(40, 40, 40)
                .addComponent(jLabel1)
                .addGap(50, 50, 50)
                .addComponent(btnQuanLyKhachHang, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnThongTinAdmin, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(btnBaoCaoThongKe, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE) 
                .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }

    // [UI] Thiết lập style cho các nút bấm (Hover effect)
    private void styleButtons() {
        javax.swing.JButton[] buttons = {
            btnQuanLyKhachHang, btnThongTinAdmin, btnBaoCaoThongKe
        };
        
        for (javax.swing.JButton button : buttons) {
            button.setFocusPainted(false);
            button.setBorderPainted(false);
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            
            button.addMouseListener(new java.awt.event.MouseAdapter() {
                Color originalColor = button.getBackground();
                
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    button.setBackground(originalColor.darker());
                }
                
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    button.setBackground(originalColor);
                }
            });
        }
        
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.setBorderPainted(false);
        btnDangXuat.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnDangXuat.addMouseListener(new java.awt.event.MouseAdapter() {
             Color originalColor = btnDangXuat.getBackground();
             public void mouseEntered(java.awt.event.MouseEvent evt) {
                 btnDangXuat.setBackground(originalColor.darker());
             }
             public void mouseExited(java.awt.event.MouseEvent evt) {
                 btnDangXuat.setBackground(originalColor);
             }
        });
    }
    
    // [ACTION] Mở chức năng Quản lý Khách hàng
    private void openQLKH() {
        try {
            DanhSachKhachHang f = new DanhSachKhachHang();
            f.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
            f.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi mở quản lý khách hàng: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // [ACTION] Mở thông tin cá nhân của Admin
    private void openAdminInfo() {
        if (!AdminSession.isLoggedIn()) {
            JOptionPane.showMessageDialog(this, 
                "Phiên đăng nhập không tồn tại. Vui lòng đăng nhập lại.", 
                "Lỗi Session", 
                JOptionPane.ERROR_MESSAGE);
            redirectToLogin();
            return;
        }
    
        Admin currentAdmin = AdminSession.getLoggedInAdmin();
    
        if (currentAdmin != null) {
            try {
                QuanTriVien adminInfoForm = new QuanTriVien(currentAdmin);
                adminInfoForm.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
                adminInfoForm.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, 
                    "Lỗi khi mở thông tin admin: " + e.getMessage(), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, 
                "Không thể lấy thông tin admin. Phiên đăng nhập có thể đã hết hạn.\nVui lòng đăng nhập lại.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            redirectToLogin();
        }
    }
    
    // [ACTION] Mở Báo cáo thống kê
    private void openBaoCaoThongKe() {
        try {
            BaoCaoThongKe baoCaoForm = new BaoCaoThongKe();
            baoCaoForm.setDefaultCloseOperation(javax.swing.JFrame.DISPOSE_ON_CLOSE);
            baoCaoForm.setVisible(true);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, 
                "Lỗi khi mở báo cáo thống kê: " + e.getMessage(), 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // [ACTION] Xử lý Đăng xuất
    private void logout() {
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn đăng xuất khỏi hệ thống?", 
            "Xác nhận đăng xuất",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
            
        if (confirm == JOptionPane.YES_OPTION) {
            AdminSession.clear();
            JOptionPane.showMessageDialog(this, 
                "Đăng xuất thành công!", 
                "Thông báo",
                JOptionPane.INFORMATION_MESSAGE);
            
            // Quay lại màn hình login
            EventQueue.invokeLater(() -> {
                new Login().setVisible(true);
            });
            dispose();
        }
    }

    // [HELPER] Chuyển hướng về Login khi mất session
    private void redirectToLogin() {
        AdminSession.clear();
        EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
        dispose();
    }

    public static void main(String[] args) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception e) {
           e.printStackTrace();
        }
        
        java.awt.EventQueue.invokeLater(() -> {
            new Menu_Admin().setVisible(true);
        });
    }

    // Khai báo biến
    private javax.swing.JButton btnBaoCaoThongKe;
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnQuanLyKhachHang;
    private javax.swing.JButton btnThongTinAdmin;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JLabel lblTitle;
}