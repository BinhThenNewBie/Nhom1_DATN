/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.TaikhoanDAO;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.GradientPaint;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Dimension;
import java.awt.Cursor;
import java.awt.BasicStroke;
import java.awt.geom.RoundRectangle2D;
import java.util.Arrays;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author mit
 */
public class forgotpass2 extends javax.swing.JFrame {

    TaikhoanDAO tkDAO = new TaikhoanDAO();

    // Declare all instance variables
    private JLabel lblTitle, lblMatKhauMoi, lblXacDinhMatKhau;
    private RoundedPasswordField newPasswordField, confirmPasswordField;  // Fixed field names
    private JButton btnXacNhan;
    private JPanel cardPanel;
    private JLabel statusLabel;
    private JButton showNewPasswordButton, showConfirmPasswordButton;
    private boolean isNewPasswordVisible = false;
    private boolean isConfirmPasswordVisible = false;

    // Reference fields for backward compatibility (if needed elsewhere in code)
    private JPasswordField txtnewpassword, txtcfnewpassword;

    /**
     * Creates new form forgotpass2
     */
    public forgotpass2() {
        setSize(720, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Đổi Mật Khẩu");

        // Initialize the UI
        setupCustomUI();

        // Map new fields to old field references for compatibility
        txtnewpassword = newPasswordField;
        txtcfnewpassword = confirmPasswordField;
    }

    public boolean txttrong() { //kiểm tra nếu ô text trống
        char[] pass = txtnewpassword.getPassword();
        char[] passcf = txtcfnewpassword.getPassword();
        String strpass = new String(pass);
        String strpasscf = new String(passcf);
        boolean check = false;
        if (strpass.isEmpty() && strpasscf.isEmpty()) {
            return check;
        }
        if (strpass.isEmpty() || strpasscf.isEmpty()) {
            return check;
        } else {
            check = true;
        }
        return check;

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

        // Tạo bảng đổi mật khẩu
        JPanel loginPanel = new RoundedPanel(25);
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));
        loginPanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        loginPanel.setOpaque(false);
        loginPanel.setMaximumSize(new Dimension(450, 600));
        loginPanel.setPreferredSize(new Dimension(450, 600));

        // Header
        loginPanel.add(createHeaderPanel());
        loginPanel.add(Box.createVerticalStrut(30));

        // Nút kết hợp mật khẩu mới và xác định mật khẩu
        loginPanel.add(createCombinedFieldButton());
        loginPanel.add(Box.createVerticalStrut(15));

        // Xác nhận button
        loginPanel.add(createButtonPanel());
        loginPanel.add(Box.createVerticalStrut(15));

        // Status label
        statusLabel = new JLabel();
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        loginPanel.add(statusLabel);

        // Căn giữa bảng đổi mật khẩu
        container.add(loginPanel, BorderLayout.CENTER);

