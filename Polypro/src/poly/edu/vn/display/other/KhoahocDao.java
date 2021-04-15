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
public class KhoahocDao {
 public void insert(KhoaHoc model){
 String sql="INSERT INTO KhoaHoc (MaCD, HocPhi, ThoiLuong, NgayKG, GhiChu, MaNV) VALUES (?, ?, ?, ?, ?, ?)";
 JdbcHelper.executeUpdate(sql,
 model.getMaCD(),
 model.getHocPhi(),
 model.getThoiLuong(),
 model.getNgayKG(),
 model.getGhiChu(),
 model.getMaNV());
 }

 public void update(KhoaHoc model){
 String sql="UPDATE KhoaHoc SET MaCD=?, HocPhi=?, ThoiLuong=?, NgayKG=?, GhiChu=?, MaNV=? WHERE MaKH=?";
 JdbcHelper.executeUpdate(sql, 
model.getMaCD(),
 model.getHocPhi(),
 model.getThoiLuong(),
 model.getNgayKG(),
 model.getGhiChu(),
 model.getMaNV(),
 model.getMaKH());
 }

 public void delete(Integer MaKH){
 String sql="DELETE FROM KhoaHoc WHERE MaKH=?";
 JdbcHelper.executeUpdate(sql, MaKH);
 }

 public List<KhoaHoc> select(){
 String sql="SELECT * FROM KhoaHoc";
 return select(sql);
 }

 public KhoaHoc findById(Integer makh){
 String sql="SELECT * FROM KhoaHoc WHERE MaKH=?";
 List<KhoaHoc> list = select(sql, makh);
 return list.size() > 0 ? list.get(0) : null;
 }

 private List<KhoaHoc> select(String sql, Object...args){
 List<KhoaHoc> list=new ArrayList<>();
 try {
 ResultSet rs = null;
 try {
 rs = JdbcHelper.executeQuery(sql, args);
 while(rs.next()){
 KhoaHoc model = readFromResultSet(rs);
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

 private KhoaHoc readFromResultSet(ResultSet rs) throws SQLException{
 KhoaHoc model=new KhoaHoc();
 model.setMaKH(rs.getInt("MaKH"));
 model.setHocPhi(rs.getDouble("HocPhi"));
 model.setThoiLuong(rs.getInt("ThoiLuong"));
 model.setNgayKG(rs.getDate("NgayKG"));
 model.setGhiChu(rs.getString("GhiChu"));
 model.setMaNV(rs.getString("MaNV"));
 model.setNgayTao(rs.getDate("NgayTao"));
 model.setMaCD(rs.getString("MaCD"));

 return model;
 }
}

