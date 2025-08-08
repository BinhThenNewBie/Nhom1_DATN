/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

import DAO.ChiTietHoaDonDAO;
import DAO.HoaDonDAO;
import DAO.SanPhamDAO;
import DAO.UuDaiDAO;
import Model.ChiTietHoaDon;
import Model.HoaDon;
import Model.SanPham;
import Model.UuDai;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.PrinterException;
import java.io.File;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import util.QRCodeGenerator;

/**
 *
 * @author ADMIN
 */
public class QuanLyBanHang extends javax.swing.JFrame {

    DefaultTableModel modelUuDai;
    DefaultTableModel modelHDCho;
    DefaultTableModel modelCTHD;
    UuDaiDAO udDAO = new UuDaiDAO();
    HoaDonDAO hdDAO = new HoaDonDAO();
    ChiTietHoaDonDAO cthdDAO = new ChiTietHoaDonDAO();
    SanPhamDAO spDAO = new SanPhamDAO();
    private String HoaDonID = null;

    public QuanLyBanHang() {
        initComponents();
        initTable();
        fillTableUuDai();
        fillTableHDCho();
        fillTableCTHD();
        fillTableMenu();
        updateGiaSP();
        setLocationRelativeTo(null);

        Timer timer = new Timer(0, (e) -> {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            String time = sdf.format(new Date());
            lblTime.setText(time);
        });
        timer.start();

    }
    private String formatVND(float amount) {
        DecimalFormat formatter = new DecimalFormat("#,###");
        return formatter.format(amount) + " ₫";
    }

    public static float unformatCurrency(String formatted) {
        String clean = formatted.replaceAll("[^\\d.]", "");
        return Float.parseFloat(clean);
    }

    public void initTable() {
        modelUuDai = new DefaultTableModel();
        String[] colsUuDai = new String[]{"Giá trị", "Áp dụng HD từ"};
        modelUuDai.setColumnIdentifiers(colsUuDai);
        tblUuDai.setModel(modelUuDai);

        modelHDCho = new DefaultTableModel();
        String[] colsHDCho = new String[]{"ID_Hóa Đơn", "Tổng HĐ", "Thành Tiền", "Ưu Đãi"};
        modelHDCho.setColumnIdentifiers(colsHDCho);
        tblHoaDon.setModel(modelHDCho);

        modelCTHD = new DefaultTableModel();
        String[] cols = new String[]{"ID_Sản Phẩm", "Tên sản phẩm", "Giá sản phẩm", "Số Lượng"};
        modelCTHD.setColumnIdentifiers(cols);
        tblChiTietHoaDon.setModel(modelCTHD);
    }

    public void fillTableHDCho() {
        modelHDCho.setRowCount(0);
        List<HoaDon> list = hdDAO.getALL_HD();
        for (HoaDon hd : list) {
            int trangThai = hd.getTrangThai();
            if (trangThai == 0) {
                modelHDCho.addRow(new Object[]{
                    hd.getID_HD(),
                    formatVND(hd.getTongTienHD()),
                    formatVND(hd.getTongTienThanhToan()),
                    hd.getUuDai()
                });
            }
        }
    }

    public void fillTableUuDai() {
        modelUuDai.setRowCount(0);
        for (UuDai ud : udDAO.getAll()) {
            Date today = new Date();
            if (!ud.getNgayKetThuc().before(today)) {
                modelUuDai.addRow(new Object[]{
                    ud.getGiaTri(),
                    formatVND(ud.getApDungVoi())
                });
            }
        }

        TableColumnModel columnModel = tblUuDai.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(100); // Cột 1
        columnModel.getColumn(1).setPreferredWidth(200); // Cột 2 
    }

    public void fillTableCTHD() {
        DefaultTableModel model = (DefaultTableModel) tblChiTietHoaDon.getModel();
        model.setRowCount(0);
        String ID_HD = lblMaHD.getText().trim();
        List<ChiTietHoaDon> lstcthd = cthdDAO.getAll_CTHD(ID_HD);
        for (ChiTietHoaDon cthd : lstcthd) {
            model.addRow(new Object[]{
                cthd.getID_SP(),
                cthd.getTenSP(),
                formatVND(cthd.getGiaSP()),
                cthd.getSoLuong()
            });
        }
        TableColumnModel columnModel = tblChiTietHoaDon.getColumnModel();

        columnModel.getColumn(0).setPreferredWidth(80);
        columnModel.getColumn(1).setPreferredWidth(250);
        columnModel.getColumn(2).setPreferredWidth(166);
        columnModel.getColumn(3).setPreferredWidth(80);
    }

    public void fillTableMenu() {
        pnlMenu.removeAll();
        int itemWidth = 130;
        int itemHeight = 190;

        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.NORTHWEST;

        String loai = cbxLoc.getSelectedItem().toString().trim();
        String keyword = txtTimKiem.getText().trim().toLowerCase();

        List<SanPham> list = spDAO.getAll();

        // Lọc theo loại nếu không chọn "TẤT CẢ"
        if (!loai.equalsIgnoreCase("TẤT CẢ")) {
            list = list.stream()
                    .filter(sp -> sp.getLoaiSanPham().equalsIgnoreCase(loai))
                    .collect(Collectors.toList());
        }

        // Lọc theo từ khoá nếu có nhập
        if (!keyword.isEmpty()) {
            list = list.stream()
                    .filter(sp -> sp.getIDSanPham().toLowerCase().contains(keyword)
                    || sp.getTenSanPham().toLowerCase().contains(keyword))
                    .collect(Collectors.toList());
        }

        int col = 0;
        int row = 0;
        for (SanPham sp : list) {
            if (sp.getTrangThai() != 1) {
                continue;
            }

            JPanel panel = new JPanel();
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            panel.setPreferredSize(new Dimension(itemWidth, itemHeight));
            panel.setBackground(Color.WHITE);
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            panel.add(Box.createVerticalStrut(5));

            JLabel lblMa = new JLabel(sp.getIDSanPham(), SwingConstants.CENTER);
            lblMa.setFont(new Font("Segoe UI", Font.BOLD, 16));
            lblMa.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblMa.setPreferredSize(new Dimension(itemWidth, 35));

            JLabel lblImage = new JLabel("", SwingConstants.CENTER);
            lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblImage.setMaximumSize(new Dimension(100, 100));
            lblImage.setPreferredSize(new Dimension(100, 100));
            try {
                ImageIcon icon = new ImageIcon("src/Images_SanPham/" + sp.getIMG());
                Image img = icon.getImage();
                Image scaledImg = img.getScaledInstance(95, 95, Image.SCALE_SMOOTH);
                lblImage.setIcon(new ImageIcon(scaledImg));
            } catch (Exception e) {
                lblImage.setText("Không có ảnh");
                lblImage.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            }

            JPanel bottom = new JPanel();
            bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));
            bottom.setBackground(Color.WHITE);
            bottom.setPreferredSize(new Dimension(itemWidth, 20));

            JLabel lblTen = new JLabel(sp.getTenSanPham(), SwingConstants.CENTER);
            lblTen.setFont(new Font("Segoe UI", Font.BOLD, 12));
            lblTen.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblTen.setForeground(Color.red);
            lblTen.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
            if (sp.getTenSanPham().length() > 15) {
                lblTen.setText(sp.getTenSanPham().substring(0, 16) + "...");
            }

            JLabel lblGia = new JLabel(formatVND(sp.getGiaTien()), SwingConstants.CENTER);
            lblGia.setFont(new Font("Segoe UI", Font.BOLD, 18));
            lblGia.setAlignmentX(Component.CENTER_ALIGNMENT);

            bottom.add(lblTen);
            bottom.add(lblGia);