        return container;
    }

    // Trong createHeaderPanel()
    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // Logo placeholder (comment out if no image available)
        /*
        JLabel logoLabel = new JLabel(new ImageIcon("path/to/your/shield.png"));
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(logoLabel);
        headerPanel.add(Box.createVerticalStrut(10));
         */
        // Title
        JLabel titleLabel = new JLabel("ĐỔI MẬT KHẨU");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(20, 35, 56));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        return headerPanel;
    }

    private JPanel createCombinedFieldButton() {
        JPanel combinedPanel = new JPanel();
        combinedPanel.setOpaque(false);
        combinedPanel.setLayout(new BoxLayout(combinedPanel, BoxLayout.Y_AXIS));
        combinedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 150));

        // Trường mật khẩu mới có label và nút ẩn/hiện
        JPanel newPasswordOuterPanel = new JPanel();
        newPasswordOuterPanel.setLayout(new BoxLayout(newPasswordOuterPanel, BoxLayout.Y_AXIS));
        newPasswordOuterPanel.setOpaque(false);

        JPanel newPasswordLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        newPasswordLabelPanel.setOpaque(false);
        JLabel newPasswordLabel = new JLabel("MẬT KHẨU MỚI");
        newPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        newPasswordLabel.setForeground(new Color(20, 35, 56));
        newPasswordLabelPanel.add(newPasswordLabel);
        newPasswordOuterPanel.add(newPasswordLabelPanel);

        JPanel newPasswordPanel = new JPanel(new BorderLayout());
        newPasswordPanel.setOpaque(false);
        newPasswordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        newPasswordField = new RoundedPasswordField(""); // dùng field bo góc
        newPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        showNewPasswordButton = new JButton("👁️");
        showNewPasswordButton.setPreferredSize(new Dimension(40, 30));
        showNewPasswordButton.setBorder(BorderFactory.createEmptyBorder());
        showNewPasswordButton.setContentAreaFilled(false);
        showNewPasswordButton.setFocusPainted(false);
        showNewPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showNewPasswordButton.addActionListener(e -> toggleNewPasswordVisibility());

        newPasswordPanel.add(newPasswordField, BorderLayout.CENTER);
        newPasswordPanel.add(showNewPasswordButton, BorderLayout.EAST);

        newPasswordOuterPanel.add(Box.createVerticalStrut(5));
        newPasswordOuterPanel.add(newPasswordPanel);

        combinedPanel.add(newPasswordOuterPanel);
        combinedPanel.add(Box.createVerticalStrut(15));

        // Trường xác định mật khẩu label
        JPanel confirmPasswordOuterPanel = new JPanel();
        confirmPasswordOuterPanel.setLayout(new BoxLayout(confirmPasswordOuterPanel, BoxLayout.Y_AXIS));
        confirmPasswordOuterPanel.setOpaque(false);

        JPanel confirmPasswordLabelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        confirmPasswordLabelPanel.setOpaque(false);
        JLabel confirmPasswordLabel = new JLabel("XÁC ĐỊNH MẬT KHẨU");
        confirmPasswordLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        confirmPasswordLabel.setForeground(new Color(20, 35, 56));
        confirmPasswordLabelPanel.add(confirmPasswordLabel);
        confirmPasswordOuterPanel.add(confirmPasswordLabelPanel);

        JPanel confirmPasswordPanel = new JPanel(new BorderLayout());
        confirmPasswordPanel.setOpaque(false);
        confirmPasswordPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        confirmPasswordField = new RoundedPasswordField("");
        confirmPasswordField.setFont(new Font("Segoe UI", Font.PLAIN, 16));

        showConfirmPasswordButton = new JButton("👁️");
        showConfirmPasswordButton.setPreferredSize(new Dimension(40, 30));
        showConfirmPasswordButton.setBorder(BorderFactory.createEmptyBorder());
        showConfirmPasswordButton.setContentAreaFilled(false);
        showConfirmPasswordButton.setFocusPainted(false);
        showConfirmPasswordButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        showConfirmPasswordButton.addActionListener(e -> toggleConfirmPasswordVisibility());

        confirmPasswordPanel.add(confirmPasswordField, BorderLayout.CENTER);
        confirmPasswordPanel.add(showConfirmPasswordButton, BorderLayout.EAST);

        confirmPasswordOuterPanel.add(Box.createVerticalStrut(5));
        confirmPasswordOuterPanel.add(confirmPasswordPanel);

        combinedPanel.add(confirmPasswordOuterPanel);

        return combinedPanel;
    }

    private void toggleNewPasswordVisibility() {
        if (isNewPasswordVisible) {
            newPasswordField.setEchoChar('•');
            showNewPasswordButton.setText("👁️");
            isNewPasswordVisible = false;
        } else {
            newPasswordField.setEchoChar((char) 0);
            showNewPasswordButton.setText("🙈");
            isNewPasswordVisible = true;
        }
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // Xác nhận button - Fixed method name
        JButton btnConfirm = new GradientButton("XÁC NHẬN");
        btnConfirm.addActionListener(e -> changepassword()); // Fixed method name
        btnConfirm.setAlignmentX(Component.CENTER_ALIGNMENT);

        buttonPanel.add(btnConfirm);

        return buttonPanel;
    }

    private void toggleConfirmPasswordVisibility() {
        if (isConfirmPasswordVisible) {
            confirmPasswordField.setEchoChar('•');
            showConfirmPasswordButton.setText("👁️");
            isConfirmPasswordVisible = false;
        } else {
            confirmPasswordField.setEchoChar((char) 0);
            showConfirmPasswordButton.setText("🙈");
            isConfirmPasswordVisible = true;
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
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
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
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 0));
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

    public boolean trungpass() {
        char[] newpass = txtnewpassword.getPassword();
        char[] cfnewpass = txtcfnewpassword.getPassword();

        boolean check = Arrays.equals(newpass, cfnewpass);
        if (!check) {
            showMessage("MẬT KHẨU KHÔNG TRÙNG KHỚP", Color.RED);
            // JOptionPane.showMessageDialog(this, "Mật khẩu không trùng khớp");
        }
        return check;
    }

    public void changepassword() {
        if (txttrong() == true) {
            if (trungpass() == true) {
                String email = forgotpass.ngnhan;
                String mk = new String(txtcfnewpassword.getPassword());
                tkDAO.passwordchange(mk, email);
                showMessage("ĐỔI MẬT KHẨU THÀNH CÔNG", new Color(34, 139, 34));
            }
        } else {
            showMessage("Không được để trống ô nhập", new Color(237, 28, 36));

        }
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

        jPanel1.setBackground(new java.awt.Color(18, 31, 51));
        jPanel1.setForeground(new java.awt.Color(18, 31, 51));

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
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
            java.util.logging.Logger.getLogger(forgotpass2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(forgotpass2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(forgotpass2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(forgotpass2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new forgotpass2().setVisible(true);

            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    // End of variables declaration//GEN-END:variables
}
