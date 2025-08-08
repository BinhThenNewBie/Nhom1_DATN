/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.TaikhoanDAO;
import Model.Taikhoan;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author PC Của Bình
 */
public class Login extends javax.swing.JFrame {

    public static String emailLogin = "";
    private boolean isPasswordVisible = false;
    private JButton showPasswordButton;
    private JLabel statusLabel;
    private JTextField emailField;
    private JPasswordField passwordField;

    /**
     * Creates new form Login
     */
    public Login() {
        initComponents();
        setSize(720, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setupCustomUI();
    }

    private void setupCustomUI() {
        // Đặt nền tùy chỉnh
        setContentPane(new GradientPanel());
        setLayout(new BorderLayout());

        // Xóa bảng điều khiển cũ và tạo bố cục hiện đại mới
        getContentPane().removeAll();

        // Tạo vùng chứa chính
        JPanel mainContainer = createMainContainer();
        add(mainContainer, BorderLayout.CENTER);

        // Làm mới giao diện người dùng
        revalidate();
        repaint();
    }

    private JPanel createMainContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(50, 50, 50, 50));

        // Tạo bảng đăng nhập
        JPanel loginPanel = new RoundedPanel(25);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        loginPanel.setOpaque(false);
        loginPanel.setMaximumSize(new Dimension(450, 600));
        loginPanel.setPreferredSize(new Dimension(450, 600));

        // Header
        loginPanel.add(createHeaderPanel());
        loginPanel.add(Box.createVerticalStrut(30));

        // Nút kết hợp email và mật khẩu
        loginPanel.add(createCombinedFieldButton());
        loginPanel.add(Box.createVerticalStrut(15));

        // Login button
        loginPanel.add(createButtonPanel());
        loginPanel.add(Box.createVerticalStrut(15));

        // Status label
        statusLabel = new JLabel();
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginPanel.add(statusLabel);

        // Căn giữa bảng đăng nhập
        container.add(loginPanel, BorderLayout.CENTER);

