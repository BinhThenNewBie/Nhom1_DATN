/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAO;

import DBconnect.DBconnect;
import Model.Taikhoan;
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

public class TaikhoanDAO {
    // lấy email từ sql
    public String getEmailByRole(String role) {
    String sql = "SELECT EMAIL FROM TAIKHOAN WHERE VAITRO = ?";
    try (Connection con = DBconnect.getConnection();
         PreparedStatement pstm = con.prepareStatement(sql)) {
        pstm.setString(1, role);
        ResultSet rs = pstm.executeQuery();
        if (rs.next()) {
            return rs.getString("EMAIL");
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}
    // Lấy tất cả tài khoản
    public List<Taikhoan> GETALL(){
        List<Taikhoan> Listtk = new ArrayList<>();
        String sql = "SELECT * FROM TAIKHOAN";
        try {
            Connection conn = DBconnect.getConnection();
            Statement stm = conn.createStatement();
            ResultSet rs = stm.executeQuery(sql);
            while (rs.next()) {
                Taikhoan tk = new Taikhoan();
                tk.setID_TK(rs.getString(1));
                tk.setID_NV(rs.getString(2));
                tk.setTenNV(rs.getString(3));
                tk.setPass(rs.getString(4));
                tk.setEmail(rs.getString(5));
                tk.setVaiTro(rs.getString(6));
                String trangThai = rs.getString("TRANGTHAI");
                tk.setTrangThai(trangThai != null ? trangThai : "ACTIVE");
                Listtk.add(tk);
            }
        } catch (Exception e) {
            System.err.println("Error getting all accounts: " + e.getMessage());
            e.printStackTrace();
        }
        return Listtk;
    }
    
    public Object[] GETROW(Taikhoan tk){
        String ID_TK = tk.getID_TK();
        String ID_NV = tk.getID_NV();
        String tenNV = tk.getTenNV();
        String Pass = tk.getPass();
        String Email = tk.getEmail();
        String vaiTro = tk.getVaiTro();
        String trangThai = tk.getTrangThai();
        Object[] rows = new Object[]{ID_TK,ID_NV,tenNV, Pass, Email, vaiTro, trangThai};
        return rows;
    }
    

public int them(Taikhoan tk){
    String sql = "INSERT INTO TAIKHOAN (ID_TK, ID_NV, TENNV, PASS, EMAIL, VAITRO, TRANGTHAI) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (Connection con = DBconnect.getConnection();
         PreparedStatement pstm = con.prepareStatement(sql)) {
        pstm.setString(1, tk.getID_TK());
        pstm.setString(2, tk.getID_NV());
        pstm.setString(3, tk.getTenNV());
        pstm.setString(4, tk.getPass());
        pstm.setString(5, tk.getEmail());
        pstm.setString(6, tk.getVaiTro());
        pstm.setString(7, tk.getTrangThai()); 
        
        int result = pstm.executeUpdate();
        if (result > 0) {
            System.out.println("Thêm tài khoản thành công!");
            return 1;
        }
    } catch (Exception e) {
        System.err.println("Error adding account: " + e.getMessage());
        e.printStackTrace();
    }
    return 0;
}
    
    // Sửa tài khoản (dành cho ADMIN)
    public int sua(String oldIDTK, Taikhoan tk){
        String sql = "UPDATE TAIKHOAN SET ID_TK= ?,ID_NV=?,TENNV=?, PASS=?, EMAIL=?, VAITRO=? WHERE ID_TK = ?";
        try (Connection con = DBconnect.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, tk.getID_TK());
            pstm.setString(2, tk.getID_NV());
            pstm.setString(3, tk.getTenNV());
            pstm.setString(4, tk.getPass());
            pstm.setString(5, tk.getEmail());
            pstm.setString(6, tk.getVaiTro());
            pstm.setString(7, oldIDTK);
            
            int result = pstm.executeUpdate();
            if (result > 0) {
                System.out.println("Sửa tài khoản thành công!");
                return 1;
            }
        } catch (Exception e) {
            System.err.println("Error updating account: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    // Khóa tài khoản
    public int khoaTaiKhoan(String ID_TK) {
        String sql = "UPDATE TAIKHOAN SET TRANGTHAI = 'LOCKED' WHERE ID_TK = ?";
        try (Connection con = DBconnect.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, ID_TK);
            
            int result = pstm.executeUpdate();
            if (result > 0) {
                System.out.println("Khóa tài khoản thành công!");
                return 1;
            }
        } catch (Exception e) {
            System.err.println("Error locking account: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    // Mở khóa tài khoản
    public int moKhoaTaiKhoan(String ID_TK) {
        String sql = "UPDATE TAIKHOAN SET TRANGTHAI = 'ACTIVE' WHERE ID_TK = ?";
        try (Connection con = DBconnect.getConnection();
             PreparedStatement pstm = con.prepareStatement(sql)) {
            pstm.setString(1, ID_TK);
            
            int result = pstm.executeUpdate();
            if (result > 0) {
                System.out.println("Mở khóa tài khoản thành công!");
                return 1;
            }
        } catch (Exception e) {
            System.err.println("Error unlocking account: " + e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }
    
    
     public int passwordchange(String passin, String email){
    String sql = "UPDATE TAIKHOAN SET PASS = ? WHERE EMAIL = ?";
    try (Connection con = DBconnect.getConnection();
         PreparedStatement pstm = con.prepareStatement(sql)) {
        
        pstm.setString(1, passin);
        pstm.setString(2, email);
        
        // Execute update và trả về số dòng bị ảnh hưởng
        int rowsAffected = pstm.executeUpdate();
        
        return rowsAffected; // Trả về 1 nếu thành công, 0 nếu không tìm thấy email
        
    } catch (Exception e) {
        e.printStackTrace();
        return -1; // Trả về -1 nếu có lỗi
        
    }
}
}
