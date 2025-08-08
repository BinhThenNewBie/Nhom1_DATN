/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author nmttt
 */
public class Nhanvien {
    private String ID_NV, hoTen, vaiTro,SDT, IMG,trangThai;

    public Nhanvien() {
    }

    public Nhanvien(String ID_NV, String hoTen, String vaiTro, String SDT, String IMG, String trangThai) {
        this.ID_NV = ID_NV;
        this.hoTen = hoTen;
        this.vaiTro = vaiTro;
        this.SDT = SDT;
        this.IMG = IMG;
        this.trangThai = trangThai;
    }

    public String getID_NV() {
        return ID_NV;
    }

    public void setID_NV(String ID_NV) {
        this.ID_NV = ID_NV;
    }

    public String getHoTen() {
        return hoTen;
    }

    public void setHoTen(String hoTen) {
        this.hoTen = hoTen;
    }

    public String getVaiTro() {
        return vaiTro;
    }

    public void setVaiTro(String vaiTro) {
        this.vaiTro = vaiTro;
    }

    public String getSDT() {
        return SDT;
    }

    public void setSDT(String SDT) {
        this.SDT = SDT;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    
    

        
    
}
