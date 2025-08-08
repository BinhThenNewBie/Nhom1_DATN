/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBconnect.DBconnect;
import Model.Nhanvien;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


/**
 *
 * @author ADMIN
 */
public class NhanvienDAO {
    public List<Nhanvien> GETALL(){
        List<Nhanvien> Listnv =new ArrayList<>();
        String sql = "SELECT * FROM NHANVIEN" ;
        try {
            Connection conn = DBconnect.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Nhanvien nv = new Nhanvien();
                nv.setID_NV(rs.getString(1));
                nv.setHoTen(rs.getString(2));
                nv.setVaiTro(rs.getString(3));
                nv.setSDT(rs.getString(4));
                nv.setIMG(rs.getString(5));
                String trangThai = rs.getString("TRANGTHAI");
                nv.setTrangThai(trangThai != null ? trangThai : "ACTIVE");
                Listnv.add(nv);
            }
        } catch (Exception e) {
        }
        return Listnv;
    }  
    public Object[] GETROW(Nhanvien nv){
    String ID_NV = nv.getID_NV();
    String hoTen = nv.getHoTen();
    String vaiTro = nv.getVaiTro();
    String SDT = nv.getSDT();
    String IMG = nv.getIMG();
    String trangThai = nv.getTrangThai();
    Object[] rows = new Object[]{ID_NV, hoTen, vaiTro, SDT,IMG, trangThai};
    return rows;
}
    public int Themnv(Nhanvien nv){
        String sql="INSERT INTO NHANVIEN (ID_NV, HOTEN, VAITRO, SDT, IMG, TRANGTHAI) VALUES (?, ?, ?,?, ?, ?)";
        Connection conn = DBconnect.getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, nv.getID_NV());
            pstm.setString(2, nv.getHoTen());
            pstm.setString(3, nv.getVaiTro());
            pstm.setString(4, nv.getSDT());
            pstm.setString(5, nv.getIMG());
            pstm.setString(6, nv.getTrangThai() != null ? nv.getTrangThai() : "ACTIVE");
            
            if(pstm.executeUpdate() > 0){
                System.out.println("Thêm nhân viên mới thành công!");
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    public int suanv(Nhanvien nv, String idnv){
        String sql="UPDATE NHANVIEN SET HOTEN=?, VAITRO=?, SDT=?, IMG=?, TRANGTHAI=? WHERE ID_NV = ?";
        Connection conn = DBconnect.getConnection();
        PreparedStatement pstm = null;
        try {
            pstm = conn.prepareStatement(sql);
            pstm.setString(1, nv.getHoTen());
            pstm.setString(2, nv.getVaiTro());
            pstm.setString(3, nv.getSDT());
            pstm.setString(4, nv.getIMG());
            pstm.setString(5, nv.getTrangThai() != null ? nv.getTrangThai() : "ACTIVE");
            pstm.setString(6, idnv);
            
            if(pstm.executeUpdate() > 0){
                System.out.println("Sửa nhân viên thành công!");
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    // Phương thức khóa tài khoản
    public int khoaTaiKhoan(String ID_NV) {
        String sql = "UPDATE NHANVIEN SET TRANGTHAI = 'LOCKED' WHERE ID_NV = ?";
        try (Connection con = DBconnect.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, ID_NV);
            if (pstm.executeUpdate() > 0) {
                System.out.println("Khóa nhân viên thành công!");
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    
    // Phương thức mở khóa tài khoản
    public int moKhoaTaiKhoan(String ID_NV) {
        String sql = "UPDATE NHANVIEN SET TRANGTHAI = 'ACTIVE' WHERE ID_NV = ?";
        try (Connection con = DBconnect.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, ID_NV);
            if (pstm.executeUpdate() > 0) {
                System.out.println("Mở khóa nhân viên thành công!");
                return 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
    //Kiểm tra mã nhân viên có tồn tại trong csdl không
   public boolean Kiemtramanv(String ID_NV){
       String sql="SELECT ID_NV FROM NHANVIEN WHERE ID_NV = ?";
       try {
           Connection conn= DBconnect.getConnection();
           PreparedStatement pstm = conn.prepareStatement(sql);
           pstm.setString(1, ID_NV);
           ResultSet rs = pstm.executeQuery();
           return rs.next();
       } catch (Exception e) {
           e.printStackTrace();
           return false;
       }
   }
   // Lấy tên nhân viên theo mã nhân viên
   public String getTenNV(String ID_NV){
       String sql ="SELECT HOTEN FROM NHANVIEN WHERE ID_NV = ?";
       try {
           Connection conn = DBconnect.getConnection();
           PreparedStatement pstm = conn.prepareStatement(sql);
           
           pstm.setString(1, ID_NV);
           ResultSet rs = pstm.executeQuery();
           if(rs.next()){
               return rs.getString("HOTEN");
           }
       } catch (Exception e) {
            e.printStackTrace();
       }
       return null;
   }
}

