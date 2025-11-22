package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import Service.AdminService;
import model.Customer;
import model.Transaction;
import model.Account;
import java.time.format.DateTimeFormatter;

// [CLASS] Màn hình quản lý chi tiết một khách hàng (Dành cho Admin)
// Chức năng: Xem/Sửa thông tin, Nạp/Rút tiền, Xem lịch sử giao dịch
public class QuanLyKhachHang extends javax.swing.JFrame {

    private DefaultTableModel transactionTableModel;
    private AdminService adminService;
    private int customerId;
    private String customerName;
    private Customer currentCustomer;
    
    // Biến callback để báo cho cửa sổ cha (Menu_Admin) cập nhật lại bảng danh sách sau khi có thay đổi
    private Runnable onStatusChange;

    // [CONSTRUCTOR] Khởi tạo màn hình với ID và Tên khách hàng cần quản lý
    public QuanLyKhachHang(int customerId, String customerName) {
        this.customerId = customerId;
        this.customerName = customerName;
        
        initComponents();
        
        setLocationRelativeTo(null);
        adminService = new AdminService();
        
        initTransactionTable();
        loadCustomerData();
        loadTransactionHistory();
        
        setTitle("Quản lý Khách hàng - " + customerName);
    }
    
    // [CALLBACK] Gán sự kiện cập nhật từ bên ngoài (Observer Pattern đơn giản)
    public void setCallback(Runnable onStatusChange) {
        this.onStatusChange = onStatusChange;
    }

