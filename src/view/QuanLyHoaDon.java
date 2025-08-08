/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.ChiTietHoaDonDAO;
import DAO.HoaDonDAO;
import Model.ChiTietHoaDon;
import Model.HoaDon;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;

/**
 *
 * @author ADMIN
 */
public class QuanLyHoaDon extends javax.swing.JFrame {

    DefaultTableModel model;
    DefaultTableModel modelCTHD;
    HoaDonDAO hdd = new HoaDonDAO();
    ChiTietHoaDonDAO cthdd = new ChiTietHoaDonDAO();

    private void improveAllTables() {
        JTable[] tables = {tblCTHD, tblHoaDon};
        for (JTable table : tables) {
            // Font và kích thước
            table.setFont(new Font("Segoe UI", Font.PLAIN, 16));
            table.setRowHeight(30);

            table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
            table.getTableHeader().setBackground(new Color(51, 51, 86));
            table.getTableHeader().setForeground(Color.WHITE);
            table.getTableHeader().setPreferredSize(new Dimension(0, 35));

            table.getTableHeader().setBorder(BorderFactory.createEmptyBorder());
            table.getTableHeader().setOpaque(true);

            table.getTableHeader().putClientProperty("JTableHeader.showBorder", false);
            table.getTableHeader().putClientProperty("JComponent.sizeVariant", "regular");
            table.getTableHeader().putClientProperty("JTableHeader.showSeparatorLine", false);

            table.getTableHeader().setDefaultRenderer(new DefaultTableCellRenderer() {
                @Override
                public Component getTableCellRendererComponent(JTable table, Object value,
                        boolean isSelected, boolean hasFocus, int row, int column) {
                    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                    setBackground(new Color(31, 51, 86));
                    setForeground(Color.WHITE);
                    setFont(new Font("Segoe UI", Font.BOLD, 16));
                    setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5)); // Không có bóng
                    setOpaque(true);
                    setHorizontalAlignment(JLabel.CENTER);
                    return this;
                }
            });

            // Màu selection - tắt bóng
            table.setSelectionBackground(new Color(173, 216, 230));
            table.setGridColor(new Color(200, 200, 200));

            // TẮT CÁC HIỆU ỨNG BÓNG VÀ FOCUS
            table.setBorder(null);
            table.setShowGrid(true); // Giữ grid nhưng không có bóng
            table.setOpaque(true);

            // Tắt các client properties gây bóng
            table.putClientProperty("JTable.autoStartsEdit", false);
            table.putClientProperty("terminateEditOnFocusLost", true);
            table.putClientProperty("JTable.showBorder", false);

            // Căn giữa nội dung
            DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
            centerRenderer.setHorizontalAlignment(JLabel.CENTER);
            centerRenderer.setOpaque(true); // Quan trọng để tắt hiệu ứng trong suốt

