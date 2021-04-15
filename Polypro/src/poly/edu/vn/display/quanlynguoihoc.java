/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.edu.vn.display;

import java.awt.HeadlessException;

import java.util.List;
import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import poly.edu.vn.display.other.NguoiHoc;

import poly.edu.vn.display.other.DateHelper;
import poly.edu.vn.display.other.DialogHelper;
import poly.edu.vn.display.other.NguochocDao;
import poly.edu.vn.display.other.ShareHelper;


public class quanlynguoihoc extends javax.swing.JFrame {
int index = 0;
    NguochocDao dao = new NguochocDao();
    /**
     * Creates new form quanlynguoihoc
     */
    public quanlynguoihoc() {
         initComponents(); 
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

    }
 public  boolean check(){
        if(txtma.getText().isEmpty() || txtngaysinh.getText().isEmpty() || txtsodt.getText().isEmpty()
           || txthoten.getText().isEmpty() || txtsodt.getText().isEmpty()    ){
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin");
            return false;
        }else if (!(txtemail.getText()).matches("^[\\w-_\\.]+\\@[\\w&&[^0-9]]+\\.com$")) {
            JOptionPane.showMessageDialog(rootPane, "Sai định dạng email","Error",1);
            txtemail.requestFocus();
            return false;
        }
        else if (!(txtma.getText()).matches("CD[0-9]{1,5}")) {
            JOptionPane.showMessageDialog(rootPane, "Sai định dạng mã \n VD : NV01","Error",1);
            txtma.requestFocus();
            return false;
        }
        else if (!(txtsodt.getText()).matches("[0-9]{1,11}")) {
            JOptionPane.showMessageDialog(rootPane, "Học phí phải nhập số","Error",1);
            txtsodt.requestFocus();
            return false;
        }
        List<NguoiHoc> list = dao.select();
        for(int i = 0; i<list.size();i++){
            if(txtma.getText().equals(list.get(i).getMaNH())){
                JOptionPane.showMessageDialog(this, "Trùng mã người học");
                return false;
            }
        }
        return true;
    
    };
    
    void load() { 
        DefaultTableModel model = (DefaultTableModel) tblQLNH.getModel(); 
        model.setRowCount(0); 
        try { 
            String keyword = txttimkiem.getText(); 
            List<NguoiHoc> list = dao.selectByKeyword(keyword); 
            for (NguoiHoc nh : list) { 
                Object[] row = { 
                    nh.getMaNH(), 
                    nh.getHoten(), 
                    nh.isGioitinh()?"Nam":"Nữ", 
                    DateHelper.toString(nh.getNgaysinh()), 
                    nh.getDienthoai(), 
                    nh.getGmail(),
                    
                    nh.getMaNV(), 
                    DateHelper.toString(nh.getNgayDK()) ,
                        nh.getGhichu()
                }; 
                model.addRow(row); 
            } 
        }  
        catch (Exception e) { 
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!"); 
        } 
    } ;
 
 
         void insert(){ 
        NguoiHoc model = getModel(); 
        try { 
            dao.insert(model); 
            this.load(); 
            this.clear(); 
            DialogHelper.alert(this, "Thêm mới thành công!"); 
        }  
        catch (Exception e) { 
            e.printStackTrace();
            DialogHelper.alert(this, "Thêm mới thất bại!"); 
        } 
    } 
     
