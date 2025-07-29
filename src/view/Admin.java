/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Insets;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.border.EmptyBorder;
import view.QuanLyNhanVien;
import view.QuanLySanPham;
import view.QuanLyTaiKhoan;
import view.QuanLyUuDai;

/**
 *
 * @author Admin
 */
public class Admin extends javax.swing.JFrame {

    /**
     * Creates new form Admin
     */
    public Admin() {
        initComponents();
        Timer timer = new Timer(0, (e) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String time = sdf.format(new Date());
            lblTime.setText(time);
        });
        timer.start();
        this.setSize(1920, 1080);
        setLocationRelativeTo(null);
        initDefaultElement();

        mainPanel.setLayout(new BorderLayout()); // Sử dụng BorderLayout
    }

    private void setForm(JComponent com) {
//        System.out.println("Hiển thị");
        mainPanel.removeAll();
        mainPanel.add(com);
        mainPanel.repaint();
        mainPanel.revalidate();
    }

    private void initDefaultElement() {
        JButton[] buttons = {btnQLNV, btnQLTK, btnQLSP, btnQLKM, btnQLBH, btnTKDT, btnXuatHoaDon, btnDangXuat};
        Color normalColor = Color.decode("#1c2e4a");   // giữ nguyên
        Color hoverColor = Color.decode("#3b5998");    // giữ nguyên
        Color selectedColor = Color.decode("#4E6688"); // giữ nguyên

        // Text cho từng button với icon đẹp
        String[] buttonTexts = {
            "  QUẢN LÝ NHÂN VIÊN",
            "  QUẢN LÝ TÀI KHOẢN",
            "️  QUẢN LÝ SẢN PHẨM",
            "  QUẢN LÝ ƯU ĐÃI",
            "  QUẢN LÝ BÁN HÀNG",
            "  THỐNG KÊ DOANH THU",
            "  XUẤT HÓA ĐƠN",
            "  LOG OUT"
        };

        for (int i = 0; i < buttons.length; i++) {
            JButton btn = buttons[i];

            // Gán text hiển thị cho button từ mảng buttonTexts
            btn.setText(buttonTexts[i]);

            // Font đẹp hơn
            btn.setFont(new Font("Segoe UI Light", Font.BOLD, 15));

            // Căn chỉnh và spacing
            btn.setHorizontalAlignment(SwingConstants.LEFT); // Căn text sang trái trong button
            btn.setVerticalAlignment(SwingConstants.CENTER); // Căn text theo chiều dọc ở giữa button
            btn.setMargin(new Insets(15, 20, 15, 20)); // Khoảng cách từ viền button đến text

            // Styling cơ bản
            btn.setBorderPainted(false); // Tắt vẽ border mặc định của button
            btn.setContentAreaFilled(false); // Tắt fill nền mặc định
            btn.setFocusPainted(false); // Tắt hiệu ứng focus (viền chấm chấm khi click)
            btn.setBorder(null); // Xóa hoàn toàn border
            btn.setOpaque(true); // Làm button không trong suốt

            // Border bo tròn đẹp
            btn.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(Color.decode("#88aaff"), 1),
                    BorderFactory.createEmptyBorder(5, 10, 5, 10)
            ));

            // Đặt kích thước để đồng nhất
            btn.setPreferredSize(new Dimension(225, 55));
            btn.setMinimumSize(new Dimension(225, 55));
            btn.setMaximumSize(new Dimension(225, 55));

            // Gán listener cho hiệu ứng hover + click
            btn.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    if (!btn.getBackground().equals(selectedColor)) {
                        btn.setBackground(hoverColor);
                        btn.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(Color.decode("#88aaff"), 2),
                                BorderFactory.createEmptyBorder(4, 9, 4, 9)
                        ));
                        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                        btn.setForeground(Color.decode("#FDEAF6"));
                    }
                }

                public void mouseExited(java.awt.event.MouseEvent evt) {
                    if (!btn.getBackground().equals(selectedColor)) {
                        btn.setBackground(normalColor);
                        btn.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(Color.decode("#88aaff"), 1),
                                BorderFactory.createEmptyBorder(5, 10, 5, 10)
                        ));
                        btn.setCursor(Cursor.getDefaultCursor());
                        btn.setForeground(Color.decode("#FDEAF6"));
                    }
                }

                public void mouseClicked(java.awt.event.MouseEvent evt) {
                    // Reset tất cả nút về màu gốc
                    for (JButton b : buttons) {
                        b.setBackground(normalColor);
                        b.setBorder(BorderFactory.createCompoundBorder(
                                BorderFactory.createLineBorder(Color.decode("#88aaff"), 1),
                                BorderFactory.createEmptyBorder(5, 10, 5, 10)
                        ));
                        b.setForeground(Color.decode("#FDEAF6"));
                    }

                    // Đặt màu click cho nút này
                    btn.setBackground(selectedColor);
                    btn.setBorder(BorderFactory.createCompoundBorder(
                            BorderFactory.createLineBorder(Color.decode("#88aaff"), 2),
                            BorderFactory.createEmptyBorder(4, 9, 4, 9)
                    ));
                    btn.setForeground(Color.decode("#FDEAF6"));

                    // Hiệu ứng click nhẹ
                    Timer timer = new Timer(100, null);
                    timer.addActionListener(e -> {
                        btn.setBackground(selectedColor);
                        timer.stop();
                    });
                    timer.start();
                }
            });
        }

        // Styling cho panel - giữ nguyên màu
        pnlMenu.setBorder(new EmptyBorder(10, 10, 10, 10));
        Logo.setBorder(new EmptyBorder(0, 0, 0, 0));
    }

