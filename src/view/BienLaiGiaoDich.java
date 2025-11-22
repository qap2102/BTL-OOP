package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.text.NumberFormat;
import java.util.Locale;

import model.Transaction;
import model.Account;
import Service.CustomerService;
import Service.AccountService; 
import Session.CustomerSession;
import model.Customer;

public class BienLaiGiaoDich extends JFrame {

    // [CONSTRUCTOR] Khởi tạo giao diện Lịch sử giao dịch
    public BienLaiGiaoDich() {
        setTitle("Lịch sử giao dịch");
        setSize(900, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // [HEADER] Tiêu đề trang
        JLabel lblTitle = new JLabel("LỊCH SỬ GIAO DỊCH", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 20));
        lblTitle.setForeground(new Color(0, 102, 102));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(lblTitle, BorderLayout.NORTH);

        // [TABLE] Cấu hình bảng hiển thị
        String[] col = {"Mã GD", "Thời gian", "Loại giao dịch", "Số tiền", "Nội dung", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép sửa dữ liệu trên bảng
            }
        };
        
        JTable table = new JTable(model);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        table.setRowHeight(25);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        table.getTableHeader().setBackground(new Color(0, 102, 102));
        table.getTableHeader().setForeground(Color.WHITE);
        
        // Thiết lập độ rộng cột
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // Mã GD
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Thời gian
        table.getColumnModel().getColumn(2).setPreferredWidth(120); // Loại GD
        table.getColumnModel().getColumn(3).setPreferredWidth(120); // Số tiền
        table.getColumnModel().getColumn(4).setPreferredWidth(250); // Nội dung
        table.getColumnModel().getColumn(5).setPreferredWidth(100); // Trạng thái
        
        JScrollPane sp = new JScrollPane(table);
        sp.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        add(sp, BorderLayout.CENTER);

        // [FOOTER] Các nút chức năng
        JPanel pnlButton = new JPanel();
        JButton btnLamMoi = new JButton("Làm mới dữ liệu");
        btnLamMoi.setBackground(new Color(0, 102, 102));
        btnLamMoi.setForeground(Color.WHITE);
        btnLamMoi.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnLamMoi.addActionListener(e -> loadTransactionData(model));
        pnlButton.add(btnLamMoi);
        
        JButton btnDong = new JButton("Đóng");
        btnDong.addActionListener(e -> dispose());
        pnlButton.add(btnDong);
        
        add(pnlButton, BorderLayout.SOUTH);

        // Tải dữ liệu ngay khi mở form
        loadTransactionData(model);
    }

    // [LOGIC] Tải dữ liệu giao dịch từ Server/DB và hiển thị lên bảng
    private void loadTransactionData(DefaultTableModel model) {
        // Xóa dữ liệu cũ trên bảng
        model.setRowCount(0);

        // Kiểm tra phiên đăng nhập
        Customer current = CustomerSession.getLoggedInCustomer();
        if (current == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng đăng nhập lại.", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // [BƯỚC 1] Lấy danh sách ID tài khoản của khách hàng
        // Mục đích: Để so sánh xem trong giao dịch chuyển khoản, khách hàng là người Gửi hay người Nhận
        AccountService accountService = new AccountService();
        List<Integer> myAccountIds = new ArrayList<>();
        try {
            List<Account> accounts = accountService.getAccountByCustomer(current.getCustomerID());
            for (Account a : accounts) {
                myAccountIds.add(a.getAccountID());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // [BƯỚC 2] Lấy lịch sử giao dịch từ Database
        CustomerService cs = new CustomerService();
        List<Transaction> list;
        try {
            list = cs.getTransactionHistoryByCustomerId(current.getCustomerID());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        // Format định dạng hiển thị
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));

        // [BƯỚC 3] Duyệt danh sách và xử lý logic hiển thị (Tiền âm/dương, Loại giao dịch)
        for (Transaction t : list) {
            String thoiGian = t.getCreatedAt() == null ? "N/A" : fmt.format(t.getCreatedAt());
            String soTienStr = currencyFormat.format(t.getAmount());
            
            String typeDB = t.getTransactionType(); // Loại gốc trong DB (DEPOSIT, TRANSFER, WITHDRAW)
            String hienThiLoaiGD = typeDB;
            boolean isIncome = false; // Cờ đánh dấu là tiền vào (+) hay ra (-)

            // Phân loại giao dịch để hiển thị tiếng Việt và dấu +/-
            if ("DEPOSIT".equalsIgnoreCase(typeDB)) {
                hienThiLoaiGD = "Nạp tiền";
                isIncome = true;
            } else if ("WITHDRAW".equalsIgnoreCase(typeDB)) {
                hienThiLoaiGD = "Rút tiền";
                isIncome = false;
            } else if ("TRANSFER".equalsIgnoreCase(typeDB)) {
                // Logic quan trọng: Kiểm tra ID người nhận có thuộc về khách hàng không
                if (t.getToAccountID() != null && myAccountIds.contains(t.getToAccountID())) {
                    hienThiLoaiGD = "Nhận tiền"; // Người khác chuyển đến mình
                    isIncome = true;
                } else {
                    hienThiLoaiGD = "Chuyển tiền"; // Mình chuyển đi
                    isIncome = false;
                }
            }

            // Gắn dấu +/- vào số tiền
            if (isIncome) {
                soTienStr = "+" + soTienStr;
            } else {
                soTienStr = "-" + soTienStr;
            }

            String trangThai = "Thành công"; // Giả định trạng thái hiển thị

            // Tạo hàng dữ liệu và thêm vào bảng
            Object[] row = {
                t.getTransactionID(),
                thoiGian,
                hienThiLoaiGD,
                soTienStr,
                t.getTransactionContent(),
                trangThai
            };
            model.addRow(row);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            BienLaiGiaoDich frame = new BienLaiGiaoDich();
            frame.setVisible(true);
        });
    }
}