      void update(){ 
        NguoiHoc model = getModel(); 
        try { 
            dao.update(model); 
            this.load(); 
            DialogHelper.alert(this, "Cập nhật thành công!"); 
        }  
        catch (Exception e) { 
            DialogHelper.alert(this, "Cập nhật thất bại!"); 
        }
      }
       void delete(){ 
        if(DialogHelper.confirm(this, "Bạn thực sự muốn xóa người học này?")){ 
            String manh = txtma.getText(); 
            try { 
                dao.delete(manh); 
                this.load(); 
                this.clear(); 
                DialogHelper.alert(this, "Xóa thành công!"); 
            }catch(HeadlessException e) { 
                DialogHelper.alert(this, "Xóa thất bại!"); 
            }         
      }
       }
        void clear(){ 
        NguoiHoc model = new NguoiHoc(); 
        model.setMaNV(ShareHelper.USER.getMaNV()); 
        model.setNgayDK(DateHelper.now()); 
        this.setModel(model); 
        
    } 
       void edit() { 
        try { 
            String manh = (String) tblQLNH.getValueAt(this.index, 0); 
            NguoiHoc model = dao.findById(manh); 
            if(model != null){ 
                this.setModel(model); 
                this.setStatus(false); 
            } 
        }  
        catch (Exception e) { 
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!"); 
        } 
    }
         void setModel(NguoiHoc model){ 
        txtma.setText(model.getMaNH()); 
        txthoten.setText(model.getHoten());
        cbogioitinh.setSelectedIndex(model.isGioitinh()?0:1); 
        txtngaysinh.setText(DateHelper.toString(model.getNgaysinh())); 
        txtsodt.setText(model.getDienthoai()); 
        txtemail.setText(model.getGmail()); 
        txtghichu.setText(model.getGhichu()); 
        
 
    } 
     NguoiHoc getModel() { 
       NguoiHoc model = new NguoiHoc(); 
        model.setMaNH(txtma.getText()); 
        model.setHoten(txthoten.getText());
        model.setGioitinh(cbogioitinh.getSelectedIndex() == 0); 
        model.setNgaysinh(DateHelper.toDate(txtngaysinh.getText()));  
        model.setDienthoai(txtsodt.getText()); 
        model.setGmail(txtemail.getText()); 
        model.setGhichu(txtghichu.getText()); 
        model.setMaNV(ShareHelper.USER.getMaNV()); 
        model.setNgayDK(DateHelper.now()); 
        return model; 
 
  
    } 
        void setStatus(boolean insertable){ 
        txtma.setEditable(insertable); 
        btnthem.setEnabled(insertable); 
        btnsua.setEnabled(!insertable); 
        btnxoa.setEnabled(!insertable); 
        
        boolean first = this.index > 0; 
        boolean last = this.index < tblQLNH.getRowCount() - 1; 
        btnFirst.setEnabled(!insertable && first); 
        btnprev.setEnabled(!insertable && first); 
        btnlast.setEnabled(!insertable && last); 
        btnnext.setEnabled(!insertable && last); 
    }
  
   /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtma = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        txthoten = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        cbogioitinh = new javax.swing.JComboBox<>();
        jLabel5 = new javax.swing.JLabel();
        txtngaysinh = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtsodt = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtemail = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtghichu = new javax.swing.JTextArea();
        btnthem = new javax.swing.JButton();
        btnsua = new javax.swing.JButton();
        btnxoa = new javax.swing.JButton();
        btnmoi = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnprev = new javax.swing.JButton();
        btnnext = new javax.swing.JButton();
        btnlast = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblQLNH = new javax.swing.JTable();
        jPanel6 = new javax.swing.JPanel();
        txttimkiem = new javax.swing.JTextField();
        btntimkiem = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HỆ THỐNG QUẢN LÝ ĐÀO TẠO");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 102));
        jLabel1.setText("QUẢN LÝ NGƯỜI HỌC");

        jLabel2.setText("Mã người học");

        jLabel3.setText("Họ và tên");

