/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBconnect.DBconnect;
import Model.ChiTietHoaDon;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ADMIN
 */
public class ChiTietHoaDonDAO {

    public void clearOrderTemp() {
        String sql = "DELETE FROM CHITIETHOADON WHERE ID_HD NOT IN (SELECT ID_HD FROM HOADON)";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // UPDATE CTHD
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

    public int UpdateGia(String ID_HD, String ID_SP, float gia) {
        String sql = "UPDATE CHITIETHOADON SET GIASP = ? WHERE ID_HD = ? AND ID_SP = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setFloat(1, gia);
            pstm.setString(2, ID_HD);
            pstm.setString(3, ID_SP);
            int row = pstm.executeUpdate();
            return row;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    // DELETE CTHD
    public int Delete_SP(String ID_SP, String ID_HD) {
        String sql = "DELETE FROM CHITIETHOADON WHERE ID_SP = ? AND ID_HD = ?";
        try (Connection con = DBconnect.getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, ID_SP);
            ps.setString(2, ID_HD);
            return ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public int Delete_CTHD(String ID_HD) {
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

    // SAVE CTHD
    public int Save_CTHD(ChiTietHoaDon cthd) {
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

    // GET ALL & GET ROW
    public ChiTietHoaDon getAll_ID_HD_SP(String ID_HD, String ID_SP) {
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

    public List<ChiTietHoaDon> getAll_CTHD(String ID_HD) {
        List<ChiTietHoaDon> lstHDCT = new ArrayList<>();
        String sql = "SELECT * FROM CHITIETHOADON WHERE ID_HD = ?";
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
                lstHDCT.add(ct); // 
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
}
