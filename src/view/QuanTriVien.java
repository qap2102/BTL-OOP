package view;

import model.Admin;
import javax.swing.*;
import java.awt.*;

// [CLASS] Màn hình hiển thị thông tin chi tiết của Quản trị viên (Admin Profile)
public class QuanTriVien extends javax.swing.JFrame {
    private Admin currentAdmin;

    // [CONSTRUCTOR] Khởi tạo giao diện và nhận dữ liệu Admin
    public QuanTriVien(Admin admin) {
        this.currentAdmin = admin;
        initComponents();
        setLocationRelativeTo(null);
        loadData();
        styleComponents();
    }

    // [LOAD] Hiển thị thông tin Admin lên các Label
    private void loadData() {
        if (currentAdmin != null) {
            lblMaNV.setText(String.valueOf(currentAdmin.getAdminID()));
            lblHoTen.setText(currentAdmin.getFullName());
            lblChucVu.setText(currentAdmin.getRole());
            lblEmail.setText(currentAdmin.getEmail());
            lblUsername.setText(currentAdmin.getUsername());
            // Định dạng ngày tạo
            lblNgayTao.setText(currentAdmin.getCreatedAt() != null ? 
                currentAdmin.getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")) : "N/A");
        }
    }

    // [STYLE] Tùy chỉnh giao diện (Font, màu sắc) cho đẹp hơn
    private void styleComponents() {
        // Style cho Panel chứa thông tin
        pnlInfo.setBackground(Color.WHITE);
        pnlInfo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 200, 200), 1),
            BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        
        // Style cho các Label (Tiêu đề in đậm, Nội dung thường)
        Component[] components = pnlInfo.getComponents();
        for (Component comp : components) {
            if (comp instanceof JLabel) {
                JLabel label = (JLabel) comp;
                // Nếu là label tiêu đề (có dấu :) thì in đậm, màu xám
                if (label.getText().contains(":")) {
                    label.setForeground(new Color(102, 102, 102));
                    label.setFont(new Font("Segoe UI", Font.BOLD, 14));
                } else {
                    // Nếu là label nội dung thì chữ thường, màu đen
                    label.setForeground(Color.BLACK);
                    label.setFont(new Font("Segoe UI", Font.PLAIN, 14));
                }
            }
        }
    }

    // [INIT] Khởi tạo các thành phần giao diện (Generated Code)
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        pnlInfo = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        lblMaNV = new javax.swing.JLabel();
        lblHoTen = new javax.swing.JLabel();
        lblChucVu = new javax.swing.JLabel();
        lblEmail = new javax.swing.JLabel();
        lblUsername = new javax.swing.JLabel();
        lblNgayTao = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        btnDong = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Hồ sơ Quản trị viên");
        setResizable(false);

        // Header
        jPanel1.setBackground(new java.awt.Color(0, 102, 102));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24));
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("THÔNG TIN QUẢN TRỊ VIÊN");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(jLabel1)
                .addGap(20, 20, 20))
        );

        // Panel Info
        pnlInfo.setBackground(new java.awt.Color(255, 255, 255));

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel2.setText("Mã nhân viên:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel3.setText("Họ và tên:");

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel4.setText("Chức vụ:");

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel5.setText("Email liên hệ:");

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel6.setText("Tên đăng nhập:");

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 14));
        jLabel7.setText("Ngày tạo tài khoản:");

        lblMaNV.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblMaNV.setText("...");

        lblHoTen.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblHoTen.setText("...");

        lblChucVu.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblChucVu.setText("...");

        lblEmail.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblEmail.setText("...");

        lblUsername.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblUsername.setText("...");

        lblNgayTao.setFont(new java.awt.Font("Segoe UI", 0, 14));
        lblNgayTao.setText("...");

        btnDong.setText("Đóng");
        btnDong.setBackground(new java.awt.Color(0, 102, 102));
        btnDong.setFont(new java.awt.Font("Segoe UI", 1, 14));
        btnDong.setForeground(new java.awt.Color(255, 255, 255));
        btnDong.addActionListener(evt -> this.dispose());

        // Layout
        javax.swing.GroupLayout pnlInfoLayout = new javax.swing.GroupLayout(pnlInfo);
        pnlInfo.setLayout(pnlInfoLayout);
        pnlInfoLayout.setHorizontalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 430, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlInfoLayout.createSequentialGroup()
                        .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, 120, Short.MAX_VALUE)
                            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(30, 30, 30)
                        .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblNgayTao, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblChucVu, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblHoTen, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblMaNV, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlInfoLayout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnDong, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(30, 30, 30))
        );
        pnlInfoLayout.setVerticalGroup(
            pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlInfoLayout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(lblMaNV))
                .addGap(15, 15, 15)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(lblHoTen))
                .addGap(15, 15, 15)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(lblChucVu))
                .addGap(15, 15, 15)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(lblUsername))
                .addGap(15, 15, 15)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(lblEmail))
                .addGap(15, 15, 15)
                .addGroup(pnlInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(lblNgayTao))
                .addGap(30, 30, 30)
                .addComponent(btnDong, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(pnlInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }


    // Variables declaration
    private javax.swing.JButton btnDong;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblChucVu;
    private javax.swing.JLabel lblEmail;
    private javax.swing.JLabel lblHoTen;
    private javax.swing.JLabel lblMaNV;
    private javax.swing.JLabel lblNgayTao;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPanel pnlInfo;
}