        txthoten.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txthotenActionPerformed(evt);
            }
        });

        jLabel4.setText("Giới tính");

        cbogioitinh.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Nam", "Nữ", " " }));

        jLabel5.setText("Ngày sinh");

        jLabel6.setText("Số điện thoại");

        jLabel7.setText("Email");

        jLabel8.setText("Ghi chú");

        txtghichu.setColumns(20);
        txtghichu.setRows(5);
        jScrollPane3.setViewportView(txtghichu);

        btnthem.setText("Thêm");
        btnthem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemActionPerformed(evt);
            }
        });

        btnsua.setText("Sửa");
        btnsua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsuaActionPerformed(evt);
            }
        });

        btnxoa.setText("Xóa");
        btnxoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoaActionPerformed(evt);
            }
        });

        btnmoi.setText("Mới");
        btnmoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnmoiActionPerformed(evt);
            }
        });

        btnFirst.setText("|<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnprev.setText("<<");
        btnprev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnprevActionPerformed(evt);
            }
        });

        btnnext.setText(">>");
        btnnext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnextActionPerformed(evt);
            }
        });

        btnlast.setText(">|");
        btnlast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnlastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txthoten)
                            .addComponent(txtma)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(13, 13, 13)
                                        .addComponent(jLabel2))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(0, 0, Short.MAX_VALUE)))
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4)
                                .addGap(248, 248, 248)
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cbogioitinh, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel6))
                                    .addComponent(txtsodt, javax.swing.GroupLayout.PREFERRED_SIZE, 237, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(41, 41, 41)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtngaysinh)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addComponent(jLabel7)
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txtemail))))
                        .addGap(10, 10, 10))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                        .addComponent(jScrollPane3)
                        .addContainerGap())
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel8)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addComponent(btnthem)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnsua)
                                .addGap(14, 14, 14)
                                .addComponent(btnxoa)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnmoi)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 206, Short.MAX_VALUE)
                                .addComponent(btnFirst)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(btnprev)
                                .addGap(18, 18, 18)
                                .addComponent(btnnext)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnlast)))
                        .addContainerGap())))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtma, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txthoten, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtngaysinh, javax.swing.GroupLayout.DEFAULT_SIZE, 29, Short.MAX_VALUE)
                    .addComponent(cbogioitinh))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtemail, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtsodt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 26, Short.MAX_VALUE)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnthem)
                    .addComponent(btnsua)
                    .addComponent(btnxoa)
                    .addComponent(btnmoi)
                    .addComponent(btnFirst)
                    .addComponent(btnprev)
                    .addComponent(btnnext)
                    .addComponent(btnlast))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabs.addTab("CẬP NHẬP", jPanel2);

        tblQLNH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NH", "HỌ VÀ TÊN", "GIỚI TÍNH", "ĐIỆN THOẠI", "NGÀY SINH", "EMAIL", "MÃ NV", "NGÀY DK", "Ghi chú"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, true
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblQLNH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQLNHMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblQLNH);

        jPanel6.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        btntimkiem.setText("Tìm");
        btntimkiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btntimkiemActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(txttimkiem)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btntimkiem)
                .addContainerGap())
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txttimkiem)
                    .addComponent(btntimkiem))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 672, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addComponent(jPanel6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 386, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        tabs.addTab("DANH SÁCH", jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 697, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txthotenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txthotenActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txthotenActionPerformed

    private void btnthemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemActionPerformed
         // TODO add your handling code here:
         try{
             if(check()){
             this.insert();
         }
         }catch(Exception e){
             DialogHelper.alert(this, "Lỗi, Vui lòng xem lại");
         }
         
    }//GEN-LAST:event_btnthemActionPerformed

    private void btnsuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnsuaActionPerformed
         // TODO add your handling code here:
         this.update();
    }//GEN-LAST:event_btnsuaActionPerformed

    private void btnxoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnxoaActionPerformed
         // TODO add your handling code here:
         this.delete();
    }//GEN-LAST:event_btnxoaActionPerformed

    private void btnmoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmoiActionPerformed
         // TODO add your handling code here:
          this.clear();
          btnmoi.setEnabled(true);
          btnthem.setEnabled(false); 
        btnsua.setEnabled(!false);
    }//GEN-LAST:event_btnmoiActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
         // TODO add your handling code here:
         this.load();
        this.setStatus(true);
    }//GEN-LAST:event_formWindowOpened

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        this.index=0;
        this.edit();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnprevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnprevActionPerformed
        // TODO add your handling code here:
        this.index--; 
        this.edit(); 
    }//GEN-LAST:event_btnprevActionPerformed

    private void btnnextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnextActionPerformed
        // TODO add your handling code here:
        this.index++;
        this.edit();
    }//GEN-LAST:event_btnnextActionPerformed

    private void btnlastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnlastActionPerformed
        // TODO add your handling code here:
        this.index=tblQLNH.getRowCount()-1;
        this.edit();
    }//GEN-LAST:event_btnlastActionPerformed

    private void tblQLNHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQLNHMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount()==2){
            this.index = tblQLNH.rowAtPoint(evt.getPoint());
            if(this.index>=0){
                this.edit();
                tabs.setSelectedIndex(0);
            }
        }
    }//GEN-LAST:event_tblQLNHMouseClicked

    private void btntimkiemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btntimkiemActionPerformed
          // TODO add your handling code here:
          this.load();
    }//GEN-LAST:event_btntimkiemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(quanlynguoihoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(quanlynguoihoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(quanlynguoihoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(quanlynguoihoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new quanlynguoihoc().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnlast;
    private javax.swing.JButton btnmoi;
    private javax.swing.JButton btnnext;
    private javax.swing.JButton btnprev;
    private javax.swing.JButton btnsua;
    private javax.swing.JButton btnthem;
    private javax.swing.JButton btntimkiem;
    private javax.swing.JButton btnxoa;
    private javax.swing.JComboBox<String> cbogioitinh;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblQLNH;
    private javax.swing.JTextField txtemail;
    private javax.swing.JTextArea txtghichu;
    private javax.swing.JTextField txthoten;
    private javax.swing.JTextField txtma;
    private javax.swing.JTextField txtngaysinh;
    private javax.swing.JTextField txtsodt;
    private javax.swing.JTextField txttimkiem;
    // End of variables declaration//GEN-END:variables
}
