/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.TaikhoanDAO;
import Model.Taikhoan;
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
import java.util.List;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.BoxLayout;
import javax.swing.BorderFactory;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import util.Email;

/**
 *
 * @author PC Của Bình
 */
public class forgotpass extends javax.swing.JFrame {
    TaikhoanDAO tkDAO = new TaikhoanDAO();    
    
    // Khai báo các biến instance
    private RoundedTextField txtemailin, txtcodein;
    private JButton btnNhanMa, btnXacNhan;
    private JLabel statusLabel;
    
    // Biến xử lý mã xác nhận
    String ngaunhien = Email.ngaunhien();
    String magui; // BIẾN ĐỂ LƯU MÃ GỬI GẦN NHẤT 
    public static String ngnhan; // BIẾN ĐỂ LƯU EMAIL NGƯỜI NHẬN
    
    /**
     * TẠO FORM FORGOTPASS MỚI
     */
    public forgotpass() {
        setSize(720, 550);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle("NHẬN MÃ XÁC NHẬN");
        
        // KHỞI TẠO GIAO DIỆN
        setupCustomUI();
    }
 
    private void setupCustomUI() {
        // ĐẶT NỀN TÙY CHỈNH
        setContentPane(new GradientPanel());
        setLayout(new BorderLayout());

        // XÓA PANEL CŨ VÀ TẠO LAYOUT MỚI
        getContentPane().removeAll();

        // TẠO CONTAINER CHÍNH
        JPanel mainContainer = createMainContainer();
        add(mainContainer, BorderLayout.CENTER);

        // LÀM MỚI GIAO DIỆN
        revalidate();
        repaint();
    }

    private JPanel createMainContainer() {
        JPanel container = new JPanel(new BorderLayout());
        container.setOpaque(false);
        container.setBorder(new EmptyBorder(50, 50, 50, 50));

        // TẠO PANEL NHẬN MÃ
        JPanel codePanel = new RoundedPanel(25);
        codePanel.setLayout(new BoxLayout(codePanel, BoxLayout.Y_AXIS));
        codePanel.setBorder(new EmptyBorder(40, 40, 40, 40));
        codePanel.setOpaque(false);
        codePanel.setMaximumSize(new Dimension(450, 500));
        codePanel.setPreferredSize(new Dimension(450, 500));

        // HEADER
        codePanel.add(createHeaderPanel());
        codePanel.add(Box.createVerticalStrut(30));

        // CÁC TRƯỜNG NHẬP LIỆU
        codePanel.add(createInputFieldsPanel());
        codePanel.add(Box.createVerticalStrut(25));

        // CÁC NÚT BẤM
        codePanel.add(createButtonPanel());
        codePanel.add(Box.createVerticalStrut(15));

        // STATUS LABEL
        statusLabel = new JLabel();
        statusLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        statusLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        codePanel.add(statusLabel);

        // CĂN GIỮA PANEL
        container.add(codePanel, BorderLayout.CENTER);

        return container;
    }

    private JPanel createHeaderPanel() {
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);

