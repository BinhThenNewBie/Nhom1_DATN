/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author QuynhAnh2311
 */
public class ChiTietHoaDon {
    private String ID_HD;
    private String ID_SP;
    private String tenSP;
    private float giaSP;
    private int soLuong;

    public ChiTietHoaDon() {
    }

    public ChiTietHoaDon(String ID_HD, String ID_SP, String tenSP, float giaSP, int soLuong) {
        this.ID_HD = ID_HD;
        this.ID_SP = ID_SP;
        this.tenSP = tenSP;
        this.giaSP = giaSP;
        this.soLuong = soLuong;
    }

    public String getID_HD() {
        return ID_HD;
    }

    public void setID_HD(String ID_HD) {
        this.ID_HD = ID_HD;
    }

    public String getID_SP() {
        return ID_SP;
    }

    public void setID_SP(String ID_SP) {
        this.ID_SP = ID_SP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public float getGiaSP() {
        return giaSP;
    }

    public void setGiaSP(float giaSP) {
        this.giaSP = giaSP;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

   
    
}
