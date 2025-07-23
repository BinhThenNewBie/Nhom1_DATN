/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package view;

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
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
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

/**
 *
 * @author ADMIN
 */
public class QuanLyBanHang extends javax.swing.JFrame {

    DefaultTableModel modelUuDai;
    DefaultTableModel modelHDCho;
    DefaultTableModel modelCTHD;
    ChiTietHoaDon cthd = new ChiTietHoaDon();
    UuDaiDAO udd = new UuDaiDAO();
    HoaDonDAO hdd = new HoaDonDAO();
    SanPhamDAO spd = new SanPhamDAO();
    String strAnh = "";

    /**
     * Creates new form StaffBanHang
     */
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

    public void initTable() {
        modelUuDai = new DefaultTableModel();
        String[] colsUuDai = new String[]{"Giá trị", "Áp dụng HD trên"};
        modelUuDai.setColumnIdentifiers(colsUuDai);
        tblUuDai.setModel(modelUuDai);

        modelHDCho = new DefaultTableModel();
        String[] colsHDCho = new String[]{"ID_Hóa Đơn", "Tổng tiền"};
        modelHDCho.setColumnIdentifiers(colsHDCho);
        tblHoaDon.setModel(modelHDCho);

        modelCTHD = new DefaultTableModel();
        String[] cols = new String[]{"ID_Sản Phẩm", "Tên sản phẩm", "Giá sản phẩm", "Số Lượng"};
        modelCTHD.setColumnIdentifiers(cols);
        tblChiTietHoaDon.setModel(modelCTHD);
    }

    public void fillTableHDCho() {
        modelHDCho.setRowCount(0);
        List<HoaDon> list = hdd.getALL();
        for (HoaDon hdc : list) {
            String trangThai = hdc.getTrangThai();
            if (trangThai.equalsIgnoreCase("Chưa thanh toán")) {
                modelHDCho.addRow(new Object[]{
                    hdc.getID_HD(),
                    formatVND(hdc.getTongTien())
                });
            }
        }
    }

