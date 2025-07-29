package view;

import DAO.SanPhamDAO;
import Model.SanPham;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
/**
 *
 * @author Admin
 */
public class QuanLySanPham extends javax.swing.JFrame {

    List<SanPham> list = new ArrayList<>();
    DefaultTableModel tableModel = new DefaultTableModel();
    SanPhamDAO spDao = new SanPhamDAO();
    String strAnh = "";

    /**
     * Creates new form QuanLySanPham
     */
    public QuanLySanPham() {
        initComponents();
        // Set layout cho panel chính
        jPanelQLSP.setLayout(new BorderLayout(10, 10)); // 10px gap

        // Căn chỉnh panels
        jPanelQLSP.add(jPanel3, BorderLayout.NORTH);  // Panel form ở trên
        jPanelQLSP.add(jPanel1, BorderLayout.CENTER); // Panel table ở dưới

        // Set kích thước
        jPanel3.setPreferredSize(new Dimension(1240, 350)); // Panel trên cao 350px
        jPanel1.setPreferredSize(new Dimension(1240, 150)); // Panel dưới cao 630px

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

        loadTatCa();
        initTable();
        fillTable();
    }

    public void initTable() {
        String[] cols = new String[]{"ID SẢN PHẨM", "TÊN SẢN PHẨM", "GIÁ TIỀN", "LOẠI SẢN PHẨM", "ẢNH", "TRẠNG THÁI"};
        tableModel.setColumnIdentifiers(cols);
        tblBang.setModel(tableModel);

        tblBang.getColumnModel().getColumn(4).setMinWidth(0);
        tblBang.getColumnModel().getColumn(4).setMaxWidth(0);
        tblBang.getColumnModel().getColumn(4).setWidth(0);
    }

    public void fillTable() {
        tableModel.setRowCount(0);
        for (SanPham sp : spDao.getAll()) {
            tableModel.addRow(spDao.getRow(sp));
        }
    }

    public void showdetail() {
        int i = tblBang.getSelectedRow();
        if (i == -1 || list == null || list.isEmpty() || i >= list.size()) {
            return;
        }

        SanPham sp = list.get(i);

        lblID.setText(sp.getIDSanPham());
        txtTensp.setText(sp.getTenSanPham());
        txtGiatien.setText(String.valueOf(sp.getGiaTien()));
        cboLoai.setSelectedItem(sp.getLoaiSanPham());
        strAnh = sp.getIMG();

        // Xử lý hiển thị ảnh
        if (strAnh == null || strAnh.trim().isEmpty() || strAnh.equalsIgnoreCase("NO IMAGE")) {
            lblAnh.setText("Hình Ảnh Không tồn tại");
            lblAnh.setIcon(null);
        } else {
            try {
                String duongDanAnhDayDu = "src/Images_SanPham/" + strAnh;
                File file = new File(duongDanAnhDayDu);
                if (!file.exists()) {
                    lblAnh.setText("Không tìm thấy ảnh");
                    lblAnh.setIcon(null);
                    return;
                }
                ImageIcon icon = new ImageIcon(duongDanAnhDayDu);
                Image img = icon.getImage().getScaledInstance(lblAnh.getWidth(), lblAnh.getHeight(), Image.SCALE_SMOOTH);
                lblAnh.setIcon(new ImageIcon(img));
                lblAnh.setText("");
            } catch (Exception e) {
                lblAnh.setText("Ảnh Bị Lỗi");
                lblAnh.setIcon(null);
            }
        }

        // Xử lý trạng thái UI dựa vào trạng thái sản phẩm
        int trangThai = sp.getTrangThai();
        boolean isLocked = (trangThai == 0);

        // Cập nhật trạng thái các nút và field
        btnThemSP.setEnabled(!isLocked);
        btnMoKhoa.setEnabled(isLocked);
        btnSuaSP.setEnabled(!isLocked);
        btnLamMoiSP.setEnabled(!isLocked);
        btnTaoMa.setEnabled(!isLocked);
        khoaSP.setEnabled(!isLocked);
        btnTimKiem.setEnabled(!isLocked);
        btnLoc.setEnabled(!isLocked);

        // Cập nhật trạng thái các component input
        lblID.setEnabled(!isLocked);
        lblAnh.setEnabled(!isLocked);
        cboLoai.setEnabled(!isLocked);
        cboLocSP.setEnabled(!isLocked);
        txtTensp.setEnabled(!isLocked);
        txtGiatien.setEnabled(!isLocked);
        txtTimkiem.setEnabled(!isLocked);
    }

