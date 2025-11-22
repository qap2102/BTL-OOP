package view;

import java.awt.*;
import javax.swing.*;
import Service.AccountService;
import Service.TransactionService;
import Session.CustomerSession;
import model.Customer;
import model.Account;
import java.math.BigDecimal;
import java.util.List;

// [CLASS] Màn hình Chuyển tiền dành cho Khách hàng
// Chức năng: Thực hiện chuyển khoản từ tài khoản cá nhân sang tài khoản khác
public class ChuyenTien extends JFrame {

    private JTextField txtSTKNhan, txtSoTien, txtNoiDung, txtMaPIN;
    private JLabel lblSoDu;

    // [CONSTRUCTOR] Khởi tạo giao diện
    public ChuyenTien() {
        setTitle("Chuyển tiền");
        setSize(500, 450);
        setLocationRelativeTo(null);
        setLayout(null);

        // Tiêu đề
        JLabel lbl = new JLabel("CHUYỂN TIỀN", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setBounds(100, 20, 300, 40);
        add(lbl);

        // Hiển thị số dư hiện tại
        JLabel lblSoDuTitle = new JLabel("Số dư hiện tại:");
        lblSoDuTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSoDuTitle.setBounds(50, 70, 100, 30);
        add(lblSoDuTitle);

        lblSoDu = new JLabel("0 VND");
        lblSoDu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSoDu.setForeground(new Color(0, 102, 102));
        lblSoDu.setBounds(160, 70, 200, 30);
        add(lblSoDu);

        // Tải số dư khi mở form
        loadSoDu();

        // Form nhập liệu
        JLabel lblSTK = new JLabel("STK nhận:");
        lblSTK.setBounds(50, 110, 100, 30);
        add(lblSTK);

        txtSTKNhan = new JTextField();
        txtSTKNhan.setBounds(150, 110, 250, 30);
        add(txtSTKNhan);

        JLabel lblTien = new JLabel("Số tiền:");
        lblTien.setBounds(50, 150, 100, 30);
        add(lblTien);

        txtSoTien = new JTextField();
        txtSoTien.setBounds(150, 150, 250, 30);
        add(txtSoTien);

        JLabel lblNoiDung = new JLabel("Nội dung:");
        lblNoiDung.setBounds(50, 190, 100, 30);
        add(lblNoiDung);

        txtNoiDung = new JTextField();
        txtNoiDung.setBounds(150, 190, 250, 30);
        add(txtNoiDung);

        JLabel lblMaPIN = new JLabel("Mã PIN:");
        lblMaPIN.setBounds(50, 230, 100, 30);
        add(lblMaPIN);

        txtMaPIN = new JPasswordField();
        txtMaPIN.setBounds(150, 230, 250, 30);
        add(txtMaPIN);

        // Nút Xác nhận
        JButton btnOk = new JButton("XÁC NHẬN CHUYỂN");
        btnOk.setBackground(new Color(0, 102, 102));
        btnOk.setForeground(Color.WHITE);
        btnOk.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnOk.setBounds(150, 280, 200, 35);
        btnOk.addActionListener(e -> xuLyChuyenTien());
        add(btnOk);

        // Nút làm mới số dư (Refresh)
        JButton btnLamMoi = new JButton("Làm mới số dư");
        btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLamMoi.setBounds(300, 70, 120, 25);
        btnLamMoi.addActionListener(e -> loadSoDu());
        add(btnLamMoi);
    }

    // [LOAD] Tải và hiển thị số dư tài khoản của khách hàng
    private void loadSoDu() {
        try {
            Customer current = CustomerSession.getLoggedInCustomer();
            if (current == null) {
                JOptionPane.showMessageDialog(this, "Hết phiên đăng nhập!");
                return;
            }

            AccountService as = new AccountService();
            List<Account> accounts = as.getAccountByCustomer(current.getCustomerID());
            if (!accounts.isEmpty()) {
                Account acc = accounts.get(0);
                lblSoDu.setText(String.format("%,d VND", acc.getBalance().intValue()));
            } else {
                lblSoDu.setText("Chưa có tài khoản");
            }
        } catch (Exception e) {
            e.printStackTrace();
            lblSoDu.setText("Lỗi tải số dư");
        }
    }

    // [ACTION] Xử lý logic chuyển tiền
    private void xuLyChuyenTien() {
        String stkNhan = txtSTKNhan.getText().trim();
        String noiDung = txtNoiDung.getText().trim();
        String maPIN = new String(((JPasswordField) txtMaPIN).getPassword());
        
        try {
            BigDecimal amount = new BigDecimal(txtSoTien.getText().trim());

            // Validate dữ liệu
            if (stkNhan.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập STK người nhận!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, "Số tiền phải lớn hơn 0!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            if (maPIN.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập mã PIN!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Customer current = CustomerSession.getLoggedInCustomer();
            if (current == null) {
                JOptionPane.showMessageDialog(this, "Hết phiên đăng nhập!");
                return;
            }

            AccountService as = new AccountService();
            List<Account> accounts = as.getAccountByCustomer(current.getCustomerID());
            if (accounts.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Bạn chưa có tài khoản ngân hàng!");
                return;
            }
            Account sourceAcc = accounts.get(0);

            // Kiểm tra số dư
            if (amount.compareTo(sourceAcc.getBalance()) > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Số dư không đủ!\nSố dư hiện tại: " + String.format("%,d VND", sourceAcc.getBalance().intValue()), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Hiển thị xác nhận trước khi thực hiện
            int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận chuyển tiền:\n" +
                "Đến STK: " + stkNhan + "\n" +
                "Số tiền: " + String.format("%,d VND", amount.intValue()) + "\n" +
                "Nội dung: " + noiDung,
                "Xác nhận giao dịch",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Gọi TransactionService để thực hiện chuyển khoản
            TransactionService ts = new TransactionService();
            ts.transfer(sourceAcc.getAccountNumber(), stkNhan, amount, maPIN, noiDung);

            JOptionPane.showMessageDialog(this, 
                "Chuyển tiền thành công!\n" +
                "Số tiền: " + String.format("%,d VND", amount.intValue()) + "\n" +
                "Đến STK: " + stkNhan,
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Làm mới số dư sau khi chuyển
            loadSoDu();
            
            // Reset form
            txtSoTien.setText("");
            txtNoiDung.setText("");
            txtMaPIN.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Giao dịch thất bại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}