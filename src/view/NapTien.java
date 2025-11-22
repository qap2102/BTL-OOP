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

// [CLASS] Màn hình Nạp tiền dành cho Khách hàng
public class NapTien extends JFrame {

    private JTextField txtSoTien;
    private JPasswordField txtMaPIN;
    private JLabel lblSoDu;

    // [CONSTRUCTOR] Khởi tạo giao diện
    public NapTien() {
        setTitle("Nạp tiền");
        setSize(460, 350); // Tăng nhẹ chiều rộng để chứa nút "Hiện"
        setLocationRelativeTo(null);
        setLayout(null);

        // Tiêu đề
        JLabel lbl = new JLabel("NẠP TIỀN", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setBounds(100, 20, 250, 40);
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

        // Tải số dư ngay khi mở form
        loadSoDu();

        // Nhập số tiền cần nạp
        JLabel lblTien = new JLabel("Số tiền nạp:");
        lblTien.setBounds(50, 120, 100, 30);
        add(lblTien);

        txtSoTien = new JTextField();
        txtSoTien.setBounds(150, 120, 200, 30);
        add(txtSoTien);

        // Nhập mã PIN để xác thực
        JLabel lblMaPIN = new JLabel("Mã PIN:");
        lblMaPIN.setBounds(50, 160, 100, 30);
        add(lblMaPIN);

        txtMaPIN = new JPasswordField();
        txtMaPIN.setBounds(150, 160, 200, 30);
        add(txtMaPIN);

        // [MỚI] Nút Checkbox hiện mã PIN
        JCheckBox chkShowPin = new JCheckBox("Hiện");
        chkShowPin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkShowPin.setBounds(360, 160, 60, 30); // Đặt bên phải ô nhập PIN
        chkShowPin.setFocusPainted(false);
        chkShowPin.addActionListener(e -> {
            if (chkShowPin.isSelected()) {
                txtMaPIN.setEchoChar((char) 0); // Hiện text thường
            } else {
                txtMaPIN.setEchoChar('•'); // Ẩn thành ký tự chấm tròn
            }
        });
        add(chkShowPin);

        // Nút xác nhận
        JButton btnNap = new JButton("XÁC NHẬN NẠP TIỀN");
        btnNap.setBackground(new Color(0, 102, 102));
        btnNap.setForeground(Color.WHITE);
        btnNap.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnNap.setBounds(120, 210, 200, 35);
        btnNap.addActionListener(e -> xuLyNapTien());
        add(btnNap);

        // Nút làm mới số dư
        JButton btnLamMoi = new JButton("Làm mới số dư");
        btnLamMoi.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        btnLamMoi.setBounds(270, 70, 120, 25);
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

    // [ACTION] Xử lý logic nạp tiền
    private void xuLyNapTien() {
        try {
            BigDecimal amount = new BigDecimal(txtSoTien.getText().trim());
            String maPIN = new String(txtMaPIN.getPassword());
            
            // Validate dữ liệu đầu vào
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
            Account acc = accounts.get(0);

            // Hiển thị hộp thoại xác nhận
            int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận nạp tiền:\n" +
                "Số tiền: " + String.format("%,d VND", amount.intValue()),
                "Xác nhận giao dịch",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Gọi TransactionService để thực hiện nạp tiền (Cộng tiền, ghi log)
            TransactionService ts = new TransactionService();
            ts.deposit(acc.getAccountNumber(), amount, maPIN, "Nạp tiền vào tài khoản");

            JOptionPane.showMessageDialog(this, 
                "Nạp tiền thành công!\n" +
                "Số tiền: " + String.format("%,d VND", amount.intValue()),
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Cập nhật lại số dư hiển thị
            loadSoDu();
            
            // Reset form
            txtSoTien.setText("");
            txtMaPIN.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Số tiền không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Giao dịch thất bại: " + e.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
}