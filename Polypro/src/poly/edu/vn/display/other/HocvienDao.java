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
public class HocvienDao {
public void insert(HocVien model){
 String sql="INSERT INTO HocVien(MaKH, MaNH, Diem) VALUES(?, ?, ?)";
 JdbcHelper.executeUpdate(sql,
 model.getMaKH(),
 model.getMaNH(),
 model.getDiem());
 }

 public void update(HocVien model){
 String sql="UPDATE HocVien SET MaKH=?, MaNH=?, Diem=? WHERE MaHV=?";
 JdbcHelper.executeUpdate(sql,
 model.getMaKH(),
 model.getMaNH(),
 model.getDiem(),
 model.getMaHV());
 }

 public void delete(Integer MaHV){
 String sql="DELETE FROM HocVien WHERE MaHV=?";
 JdbcHelper.executeUpdate(sql, MaHV);
 }

 public List<HocVien> select(){
 String sql="SELECT * FROM HocVien";
 return select(sql);
 }

 public HocVien findById(Integer MaHV){
 String sql="SELECT * FROM HocVien WHERE MaHV=?";
 List<HocVien> list = select(sql, MaHV);
 return list.size() > 0 ? list.get(0) : null;
 }
 private List<HocVien> select(String sql, Object...args){
 List<HocVien> list = new ArrayList<>();
 try {
 ResultSet rs = null;
 try {
 rs = JdbcHelper.executeQuery(sql, args);
 while(rs.next()){
 HocVien model=readFromResultSet(rs);
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

 private HocVien readFromResultSet(ResultSet rs) throws SQLException{
 HocVien model=new HocVien();
 model.setMaHV(rs.getInt("maHV"));
 model.setMaKH(rs.getInt("maKH"));
 model.setMaNH(rs.getString("maNH"));
 model.setDiem(rs.getDouble("diem"));
 return model;
 }

    
}