            panel.add(lblMa);
            panel.add(Box.createVerticalStrut(5));
            panel.add(lblImage);
            panel.add(Box.createVerticalStrut(5));
            panel.add(bottom);

            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setBackground(new Color(240, 240, 240));
                    panel.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 150), 2));
                }

                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setBackground(Color.WHITE);
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    showDetail(sp);
                }
            });
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            gbc.gridx = col;
            gbc.gridy = row;
            contentPanel.add(panel, gbc);
            col++;
            if (col >= 5) {
                col = 0;
                row++;
            }
        }

        if (col != 0) {
            for (int i = col; i < 5; i++) {
                JPanel panelTrang = new JPanel();
                panelTrang.setPreferredSize(new Dimension(itemWidth, itemHeight));
                panelTrang.setMinimumSize(new Dimension(itemWidth, itemHeight));
                panelTrang.setMaximumSize(new Dimension(itemWidth, itemHeight));
                panelTrang.setBackground(Color.WHITE);
                gbc.gridx = i;
                gbc.gridy = row;
                gbc.fill = GridBagConstraints.HORIZONTAL;
                gbc.weightx = 1.0;
                contentPanel.add(panelTrang, gbc);
            }
        }

        Dimension originalSize = pnlMenu.getSize();
        Point originalLocation = pnlMenu.getLocation();
        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);
        scrollPane.setPreferredSize(originalSize);
        scrollPane.setMinimumSize(originalSize);
        scrollPane.setMaximumSize(originalSize);

        pnlMenu.setLayout(new BorderLayout());
        pnlMenu.setPreferredSize(originalSize);
        pnlMenu.setMinimumSize(originalSize);
        pnlMenu.setMaximumSize(originalSize);
        pnlMenu.setSize(originalSize);
        pnlMenu.setBounds(originalLocation.x, originalLocation.y, originalSize.width, originalSize.height);
        pnlMenu.add(scrollPane, BorderLayout.CENTER);

        SwingUtilities.invokeLater(() -> {
            pnlMenu.setBounds(originalLocation.x, originalLocation.y, originalSize.width, originalSize.height);
            scrollPane.getVerticalScrollBar().setValue(0);
            scrollPane.getHorizontalScrollBar().setValue(0);
        });
        pnlMenu.revalidate();
        pnlMenu.repaint();
    }

    private void showDetail(SanPham sp) {
        if (sp == null) {
            return;
        }

        lblMaSP.setText(sp.getIDSanPham());
        txtSoLuong.setText("1");
        txtSoLuong.requestFocus();
        txtSoLuong.selectAll();

        try {
            ImageIcon icon = new ImageIcon("src/Images_SanPham/" + sp.getIMG());
            if (icon.getIconWidth() > 0) {
                // Sử dụng kích thước cố định thay vì kích thước hiện tại của label
                int fixedWidth = 156;
                int fixedHeight = 156;

                lblAnhSanPham.setIcon(new ImageIcon(icon.getImage().getScaledInstance(
                        fixedWidth, fixedHeight, Image.SCALE_SMOOTH)));
                lblAnhSanPham.setText("");
            } else {
                lblAnhSanPham.setIcon(null);
                lblAnhSanPham.setText("Không có ảnh");
            }
        } catch (Exception e) {
            lblAnhSanPham.setIcon(null);
            lblAnhSanPham.setText("Không có ảnh");
        }

        // Đảm bảo label giữ nguyên kích thước
        lblAnhSanPham.revalidate();
        lblAnhSanPham.repaint();
    }

    public void showDetailsHDCho() {
        int i = tblHoaDon.getSelectedRow();
        if (i >= 0) {
            String ID_HD = tblHoaDon.getValueAt(i, 0).toString();
            lblMaHD.setText(ID_HD);
            txtArea.setText("");
            List<HoaDon> listHD = hdDAO.getALL_ID_HD(ID_HD);
            float tienUUDAI = listHD.get(0).getTongTienUuDai();
            lblTienUUdAI.setText(formatVND(tienUUDAI));

            if (!listHD.isEmpty()) {
                HoaDon hd = listHD.get(0);

                String uudai = hd.getUuDai();
                if (uudai == null || uudai.trim().equals("") || uudai.equals("0%")) {
                    lblUuDai.setText("Không có ưu đãi");
                } else {
                    lblUuDai.setText(uudai + " Được áp dụng");
                }

                String pt = hd.getPhuongThucThanhToan();
                if (pt != null && !pt.trim().isEmpty()) {
                    if (pt.equalsIgnoreCase("Tiền mặt")) {
                        rdoTienMat.setSelected(true);
                        txtTienKhachDua.setEnabled(true);
                        btnTienTraLai.setEnabled(true);
                        lblTienTraLai.setEnabled(true);
                    } else if (pt.equalsIgnoreCase("Chuyển khoản")) {
                        rdoChuyenKhoan.setSelected(true);
                        txtTienKhachDua.setEnabled(false);
                        btnTienTraLai.setEnabled(false);
                        lblTienTraLai.setEnabled(false);
                    }
                } else {
                    buttonGroup1.clearSelection();
                }

                Float tienKhach = hd.getTienKhachHang();
                Float tienTra = hd.getTienTraLai();

                txtTienKhachDua.setText(String.valueOf(tienKhach));
                lblTienTraLai.setText(formatVND(tienTra));

            } else {
                lblUuDai.setText("Không có ưu đãi");
                txtTienKhachDua.setText("0");
                lblTienTraLai.setText("0");
                buttonGroup1.clearSelection();
            }

            List<ChiTietHoaDon> lstcthd = cthdDAO.getAll_CTHD(ID_HD);
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

    public void showDetailsUuDai() {
        int i = tblUuDai.getSelectedRow();
        if (i >= 0) {
            lblUuDai.setText(tblUuDai.getValueAt(i, 0).toString());
        }
    }

    public void showDetailsCTHD() {
        int i = tblChiTietHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một dòng trong bảng chi tiết!");
            return;
        }

        // Lấy dữ liệu từ bảng
        String maHD = lblMaHD.getText().trim();
        String maSP = tblChiTietHoaDon.getValueAt(i, 0).toString();

        ChiTietHoaDon cthd = cthdDAO.getAll_ID_HD_SP(maHD, maSP);
        if (cthd == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết hóa đơn!");
            return;
        }

        lblMaHD.setText(cthd.getID_HD());
        lblMaSP.setText(cthd.getID_SP());
        txtSoLuong.setText(String.valueOf(cthd.getSoLuong()));

        // Lấy ảnh sản phẩm từ mã SP
        List<SanPham> spList = spDAO.getAllID_SP(maSP);
        if (spList == null || spList.isEmpty()) {
            lblAnhSanPham.setText("Không tìm thấy sản phẩm");
            lblAnhSanPham.setIcon(null);
            return;
        }

        SanPham sp = spList.get(0);
        String tenAnh = sp.getIMG();

        if (tenAnh == null || tenAnh.trim().isEmpty() || tenAnh.equalsIgnoreCase("NO IMAGE")) {
            lblAnhSanPham.setText("Hình ảnh không tồn tại");
            lblAnhSanPham.setIcon(null);
        } else {
            try {
                String duongDan = "src/Images_SanPham/" + tenAnh;
                File file = new File(duongDan);
                if (!file.exists()) {
                    lblAnhSanPham.setText("Không tìm thấy ảnh");
                    lblAnhSanPham.setIcon(null);
                    return;
                }
                ImageIcon icon = new ImageIcon(duongDan);
                Image img = icon.getImage().getScaledInstance(lblAnhSanPham.getWidth(), lblAnhSanPham.getHeight(), Image.SCALE_SMOOTH);
                lblAnhSanPham.setIcon(new ImageIcon(img));
                lblAnhSanPham.setText("");
            } catch (Exception e) {
                lblAnhSanPham.setText("Lỗi ảnh");
                lblAnhSanPham.setIcon(null);
            }
        }
    }

    public void showdetailPTTT() {
        int i = tblHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
            return;
        }

        String ID_HD = tblHoaDon.getValueAt(i, 0).toString();
        List<HoaDon> hd = hdDAO.getALL_ID_HD(ID_HD);
        float tongTien = hd.get(0).getTongTienThanhToan();

        float tienKhachHang1 = 0;
        float tienKhachHang = tongTien;
        float tienTraLai = 0;

        String pttt1 = "Tiền mặt";
        String pttt2 = "Chuyển Khoản";

        if (rdoTienMat.isSelected()) {
            txtTienKhachDua.setEnabled(true);
            btnTienTraLai.setEnabled(true);
            hdDAO.Update_PhuongThucThanhToan(ID_HD, pttt1);
            hdDAO.Update_TKhachHang(ID_HD, tienKhachHang1);
        } else if (rdoChuyenKhoan.isSelected()) {
            txtTienKhachDua.setEnabled(false);
            btnTienTraLai.setEnabled(false);
            txtTienKhachDua.setText("0");

            hdDAO.Update_PhuongThucThanhToan(ID_HD, pttt2);
            hdDAO.Update_TKhachHang(ID_HD, tienKhachHang);
            hdDAO.Update_TTraLai(ID_HD, tienTraLai);

            String stk = "0964250706";
            String bankCode = "MB";
            String noiDung = "THANHTOAN-" + ID_HD;

            String qrUrl = "https://img.vietqr.io/image/" + bankCode + "-" + stk + "-compact2.png"
                    + "?amount=" + Math.round(tongTien)
                    + "&addInfo=" + URLEncoder.encode(noiDung, StandardCharsets.UTF_8);

            try {
                BufferedImage qrImage = QRCodeGenerator.showQRCode(qrUrl);
                JOptionPane.showMessageDialog(this,
                        new JLabel(new ImageIcon(qrImage)),
                        "Quét mã để chuyển khoản",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(this, "Lỗi tạo QR: " + e.getMessage());
            }
        }
    }

// TẠO MÃ HOÁ ĐƠN NGẪU NHIÊN
    private String generateMaHD() {
        Random rnd = new Random();
        String maHD;
        do {
            int number = 100000 + rnd.nextInt(900000);
            maHD = "HD" + number;
        } while (hdDAO.existsMaHD(maHD));
        return maHD;
    }
// TẠO HOÁ ĐƠN

    public void createMaHD() {
        cthdDAO.clearOrderTemp();

        DefaultTableModel model = (DefaultTableModel) tblChiTietHoaDon.getModel();
        String newMaHD = generateMaHD();
        lblMaHD.setText(newMaHD);

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        String nguoiPhuTrach = Login.emailLogin;
        String ngayThangNam = sdfDate.format(now);
        String thoiGian = sdfTime.format(now);
        float tongTienHD = 0.0f;
        float ttUuDai = 0.0f;
        float ttThanhToan = 0.0f;
        String phuongthucThanhToan = "";
        float tKhachTra = 0.0f;
        float tTraLai = 0.0f;
        String uuDai = "0%";
        int trangThai = 0;
        HoaDon hd = new HoaDon(newMaHD, nguoiPhuTrach, ngayThangNam, thoiGian, tongTienHD, ttUuDai, ttThanhToan, phuongthucThanhToan, tKhachTra, tTraLai, uuDai, trangThai);
        hdDAO.Save_HD(hd);

        lblMaSP.setText("");
        lblUuDai.setText("");
        model.setRowCount(0);
        fillTableHDCho();
    }
// THÊM SỐ LƯỢNG CHO SẢN PHẨM

    public void addSP() {
        String ID_SP = lblMaSP.getText();
        String ID_HD = lblMaHD.getText();
        int soLuong = Integer.parseInt(txtSoLuong.getText());

        if (ID_HD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CHƯA CÓ HÓA ĐƠN");
            return;
        }

        ChiTietHoaDon cthdCu = cthdDAO.getAll_ID_HD_SP(ID_HD, ID_SP);
        if (cthdCu != null) {
            int soLuongHienTai = cthdCu.getSoLuong();
            int tongSoLuong = soLuongHienTai + soLuong;

            if (tongSoLuong > 50) {
                JOptionPane.showMessageDialog(this,
                        "Bạn chỉ có thể thêm tối đa 50 sản phẩm.\n"
                        + "Hiện tại đã có: " + soLuongHienTai + "\n"
                        + "Bạn đang cố thêm: " + soLuong + "\n"
                        + "Tổng cộng sẽ là: " + tongSoLuong + " > 50");
                return;
            }

            cthdCu.setSoLuong(tongSoLuong);
            cthdDAO.UpdateSP(ID_HD, ID_SP, cthdCu);

        } else {
            if (soLuong > 50) {
                JOptionPane.showMessageDialog(this,
                        "Bạn chỉ có thể thêm tối đa 50 sản phẩm.\n"
                        + "Bạn đang cố thêm: " + soLuong + " > 50");
                return;
            }

            for (SanPham sp : spDAO.getAll()) {
                if (sp.getIDSanPham().equals(ID_SP)) {
                    ChiTietHoaDon cthd = new ChiTietHoaDon();
                    cthd.setID_HD(ID_HD);
                    cthd.setID_SP(sp.getIDSanPham());
                    cthd.setTenSP(sp.getTenSanPham());
                    cthd.setGiaSP(sp.getGiaTien());
                    cthd.setSoLuong(soLuong);
                    cthdDAO.Save_CTHD(cthd);
                    break;
                }
            }
        }

        float tong = 0;
        float tienUuDai = 0;
        List<ChiTietHoaDon> list = cthdDAO.getAll_CTHD(ID_HD);
        for (ChiTietHoaDon ct : list) {
            tong += ct.getGiaSP() * ct.getSoLuong();
        }

        String uuDai = hdDAO.getALL_HD().stream()
                .filter(hd -> hd.getID_HD().equals(ID_HD))
                .findFirst()
                .map(hd -> hd.getUuDai())
                .orElse("0%");

        float tongSauUuDai = tong;
        if (!uuDai.equals("0%")) {
            try {
                float giam = Float.parseFloat(uuDai.replace("%", "").trim());
                tongSauUuDai = tong * (1 - giam / 100);
                tienUuDai = tong - tongSauUuDai;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        hdDAO.Update_TT_HD(ID_HD, tong);
        hdDAO.Update_TTThanhToan(ID_HD, tongSauUuDai);
        hdDAO.Update_TTUuDai(ID_HD, tienUuDai);
        fillTableHDCho();
        fillTableCTHD();
    }

//XOÁ HOÁ ĐƠN VÀ XOÁ SẢN PHẨM
    public void deleteHD() {
        int i = tblHoaDon.getSelectedRow();
        if (i >= 0) {
            int choose = JOptionPane.showConfirmDialog(this, "XÁC NHẬN", "BẠN MUỐN HỦY", JOptionPane.YES_NO_OPTION);
            if (choose == JOptionPane.YES_OPTION) {
                String ID_HD = tblHoaDon.getValueAt(i, 0).toString();
                int res1 = cthdDAO.Delete_CTHD(ID_HD);
                int res2 = hdDAO.Delete_HD(ID_HD);
                if (res1 == 1 && res2 == 1) {
                    fillTableHDCho();
                    lblMaHD.setText("");
                    lblMaSP.setText("");
                    lblTienTraLai.setText("");
                    txtSoLuong.setText("");
                    txtTienKhachDua.setText("");
                    lblUuDai.setText("");
                    txtArea.setText("");
                    fillTableCTHD();
                    JOptionPane.showMessageDialog(this, " HỦY THÀNH CÔNG");
                } else {
                    JOptionPane.showMessageDialog(this, "HỦY THẤT BẠI");
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "VUI LÒNG CHỌN HÓA ĐƠN MUỐN HỦY!");
        }
    }

    public void deleteSP() {
        int i = tblChiTietHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xoá");
            return;
        }
        if (!txtArea.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng xoá hiển thị hoá đơn");
            return;
        }
        String ID_HD = lblMaHD.getText();

        //Kiểm tra ưu đãi trước khi xoá
        String uuDai = hdDAO.getALL_HD().stream()
                .filter(hd -> hd.getID_HD().equals(ID_HD))
                .findFirst()
                .map(hd -> hd.getUuDai())
                .orElse("0%");
        if (!uuDai.equals("0%")) {
            JOptionPane.showMessageDialog(this, "Vui lòng huỷ ưu đãi trước khi xoá sản phẩm!");
            return;
        }

        String ID_SP = tblChiTietHoaDon.getValueAt(i, 0).toString();
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc chắn muốn xoá sản phẩm này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        List<ChiTietHoaDon> listSauXoa = cthdDAO.getAll_CTHD(ID_HD);
        listSauXoa.removeIf(ct -> ct.getID_SP().equals(ID_SP));
        float tong = 0;
        for (ChiTietHoaDon ct : listSauXoa) {
            tong += ct.getGiaSP() * ct.getSoLuong();
        }

        int result = cthdDAO.Delete_SP(ID_SP, ID_HD);
        if (result == 1) {
            hdDAO.Update_TT_HD(ID_HD, tong);
            fillTableHDCho();
            fillTableCTHD();
            JOptionPane.showMessageDialog(this, "Xoá thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Xoá không thành công!");
        }
    }
//  UPDATE GIÁ CỦA SẢN PHẨM & UPDATE SỐ LƯỢNG

    public void updateGiaSP() {
        List<HoaDon> lsthdc = hdDAO.getALL_HD();
        for (HoaDon hdc : lsthdc) {
            if (hdc.getTrangThai() == 0) {
                String ID_HD = hdc.getID_HD();
                List<ChiTietHoaDon> lstcthd = cthdDAO.getAll_CTHD(ID_HD);
                float tong = 0;
                for (ChiTietHoaDon cthd : lstcthd) {
                    float giaMoi = spDAO.getGiaByID(cthd.getID_SP());
                    cthdDAO.UpdateGia(ID_HD, cthd.getID_SP(), giaMoi);
                    tong += giaMoi * cthd.getSoLuong();
                }
                String uuDai = hdc.getUuDai();
                float tongSauUuDai = tong;
                float tienUuDai = 0;
                if (!uuDai.equals("0%")) {
                    try {
                        float giam = Float.parseFloat(uuDai.replace("%", "").trim());
                        if (giam > 0) {
                            tongSauUuDai = tong * (1 - giam / 100);
                            tienUuDai = tong - tongSauUuDai;
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                hdDAO.Update_TT_HD(ID_HD, tong);
                hdDAO.Update_TTThanhToan(ID_HD, tongSauUuDai);
                hdDAO.Update_TTUuDai(ID_HD, tienUuDai);
            }
        }
        fillTableHDCho();
    }

    public void updateSP() {
        int i = tblChiTietHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa");
            return;
        }

        String ID_HD = lblMaHD.getText();

        //Kiểm tra ưu đãi trước khi sửa
        String uuDai = hdDAO.getALL_HD().stream()
                .filter(hd -> hd.getID_HD().equals(ID_HD))
                .findFirst()
                .map(hd -> hd.getUuDai())
                .orElse("0%");
        if (!uuDai.equals("0%")) {
            JOptionPane.showMessageDialog(this, "Vui lòng huỷ ưu đãi trước khi sửa sản phẩm!");
            return;
        }

        String ID_SP = tblChiTietHoaDon.getValueAt(i, 0).toString();
        String tenSP = tblChiTietHoaDon.getValueAt(i, 1).toString();

        float giaSP;
        try {
            giaSP = unformatCurrency(tblChiTietHoaDon.getValueAt(i, 2).toString());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá không hợp lệ!");
            return;
        }

        int soLuong;
        try {
            soLuong = Integer.parseInt(txtSoLuong.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng không hợp lệ!");
            return;
        }

        ChiTietHoaDon cthd = new ChiTietHoaDon(ID_HD, ID_SP, tenSP, giaSP, soLuong);
        List<ChiTietHoaDon> listSauSua = cthdDAO.getAll_CTHD(ID_HD);
        for (ChiTietHoaDon ct : listSauSua) {
            if (ct.getID_SP().equals(ID_SP)) {
                ct.setSoLuong(soLuong);
                break;
            }
        }

        float tong = 0;
        for (ChiTietHoaDon ct : listSauSua) {
            tong += ct.getGiaSP() * ct.getSoLuong();
        }

        int result = cthdDAO.UpdateSP(ID_HD, ID_SP, cthd);
        if (result == 1) {
            hdDAO.Update_TT_HD(ID_HD, tong);
            fillTableHDCho();
            fillTableCTHD();
            JOptionPane.showMessageDialog(this, "Sửa thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Sửa không thành công!");
        }
    }
// KIỂM TRA ĐỊNH DẠNG NHẬP SỐ LƯỢNG

    public boolean validateForm() {
        String maHD = lblMaHD.getText().trim();
        String maSP = lblMaSP.getText().trim();
        String soLuongStr = txtSoLuong.getText().trim();

        if (!txtArea.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng xoá hiển thị hoá đơn");
            return false;
        }

        if (maHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
            return false;
        }

        if (maSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm!");
            return false;
        }

        if (soLuongStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số lượng!");
            return false;
        }

        try {
            int soLuong = Integer.parseInt(soLuongStr);
            if (soLuong <= 0) {
                JOptionPane.showMessageDialog(this, "Số lượng tối thiểu là 1");
                return false;
            }
            if (soLuong >= 50) {
                JOptionPane.showMessageDialog(this, "Số lượng tối đa là 50");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số lượng phải là số nguyên!");
            return false;
        }
        return true;
    }

    public boolean validateTienKhachDua() {
        int i = tblHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn!");
            return false;
        }

        String tienKDStr = txtTienKhachDua.getText().trim();
        if (tienKDStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tiền khách đưa!");
            return false;
        }

        float tienKD;
        try {
            tienKD = Float.parseFloat(tienKDStr);
            if (tienKD < 0) {
                JOptionPane.showMessageDialog(this, "Tiền khách đưa không được âm!");
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Tiền khách đưa phải là số hợp lệ!");
            return false;
        }

        String ID_HD = tblHoaDon.getValueAt(i, 0).toString();
        float thanhTien = hdDAO.getALL_ID_HD(ID_HD).get(0).getTongTienThanhToan();

        if (tienKD < thanhTien) {
            JOptionPane.showMessageDialog(this, "Tiền khách đưa không đủ để thanh toán!");
            return false;
        }

        return true;
    }

    public void uuDai() {
        int rowUuDai = tblUuDai.getSelectedRow();
        int rowHoaDon = tblHoaDon.getSelectedRow();

        if (rowHoaDon < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoá đơn để áp dụng ưu đãi.");
            return;
        }

        String ID_HD = tblHoaDon.getValueAt(rowHoaDon, 0).toString().trim();
        if (ID_HD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã hoá đơn không hợp lệ!");
            return;
        }

        List<HoaDon> dsHoaDon = hdDAO.getALL_ID_HD(ID_HD);
        if (dsHoaDon.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn phù hợp.");
            return;
        }

        HoaDon hd = dsHoaDon.get(0);
        String uuDaiHienTai = hd.getUuDai();
        if (uuDaiHienTai != null && !uuDaiHienTai.trim().isEmpty()) {
            try {
                float daCoUuDai = Float.parseFloat(uuDaiHienTai.replace("%", "").trim());
                if (daCoUuDai > 0) {
                    JOptionPane.showMessageDialog(this, "Hóa đơn đã áp dụng ưu đãi. Vui lòng xóa ưu đãi cũ trước.");
                    return;
                }
            } catch (NumberFormatException e) {
                // Không đọc được -> coi như chưa có ưu đãi
            }
        }

        if (rowUuDai < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn mã ưu đãi để áp dụng.");
            return;
        }

        // Lấy thông tin ưu đãi từ bảng
        String giaTriUuDai = tblUuDai.getValueAt(rowUuDai, 0).toString();
        float dieuKien = unformatCurrency(tblUuDai.getValueAt(rowUuDai, 1).toString());
        float tongTienHD = unformatCurrency(tblHoaDon.getValueAt(rowHoaDon, 1).toString());

        if (tongTienHD < dieuKien) {
            JOptionPane.showMessageDialog(this, "Không đủ điều kiện để áp dụng ưu đãi này.");
            return;
        }

        // Tính lại tổng tiền chi tiết hóa đơn
        List<ChiTietHoaDon> chiTiet = cthdDAO.getAll_CTHD(ID_HD);
        float tongGoc = 0;
        for (ChiTietHoaDon ct : chiTiet) {
            tongGoc += ct.getGiaSP() * ct.getSoLuong();
        }

        float phanTramUuDai = Float.parseFloat(giaTriUuDai.replace("%", "").trim());
        float tongSauUuDai = tongGoc * (1 - phanTramUuDai / 100f);
        float tienUuDai = tongGoc - tongSauUuDai;

        hdDAO.updateUuDai(ID_HD, giaTriUuDai);
        hdDAO.Update_TTUuDai(ID_HD, tienUuDai);
        hdDAO.Update_TTThanhToan(ID_HD, tongSauUuDai);
        showDetailsHDCho();
        lblUuDai.setText(giaTriUuDai + " Được áp dụng");
        fillTableHDCho();
        JOptionPane.showMessageDialog(this, "Áp dụng thành công!");
    }

    public void xoaUuDai() {
        int i = tblHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoá đơn để xoá ưu đãi.");
            return;
        }

        String ID_HD = tblHoaDon.getValueAt(i, 0).toString();

        List<ChiTietHoaDon> chiTiet = cthdDAO.getAll_CTHD(ID_HD);
        float tongThanhToan = 0;
        for (ChiTietHoaDon ct : chiTiet) {
            tongThanhToan += ct.getGiaSP() * ct.getSoLuong();
        }

        float tienUuDai = 0;
        // kiểu tra nếu không có ưu đãi thì báo là không có
        String uudai = "0%";
        List<HoaDon> hd = hdDAO.getALL_ID_HD(ID_HD);
        String uuDAI = hd.get(0).getUuDai();
        if (uudai.equalsIgnoreCase(uuDAI)) {
            JOptionPane.showMessageDialog(this, "Không có ưu đãi cần huỷ!");
            return;
        }
        lblUuDai.setText("Không có ưu đãi");
        hdDAO.Update_TTThanhToan(ID_HD, tongThanhToan);
        hdDAO.Update_TTUuDai(ID_HD, tienUuDai);
        hdDAO.updateUuDai(ID_HD, "0%");
        showDetailsHDCho();
        fillTableHDCho();

        JOptionPane.showMessageDialog(this, "Huỷ áp dụng thành công!");
    }

    public void Change() {
        int i = tblHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn một hoá đơn!");
            return;
        }

        String ID_HD = tblHoaDon.getValueAt(i, 0).toString();

        if (txtTienKhachDua.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền khách đưa!");
            return;
        }

        float tienKhachDua;
        try {
            tienKhachDua = Float.parseFloat(txtTienKhachDua.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Số tiền khách đưa không hợp lệ!");
            return;
        }

        List<HoaDon> listHD = hdDAO.getALL_ID_HD(ID_HD);
        if (listHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn phù hợp!");
            return;
        }

        float thanhTien = listHD.get(0).getTongTienThanhToan();  // Lấy phần tử đầu tiên an toàn
        float tienTraLai = tienKhachDua - thanhTien;
        lblTienTraLai.setText(formatVND(tienTraLai));

        hdDAO.Update_TKhachHang(ID_HD, tienKhachDua);
        hdDAO.Update_TTraLai(ID_HD, tienTraLai);
    }

    public void addHD() {
        String ID_HD = lblMaHD.getText();
        List<HoaDon> lsthd = hdDAO.getALL_ID_HD(ID_HD);
        if (lsthd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hoá đơn!");
            return;
        }
        HoaDonID = ID_HD;
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

        for (int row = 0; row < tblChiTietHoaDon.getRowCount(); row++) {
            String tenMon = tblChiTietHoaDon.getValueAt(row, 1).toString();
            String soLuong = tblChiTietHoaDon.getValueAt(row, 3).toString();
            String donGia = tblChiTietHoaDon.getValueAt(row, 2).toString();
            float donGiaFloat = Float.parseFloat(donGia.replace(",", "").replace("₫", "").trim());
            hoaDon.append(String.format("%-20s %3s %,15.0f\n", tenMon, soLuong, donGiaFloat));
        }

        hoaDon.append("________________________________________\n");
        hoaDon.append(String.format("Tổng tiền             : %,15.0f đ\n", tongTien_HD));
        hoaDon.append(String.format("Ưu đãi                : %s - %,10.0f đ\n", uuDai, tienUuDai));
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

        txtTienKhachDua.setEnabled(false);
        btnTienTraLai.setEnabled(false);
        rdoChuyenKhoan.setEnabled(false);
        rdoTienMat.setEnabled(false);
    }

    public void clear() {
        txtArea.setText("");
        txtTienKhachDua.setText("");
        lblTienTraLai.setText("0");
        buttonGroup1.clearSelection();
        txtTienKhachDua.setEnabled(true);
        btnTienTraLai.setEnabled(true);
        rdoChuyenKhoan.setEnabled(true);
        rdoTienMat.setEnabled(true);
    }

    public void payment() {
        int i = tblHoaDon.getSelectedRow();
        if (txtArea.getText().equals("")) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn ADD để hiển thị hoá đơn!");
            return;
        }
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoá đơn để thanh toán");
            return;
        }

        String ID_HD = tblHoaDon.getValueAt(i, 0).toString();

        if (HoaDonID == null || !ID_HD.equals(HoaDonID)) {
            int choice = JOptionPane.showConfirmDialog(
                    this,
                    "Bạn đang hiển thị hoá đơn " + HoaDonID + ", nhưng bạn vừa chọn thanh toán hoá đơn " + ID_HD + ".\n"
                    + "Bạn có muốn tiếp tục không?",
                    "CẢNH BÁO!!!",
                    JOptionPane.YES_NO_OPTION
            );

            if (choice != JOptionPane.YES_OPTION) {
                return;
            }

            lblMaHD.setText(ID_HD);
            addHD();
        }

        int input = JOptionPane.showConfirmDialog(this, "Bạn muốn thanh toán hoá đơn?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (input == JOptionPane.YES_OPTION) {
            int printHD = JOptionPane.showConfirmDialog(this, "Bạn muốn xuất hoá đơn không?", "Xác nhận!", JOptionPane.YES_NO_OPTION);
            if (printHD == JOptionPane.YES_OPTION) {
                try {
                    txtArea.print();
                } catch (PrinterException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + ex.getMessage());
                }
            }
            int trangThai = 1;
            hdDAO.updatetrangThai(ID_HD, trangThai);
            fillTableHDCho();
            clear();
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Huỷ thanh toán!");
        }
    }

    public boolean validatePayment() {
        if (tblChiTietHoaDon.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm nào trong hoá đơn");
            return false;
        }
        return true;
    }

    public boolean validateAddHD() {
        int rowCount = tblChiTietHoaDon.getRowCount();
        if (rowCount == 0) {
            JOptionPane.showMessageDialog(this, "Hóa đơn không có món nào!");
            return false;
        }

        int i = tblHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để thanh toán!");
            return false;
        }

        String ID_HD = tblHoaDon.getValueAt(i, 0).toString().trim();
        List<HoaDon> hd = hdDAO.getALL_ID_HD(ID_HD);
        if (hd.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn phù hợp! ID_HD: " + ID_HD);
            return false;
        }

        List<HoaDon> lsthd = hdDAO.getALL_ID_HD(ID_HD);
        String pttt = lsthd.get(0).getPhuongThucThanhToan();
        float thanhtien = lsthd.get(0).getTongTienThanhToan();
        if (pttt == null || pttt.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn phương thức thanh toán!");
            return false;
        }

        if (pttt.equalsIgnoreCase("Tiền mặt")) {
            String tienKhach = txtTienKhachDua.getText().trim();
            String tienTraLai = lblTienTraLai.getText().trim();

            float tienKhachDB = lsthd.get(0).getTienKhachHang();
            if (tienKhachDB <= 0) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền khách đưa!");
                return false;
            }

            if (tienKhach.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhập số tiền khách đưa!");
                return false;
            }

            try {
                float tien = Float.parseFloat(tienKhach);
                if (tien < 0) {
                    JOptionPane.showMessageDialog(this, "Tiền khách đưa không hợp lệ!");
                    return false;
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Tiền khách đưa phải là số!");
                return false;
            }

            if (tienTraLai.isEmpty() || tienTraLai.equals("0") || tienTraLai.equals("0₫")) {
                JOptionPane.showMessageDialog(this, "Vui lòng nhấn 'CHANGE' để tính tiền trả lại!");
                return false;
            }
            if (tienKhachDB < thanhtien) {
                JOptionPane.showMessageDialog(this, "Tiền khách đưa không thể thanh toán hoá đơn!");
                return false;
            }

        } else {
            hdDAO.Update_TKhachHang(ID_HD, thanhtien);
            showDetailsHDCho();
        }

        return true;
    }

    public boolean validateFindSP() {
        String input = txtTimKiem.getText().trim();

        if (input.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập từ khóa tìm kiếm!");
            txtTimKiem.requestFocus();
            return false;
        }

        if (!input.matches(".*[a-zA-Z0-9].*")) {
            JOptionPane.showMessageDialog(this, "Từ khóa tìm kiếm phải chứa chữ hoặc số!");
            txtTimKiem.requestFocus();
            return false;
        }
        return true;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        pnlBanHang = new javax.swing.JPanel();
        pnlChiTietHoaDon7 = new javax.swing.JPanel();
        lblTittlePnlChiTietHoaDon7 = new javax.swing.JLabel();
        jSeparator12 = new javax.swing.JSeparator();
        jScrollPane11 = new javax.swing.JScrollPane();
        tblChiTietHoaDon = new javax.swing.JTable();
        pnlUuDai = new javax.swing.JPanel();
        lblTittlePnlUuDai = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane3 = new javax.swing.JScrollPane();
        tblUuDai = new javax.swing.JTable();
        btnApDung = new javax.swing.JButton();
        btnHuyUuDai = new javax.swing.JButton();
        pnlHoaDon = new javax.swing.JPanel();
        lblTittlePnlHoaDon = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        btnThanhToan = new javax.swing.JButton();
        btnHuyDon = new javax.swing.JButton();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblHoaDon = new javax.swing.JTable();
        btnTaoHoaDon = new javax.swing.JButton();
        pnlThongTin = new javax.swing.JPanel();
        lblTittlePnlThongTin = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        lblTittleMaHD = new javax.swing.JLabel();
        lblMaHD = new javax.swing.JLabel();
        lblMaSP = new javax.swing.JLabel();
        lblTittleMaSP = new javax.swing.JLabel();
        lblTittleSoLuong = new javax.swing.JLabel();
        txtSoLuong = new javax.swing.JTextField();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        lblTittleUuDai = new javax.swing.JLabel();
        lblUuDai = new javax.swing.JLabel();
        lblAnhSanPham = new javax.swing.JLabel();
        txtTienKhachDua = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        rdoTienMat = new javax.swing.JRadioButton();
        rdoChuyenKhoan = new javax.swing.JRadioButton();
        jSeparator6 = new javax.swing.JSeparator();
        btnTienTraLai = new javax.swing.JButton();
        lblTienTraLai = new javax.swing.JLabel();
        lblTienUUdAI = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        pnlMenu = new javax.swing.JPanel();
        lblTittlePnlMenu = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        cbxLoc = new javax.swing.JComboBox<>();
        btnFilter = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtArea = new javax.swing.JTextArea();
        btnAdd = new javax.swing.JButton();
        btnClear = new javax.swing.JButton();
        txtTimKiem = new javax.swing.JTextField();
        btnFind = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlBanHang.setBackground(new java.awt.Color(31, 51, 86));

        pnlChiTietHoaDon7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblTittlePnlChiTietHoaDon7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTittlePnlChiTietHoaDon7.setForeground(new java.awt.Color(31, 51, 86));
        lblTittlePnlChiTietHoaDon7.setText("CHI TIẾT HÓA ĐƠN:");

        tblChiTietHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblChiTietHoaDon.setModel(new javax.swing.table.DefaultTableModel(
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
        tblChiTietHoaDon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblChiTietHoaDonMouseClicked(evt);
            }
        });
        jScrollPane11.setViewportView(tblChiTietHoaDon);

        javax.swing.GroupLayout pnlChiTietHoaDon7Layout = new javax.swing.GroupLayout(pnlChiTietHoaDon7);
        pnlChiTietHoaDon7.setLayout(pnlChiTietHoaDon7Layout);
        pnlChiTietHoaDon7Layout.setHorizontalGroup(
            pnlChiTietHoaDon7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChiTietHoaDon7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlChiTietHoaDon7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlChiTietHoaDon7Layout.createSequentialGroup()
                        .addComponent(lblTittlePnlChiTietHoaDon7)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator12)
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 777, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlChiTietHoaDon7Layout.setVerticalGroup(
            pnlChiTietHoaDon7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlChiTietHoaDon7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTittlePnlChiTietHoaDon7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane11, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addContainerGap())
        );

        pnlUuDai.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblTittlePnlUuDai.setBackground(new java.awt.Color(31, 51, 86));
        lblTittlePnlUuDai.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTittlePnlUuDai.setForeground(new java.awt.Color(31, 51, 86));
        lblTittlePnlUuDai.setText("ƯU ĐÃI:");

        tblUuDai.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
        tblUuDai.setModel(new javax.swing.table.DefaultTableModel(
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
        tblUuDai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblUuDaiMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(tblUuDai);

        btnApDung.setBackground(new java.awt.Color(31, 51, 86));
        btnApDung.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnApDung.setForeground(new java.awt.Color(255, 255, 255));
        btnApDung.setText("APPLY");
        btnApDung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApDungActionPerformed(evt);
            }
        });

        btnHuyUuDai.setBackground(new java.awt.Color(31, 51, 86));
        btnHuyUuDai.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnHuyUuDai.setForeground(new java.awt.Color(255, 0, 0));
        btnHuyUuDai.setText("REMVOE");
        btnHuyUuDai.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyUuDaiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlUuDaiLayout = new javax.swing.GroupLayout(pnlUuDai);
        pnlUuDai.setLayout(pnlUuDaiLayout);
        pnlUuDaiLayout.setHorizontalGroup(
            pnlUuDaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUuDaiLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlUuDaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlUuDaiLayout.createSequentialGroup()
                        .addGroup(pnlUuDaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator3)
                            .addGroup(pnlUuDaiLayout.createSequentialGroup()
                                .addGroup(pnlUuDaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTittlePnlUuDai)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(0, 3, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(pnlUuDaiLayout.createSequentialGroup()
                        .addGap(17, 17, 17)
                        .addComponent(btnApDung)
                        .addGap(26, 26, 26)
                        .addComponent(btnHuyUuDai)
                        .addGap(20, 20, 20))))
        );
        pnlUuDaiLayout.setVerticalGroup(
            pnlUuDaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUuDaiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTittlePnlUuDai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 221, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlUuDaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApDung)
                    .addComponent(btnHuyUuDai))
                .addGap(11, 11, 11))
        );

        pnlHoaDon.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblTittlePnlHoaDon.setBackground(new java.awt.Color(31, 51, 86));
        lblTittlePnlHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTittlePnlHoaDon.setForeground(new java.awt.Color(31, 51, 86));
        lblTittlePnlHoaDon.setText("HÓA ĐƠN:");

        btnThanhToan.setBackground(new java.awt.Color(31, 51, 86));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnThanhToan.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhToan.setText("CHECKOUT");
        btnThanhToan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnThanhToanMouseClicked(evt);
            }
        });
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnHuyDon.setBackground(new java.awt.Color(31, 51, 86));
        btnHuyDon.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnHuyDon.setForeground(new java.awt.Color(255, 0, 0));
        btnHuyDon.setText("CANCEL");
        btnHuyDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyDonActionPerformed(evt);
            }
        });

        tblHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 16)); // NOI18N
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
        jScrollPane4.setViewportView(tblHoaDon);

        btnTaoHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTaoHoaDon.setForeground(new java.awt.Color(255, 0, 0));
        btnTaoHoaDon.setText("CREATE INVOICE");
        btnTaoHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHoaDonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlHoaDonLayout = new javax.swing.GroupLayout(pnlHoaDon);
        pnlHoaDon.setLayout(pnlHoaDonLayout);
        pnlHoaDonLayout.setHorizontalGroup(
            pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHoaDonLayout.createSequentialGroup()
                .addGap(58, 58, 58)
                .addComponent(btnThanhToan)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 127, Short.MAX_VALUE)
                .addComponent(btnHuyDon)
                .addGap(51, 51, 51))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator5)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlHoaDonLayout.createSequentialGroup()
                        .addComponent(lblTittlePnlHoaDon)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnTaoHoaDon)
                        .addGap(11, 11, 11)))
                .addContainerGap())
        );
        pnlHoaDonLayout.setVerticalGroup(
            pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblTittlePnlHoaDon)
                    .addComponent(btnTaoHoaDon))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 7, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnHuyDon)
                    .addComponent(btnThanhToan))
                .addGap(17, 17, 17))
        );

        pnlThongTin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblTittlePnlThongTin.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTittlePnlThongTin.setForeground(new java.awt.Color(31, 51, 86));
        lblTittlePnlThongTin.setText("THÔNG TIN HÓA ĐƠN:");

        lblTittleMaHD.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTittleMaHD.setText("MÃ HÓA ĐƠN:");

        lblMaHD.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblMaHD.setForeground(new java.awt.Color(255, 0, 0));

        lblMaSP.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblMaSP.setForeground(new java.awt.Color(255, 0, 0));

        lblTittleMaSP.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTittleMaSP.setText("MÃ SẢN PHẨM:");

        lblTittleSoLuong.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTittleSoLuong.setText("SỐ LƯỢNG:");

        txtSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        btnThem.setBackground(new java.awt.Color(31, 51, 86));
        btnThem.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setText("+");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(31, 51, 86));
        btnSua.setFont(new java.awt.Font("Segoe UI Black", 1, 14)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setText("-");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(31, 51, 86));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText("DELETE");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        lblTittleUuDai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTittleUuDai.setText("ƯU ĐÃI:");

        lblUuDai.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblUuDai.setForeground(new java.awt.Color(255, 0, 0));

        lblAnhSanPham.setBackground(new java.awt.Color(118, 142, 163));
        lblAnhSanPham.setForeground(new java.awt.Color(118, 142, 163));
        lblAnhSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 51, 86), 5));
        lblAnhSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhSanPhamMouseClicked(evt);
            }
        });

        txtTienKhachDua.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("TIỀN KHÁCH ĐƯA:");

        buttonGroup1.add(rdoTienMat);
        rdoTienMat.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rdoTienMat.setText("TIỀN MẶT");
        rdoTienMat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoTienMatMouseClicked(evt);
            }
        });

        buttonGroup1.add(rdoChuyenKhoan);
        rdoChuyenKhoan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        rdoChuyenKhoan.setText("CHUYỂN KHOẢN");
        rdoChuyenKhoan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                rdoChuyenKhoanMouseClicked(evt);
            }
        });
        rdoChuyenKhoan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                rdoChuyenKhoanKeyPressed(evt);
            }
        });

        btnTienTraLai.setBackground(new java.awt.Color(31, 51, 86));
        btnTienTraLai.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnTienTraLai.setForeground(new java.awt.Color(255, 255, 255));
        btnTienTraLai.setText("CHANGE");
        btnTienTraLai.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnTienTraLaiMouseClicked(evt);
            }
        });

        lblTienTraLai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        lblTienUUdAI.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTienUUdAI.setForeground(new java.awt.Color(255, 0, 0));

        javax.swing.GroupLayout pnlThongTinLayout = new javax.swing.GroupLayout(pnlThongTin);
        pnlThongTin.setLayout(pnlThongTinLayout);
        pnlThongTinLayout.setHorizontalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addComponent(jSeparator4)
                        .addContainerGap())
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addContainerGap())
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTittlePnlThongTin)
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 151, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnThem)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnSua)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnXoa)))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTittleUuDai)
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addGap(19, 19, 19)
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTienUUdAI, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(lblUuDai, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinLayout.createSequentialGroup()
                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblTittleMaHD)
                                            .addComponent(lblTittleSoLuong)
                                            .addComponent(lblTittleMaSP))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinLayout.createSequentialGroup()
                                        .addGap(0, 9, Short.MAX_VALUE)
                                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinLayout.createSequentialGroup()
                                                .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(18, 18, 18))
                                            .addComponent(lblMaSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                .addGap(18, 18, 18)
                                .addComponent(lblAnhSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, pnlThongTinLayout.createSequentialGroup()
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel2)
                                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                                        .addGap(15, 15, 15)
                                        .addComponent(btnTienTraLai, javax.swing.GroupLayout.PREFERRED_SIZE, 89, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(24, 24, 24)
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTienTraLai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(txtTienKhachDua))))
                        .addGap(15, 15, 15))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinLayout.createSequentialGroup()
                        .addComponent(rdoTienMat)
                        .addGap(40, 40, 40)
                        .addComponent(rdoChuyenKhoan)
                        .addGap(49, 49, 49))
                    .addComponent(jSeparator6)))
        );
        pnlThongTinLayout.setVerticalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTittlePnlThongTin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addComponent(lblTittleMaHD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblTittleMaSP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(lblAnhSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTittleSoLuong)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnXoa, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnThem, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSua, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(lblTittleUuDai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblUuDai, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTienUUdAI, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtTienKhachDua, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblTienTraLai, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTienTraLai))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(rdoTienMat)
                    .addComponent(rdoChuyenKhoan))
                .addGap(18, 18, 18))
        );

        lblTime.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTime.setForeground(new java.awt.Color(255, 255, 255));

        pnlMenu.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        pnlMenu.setForeground(new java.awt.Color(118, 142, 163));

        lblTittlePnlMenu.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTittlePnlMenu.setForeground(new java.awt.Color(31, 51, 86));
        lblTittlePnlMenu.setText("MENU:");

        javax.swing.GroupLayout pnlMenuLayout = new javax.swing.GroupLayout(pnlMenu);
        pnlMenu.setLayout(pnlMenuLayout);
        pnlMenuLayout.setHorizontalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator2)
                    .addGroup(pnlMenuLayout.createSequentialGroup()
                        .addComponent(lblTittlePnlMenu)
                        .addGap(0, 712, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlMenuLayout.setVerticalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTittlePnlMenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        cbxLoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TẤT CẢ", "CAFE", "BÁNH NGỌT", "NƯỚC ÉP" }));

        btnFilter.setBackground(new java.awt.Color(31, 51, 86));
        btnFilter.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnFilter.setForeground(new java.awt.Color(255, 255, 255));
        btnFilter.setText("FILTER");
        btnFilter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFilterActionPerformed(evt);
            }
        });

        jPanel1.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));

        txtArea.setColumns(20);
        txtArea.setRows(5);
        jScrollPane1.setViewportView(txtArea);

        btnAdd.setBackground(new java.awt.Color(31, 51, 86));
        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("ADD");
        btnAdd.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnAddMouseClicked(evt);
            }
        });
        btnAdd.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                btnAddKeyPressed(evt);
            }
        });

        btnClear.setBackground(new java.awt.Color(31, 51, 86));
        btnClear.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnClear.setForeground(new java.awt.Color(255, 255, 255));
        btnClear.setText("CLEAR");
        btnClear.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnClearMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(62, 62, 62)
                .addComponent(btnAdd)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 114, Short.MAX_VALUE)
                .addComponent(btnClear)
                .addGap(71, 71, 71))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 502, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnAdd)
                    .addComponent(btnClear))
                .addGap(15, 15, 15))
        );

        btnFind.setBackground(new java.awt.Color(31, 51, 86));
        btnFind.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnFind.setForeground(new java.awt.Color(255, 255, 255));
        btnFind.setText("FIND");
        btnFind.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnFindMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlBanHangLayout = new javax.swing.GroupLayout(pnlBanHang);
        pnlBanHang.setLayout(pnlBanHangLayout);
        pnlBanHangLayout.setHorizontalGroup(
            pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBanHangLayout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                        .addComponent(pnlHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(27, 27, 27)
                        .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(pnlBanHangLayout.createSequentialGroup()
                                .addComponent(cbxLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 243, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(37, 37, 37)
                                .addComponent(btnFilter, javax.swing.GroupLayout.PREFERRED_SIZE, 93, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(61, 61, 61)
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(34, 34, 34)
                                .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(134, 134, 134)
                                .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlBanHangLayout.createSequentialGroup()
                                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                                        .addComponent(pnlThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(30, 30, 30)
                                        .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                                        .addComponent(pnlChiTietHoaDon7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGap(29, 29, 29)
                                        .addComponent(pnlUuDai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(7, 7, 7)))
                        .addContainerGap(39, Short.MAX_VALUE))))
        );
        pnlBanHangLayout.setVerticalGroup(
            pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBanHangLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                        .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(txtTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnFind, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(cbxLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(btnFilter)))
                        .addGap(18, 18, 18)
                        .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(pnlMenu, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(pnlThongTin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(21, 21, 21)
                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlHoaDon, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlChiTietHoaDon7, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlUuDai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(pnlBanHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(pnlBanHang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblChiTietHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblChiTietHoaDonMouseClicked
        // TODO add your handling code here:
        showDetailsCTHD();
    }//GEN-LAST:event_tblChiTietHoaDonMouseClicked

    private void tblUuDaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblUuDaiMouseClicked
        // TODO add your handling code here:
        showDetailsUuDai();
    }//GEN-LAST:event_tblUuDaiMouseClicked

    private void btnApDungActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnApDungActionPerformed
        // TODO add your handling code here:
        uuDai();
    }//GEN-LAST:event_btnApDungActionPerformed

    private void btnHuyUuDaiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyUuDaiActionPerformed
        // TODO add your handling code here:
        xoaUuDai();
    }//GEN-LAST:event_btnHuyUuDaiActionPerformed

    private void btnThanhToanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThanhToanActionPerformed
        // TODO add your handling code here:

    }//GEN-LAST:event_btnThanhToanActionPerformed

    private void btnHuyDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHuyDonActionPerformed
        // TODO add your handling code here:
        deleteHD();
    }//GEN-LAST:event_btnHuyDonActionPerformed

    private void tblHoaDonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblHoaDonMouseClicked
        // TODO add your handling code here:
        showDetailsHDCho();
    }//GEN-LAST:event_tblHoaDonMouseClicked

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        if (!validateForm()) {
            return;
        } else {
            addSP();
        }
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        if (!validateForm()) {
            return;
        } else {
            updateSP();
        }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        deleteSP();
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnTaoHoaDonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoHoaDonActionPerformed
        // TODO add your handling code here:
        createMaHD();
    }//GEN-LAST:event_btnTaoHoaDonActionPerformed

    private void lblAnhSanPhamMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblAnhSanPhamMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_lblAnhSanPhamMouseClicked

    private void btnFilterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFilterActionPerformed
        // TODO add your handling code here:
        fillTableMenu();
    }//GEN-LAST:event_btnFilterActionPerformed

    private void btnThanhToanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnThanhToanMouseClicked
        // TODO add your handling code here:
        if (!validatePayment()) {
            return;
        } else {
            payment();
        }
    }//GEN-LAST:event_btnThanhToanMouseClicked

    private void btnFindMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnFindMouseClicked
        // TODO add your handling code here:
        if (!validateFindSP()) {
            return;
        } else {
            fillTableMenu();
        }
    }//GEN-LAST:event_btnFindMouseClicked

    private void btnTienTraLaiMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnTienTraLaiMouseClicked
        // TODO add your handling code here:
        if (!validateTienKhachDua()) {
            return;
        } else {
            Change();
        }
    }//GEN-LAST:event_btnTienTraLaiMouseClicked

    private void rdoTienMatMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoTienMatMouseClicked
        // TODO add your handling code here:
        showdetailPTTT();
    }//GEN-LAST:event_rdoTienMatMouseClicked

    private void rdoChuyenKhoanKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_rdoChuyenKhoanKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_rdoChuyenKhoanKeyPressed

    private void rdoChuyenKhoanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_rdoChuyenKhoanMouseClicked
        // TODO add your handling code here:
        showdetailPTTT();
    }//GEN-LAST:event_rdoChuyenKhoanMouseClicked

    private void btnAddKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_btnAddKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAddKeyPressed

    private void btnAddMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnAddMouseClicked
        // TODO add your handling code here:
        if (!validateAddHD()) {
            return;
        } else {
            addHD();
        }
    }//GEN-LAST:event_btnAddMouseClicked

    private void btnClearMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnClearMouseClicked
        // TODO add your handling code here:
        clear();
    }//GEN-LAST:event_btnClearMouseClicked

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
            java.util.logging.Logger.getLogger(QuanLyBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLyBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLyBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLyBanHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLyBanHang().setVisible(true);
            }
        });
    }

    public JPanel getPanelQLBH() {
        return pnlBanHang;
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnApDung;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnFilter;
    private javax.swing.JButton btnFind;
    private javax.swing.JButton btnHuyDon;
    private javax.swing.JButton btnHuyUuDai;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTaoHoaDon;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTienTraLai;
    private javax.swing.JButton btnXoa;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JComboBox<String> cbxLoc;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JLabel lblAnhSanPham;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblMaSP;
    private javax.swing.JLabel lblTienTraLai;
    private javax.swing.JLabel lblTienUUdAI;
    private javax.swing.JLabel lblTime;
    private javax.swing.JLabel lblTittleMaHD;
    private javax.swing.JLabel lblTittleMaSP;
    private javax.swing.JLabel lblTittlePnlChiTietHoaDon7;
    private javax.swing.JLabel lblTittlePnlHoaDon;
    private javax.swing.JLabel lblTittlePnlMenu;
    private javax.swing.JLabel lblTittlePnlThongTin;
    private javax.swing.JLabel lblTittlePnlUuDai;
    private javax.swing.JLabel lblTittleSoLuong;
    private javax.swing.JLabel lblTittleUuDai;
    private javax.swing.JLabel lblUuDai;
    private javax.swing.JPanel pnlBanHang;
    private javax.swing.JPanel pnlChiTietHoaDon7;
    private javax.swing.JPanel pnlHoaDon;
    private javax.swing.JPanel pnlMenu;
    private javax.swing.JPanel pnlThongTin;
    private javax.swing.JPanel pnlUuDai;
    private javax.swing.JRadioButton rdoChuyenKhoan;
    private javax.swing.JRadioButton rdoTienMat;
    private javax.swing.JTable tblChiTietHoaDon;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblUuDai;
    private javax.swing.JTextArea txtArea;
    private javax.swing.JTextField txtSoLuong;
    private javax.swing.JTextField txtTienKhachDua;
    private javax.swing.JTextField txtTimKiem;
    // End of variables declaration//GEN-END:variables
}
