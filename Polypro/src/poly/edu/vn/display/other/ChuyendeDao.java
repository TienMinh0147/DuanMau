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


public class ChuyendeDao {
    //câu lệnh thêm dữ liệu vào sql
    public void insert(ChuyenDe model){ 
        String sql="INSERT INTO chuyenDe  VALUES (?, ?, ?, ?, ?, ?)";
        JdbcHelper.executeUpdate(sql,
                model.getMaCD(),
                model.getTenCD(),
                model.getHocPhi(),
                model.getThoiLuong(),
                model.getHinh(),
                model.getMoTa());
    }
    //câu lệnh cập nhập dữ liệu vào sql
     public void update(ChuyenDe model){
         String sql="UPDATE chuyenDe SET TenCD=?, HocPhi=?, ThoiLuong=?, Hinh=?, MoTa=? WHERE MaCD=?";
         JdbcHelper.executeUpdate(sql,
                 model.getTenCD(),
                 model.getHocPhi(),
                 model.getThoiLuong(),
                 model.getHinh(),
                 model.getMoTa(),
                 model.getMaCD());
     } 
     //câu lệnh xóa dữ liệu  sql
      public void delete(String MaCD){
          String sql="DELETE FROM chuyenDe WHERE MaCD=?";
          JdbcHelper.executeUpdate(sql, MaCD);
      }
      
      public List<ChuyenDe> select(){ 
          String sql="SELECT * FROM chuyenDe";
          return select(sql);
      }
      public void tim(){
          String sql="SELECT * FROM chuyenDe";
      };
      public ChuyenDe findById(String macd){
          String sql="SELECT * FROM chuyenDe WHERE MaCD=?";
          List<ChuyenDe> list = select(sql, macd);
          return list.size() > 0 ? list.get(0) : null;
      }  
      
       private List<ChuyenDe> select(String sql, Object...args){
           List<ChuyenDe> list=new ArrayList<>();  
       try {
         ResultSet rs = null;
       try {
         rs = JdbcHelper.executeQuery(sql, args);
         while(rs.next()){ 
             ChuyenDe model=readFromResultSet(rs);
             list.add(model);
         }
       }
       finally{
           rs.getStatement().getConnection().close();
         }
       } 
       catch (SQLException ex) {
           throw new RuntimeException(ex);
        }
       return list;         
  }
      private ChuyenDe readFromResultSet(ResultSet rs) throws SQLException{ 
          ChuyenDe model=new ChuyenDe();
          model.setMaCD(rs.getString("MaCD"));
          model.setTenCD(rs.getString("TenCD"));
          model.setHocPhi(rs.getDouble("HocPhi"));
          model.setThoiLuong(rs.getInt("ThoiLuong"));       
          model.setHinh(rs.getString("hinh"));
          model.setMoTa(rs.getString("MoTa"));
          return model; 
      }
}
