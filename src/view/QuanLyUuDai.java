/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.UuDaiDAO;
import Model.UuDai;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import javax.swing.JPanel;

/**
 *
 * @author nmttt
 */
public class QuanLyUuDai extends javax.swing.JFrame {

    DefaultTableModel tableModel = new DefaultTableModel();
    UuDaiDAO udDAO = new UuDaiDAO();

    /**
     * Creates new form QuanLyUuDai
     */
    public QuanLyUuDai() {
        initComponents();
        this.setSize(1200, 800);
        this.setLocationRelativeTo(null);     // Canh giữa màn hình   
        // Set layout cho panel chính
        jPanelQLUD.setLayout(new BorderLayout(10, 10)); // 10px gap

        // Căn chỉnh panels
        jPanelQLUD.add(jPanel1, BorderLayout.CENTER); // Panel table ở dưới

        // Set kích thước
        jPanel1.setPreferredSize(new Dimension(240, 350));
        initTable();
        fillTable();
    }

    public void initTable() {
        String[] cols = {"ID ƯU ĐÃI", "GIÁ TRỊ", "ÁP DỤNG VỚI", "NGÀY BẮT ĐẦU", "NGÀY KẾT THÚC", "TRẠNG THÁI"};
        tableModel.setColumnIdentifiers(cols);
        tblBang1.setModel(tableModel);
    }

    public void fillTable() {
        tableModel.setRowCount(0);
        for (UuDai ud : udDAO.getAll()) {
            tableModel.addRow(udDAO.getRow(ud));
        }
    }

    private Date getDateFromComboBox(JComboBox<String> day, JComboBox<String> month, JComboBox<String> year) {
        try {
            String dateStr = year.getSelectedItem() + "-" + month.getSelectedItem() + "-" + day.getSelectedItem();
            return java.sql.Date.valueOf(dateStr); // yyyy-MM-dd
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Ngày không hợp lệ");
            return null;
        }
    }

    public String sinhMaUuDai() {
        List<UuDai> danhSach = udDAO.getAll();
        int max = 0;
        for (UuDai ud : danhSach) {
            try {
                String so = ud.getIdUD().replaceAll("[^0-9]", ""); // Lấy phần số
                int maSo = Integer.parseInt(so);
                if (maSo > max) {
                    max = maSo;
                }
            } catch (Exception e) {
            }
        }
        return String.format("UD%02d", max + 1);
    }

    public void themUuDai() {
        UuDai ud = new UuDai();
        ud.setIdUD(sinhMaUuDai());
        ud.setGiaTri(txtGiatri1.getText());
        ud.setApDungVoi(Float.parseFloat(txtApDung1.getText()));
        ud.setNgayBatDau(getDateFromComboBox(cboNgayStart1, cboThangStart1, cboNamStart1));
        ud.setNgayKetThuc(getDateFromComboBox(cboNgayEnd1, cboThangEnd1, cboNamEnd1));
        if (ud.getNgayBatDau() == null || ud.getNgayKetThuc() == null) {
            return;
        }

        udDAO.them(ud);
        fillTable();
        JOptionPane.showMessageDialog(this, "Thêm thành công");
    }

    public void suaUuDai() {
        int i = tblBang1.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa");
            return;
        }

        UuDai ud = new UuDai();
        ud.setIdUD(lblID.getText());
        ud.setGiaTri(txtGiatri1.getText());
        ud.setApDungVoi(Float.parseFloat(txtApDung1.getText()));
        ud.setNgayBatDau(getDateFromComboBox(cboNgayStart1, cboThangStart1, cboNamStart1));
        ud.setNgayKetThuc(getDateFromComboBox(cboNgayEnd1, cboThangEnd1, cboNamEnd1));
        if (ud.getNgayBatDau() == null || ud.getNgayKetThuc() == null) {
            return;
        }

