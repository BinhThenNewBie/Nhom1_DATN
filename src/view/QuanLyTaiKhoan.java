/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.TaikhoanDAO;
import Model.Taikhoan;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class QuanLyTaiKhoan extends javax.swing.JFrame {
    DefaultTableModel tableModel= new DefaultTableModel();
    TaikhoanDAO tkd = new TaikhoanDAO();
    /**
     * Creates new form QuanLyTaiKhoan
     */
    public QuanLyTaiKhoan() {
        initComponents();
        initTable();
        fillTable();
    }
    public void initTable(){
    tableModel = new DefaultTableModel();
    String[] cols = new String[]{"ID tài khoản", "Password","Email", "Vai trò","Trạng Thái"};
    tableModel.setColumnIdentifiers(cols);
    tblTaikhoan.setModel(tableModel);
}
public void fillTable(){
    tableModel.setRowCount(0);
    for (Taikhoan tk : tkd.GETALL()) {
        Object[] rows = tkd.GETROW(tk);
        tableModel.addRow(rows);
    }
}
public void showdetail(){
    int chon = tblTaikhoan.getSelectedRow();
    if(chon >= 0){
        Taikhoan tk = tkd.GETALL().get(chon);
        txtIdnv.setText(tk.getID_TK());
        txtPass.setText(tk.getPass());
        txtEmail.setText(tk.getEmail());
        cboVaitro.setSelectedItem(tk.getVaiTro());
        
        String trangThai = tk.getTrangThai();
        String vaiTro = tk.getVaiTro();
        
        // Logic hiển thị button dựa trên trạng thái
        if("LOCKED".equalsIgnoreCase(trangThai)){
            btnKhoa.setEnabled(false);
            btnMokhoa.setEnabled(true);
            btnSua.setEnabled(false);
            txtEmail.setEnabled(false);
            txtIdnv.setEnabled(false);
            txtPass.setEnabled(false);
            cboVaitro.setEnabled(false);
            btnLammoi.setEnabled(false);
        } else {
            btnKhoa.setEnabled(true);
            btnMokhoa.setEnabled(false);
            btnSua.setEnabled(true);
            btnLammoi.setEnabled(true);
            
            // Nếu là STAFF, chỉ cho phép sửa email 
            if("STAFF".equalsIgnoreCase(vaiTro)){
                txtEmail.setEnabled(true);
                txtIdnv.setEnabled(false);
                txtPass.setEnabled(false);  
                cboVaitro.setEnabled(false);
            } else {
                // Nếu là ADMIN, cho phép sửa tất cả
                txtEmail.setEnabled(true);
                txtIdnv.setEnabled(true);
                txtPass.setEnabled(true);
                cboVaitro.setEnabled(true);
            }
        }
    }
}

public void lammoi(){
    txtEmail.setText(null);
    txtIdnv.setText(null);
    txtPass.setText(null);
}

public void sua(){
    int chon = tblTaikhoan.getSelectedRow();
    if(chon >= 0){
        Taikhoan chontk = tkd.GETALL().get(chon);
        
        String vaiTroHienTai = chontk.getVaiTro();
        
        int sua = JOptionPane.showConfirmDialog(this, "Bạn muốn sửa không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if(sua == JOptionPane.YES_OPTION){
            String Email = txtEmail.getText().trim();
            
            // Validation dữ liệu
            if(Email.isEmpty()){
                JOptionPane.showMessageDialog(this, "Vui lòng nhập email!");
                return;
            }
            
            int result = 0;
            
            // Nếu là STAFF, chỉ sửa email
            if("STAFF".equalsIgnoreCase(vaiTroHienTai)){
                result = tkd.suaStaff(chontk.getID_TK(), Email);
            } else {
                // Nếu là ADMIN, sửa tất cả
                String IDTK = txtIdnv.getText().trim();
                String Pass = txtPass.getText().trim();
                String vaiTro = cboVaitro.getSelectedItem().toString(); // Lấy vai trò từ combobox
                
                if(IDTK.isEmpty() || Pass.isEmpty()){
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }
                
                String trangThai = chontk.getTrangThai();
                Taikhoan tk = new Taikhoan(IDTK, Pass, Email, vaiTro, trangThai);
                result = tkd.sua(chontk.getID_TK(), tk);
            }
            
            if(result == 1){
                fillTable();
                JOptionPane.showMessageDialog(this, "Sửa thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi sửa!");
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần sửa!");
    }
}
public void moKhoaTaiKhoan(){
    int chon = tblTaikhoan.getSelectedRow();
    if(chon >= 0){
        Taikhoan chontk = tkd.GETALL().get(chon);
        
        // Kiểm tra trạng thái hiện tại
        if("ACTIVE".equalsIgnoreCase(chontk.getTrangThai())){
            JOptionPane.showMessageDialog(this, "Tài khoản đang hoạt động, không cần mở khóa!");
            return;
        }
        
        int xacNhan = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc muốn mở khóa tài khoản: " + chontk.getID_TK() + "?", 
            "Xác nhận mở khóa", JOptionPane.YES_NO_OPTION);
            
        if(xacNhan == JOptionPane.YES_OPTION){
            int result = tkd.moKhoaTaiKhoan(chontk.getID_TK());
            if(result == 1){
                fillTable();
                showdetail(); // Refresh button states
                JOptionPane.showMessageDialog(this, "Mở khóa tài khoản thành công!");
            } else {
                JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi mở khóa tài khoản!");
            }
        }
    } else {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần mở khóa!");
    }
}
  // Hàm khóa tài khoản
    public void khoaTaiKhoan(){
        int chon = tblTaikhoan.getSelectedRow();
        if(chon >= 0){
            Taikhoan chontk = tkd.GETALL().get(chon);
            
            // Kiểm tra nếu tài khoản đã bị khóa
            if("LOCKED".equals(chontk.getTrangThai())){
                JOptionPane.showMessageDialog(this, "Tài khoản đã bị khóa!");
                return;
            }
            if ("ADMIN".equals(chontk.getVaiTro())) {
            JOptionPane.showMessageDialog(this, "Tài khoản admin không khóa được");
            return;
}
            
            int xacNhan = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc muốn khóa tài khoản: " + chontk.getID_TK() + "?", 
                "Xác nhận khóa", JOptionPane.YES_NO_OPTION);
                
            if(xacNhan == JOptionPane.YES_OPTION){
                int result = tkd.khoaTaiKhoan(chontk.getID_TK());
                if(result == 1){
                    fillTable();
                    JOptionPane.showMessageDialog(this, "Khóa tài khoản thành công!");
                } else {
                    JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi khóa tài khoản!");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn tài khoản cần khóa!");
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

        jSeparator1 = new javax.swing.JSeparator();
        btnSua = new javax.swing.JButton();
        btnKhoa = new javax.swing.JButton();
        btnLammoi = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtIdnv = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTaikhoan = new javax.swing.JTable();
        txtEmail = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        cboVaitro = new javax.swing.JComboBox<>();
        txtPass = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        btnMokhoa = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnSua.setBackground(new java.awt.Color(31, 51, 86));
        btnSua.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/Sua.png"))); // NOI18N
        btnSua.setText("SỬA TÀI KHOẢN");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnKhoa.setBackground(new java.awt.Color(31, 51, 86));
        btnKhoa.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnKhoa.setForeground(new java.awt.Color(255, 255, 255));
        btnKhoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/Xoa.png"))); // NOI18N
        btnKhoa.setText("KHÓA TÀI KHOẢN");
        btnKhoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhoaActionPerformed(evt);
            }
        });

        btnLammoi.setBackground(new java.awt.Color(31, 51, 86));
        btnLammoi.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnLammoi.setForeground(new java.awt.Color(255, 255, 255));
        btnLammoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/lamMoi.png"))); // NOI18N
        btnLammoi.setText("LÀM MỚI ");
        btnLammoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLammoiActionPerformed(evt);
            }
        });

        jLabel1.setText("ID_NV");

        jLabel2.setText("PASSWORD");

        tblTaikhoan.setModel(new javax.swing.table.DefaultTableModel(
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
        tblTaikhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblTaikhoanMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblTaikhoan);

        jLabel3.setText("VAI TRÒ");

        cboVaitro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "STAFF", "TẤT CẢ" }));

        jLabel4.setText("EMAIL");

        btnMokhoa.setBackground(new java.awt.Color(31, 51, 86));
        btnMokhoa.setFont(new java.awt.Font("Segoe UI Light", 1, 13)); // NOI18N
        btnMokhoa.setForeground(new java.awt.Color(255, 255, 255));
        btnMokhoa.setText("MỞ KHÓA");
        btnMokhoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMokhoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(145, 145, 145)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addGap(34, 34, 34)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(txtEmail, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                        .addComponent(cboVaitro, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(txtPass, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE))
                    .addComponent(txtIdnv, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 81, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnKhoa)
                    .addComponent(btnLammoi, javax.swing.GroupLayout.PREFERRED_SIZE, 185, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(64, 64, 64))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnMokhoa)
                .addGap(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGap(76, 76, 76)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtIdnv, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1))
                        .addGap(39, 39, 39)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(38, 38, 38))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnSua)
                        .addGap(39, 39, 39)
                        .addComponent(btnKhoa)
                        .addGap(5, 5, 5)))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnLammoi)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cboVaitro, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(45, 45, 45)
                .addComponent(btnMokhoa)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 239, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(21, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        sua();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnMokhoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMokhoaActionPerformed
        // TODO add your handling code here:
        moKhoaTaiKhoan();
    }//GEN-LAST:event_btnMokhoaActionPerformed

    private void tblTaikhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblTaikhoanMouseClicked
        // TODO add your handling code here:
        showdetail();
    }//GEN-LAST:event_tblTaikhoanMouseClicked

    private void btnLammoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLammoiActionPerformed
        // TODO add your handling code here:
        lammoi();
    }//GEN-LAST:event_btnLammoiActionPerformed

    private void btnKhoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoaActionPerformed
        // TODO add your handling code here:
        khoaTaiKhoan();
    }//GEN-LAST:event_btnKhoaActionPerformed

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
            java.util.logging.Logger.getLogger(QuanLyTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyTaiKhoan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyTaiKhoan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnKhoa;
    private javax.swing.JButton btnLammoi;
    private javax.swing.JButton btnMokhoa;
    private javax.swing.JButton btnSua;
    private javax.swing.JComboBox<String> cboVaitro;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTable tblTaikhoan;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtIdnv;
    private javax.swing.JTextField txtPass;
    // End of variables declaration//GEN-END:variables
}