            for (int i = 0; i < table.getColumnCount(); i++) {
                table.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
            }
        }

        // Nếu muốn tắt hoàn toàn Look and Feel effects
        try {
            UIManager.put("Table.focusCellHighlightBorder", null);
            UIManager.put("Table.focusCellBackground", null);
            UIManager.put("Table.dropCellBackground", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public QuanLyHoaDon() {
        initComponents();
        improveAllTables();
        initTable();
        fillTable();
        fillTableCTHD();
        setLocationRelativeTo(null);
    }

    private String formatVND(float amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + " ₫";
    }

    public void initTable() {
        model = new DefaultTableModel();
        String[] cols = new String[]{"ID_HD", "NGƯỜI PHỤ TRÁCH", "NGÀY THÁNG NĂM", "THỜI GIAN", "TỔNG HOÁ ĐƠN", "TIỀN ƯU ĐÃI", "THÀNH TIỀN", "ƯU ĐÃI"};
        model.setColumnIdentifiers(cols);
        tblHoaDon.setModel(model);

        modelCTHD = new DefaultTableModel();
        String[] colsCTHD = new String[]{"ID_SP", "TÊN SẢN PHẨM", "GIÁ MÓN", "SỐ LƯỢNG"};
        modelCTHD.setColumnIdentifiers(colsCTHD);
        tblCTHD.setModel(modelCTHD);
    }

    public void fillTable() {
        model.setRowCount(0);
        List<HoaDon> list = hdd.getALL_HD();
        for (HoaDon hd : list) {
            int trangThai = hd.getTrangThai();
            if (trangThai == 1) {
                model.addRow(new Object[]{
                    hd.getID_HD(),
                    hd.getNguoiPhuTrach(),
                    hd.getNgayThangNam(),
                    hd.getThoiGian(),
                    formatVND(hd.getTongTienHD()),
                    formatVND(hd.getTongTienUuDai()),
                    formatVND(hd.getTongTienThanhToan()),
                    hd.getUuDai()
                });
            }
        }
        TableColumnModel columnModel = tblCTHD.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(166);
        columnModel.getColumn(3).setPreferredWidth(80);
    }

    public void fillTableCTHD() {
        modelCTHD.setRowCount(0);
        int i = tblHoaDon.getSelectedRow();
        if (i < 0) {
            return;
        }

        String ID_HD = model.getValueAt(i, 0).toString();
        List<ChiTietHoaDon> lstcthd = cthdd.getAll_CTHD(ID_HD);
        for (ChiTietHoaDon cthd : lstcthd) {
            modelCTHD.addRow(new Object[]{
                cthd.getID_SP(),
                cthd.getTenSP(),
                formatVND(cthd.getGiaSP()),
                cthd.getSoLuong()
            });
        }
    }

    public void showDetailsHDCho() {
        int i = tblHoaDon.getSelectedRow();
        if (i >= 0) {
            String ID_HD = tblHoaDon.getValueAt(i, 0).toString();
            lblMaHD.setText(ID_HD);
            List<ChiTietHoaDon> lstcthd = cthdd.getAll_CTHD(ID_HD);
            modelCTHD.setRowCount(0);

            for (ChiTietHoaDon cthd : lstcthd) {
                modelCTHD.addRow(new Object[]{
                    cthd.getID_SP(),
                    cthd.getTenSP(),
                    formatVND(cthd.getGiaSP()),
                    cthd.getSoLuong()
                });
            }
        }
    }

    public void add() {
        int i = tblHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoá đơn!");
            return;
        }

        if (!txtArea.getText().trim().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng xoá hoá đơn cũ để thêm mới");
            return;
        }

        String ID_HD = tblHoaDon.getValueAt(i, 0).toString();
        List<HoaDon> lsthd = hdd.getALL_ID_HD(ID_HD);
        String nguoiPhuTrach = lsthd.get(0).getNguoiPhuTrach();
        String ngayThangNam = lsthd.get(0).getNgayThangNam();
        String thoiGian = lsthd.get(0).getThoiGian();
        float tongTien_HD = lsthd.get(0).getTongTienHD();
        float tienUuDai = lsthd.get(0).getTongTienUuDai();
        float thanhTien = lsthd.get(0).getTongTienThanhToan();
        String pttt = lsthd.get(0).getPhuongThucThanhToan();
        float tienKhachHang = lsthd.get(0).getTienKhachHang();
        float tienTraLai = lsthd.get(0).getTienTraLai();
        String uuDai = lsthd.get(0).getUuDai();

        StringBuilder hoaDon = new StringBuilder();
        hoaDon.append("________________________________________\n");
        hoaDon.append("         HÓA ĐƠN THANH TOÁN\n");
        hoaDon.append("________________________________________\n");
        hoaDon.append("Mã hóa đơn        : ").append(ID_HD).append("\n");
        hoaDon.append("Ngày lập          : ").append(ngayThangNam).append("\n");
        hoaDon.append("Thời gian         : ").append(thoiGian).append("\n");
        hoaDon.append("Người phụ trách   : ").append(nguoiPhuTrach).append("\n\n");
        hoaDon.append("Danh sách món:\n");
        hoaDon.append("________________________________________\n");
        hoaDon.append(String.format("%-20s %3s %15s\n", "Tên món", "SL", "Giá món (đ)"));
        hoaDon.append("________________________________________\n");

        List<ChiTietHoaDon> ds = cthdd.getAll_CTHD(ID_HD);
        for (ChiTietHoaDon ct : ds) {
            hoaDon.append(String.format("%-25s %-5d %-15s\n", ct.getTenSP(), ct.getSoLuong(), formatVND(ct.getGiaSP())));
        }
        hoaDon.append("________________________________________\n");
        hoaDon.append(String.format("Tổng tiền             : %,15.0f đ\n", tongTien_HD));
        hoaDon.append(String.format("Ưu đãi                : %s -%,10.0f đ\n", uuDai, tienUuDai));
        hoaDon.append(String.format("Thành tiền            : %,15.0f đ\n", thanhTien));
        hoaDon.append("----------------------------------------\n");
        hoaDon.append("Phương thức thanh toán:  _").append(pttt).append("\n");
        hoaDon.append(String.format("Tiền khách đưa        : %,15.0f đ\n", tienKhachHang));
        hoaDon.append(String.format("Tiền trả lại          : %,15.0f đ\n", tienTraLai));
        hoaDon.append("________________________________________\n");
        hoaDon.append("Cảm ơn quý khách, hẹn gặp lại!\n");

        txtArea.setText(hoaDon.toString());
        txtArea.setEditable(false);
        txtArea.setFont(new Font("Monospaced", Font.PLAIN, 15));

    }

    public void clear() {
        txtArea.setText("");
        lblMaHD.setText("");
        modelCTHD.setRowCount(0);
    }

    public void printHD() {
        String txt = txtArea.getText();
        if (txt.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không có nội dung để in!", "Thông báo", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            txtArea.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pnlXuat = new javax.swing.JPanel();
        pnlXuatHoaDon = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();
        btnPrint = new javax.swing.JButton();
        pnlThongTinHoaDon = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        lblTittleHD = new javax.swing.JLabel();
        lblMaHD = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblCTHD = new javax.swing.JTable();
        btnClear = new javax.swing.JButton();
        btnAdd = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlXuat.setBackground(new java.awt.Color(31, 51, 86));
        pnlXuat.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        pnlXuat.setForeground(new java.awt.Color(0, 51, 102));

        pnlXuatHoaDon.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 2));

        txtArea.setColumns(20);
        txtArea.setRows(5);
        jScrollPane2.setViewportView(txtArea);

        btnPrint.setBackground(new java.awt.Color(31, 51, 86));
        btnPrint.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnPrint.setForeground(new java.awt.Color(255, 255, 255));
        btnPrint.setText("PRINT");
        btnPrint.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnPrintMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlXuatHoaDonLayout = new javax.swing.GroupLayout(pnlXuatHoaDon);
        pnlXuatHoaDon.setLayout(pnlXuatHoaDonLayout);
        pnlXuatHoaDonLayout.setHorizontalGroup(
            pnlXuatHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlXuatHoaDonLayout.createSequentialGroup()
                .addContainerGap(14, Short.MAX_VALUE)
                .addGroup(pnlXuatHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlXuatHoaDonLayout.createSequentialGroup()
                        .addComponent(btnPrint)
                        .addGap(177, 177, 177))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlXuatHoaDonLayout.createSequentialGroup()
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 443, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(15, 15, 15))))
        );
        pnlXuatHoaDonLayout.setVerticalGroup(
            pnlXuatHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlXuatHoaDonLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 863, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnPrint)
                .addGap(40, 40, 40))
        );

        pnlThongTinHoaDon.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 2, true));

        tblHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblHoaDonMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblHoaDon);

        lblTittleHD.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTittleHD.setForeground(new java.awt.Color(0, 51, 102));
        lblTittleHD.setText("ID_ HD:");

        lblMaHD.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblMaHD.setForeground(new java.awt.Color(0, 51, 102));

        tblCTHD.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane3.setViewportView(tblCTHD);

        btnClear.setBackground(new java.awt.Color(31, 51, 86));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("CLEAR");
        btnClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearMouseClicked(evt);
            }
        });

        btnAdd.setBackground(new java.awt.Color(31, 51, 86));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("ADD");
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlThongTinHoaDonLayout = new javax.swing.GroupLayout(pnlThongTinHoaDon);
        pnlThongTinHoaDon.setLayout(pnlThongTinHoaDonLayout);
        pnlThongTinHoaDonLayout.setHorizontalGroup(
            pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinHoaDonLayout.createSequentialGroup()
                        .addComponent(lblTittleHD, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnAdd)
                        .addGap(58, 58, 58)
                        .addComponent(btnClear)
                        .addGap(0, 554, Short.MAX_VALUE))
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        pnlThongTinHoaDonLayout.setVerticalGroup(
            pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinHoaDonLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(lblTittleHD, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
                        .addComponent(lblMaHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnClear)
                        .addComponent(btnAdd)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 18, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 365, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(46, 46, 46)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 467, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        javax.swing.GroupLayout pnlXuatLayout = new javax.swing.GroupLayout(pnlXuat);
        pnlXuat.setLayout(pnlXuatLayout);
        pnlXuatLayout.setHorizontalGroup(
            pnlXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlXuatLayout.createSequentialGroup()
                .addContainerGap(19, Short.MAX_VALUE)
                .addComponent(pnlThongTinHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(pnlXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(51, 51, 51))
        );
        pnlXuatLayout.setVerticalGroup(
            pnlXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlXuatLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(pnlXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(pnlThongTinHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(37, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlXuat, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        // TODO add your handling code here:
        showDetailsHDCho();
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseClicked
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnClearMouseClicked

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
        // TODO add your handling code here:
        printHD();
    }//GEN-LAST:event_btnPrintMouseClicked

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        // TODO add your handling code here:
        add();
    }//GEN-LAST:event_btnAddMouseClicked

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
            java.util.logging.Logger.getLogger(QuanLyHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyHoaDon().setVisible(true);
            }
        });
    }

    public JPanel getPanelQLHD() {
        return pnlXuat;
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnPrint;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblTittleHD;
    private javax.swing.JPanel pnlThongTinHoaDon;
    private javax.swing.JPanel pnlXuat;
    private javax.swing.JPanel pnlXuatHoaDon;
    private javax.swing.JTable tblCTHD;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTextArea txtArea;
    // End of variables declaration//GEN-END:variables
}
