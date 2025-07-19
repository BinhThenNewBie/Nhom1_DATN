/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.UuDaiDAO;
import Model.UuDai;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

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
        this.setSize(1000, 950);               // Đặt kích thước cửa sổ
        this.setResizable(false);             // Không cho resize (tuỳ chọn)
        this.setLocationRelativeTo(null);     // Canh giữa màn hình              initTable();
        fillTable();
    }

    public void initTable() {
        String[] cols = {"ID ƯU ĐÃI", "GIÁ TRỊ", "ÁP DỤNG VỚI", "NGÀY BẮT ĐẦU", "NGÀY KẾT THÚC", "TRẠNG THÁI"};
        tableModel.setColumnIdentifiers(cols);
        tblBang.setModel(tableModel);
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
        ud.setGiaTri(txtGiatri.getText());
        ud.setApDungVoi(Float.parseFloat(txtApDung.getText()));
        ud.setNgayBatDau(getDateFromComboBox(cboNgayStart, cboThangStart, cboNamStart));
        ud.setNgayKetThuc(getDateFromComboBox(cboNgayEnd, cboThangEnd, cboNamEnd));
        if (ud.getNgayBatDau() == null || ud.getNgayKetThuc() == null) {
            return;
        }

        udDAO.them(ud);
        fillTable();
        JOptionPane.showMessageDialog(this, "Thêm thành công");
    }

    public void suaUuDai() {
        int i = tblBang.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn dòng cần sửa");
            return;
        }

        UuDai ud = new UuDai();
        ud.setIdUD(lblID.getText());
        ud.setGiaTri(txtGiatri.getText());
        ud.setApDungVoi(Float.parseFloat(txtApDung.getText()));
        ud.setNgayBatDau(getDateFromComboBox(cboNgayStart, cboThangStart, cboNamStart));
        ud.setNgayKetThuc(getDateFromComboBox(cboNgayEnd, cboThangEnd, cboNamEnd));
        if (ud.getNgayBatDau() == null || ud.getNgayKetThuc() == null) {
            return;
        }

        udDAO.sua(ud);
        fillTable();
        JOptionPane.showMessageDialog(this, "Sửa thành công");
    }

    public void lamMoiUuDai() {
        lblID.setText("");
        lblTrangThai.setText("");
        txtGiatri.setText("");
        txtApDung.setText("");
        cboNgayStart.setSelectedIndex(0);
        cboThangStart.setSelectedIndex(0);
        cboNamStart.setSelectedIndex(0);
        cboNgayEnd.setSelectedIndex(0);
        cboThangEnd.setSelectedIndex(0);
        cboNamEnd.setSelectedIndex(0);
        tblBang.clearSelection();
    }

    public void showDetail() {
        int i = tblBang.getSelectedRow();
        if (i < 0) {
            return;
        }

        UuDai ud = udDAO.getAll().get(i);
        lblID.setText(ud.getIdUD());
        txtGiatri.setText(ud.getGiaTri());
        txtApDung.setText(String.valueOf(ud.getApDungVoi()));

        // Ngày bắt đầu
        Calendar calBD = Calendar.getInstance();
        calBD.setTime(ud.getNgayBatDau());
        cboNamStart.setSelectedItem(String.format("%04d", calBD.get(Calendar.YEAR)));
        cboThangStart.setSelectedItem(String.format("%02d", calBD.get(Calendar.MONTH) + 1));
        cboNgayStart.setSelectedItem(String.format("%02d", calBD.get(Calendar.DAY_OF_MONTH)));

        // Ngày kết thúc
        Calendar calKT = Calendar.getInstance();
        calKT.setTime(ud.getNgayKetThuc());
        cboNamEnd.setSelectedItem(String.format("%04d", calKT.get(Calendar.YEAR)));
        cboThangEnd.setSelectedItem(String.format("%02d", calKT.get(Calendar.MONTH) + 1));
        cboNgayEnd.setSelectedItem(String.format("%02d", calKT.get(Calendar.DAY_OF_MONTH)));

        // Tính trạng thái
        String trangThai = ud.getNgayKetThuc().before(new Date()) ? "Hết hạn" : "Còn hạn";
        lblTrangThai.setText(trangThai);
    }

    private boolean validateUuDai() {
        String giatri = txtGiatri.getText().trim();
        String apdung = txtApDung.getText().trim();

        if (giatri.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá trị ưu đãi");
            return false;
        }
        if (apdung.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá trị 'áp dụng với'");
            return false;
        }

        if (!giatri.matches("^\\d+%$")) {
            JOptionPane.showMessageDialog(this, "Giá trị ưu đãi phải là số nguyên dương và có dấu % ");
            return false;
        }

        int giaTriSo = Integer.parseInt(giatri.replace("%", ""));
        if (giaTriSo < 5 || giaTriSo > 80) {
            JOptionPane.showMessageDialog(this, "Giá trị ưu đãi phải từ 5% đến 80%");
            return false;
        }

        if (apdung.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá trị áp dụng với");
            return false;
        }

        if (!apdung.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "Áp dụng với chỉ được chứa số và không âm");
            return false;
        }

        float apDung = Float.parseFloat(apdung);
        if (apDung < 10000 || apDung > 500000) {
            JOptionPane.showMessageDialog(this, "Áp dụng với phải từ 10.000 đến 500.000 VND");
            return false;
        }

        // === Validate ngày bắt đầu và kết thúc ===
        Date ngayBD = getDateFromComboBox(cboNgayStart, cboThangStart, cboNamStart);
        Date ngayKT = getDateFromComboBox(cboNgayEnd, cboThangEnd, cboNamEnd);

        if (ngayBD == null || ngayKT == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ngày bắt đầu và ngày kết thúc hợp lệ");
            return false;
        }

        LocalDate localNgayBD = ((java.sql.Date) ngayBD).toLocalDate();
        LocalDate localNgayKT = ((java.sql.Date) ngayKT).toLocalDate();

        if (localNgayBD.isAfter(localNgayKT)) {
            JOptionPane.showMessageDialog(this, "Ngày bắt đầu phải trước ngày kết thúc");
            return false;
        }

        long daysBetween = ChronoUnit.DAYS.between(localNgayBD, localNgayKT);

        if (daysBetween < 7) {
            JOptionPane.showMessageDialog(this, "Ngày kết thúc phải cách ngày bắt đầu ít nhất 7 ngày");
            return false;
        }

        if (daysBetween > 183) {
            JOptionPane.showMessageDialog(this, "Ưu đãi không được kéo dài quá 6 tháng");
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
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtGiatri = new javax.swing.JTextField();
        txtApDung = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBang = new javax.swing.JTable();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        lblBatDau = new javax.swing.JLabel();
        lblKetThuc = new javax.swing.JLabel();
        cboNgayStart = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        cboThangStart = new javax.swing.JComboBox<>();
        jLabel8 = new javax.swing.JLabel();
        cboNamStart = new javax.swing.JComboBox<>();
        jLabel9 = new javax.swing.JLabel();
        cboNgayEnd = new javax.swing.JComboBox<>();
        cboThangEnd = new javax.swing.JComboBox<>();
        cboNamEnd = new javax.swing.JComboBox<>();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        lblID = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        lblTrangThai = new javax.swing.JLabel();

        jLabel6.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel6.setText("KẾT THÚC");

        jLabel4.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel4.setText("ID ƯU ĐÃI");

        jLabel13.setText("jLabel13");

        lblID1.setFont(new java.awt.Font("Segoe UI Light", 1, 36)); // NOI18N
        lblID1.setForeground(new java.awt.Color(102, 0, 51));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel1.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel1.setText("ID ƯU ĐÃI");

        jLabel2.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel2.setText("GIÁ TRỊ %");

        txtGiatri.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N

        txtApDung.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N

        jLabel3.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel3.setText("ÁP DỤNG VỚI");

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
        btnThem.setText("THÊM ƯU ĐÃI");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(31, 51, 86));
        btnSua.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/Sua.png"))); // NOI18N
        btnSua.setText("SỬA ƯU ĐÃI");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
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

        lblBatDau.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        lblBatDau.setText("BẮT ĐẦU");

        lblKetThuc.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        lblKetThuc.setText("KẾT THÚC");

        cboNgayStart.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        jLabel7.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel7.setText("THÁNG");

        cboThangStart.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12" }));

        jLabel8.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel8.setText("NGÀY");

        cboNamStart.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2026" }));

        jLabel9.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel9.setText("NĂM");

        cboNgayEnd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31" }));

        cboThangEnd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", " " }));

        cboNamEnd.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2025", "2026" }));

        jLabel10.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel10.setText("NGÀY");

        jLabel11.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel11.setText("THÁNG");

        jLabel12.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel12.setText("NĂM");

        lblID.setFont(new java.awt.Font("Segoe UI Light", 1, 36)); // NOI18N
        lblID.setForeground(new java.awt.Color(102, 0, 51));

        jLabel5.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel5.setText("TRẠNG THÁI ");

        lblTrangThai.setFont(new java.awt.Font("Segoe UI Light", 1, 18)); // NOI18N
        lblTrangThai.setForeground(new java.awt.Color(102, 0, 51));

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGap(27, 27, 27)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel3)
                                    .addComponent(jLabel2)
                                    .addComponent(lblBatDau)
                                    .addComponent(lblKetThuc)
                                    .addComponent(jLabel1)))
                            .addComponent(btnThem, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblID, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(txtGiatri)
                                        .addComponent(txtApDung, javax.swing.GroupLayout.PREFERRED_SIZE, 331, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cboNgayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel10))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel11)
                                            .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addComponent(cboThangEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18)
                                                .addComponent(cboNamEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(43, 43, 43)
                                                .addComponent(jLabel5))))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cboNgayStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel8))
                                        .addGap(18, 18, 18)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cboThangStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel7))
                                        .addGap(20, 20, 20)
                                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel9)
                                            .addComponent(cboNamStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jLabel12))))
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addGap(0, 0, Short.MAX_VALUE)
                                        .addComponent(btnLamMoi, javax.swing.GroupLayout.PREFERRED_SIZE, 221, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 152, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(0, 0, Short.MAX_VALUE))))))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 894, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(25, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(lblID, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtGiatri, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addGap(21, 21, 21)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtApDung, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addGap(32, 32, 32)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(jLabel8)
                    .addComponent(jLabel9))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblBatDau)
                    .addComponent(cboNgayStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboThangStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboNamStart, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel10)
                            .addComponent(jLabel11)
                            .addComponent(jLabel12))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lblKetThuc)
                            .addComponent(cboNgayEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboThangEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cboNamEnd, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5)))
                    .addComponent(lblTrangThai, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(62, 62, 62)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnLamMoi)
                    .addComponent(btnSua))
                .addGap(69, 69, 69)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 253, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(84, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(22, 22, 22)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(31, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(42, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        if (!validateUuDai()) {
            return;
        }
        suaUuDai();
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
        lamMoiUuDai();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        if (!validateUuDai()) {
            return;
        }
        themUuDai();
    }//GEN-LAST:event_btnThemActionPerformed

    private void tblBangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangMouseClicked
        // TODO add your handling code here:
        showDetail();
    }//GEN-LAST:event_tblBangMouseClicked

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

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btgHan;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JComboBox<String> cboNamEnd;
    private javax.swing.JComboBox<String> cboNamStart;
    private javax.swing.JComboBox<String> cboNgayEnd;
    private javax.swing.JComboBox<String> cboNgayStart;
    private javax.swing.JComboBox<String> cboThangEnd;
    private javax.swing.JComboBox<String> cboThangStart;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblBatDau;
    private javax.swing.JLabel lblID;
    private javax.swing.JLabel lblID1;
    private javax.swing.JLabel lblKetThuc;
    private javax.swing.JLabel lblTrangThai;
    private javax.swing.JTable tblBang;
    private javax.swing.JTextField txtApDung;
    private javax.swing.JTextField txtGiatri;
    // End of variables declaration//GEN-END:variables
}
