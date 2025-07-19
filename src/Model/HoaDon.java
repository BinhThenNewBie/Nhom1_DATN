/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author QuynhAnh2311
 */
public class HoaDon {
    private String ID_HD;
    private String ngayThangNam;
    private String thoiGian;
    private float tongTien;
    private String uuDai;
    private String trangThai;
    
    public HoaDon() {
    }

    public HoaDon(String ID_HD, String ngayThangNam, String thoiGian, float tongTien, String uuDai, String trangThai) {
        this.ID_HD = ID_HD;
        this.ngayThangNam = ngayThangNam;
        this.thoiGian = thoiGian;
        this.tongTien = tongTien;
        this.uuDai = uuDai;
        this.trangThai = trangThai;
    }

    public String getID_HD() {
        return ID_HD;
    }

    public void setID_HD(String ID_HD) {
        this.ID_HD = ID_HD;
    }

    public String getNgayThangNam() {
        return ngayThangNam;
    }

    public void setNgayThangNam(String ngayThangNam) {
        this.ngayThangNam = ngayThangNam;
    }

    public String getThoiGian() {
        return thoiGian;
    }

    public void setThoiGian(String thoiGian) {
        this.thoiGian = thoiGian;
    }

    public float getTongTien() {
        return tongTien;
    }

    public void setTongTien(float tongTien) {
        this.tongTien = tongTien;
    }

    public String getUuDai() {
        return uuDai;
    }

    public void setUuDai(String uuDai) {
        this.uuDai = uuDai;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    
    
}
