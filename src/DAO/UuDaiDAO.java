/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBconnect.DBconnect;
import Model.UuDai;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Connection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;

/**
 *
 * @author nmttt
 */
public class UuDaiDAO {

    public List<UuDai> getAll() {
        List<UuDai> listud = new ArrayList<>();
        String sql = "SELECT * FROM UUDAI";
        Connection con = DBconnect.getConnection();
        try {
            Statement stm = con.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                UuDai ud = new UuDai();
                ud.setIdUD(rs.getString(1));
                ud.setGiaTri(rs.getString(2));
                ud.setApDungVoi(rs.getFloat(3));
                ud.setNgayBatDau(rs.getDate(4));
                ud.setNgayKetThuc(rs.getDate(5));
                listud.add(ud);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return listud;
    }

    public Object[] getRowUuDai(UuDai ud) {
        return new Object[]{
            ud.getGiaTri(),
            ud.getApDungVoi()
        };
    }

    public Object[] getRow(UuDai ud) {
        String trangThai;
        Date today = new Date();
        if (ud.getNgayKetThuc().before(today)) {
            trangThai = "HẾT HẠN";
        } else {
            trangThai = "CÒN HẠN";
        }

        return new Object[]{
            ud.getIdUD(),
            ud.getGiaTri(),
            formatVND(ud.getApDungVoi()),
            ud.getNgayBatDau(),
            ud.getNgayKetThuc(),
            trangThai
        };
    }

    public void them(UuDai ud) {
        try {
            Connection conn = DBconnect.getConnection();
            String sql = "INSERT INTO UUDAI (ID_UD, GIATRI, APDUNGVOI, NGAYBATDAU, NGAYKETTHUC) VALUES (?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ud.getIdUD());
            ps.setString(2, ud.getGiaTri());
            ps.setFloat(3, ud.getApDungVoi());
            ps.setDate(4, new java.sql.Date(ud.getNgayBatDau().getTime()));
            ps.setDate(5, new java.sql.Date(ud.getNgayKetThuc().getTime()));
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Thêm ưu đãi thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Thêm ưu đãi thất bại!");
        }
    }

    public void sua(UuDai ud) {
        try {
            Connection conn = DBconnect.getConnection();
            String sql = "UPDATE UUDAI SET GIATRI=?, APDUNGVOI=?, NGAYBATDAU=?, NGAYKETTHUC=? WHERE ID_UD=?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ud.getGiaTri());
            ps.setFloat(2, ud.getApDungVoi());
            ps.setDate(3, new java.sql.Date(ud.getNgayBatDau().getTime()));
            ps.setDate(4, new java.sql.Date(ud.getNgayKetThuc().getTime()));
            ps.setString(5, ud.getIdUD());
            ps.executeUpdate();
            JOptionPane.showMessageDialog(null, "Cập nhật ưu đãi thành công!");
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, "Cập nhật ưu đãi thất bại!");
        }
    }

    private String formatVND(float amount) {
        DecimalFormat df = new DecimalFormat("#,###");
        return df.format(amount) + " VND";
    }
}
