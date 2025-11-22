package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import Service.AdminService;
import model.Customer;

// [CLASS] Màn hình Danh sách Khách hàng (Chức năng chính của Admin)
public class DanhSachKhachHang extends javax.swing.JFrame {

    private DefaultTableModel tableModel;
    private AdminService adminService;

    // [CONSTRUCTOR] Khởi tạo giao diện và tải dữ liệu ban đầu
    public DanhSachKhachHang() {
        initComponents();
        this.setSize(1100, 650);
        this.setLocationRelativeTo(null);
        
        adminService = new AdminService();
        initTable();
        loadCustomerData();
    }

    // [INIT] Cấu hình bảng danh sách (Cột, độ rộng, font chữ)
    private void initTable() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã KH");
        tableModel.addColumn("Tên đăng nhập");
        tableModel.addColumn("Họ tên");
        tableModel.addColumn("CCCD");
        tableModel.addColumn("Email");
        tableModel.addColumn("SĐT");
        tableModel.addColumn("Trạng thái");
        
        jTable1.setModel(tableModel);
        jTable1.setRowHeight(25);
        jTable1.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 12));
        
        // Cho phép chọn nhiều dòng để thực hiện thao tác hàng loạt (VD: Khóa nhiều TK)
        jTable1.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        
        // Chỉnh độ rộng cột
        jTable1.getColumnModel().getColumn(0).setPreferredWidth(60);
        jTable1.getColumnModel().getColumn(1).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(2).setPreferredWidth(150);
        jTable1.getColumnModel().getColumn(3).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(4).setPreferredWidth(150);
        jTable1.getColumnModel().getColumn(5).setPreferredWidth(100);
        jTable1.getColumnModel().getColumn(6).setPreferredWidth(120);
    }

    // [LOAD] Tải danh sách khách hàng từ DB lên bảng và cập nhật thống kê
    public void loadCustomerData() {
        try {
            List<Customer> customers = adminService.getAllCustomers();
            tableModel.setRowCount(0); // Xóa dữ liệu cũ
            
            if (customers.isEmpty()) return;
            
            for (Customer customer : customers) {
                Object[] row = {
                    customer.getCustomerID(),
                    customer.getUsername(),
                    customer.getFullName(),
                    customer.getCitizenID(),
                    customer.getEmail(),
                    customer.getPhone(),
                    formatStatus(customer.getUserStatus()) // Format trạng thái cho đẹp
                };
                tableModel.addRow(row);
            }
            // Cập nhật số lượng và tổng tiền
            updateStatistics(customers.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // [HELPER] Định dạng hiển thị trạng thái (Thêm icon màu sắc)
    private String formatStatus(String status) {
        if (status == null || status.isEmpty()) return "Đang hoạt động";
        switch (status.toUpperCase()) {
            case "ACTIVE": return "🟢 Đang hoạt động";
            case "INACTIVE": return "🔴 Ngừng hoạt động";
            case "LOCKED": return "🔒 ĐÃ KHÓA";
            default: return status;
        }
    }

    // [HELPER] Cập nhật các ô thống kê (Tổng KH, Tổng tiền)
    private void updateStatistics(int totalCustomers) {
        jTextField2.setText(String.valueOf(totalCustomers));
        try {
            java.math.BigDecimal totalBalance = adminService.getTotalSystemBalance();
            jTextField3.setText(String.format("%,d VND", totalBalance.intValue()));
        } catch (Exception e) {
            jTextField3.setText("0 VND");
        }
    }

    // [INIT] Khởi tạo các thành phần giao diện (Generated Code)
    @SuppressWarnings("unchecked")
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        // Đã xóa jButton3 (Xuất dữ liệu)
        jButton4 = new javax.swing.JButton();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Quản lý Khách hàng");

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 14));
        jLabel1.setText("Tìm KH:");

        jButton1.setText("Tìm");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {}, new String [] {}
        ));
        jScrollPane1.setViewportView(jTable1);
        jScrollPane1.setPreferredSize(new Dimension(800, 400));

        jButton2.setText("Thoát");
        jButton2.addActionListener(evt -> dispose());


        jButton4.setText("Làm mới");
        jButton4.addActionListener(evt -> {
            loadCustomerData();
            JOptionPane.showMessageDialog(this, "Đã làm mới dữ liệu.");
        });

        jTextField2.setEditable(false);
        jTextField2.setFont(new java.awt.Font("Segoe UI", 1, 16));
        jTextField2.setHorizontalAlignment(JTextField.CENTER);

        jTextField3.setEditable(false);
        jTextField3.setFont(new java.awt.Font("Segoe UI", 1, 16));
        jTextField3.setForeground(new java.awt.Color(0, 102, 51));
        jTextField3.setHorizontalAlignment(JTextField.CENTER);

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        jLabel2.setText("Tổng khách hàng:");

        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        jLabel3.setText("Tổng tiền hệ thống:");

        jButton6.setText("Khóa (Nhiều TK)");
        jButton6.setBackground(new java.awt.Color(255, 102, 102));
        jButton6.setForeground(Color.WHITE);
        jButton6.addActionListener(evt -> jButton6ActionPerformed(evt));

        jButton7.setText("Mở khóa TK");
        jButton7.setBackground(new java.awt.Color(0, 153, 102));
        jButton7.setForeground(Color.WHITE);
        jButton7.addActionListener(evt -> jButton7ActionPerformed(evt));

        jButton8.setText("Quản lý chi tiết");
        jButton8.setBackground(new java.awt.Color(51, 102, 255));
        jButton8.setForeground(Color.WHITE);
        jButton8.setFont(new java.awt.Font("Segoe UI", 1, 12));
        jButton8.addActionListener(evt -> jButton8ActionPerformed(evt));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 150, Short.MAX_VALUE)
                        .addComponent(jButton6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton8))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE) 
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(50, 50, 50)
                        .addComponent(jLabel3)
                        .addGap(18, 18, 18)
                        .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)) 
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton2)))
                .addGap(20, 20, 20))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 400, Short.MAX_VALUE)
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.CENTER)
                    .addComponent(jLabel2)
                    .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }

    // --- SỰ KIỆN (ACTIONS) ---

    // [ACTION] Tìm kiếm khách hàng
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        String keyword = jTextField1.getText().trim();
        if (keyword.isEmpty()) { loadCustomerData(); return; }
        try {
            List<Customer> customers = adminService.searchCustomers(keyword);
            tableModel.setRowCount(0);
            for (Customer c : customers) {
                tableModel.addRow(new Object[]{
                    c.getCustomerID(), c.getUsername(), c.getFullName(), c.getCitizenID(),
                    c.getEmail(), c.getPhone(), formatStatus(c.getUserStatus())
                });
            }
        } catch (Exception e) { e.printStackTrace(); }
    }

    // [ACTION] Khóa tài khoản hàng loạt (Nút màu đỏ)
    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        int[] selectedRows = jTable1.getSelectedRows();
        
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một khách hàng!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn KHÓA " + selectedRows.length + " tài khoản đã chọn?", 
            "Xác nhận khóa hàng loạt", 
            JOptionPane.YES_NO_OPTION);
        
        if (confirm == JOptionPane.YES_OPTION) {
            try {
                int successCount = 0;
                for (int row : selectedRows) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    String currentStatus = (String) tableModel.getValueAt(row, 6);
                    // Chỉ khóa những tài khoản chưa bị khóa
                    if (!currentStatus.contains("KHÓA")) {
                        adminService.lockCustomerAccount(id);
                        successCount++;
                    }
                }
                loadCustomerData();
                JOptionPane.showMessageDialog(this, "Đã khóa thành công " + successCount + " tài khoản.");
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    // [ACTION] Mở khóa tài khoản (Nút màu xanh lá)
    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {
        int[] selectedRows = jTable1.getSelectedRows();
        if (selectedRows.length == 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần mở khóa!");
            return;
        }
        
        if (JOptionPane.showConfirmDialog(this, "Mở khóa các tài khoản đã chọn?", "Xác nhận", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            try {
                for (int row : selectedRows) {
                    int id = (int) tableModel.getValueAt(row, 0);
                    adminService.unlockCustomerAccount(id);
                }
                loadCustomerData();
                JOptionPane.showMessageDialog(this, "Đã mở khóa thành công.");
            } catch (Exception e) { e.printStackTrace(); }
        }
    }

    // [ACTION] Mở màn hình Quản lý chi tiết và gắn Callback cập nhật
    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {
        int selectedRow = jTable1.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn 1 khách hàng để xem chi tiết!");
            return;
        }
        
        int id = (int) tableModel.getValueAt(selectedRow, 0);
        String name = (String) tableModel.getValueAt(selectedRow, 2);
        
        QuanLyKhachHang ql = new QuanLyKhachHang(id, name);
        ql.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        
        // [CALLBACK] Khi bên cửa sổ con báo thay đổi, cửa sổ này (cha) sẽ load lại dữ liệu ngay lập tức
        ql.setCallback(() -> {
            System.out.println("Nhận tín hiệu thay đổi từ cửa sổ chi tiết. Đang cập nhật...");
            loadCustomerData();
        });
        
        ql.setVisible(true);
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> new DanhSachKhachHang().setVisible(true));
    }

    private javax.swing.JButton jButton1, jButton2, jButton4, jButton6, jButton7, jButton8;
    private javax.swing.JLabel jLabel1, jLabel2, jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    private javax.swing.JTextField jTextField1, jTextField2, jTextField3;
}