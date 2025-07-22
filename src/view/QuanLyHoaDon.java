/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.HoaDonDAO;
import Model.ChiTietHoaDon;
import Model.HoaDon;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.print.PrinterException;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
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

    public QuanLyHoaDon() {
        initComponents();
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
        String[] cols = new String[]{"ID_HD", "Ngày Tháng Năm", "Thời Gian", "Tổng tiền", "Ưu đãi"};
        model.setColumnIdentifiers(cols);
        tblHoaDon.setModel(model);

        modelCTHD = new DefaultTableModel();
        String[] colsCTHD = new String[]{"ID_SP", "Tên sản phẩm", "Giá món", "Số Lượng"};
        modelCTHD.setColumnIdentifiers(colsCTHD);
        tblCTHD.setModel(modelCTHD);
    }

    public void fillTable() {
        model.setRowCount(0);
        List<HoaDon> list = hdd.getALL();
        for (HoaDon hdc : list) {
            String trangThai = hdc.getTrangThai();
            if (trangThai.equalsIgnoreCase("Đã thanh toán")) {
                model.addRow(new Object[]{
                    hdc.getID_HD(),
                    hdc.getNgayThangNam(),
                    hdc.getThoiGian(),
                    formatVND(hdc.getTongTien()),
                    hdc.getUuDai()
                });
            }
        }
        TableColumnModel columnModel = tblCTHD.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(50);
        columnModel.getColumn(1).setPreferredWidth(200);
    }

    public void fillTableCTHD() {
        modelCTHD.setRowCount(0);
        int i = tblHoaDon.getSelectedRow();
        if (i < 0) {
            return; // Không có hàng nào được chọn, thoát luôn
        }

        String ID_HD = model.getValueAt(i, 0).toString(); // Lấy ID từ dòng đã chọn trong bảng
        List<ChiTietHoaDon> lstcthd = hdd.getAllID_HD(ID_HD);
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
            List<ChiTietHoaDon> lstcthd = hdd.getAllID_HD(ID_HD);
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
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để thanh toán");
            return;
        }

        String ID_HD = lblMaHD.getText();
        List<HoaDon> listHD = hdd.getALLID_hoadon(ID_HD);
        String thoiGian = listHD.get(0).getThoiGian();
        String ngayThangNam = listHD.get(0).getNgayThangNam();
        String uuDai = listHD.get(0).getUuDai();
        String trangThai = listHD.get(0).getTrangThai();

        List<ChiTietHoaDon> ds = hdd.getAllID_HD(ID_HD);
        float tongTien = 0;
        for (ChiTietHoaDon ct : ds) {
            tongTien += ct.getGiaSP() * ct.getSoLuong();
        }
        float tienUuDai = 0;
        float tienHD = tongTien;
        if (!uuDai.equalsIgnoreCase("") && !uuDai.equals("0%")) {
            String uuDaiPhanTram = uuDai.replace("%", "").trim();
            float phanTram = Float.parseFloat(uuDaiPhanTram);
            tienUuDai = tongTien * (phanTram / 100f);
            tienHD = tongTien - tienUuDai;
        }

        StringBuilder sb = new StringBuilder();

        sb.append("HOÁ ĐƠN THANH TOÁN\n");
        sb.append("_________________________________________\n");
        sb.append("Mã hóa đơn   : ").append(ID_HD).append("\n");
        sb.append("Ngày lập     : ").append(ngayThangNam).append("\n");
        sb.append("Thời gian    : ").append(thoiGian).append("\n\n");
        sb.append("Danh sách món:\n");
        sb.append("_________________________________________\n");
        sb.append(String.format("%-25s %-5s %-15s\n", "Tên món", "SL", "Giá món"));
        sb.append("_________________________________________\n");
        for (ChiTietHoaDon ct : ds) {
            sb.append(String.format("%-25s %-5d %-15s\n", ct.getTenSP(), ct.getSoLuong(), formatVND(ct.getGiaSP())));
        }
        sb.append("_________________________________________\n");
        sb.append("Tổng tiền    : ").append(formatVND(tongTien)).append("\n");
        if (!uuDai.equalsIgnoreCase("") && !uuDai.equals("0%")) {
            sb.append("Ưu đãi       : ").append(uuDai).append(" - ").append(formatVND(tienUuDai)).append("\n");
        } else {
            sb.append("Ưu đãi       : 0% - 0 VND\n");
        }
        sb.append("Thành tiền   : ").append(formatVND(tienHD)).append("\n");
        sb.append("_________________________________________\n");
        sb.append("Cảm ơn quý khách, hẹn gặp lại!");

        txtArea.setText(sb.toString());
        txtArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        txtArea.setEditable(false);
        txtArea.setBackground(Color.white);
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
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlXuatHoaDonLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnPrint)
                .addGap(179, 179, 179))
        );
        pnlXuatHoaDonLayout.setVerticalGroup(
            pnlXuatHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlXuatHoaDonLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 564, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35)
                .addComponent(btnPrint)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlThongTinHoaDonLayout = new javax.swing.GroupLayout(pnlThongTinHoaDon);
        pnlThongTinHoaDon.setLayout(pnlThongTinHoaDonLayout);
        pnlThongTinHoaDonLayout.setHorizontalGroup(
            pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinHoaDonLayout.createSequentialGroup()
                .addContainerGap(21, Short.MAX_VALUE)
                .addGroup(pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(pnlThongTinHoaDonLayout.createSequentialGroup()
                        .addComponent(lblTittleHD, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 143, Short.MAX_VALUE)
                        .addComponent(btnAdd)
                        .addGap(58, 58, 58)
                        .addComponent(btnClear))
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane3))
                .addGap(20, 20, 20))
        );
        pnlThongTinHoaDonLayout.setVerticalGroup(
            pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinHoaDonLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnClear)
                        .addComponent(btnAdd))
                    .addComponent(lblTittleHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(lblMaHD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(18, 30, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 199, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout pnlXuatLayout = new javax.swing.GroupLayout(pnlXuat);
        pnlXuat.setLayout(pnlXuatLayout);
        pnlXuatLayout.setHorizontalGroup(
            pnlXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlXuatLayout.createSequentialGroup()
                .addContainerGap(66, Short.MAX_VALUE)
                .addComponent(pnlThongTinHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(59, 59, 59)
                .addComponent(pnlXuatHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(63, 63, 63))
        );
        pnlXuatLayout.setVerticalGroup(
            pnlXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlXuatLayout.createSequentialGroup()
                .addContainerGap(51, Short.MAX_VALUE)
                .addGroup(pnlXuatLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlThongTinHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlXuatHoaDon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(53, 53, 53))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlXuat, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        add();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseClicked
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnClearMouseClicked

    private void btnPrintMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnPrintMouseClicked
        // TODO add your handling code here:
        printHD();
    }//GEN-LAST:event_btnPrintMouseClicked

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

    public JPanel pnlXuat() {
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
