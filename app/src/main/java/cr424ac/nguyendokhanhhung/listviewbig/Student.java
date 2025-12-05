package cr424ac.nguyendokhanhhung.listviewbig;

import java.io.Serializable;
import java.util.ArrayList;

public class Student implements Serializable {
    private String hoten, masv, sdt, email, ngaysinh, khoa, gt, avatar;
    private ArrayList<String> sothich;

    public Student(String hoten, String masv, String sdt, String email, String ngaysinh, String khoa, String gt, String avatar) {
        this.hoten = hoten;
        this.masv = masv;
        this.sdt = sdt;
        this.email = email;
        this.ngaysinh = ngaysinh;
        this.khoa = khoa;
        this.avatar = avatar;
        this.gt = gt;
        this.sothich = new ArrayList<>();
    }

    public String getHoten() { return hoten; }
    public String getAvatar() { return avatar;}
        public String getMasv() { return masv; }
    public String getSdt() { return sdt; }
    public String getEmail() { return email; }
    public String getNgaysinh() { return ngaysinh; }
    public String getKhoa() { return khoa; }
    public String getGt() { return gt; }
    public ArrayList<String> getSothich() { return sothich; }

    public void setSothich(ArrayList<String> sothich) { this.sothich = sothich; }
    public void setAvatar(String avatar) {this.avatar = avatar;}

    @Override
    public String toString() {
        return hoten + "\n" + "SĐT: " + sdt + "\nMã SV: " + masv;
    }
}
