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
    private float tongTienHD;
    private float tongTienUuDai;
    private float tongTienThanhToan;
    private float tienKhachHang;
    private float tienTraLai;
    private String uuDai;
    private int trangThai;

    public HoaDon() {
    }

    public HoaDon(String ID_HD, String ngayThangNam, String thoiGian, float tongTienHD, float tongTienUuDai, float tongTienThanhToan, float tienKhachHang, float tienTraLai, String uuDai, int trangThai) {
        this.ID_HD = ID_HD;
        this.ngayThangNam = ngayThangNam;
        this.thoiGian = thoiGian;
        this.tongTienHD = tongTienHD;
        this.tongTienUuDai = tongTienUuDai;
        this.tongTienThanhToan = tongTienThanhToan;
        this.tienKhachHang = tienKhachHang;
        this.tienTraLai = tienTraLai;
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

    public float getTongTienHD() {
        return tongTienHD;
    }

    public void setTongTienHD(float tongTienHD) {
        this.tongTienHD = tongTienHD;
    }

    public float getTongTienUuDai() {
        return tongTienUuDai;
    }

    public void setTongTienUuDai(float tongTienUuDai) {
        this.tongTienUuDai = tongTienUuDai;
    }

    public float getTongTienThanhToan() {
        return tongTienThanhToan;
    }

    public void setTongTienThanhToan(float tongTienThanhToan) {
        this.tongTienThanhToan = tongTienThanhToan;
    }

    public float getTienKhachHang() {
        return tienKhachHang;
    }

    public void setTienKhachHang(float tienKhachHang) {
        this.tienKhachHang = tienKhachHang;
    }

    public float getTienTraLai() {
        return tienTraLai;
    }

    public void setTienTraLai(float tienTraLai) {
        this.tienTraLai = tienTraLai;
    }

    public String getUuDai() {
        return uuDai;
    }

    public void setUuDai(String uuDai) {
        this.uuDai = uuDai;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

}
