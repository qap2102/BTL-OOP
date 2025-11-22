package view;

import Service.AdminService;
import java.awt.Color;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import model.Transaction;

// [CLASS] Màn hình Báo cáo & Thống kê (Dashboard) dành cho Admin
public class BaoCaoThongKe extends javax.swing.JFrame {

    private final AdminService adminService = new AdminService();
    private DefaultTableModel tableModel;

    // [CONSTRUCTOR] Khởi tạo giao diện và tải dữ liệu thống kê
    public BaoCaoThongKe() {
        initComponents();
        setLocationRelativeTo(null);
        
        initTable();
        loadStatistics();
        loadTransactionHistory();
    }

    // [INIT] Cấu hình bảng lịch sử giao dịch
    private void initTable() {
        tableModel = new DefaultTableModel();
        tableModel.addColumn("Mã GD");
        tableModel.addColumn("Loại");
        tableModel.addColumn("Số tiền");
        tableModel.addColumn("Nguồn -> Đích");
        tableModel.addColumn("Thời gian");
        tableModel.addColumn("Nội dung");
        tblGiaoDich.setModel(tableModel);

        tblGiaoDich.getColumnModel().getColumn(0).setPreferredWidth(50);
        tblGiaoDich.getColumnModel().getColumn(4).setPreferredWidth(150);
        tblGiaoDich.getColumnModel().getColumn(5).setPreferredWidth(200);
    }

    // [LOAD] Tải và hiển thị các con số thống kê (Tổng khách hàng, Tổng tiền hệ thống)
    private void loadStatistics() {
        try {
            int totalCustomers = adminService.getTotalCustomers();
            java.math.BigDecimal totalBalance = adminService.getTotalSystemBalance();

            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            
            lblTotalCustomers.setText(String.valueOf(totalCustomers));
            lblTotalMoney.setText(currencyFormat.format(totalBalance));
            
        } catch (Exception e) {
            e.printStackTrace();
            lblTotalCustomers.setText("Lỗi");
            lblTotalMoney.setText("Lỗi");
        }
    }

    // [LOAD] Tải và hiển thị danh sách giao dịch gần đây nhất của toàn hệ thống
    private void loadTransactionHistory() {
        try {
            List<Transaction> list = adminService.getRecentTransactions();
            tableModel.setRowCount(0); // Xóa dữ liệu cũ
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

            for (Transaction t : list) {
                // Kiểm tra null để tránh lỗi NullPointerException khi hiển thị nguồn/đích
                Object fromAccount = (t.getFromAccountID() == null) ? "Nạp tiền" : t.getFromAccountID();
                Object toAccount = (t.getToAccountID() == null) ? "Rút tiền" : t.getToAccountID();

                String sourceDest = fromAccount + " -> " + toAccount;
                
                tableModel.addRow(new Object[]{
                    t.getTransactionID(),
                    t.getTransactionType(),
                    currencyFormat.format(t.getAmount()),
                    sourceDest,
                    t.getCreatedAt().format(formatter),
                    t.getTransactionContent()
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi tải lịch sử giao dịch: " + e.getMessage());
        }
    }

    // [INIT] Khởi tạo các thành phần giao diện (Generated Code)
    @SuppressWarnings("unchecked")
    private void initComponents() {
        jPanelHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        
        jPanelStats = new javax.swing.JPanel();
        pnlCustomer = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        lblTotalCustomers = new javax.swing.JLabel();
        pnlMoney = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        lblTotalMoney = new javax.swing.JLabel();

        jScrollPane1 = new javax.swing.JScrollPane();
        tblGiaoDich = new javax.swing.JTable();
        btnLamMoi = new javax.swing.JButton();
        btnDong = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Báo cáo & Thống kê Hệ thống");

        // Header
        jPanelHeader.setBackground(new java.awt.Color(0, 102, 102));
        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 24)); 
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("DASHBOARD THỐNG KÊ");
        jPanelHeader.add(jLabel1);

        // Panel Thống kê (Grid 1 hàng 2 cột)
        jPanelStats.setLayout(new java.awt.GridLayout(1, 2, 20, 0)); 

        // Card 1: Tổng khách hàng
        pnlCustomer.setBackground(new java.awt.Color(52, 152, 219)); // Xanh dương
        pnlCustomer.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); 
        jLabel2.setForeground(Color.WHITE);
        jLabel2.setText("TỔNG KHÁCH HÀNG");
        
        lblTotalCustomers.setFont(new java.awt.Font("Segoe UI", 1, 36)); 
        lblTotalCustomers.setForeground(Color.WHITE);
        lblTotalCustomers.setText("0");
        
        javax.swing.GroupLayout l1 = new javax.swing.GroupLayout(pnlCustomer);
        pnlCustomer.setLayout(l1);
        l1.setHorizontalGroup(l1.createParallelGroup().addComponent(jLabel2).addComponent(lblTotalCustomers));
        l1.setVerticalGroup(l1.createSequentialGroup().addComponent(jLabel2).addGap(10).addComponent(lblTotalCustomers));

        // Card 2: Tổng tiền hệ thống
        pnlMoney.setBackground(new java.awt.Color(46, 204, 113)); // Xanh lá
        pnlMoney.setBorder(javax.swing.BorderFactory.createEmptyBorder(20, 20, 20, 20));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 18)); 
        jLabel3.setForeground(Color.WHITE);
        jLabel3.setText("TỔNG DÒNG TIỀN");
        
        lblTotalMoney.setFont(new java.awt.Font("Segoe UI", 1, 36)); 
        lblTotalMoney.setForeground(Color.WHITE);
        lblTotalMoney.setText("0 đ");
        
        javax.swing.GroupLayout l2 = new javax.swing.GroupLayout(pnlMoney);
        pnlMoney.setLayout(l2);
        l2.setHorizontalGroup(l2.createParallelGroup().addComponent(jLabel3).addComponent(lblTotalMoney));
        l2.setVerticalGroup(l2.createSequentialGroup().addComponent(jLabel3).addGap(10).addComponent(lblTotalMoney));

        jPanelStats.add(pnlCustomer);
        jPanelStats.add(pnlMoney);

        // Bảng giao dịch
        jScrollPane1.setViewportView(tblGiaoDich);

        // Nút bấm
        btnLamMoi.setText("Làm mới dữ liệu");
        btnLamMoi.addActionListener(evt -> {
            loadStatistics();
            loadTransactionHistory();
        });

        btnDong.setText("Đóng");
        btnDong.addActionListener(evt -> this.dispose());

        jLabel4.setFont(new java.awt.Font("Segoe UI", 1, 14)); 
        jLabel4.setText("Giao dịch gần đây (Toàn hệ thống):");

        // Layout chính
        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelHeader, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanelStats, javax.swing.GroupLayout.DEFAULT_SIZE, 760, Short.MAX_VALUE)
                    .addComponent(jScrollPane1)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnLamMoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDong)))
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelHeader, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jPanelStats, javax.swing.GroupLayout.PREFERRED_SIZE, 130, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(20, 20, 20)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnDong)
                    .addComponent(btnLamMoi))
                .addGap(20, 20, 20))
        );

        pack();
    }
    
    // Khai báo biến giao diện
    private javax.swing.JButton btnDong;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanelHeader;
    private javax.swing.JPanel jPanelStats;
    private javax.swing.JPanel pnlCustomer;
    private javax.swing.JPanel pnlMoney;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblTotalCustomers;
    private javax.swing.JLabel lblTotalMoney;
    private javax.swing.JTable tblGiaoDich;
}