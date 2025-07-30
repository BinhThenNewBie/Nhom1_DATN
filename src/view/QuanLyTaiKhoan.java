/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.TaikhoanDAO;
import Model.Taikhoan;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author ADMIN
 */
public class QuanLyTaiKhoan extends javax.swing.JFrame {

    DefaultTableModel tableModel = new DefaultTableModel();
    TaikhoanDAO tkd = new TaikhoanDAO();

    /**
     * Creates new form QuanLyTaiKhoan
     */
    public QuanLyTaiKhoan() {
        initComponents();
        // Căn chỉnh panels
        jPanelQLTK.add(jPanel1, BorderLayout.NORTH);  // Panel form ở trên
        // Set kích thước
        jPanelQLTK.setPreferredSize(new Dimension(1240, 150)); // Panel dưới cao 630px
        // Đổi màu nền bảng
        tblBang.setBackground(new Color(230, 230, 230)); // màu nền bảng

        // Đổi màu viền của tiêu đề cột
        tblBang.getTableHeader().setBackground(new Color(31, 51, 86)); // màu nền xanh đậm
        tblBang.getTableHeader().setForeground(Color.BLACK);           // màu chữ trắng
        tblBang.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16)); // font đậm

        // Đổi màu hàng được chọn
        tblBang.setSelectionBackground(new Color(60, 120, 200)); // màu nền khi chọn
        tblBang.setSelectionForeground(Color.WHITE);             // chữ khi chọn

        // Đổi màu đường lưới (nếu có)
        tblBang.setGridColor(Color.GRAY);

        // Đổi màu chữ trong bảng
        tblBang.setForeground(Color.BLACK); // màu chữ
        tblBang.setFont(new Font("Segoe UI", Font.PLAIN, 12)); // font chữ

        // Đặt độ cao hàng
        tblBang.setRowHeight(30);
        initTable();
        fillTable();
        checkEmailTrung();
    }

    public void initTable() {
        tableModel = new DefaultTableModel();
        String[] cols = new String[]{"ID TÀI KHOẢN", "ID NHÂN VIÊN","TÊN NHÂN VIÊN", "MẬT KHẨU","EMAIL", "VAI TRÒ", "TRẠNG THÁI"};
        tableModel.setColumnIdentifiers(cols);
        tblBang.setModel(tableModel);
    }

    public void fillTable() {
        tableModel.setRowCount(0);
        for (Taikhoan tk : tkd.GETALL()) {
            Object[] rows = tkd.GETROW(tk);
            tableModel.addRow(rows);
        }
    }

    public void showdetail() {
    int chon = tblBang.getSelectedRow();
    if (chon >= 0) {
        Taikhoan tk = tkd.GETALL().get(chon);
        
        txtID.setText(tk.getID_TK());        
        txtIdnv.setText(tk.getID_NV());    
        txtTentk.setText(tk.getTenNV());    
        txtPass.setText(tk.getPass());   
        txtEmail.setText(tk.getEmail());    
        cboVaitro.setSelectedItem(tk.getVaiTro());

        String trangThai = tk.getTrangThai();
        String vaiTro = tk.getVaiTro();

        if ("LOCKED".equalsIgnoreCase(trangThai)) {
            btnKhoa.setEnabled(false);
            btnMokhoa.setEnabled(true);
            btnThem.setEnabled(false);
            btnSua.setEnabled(false);
            btnLamMoi.setEnabled(false);
            txtPass.setEnabled(false);
            txtID.setEnabled(false);
            txtTentk.setEnabled(false);
            txtIdnv.setEnabled(false);
            btnLamMoi.setEnabled(false);
        } else {
            btnKhoa.setEnabled(true);
            btnMokhoa.setEnabled(false);
            btnThem.setEnabled(true);
            btnSua.setEnabled(true);
            btnLamMoi.setEnabled(true);
            txtPass.setEnabled(true);
            txtID.setEnabled(true);
            txtIdnv.setEnabled(true);
            txtTentk.setEnabled(true);
            txtEmail.setEnabled(true);
        }
        }
    }


    public void lammoi() {
        txtID.setText("");
        txtPass.setText("");
        txtIdnv.setText("");
        cboVaitro.setSelectedItem(0);
    }
