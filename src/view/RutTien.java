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

// [CLASS] Màn hình Rút tiền dành cho Khách hàng
// Chức năng: Cho phép khách hàng rút tiền từ tài khoản, chọn mệnh giá nhanh
public class RutTien extends JFrame {

    private JTextField txtSoTien;
    private JPasswordField txtMaPIN;
    private JLabel lblSoDu;

    // [CONSTRUCTOR] Khởi tạo giao diện
    public RutTien() {
        setTitle("Rút tiền");
        setSize(500, 450); // Kích thước cửa sổ đủ rộng để chứa các nút
        setLocationRelativeTo(null);
        setLayout(null);

        // Tiêu đề
        JLabel lbl = new JLabel("RÚT TIỀN", SwingConstants.CENTER);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 22));
        lbl.setBounds(150, 20, 200, 40);
        add(lbl);

        // Hiển thị số dư hiện tại
        JLabel lblSoDuTitle = new JLabel("Số dư hiện tại:");
        lblSoDuTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSoDuTitle.setBounds(50, 70, 100, 30);
        add(lblSoDuTitle);

        lblSoDu = new JLabel("0 VND");
        lblSoDu.setFont(new Font("Segoe UI", Font.BOLD, 14));
        lblSoDu.setForeground(new Color(0, 102, 102)); // Màu xanh đậm
        lblSoDu.setBounds(160, 70, 200, 30);
        add(lblSoDu);

        // Tải số dư ngay khi mở form
        loadSoDu();

        // Nhập số tiền
        JLabel lblTien = new JLabel("Số tiền rút:");
        lblTien.setBounds(50, 120, 100, 30);
        add(lblTien);

        txtSoTien = new JTextField();
        txtSoTien.setBounds(160, 120, 200, 30);
        add(txtSoTien);

        // Nhập mã PIN
        JLabel lblMaPIN = new JLabel("Mã PIN:");
        lblMaPIN.setBounds(50, 160, 100, 30);
        add(lblMaPIN);

        txtMaPIN = new JPasswordField();
        txtMaPIN.setBounds(160, 160, 200, 30);
        add(txtMaPIN);

        // [MỚI] Nút Checkbox hiện mã PIN
        JCheckBox chkShowPin = new JCheckBox("Hiện");
        chkShowPin.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chkShowPin.setBounds(370, 160, 60, 30); // Đặt bên phải ô nhập PIN
        chkShowPin.setFocusPainted(false);
        chkShowPin.addActionListener(e -> {
            if (chkShowPin.isSelected()) {
                txtMaPIN.setEchoChar((char) 0); // Hiện text thường
            } else {
                txtMaPIN.setEchoChar('•'); // Ẩn thành ký tự chấm tròn
            }
        });
        add(chkShowPin);

        // [PANEL] Các nút chọn mệnh giá nhanh (100k, 200k, 500k...)
        JPanel pnlMenhGia = new JPanel();
        pnlMenhGia.setLayout(new GridLayout(2, 3, 5, 5)); // Grid 2 hàng 3 cột
        pnlMenhGia.setBounds(50, 200, 400, 80);
        pnlMenhGia.setBorder(BorderFactory.createTitledBorder("Mệnh giá nhanh"));
        
        String[] menhGia = {"100,000", "200,000", "500,000", "1,000,000", "2,000,000", "5,000,000"};
        for (String mg : menhGia) {
            JButton btn = new JButton(mg);
            btn.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            btn.setBackground(new Color(240, 240, 240));
            // Sự kiện: Click vào nút -> Điền tiền vào ô nhập
            btn.addActionListener(e -> txtSoTien.setText(mg.replace(",", "")));
            pnlMenhGia.add(btn);
        }
        add(pnlMenhGia);

        // Nút Xác nhận Rút tiền
        JButton btnRut = new JButton("XÁC NHẬN RÚT TIỀN");
        btnRut.setBackground(new Color(0, 102, 102));
        btnRut.setForeground(Color.WHITE);
        btnRut.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRut.setBounds(150, 300, 200, 35);
        btnRut.addActionListener(e -> xuLyRutTien());
        add(btnRut);

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

    // [ACTION] Xử lý logic rút tiền
    private void xuLyRutTien() {
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

            // Kiểm tra số dư
            if (amount.compareTo(acc.getBalance()) > 0) {
                JOptionPane.showMessageDialog(this, 
                    "Số dư không đủ!\nSố dư hiện tại: " + String.format("%,d VND", acc.getBalance().intValue()), 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Hiển thị xác nhận trước khi thực hiện
            int confirm = JOptionPane.showConfirmDialog(this,
                "Xác nhận rút tiền:\n" +
                "Số tiền: " + String.format("%,d VND", amount.intValue()) + "\n" +
                "Phí giao dịch: 0 VND\n" +
                "Số tiền thực nhận: " + String.format("%,d VND", amount.intValue()),
                "Xác nhận giao dịch",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm != JOptionPane.YES_OPTION) {
                return;
            }

            // Gọi TransactionService để thực hiện giao dịch (Trừ tiền, ghi log)
            TransactionService ts = new TransactionService();
            ts.withdraw(acc.getAccountNumber(), amount, maPIN, "Rút tiền mặt tại quầy");

            JOptionPane.showMessageDialog(this, 
                "Rút tiền thành công!\n" +
                "Số tiền: " + String.format("%,d VND", amount.intValue()) + "\n" +
                "Vui lòng nhận tiền tại quầy giao dịch.",
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            
            // Làm mới số dư sau khi rút
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