        udDAO.sua(ud);
        fillTable();
        JOptionPane.showMessageDialog(this, "Sửa thành công");
    }

    public void lamMoiUuDai() {
        lblID.setText("");
        lblTrangThai1.setText("");
        txtGiatri1.setText("");
        txtApDung1.setText("");
        cboNgayStart1.setSelectedIndex(0);
        cboThangStart1.setSelectedIndex(0);
        cboNamStart1.setSelectedIndex(0);
        cboNgayEnd1.setSelectedIndex(0);
        cboThangEnd1.setSelectedIndex(0);
        cboNamEnd1.setSelectedIndex(0);
        tblBang1.clearSelection();
    }

    public void showDetail() {
        int i = tblBang1.getSelectedRow();
        if (i < 0) {
            return;
        }

        UuDai ud = udDAO.getAll().get(i);
        lblID.setText(ud.getIdUD());
        txtGiatri1.setText(ud.getGiaTri());
        txtApDung1.setText(String.valueOf(ud.getApDungVoi()));

        // Ngày bắt đầu
        Calendar calBD = Calendar.getInstance();
        calBD.setTime(ud.getNgayBatDau());
        cboNamStart1.setSelectedItem(String.format("%04d", calBD.get(Calendar.YEAR)));
        cboThangStart1.setSelectedItem(String.format("%02d", calBD.get(Calendar.MONTH) + 1));
        cboNgayStart1.setSelectedItem(String.format("%02d", calBD.get(Calendar.DAY_OF_MONTH)));

        // Ngày kết thúc
        Calendar calKT = Calendar.getInstance();
        calKT.setTime(ud.getNgayKetThuc());
        cboNamEnd1.setSelectedItem(String.format("%04d", calKT.get(Calendar.YEAR)));
        cboThangEnd1.setSelectedItem(String.format("%02d", calKT.get(Calendar.MONTH) + 1));
        cboNgayEnd1.setSelectedItem(String.format("%02d", calKT.get(Calendar.DAY_OF_MONTH)));

        // Tính trạng thái
        String trangThai = ud.getNgayKetThuc().before(new Date()) ? "Hết hạn" : "Còn hạn";
        lblTrangThai1.setText(trangThai);
    }

    public boolean validateUuDai() {
        String giatri = txtGiatri1.getText().trim();
        if (giatri.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá trị ưu đãi không được để trống");
            return false;
        }
        if (giatri.matches(".*[a-zA-Z]+.*")) {
            JOptionPane.showMessageDialog(this, "Giá trị ưu đãi không được chứa chữ cái");
            return false;
        }
        if (giatri.matches(".*[^0-9%\\-].*")) {
            JOptionPane.showMessageDialog(this, "Giá trị ưu đãi không được chứa ký tự đặc biệt");
            return false;
        }
        String soGiatri = giatri.replace("%", "");
        int giaTriSo;
        try {
            giaTriSo = Integer.parseInt(soGiatri);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị ưu đãi không hợp lệ");
            return false;
        }
        if (giaTriSo < 0) {
            JOptionPane.showMessageDialog(this, "Giá trị ưu đãi không được âm");
            return false;
        }
        if (giaTriSo < 5 || giaTriSo > 80) {
            JOptionPane.showMessageDialog(this, "Giá trị ưu đãi phải từ 5% đến 80%");
            return false;
        }
        txtGiatri1.setText(giaTriSo + "%");

        String apdung = txtApDung1.getText().trim();
        if (apdung.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá trị áp dụng với không được để trống");
            return false;
        }
        if (apdung.matches(".*[a-zA-Z]+.*")) {
            JOptionPane.showMessageDialog(this, "Giá trị áp dụng với không được chứa chữ cái");
            return false;
        }
        if (apdung.matches(".*[^0-9].*")) {
            JOptionPane.showMessageDialog(this, "Giá trị áp dụng với không được chứa ký tự đặc biệt");
            return false;
        }
        float apDungSo;
        try {
            apDungSo = Float.parseFloat(apdung);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá trị áp dụng với không hợp lệ");
            return false;
        }
        if (apDungSo <= 0) {
            JOptionPane.showMessageDialog(this, "Giá trị áp dụng với phải lớn hơn 0");
            return false;
        }
        if (apDungSo < 10000 || apDungSo > 5000000) {
            JOptionPane.showMessageDialog(this, "Giá trị áp dụng với phải từ 10.000 VND đến 5.000.000 VND");
            return false;
        }

        return true;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel6 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        btgHan = new javax.swing.ButtonGroup();
        jLabel13 = new javax.swing.JLabel();
        lblID1 = new javax.swing.JLabel();
        jPanelQLUD = new javax.swing.JPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel14 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        txtGiatri1 = new javax.swing.JTextField();
        txtApDung1 = new javax.swing.JTextField();
        jLabel17 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblBang1 = new javax.swing.JTable();
        btnThem1 = new javax.swing.JButton();
        btnSua1 = new javax.swing.JButton();
        btnLamMoi1 = new javax.swing.JButton();
        lblBatDau1 = new javax.swing.JLabel();
        lblKetThuc1 = new javax.swing.JLabel();
        cboNgayStart1 = new javax.swing.JComboBox<>();
        jLabel18 = new javax.swing.JLabel();
        cboThangStart1 = new javax.swing.JComboBox<>();
        jLabel19 = new javax.swing.JLabel();
        cboNamStart1 = new javax.swing.JComboBox<>();
        jLabel20 = new javax.swing.JLabel();
        cboNgayEnd1 = new javax.swing.JComboBox<>();
        cboThangEnd1 = new javax.swing.JComboBox<>();
        cboNamEnd1 = new javax.swing.JComboBox<>();
        jLabel21 = new javax.swing.JLabel();
        jLabel22 = new javax.swing.JLabel();
        jLabel23 = new javax.swing.JLabel();
        lblID = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        lblTrangThai1 = new javax.swing.JLabel();

        jLabel6.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel6.setText("KẾT THÚC");

        jLabel4.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel4.setText("ID ƯU ĐÃI");

        jLabel13.setText("jLabel13");

        lblID1.setFont(new java.awt.Font("Segoe UI Light", 1, 36)); // NOI18N
        lblID1.setForeground(new java.awt.Color(102, 0, 51));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelQLUD.setBackground(new java.awt.Color(234, 232, 232));

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel14.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel14.setText("ID ƯU ĐÃI");

        jLabel16.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel16.setText("GIÁ TRỊ %");

        txtGiatri1.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N

        txtApDung1.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N

        jLabel17.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel17.setText("ÁP DỤNG VỚI");

        tblBang1.setModel(new javax.swing.table.DefaultTableModel(
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
        tblBang1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBang1MouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblBang1);

        btnThem1.setBackground(new java.awt.Color(31, 51, 86));
        btnThem1.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnThem1.setForeground(new java.awt.Color(255, 255, 255));
        btnThem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/Them.png"))); // NOI18N
        btnThem1.setText("THÊM ƯU ĐÃI");
        btnThem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem1ActionPerformed(evt);
            }
        });

        btnSua1.setBackground(new java.awt.Color(31, 51, 86));
        btnSua1.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnSua1.setForeground(new java.awt.Color(255, 255, 255));
        btnSua1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/Sua.png"))); // NOI18N
        btnSua1.setText("SỬA ƯU ĐÃI");
        btnSua1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSua1ActionPerformed(evt);
            }
        });

        btnLamMoi1.setBackground(new java.awt.Color(31, 51, 86));
        btnLamMoi1.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnLamMoi1.setForeground(new java.awt.Color(255, 255, 255));
        btnLamMoi1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/lamMoi.png"))); // NOI18N
        btnLamMoi1.setText("LÀM MỚI ");
        btnLamMoi1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoi1ActionPerformed(evt);
            }
        });

        lblBatDau1.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        lblBatDau1.setText("BẮT ĐẦU");

        lblKetThuc1.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        lblKetThuc1.setText("KẾT THÚC");

        cboNgayStart1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jLabel18.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel18.setText("THÁNG");

        cboThangStart1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        jLabel19.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel19.setText("NGÀY");

        cboNamStart1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2026" }));

        jLabel20.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel20.setText("NĂM");

        cboNgayEnd1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        cboThangEnd1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", " " }));

        cboNamEnd1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2026" }));

        jLabel21.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel21.setText("NGÀY");

        jLabel22.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel22.setText("THÁNG");

        jLabel23.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel23.setText("NĂM");

        lblID.setFont(new java.awt.Font("Segoe UI Light", 1, 36)); // NOI18N
        lblID.setForeground(new java.awt.Color(102, 0, 51));

        jLabel24.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel24.setText("TRẠNG THÁI ");

        lblTrangThai1.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        lblTrangThai1.setForeground(new java.awt.Color(102, 0, 51));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(55, 55, 55)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(27, 27, 27)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel17)
                                    .addComponent(jLabel16)
                                    .addComponent(lblBatDau1)
                                    .addComponent(lblKetThuc1)
                                    .addComponent(jLabel14))
                                .addGap(127, 127, 127)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblID, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtGiatri1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txtApDung1, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cboNgayEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel21))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel22)
                                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                                        .addComponent(cboThangEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(18, 18, 18)
                                                        .addComponent(cboNamEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addGap(43, 43, 43)
                                                        .addComponent(jLabel24))))
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cboNgayStart1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel19))
                                                .addGap(18, 18, 18)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(cboThangStart1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel18))
                                                .addGap(20, 20, 20)
                                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel20)
                                                    .addComponent(cboNamStart1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel23))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblTrangThai1, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(btnThem1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(141, 141, 141)
                                .addComponent(btnSua1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(138, 138, 138)
                                .addComponent(btnLamMoi1, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1070, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(111, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel14)
                    .addComponent(lblID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiatri1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel16))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApDung1, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel18)
                    .addComponent(jLabel19)
                    .addComponent(jLabel20))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBatDau1)
                    .addComponent(cboNgayStart1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboThangStart1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNamStart1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel21)
                            .addComponent(jLabel22)
                            .addComponent(jLabel23))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblKetThuc1)
                            .addComponent(cboNgayEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboThangEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboNamEnd1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel24)))
                    .addComponent(lblTrangThai1, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(63, 63, 63)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem1)
                    .addComponent(btnLamMoi1)
                    .addComponent(btnSua1))
                .addGap(68, 68, 68)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(50, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelQLUDLayout = new javax.swing.GroupLayout(jPanelQLUD);
        jPanelQLUD.setLayout(jPanelQLUDLayout);
        jPanelQLUDLayout.setHorizontalGroup(
            jPanelQLUDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQLUDLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelQLUDLayout.setVerticalGroup(
            jPanelQLUDLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQLUDLayout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(73, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelQLUD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelQLUD, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblBang1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBang1MouseClicked
        // TODO add your handling code here:

        showDetail();
    }//GEN-LAST:event_tblBang1MouseClicked

    private void btnThem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem1ActionPerformed
        // TODO add your handling code here:
        if (!validateUuDai()) {
            return;
        }
        themUuDai();
    }//GEN-LAST:event_btnThem1ActionPerformed

    private void btnSua1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSua1ActionPerformed
        // TODO add your handling code here:
        if (!validateUuDai()) {
            return;
        }
        suaUuDai();
    }//GEN-LAST:event_btnSua1ActionPerformed

    private void btnLamMoi1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoi1ActionPerformed
        // TODO add your handling code here:
        lamMoiUuDai();
    }//GEN-LAST:event_btnLamMoi1ActionPerformed

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
            java.util.logging.Logger.getLogger(QuanLyUuDai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyUuDai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyUuDai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyUuDai.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyUuDai().setVisible(true);
            }
        });
    }

    public JPanel getMainPanel() {
        return jPanelQLUD; // Hoặc tên panel chính trong QLUD
    }

    public JPanel getJPanelQLUD() {
        return jPanelQLUD;
    }

    public JPanel getJPanel1() {
        return jPanel1; // Nếu có panel1
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgHan;
    private javax.swing.JButton btnLamMoi1;
    private javax.swing.JButton btnSua1;
    private javax.swing.JButton btnThem1;
    private javax.swing.JComboBox<String> cboNamEnd1;
    private javax.swing.JComboBox<String> cboNamStart1;
    private javax.swing.JComboBox<String> cboNgayEnd1;
    private javax.swing.JComboBox<String> cboNgayStart1;
    private javax.swing.JComboBox<String> cboThangEnd1;
    private javax.swing.JComboBox<String> cboThangStart1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanelQLUD;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblBatDau1;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblID1;
    private javax.swing.JLabel lblKetThuc1;
    private javax.swing.JLabel lblTrangThai1;
    private javax.swing.JTable tblBang1;
    private javax.swing.JTextField txtApDung1;
    private javax.swing.JTextField txtGiatri1;
    // End of variables declaration//GEN-END:variables
}
