/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author ADMIN
 */
public class Taikhoan {
    private String ID_TK,ID_NV, pass,email, vaiTro, trangThai;

    public Taikhoan() {
    }

    public Taikhoan(String ID_TK, String ID_NV, String pass, String email, String vaiTro, String trangThai) {
        this.ID_TK = ID_TK;
        this.ID_NV = ID_NV;
        this.pass = pass;
        this.email = email;
        this.vaiTro = vaiTro;
        this.trangThai = trangThai;
    }

    public String getID_TK() {
        return ID_TK;
    }

    public void setID_TK(String ID_TK) {
        this.ID_TK = ID_TK;
    }

    public String getID_NV() {
        return ID_NV;
    }

    public void setID_NV(String ID_NV) {
        this.ID_NV = ID_NV;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    

   
    
    

    
}