// Thêm method này để setup layout cho menu nếu cần
    private void setupMenuLayout() {
        pnlMenu.setLayout(new BoxLayout(pnlMenu, BoxLayout.Y_AXIS));

        // Thêm khoảng cách giữa các button
        Component[] components = pnlMenu.getComponents();
        for (Component comp : components) {
            if (comp instanceof JButton) {
                ((JButton) comp).setAlignmentX(Component.CENTER_ALIGNMENT);
            }
        }
    }

// Method để tạo gradient background cho panel (optional)
    private void setGradientBackground(JPanel panel) {
        panel.setBackground(null);
        panel.setOpaque(false);

        // Tạo gradient paint
        Graphics2D g2d = (Graphics2D) panel.getGraphics();
        if (g2d != null) {
            GradientPaint gradient = new GradientPaint(
                    0, 0, Color.decode("#2c3e50"),
                    0, panel.getHeight(), Color.decode("#34495e")
            );
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, panel.getWidth(), panel.getHeight());
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

        pnlMenu = new javax.swing.JPanel();
        btnQLNV = new javax.swing.JButton();
        lblexit = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        Logo = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        btnQLTK = new javax.swing.JButton();
        btnQLSP = new javax.swing.JButton();
        btnQLKM = new javax.swing.JButton();
        btnTKDT = new javax.swing.JButton();
        btnQLBH = new javax.swing.JButton();
        btnDangXuat = new javax.swing.JButton();
        btnXuatHoaDon = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlMenu.setBackground(new java.awt.Color(21, 35, 56));
        pnlMenu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                pnlMenuMouseClicked(evt);
            }
        });

        btnQLNV.setBackground(new java.awt.Color(31, 51, 86));
        btnQLNV.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        btnQLNV.setForeground(new java.awt.Color(255, 255, 255));
        btnQLNV.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/stafff.png"))); // NOI18N
        btnQLNV.setText(" QUẢN LÝ NHÂN VIÊN");
        btnQLNV.setBorder(null);
        btnQLNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnQLNVMouseClicked(evt);
            }
        });
        btnQLNV.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQLNVActionPerformed(evt);
            }
        });

        lblexit.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/dangxuat.png"))); // NOI18N
        lblexit.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblexitMouseClicked(evt);
            }
        });

        lblTime.setBackground(new java.awt.Color(104, 142, 216));
        lblTime.setFont(new java.awt.Font("DialogInput", 1, 18)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 255, 255));

        Logo.setBackground(new java.awt.Color(21, 35, 56));

        jLabel1.setFont(new java.awt.Font("Segoe UI Light", 1, 26)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/trangchu.png"))); // NOI18N
        jLabel1.setText(" ADMIN");

        javax.swing.GroupLayout LogoLayout = new javax.swing.GroupLayout(Logo);
        Logo.setLayout(LogoLayout);
        LogoLayout.setHorizontalGroup(
            LogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LogoLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        LogoLayout.setVerticalGroup(
            LogoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        btnQLTK.setBackground(new java.awt.Color(31, 51, 86));
        btnQLTK.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        btnQLTK.setForeground(new java.awt.Color(255, 255, 255));
        btnQLTK.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/account.png"))); // NOI18N
        btnQLTK.setText(" QUẢN LÝ TÀI KHOẢN");
        btnQLTK.setBorder(null);
        btnQLTK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnQLTKMouseClicked(evt);
            }
        });
        btnQLTK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQLTKActionPerformed(evt);
            }
        });

        btnQLSP.setBackground(new java.awt.Color(31, 51, 86));
        btnQLSP.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        btnQLSP.setForeground(new java.awt.Color(255, 255, 255));
        btnQLSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/caphe.png"))); // NOI18N
        btnQLSP.setText(" QUẢN LÝ SẢN PHẨM");
        btnQLSP.setBorder(null);
        btnQLSP.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnQLSPMouseClicked(evt);
            }
        });
        btnQLSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQLSPActionPerformed(evt);
            }
        });

        btnQLKM.setBackground(new java.awt.Color(31, 51, 86));
        btnQLKM.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        btnQLKM.setForeground(new java.awt.Color(255, 255, 255));
        btnQLKM.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/uudai.png"))); // NOI18N
        btnQLKM.setText("QUẢN LÝ KHUYẾN MÃI");
        btnQLKM.setBorder(null);
        btnQLKM.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnQLKMMouseClicked(evt);
            }
        });
        btnQLKM.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQLKMActionPerformed(evt);
            }
        });

        btnTKDT.setBackground(new java.awt.Color(31, 51, 86));
        btnTKDT.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        btnTKDT.setForeground(new java.awt.Color(255, 255, 255));
        btnTKDT.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/thongke.png"))); // NOI18N
        btnTKDT.setText("THỐNG KÊ DOANH THU");
        btnTKDT.setBorder(null);
        btnTKDT.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTKDTMouseClicked(evt);
            }
        });
        btnTKDT.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTKDTActionPerformed(evt);
            }
        });

        btnQLBH.setBackground(new java.awt.Color(31, 51, 86));
        btnQLBH.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        btnQLBH.setForeground(new java.awt.Color(255, 255, 255));
        btnQLBH.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/sell.png"))); // NOI18N
        btnQLBH.setText(" QUẢN LÝ BÁN HÀNG");
        btnQLBH.setBorder(null);
        btnQLBH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnQLBHMouseClicked(evt);
            }
        });
        btnQLBH.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnQLBHActionPerformed(evt);
            }
        });

        btnDangXuat.setBackground(new java.awt.Color(31, 51, 86));
        btnDangXuat.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        btnDangXuat.setForeground(new java.awt.Color(255, 255, 255));
        btnDangXuat.setText("LOG OUT");
        btnDangXuat.setBorder(null);
        btnDangXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDangXuatMouseClicked(evt);
            }
        });
        btnDangXuat.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDangXuatActionPerformed(evt);
            }
        });

        btnXuatHoaDon.setBackground(new java.awt.Color(31, 51, 86));
        btnXuatHoaDon.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        btnXuatHoaDon.setForeground(new java.awt.Color(255, 255, 255));
        btnXuatHoaDon.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/print.png"))); // NOI18N
        btnXuatHoaDon.setText("XUẤT HÓA ĐƠN");
        btnXuatHoaDon.setBorder(null);
        btnXuatHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnXuatHoaDonMouseClicked(evt);
            }
        });
        btnXuatHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXuatHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlMenuLayout = new javax.swing.GroupLayout(pnlMenu);
        pnlMenu.setLayout(pnlMenuLayout);
        pnlMenuLayout.setHorizontalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlMenuLayout.createSequentialGroup()
                .addGap(0, 28, Short.MAX_VALUE)
                .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 265, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(Logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlMenuLayout.createSequentialGroup()
                        .addGap(16, 16, 16)
                        .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlMenuLayout.createSequentialGroup()
                                .addComponent(lblexit, javax.swing.GroupLayout.PREFERRED_SIZE, 57, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 119, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(btnQLTK, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnQLSP, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnQLKM, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnTKDT, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
                            .addComponent(btnQLBH, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnQLNV, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pnlMenuLayout.setVerticalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addComponent(Logo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(48, 48, 48)
                .addComponent(btnQLNV, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(55, 55, 55)
                .addComponent(btnQLTK, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(btnQLSP, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(btnQLKM, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(btnQLBH, javax.swing.GroupLayout.PREFERRED_SIZE, 44, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(52, 52, 52)
                .addComponent(btnTKDT, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(54, 54, 54)
                .addComponent(btnXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 45, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 72, Short.MAX_VALUE)
                .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnDangXuat, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblexit, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(53, 53, 53))
        );

        mainPanel.setBackground(new java.awt.Color(234, 232, 232));

        javax.swing.GroupLayout mainPanelLayout = new javax.swing.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 970, Short.MAX_VALUE)
        );
        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(mainPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnQLNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQLNVMouseClicked
        // TODO add your handling code here:
        btnQLNV.setBackground(Color.decode("#4E6688"));
        btnQLNV.setForeground(Color.decode("#FDFAF6"));
        btnQLNV.setFocusPainted(false);       // Tắt viền khi focus
        btnQLNV.setBorderPainted(false);      // Tắt viền ngoài
        btnQLNV.setContentAreaFilled(false);  // Tắt nền mặc định
        btnQLNV.setOpaque(true);              // Đảm bảo có thể set background màu nếu cần

        btnQLTK.setBackground(Color.decode("#1c2e4a"));
        btnQLTK.setForeground(Color.decode("#FDFAF6"));

        btnQLSP.setBackground(Color.decode("#1c2e4a"));
        btnQLSP.setForeground(Color.decode("#FDFAF6"));

        btnQLKM.setBackground(Color.decode("#1c2e4a"));
        btnQLKM.setForeground(Color.decode("#FDFAF6"));

        btnQLBH.setBackground(Color.decode("#1c2e4a"));
        btnQLBH.setForeground(Color.decode("#FDFAF6"));

        btnTKDT.setBackground(Color.decode("#1c2e4a"));
        btnTKDT.setForeground(Color.decode("#FDFAF6"));

        btnDangXuat.setBackground(Color.decode("#1c2e4a"));
        btnDangXuat.setForeground(Color.decode("#FDFAF6"));
    }//GEN-LAST:event_btnQLNVMouseClicked

    private void btnQLNVActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQLNVActionPerformed
        // TODO add your handling code here:
        QuanLyNhanVien qlnvForm = new QuanLyNhanVien();
        // Lấy panel cụ thể (ví dụ: panel chính có tên jPanel1)
        JPanel panelQLNV = qlnvForm.getJPanelQLNV();

        setForm(panelQLNV);
    }//GEN-LAST:event_btnQLNVActionPerformed

    private void lblexitMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblexitMouseClicked
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_lblexitMouseClicked

    private void btnQLTKMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQLTKMouseClicked
        // TODO add your handling code here:
        btnQLTK.setBackground(Color.decode("#4E6688"));
        btnQLTK.setForeground(Color.decode("#FDFAF6"));
        btnQLTK.setFocusPainted(false);       // Tắt viền khi focus
        btnQLTK.setBorderPainted(false);      // Tắt viền ngoài
        btnQLTK.setContentAreaFilled(false);  // Tắt nền mặc định
        btnQLTK.setOpaque(true);              // Đảm bảo có thể set background màu nếu cần

        btnQLNV.setBackground(Color.decode("#1c2e4a"));
        btnQLNV.setForeground(Color.decode("#FDFAF6"));

        btnQLSP.setBackground(Color.decode("#1c2e4a"));
        btnQLSP.setForeground(Color.decode("#FDFAF6"));

        btnQLKM.setBackground(Color.decode("#1c2e4a"));
        btnQLKM.setForeground(Color.decode("#FDFAF6"));

        btnQLBH.setBackground(Color.decode("#1c2e4a"));
        btnQLBH.setForeground(Color.decode("#FDFAF6"));

        btnTKDT.setBackground(Color.decode("#1c2e4a"));
        btnTKDT.setForeground(Color.decode("#FDFAF6"));

        btnDangXuat.setBackground(Color.decode("#1c2e4a"));
        btnDangXuat.setForeground(Color.decode("#FDFAF6"));
    }//GEN-LAST:event_btnQLTKMouseClicked

    private void btnQLTKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQLTKActionPerformed
        // TODO add your handling code here:        
        QuanLyTaiKhoan qltkForm = new QuanLyTaiKhoan();
        JPanel panelQLTK = qltkForm.getJPanelQLTK();
        setForm(panelQLTK);

    }//GEN-LAST:event_btnQLTKActionPerformed

    private void btnQLSPMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQLSPMouseClicked
        // TODO add your handling code here:
        btnQLSP.setBackground(Color.decode("#4E6688"));
        btnQLSP.setForeground(Color.decode("#FDFAF6"));
        btnQLSP.setFocusPainted(false);       // Tắt viền khi focus
        btnQLSP.setBorderPainted(false);      // Tắt viền ngoài
        btnQLSP.setContentAreaFilled(false);  // Tắt nền mặc định
        btnQLSP.setOpaque(true);              // Đảm bảo có thể set background màu nếu cần

        btnQLTK.setBackground(Color.decode("#1c2e4a"));
        btnQLTK.setForeground(Color.decode("#FDFAF6"));

        btnQLNV.setBackground(Color.decode("#1c2e4a"));
        btnQLNV.setForeground(Color.decode("#FDFAF6"));

        btnQLKM.setBackground(Color.decode("#1c2e4a"));
        btnQLKM.setForeground(Color.decode("#FDFAF6"));

        btnQLBH.setBackground(Color.decode("#1c2e4a"));
        btnQLBH.setForeground(Color.decode("#FDFAF6"));

        btnTKDT.setBackground(Color.decode("#1c2e4a"));
        btnTKDT.setForeground(Color.decode("#FDFAF6"));

        btnDangXuat.setBackground(Color.decode("#1c2e4a"));
        btnDangXuat.setForeground(Color.decode("#FDFAF6"));
    }//GEN-LAST:event_btnQLSPMouseClicked

    private void btnQLSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQLSPActionPerformed
        // TODO add your handling code here:
        QuanLySanPham qlspForm = new QuanLySanPham();
        // Lấy panel cụ thể (ví dụ: panel chính có tên jPanel1)
        JPanel panelQLSP = qlspForm.getJPanelQLSP();

        setForm(panelQLSP);
    }//GEN-LAST:event_btnQLSPActionPerformed

    private void btnQLKMMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQLKMMouseClicked
        // TODO add your handling code here:
        btnQLKM.setBackground(Color.decode("#4E6688"));
        btnQLKM.setForeground(Color.decode("#FDFAF6"));
        btnQLKM.setFocusPainted(false);       // Tắt viền khi focus
        btnQLKM.setBorderPainted(false);      // Tắt viền ngoài
        btnQLKM.setContentAreaFilled(false);  // Tắt nền mặc định
        btnQLKM.setOpaque(true);              // Đảm bảo có thể set background màu nếu cần

        btnQLTK.setBackground(Color.decode("#1c2e4a"));
        btnQLTK.setForeground(Color.decode("#FDFAF6"));

        btnQLSP.setBackground(Color.decode("#1c2e4a"));
        btnQLSP.setForeground(Color.decode("#FDFAF6"));

        btnQLNV.setBackground(Color.decode("#1c2e4a"));
        btnQLNV.setForeground(Color.decode("#FDFAF6"));

        btnQLBH.setBackground(Color.decode("#1c2e4a"));
        btnQLBH.setForeground(Color.decode("#FDFAF6"));

        btnTKDT.setBackground(Color.decode("#1c2e4a"));
        btnTKDT.setForeground(Color.decode("#FDFAF6"));

        btnDangXuat.setBackground(Color.decode("#1c2e4a"));
        btnDangXuat.setForeground(Color.decode("#FDFAF6"));
    }//GEN-LAST:event_btnQLKMMouseClicked

    private void btnQLKMActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQLKMActionPerformed
        // TODO add your handling code here:
        QuanLyUuDai qludForm = new QuanLyUuDai();
        // Lấy panel cụ thể (ví dụ: panel chính có tên jPanel1)
        JPanel panelQLUD = qludForm.getJPanelQLUD();

        setForm(panelQLUD);
    }//GEN-LAST:event_btnQLKMActionPerformed

    private void btnTKDTMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTKDTMouseClicked
        // TODO add your handling code here:
        btnTKDT.setBackground(Color.decode("#4E6688"));
        btnTKDT.setForeground(Color.decode("#FDFAF6"));
        btnTKDT.setFocusPainted(false);       // Tắt viền khi focus
        btnTKDT.setBorderPainted(false);      // Tắt viền ngoài
        btnTKDT.setContentAreaFilled(false);  // Tắt nền mặc định
        btnTKDT.setOpaque(true);              // Đảm bảo có thể set background màu nếu cần

        btnQLTK.setBackground(Color.decode("#1c2e4a"));
        btnQLTK.setForeground(Color.decode("#FDFAF6"));

        btnQLSP.setBackground(Color.decode("#1c2e4a"));
        btnQLSP.setForeground(Color.decode("#FDFAF6"));

        btnQLNV.setBackground(Color.decode("#1c2e4a"));
        btnQLNV.setForeground(Color.decode("#FDFAF6"));

        btnQLKM.setBackground(Color.decode("#1c2e4a"));
        btnQLKM.setForeground(Color.decode("#FDFAF6"));

        btnQLBH.setBackground(Color.decode("#1c2e4a"));
        btnQLBH.setForeground(Color.decode("#FDFAF6"));

        btnDangXuat.setBackground(Color.decode("#1c2e4a"));
        btnDangXuat.setForeground(Color.decode("#FDFAF6"));
    }//GEN-LAST:event_btnTKDTMouseClicked

    private void btnTKDTActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTKDTActionPerformed
        // TODO add your handling code here:
        ThongKe thongKeForm = new ThongKe();
        JPanel panelTK = thongKeForm.getPanelThongKe();
        setForm(panelTK);
    }//GEN-LAST:event_btnTKDTActionPerformed

    private void btnQLBHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnQLBHMouseClicked
        // TODO add your handling code here:
        btnQLBH.setBackground(Color.decode("#4E6688"));
        btnQLBH.setForeground(Color.decode("#FDFAF6"));
        btnQLBH.setFocusPainted(false);       // Tắt viền khi focus
        btnQLBH.setBorderPainted(false);      // Tắt viền ngoài
        btnQLBH.setContentAreaFilled(false);  // Tắt nền mặc định
        btnQLBH.setOpaque(true);              // Đảm bảo có thể set background màu nếu cần

        btnQLTK.setBackground(Color.decode("#1c2e4a"));
        btnQLTK.setForeground(Color.decode("#FDFAF6"));

        btnQLSP.setBackground(Color.decode("#1c2e4a"));
        btnQLSP.setForeground(Color.decode("#FDFAF6"));

        btnQLNV.setBackground(Color.decode("#1c2e4a"));
        btnQLNV.setForeground(Color.decode("#FDFAF6"));

        btnQLKM.setBackground(Color.decode("#1c2e4a"));
        btnQLKM.setForeground(Color.decode("#FDFAF6"));

        btnTKDT.setBackground(Color.decode("#1c2e4a"));
        btnTKDT.setForeground(Color.decode("#FDFAF6"));

        btnDangXuat.setBackground(Color.decode("#1c2e4a"));
        btnDangXuat.setForeground(Color.decode("#FDFAF6"));
    }//GEN-LAST:event_btnQLBHMouseClicked

    private void btnQLBHActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnQLBHActionPerformed
        // TODO add your handling code here:
        QuanLyBanHang qlbhForm = new QuanLyBanHang();
        JPanel panelQLBH = qlbhForm.getPanelQLBH();
        setForm(panelQLBH);
    }//GEN-LAST:event_btnQLBHActionPerformed

    private void pnlMenuMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_pnlMenuMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_pnlMenuMouseClicked

    private void btnDangXuatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDangXuatMouseClicked
        // TODO add your handling code here:
        Login lg = new Login();
        lg.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnDangXuatMouseClicked

    private void btnDangXuatActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDangXuatActionPerformed
        // TODO add your handling code here:
        btnDangXuat.setBackground(Color.decode("#4E6688"));
        btnDangXuat.setForeground(Color.decode("#FDFAF6"));
        btnDangXuat.setFocusPainted(false);       // Tắt viền khi focus
        btnDangXuat.setBorderPainted(false);      // Tắt viền ngoài
        btnDangXuat.setContentAreaFilled(false);  // Tắt nền mặc định
        btnDangXuat.setOpaque(true);              // Đảm bảo có thể set background màu nếu cần

        btnQLTK.setBackground(Color.decode("#1c2e4a"));
        btnQLTK.setForeground(Color.decode("#FDFAF6"));

        btnQLSP.setBackground(Color.decode("#1c2e4a"));
        btnQLSP.setForeground(Color.decode("#FDFAF6"));

        btnQLNV.setBackground(Color.decode("#1c2e4a"));
        btnQLNV.setForeground(Color.decode("#FDFAF6"));

        btnQLBH.setBackground(Color.decode("#1c2e4a"));
        btnQLBH.setForeground(Color.decode("#FDFAF6"));

        btnQLKM.setBackground(Color.decode("#1c2e4a"));
        btnQLKM.setForeground(Color.decode("#FDFAF6"));

        btnTKDT.setBackground(Color.decode("#1c2e4a"));
        btnTKDT.setForeground(Color.decode("#FDFAF6"));


    }//GEN-LAST:event_btnDangXuatActionPerformed

    private void btnXuatHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnXuatHoaDonMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_btnXuatHoaDonMouseClicked

    private void btnXuatHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXuatHoaDonActionPerformed
        // TODO add your handling code here:
        QuanLyHoaDon qlhdForm = new QuanLyHoaDon();
        JPanel panelQLHD = qlhdForm.getPanelQLHD();
        setForm(panelQLHD);
    }//GEN-LAST:event_btnXuatHoaDonActionPerformed

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
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Admin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Admin().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Logo;
    private javax.swing.JButton btnDangXuat;
    private javax.swing.JButton btnQLBH;
    private javax.swing.JButton btnQLKM;
    private javax.swing.JButton btnQLNV;
    private javax.swing.JButton btnQLSP;
    private javax.swing.JButton btnQLTK;
    private javax.swing.JButton btnTKDT;
    private javax.swing.JButton btnXuatHoaDon;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblexit;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JPanel pnlMenu;
    // End of variables declaration//GEN-END:variables
}