        return container;
    }

    // Trong createHeaderPanel()
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // Thay logo bằng hình ảnh
        JLabel logoLabel = new JLabel(new ImageIcon("path/to/your/shield.png"));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Title
        JLabel titleLabel = new JLabel("ĐĂNG NHẬP");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(20, 35, 56));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(logoLabel);
        headerPanel.add(Box.createVerticalStrut(10));
        headerPanel.add(titleLabel);

        return headerPanel;
    }

    private JPanel createCombinedFieldButton() {
        JPanel combinedPanel = new JPanel();
        combinedPanel.setOpaque(false);
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Trường email có label
        JPanel emailPanel = new JPanel();
        emailPanel.setLayout(new BoxLayout(emailPanel, BoxLayout.Y_AXIS));
        emailPanel.setOpaque(false);

        JPanel emailLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        emailLabelPanel.setOpaque(false);
        JLabel emailLabel = new JLabel("EMAIL");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        emailLabel.setForeground(new Color(20, 35, 56));
        emailLabelPanel.add(emailLabel);
        emailPanel.add(emailLabelPanel);

        emailField = new RoundedTextField(""); // dùng field bo góc như password
        emailField.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        emailPanel.add(Box.createVerticalStrut(5));
        emailPanel.add(emailField);

        combinedPanel.add(emailPanel);
        combinedPanel.add(Box.createVerticalStrut(15));

        // Trường mật khẩu label
        JPanel passwordOuterPanel = new JPanel();
        passwordOuterPanel.setLayout(new BoxLayout(passwordOuterPanel, BoxLayout.Y_AXIS));
        passwordOuterPanel.setOpaque(false);

        JPanel passwordLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        passwordLabelPanel.setOpaque(false);
        JLabel passwordLabel = new JLabel("MẬT KHẨU");
        passwordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        passwordLabel.setForeground(new Color(20, 35, 56));
        passwordLabelPanel.add(passwordLabel);
        passwordOuterPanel.add(passwordLabelPanel);

        JPanel passwordPanel = new JPanel(new BorderLayout());
        passwordPanel.setOpaque(false);
        passwordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        passwordField = new RoundedPasswordField(""); // Use RoundedPasswordField
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        showPasswordButton = new JButton("👁️");
        showPasswordButton.setPreferredSize(new Dimension(40, 30));
        showPasswordButton.setBorder(BorderFactory.createEmptyBorder());
        showPasswordButton.setContentAreaFilled(false);
        showPasswordButton.setFocusPainted(false);
        showPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showPasswordButton.addActionListener(e -> togglePasswordVisibility());

        passwordPanel.add(passwordField, BorderLayout.CENTER);
        passwordPanel.add(showPasswordButton, BorderLayout.EAST);

        passwordOuterPanel.add(Box.createVerticalStrut(5));
        passwordOuterPanel.add(passwordPanel);

        combinedPanel.add(passwordOuterPanel);

        return combinedPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // Login button
        JButton btnLogin = new GradientButton("ĐĂNG NHẬP");
        btnLogin.addActionListener(e -> Login());
        btnLogin.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(btnLogin);
        buttonPanel.add(Box.createVerticalStrut(15));

        // Forgot password link
        JLabel forgotLabel = new JLabel("<html><u>Quên mật khẩu?</u></html>");
        forgotLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        forgotLabel.setForeground(new Color(20, 35, 56));
        forgotLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        forgotLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        forgotLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                forgotpass();
            }
        });

        buttonPanel.add(forgotLabel);

        return buttonPanel;
    }

    private void togglePasswordVisibility() {
        if (isPasswordVisible) {
            passwordField.setEchoChar('•');
            showPasswordButton.setText("👁️");
            isPasswordVisible = false;
        } else {
            passwordField.setEchoChar((char) 0);
            showPasswordButton.setText("🙈");
            isPasswordVisible = true;
        }
    }

    private void showMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);

        Timer timer = new Timer(3000, e -> statusLabel.setText(""));
        timer.setRepeats(false);
        timer.start();
    }

    //// Thành phần tùy chỉnh
    class GradientPanel extends JPanel {

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(20, 35, 56),
                    getWidth(), getHeight(), new Color(20, 35, 56).darker()
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }

    class RoundedPanel extends JPanel {

        private int radius;

        public RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(new Color(255, 255, 255, 240));
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));

            g2d.setColor(new Color(20, 35, 56, 50));
            g2d.setStroke(new BasicStroke(1));
            g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, radius, radius));
        }
    }

    class RoundedTextField extends JTextField {

        public RoundedTextField(String placeholder) {
            super(placeholder);
            setPreferredSize(new Dimension(0, 45));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0)); // Adjust for icon
            setOpaque(false);
            setForeground(new Color(20, 35, 56));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(getBackground());
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

            if (hasFocus()) {
                g2d.setColor(new Color(20, 35, 56));
                g2d.setStroke(new BasicStroke(2));
            } else {
                g2d.setColor(new Color(225, 229, 233));
                g2d.setStroke(new BasicStroke(2));
            }
            g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));

            super.paintComponent(g);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            setBackground(Color.WHITE);
        }
    }

    class RoundedPasswordField extends JPasswordField {

        public RoundedPasswordField(String placeholder) {
            super(placeholder);
            setPreferredSize(new Dimension(0, 45));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
            setFont(new Font("Segoe UI", Font.PLAIN, 16));
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0)); // Adjust for icon
            setOpaque(false);
            setForeground(new Color(20, 35, 56));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2d.setColor(getBackground());
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

            if (hasFocus()) {
                g2d.setColor(new Color(20, 35, 56));
                g2d.setStroke(new BasicStroke(2));
            } else {
                g2d.setColor(new Color(225, 229, 233));
                g2d.setStroke(new BasicStroke(2));
            }
            g2d.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, 12, 12));

            super.paintComponent(g);
        }

        @Override
        public void updateUI() {
            super.updateUI();
            setBackground(Color.WHITE);
        }
    }

    class GradientButton extends JButton {

        public GradientButton(String text) {
            super(text);
            setPreferredSize(new Dimension(0, 50));
            setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));
            setFont(new Font("Segoe UI", Font.BOLD, 16));
            setForeground(Color.WHITE);
            setBorder(BorderFactory.createEmptyBorder());
            setContentAreaFilled(false);
            setFocusPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(20, 35, 56),
                    getWidth(), getHeight(), new Color(20, 35, 56).darker()
            );
            g2d.setPaint(gradient);
            g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 12, 12));

            super.paintComponent(g);
        }
    }

    public void forgotpass() {
        forgotpass fgp = new forgotpass();
        fgp.setVisible(true);
    }

    public boolean txttrong() { // kiểm tra nếu các ô txt trống 
        String usernamein = emailField.getText();
        char[] passwordin = passwordField.getPassword();
        boolean check = false;
        String passwordStr = new String(passwordin);

        if (usernamein.isEmpty() || passwordStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Xin vui lòng nhập đầy đủ thông tin", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            check = true;
        }
        return check;
    }

    public void kttk() { // kiểm tra kiểm tra xem tài khoản có trong csdl không
        String usernamein = emailField.getText();
        char[] passwordin = passwordField.getPassword();
        boolean loginSuccess = false;
        String passwordStr = new String(passwordin);
        TaikhoanDAO tkDAO = new TaikhoanDAO();
        List<Taikhoan> dstk = tkDAO.GETALL();

        for (Taikhoan tk : dstk) {
            if (usernamein.equals(tk.getEmail()) && passwordStr.equals(tk.getPass())) {
                loginSuccess = true;

                // luu lại tên nhân viên
                String TenNV = TimNhanVienTheoEmail(usernamein);
                if (TenNV != null) {
                    Login.emailLogin = TenNV;
                    System.err.println("Đăng nhập bởi: " + TenNV);
                }

                if (tk.getVaiTro().equals("ADMIN")) {
                    Admin ad = new Admin();
                    ad.setVisible(true);
                } else if (tk.getVaiTro().equals("STAFF")) {
                    Staff st = new Staff();
                    st.setVisible(true);
                }

                break;
            }
        }

        if (!loginSuccess) {
            JOptionPane.showMessageDialog(this, "Sai thông tin đăng nhập!!!");
        } else if (loginSuccess) {
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công");
        }
    }

    public void Login() {
        if (txttrong() == true) {
            kttk();
            this.dispose();
        }
    }

    public String TimNhanVienTheoEmail(String email) {
        TaikhoanDAO tkDAO = new TaikhoanDAO();
        List<Taikhoan> dsNV = tkDAO.GETALL();
        for (Taikhoan tk : dsNV) {
            if (tk.getEmail().equalsIgnoreCase(email)) {
                return tk.getTenNV();
            }
        }
        return null;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBackground(new java.awt.Color(20, 35, 56));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 713, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 510, Short.MAX_VALUE)
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

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Login.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Login().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