    public void loadTatCa() {
        list = spDao.getAll(); // Cập nhật list
        tableModel.setRowCount(0);

        for (SanPham sp : list) {
            tableModel.addRow(spDao.getRow(sp));
        }

        // Reset selection sau khi load
        if (tblBang.getRowCount() > 0) {
            tblBang.clearSelection();
        }
    }

    public void timKiemKetHop() {
        String tuKhoa = txtTimkiem.getText().trim();
        if (tuKhoa.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Vui lòng nhập ID hoặc tên để tìm kiếm!");
            return;
        }

        list = spDao.timKiemKetHop(tuKhoa);
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy sản phẩm theo ID hoặc tên!");
            // Nếu không tìm thấy, load lại tất cả
            loadTatCa();
        } else {
            loadTable(list);
            if (tblBang.getRowCount() > 0) {
                tblBang.setRowSelectionInterval(0, 0);
                showdetail();
            }
        }
    }

    public void locTheoLoai() {
        String loai = cboLocSP.getSelectedItem().toString();

        if (loai.equalsIgnoreCase("TẤT CẢ")) {
            loadTatCa();
        } else {
            list = spDao.locTheoLoai(loai);
            loadTable(list);
        }

        // Clear selection sau khi lọc
        if (tblBang.getRowCount() > 0) {
            tblBang.clearSelection();
        }
    }

    public void loadTable(List<SanPham> sanPhamList) {
        DefaultTableModel model = (DefaultTableModel) tblBang.getModel();
        model.setRowCount(0);
        for (SanPham sp : sanPhamList) {
            model.addRow(new Object[]{
                sp.getIDSanPham(),
                sp.getTenSanPham(),
                sp.getGiaTien(),
                sp.getLoaiSanPham(),
                sp.getIMG(),
                sp.getTrangThai() == 1 ? "ĐANG BÁN" : "ĐÃ KHOÁ"
            });
        }
    }

    public void khoaSanPham() {
        int i = tblBang.getSelectedRow();
        if (i == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần khóa!");
            return;
        }

        // Lấy sản phẩm từ danh sách hiện tại (list) thay vì gọi spDao.getAll()
        SanPham sp;
        if (list == null || list.isEmpty()) {
            // Nếu list rỗng thì load lại từ database
            list = spDao.getAll();
        }

        if (i >= list.size()) {
            JOptionPane.showMessageDialog(this, "Chỉ số không hợp lệ!");
            return;
        }

        sp = list.get(i);

        if (sp.getTrangThai() == 0) {
            JOptionPane.showMessageDialog(this, "Sản phẩm đã bị khóa!");
            return;
        }

        int chon = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn khóa sản phẩm: " + sp.getTenSanPham() + "?",
                "Xác Nhận Khóa", JOptionPane.YES_NO_OPTION);

        if (chon == JOptionPane.YES_OPTION) {
            boolean result = spDao.khoaSanPham(sp.getIDSanPham());
            if (result) {
                JOptionPane.showMessageDialog(this, "Khóa sản phẩm thành công!");
                // Cập nhật lại danh sách và bảng
                loadTatCa(); // Load lại toàn bộ dữ liệu
                // Giữ nguyên dòng được chọn nếu có thể
                if (i < tblBang.getRowCount()) {
                    tblBang.setRowSelectionInterval(i, i);
                    showdetail();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Khóa sản phẩm thất bại!");
            }
        }
    }

    public void moKhoaSanPham() {
        int i = tblBang.getSelectedRow();
        if (i == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần mở khóa!");
            return;
        }

        SanPham sp;

        // Kiểm tra xem đang ở chế độ tìm kiếm hay không
        String timKiem = txtTimkiem.getText().trim();

        if (timKiem.isEmpty()) {
            // Không có tìm kiếm - lấy từ list hiện tại
            if (list == null || list.isEmpty()) {
                list = spDao.getAll();
            }
            if (i >= list.size()) {
                JOptionPane.showMessageDialog(this, "Chỉ số không hợp lệ!");
                return;
            }
            sp = list.get(i);
        } else {
            // Có tìm kiếm - lấy từ kết quả tìm kiếm
            if (list == null || list.isEmpty() || i >= list.size()) {
                JOptionPane.showMessageDialog(this, "Không thể lấy thông tin sản phẩm!");
                return;
            }
            sp = list.get(i);
        }

        if (sp.getTrangThai() == 1) {
            JOptionPane.showMessageDialog(this, "Sản phẩm đang hoạt động!");
            return;
        }

        int chon = JOptionPane.showConfirmDialog(this,
                "Bạn có chắc muốn mở khóa sản phẩm: " + sp.getTenSanPham() + "?",
                "Xác Nhận Mở Khóa", JOptionPane.YES_NO_OPTION);

        if (chon == JOptionPane.YES_OPTION) {
            boolean result = spDao.moKhoaSanPham(sp.getIDSanPham());
            if (result) {
                JOptionPane.showMessageDialog(this, "Mở khóa sản phẩm thành công!");
                // Cập nhật lại danh sách và bảng
                if (timKiem.isEmpty()) {
                    loadTatCa(); // Load lại toàn bộ dữ liệu
                } else {
                    timKiemKetHop(); // Load lại kết quả tìm kiếm
                    return; // Thoát vì timKiemKetHop() đã xử lý selection
                }

                // Giữ nguyên dòng được chọn nếu có thể
                if (i < tblBang.getRowCount()) {
                    fillTable();
                    tblBang.setRowSelectionInterval(i, i);
                    showdetail();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Mở khóa sản phẩm thất bại!");
            }
        }
    }

    private boolean validatethemSanPham() {
        String id = lblID.getText().trim();
        String tenSP = txtTensp.getText().trim();
        String giaStr = txtGiatien.getText().trim();
        String loaiSP = (cboLoai.getSelectedItem() != null) ? cboLoai.getSelectedItem().toString().trim() : "";

        // Check mã sản phẩm
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhấn nút TẠO MÃ trước!");
            return false;
        }

        // ======= 1. Tên sản phẩm =======
        // 1.1 Không được để trống
        if (tenSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!");
            return false;
        }

        // 1.2 Không được chứa số
        if (tenSP.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được chứa số!");
            return false;
        }

        // 1.3 Không được chứa ký tự đặc biệt (chỉ cho phép chữ và khoảng trắng)
        if (!tenSP.matches("^[\\p{L}\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được chứa ký tự đặc biệt!");
            return false;
        }

        // 1.4 Không được trùng với tên sản phẩm đã có
        for (SanPham sp : spDao.getAll()) {
            if (tenSP.equalsIgnoreCase(sp.getTenSanPham())) {
                JOptionPane.showMessageDialog(this, "Tên sản phẩm đã tồn tại trong bảng!");
                return false;
            }
        }

        // ======= 2. Giá tiền =======
        // 2.1 Không được để trống
        if (giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá tiền không được để trống!");
            return false;
        }

        // 2.2 Không chứa chữ cái
        if (giaStr.matches(".*[a-zA-Z].*")) {
            JOptionPane.showMessageDialog(this, "Giá tiền không được chứa chữ cái!");
            return false;
        }

        // 2.3 Không chứa ký tự đặc biệt
        if (giaStr.matches(".*[~!@#$%^&()_=|<>?{}\\[\\]\\\\:;'\".,`\\s].")) {
            JOptionPane.showMessageDialog(this, "Giá tiền không được chứa ký tự đặc biệt!");
            return false;
        }

        // 2.3 Không được là biểu thức như 10+1, 10-1, 10*2, 100/5
        if (giaStr.contains("+") || giaStr.contains("*") || giaStr.contains("/")) {
            JOptionPane.showMessageDialog(this, "Giá tiền phải là số dương!");
            return false;
        }

        // 2.4 Phải là số nguyên dương và trong khoảng 10.000 – 500.000
        try {
            int gia = Integer.parseInt(giaStr);

            if (gia <= 0) {
                JOptionPane.showMessageDialog(this, "Giá tiền phải là số dương!");
                return false;
            }

            if (gia < 10000) {
                JOptionPane.showMessageDialog(this, "Giá tiền phải lớn hơn hoặc bằng 10.000!");
                return false;
            }

            if (gia > 500000) {
                JOptionPane.showMessageDialog(this, "Giá tiền không được vượt quá 500.000!");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá tiền phải là số nguyên dương hợp lệ!");
            return false;
        }

        return true; // Tất cả hợp lệ
    }

    private boolean validateSuaSanPham() {
        String id = lblID.getText().trim();
        String tenSP = txtTensp.getText().trim();
        String giaStr = txtGiatien.getText().trim().replace(",", "").replace(".", "");

        // Check mã sản phẩm
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
            return false;
        }

        // ======= 1. Tên sản phẩm =======
        // 1.1 Không được để trống
        if (tenSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được để trống!");
            return false;
        }

        // 1.2 Không được chứa số
        if (tenSP.matches(".*\\d.*")) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được chứa số!");
            return false;
        }

        // 1.3 Không được chứa ký tự đặc biệt (chỉ cho phép chữ và khoảng trắng)
        if (!tenSP.matches("^[\\p{L}\\s]+$")) {
            JOptionPane.showMessageDialog(this, "Tên sản phẩm không được chứa ký tự đặc biệt!");
            return false;
        }

        // Check tên đã tồn tại (chỉ check với sản phẩm khác, không check với chính nó)
        List<SanPham> danhSach = new SanPhamDAO().getAll();
        for (SanPham sp : danhSach) {
            if (sp.getTenSanPham().equalsIgnoreCase(tenSP) && !sp.getIDSanPham().equals(id)) {
                JOptionPane.showMessageDialog(this, "Tên sản phẩm đã tồn tại. Vui lòng nhập tên khác!");
                return false;
            }
        }

        // ======= 2. Giá tiền =======
        // 2.1 Không được để trống
        if (giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Giá tiền không được để trống!");
            return false;
        }

        // 2.2 Không chứa chữ cái
        if (giaStr.matches(".*[a-zA-Z].*")) {
            JOptionPane.showMessageDialog(this, "Giá tiền không được chứa chữ cái!");
            return false;
        }

        // 2.3 Không chứa ký tự đặc biệt
        if (giaStr.matches(".*[~!@#$%^&()_=|<>?{}\\[\\]\\\\:;'\".,`\\s].")) {
            JOptionPane.showMessageDialog(this, "Giá tiền không được chứa ký tự đặc biệt!");
            return false;
        }

        // 2.3 Không được là biểu thức như 10+1, 10-1, 10*2, 100/5
        if (giaStr.contains("+") || giaStr.contains("*") || giaStr.contains("/")) {
            JOptionPane.showMessageDialog(this, "Giá tiền phải là số dương!");
            return false;
        }
        // 2.4 Phải là số nguyên dương và trong khoảng 10.000 – 500.000
        try {
            int gia = Integer.parseInt(giaStr);

            if (gia <= 0) {
                JOptionPane.showMessageDialog(this, "Giá tiền phải là số dương!");
                return false;
            }

            if (gia < 10000) {
                JOptionPane.showMessageDialog(this, "Giá tiền phải lớn hơn hoặc bằng 10.000!");
                return false;
            }

            if (gia > 500000) {
                JOptionPane.showMessageDialog(this, "Giá tiền không được vượt quá 500.000!");
                return false;
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá tiền phải là số nguyên dương hợp lệ!");
            return false;
        }

        return true;

    }

    private void taoMaSanPham() {
        String maTuDong = spDao.layMaTuDong();
        lblID.setText(maTuDong);
        JOptionPane.showMessageDialog(this, "Đã tạo mã sản phẩm: " + maTuDong);
    }

    private void themSanPham() {
        if (!validatethemSanPham()) {
            return;
        }

        String maSP = lblID.getText().trim();
        String tenSP = txtTensp.getText().trim();
        float giaTien = Float.parseFloat(txtGiatien.getText().trim());
        String loai = cboLoai.getSelectedItem().toString();

        // Sử dụng constructor mặc định và set từng thuộc tính
        SanPham sp = new SanPham();
        sp.setIDSanPham(maSP);
        sp.setTenSanPham(tenSP);
        sp.setGiaTien(giaTien);
        sp.setLoaiSanPham(loai);
        sp.setIMG(strAnh);
        sp.setTrangThai(1); // Mặc định là đang hoạt động

        spDao.them(sp);
        JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
        fillTable();
        lamMoiForm();
    }

    private void suaSanPham() {
    // Kiểm tra xem có chọn sản phẩm không
    int i = tblBang.getSelectedRow();
    if (i == -1) {
        JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
        return;
    }

    // Validate dữ liệu nhập
    if (!validateSuaSanPham()) {
        return;
    }

    // Lấy sản phẩm gốc để so sánh - SỬA LẠI PHẦN NÀY
    SanPham spGoc;
    String timKiem = txtTimkiem.getText().trim();
    
    // Đảm bảo list luôn được cập nhật từ database
    if (timKiem.isEmpty()) {
        list = spDao.getAll(); // Refresh list từ DB
        if (i >= list.size()) {
            JOptionPane.showMessageDialog(this, "Chỉ số không hợp lệ!");
            return;
        }
        spGoc = list.get(i);
    } else {
        list = spDao.timKiemKetHop(timKiem); // Refresh search results
        if (i >= list.size()) {
            JOptionPane.showMessageDialog(this, "Chỉ số không hợp lệ!");
            return;
        }
        spGoc = list.get(i);
    }

    // Kiểm tra xem có thay đổi gì không
    String tenMoi = txtTensp.getText().trim();
    String giaStrMoi = txtGiatien.getText().trim().replace(",", "").replace(".", "");
    if (giaStrMoi.startsWith("+")) {
        giaStrMoi = giaStrMoi.substring(1);
    }
    float giaMoi = Float.parseFloat(giaStrMoi);
    String loaiMoi = cboLoai.getSelectedItem().toString();
    String anhMoi = strAnh;

    // So sánh với dữ liệu gốc
    boolean coThayDoi = false;
    if (!spGoc.getTenSanPham().equals(tenMoi)) {
        coThayDoi = true;
    }
    if (spGoc.getGiaTien() != giaMoi) {
        coThayDoi = true;
    }
    if (!spGoc.getLoaiSanPham().equals(loaiMoi)) {
        coThayDoi = true;
    }
    if (!spGoc.getIMG().equals(anhMoi)) {
        coThayDoi = true;
    }

    // Nếu không có thay đổi gì
    if (!coThayDoi) {
        JOptionPane.showMessageDialog(this, "Không có thay đổi nào để lưu!");
        return;
    }

    int chon = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn lưu các thay đổi này?",
            "Xác Nhận", JOptionPane.YES_NO_OPTION);
    if (chon == JOptionPane.YES_OPTION) {
        try {
            // Lấy dữ liệu từ form
            String ID = lblID.getText().trim();
            String ten = txtTensp.getText().trim();

            // Xử lý giá tiền giống như trong validation
            String giaStr = txtGiatien.getText().trim().replace(",", "").replace(".", "");
            if (giaStr.startsWith("+")) {
                giaStr = giaStr.substring(1);
            }

            float gia = Float.parseFloat(giaStr);
            String loaiSanPham = cboLoai.getSelectedItem().toString();
            String anh = strAnh;

            // Tạo đối tượng sản phẩm mới
            SanPham sp = new SanPham();
            sp.setIDSanPham(ID);
            sp.setTenSanPham(ten);
            sp.setGiaTien(gia);
            sp.setLoaiSanPham(loaiSanPham);
            sp.setIMG(anh);
            sp.setTrangThai(spGoc.getTrangThai()); // Giữ nguyên trạng thái cũ

            // Gọi DAO để update
            int result = spDao.suaSanPham(sp, ID);
            if (result == 1) {
                JOptionPane.showMessageDialog(this, "Sửa sản phẩm thành công!");
                
                // QUAN TRỌNG: Refresh dữ liệu theo đúng trạng thái hiện tại
                if (timKiem.isEmpty()) {
                    // Không có tìm kiếm - load tất cả
                    loadTatCa();
                } else {
                    // Có tìm kiếm - load lại kết quả tìm kiếm
                    timKiemKetHop();
                    return; // Exit vì timKiemKetHop() đã xử lý selection
                }
                
                // Giữ nguyên selection và refresh detail
                if (i < tblBang.getRowCount()) {
                    tblBang.setRowSelectionInterval(i, i);
                    showdetail(); // Refresh detail với dữ liệu mới
                }
                
                lamMoiForm(); // Reset form sau khi sửa thành công
            } else {
                JOptionPane.showMessageDialog(this, "Sửa sản phẩm thất bại!");
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Lỗi định dạng số: " + e.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Lỗi khi sửa sản phẩm: " + e.getMessage());
        }
    }
}

    private void lamMoiForm() {
        lblID.setText("");
        txtTensp.setText("");
        txtGiatien.setText("");
        cboLoai.setSelectedIndex(0);
        strAnh = "";
        lblAnh.setText("");
        lblAnh.setIcon(null);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanelQLSP = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        txtTensp = new javax.swing.JTextField();
        txtGiatien = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        lblAnh = new javax.swing.JLabel();
        btnThemSP = new javax.swing.JButton();
        khoaSP = new javax.swing.JButton();
        btnSuaSP = new javax.swing.JButton();
        btnLamMoiSP = new javax.swing.JButton();
        cboLoai = new javax.swing.JComboBox<>();
        lblID = new javax.swing.JLabel();
        btnTaoMa = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        txtTimkiem = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        btnTimKiem = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblBang = new javax.swing.JTable();
        btnLoc = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        cboLocSP = new javax.swing.JComboBox<>();
        btnMoKhoa = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanelQLSP.setBackground(new java.awt.Color(234, 232, 232));

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jLabel3.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel3.setText("ID SẢN PHẨM");

        jLabel4.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel4.setText("TÊN SẢN PHẨM");

        jLabel5.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel5.setText("GIÁ TIỀN");

        jLabel6.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        jLabel6.setText("LOẠI SẢN PHẨM");

        txtTensp.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N

        txtGiatien.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        txtGiatien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtGiatienActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(133, 151, 186));

        lblAnh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblAnhMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnh, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblAnh, javax.swing.GroupLayout.DEFAULT_SIZE, 231, Short.MAX_VALUE)
                .addContainerGap())
        );

        btnThemSP.setBackground(new java.awt.Color(31, 51, 86));
        btnThemSP.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnThemSP.setForeground(new java.awt.Color(255, 255, 255));
        btnThemSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/Them.png"))); // NOI18N
        btnThemSP.setText("THÊM SẢN PHẨM");
        btnThemSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemSPActionPerformed(evt);
            }
        });

        khoaSP.setBackground(new java.awt.Color(31, 51, 86));
        khoaSP.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        khoaSP.setForeground(new java.awt.Color(255, 255, 255));
        khoaSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/lock.png"))); // NOI18N
        khoaSP.setText("KHÓA SẢN PHẨM");
        khoaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                khoaSPActionPerformed(evt);
            }
        });

        btnSuaSP.setBackground(new java.awt.Color(31, 51, 86));
        btnSuaSP.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnSuaSP.setForeground(new java.awt.Color(255, 255, 255));
        btnSuaSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/Sua.png"))); // NOI18N
        btnSuaSP.setText("SỬA SẢN PHẨM");
        btnSuaSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaSPActionPerformed(evt);
            }
        });

        btnLamMoiSP.setBackground(new java.awt.Color(31, 51, 86));
        btnLamMoiSP.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        btnLamMoiSP.setForeground(new java.awt.Color(255, 255, 255));
        btnLamMoiSP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/MainForm_Admin/image/lamMoi.png"))); // NOI18N
        btnLamMoiSP.setText("LÀM MỚI ");
        btnLamMoiSP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiSPActionPerformed(evt);
            }
        });

        cboLoai.setFont(new java.awt.Font("Segoe UI Light", 1, 12)); // NOI18N
        cboLoai.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "CAFE", "BÁNH NGỌT", "NƯỚC ÉP" }));

        lblID.setFont(new java.awt.Font("Segoe UI Light", 1, 36)); // NOI18N
        lblID.setForeground(new java.awt.Color(102, 0, 51));

        btnTaoMa.setBackground(new java.awt.Color(31, 51, 86));
        btnTaoMa.setFont(new java.awt.Font("Segoe UI Light", 1, 15)); // NOI18N
        btnTaoMa.setForeground(new java.awt.Color(255, 255, 255));
        btnTaoMa.setText("TẠO MÃ");
        btnTaoMa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTaoMaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addComponent(btnThemSP, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(btnSuaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(26, 26, 26)
                        .addComponent(khoaSP, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(btnLamMoiSP, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel3)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addComponent(lblID, javax.swing.GroupLayout.PREFERRED_SIZE, 98, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(btnTaoMa, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(txtGiatien)
                            .addComponent(cboLoai, 0, 571, Short.MAX_VALUE)
                            .addComponent(txtTensp))
                        .addGap(46, 46, 46)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel3Layout.createSequentialGroup()
                                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(35, 35, 35)
                                        .addComponent(btnTaoMa))
                                    .addGroup(jPanel3Layout.createSequentialGroup()
                                        .addGap(40, 40, 40)
                                        .addComponent(jLabel3)))
                                .addGap(30, 30, 30))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                                .addComponent(lblID, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTensp, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addGap(28, 28, 28)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtGiatien, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addGap(32, 32, 32)
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel6)
                            .addComponent(cboLoai, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 92, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(khoaSP)
                    .addComponent(btnSuaSP)
                    .addComponent(btnThemSP)
                    .addComponent(btnLamMoiSP))
                .addGap(46, 46, 46))
        );

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtTimkiem.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N

        jLabel7.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(31, 51, 86));
        jLabel7.setText("TÌM KIẾM SẢN PHẨM");

        btnTimKiem.setBackground(new java.awt.Color(31, 51, 86));
        btnTimKiem.setFont(new java.awt.Font("Segoe UI Light", 1, 15)); // NOI18N
        btnTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        btnTimKiem.setText("TÌM KIẾM");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        tblBang.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
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
        tblBang.setGridColor(new java.awt.Color(51, 102, 255));
        tblBang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblBangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblBang);

        btnLoc.setBackground(new java.awt.Color(31, 51, 86));
        btnLoc.setFont(new java.awt.Font("Segoe UI Light", 1, 15)); // NOI18N
        btnLoc.setForeground(new java.awt.Color(255, 255, 255));
        btnLoc.setText("LỌC");
        btnLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocActionPerformed(evt);
            }
        });

        jLabel8.setFont(new java.awt.Font("Segoe UI Light", 1, 16)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(31, 51, 86));
        jLabel8.setText("LỌC SẢN PHẨM");

        cboLocSP.setFont(new java.awt.Font("Segoe UI Light", 1, 14)); // NOI18N
        cboLocSP.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "TẤT CẢ", "CAFE", "BÁNH NGỌT", "NƯỚC ÉP" }));

        btnMoKhoa.setBackground(new java.awt.Color(31, 51, 86));
        btnMoKhoa.setFont(new java.awt.Font("Segoe UI Light", 1, 13)); // NOI18N
        btnMoKhoa.setForeground(new java.awt.Color(255, 255, 255));
        btnMoKhoa.setText("MỞ KHÓA");
        btnMoKhoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoKhoaActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(94, 94, 94)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1370, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(63, 63, 63)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel8)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cboLocSP, javax.swing.GroupLayout.PREFERRED_SIZE, 366, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMoKhoa)))
                .addContainerGap(606, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(26, 26, 26)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtTimkiem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cboLocSP, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLoc, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(7, 7, 7)
                .addComponent(btnMoKhoa)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanelQLSPLayout = new javax.swing.GroupLayout(jPanelQLSP);
        jPanelQLSP.setLayout(jPanelQLSPLayout);
        jPanelQLSPLayout.setHorizontalGroup(
            jPanelQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanelQLSPLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(jPanelQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanelQLSPLayout.setVerticalGroup(
            jPanelQLSPLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanelQLSPLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelQLSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanelQLSP, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void tblBangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblBangMouseClicked
        // TODO add your handling code here:
        showdetail();
    }//GEN-LAST:event_tblBangMouseClicked

    private void lblAnhMouseClicked(java.awt.event.MouseEvent evt) {
        // TODO add your handling code here:
        JFileChooser jFC = new JFileChooser("src\\Images");
        jFC.showOpenDialog(null);
        File file = jFC.getSelectedFile();
        lblAnh.setText("");
        try {
            Image img = ImageIO.read(file);
            strAnh = file.getName();
            int width = lblAnh.getWidth();
            int height = lblAnh.getHeight();
            lblAnh.setIcon(new ImageIcon(img.getScaledInstance(width, height, 0)));
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "ĐÃ XẢY RA LỖI!");
        }

    }

    private void btnThemSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemSPActionPerformed
        // TODO add your handling code here:
        themSanPham();
    }//GEN-LAST:event_btnThemSPActionPerformed

    private void btnTaoMaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoMaSPActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnTaoMaSPActionPerformed

    private void btnLamMoiSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiSPActionPerformed
        // TODO add your handling code here:
        lamMoiForm();
    }//GEN-LAST:event_btnLamMoiSPActionPerformed

    private void btnTaoMaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTaoMaActionPerformed
        // TODO add your handling code here:
        taoMaSanPham();

    }//GEN-LAST:event_btnTaoMaActionPerformed

    private void btnSuaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaSPActionPerformed
        // TODO add your handling code here:
        suaSanPham();
    }//GEN-LAST:event_btnSuaSPActionPerformed

    private void txtGiatienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtGiatienActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtGiatienActionPerformed

    private void khoaSPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_khoaSPActionPerformed
        // TODO add your handling code here:
        khoaSanPham();
    }//GEN-LAST:event_khoaSPActionPerformed

    private void btnMoKhoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoKhoaActionPerformed
        // TODO add your handling code here:
        moKhoaSanPham();
    }//GEN-LAST:event_btnMoKhoaActionPerformed

    private void btnLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocActionPerformed
        // TODO add your handling code here:
        locTheoLoai();
    }//GEN-LAST:event_btnLocActionPerformed

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimKiemActionPerformed
        // TODO add your handling code here:
        timKiemKetHop();
    }//GEN-LAST:event_btnTimKiemActionPerformed

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
            java.util.logging.Logger.getLogger(QuanLySanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QuanLySanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QuanLySanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QuanLySanPham.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QuanLySanPham().setVisible(true);
            }
        });
    }

    public JPanel getMainPanel() {
        return jPanelQLSP;
    }

    public JPanel getJPanelQLSP() {
        return jPanelQLSP;
    }

    public JPanel getJPanel1() {
        return jPanel1;
    }

    public JPanel getJPanel3() {
        return jPanel3;
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnLamMoiSP;
    private javax.swing.JButton btnLoc;
    private javax.swing.JButton btnMoKhoa;
    private javax.swing.JButton btnSuaSP;
    private javax.swing.JButton btnTaoMa;
    private javax.swing.JButton btnThemSP;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JComboBox<String> cboLoai;
    private javax.swing.JComboBox<String> cboLocSP;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanelQLSP;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton khoaSP;
    private javax.swing.JLabel lblAnh;
    private javax.swing.JLabel lblID;
    private javax.swing.JTable tblBang;
    private javax.swing.JTextField txtGiatien;
    private javax.swing.JTextField txtTensp;
    private javax.swing.JTextField txtTimkiem;
    // End of variables declaration//GEN-END:variables
}