public void them(){
        String ID_TK = txtTentk.getText().trim();
        String ID_NV = txtIdnv.getText().trim();
        String hoTen = txtTentk.getText().trim();
        String pass = txtPass.getText().trim();
        String email = txtEmail.getText().trim();
        String vaiTro = (String) cboVaitro.getSelectedItem();
        
        //Kiểm tra tên
        if (!hoTen.matches("^[\\p{L}\\s]+$")) {
        JOptionPane.showMessageDialog(this, "Tên phải nhập bằng chữ", "Lỗi", JOptionPane.ERROR_MESSAGE);
        return;
    }
        // Kiểm tra dữ liệu đầu vào
        if (ID_NV.isEmpty() || hoTen.isEmpty() || ID_TK.isEmpty()|| pass.isEmpty()||email.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // kiểm tra định dạng email
        if (!email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                    JOptionPane.showMessageDialog(this, "Email không đúng định dạng!");
                    return;
                }
        for (Taikhoan x: tkd.GETALL()) {
            if(x.getEmail() != null && x.getEmail().equalsIgnoreCase(email)&& !x.getID_NV().equals(ID_NV)){
                JOptionPane.showMessageDialog(this,"EMail này đã được dùng bởi nhân viên có mã: "+x.getID_NV(),
                        "Lỗi email trùng",JOptionPane.ERROR_MESSAGE );
                txtEmail.requestFocus();
                return;
            }
        }

        Taikhoan nv = new Taikhoan(ID_TK, ID_NV, ID_NV, pass, email, vaiTro, vaiTro);
        nv.setTrangThai("ACTIVE"); // Set trạng thái mặc định

        int result = tkd.them(nv);
        if (result == 1) {
            fillTable();
            JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Có lỗi xảy ra khi thêm nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
}
    public void sua() {
    int chon = tblBang.getSelectedRow();
    if (chon >= 0) {
        Taikhoan chontk = tkd.GETALL().get(chon);

        String vaiTroHienTai = chontk.getVaiTro();

        int sua = JOptionPane.showConfirmDialog(this, "Bạn muốn sửa không?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (sua == JOptionPane.YES_OPTION) {
            String Email = txtEmail.getText().trim(); 

            // Validation dữ liệu
            if (Email.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập email!");
                return;
            }
            // Kiểm tra định dạng email
            if (!Email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
                JOptionPane.showMessageDialog(this, "Email không đúng định dạng!");
                return;
            }

            int result = 0;

            // Nếu là STAFF, chỉ sửa email
            if ("STAFF".equalsIgnoreCase(vaiTroHienTai)) {
                result = tkd.suaStaff(chontk.getID_TK(), Email);
            } else {
                // Nếu là ADMIN, sửa tất cả
                String IDTK = txtID.getText().trim();
                String IDNV = txtIdnv.getText().trim();
                String tenTK = txtTentk.getText().trim();
                String Pass = txtPass.getText().trim(); // SỬA: lấy từ txtPass
                String vaiTro = cboVaitro.getSelectedItem().toString(); // Lấy vai trò từ combobox

                if (IDTK.isEmpty() || Pass.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin!");
                    return;
                }

                String trangThai = chontk.getTrangThai();
                Taikhoan tk = new Taikhoan(IDTK, IDNV, tenTK, Pass, Email, vaiTro, trangThai);
                result = tkd.sua(chontk.getID_TK(), tk);
            }

            if (result == 1) {
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

    public void moKhoaTaiKhoan() {
        int chon = tblBang.getSelectedRow();
        if (chon >= 0) {
            Taikhoan chontk = tkd.GETALL().get(chon);

            // Kiểm tra trạng thái hiện tại
            if ("ACTIVE".equalsIgnoreCase(chontk.getTrangThai())) {
                JOptionPane.showMessageDialog(this, "Tài khoản đang hoạt động, không cần mở khóa!");
                return;
            }

            int xacNhan = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn mở khóa tài khoản: " + chontk.getID_TK() + "?",
                    "Xác nhận mở khóa", JOptionPane.YES_NO_OPTION);

            if (xacNhan == JOptionPane.YES_OPTION) {
                int result = tkd.moKhoaTaiKhoan(chontk.getID_TK());
                if (result == 1) {
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

    public void khoaTaiKhoan() {
        int chon = tblBang.getSelectedRow();
        if (chon >= 0) {
            Taikhoan chontk = tkd.GETALL().get(chon);

            // Kiểm tra nếu tài khoản đã bị khóa
            if ("LOCKED".equals(chontk.getTrangThai())) {
                JOptionPane.showMessageDialog(this, "Tài khoản đã bị khóa!");
                return;
            }

            int xacNhan = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc muốn khóa tài khoản: " + chontk.getID_TK() + "?",
                    "Xác nhận khóa", JOptionPane.YES_NO_OPTION);

            if (xacNhan == JOptionPane.YES_OPTION) {
                int result = tkd.khoaTaiKhoan(chontk.getID_TK());
                if (result == 1) {
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

    public void checkEmailTrung() {
    List<Taikhoan> danhSach = tkd.GETALL();
    Map<String, Integer> demEmail = new HashMap<>();

    // Đếm số lần xuất hiện của mỗi email
    for (Taikhoan tk : danhSach) {
        String email = tk.getEmail();
        if (email != null && !email.isBlank()) {
            email = email.trim().toLowerCase(); // Chuẩn hóa email
            demEmail.put(email, demEmail.getOrDefault(email, 0) + 1);
        }
    }

    // Kiểm tra có email nào trùng không
    for (Map.Entry<String, Integer> entry : demEmail.entrySet()) {
        if (entry.getValue() > 1) {
            // Có email bị trùng
            String emailTrung = entry.getKey();
            
            // Vô hiệu hóa các thành phần
            btnThem.setEnabled(false);
            txtTentk.setEnabled(false);
            txtID.setEnabled(false);
            txtPass.setEnabled(false);
            txtIdnv.setEnabled(false);
            btnKhoa.setEnabled(false);
            btnMokhoa.setEnabled(false);
            btnLamMoi.setEnabled(false);
            
            txtEmail.setText(emailTrung); // Gợi ý email bị trùng

            JOptionPane.showMessageDialog(this,
                    "Phát hiện email bị trùng: " + emailTrung
                    + "\nVui lòng sửa lại để tiếp tục sử dụng!",
                    "Trùng Email", JOptionPane.WARNING_MESSAGE);
            return;
        }
    }

    // Nếu không có email trùng => bật lại các thành phần
        btnThem.setEnabled(true);
        txtTentk.setEnabled(true);
        txtIdnv.setEnabled(true);
        txtID.setEnabled(true);
        txtPass.setEnabled(true);
        txtEmail.setEnabled(true);
        btnKhoa.setEnabled(true);
        btnMokhoa.setEnabled(true);
        btnLamMoi.setEnabled(true);
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
        jPanelQLTK = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtID = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        btnSua = new javax.swing.JButton();
        btnKhoa = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        lblBatDau1 = new javax.swing.JLabel();
        btnMokhoa = new javax.swing.JButton();
        txtIdnv = new javax.swing.JTextField();
        txtPass = new javax.swing.JTextField();
        cboVaitro = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBang = new javax.swing.JTable();
        btnThem = new javax.swing.JButton();
        jLabel18 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        txtTentk = new javax.swing.JTextField();
        txtEmail = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel14.setText("ID TÀI KHOẢN");

        jLabel16.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel16.setText("PASSWORD");

        txtID.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel17.setText("EMAIL");

        btnSua.setBackground(new java.awt.Color(31, 51, 86));
        btnSua.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/Them.png"))); // NOI18N
        btnSua.setText("SỬA TÀI KHOẢN");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnKhoa.setBackground(new java.awt.Color(31, 51, 86));
        btnKhoa.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnKhoa.setForeground(new java.awt.Color(255, 255, 255));
        btnKhoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/Sua.png"))); // NOI18N
        btnKhoa.setText("KHÓA TÀI KHOẢN");
        btnKhoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKhoaActionPerformed(evt);
            }
        });

        btnLamMoi.setBackground(new java.awt.Color(31, 51, 86));
        btnLamMoi.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnLamMoi.setForeground(new java.awt.Color(255, 255, 255));
        btnLamMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/lamMoi.png"))); // NOI18N
        btnLamMoi.setText("LÀM MỚI ");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        lblBatDau1.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        lblBatDau1.setText("VAI TRÒ");

        btnMokhoa.setBackground(new java.awt.Color(31, 51, 86));
        btnMokhoa.setFont(new java.awt.Font("Segoe UI Light", 1, 13)); // NOI18N
        btnMokhoa.setForeground(new java.awt.Color(255, 255, 255));
        btnMokhoa.setText("MỞ KHÓA");
        btnMokhoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMokhoaActionPerformed(evt);
            }
        });

        txtIdnv.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N

        txtPass.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N

        cboVaitro.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        cboVaitro.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "ADMIN", "STAFF" }));

        tblBang.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBang);

        btnThem.setBackground(new java.awt.Color(31, 51, 86));
        btnThem.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/Them.png"))); // NOI18N
        btnThem.setText("THÊM TÀI KHOẢN");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        jLabel18.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel18.setText("TÊN TÀI KHOẢN");

        jLabel19.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel19.setText("ID NHÂN VIÊN");

        txtTentk.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N

        txtEmail.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(82, 82, 82)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnMokhoa)
                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(40, 40, 40)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(jLabel14)
                                                .addComponent(jLabel19)))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                            .addGap(26, 26, 26)
                                            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(jLabel16)
                                                .addComponent(jLabel18)
                                                .addComponent(jLabel17)
                                                .addComponent(lblBatDau1))))
                                    .addGap(59, 59, 59)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(txtIdnv, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                            .addComponent(txtTentk, javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(txtPass, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE)
                                            .addComponent(txtEmail, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 445, Short.MAX_VALUE))
                                        .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGroup(jPanel1Layout.createSequentialGroup()
                                    .addGap(190, 190, 190)
                                    .addComponent(cboVaitro, javax.swing.GroupLayout.PREFERRED_SIZE, 445, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1377, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(93, 93, 93)
                        .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(129, 129, 129)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(133, 133, 133)
                        .addComponent(btnKhoa, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(134, 134, 134)
                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(380, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel14))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtIdnv, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTentk, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtPass, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addGap(45, 45, 45))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(txtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel17)))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBatDau1)
                    .addComponent(cboVaitro, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(33, 33, 33)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSua)
                    .addComponent(btnThem)
                    .addComponent(btnKhoa)
                    .addComponent(btnLamMoi))
                .addGap(38, 38, 38)
                .addComponent(btnMokhoa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 363, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(22, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelQLTKLayout = new javax.swing.GroupLayout(jPanelQLTK);
        jPanelQLTK.setLayout(jPanelQLTKLayout);
        jPanelQLTKLayout.setHorizontalGroup(
            jPanelQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQLTKLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        jPanelQLTKLayout.setVerticalGroup(
            jPanelQLTKLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelQLTKLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelQLTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 1145, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanelQLTK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 75, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        sua();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnKhoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKhoaActionPerformed
        // TODO add your handling code here:
        khoaTaiKhoan();
    }//GEN-LAST:event_btnKhoaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        lammoi();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void tblBangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangMouseClicked
        // TODO add your handling code here:
        showdetail();
    }//GEN-LAST:event_tblBangMouseClicked

    private void btnMokhoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMokhoaActionPerformed
        // TODO add your handling code here:
        moKhoaTaiKhoan();
    }//GEN-LAST:event_btnMokhoaActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        them();
    }//GEN-LAST:event_btnThemActionPerformed
    public JPanel getMainPanel() {
        return jPanel1;
    }

    public JPanel getJPanelQLTK() {
        return jPanel1;
    }

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
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnMokhoa;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JComboBox<String> cboVaitro;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelQLTK;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel lblBatDau1;
    private javax.swing.JTable tblBang;
    private javax.swing.JTextField txtEmail;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtIdnv;
    private javax.swing.JTextField txtPass;
    private javax.swing.JTextField txtTentk;
    // End of variables declaration//GEN-END:variables
}
