/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBconnect.DBconnect;
import Model.HoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class HoaDonDAO {

    public boolean existsMaHD(String maHD) {
        String sql = "SELECT COUNT(*) FROM HOADON WHERE ID_HD = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, maHD);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // UPDATE HOÁ ĐƠN
    public int Update_TT_HD(String ID_HD, float tongTien) {
        String sql = "UPDATE HOADON SET TONGTIEN_HD = ? WHERE ID_HD = ?";
        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setFloat(1, tongTien);
            ps.setString(2, ID_HD);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int Update_TTUuDai(String ID_HD, float tongTienUuDai) {
        String sql = "UPDATE HOADON SET TONGTIENUUDAI = ? WHERE ID_HD = ?";
        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setFloat(1, tongTienUuDai);
            ps.setString(2, ID_HD);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int Update_TTThanhToan(String ID_HD, float tongThanhToan) {
        String sql = "UPDATE HOADON SET TONGTHANHTOAN = ? WHERE ID_HD = ?";
        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setFloat(1, tongThanhToan);
            ps.setString(2, ID_HD);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int Update_PhuongThucThanhToan(String ID_HD, String pttt) {
        String sql = "UPDATE HOADON SET PHUONGTHUCTHANHTOAN = ? WHERE ID_HD = ?";
        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, pttt);
            ps.setString(2, ID_HD);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int Update_TKhachHang(String ID_HD, float tKhachHang) {
        String sql = "UPDATE HOADON SET TIENKHACHHANG = ? WHERE ID_HD = ?";
        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setFloat(1, tKhachHang);
            ps.setString(2, ID_HD);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int Update_TTraLai(String ID_HD, float tTraLai) {
        String sql = "UPDATE HOADON SET TIENTRALAI = ? WHERE ID_HD = ?";
        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setFloat(1, tTraLai);
            ps.setString(2, ID_HD);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public int updateUuDai(String idHD, String uudai) {
        String sql = "UPDATE HOADON SET UUDAI = ? WHERE ID_HD = ?";

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

    public int updatetrangThai(String idHD, int trangThai) {
        String sql = "UPDATE HOADON SET TRANGTHAI = ? WHERE ID_HD = ?";

        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setInt(1, trangThai);
            ps.setString(2, idHD);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    // DELETE HOA DON
    public int Delete_HD(String ID_HD) {
        String sql = "DELETE FROM HOADON WHERE ID_HD = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ID_HD);
            ps.executeUpdate();
            return 1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    //SAVE HOÁ ĐƠN 
    public int Save_HD(HoaDon hd) {
        String sql = "INSERT INTO HOADON VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection con = DBconnect.getConnection(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, hd.getID_HD());
            pstm.setString(2, hd.getNguoiPhuTrach());
            pstm.setString(3, hd.getNgayThangNam());
            pstm.setString(4, hd.getThoiGian());
            pstm.setFloat(5, hd.getTongTienHD());
            pstm.setFloat(6, hd.getTongTienUuDai());
            pstm.setFloat(7, hd.getTongTienThanhToan());
            pstm.setString(8, hd.getPhuongThucThanhToan());
            pstm.setFloat(9, hd.getTienKhachHang());
            pstm.setFloat(10, hd.getTienTraLai());
            pstm.setString(11, hd.getUuDai());
            pstm.setInt(12, hd.getTrangThai());
            int row = pstm.executeUpdate();
            if (row > 0) {
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // GET ALL & GET ROW
    public List<HoaDon> getALL_HD() {
        List<HoaDon> lstHD = new ArrayList<>();
        String sql = "SELECT * FROM HOADON";
        try (Connection con = DBconnect.getConnection(); Statement stm = con.createStatement(); ResultSet rs = stm.executeQuery(sql)) {
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setID_HD(rs.getString(1));
                hd.setNguoiPhuTrach(rs.getString(2));
                hd.setNgayThangNam(rs.getString(3));
                hd.setThoiGian(rs.getString(4));
                hd.setTongTienHD(rs.getFloat(5));
                hd.setTongTienUuDai(rs.getFloat(6));
                hd.setTongTienThanhToan(rs.getFloat(7));
                hd.setPhuongThucThanhToan(rs.getString(8));
                hd.setTienKhachHang(rs.getFloat(9));
                hd.setTienTraLai(rs.getFloat(10));
                hd.setUuDai(rs.getString(11));
                hd.setTrangThai(rs.getInt(12));
                lstHD.add(hd);
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        return lstHD;
    }

    public List<HoaDon> getALL_ID_HD(String ID_HD) {
        List<HoaDon> lstHD = new ArrayList<>();
        String sql = "SELECT * FROM HOADON WHERE ID_HD = ?";
        try (
                Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql);) {
            ps.setString(1, ID_HD);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                HoaDon hd = new HoaDon();
                hd.setID_HD(rs.getString("ID_HD"));
                hd.setNguoiPhuTrach(rs.getString("NGUOIPHUTRACH"));
                hd.setNgayThangNam(rs.getString("NGAYTHANGNAM"));
                hd.setThoiGian(rs.getString("THOIGIAN"));
                hd.setTongTienHD(rs.getFloat("TONGTIEN_HD"));
                hd.setTongTienUuDai(rs.getFloat("TONGTIENUUDAI"));
                hd.setTongTienThanhToan(rs.getFloat("TONGTHANHTOAN"));
                hd.setPhuongThucThanhToan(rs.getString("PHUONGTHUCTHANHTOAN"));
                hd.setTienKhachHang(rs.getFloat("TIENKHACHHANG"));
                hd.setTienTraLai(rs.getFloat("TIENTRALAI"));
                hd.setUuDai(rs.getString("UUDAI"));
                hd.setTrangThai(rs.getInt("TRANGTHAI"));
                lstHD.add(hd);
            }
        } catch (Exception e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
        return lstHD;
    }

    public Object getRow_HDCHO(HoaDon hd) {
        Object[] row = new Object[]{
            hd.getID_HD(),
            hd.getTongTienHD(),
            hd.getTongTienUuDai(),
            hd.getTongTienThanhToan()
        };
        return row;
    }

    public Object getRow_HDThanhToan(HoaDon hd) {
        Object[] row = new Object[]{
            hd.getID_HD(),
            hd.getNguoiPhuTrach(),
            hd.getNgayThangNam(),
            hd.getThoiGian(),
            hd.getTongTienHD(),
            hd.getTongTienUuDai(),
            hd.getTongTienThanhToan(),
            hd.getUuDai()
        };
        return row;
    }
}
