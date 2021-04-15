/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.edu.vn.display.other;

import java.io.Serializable;

public class Doipass implements Serializable {
    String matkhau;
    String MaNV;
    
     
   public Doipass(){};

    public Doipass(String matkhau, String MaNV) {
        this.matkhau = matkhau;
        this.MaNV = MaNV;
    }

    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    public String getMaNV() {
        return MaNV;
    }

    public void setMaNV(String MaNV) {
        this.MaNV = MaNV;
    }

    
    
}
