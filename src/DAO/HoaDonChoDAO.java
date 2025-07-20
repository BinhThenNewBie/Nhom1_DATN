/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBconnect.DBconnect;
import Model.ChiTietHoaDon;
import Model.HoaDonCho;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author QuynhAnh2311
 */
public class HoaDonChoDAO {

    List<HoaDonCho> lstHDC = new ArrayList<>();
    List<ChiTietHoaDon> lstHDCT = new ArrayList<>();

    public ChiTietHoaDon selectCTHD(String ID_HD, String ID_SP) {
        String sql = "SELECT * FROM CHITIETHOADON WHERE ID_HD = ? AND ID_SP = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ID_HD);
            ps.setString(2, ID_SP);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setID_HD(rs.getString("ID_HD"));
                cthd.setID_SP(rs.getString("ID_SP"));
                cthd.setTenSP(rs.getString("TENSP"));
                cthd.setGiaSP(rs.getFloat("GIASP"));
                cthd.setSoLuong(rs.getInt("SOLUONG"));
                return cthd;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void clearOrderTemp() {
        String sql = "DELETE FROM CHITIETHOADON WHERE ID_HD NOT IN (SELECT ID_HD FROM HOADONCHO)";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //CẬP NHẬT
    public int UpdateSP(String ID_HD, String ID_SP, ChiTietHoaDon cthd) {
        String sql = "UPDATE CHITIETHOADON SET SOLUONG = ? WHERE ID_HD = ? AND ID_SP = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setInt(1, cthd.getSoLuong());
            pstm.setString(2, ID_HD);
            pstm.setString(3, ID_SP);

            int row = pstm.executeUpdate();
            if (row > 0) {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int updateTongTien(String idHD, float tongTien) {
        String sql = "UPDATE HOADONCHO SET TONGTIEN = ? WHERE ID_HD = ?";

        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setFloat(1, tongTien);
            ps.setString(2, idHD);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateUuDai(String idHD, String uudai) {
        String sql = "UPDATE HOADONCHO SET GIATRI = ? WHERE ID_HD = ?";

        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, uudai);
            ps.setString(2, idHD);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int UpdateGia(String ID_HD, String ID_SP, float gia) {
        String sql = "UPDATE CHITIETHOADON SET GIASP = ? WHERE ID_HD = ? AND ID_SP = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setFloat(1, gia);
            pstm.setString(2, ID_HD);
            pstm.setString(3, ID_SP);
            int row = pstm.executeUpdate();
            return row; // Trả về số dòng cập nhật được
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // XÓA
    public int DeleteSP(String ID_SP, String ID_HD) {
        String sql = "DELETE FROM CHITIETHOADON WHERE ID_SP = ? AND ID_HD = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ID_SP);
            ps.setString(2, ID_HD);
            return ps.executeUpdate(); // Trả về số dòng bị ảnh hưởng (1 nếu xoá thành công)
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int DeleteHD(String ID_HD) {
        String sql = "DELETE FROM HOADONCHO WHERE ID_HD LIKE?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ID_HD);
            ps.executeUpdate();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int DeleteCTHD(String ID_HD) {
        String sql = "DELETE FROM CHITIETHOADON WHERE ID_HD = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ID_HD);
            ps.executeUpdate();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // LƯU THÔNG TIN VÀO HÓA ĐƠN CHỜ 
  

    public int SaveHDCHO(HoaDonCho hdc) {
        String sql = "INSERT INTO HOADONCHO VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBconnect.getConnection(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, hdc.getID_HD());
            pstm.setString(2, hdc.getNgayThangNam());
            pstm.setString(3, hdc.getThoiGian());
            pstm.setFloat(4, hdc.getTongTien());
            pstm.setString(5, hdc.getUuDai());
            int row = pstm.executeUpdate();
            if (row > 0) {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // LƯU SẢN PHẨM VÀO CHI TIẾT HÓA ĐƠN
    public int SaveCTHD(ChiTietHoaDon cthd) {
        String sql = "INSERT INTO CHITIETHOADON VALUES (?, ?, ?, ?, ?)";
        try (Connection con = DBconnect.getConnection(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, cthd.getID_HD());
            pstm.setString(2, cthd.getID_SP());
            pstm.setString(3, cthd.getTenSP());
            pstm.setFloat(4, cthd.getGiaSP());
            pstm.setInt(5, cthd.getSoLuong());
            int row = pstm.executeUpdate();
            if (row > 0) {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<String> getIDHDTheoIDSP(String idSP) {
        List<String> ds = new ArrayList<>();
        try {
            String sql = "SELECT DISTINCT ID_HD FROM CHITIETHOADON WHERE ID_SP = ?";
            Connection con = DBconnect.getConnection();
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, idSP);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ds.add(rs.getString("ID_HD"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    // GET ALL ID HÓA ĐƠN
    public List<ChiTietHoaDon> getAllID_HD(String ID_HD) {
        List<ChiTietHoaDon> lstHDCT = new ArrayList<>();

        String sql = "SELECT ID_HD, ID_SP, TENSP, GIASP, SOLUONG FROM CHITIETHOADON WHERE ID_HD = ?";
        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, ID_HD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietHoaDon ct = new ChiTietHoaDon();
                ct.setID_HD(rs.getString("ID_HD"));
                ct.setID_SP(rs.getString("ID_SP"));
                ct.setTenSP(rs.getString("TENSP"));
                ct.setGiaSP(rs.getFloat("GIASP"));
                ct.setSoLuong(rs.getInt("SOLUONG"));
                lstHDCT.add(ct); // ✅ Không dùng biến toàn cục
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstHDCT;
    }

    /// GET ALL & GET ROW CHI TIẾT HÓA ĐƠN
    public List<ChiTietHoaDon> getAllCTHD() {
        String sql = "SELECT * FROM CHITIETHOADON";
        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChiTietHoaDon cthd = new ChiTietHoaDon();
                cthd.setID_HD(rs.getString(1));
                cthd.setID_SP(rs.getString(2));
                cthd.setTenSP(rs.getString(3));
                cthd.setGiaSP(rs.getFloat(4));
                cthd.setSoLuong(rs.getInt(5));
                lstHDCT.add(cthd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lstHDCT;
    }

    public Object getRowCTHD(ChiTietHoaDon cthd) {
        Object[] row = new Object[]{
            cthd.getID_SP(),
            cthd.getTenSP(),
            cthd.getGiaSP(),
            cthd.getSoLuong()
        };
        return row;
    }

    /// GET ALL & GET ROW HÓA ĐƠN CHỜ
    public List<HoaDonCho> getALLHDCHO() {
        List<HoaDonCho> lstHDC = new ArrayList<>();
        String sql = "SELECT * FROM HOADONCHO";
        try (Connection con = DBconnect.getConnection(); Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                HoaDonCho hdc = new HoaDonCho();
                hdc.setID_HD(rs.getString(1));
                hdc.setNgayThangNam(rs.getString(2));
                hdc.setThoiGian(rs.getString(3));
                hdc.setTongTien(rs.getFloat(4));
                hdc.setUuDai(rs.getString(5));
                lstHDC.add(hdc);
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        return lstHDC;
    }

    public Object getRowHDCHO(HoaDonCho hdc) {
        Object[] row = new Object[]{
            hdc.getID_HD(),
            hdc.getTongTien()
        };
        return row;
    }
}
