/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.edu.vn.display.other;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Admin
 */
public class NhanvienDao {
     public void insert(NhanVien model){ 
         String sql="INSERT INTO nhanvien (Manv, Matkhau, Hoten, Vaitro) VALUES (?, ?, ?, ?)";
     JdbcHelper.executeUpdate(sql,
             model.getMaNV(), 
             model.getMatKhau(),
             model.getHoTen(),
             model.isVaiTro());
}    
     public void update(NhanVien model){ 
String sql="UPDATE nhanvien SET Matkhau=?, Hoten=?, Vaitro=? WHERE Manv=?"; 
    JdbcHelper.executeUpdate(sql,
            model.getMatKhau(),
            model.getHoTen(),
            model.isVaiTro(),
            model.getMaNV()); 
    
}
     public void update1(Doipass model){ 
String sql="UPDATE nhanvien SET Matkhau=? WHERE Manv=?"; 
    JdbcHelper.executeUpdate(sql,
            model.getMatkhau(),
            model.getMaNV()); 
    
}
    public void delete(String MaNV){ 
     String sql="DELETE FROM nhanvien WHERE Manv=?";  
   JdbcHelper.executeUpdate(sql, MaNV);   
}   
    public List<NhanVien> select(){ 
        String sql="SELECT * FROM Nhanvien"; 
        return select(sql);
    } 
    
    public NhanVien findById(String manv){
        String sql="SELECT * FROM Nhanvien WHERE Manv=?";
        List<NhanVien> list = select(sql, manv); 
        return list.size() > 0 ? list.get(0) : null;
    }
    
    private List<NhanVien> select(String sql, Object...args){ 
        List<NhanVien> list=new ArrayList<>(); 
        try { 
            ResultSet rs = null;
            try {
                rs = JdbcHelper.executeQuery(sql, args);
                while(rs.next()){
                    NhanVien model=readFromResultSet(rs);
                    list.add(model); 
                }
            }  
            finally{
                rs.getStatement().getConnection().close();
            }
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }         return list;
    }
    private NhanVien readFromResultSet(ResultSet rs) throws SQLException{
        NhanVien model=new NhanVien();
        model.setMaNV(rs.getString("Manv"));
        model.setMatKhau(rs.getString("Matkhau"));
        model.setHoTen(rs.getString("Hoten"));
        model.setVaiTro(rs.getBoolean("Vaitro"));
        return model;
    }  


}
