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


public class NguochocDao {
    public static void insert(NguoiHoc model){ 
          String sql="INSERT INTO NguoiHoc (MaNH, HoTen,  NgaySinh, GioiTinh,  DienThoai, Email, GhiChu, MaNV) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
          JdbcHelper.executeUpdate(sql,
                  model.getMaNH(),
                  model.getHoten(),   
                  model.getNgaysinh(),
                  model.isGioitinh(),
                  model.getDienthoai(),
                  model.getGmail(),
                  model.getGhichu(),
                  model.getMaNV());
      } 
      public void update(NguoiHoc model){
           String sql="UPDATE NguoiHoc SET HoTen=?, NgaySinh=?, GioiTinh=?, DienThoai=?, Email=?, GhiChu=?, MaNV=? WHERE MaNH=?";
     
           JdbcHelper.executeUpdate(sql,
               model.getHoten(),
               model.getNgaysinh(),
               model.isGioitinh(),
               model.getDienthoai(),
               model.getGmail(), 
               model.getGhichu(), 
               model.getMaNV(),
               model.getMaNH());
   } 
      
     public void delete(String id){
     String sql="DELETE FROM NguoiHoc WHERE MaNH=?";
     JdbcHelper.executeUpdate(sql, id);   
}    
     public List<NguoiHoc> select(){
         String sql="SELECT * FROM NguoiHoc"; 
         return select(sql);
}   
     public List<NguoiHoc> selectByKeyword(String keyword){
     String sql="SELECT * FROM NguoiHoc WHERE HoTen LIKE ?";
     return select(sql,"%"+keyword+"%");
}
     public List<NguoiHoc> selectByCourse(Integer makh){
     String sql="SELECT * FROM NguoiHoc WHERE MaNH NOT IN (SELECT MaNH FROM HocVien WHERE MaKH=?)"; 
     return select(sql, makh);
}
     public NguoiHoc findById(String manh){
     String sql="SELECT * FROM NguoiHoc WHERE MaNH=?";
     List<NguoiHoc> list = select(sql, manh);
     return list.size() > 0 ? list.get(0) : null;
}         
     private List<NguoiHoc> select(String sql, Object...args){
     List<NguoiHoc> list=new ArrayList<>();
       try { 
       ResultSet rs = null; 
       try {
     rs = JdbcHelper.executeQuery(sql, args);
      while(rs.next()){
      NguoiHoc model=readFromResultSet(rs);
      list.add(model); 
          } 
     }
     finally{ 
       rs.getStatement().getConnection().close();
          }
       
       }catch (SQLException ex) {
           throw new RuntimeException(ex);
       } 
       return list;
     } 
     
      private NguoiHoc readFromResultSet(ResultSet rs) throws SQLException{ 
      NguoiHoc model=new NguoiHoc();
      model.setMaNH(rs.getString("MaNH"));
      model.setHoten(rs.getString("HoTen"));
      model.setNgaysinh(rs.getDate("NgaySinh"));
      model.setGioitinh(rs.getBoolean("GioiTinh"));
      model.setDienthoai(rs.getString("DienThoai"));
      model.setGmail(rs.getString("Email")); 
      model.setGhichu(rs.getString("GhiChu"));
      model.setMaNV(rs.getString("MaNV")); 
      model.setNgayDK(rs.getDate("NgayDK"));
      
      return model;     }
}