        // TITLE
        JLabel titleLabel = new JLabel("NHẬN MÃ");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 32));
        titleLabel.setForeground(new Color(20, 35, 56));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        headerPanel.add(titleLabel);

        return headerPanel;
    }

    private JPanel createInputFieldsPanel() {
        JPanel fieldsPanel = new JPanel();
        fieldsPanel.setOpaque(false);
        fieldsPanel.setLayout(new BoxLayout(fieldsPanel, BoxLayout.Y_AXIS));
        fieldsPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 200));

        // EMAIL FIELD
        JPanel emailPanel = createFieldWithLabel("EMAIL LIÊN KẾT VỚI TÀI KHOẢN");
        txtemailin = new RoundedTextField("");
        txtemailin.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        emailPanel.add(Box.createVerticalStrut(5));
        emailPanel.add(txtemailin);
        
        fieldsPanel.add(emailPanel);
        fieldsPanel.add(Box.createVerticalStrut(20));

        // MÃ XÁC NHẬN FIELD
        JPanel codePanel = createFieldWithLabel("MÃ XÁC NHẬN");
        txtcodein = new RoundedTextField("");
        txtcodein.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        
        codePanel.add(Box.createVerticalStrut(5));
        codePanel.add(txtcodein);
        
        fieldsPanel.add(codePanel);

        return fieldsPanel;
    }

    private JPanel createFieldWithLabel(String labelText) {
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(new BoxLayout(fieldPanel, BoxLayout.Y_AXIS));
        fieldPanel.setOpaque(false);

        JPanel labelPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        labelPanel.setOpaque(false);
        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 14));
        label.setForeground(new Color(20, 35, 56));
        labelPanel.add(label);
        fieldPanel.add(labelPanel);

        return fieldPanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        buttonPanel.setOpaque(false);

        // CONTAINER CHO 2 NÚT NẰM NGANG
        JPanel buttonsContainer = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonsContainer.setOpaque(false);

        // NÚT NHẬN MÃ
        btnNhanMa = new GradientButton("NHẬN MÃ");
        btnNhanMa.setPreferredSize(new Dimension(120, 45));
        btnNhanMa.addActionListener(e -> nhanma());

        // NÚT XÁC NHẬN
        btnXacNhan = new GradientButton("XÁC NHẬN");
        btnXacNhan.setPreferredSize(new Dimension(120, 45));
        btnXacNhan.addActionListener(e -> checkma());

        buttonsContainer.add(btnNhanMa);
        buttonsContainer.add(btnXacNhan);

        buttonPanel.add(buttonsContainer);

        return buttonPanel;
    }

    private void showMessage(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setForeground(color);

        Timer timer = new Timer(3000, e -> statusLabel.setText(""));
        timer.setRepeats(false);
        timer.start();
    }

    
    public boolean checkmail(){
        List<Taikhoan> dstk = tkDAO.GETALL();
        boolean check = false;
        for (Taikhoan tk : dstk) {
            if(txtemailin.getText().equals(tk.getEmail())){
                check = true;
            }
        }
        return check;
    }
    
    // XỬ LÝ SỰ KIỆN NÚT NHẬN MÃ
    public void nhanma() {
        
        ngnhan = txtemailin.getText().trim();
        String tieude = "MÃ XÁC NHẬN TÀI KHOẢN, VUI LÒNG KHÔNG CHIA SẺ";
        
        if (txtemailin.getText().isEmpty()) {
            showMessage("VUI LÒNG NHẬP EMAIL ĐỂ NHẬN MÃ XÁC NHẬN!", Color.RED);
            // JOptionPane.showMessageDialog(this, "VUI LÒNG NHẬP EMAIL ĐỂ NHẬN MÃ XÁC NHẬN");
        } if(checkmail() == true) {
            Email.sendEmail(ngnhan, tieude, "MÃ Ở ĐÂY: " + ngaunhien);
            magui = ngaunhien; 
            showMessage("Bạn sẽ nhận được mã nếu email có liên kết với tài khoản", new Color(34, 139, 34));
        } if(checkmail() == false){
            showMessage("Bạn sẽ nhận được mã nếu email có liên kết với tài khoản", new Color(34, 139, 34));
        }
    }
    
    // XỬ LÝ SỰ KIỆN NÚT XÁC NHẬN
    public void checkma(){
        String codein = txtcodein.getText().trim();
        
        if(codein.isEmpty()) {
            showMessage("VUI LÒNG NHẬP MÃ XÁC NHẬN!", Color.RED);
            return;
        }
        
        if(codein.equals(magui)){
            showMessage("XÁC NHẬN THÀNH CÔNG! CHUYỂN SANG FORM ĐỔI MẬT KHẨU...", new Color(34, 139, 34));
            
            // DELAY RỒI CHUYỂN FORM
            Timer timer = new Timer(1500, e -> {
                forgotpass2 fgp2 = new forgotpass2();
                fgp2.setVisible(true);
                dispose();
            });
            timer.setRepeats(false);
            timer.start();
        } else {
            showMessage("MÃ KHÔNG CHÍNH XÁC!", Color.RED);
            // JOptionPane.showMessageDialog(this, "MÃ KHÔNG CHÍNH XÁC");
        }
    }

    //// CÁC COMPONENT TÙY CHỈNH
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
            setBorder(BorderFactory.createEmptyBorder(0, 14, 0, 14));
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
            java.util.logging.Logger.getLogger(forgotpass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(forgotpass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(forgotpass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(forgotpass.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new forgotpass().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    // End of variables declaration//GEN-END:variables
}
