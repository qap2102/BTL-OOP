package view;

import Service.CustomerService;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class Register extends JFrame {

    // Components giao diện
    private JTextField txtUsername, txtFullName, txtDob, txtPhone, txtCccd, txtEmail, txtAddress;
    private JPasswordField txtPass, txtConfirmPass, txtPin, txtConfirmPin;
    private JCheckBox chkShowPass, chkShowPassConfirm, chkShowPin, chkShowPinConfirm;
    
    // Labels thông báo (Validation Real-time)
    private JLabel lblMsgPhone, lblMsgCccd, lblMsgPin;
    
    // Buttons điều hướng
    private JButton btnRegister, btnBack;
    
    private final CustomerService customerService = new CustomerService();
    
    // Màu sắc chủ đạo (Theme)
    private final Color COL_PRIMARY = new Color(0, 102, 102);   // Xanh đậm header
    private final Color COL_ACCENT = new Color(0, 153, 153);    // Xanh sáng hơn cho button
    private final Color COL_BG = new Color(240, 245, 249);      // Nền xám nhạt toàn form
    private final Color COL_SUCCESS = new Color(40, 167, 69);   // Xanh lá (Thành công)
    private final Color COL_ERROR = new Color(220, 53, 69);     // Đỏ (Lỗi)

    // [CONSTRUCTOR] Khởi tạo giao diện Đăng ký
    public Register() {
        initComponents();
        setLocationRelativeTo(null);
    }

    // [INIT] Thiết lập các thành phần giao diện
    private void initComponents() {
        setTitle("Đăng ký tài khoản E-Banking");
        setSize(750, 900); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Panel chính dùng BorderLayout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(COL_BG);
        setContentPane(mainPanel);

        // --- 1. HEADER (Tiêu đề) ---
        JPanel pnlHeader = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 20));
        pnlHeader.setBackground(COL_PRIMARY);
        pnlHeader.setPreferredSize(new Dimension(750, 80));
        
        JLabel lblTitle = new JLabel("ĐĂNG KÝ TÀI KHOẢN", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitle.setForeground(Color.WHITE);
        pnlHeader.add(lblTitle);
        mainPanel.add(pnlHeader, BorderLayout.NORTH);

        // --- 2. BODY (Form nhập liệu - Dạng thẻ trắng) ---
        JPanel pnlCard = new JPanel(new GridBagLayout());
        pnlCard.setBackground(Color.WHITE);
        // Tạo viền đổ bóng nhẹ cho đẹp
        pnlCard.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(220, 220, 220), 1),
            new EmptyBorder(20, 40, 20, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(0, 0, 15, 0); // Khoảng cách mặc định giữa các dòng input
        
        int row = 0;

        // === CÁC DÒNG NHẬP LIỆU ===
        
        // 1. Tên đăng nhập
        addFormRow(pnlCard, gbc, row++, "Tên đăng nhập (*):", txtUsername = createTextField());
        
        // 2. Họ và tên
        addFormRow(pnlCard, gbc, row++, "Họ và tên (*):", txtFullName = createTextField());

        // 3. Ngày sinh (Có Placeholder)
        txtDob = createTextField();
        txtDob.setText("dd/MM/yyyy");
        txtDob.setForeground(Color.GRAY);
        txtDob.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent evt) {
                if (txtDob.getText().equals("dd/MM/yyyy")) {
                    txtDob.setText("");
                    txtDob.setForeground(Color.BLACK);
                }
            }
            public void focusLost(FocusEvent evt) {
                if (txtDob.getText().isEmpty()) {
                    txtDob.setText("dd/MM/yyyy");
                    txtDob.setForeground(Color.GRAY);
                }
            }
        });
        addFormRow(pnlCard, gbc, row++, "Ngày sinh (*):", txtDob);

        // 4. Số điện thoại (Có Validation Real-time)
        txtPhone = createTextField();
        lblMsgPhone = createValidationLabel();
        addFormRowWithValidation(pnlCard, gbc, row++, "Số điện thoại (*):", txtPhone, lblMsgPhone);
        // Setup logic kiểm tra
        setupValidation(txtPhone, lblMsgPhone, 10, "SĐT");

        // 5. CCCD (Có Validation Real-time)
        txtCccd = createTextField();
        lblMsgCccd = createValidationLabel();
        addFormRowWithValidation(pnlCard, gbc, row++, "CCCD/CMND (*):", txtCccd, lblMsgCccd);
        setupValidation(txtCccd, lblMsgCccd, 12, "CCCD");

        // 6. Email
        addFormRow(pnlCard, gbc, row++, "Email:", txtEmail = createTextField());

        // 7. Địa chỉ
        addFormRow(pnlCard, gbc, row++, "Địa chỉ:", txtAddress = createTextField());

        // 8. Mật khẩu
        txtPass = createPasswordField();
        chkShowPass = createShowPassCheckbox(txtPass);
        addFormRowPassword(pnlCard, gbc, row++, "Mật khẩu (*):", txtPass, chkShowPass);

        // 9. Xác nhận mật khẩu
        txtConfirmPass = createPasswordField();
        chkShowPassConfirm = createShowPassCheckbox(txtConfirmPass);
        addFormRowPassword(pnlCard, gbc, row++, "Xác nhận mật khẩu (*):", txtConfirmPass, chkShowPassConfirm);

        // 10. PIN (Có Validation Real-time)
        txtPin = createPasswordField();
        chkShowPin = createShowPassCheckbox(txtPin);
        lblMsgPin = createValidationLabel();
        addFormRowPasswordWithValidation(pnlCard, gbc, row++, "Mã PIN (6 số) (*):", txtPin, chkShowPin, lblMsgPin);
        setupValidation(txtPin, lblMsgPin, 6, "PIN");

        // 11. Xác nhận PIN
        txtConfirmPin = createPasswordField();
        chkShowPinConfirm = createShowPassCheckbox(txtConfirmPin);
        addFormRowPassword(pnlCard, gbc, row++, "Xác nhận mã PIN (*):", txtConfirmPin, chkShowPinConfirm);


        // Bọc panel trắng vào ScrollPane để cuộn nếu màn hình nhỏ
        JScrollPane scrollPane = new JScrollPane(pnlCard);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(20);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        // --- 3. FOOTER (BUTTONS) ---
        JPanel pnlFooter = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 20));
        pnlFooter.setBackground(COL_BG);

        btnRegister = createButton("ĐĂNG KÝ NGAY", COL_ACCENT);
        btnBack = createButton("QUAY LẠI", new Color(108, 117, 125)); // Màu xám

        btnRegister.addActionListener(e -> handleRegister());
        btnBack.addActionListener(e -> {
            new Login().setVisible(true);
            this.dispose();
        });

        pnlFooter.add(btnRegister);
        pnlFooter.add(btnBack);
        mainPanel.add(pnlFooter, BorderLayout.SOUTH);
    }

    // =================================================================
    // CÁC HÀM HỖ TRỢ TẠO GIAO DIỆN (HELPER METHODS)
    // =================================================================

    // [HELPER] Tạo TextField đẹp có padding
    private JTextField createTextField() {
        JTextField txt = new JTextField();
        txt.setPreferredSize(new Dimension(300, 35));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        // Padding bên trong để chữ không dính viền
        txt.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    // [HELPER] Tạo PasswordField đẹp
    private JPasswordField createPasswordField() {
        JPasswordField txt = new JPasswordField();
        txt.setPreferredSize(new Dimension(300, 35));
        txt.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        txt.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(new Color(200, 200, 200)), 
            new EmptyBorder(5, 10, 5, 10)
        ));
        return txt;
    }

    // [HELPER] Tạo Button đẹp có hiệu ứng hover
    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(180, 45));
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hiệu ứng hover
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.darker()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });
        return btn;
    }

    // [HELPER] Tạo Checkbox ẩn/hiện mật khẩu
    private JCheckBox createShowPassCheckbox(JTextField txt) {
        JCheckBox chk = new JCheckBox("Hiện");
        chk.setBackground(Color.WHITE);
        chk.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        chk.setCursor(new Cursor(Cursor.HAND_CURSOR));
        chk.addActionListener(e -> {
            if (txt instanceof JPasswordField) {
                JPasswordField pass = (JPasswordField) txt;
                if (chk.isSelected()) pass.setEchoChar((char) 0);
                else pass.setEchoChar('•');
            }
        });
        return chk;
    }
    
    // [HELPER] Tạo Label Validation (Mặc định ẩn nội dung)
    private JLabel createValidationLabel() {
        JLabel lbl = new JLabel(" "); // Ban đầu rỗng để giữ chỗ
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 11));
        return lbl;
    }

    // =================================================================
    // CÁC HÀM CĂN CHỈNH LAYOUT (QUAN TRỌNG ĐỂ ĐỀU DÒNG)
    // =================================================================

    // [LAYOUT] Thêm dòng: Label + Input (Cơ bản)
    private void addFormRow(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent input) {
        // Label
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3; 
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        lbl.setForeground(new Color(80, 80, 80));
        panel.add(lbl, gbc);

        // Input
        gbc.gridx = 1; 
        gbc.weightx = 0.7;
        panel.add(input, gbc);
    }

    // [LAYOUT] Thêm dòng: Label + Input + Validation Label (Bên dưới input)
    private void addFormRowWithValidation(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent input, JLabel valLabel) {
        addFormRow(panel, gbc, row, labelText, input);
        
        // Chèn validation label xuống dưới input bằng cách chỉnh Insets
        GridBagConstraints gbcVal = (GridBagConstraints) gbc.clone();
        gbcVal.gridx = 1;
        gbcVal.gridy = row; 
        gbcVal.insets = new Insets(40, 0, 0, 0); // Dịch xuống dưới input (Input cao 35 + padding 5 = 40)
        gbcVal.anchor = GridBagConstraints.WEST;
        panel.add(valLabel, gbcVal);
    }

    // [LAYOUT] Thêm dòng: Label + Password + Checkbox
    private void addFormRowPassword(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent input, JCheckBox chk) {
        gbc.gridx = 0; gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(lbl, gbc);

        // Panel con chứa Input + Checkbox
        JPanel subPanel = new JPanel(new BorderLayout(5, 0));
        subPanel.setBackground(Color.WHITE);
        subPanel.add(input, BorderLayout.CENTER);
        subPanel.add(chk, BorderLayout.EAST);

        gbc.gridx = 1; 
        gbc.weightx = 0.7;
        panel.add(subPanel, gbc);
    }

    // [LAYOUT] Thêm dòng: Label + Password + Checkbox + Validation
    private void addFormRowPasswordWithValidation(JPanel panel, GridBagConstraints gbc, int row, String labelText, JComponent input, JCheckBox chk, JLabel valLabel) {
        addFormRowPassword(panel, gbc, row, labelText, input, chk);

        GridBagConstraints gbcVal = (GridBagConstraints) gbc.clone();
        gbcVal.gridx = 1;
        gbcVal.gridy = row;
        gbcVal.insets = new Insets(40, 0, 0, 0);
        gbcVal.anchor = GridBagConstraints.WEST;
        panel.add(valLabel, gbcVal);
    }

    // =================================================================
    // LOGIC XỬ LÝ (VALIDATION & SUBMIT)
    // =================================================================

    // [VALIDATION] Thiết lập logic kiểm tra real-time (Ngay khi gõ phím)
    private void setupValidation(JTextField txt, JLabel lbl, int requiredLength, String fieldName) {
        txt.getDocument().addDocumentListener(new DocumentListener() {
            public void changedUpdate(DocumentEvent e) { validate(); }
            public void removeUpdate(DocumentEvent e) { validate(); }
            public void insertUpdate(DocumentEvent e) { validate(); }

            public void validate() {
                String text = txt.getText().trim();
                if (text.isEmpty()) {
                    lbl.setText(" "); 
                    txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(new Color(200, 200, 200)), new EmptyBorder(5, 10, 5, 10)));
                } else if (!text.matches("\\d+")) {
                    lbl.setText("⚠ " + fieldName + " chỉ được chứa số");
                    lbl.setForeground(COL_ERROR);
                    txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(COL_ERROR), new EmptyBorder(5, 10, 5, 10)));
                } else if (text.length() != requiredLength) {
                    lbl.setText("⚠ Phải đủ " + requiredLength + " số (Hiện: " + text.length() + ")");
                    lbl.setForeground(COL_ERROR);
                    txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(COL_ERROR), new EmptyBorder(5, 10, 5, 10)));
                } else {
                    lbl.setText("✔ Hợp lệ");
                    lbl.setForeground(COL_SUCCESS);
                    txt.setBorder(BorderFactory.createCompoundBorder(new LineBorder(COL_SUCCESS), new EmptyBorder(5, 10, 5, 10)));
                }
            }
        });
    }

    // [SUBMIT] Xử lý sự kiện bấm nút Đăng ký
    private void handleRegister() {
        String user = txtUsername.getText().trim();
        String name = txtFullName.getText().trim();
        String dob = txtDob.getText().trim();
        String phone = txtPhone.getText().trim();
        String cccd = txtCccd.getText().trim();
        String email = txtEmail.getText().trim();
        String addr = txtAddress.getText().trim();
        String pass = new String(txtPass.getPassword());
        String passConf = new String(txtConfirmPass.getPassword());
        String pin = new String(txtPin.getPassword());
        String pinConf = new String(txtConfirmPin.getPassword());

        // Validate tổng thể
        if (user.isEmpty() || name.isEmpty() || phone.isEmpty() || cccd.isEmpty() || pass.isEmpty() || pin.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng điền các thông tin bắt buộc (*)", "Thiếu thông tin", JOptionPane.WARNING_MESSAGE);
            return;
        }
        if (!phone.matches("\\d{10}")) {
            JOptionPane.showMessageDialog(this, "Số điện thoại không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!cccd.matches("\\d{12}")) {
            JOptionPane.showMessageDialog(this, "CCCD không hợp lệ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pass.equals(passConf)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pin.equals(pinConf)) {
            JOptionPane.showMessageDialog(this, "Mã PIN xác nhận không khớp!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (!pin.matches("\\d{6}")) {
            JOptionPane.showMessageDialog(this, "Mã PIN phải đủ 6 số!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Gọi Service để đăng ký
        try {
            customerService.registerNewUserFullProcess(user, name, pass, cccd, email, phone, addr, dob, pin);
            JOptionPane.showMessageDialog(this, "Đăng ký thành công! Vui lòng đăng nhập.", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            new Login().setVisible(true);
            this.dispose();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi đăng ký: " + e.getMessage(), "Lỗi hệ thống", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {}
        SwingUtilities.invokeLater(() -> new Register().setVisible(true));
    }
}