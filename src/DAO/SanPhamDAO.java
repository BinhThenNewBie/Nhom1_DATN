/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBconnect.DBconnect;
import Model.SanPham;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class SanPhamDAO {

    // 1. Khóa sản phẩm theo ID
    public boolean khoaSanPham(String idSP) {
        String sql = "UPDATE SANPHAM SET TRANGTHAI = 0 WHERE ID_SP = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idSP);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public boolean moKhoaSanPham(String idSP) {
        String sql = "UPDATE SANPHAM SET TRANGTHAI = 1 WHERE ID_SP = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, idSP);
            return ps.executeUpdate() > 0;
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public float getGiaByID(String id_SP) {
        String sql = "SELECT GIA FROM SANPHAM WHERE ID_SP = ?";
        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, id_SP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getFloat(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 2. Lấy sản phẩm theo ID_SP
    public List<SanPham> getAllID_SP(String ID_SP) {
        List<SanPham> listsp = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM WHERE ID_SP LIKE ?";
        Connection con = DBconnect.getConnection();
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, ID_SP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setIDSanPham(rs.getString(1));
                sp.setTenSanPham(rs.getString(2));
                sp.setGiaTien(rs.getFloat(3));
                sp.setLoaiSanPham(rs.getString(4));
                sp.setIMG(rs.getString(5));
                sp.setTrangThai(rs.getInt(6));
                listsp.add(sp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listsp;
    }

    // 3. Lấy tất cả sản phẩm
    public List<SanPham> getAll() {
        List<SanPham> listsp = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM";
        Connection con = DBconnect.getConnection();
        try {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setIDSanPham(rs.getString(1));
                sp.setTenSanPham(rs.getString(2));
                sp.setGiaTien(rs.getFloat(3));
                sp.setLoaiSanPham(rs.getString(4));
                sp.setIMG(rs.getString(5));
                sp.setTrangThai(rs.getInt(6));
                listsp.add(sp);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return listsp;
    }

    // 4. Lọc sản phẩm theo loại
    public List<SanPham> locTheoLoai(String loai) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM WHERE LOAI = ?";
        try (Connection conn = DBconnect.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, loai);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                SanPham sp = new SanPham();
                sp.setIDSanPham(rs.getString(1));
                sp.setTenSanPham(rs.getString(2));
                sp.setGiaTien(rs.getFloat(3));
                sp.setLoaiSanPham(rs.getString(4));
                sp.setIMG(rs.getString(5));
                sp.setTrangThai(rs.getInt(6));
                list.add(sp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public List<SanPham> getSPByTen(String ten) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM WHERE TENSP LIKE ?";
        try (Connection con = DBconnect.getConnection(); 
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, "%" + ten + "%");
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setIDSanPham(rs.getString("ID_SP"));
                    sp.setTenSanPham(rs.getString("TENSP"));
                    sp.setGiaTien(rs.getFloat("GIA"));
                    sp.setLoaiSanPham(rs.getString("LOAI"));
                    sp.setIMG(rs.getString("IMG"));
                    sp.setTrangThai(rs.getInt("TRANGTHAI"));
                    list.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public SanPham timKiemTheoID(String id) {
        String sql = "SELECT * FROM SANPHAM WHERE ID_SP = ?";
        try (Connection con = DBconnect.getConnection(); 
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, id);
            try (ResultSet rs = pstm.executeQuery()) {
                if (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setIDSanPham(rs.getString("ID_SP"));
                    sp.setTenSanPham(rs.getString("TENSP"));
                    sp.setGiaTien(rs.getFloat("GIA"));
                    sp.setLoaiSanPham(rs.getString("LOAI"));
                    sp.setIMG(rs.getString("IMG"));
                    sp.setTrangThai(rs.getInt("TRANGTHAI"));
                    return sp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<SanPham> timKiemKetHop(String tuKhoa) {
        List<SanPham> list = new ArrayList<>();
        String sql = "SELECT * FROM SANPHAM WHERE ID_SP LIKE ? OR TENSP LIKE ?";
        try (Connection con = DBconnect.getConnection(); 
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, "%" + tuKhoa + "%");
            pstm.setString(2, "%" + tuKhoa + "%");
            try (ResultSet rs = pstm.executeQuery()) {
                while (rs.next()) {
                    SanPham sp = new SanPham();
                    sp.setIDSanPham(rs.getString("ID_SP"));
                    sp.setTenSanPham(rs.getString("TENSP"));
                    sp.setGiaTien(rs.getFloat("GIA"));
                    sp.setLoaiSanPham(rs.getString("LOAI"));
                    sp.setIMG(rs.getString("IMG"));
                    sp.setTrangThai(rs.getInt("TRANGTHAI"));
                    list.add(sp);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Lỗi tìm kiếm kết hợp: " + e.getMessage());
        }
        return list;
    }

    // 7. Lấy từng dòng để hiển thị lên JTable
    public Object[] getRow(SanPham sp) {
        DecimalFormat df = new DecimalFormat("#,###");
        String tienVND = df.format(sp.getGiaTien()) + " VND";
        return new Object[]{
            sp.getIDSanPham(),
            sp.getTenSanPham(),
            tienVND,
            sp.getLoaiSanPham(),
            sp.getIMG(),
            sp.getTrangThai() == 1 ? "Đang hoạt động" : "Đã khóa"
        };
    }

    // 8. Thêm sản phẩm
    public void them(SanPham sp) {
        String sql = "INSERT INTO SANPHAM (ID_SP, TENSP, GIA, LOAI, IMG, TrangThai) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, sp.getIDSanPham());
            ps.setString(2, sp.getTenSanPham());
            ps.setFloat(3, sp.getGiaTien());
            ps.setString(4, sp.getLoaiSanPham());
            ps.setString(5, sp.getIMG());
            ps.setInt(6, sp.getTrangThai());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 9. Cập nhật sản phẩm
    public int suaSanPham(SanPham sp, String IDcu) {
        int result = 0;
        String sql = "UPDATE SANPHAM SET ID_SP = ?, TENSP = ?, GIA = ?, LOAI = ?, IMG = ?, TrangThai = ? WHERE ID_SP = ?";
        Connection con = DBconnect.getConnection();
        try {
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, sp.getIDSanPham());
            pst.setString(2, sp.getTenSanPham());
            pst.setFloat(3, sp.getGiaTien());
            pst.setString(4, sp.getLoaiSanPham());
            pst.setString(5, sp.getIMG());
            pst.setInt(6, sp.getTrangThai());
            pst.setString(7, IDcu);
            result = pst.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    // 10. Tạo mã tự động
    public String layMaTuDong() {
        String sql = "SELECT MAX(CAST(SUBSTRING(ID_SP, 3, LEN(ID_SP)) AS INT)) AS maxID FROM SANPHAM";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                int max = rs.getInt("maxID") + 1;
                return "SP" + max;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "SP1";
    }

}
