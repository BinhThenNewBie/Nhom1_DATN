/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author nmttt
 */
public class SanPham {
    private String IDSanPham, tenSanPham;
    private float giaTien;
    private String loaiSanPham, IMG;
    private int trangThai;

    public SanPham() {
    }

    public SanPham(String IDSanPham, String tenSanPham, float giaTien, String loaiSanPham, String IMG, int trangThai) {
        this.IDSanPham = IDSanPham;
        this.tenSanPham = tenSanPham;
        this.giaTien = giaTien;
        this.loaiSanPham = loaiSanPham;
        this.IMG = IMG;
        this.trangThai = trangThai;
    }

    public String getIDSanPham() {
        return IDSanPham;
    }

    public void setIDSanPham(String IDSanPham) {
        this.IDSanPham = IDSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public float getGiaTien() {
        return giaTien;
    }

    public void setGiaTien(float giaTien) {
        this.giaTien = giaTien;
    }

    public String getLoaiSanPham() {
        return loaiSanPham;
    }

    public void setLoaiSanPham(String loaiSanPham) {
        this.loaiSanPham = loaiSanPham;
    }

    public String getIMG() {
        return IMG;
    }

    public void setIMG(String IMG) {
        this.IMG = IMG;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }
    

    
}