    public void fillTableUuDai() {
        modelUuDai.setRowCount(0);
        for (UuDai ud : udd.getAll()) {
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
        List<ChiTietHoaDon> lstcthd = hdd.getAllID_HD(ID_HD);
        for (ChiTietHoaDon cthd : lstcthd) {
            model.addRow(new Object[]{
                cthd.getID_SP(),
                cthd.getTenSP(),
                formatVND(cthd.getGiaSP()),
                cthd.getSoLuong()
            });
        }
    }

    public void fillTableMenu() {
        // Xoá tất cả các component con khỏi panel menu để cbi làm mới
        pnlMenu.removeAll();

        // Kích thước của một sản phẩm
        int itemWidth = 143;
        int itemHeight = 200;

        // ContentPanel: Panel chúa tất cả item sản phẩm
        // GridBagLayout: Layout cho phép xếp itém theo dạng linh hoạt
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        //Tạo khoảng cách 10px ở 4 phía
        contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // GridBagConstraints: Đối tượng điều khiển cách các component được đặt trong GridBagLayout
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        // Căn itém về phía trên-trái
        gbc.anchor = GridBagConstraints.NORTHWEST;

        String loai = cbxLoc.getSelectedItem().toString().trim();
        List<SanPham> list = spd.getAll();
        if (!loai.equalsIgnoreCase("TẤT CẢ")) {
            list = list.stream()
                    .filter(sp -> sp.getLoaiSanPham().equalsIgnoreCase(loai))
                    .collect(Collectors.toList());
        }
        //filter để lọc, collect để thu thập kết quả

        int col = 0;
        int row = 0;
        for (SanPham sp : list) {
            if (sp.getTrangThai() != 1) {
                continue;
            }

            JPanel panel = new JPanel();
            // Xếp các thành phần theo chiều dọc
            panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
            // Đặt kích thước 140x200px
            panel.setPreferredSize(new Dimension(itemWidth, itemHeight));
            panel.setBackground(Color.WHITE);
            // Viền đen
            panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
            // Khoảng trống 5px phía trên
            panel.add(Box.createVerticalStrut(5));

            // SwingConstants.CENTER: Căn giữa text
            JLabel lblMa = new JLabel(sp.getIDSanPham(), SwingConstants.CENTER);
            lblMa.setFont(new Font("Segoe UI", Font.BOLD, 16));
            // CENTER_ALIGMENT: Căn giữa trong BoxLayout
            lblMa.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblMa.setPreferredSize(new Dimension(itemWidth, 35));

            JLabel lblImage = new JLabel("", SwingConstants.CENTER);
            lblImage.setAlignmentX(Component.CENTER_ALIGNMENT);
            lblImage.setMaximumSize(new Dimension(100, 100));
            lblImage.setPreferredSize(new Dimension(100, 100));
            try {
                ImageIcon icon = new ImageIcon("src/Images_SanPham/" + sp.getIMG());
                Image img = icon.getImage();
                // SCALE_SMOOTH: resize ảnh mượt mà
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
            // Nếu tên > 15 ký tự, cắt còn 20 ký tự + "..." 
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
                // Di chuột vào nền xám nhạt viền xanh
                @Override
                public void mouseEntered(MouseEvent e) {
                    panel.setBackground(new Color(240, 240, 240));
                    panel.setBorder(BorderFactory.createLineBorder(new Color(0, 120, 150), 2));
                }

                // khi di chuột ra nền trắng viền đen
                @Override
                public void mouseExited(MouseEvent e) {
                    panel.setBackground(Color.WHITE);
                    panel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
                }

                // showdetail khi ấn vào sản phẩm
                @Override
                public void mouseClicked(MouseEvent e) {
                    showDetail(sp);
                }
            });
            // Đổi con trỏ thành hình bàn tay
            panel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            // Vị trí trên grid
            gbc.gridx = col;
            gbc.gridy = row;
            contentPanel.add(panel, gbc);

            col++;
            if (col >= 5) {
                col = 0;
                row++;
            }
        }
        // Thêm panel trắng để đủ dòng cuối
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

        // LƯU KÍCH THƯỚC GỐC CỦA PANEL
        Dimension originalSize = pnlMenu.getSize();
        Point originalLocation = pnlMenu.getLocation();

        JScrollPane scrollPane = new JScrollPane(contentPanel);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        scrollPane.setBorder(null);

        // CỐ ĐỊNH KÍCH THƯỚC SCROLLPANE
        scrollPane.setPreferredSize(originalSize);
        scrollPane.setMinimumSize(originalSize);
        scrollPane.setMaximumSize(originalSize);

        // CỐ ĐỊNH LAYOUT VÀ KÍCH THƯỚC PANEL
        pnlMenu.setLayout(new BorderLayout());
        pnlMenu.setPreferredSize(originalSize);
        pnlMenu.setMinimumSize(originalSize);
        pnlMenu.setMaximumSize(originalSize);
        pnlMenu.setSize(originalSize);
        pnlMenu.setBounds(originalLocation.x, originalLocation.y, originalSize.width, originalSize.height);

        pnlMenu.add(scrollPane, BorderLayout.CENTER);

        // CỐ ĐỊNH VỊ TRÍ VÀ CUỘN VỀ ĐẦU
        SwingUtilities.invokeLater(() -> {
            pnlMenu.setBounds(originalLocation.x, originalLocation.y, originalSize.width, originalSize.height);
            scrollPane.getVerticalScrollBar().setValue(0);
            scrollPane.getHorizontalScrollBar().setValue(0);
        });

        pnlMenu.revalidate();
        pnlMenu.repaint();

    }