    // [INIT] Khởi tạo các thành phần giao diện (Tabs, Form, Table)
    private void initComponents() {
        jTabbedPane1 = new JTabbedPane();
        
        // --- TAB 1: THÔNG TIN & TÀI KHOẢN ---
        JPanel panelInfo = new JPanel(new BorderLayout(10, 10));
        panelInfo.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 15));
        
        jTextFieldUsername = new JTextField(); jTextFieldUsername.setEditable(false);
        jTextFieldFullName = new JTextField();
        jTextFieldCitizenID = new JTextField(); jTextFieldCitizenID.setEditable(false); 
        jTextFieldEmail = new JTextField();
        jTextFieldPhone = new JTextField();
        jTextFieldAddress = new JTextField();
        
        jTextFieldAccountNumber = new JTextField(); jTextFieldAccountNumber.setEditable(false);
        jTextFieldBalance = new JTextField(); jTextFieldBalance.setEditable(false);
        jTextFieldBalance.setFont(new Font("Segoe UI", Font.BOLD, 14));
        jTextFieldBalance.setForeground(new Color(0, 102, 51));
        jTextFieldAccountStatus = new JTextField(); jTextFieldAccountStatus.setEditable(false);
        jTextFieldAccountStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));

        formPanel.add(new JLabel("Tên đăng nhập:")); formPanel.add(jTextFieldUsername);
        formPanel.add(new JLabel("Họ và tên:")); formPanel.add(jTextFieldFullName);
        formPanel.add(new JLabel("CCCD (Không thể sửa):")); formPanel.add(jTextFieldCitizenID);
        formPanel.add(new JLabel("Email:")); formPanel.add(jTextFieldEmail);
        formPanel.add(new JLabel("SĐT:")); formPanel.add(jTextFieldPhone);
        formPanel.add(new JLabel("Địa chỉ:")); formPanel.add(jTextFieldAddress);
        formPanel.add(new JSeparator()); formPanel.add(new JSeparator());
        formPanel.add(new JLabel("Số tài khoản:")); formPanel.add(jTextFieldAccountNumber);
        formPanel.add(new JLabel("Số dư hiện tại:")); formPanel.add(jTextFieldBalance);
        formPanel.add(new JLabel("Trạng thái TK:")); formPanel.add(jTextFieldAccountStatus);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        // Các nút chức năng Admin có thể thực hiện thay khách
        jButtonUpdateInfo = new JButton("Cập nhật Info");
        jButtonDeposit = new JButton("Nạp tiền");
        jButtonWithdraw = new JButton("Rút tiền");
        jButtonTransfer = new JButton("Chuyển khoản");
        
        jButtonUpdateInfo.setBackground(new Color(52, 152, 219)); jButtonUpdateInfo.setForeground(Color.WHITE);
        jButtonDeposit.setBackground(new Color(46, 204, 113)); jButtonDeposit.setForeground(Color.WHITE);
        jButtonWithdraw.setBackground(new Color(241, 196, 15)); jButtonWithdraw.setForeground(Color.BLACK);
        jButtonTransfer.setBackground(new Color(155, 89, 182)); jButtonTransfer.setForeground(Color.WHITE); 

        buttonPanel.add(jButtonUpdateInfo);
        buttonPanel.add(jButtonDeposit);
        buttonPanel.add(jButtonWithdraw);
        buttonPanel.add(jButtonTransfer);

        panelInfo.add(formPanel, BorderLayout.CENTER);
        panelInfo.add(buttonPanel, BorderLayout.SOUTH);

        // --- TAB 2: LỊCH SỬ GIAO DỊCH ---
        JPanel panelHistory = new JPanel(new BorderLayout());
        jTableTransactions = new JTable();
        JScrollPane scrollPane = new JScrollPane(jTableTransactions);
        panelHistory.add(scrollPane, BorderLayout.CENTER);
        
        JButton btnRefreshHistory = new JButton("Làm mới lịch sử");
        btnRefreshHistory.addActionListener(e -> loadTransactionHistory());
        panelHistory.add(btnRefreshHistory, BorderLayout.NORTH);

        jTabbedPane1.addTab("Thông tin & Tài khoản", new ImageIcon(), panelInfo, "Quản lý thông tin và số dư");
        jTabbedPane1.addTab("Lịch sử giao dịch", new ImageIcon(), panelHistory, "Xem sao kê giao dịch");

        this.setLayout(new BorderLayout());
        this.add(jTabbedPane1, BorderLayout.CENTER);
        this.setSize(750, 600);

        // Gán sự kiện
        jButtonUpdateInfo.addActionListener(this::jButtonUpdateInfoActionPerformed);
        jButtonDeposit.addActionListener(this::jButtonDepositActionPerformed);
        jButtonWithdraw.addActionListener(this::jButtonWithdrawActionPerformed);
        jButtonTransfer.addActionListener(this::jButtonTransferActionPerformed);
    }

    // [INIT] Cấu hình bảng lịch sử giao dịch
    private void initTransactionTable() {
        transactionTableModel = new DefaultTableModel();
        transactionTableModel.addColumn("Mã GD");
        transactionTableModel.addColumn("Loại GD");
        transactionTableModel.addColumn("Số tiền");
        transactionTableModel.addColumn("Thời gian");
        transactionTableModel.addColumn("Mô tả");
        transactionTableModel.addColumn("Trạng thái");
        jTableTransactions.setModel(transactionTableModel);
        jTableTransactions.getColumnModel().getColumn(0).setPreferredWidth(50);
        jTableTransactions.getColumnModel().getColumn(2).setPreferredWidth(100);
        jTableTransactions.getColumnModel().getColumn(3).setPreferredWidth(130);
        jTableTransactions.getColumnModel().getColumn(4).setPreferredWidth(200);
    }

    // [LOAD] Tải thông tin cá nhân của khách hàng
    private void loadCustomerData() {
        try {
            currentCustomer = adminService.getCustomerById(customerId);
            if (currentCustomer != null) {
                jTextFieldUsername.setText(currentCustomer.getUsername());
                jTextFieldFullName.setText(currentCustomer.getFullName());
                jTextFieldCitizenID.setText(currentCustomer.getCitizenID());
                jTextFieldEmail.setText(currentCustomer.getEmail());
                jTextFieldPhone.setText(currentCustomer.getPhone());
                jTextFieldAddress.setText(currentCustomer.getPlaceOfResidence());
                loadAccountInfo();
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi tải thông tin: " + e.getMessage());
        }
    }

    // [LOAD] Tải thông tin tài khoản ngân hàng (Số dư, Trạng thái)
    private void loadAccountInfo() {
        try {
            List<Account> accounts = adminService.getCustomerAccounts(customerId);
            if (!accounts.isEmpty()) {
                Account mainAccount = accounts.get(0); 
                jTextFieldAccountNumber.setText(mainAccount.getAccountNumber());
                jTextFieldBalance.setText(String.format("%,d VND", mainAccount.getBalance().longValue()));
                
                String statusDisplay = formatAccountStatus(mainAccount.getAccountStatus());
                jTextFieldAccountStatus.setText(statusDisplay);
                
                if ("Đã khóa".equals(statusDisplay)) {
                    jTextFieldAccountStatus.setForeground(Color.RED);
                } else {
                    jTextFieldAccountStatus.setForeground(new Color(0, 153, 51));
                }
            } else {
                jTextFieldAccountStatus.setText("Chưa có TK");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // [LOAD] Tải lịch sử giao dịch
    private void loadTransactionHistory() {
        try {
            List<Transaction> transactions = adminService.getTransactionHistory(customerId);
            transactionTableModel.setRowCount(0);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            for (Transaction t : transactions) {
                String dateStr = (t.getCreatedAt() != null) ? t.getCreatedAt().format(formatter) : "";
                Object[] row = {
                    t.getTransactionID(),
                    formatTransactionType(t.getTransactionType()),
                    formatAmount(t.getAmount().longValue()),
                    dateStr,
                    t.getTransactionContent(),
                    formatTransactionStatus(t.getTransactionStatus())
                };
                transactionTableModel.addRow(row);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // [HELPER] Định dạng loại giao dịch sang tiếng Việt
    private String formatTransactionType(String type) {
        if (type == null) return "";
        switch (type) {
            case "DEPOSIT": return "Nạp tiền";
            case "WITHDRAW": return "Rút tiền";
            case "TRANSFER": return "Chuyển khoản";
            case "TRANSFER_IN": return "Nhận tiền";
            case "TRANSFER_OUT": return "Chuyển đi";
            default: return type;
        }
    }

    // [HELPER] Định dạng số tiền
    private String formatAmount(long amount) {
        return String.format("%,d", amount);
    }

    // [HELPER] Định dạng trạng thái giao dịch
    private String formatTransactionStatus(String status) {
        return "SUCCESS".equals(status) ? "Thành công" : status;
    }

    // [HELPER] Định dạng trạng thái tài khoản
    private String formatAccountStatus(String status) {
        if ("ACTIVE".equals(status)) return "Hoạt động";
        if ("LOCKED".equals(status)) return "Đã khóa";
        return status;
    }

    // --- CÁC SỰ KIỆN (ACTIONS) ---

    // [ACTION] Cập nhật thông tin khách hàng (Admin sửa)
    private void jButtonUpdateInfoActionPerformed(java.awt.event.ActionEvent evt) {
        try {
            currentCustomer.setFullName(jTextFieldFullName.getText());
            currentCustomer.setEmail(jTextFieldEmail.getText());
            currentCustomer.setPhone(jTextFieldPhone.getText());
            currentCustomer.setPlaceOfResidence(jTextFieldAddress.getText());
            
            adminService.updateCustomerInfo(currentCustomer);
            JOptionPane.showMessageDialog(this, "Cập nhật thông tin thành công!");
            
            if (onStatusChange != null) onStatusChange.run(); // Báo cho Menu_Admin cập nhật
            
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi cập nhật: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    // [ACTION] Admin nạp tiền cho khách
    private void jButtonDepositActionPerformed(java.awt.event.ActionEvent evt) {
        String amountStr = JOptionPane.showInputDialog(this, "Nhập số tiền nạp:");
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                long amount = Long.parseLong(amountStr);
                adminService.depositForCustomer(customerId, amount, "Admin nạp tiền");
                JOptionPane.showMessageDialog(this, "Nạp tiền thành công!");
                loadAccountInfo();
                loadTransactionHistory();
                if (onStatusChange != null) onStatusChange.run();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    // [ACTION] Admin rút tiền cho khách
    private void jButtonWithdrawActionPerformed(java.awt.event.ActionEvent evt) {
        String amountStr = JOptionPane.showInputDialog(this, "Nhập số tiền rút:");
        if (amountStr != null && !amountStr.isEmpty()) {
            try {
                long amount = Long.parseLong(amountStr);
                adminService.withdrawForCustomer(customerId, amount, "Admin rút tiền");
                JOptionPane.showMessageDialog(this, "Rút tiền thành công!");
                loadAccountInfo();
                loadTransactionHistory();
                if (onStatusChange != null) onStatusChange.run();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }
    
    // [ACTION] Admin chuyển khoản hộ khách
    private void jButtonTransferActionPerformed(java.awt.event.ActionEvent evt) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JTextField txtToAccount = new JTextField();
        JTextField txtAmount = new JTextField();
        JTextField txtContent = new JTextField();
        panel.add(new JLabel("Số tài khoản nhận:")); panel.add(txtToAccount);
        panel.add(new JLabel("Số tiền:")); panel.add(txtAmount);
        panel.add(new JLabel("Nội dung:")); panel.add(txtContent);
        
        int result = JOptionPane.showConfirmDialog(null, panel, "Chuyển tiền", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                long amount = Long.parseLong(txtAmount.getText());
                adminService.transferForCustomer(customerId, txtToAccount.getText(), amount, txtContent.getText());
                JOptionPane.showMessageDialog(this, "Chuyển tiền thành công!");
                loadAccountInfo();
                loadTransactionHistory();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi: " + e.getMessage());
            }
        }
    }

    // Khai báo biến
    private JTabbedPane jTabbedPane1;
    private JTable jTableTransactions;
    private JTextField jTextFieldUsername, jTextFieldFullName, jTextFieldCitizenID;
    private JTextField jTextFieldEmail, jTextFieldPhone, jTextFieldAddress;
    private JTextField jTextFieldAccountNumber, jTextFieldBalance, jTextFieldAccountStatus;
    private JButton jButtonUpdateInfo, jButtonDeposit, jButtonWithdraw, jButtonTransfer;
}