    public void updateGiaSP() {
        List<HoaDon> lsthdc = hdd.getALL();
        for (HoaDon hdc : lsthdc) {
            String ID_HD = hdc.getID_HD();
            List<ChiTietHoaDon> lstcthd = hdd.getAllID_HD(ID_HD);
            float tong = 0;

            for (ChiTietHoaDon cthd : lstcthd) {
                float giaMoi = spd.getGiaByID(cthd.getID_SP());
                // Cập nhật giá mới cho sản phẩm trong hóa đơn
                hdd.UpdateGia(ID_HD, cthd.getID_SP(), giaMoi);
                tong += giaMoi * cthd.getSoLuong();
            }

            // Áp dụng ưu đãi nếu có
            String uuDai = hdc.getUuDai();
            float tongSauUuDai = tong;

            if (!uuDai.equals("0%")) {
                try {
                    float giam = Float.parseFloat(uuDai.replace("%", "").trim());
                    if (giam > 0) {
                        tongSauUuDai = tong * (1 - giam / 100);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

            // Cập nhật lại tổng tiền cho hóa đơn này
            hdd.updateTongTien(ID_HD, tongSauUuDai);
        }

        fillTableHDCho(); // Sau khi cập nhật giá và tổng tiền thì hiển thị lại
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
                int fixedWidth = 156;  // Kích thước cố định từ layout
                int fixedHeight = 156; // Kích thước cố định (có thể điều chỉnh tỷ lệ)

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

            List<HoaDon> listHD = hdd.getALLID_hoadon(ID_HD);
            if (!listHD.isEmpty()) {
                String uudai = listHD.get(0).getUuDai();
                lblUuDai.setText(uudai + " Được áp dụng");
            } else {
                lblUuDai.setText("Không có ưu đãi");
            }

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

        ChiTietHoaDon cthd = hdd.selectCTHD(maHD, maSP);
        if (cthd == null) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy chi tiết hóa đơn!");
            return;
        }

        // Set dữ liệu lên giao diện
        lblMaHD.setText(cthd.getID_HD());
        lblMaSP.setText(cthd.getID_SP());
        txtSoLuong.setText(String.valueOf(cthd.getSoLuong()));

        // Lấy ảnh sản phẩm từ mã SP
        List<SanPham> spList = spd.getAllID_SP(maSP);
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

    private String generateMaHD() {
        Random rnd = new Random();
        String maHD;
        do {
            int number = 100000 + rnd.nextInt(900000);
            maHD = "HD" + number;
        } while (hdd.existsMaHD(maHD));
        return maHD;
    }

    public void createMaHD() {
        hdd.clearOrderTemp();

        DefaultTableModel model = (DefaultTableModel) tblChiTietHoaDon.getModel();
        String newMaHD = generateMaHD();
        lblMaHD.setText(newMaHD);

        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");
        Date now = new Date();

        String ngayThangNam = sdfDate.format(now);
        String thoiGian = sdfTime.format(now);
        float tongTien = 0.0f;
        String uuDai = "0%";
        String trangThai = "Chưa thanh toán";
        HoaDon hd = new HoaDon(newMaHD, ngayThangNam, thoiGian, tongTien, uuDai, trangThai);
        hdd.SaveHDCHO(hd);

        lblMaSP.setText("");
        lblUuDai.setText("");
        model.setRowCount(0);
        fillTableHDCho();
    }

    public void addSP() {
        String ID_SP = lblMaSP.getText();
        String ID_HD = lblMaHD.getText();
        int soLuong = Integer.parseInt(txtSoLuong.getText());

        if (ID_HD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "CHƯA CÓ HÓA ĐƠN");
            return;
        }

        ChiTietHoaDon cthdCu = hdd.selectCTHD(ID_HD, ID_SP);
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
            hdd.UpdateSP(ID_HD, ID_SP, cthdCu);

        } else {
            if (soLuong > 50) {
                JOptionPane.showMessageDialog(this,
                        "Bạn chỉ có thể thêm tối đa 50 sản phẩm.\n"
                        + "Bạn đang cố thêm: " + soLuong + " > 50");
                return;
            }

            for (SanPham sp : spd.getAll()) {
                if (sp.getIDSanPham().equals(ID_SP)) {
                    ChiTietHoaDon cthd = new ChiTietHoaDon();
                    cthd.setID_HD(ID_HD);
                    cthd.setID_SP(sp.getIDSanPham());
                    cthd.setTenSP(sp.getTenSanPham());
                    cthd.setGiaSP(sp.getGiaTien());
                    cthd.setSoLuong(soLuong);
                    hdd.SaveCTHD(cthd);
                    break;
                }
            }
        }

        float tong = 0;
        List<ChiTietHoaDon> list = hdd.getAllID_HD(ID_HD);
        for (ChiTietHoaDon ct : list) {
            tong += ct.getGiaSP() * ct.getSoLuong();
        }

        String uuDai = hdd.getALL().stream()
                .filter(hd -> hd.getID_HD().equals(ID_HD))
                .findFirst()
                .map(hd -> hd.getUuDai())
                .orElse("0%");

        float tongSauUuDai = tong;
        if (!uuDai.equals("0%")) {
            try {
                float giam = Float.parseFloat(uuDai.replace("%", "").trim());
                tongSauUuDai = tong * (1 - giam / 100);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        hdd.updateTongTien(ID_HD, tongSauUuDai);
        fillTableHDCho();
        fillTableCTHD();
    }

    public void deleteHD() {
        int i = tblHoaDon.getSelectedRow();
        if (i >= 0) {
            int choose = JOptionPane.showConfirmDialog(this, "XÁC NHẬN", "BẠN MUỐN HỦY", JOptionPane.YES_NO_OPTION);
            if (choose == JOptionPane.YES_OPTION) {
                String ID_HD = tblHoaDon.getValueAt(i, 0).toString();
                int res1 = hdd.DeleteCTHD(ID_HD);
                int res2 = hdd.DeleteHD(ID_HD);
                if (res1 == 1 && res2 == 1) {
                    fillTableHDCho();
                    lblMaHD.setText("");
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

        String ID_HD = lblMaHD.getText();

        //Kiểm tra ưu đãi trước khi xoá
        String uuDai = hdd.getALL().stream()
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

        List<ChiTietHoaDon> listSauXoa = hdd.getAllID_HD(ID_HD);
        listSauXoa.removeIf(ct -> ct.getID_SP().equals(ID_SP));
        float tong = 0;
        for (ChiTietHoaDon ct : listSauXoa) {
            tong += ct.getGiaSP() * ct.getSoLuong();
        }

        int result = hdd.DeleteSP(ID_SP, ID_HD);
        if (result == 1) {
            hdd.updateTongTien(ID_HD, tong);
            fillTableHDCho();
            fillTableCTHD();
            JOptionPane.showMessageDialog(this, "Xoá thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Xoá không thành công!");
        }
    }

    public void updateSP() {
        int i = tblChiTietHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa");
            return;
        }

        String ID_HD = lblMaHD.getText();

        //Kiểm tra ưu đãi trước khi sửa
        String uuDai = hdd.getALL().stream()
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
        List<ChiTietHoaDon> listSauSua = hdd.getAllID_HD(ID_HD);
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

        int result = hdd.UpdateSP(ID_HD, ID_SP, cthd);
        if (result == 1) {
            hdd.updateTongTien(ID_HD, tong);
            fillTableHDCho();
            fillTableCTHD();
            JOptionPane.showMessageDialog(this, "Sửa thành công!");
        } else {
            JOptionPane.showMessageDialog(this, "Sửa không thành công!");
        }
    }

    public boolean validateForm() {
        String maHD = lblMaHD.getText().trim();
        String maSP = lblMaSP.getText().trim();
        String soLuongStr = txtSoLuong.getText().trim();

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

    public static float unformatCurrency(String formatted) {
        String clean = formatted.replaceAll("[^\\d.]", "");
        return Float.parseFloat(clean);
    }

    public void uuDai() {
        int rowUuDai = tblUuDai.getSelectedRow();
        int rowHoaDon = tblHoaDon.getSelectedRow();

        if (rowHoaDon < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hoá đơn để áp dụng ưu đãi.");
            return;
        }

        String ID_HD = tblHoaDon.getValueAt(rowHoaDon, 0).toString().trim(); // lấy đúng từ bảng
        if (ID_HD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Mã hoá đơn không hợp lệ!");
            return;
        }

        List<HoaDon> dsHoaDon = hdd.getALLID_hoadon(ID_HD);
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
        String giaTriUuDai = tblUuDai.getValueAt(rowUuDai, 0).toString(); // ví dụ "10%"
        float dieuKien = unformatCurrency(tblUuDai.getValueAt(rowUuDai, 1).toString()); // ví dụ: 200000
        float tongTienHD = unformatCurrency(tblHoaDon.getValueAt(rowHoaDon, 1).toString());

        if (tongTienHD < dieuKien) {
            JOptionPane.showMessageDialog(this, "Không đủ điều kiện để áp dụng ưu đãi này.");
            return;
        }

        // Tính lại tổng tiền chi tiết hóa đơn
        List<ChiTietHoaDon> chiTiet = hdd.getAllID_HD(ID_HD);
        float tongGoc = 0;
        for (ChiTietHoaDon ct : chiTiet) {
            tongGoc += ct.getGiaSP() * ct.getSoLuong();
        }

        float phanTramUuDai = Float.parseFloat(giaTriUuDai.replace("%", "").trim());
        float tongSauUuDai = tongGoc * (1 - phanTramUuDai / 100f);

        // Cập nhật DB
        hdd.updateUuDai(ID_HD, giaTriUuDai);
        hdd.updateTongTien(ID_HD, tongSauUuDai);
        lblUuDai.setText(giaTriUuDai + " Được áp dụng");
        fillTableHDCho();
    }

    public void xoaUuDai() {
        String ID_HD = lblMaHD.getText();
        if (ID_HD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để xoá ưu đãi");
            return;
        }
        float tong = 0;
        List<ChiTietHoaDon> list = hdd.getAllID_HD(ID_HD);
        for (ChiTietHoaDon ct : list) {
            tong += ct.getGiaSP() * ct.getSoLuong();
        }
        lblUuDai.setText("");
        hdd.updateUuDai(ID_HD, "0%");
        hdd.updateTongTien(ID_HD, tong);
        fillTableHDCho();
    }

    public void thanhToan() {
        int i = tblHoaDon.getSelectedRow();
        if (i < 0) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn hóa đơn để thanh toán");
            return;
        }
        if (tblChiTietHoaDon.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "Không có sản phẩm nào trong hoá đơn!");
            return;
        }
        String ID_HD = lblMaHD.getText();
        List<HoaDon> listHD = hdd.getALLID_hoadon(ID_HD);
        if (listHD.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy hóa đơn!");
            return;
        }
        String thoiGian = listHD.get(0).getThoiGian();
        String ngayThangNam = listHD.get(0).getNgayThangNam();
        String uuDai = listHD.get(0).getUuDai();
        String trangThai = "Đã thanh toán";
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

        // Tạo nội dung hóa đơn
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

        JTextArea textArea = new JTextArea(sb.toString());
        textArea.setEditable(false);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 13));
        textArea.setBackground(Color.WHITE);
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(400, 500));

        if (JOptionPane.showConfirmDialog(this, scrollPane, "Xác nhận thanh toán",
                JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION) {
            return;
        }
        int update = hdd.updatetrangThai(ID_HD, trangThai);
        if (update == 1) {
            int print = JOptionPane.showConfirmDialog(this, "Bạn có muốn xuất hóa đơn không?", "In hóa đơn",
                    JOptionPane.YES_NO_OPTION);
            if (print == JOptionPane.YES_OPTION) {
                try {
                    textArea.print();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Lỗi khi in hóa đơn: " + ex.getMessage());
                }
            }

            fillTableHDCho();
            modelCTHD.setRowCount(0);
            lblMaHD.setText("");
            lblMaSP.setText("");
            lblUuDai.setText("");
            txtSoLuong.setText("");
            JOptionPane.showMessageDialog(this, "Thanh toán thành công!", "Xác nhận", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Thanh toán thất bại!", "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

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
        btnTaoHoaDon = new javax.swing.JButton();
        lblAnhSanPham = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        pnlMenu = new javax.swing.JPanel();
        lblTittlePnlMenu = new javax.swing.JLabel();
        jSeparator2 = new javax.swing.JSeparator();
        cbxLoc = new javax.swing.JComboBox<>();
        btnLoc = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        pnlBanHang.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        pnlChiTietHoaDon7.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblTittlePnlChiTietHoaDon7.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTittlePnlChiTietHoaDon7.setForeground(new java.awt.Color(31, 51, 86));
        lblTittlePnlChiTietHoaDon7.setText("CHI TIẾT HÓA ĐƠN:");

        tblChiTietHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
                    .addComponent(jScrollPane11, javax.swing.GroupLayout.DEFAULT_SIZE, 763, Short.MAX_VALUE))
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
        lblTittlePnlUuDai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTittlePnlUuDai.setForeground(new java.awt.Color(31, 51, 86));
        lblTittlePnlUuDai.setText("ƯU ĐÃI:");

        tblUuDai.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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
        btnApDung.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnApDung.setForeground(new java.awt.Color(255, 255, 255));
        btnApDung.setText("ÁP DỤNG");
        btnApDung.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnApDungActionPerformed(evt);
            }
        });

        btnHuyUuDai.setBackground(new java.awt.Color(31, 51, 86));
        btnHuyUuDai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHuyUuDai.setForeground(new java.awt.Color(255, 0, 0));
        btnHuyUuDai.setText("HUỶ");
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
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(pnlUuDaiLayout.createSequentialGroup()
                                .addComponent(lblTittlePnlUuDai)
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(pnlUuDaiLayout.createSequentialGroup()
                        .addGap(0, 11, Short.MAX_VALUE)
                        .addComponent(btnApDung)
                        .addGap(18, 18, 18)
                        .addComponent(btnHuyUuDai)
                        .addGap(16, 16, 16))))
        );
        pnlUuDaiLayout.setVerticalGroup(
            pnlUuDaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlUuDaiLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTittlePnlUuDai)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 193, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlUuDaiLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnApDung)
                    .addComponent(btnHuyUuDai))
                .addContainerGap(12, Short.MAX_VALUE))
        );

        pnlHoaDon.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblTittlePnlHoaDon.setBackground(new java.awt.Color(31, 51, 86));
        lblTittlePnlHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTittlePnlHoaDon.setForeground(new java.awt.Color(31, 51, 86));
        lblTittlePnlHoaDon.setText("HÓA ĐƠN:");

        btnThanhToan.setBackground(new java.awt.Color(31, 51, 86));
        btnThanhToan.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnThanhToan.setForeground(new java.awt.Color(255, 255, 255));
        btnThanhToan.setText("THANH TOÁN");
        btnThanhToan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThanhToanActionPerformed(evt);
            }
        });

        btnHuyDon.setBackground(new java.awt.Color(31, 51, 86));
        btnHuyDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnHuyDon.setForeground(new java.awt.Color(255, 0, 0));
        btnHuyDon.setText("HỦY ĐƠN");
        btnHuyDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHuyDonActionPerformed(evt);
            }
        });

        tblHoaDon.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
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

        javax.swing.GroupLayout pnlHoaDonLayout = new javax.swing.GroupLayout(pnlHoaDon);
        pnlHoaDon.setLayout(pnlHoaDonLayout);
        pnlHoaDonLayout.setHorizontalGroup(
            pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlHoaDonLayout.createSequentialGroup()
                        .addComponent(lblTittlePnlHoaDon)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(pnlHoaDonLayout.createSequentialGroup()
                        .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane4, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                            .addGroup(pnlHoaDonLayout.createSequentialGroup()
                                .addComponent(btnThanhToan)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnHuyDon)))
                        .addGap(30, 30, 30))))
        );
        pnlHoaDonLayout.setVerticalGroup(
            pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHoaDonLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTittlePnlHoaDon)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(10, 10, 10)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlHoaDonLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThanhToan)
                    .addComponent(btnHuyDon))
                .addContainerGap(15, Short.MAX_VALUE))
        );

        pnlThongTin.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        lblTittlePnlThongTin.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
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
        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setText("THÊM");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(31, 51, 86));
        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setText("SỬA");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(31, 51, 86));
        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText("XÓA");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        lblTittleUuDai.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lblTittleUuDai.setText("ƯU ĐÃI:");

        lblUuDai.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblUuDai.setForeground(new java.awt.Color(255, 0, 0));

        btnTaoHoaDon.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnTaoHoaDon.setForeground(new java.awt.Color(255, 0, 0));
        btnTaoHoaDon.setText("TẠO HÓA ĐƠN");
        btnTaoHoaDon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoHoaDonActionPerformed(evt);
            }
        });

        lblAnhSanPham.setBackground(new java.awt.Color(118, 142, 163));
        lblAnhSanPham.setForeground(new java.awt.Color(118, 142, 163));
        lblAnhSanPham.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(31, 51, 86), 5));
        lblAnhSanPham.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhSanPhamMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout pnlThongTinLayout = new javax.swing.GroupLayout(pnlThongTin);
        pnlThongTin.setLayout(pnlThongTinLayout);
        pnlThongTinLayout.setHorizontalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jSeparator1)
                            .addGroup(pnlThongTinLayout.createSequentialGroup()
                                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTittlePnlThongTin)
                                    .addComponent(lblTittleUuDai)
                                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                                        .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblTittleMaHD)
                                            .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addComponent(lblTittleSoLuong)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlThongTinLayout.createSequentialGroup()
                                                    .addGap(19, 19, 19)
                                                    .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                            .addComponent(lblTittleMaSP)
                                            .addComponent(lblMaSP, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblMaHD, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 190, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(lblAnhSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 159, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                                        .addGap(19, 19, 19)
                                        .addComponent(btnThem)
                                        .addGap(13, 13, 13)
                                        .addComponent(btnSua)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(btnXoa)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(lblUuDai, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnTaoHoaDon)
                        .addGap(21, 21, 21))))
        );
        pnlThongTinLayout.setVerticalGroup(
            pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlThongTinLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTittlePnlThongTin)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addComponent(lblTittleMaHD)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMaHD, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(23, 23, 23)
                        .addComponent(lblTittleMaSP)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(lblMaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 21, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(lblTittleSoLuong))
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addComponent(lblAnhSanPham, javax.swing.GroupLayout.PREFERRED_SIZE, 160, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtSoLuong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(17, 17, 17)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa))
                .addGap(18, 18, 18)
                .addGroup(pnlThongTinLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(pnlThongTinLayout.createSequentialGroup()
                        .addComponent(lblTittleUuDai)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUuDai, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnTaoHoaDon))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        lblTime.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTime.setForeground(new java.awt.Color(31, 51, 86));

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
                        .addGap(0, 790, Short.MAX_VALUE)))
                .addContainerGap())
        );
        pnlMenuLayout.setVerticalGroup(
            pnlMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlMenuLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTittlePnlMenu)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(350, Short.MAX_VALUE))
        );

        cbxLoc.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TẤT CẢ", "CAFE", "BÁNH NGỌT", "NƯỚC ÉP" }));

        btnLoc.setBackground(new java.awt.Color(31, 51, 86));
        btnLoc.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        btnLoc.setForeground(new java.awt.Color(255, 255, 255));
        btnLoc.setText("LỌC");
        btnLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout pnlBanHangLayout = new javax.swing.GroupLayout(pnlBanHang);
        pnlBanHang.setLayout(pnlBanHangLayout);
        pnlBanHangLayout.setHorizontalGroup(
            pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlBanHangLayout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                        .addComponent(pnlThongTin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(pnlBanHangLayout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(cbxLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 321, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(pnlBanHangLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(pnlMenu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                        .addComponent(pnlUuDai, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlChiTietHoaDon7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(pnlHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, 246, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(30, 30, 30))
        );
        pnlBanHangLayout.setVerticalGroup(
            pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBanHangLayout.createSequentialGroup()
                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(pnlBanHangLayout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlBanHangLayout.createSequentialGroup()
                        .addGap(9, 9, 9)
                        .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(cbxLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnLoc))))
                .addGap(18, 18, 18)
                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(pnlThongTin, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlMenu, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(34, 34, 34)
                .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(pnlHoaDon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(pnlBanHangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(pnlChiTietHoaDon7, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pnlUuDai, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBanHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(pnlBanHang, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
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
        thanhToan();
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

    private void btnLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocActionPerformed
        // TODO add your handling code here:
        fillTableMenu();
    }//GEN-LAST:event_btnLocActionPerformed

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
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
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
    private javax.swing.JButton btnApDung;
    private javax.swing.JButton btnHuyDon;
    private javax.swing.JButton btnHuyUuDai;
    private javax.swing.JButton btnLoc;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnTaoHoaDon;
    private javax.swing.JButton btnThanhToan;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JComboBox<String> cbxLoc;
    private javax.swing.JScrollPane jScrollPane11;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JLabel lblAnhSanPham;
    private javax.swing.JLabel lblMaHD;
    private javax.swing.JLabel lblMaSP;
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
    private javax.swing.JTable tblChiTietHoaDon;
    private javax.swing.JTable tblHoaDon;
    private javax.swing.JTable tblUuDai;
    private javax.swing.JTextField txtSoLuong;
    // End of variables declaration//GEN-END:variables
}
