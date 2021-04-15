/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.edu.vn.display;

import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import poly.edu.vn.display.other.DialogHelper;
import poly.edu.vn.display.other.NhanVien;
import poly.edu.vn.display.other.NhanvienDao;
import poly.edu.vn.display.other.ShareHelper;




public class Quanlinhanvienquantri extends javax.swing.JFrame {
int index = 0;  
  NhanvienDao dao = new NhanvienDao(); 
    
    /**
     * Creates new form Quanlinhanvien
     */
    public Quanlinhanvienquantri() {
      
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

    }
    
    public boolean check() {
    
        if (txtnhanvien.getText().equals("") || txthoten.getText().equals("") || txtmatkhau.getText().equals("")
                || txtxacnhanmk.getText().equals("")  ) {
            JOptionPane.showMessageDialog(rootPane, "Hãy nhập đủ dữ liệu sau đó ấn Save","Error",1);
            return false;
            
        } else if (!rdonhanvien.isSelected() && !rdotruongphong.isSelected()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn Vai trò","Error",1);
            return false;
        }else if (!(txtnhanvien.getText()).matches("NV[0-9]{1,5}")) {
            JOptionPane.showMessageDialog(rootPane, "Sai định dạng mã \n VD : NV01","Error",1);
            txtnhanvien.requestFocus();
            return false;
        }
       List<NhanVien> list = dao.select(); 
                for(int i=0; i< list.size();i++){
            if(txtnhanvien.getText().equalsIgnoreCase(list.get(i).getMaNV())){
                JOptionPane.showMessageDialog(this, "Trùng Mã Nhân Viên","Error",1);
                return false;
            }
        }
        
        
        
       
        return true;
    };
    
    
    public boolean check1() {
    
        if (txtnhanvien.getText().equals("") || txthoten.getText().equals("") || txtmatkhau.getText().equals("")
                || txtxacnhanmk.getText().equals("")  ) {
            JOptionPane.showMessageDialog(rootPane, "Hãy nhập đủ dữ liệu sau đó ấn Update","Error",1);
            return false;
            
        } else if (!rdonhanvien.isSelected() && !rdotruongphong.isSelected()) {
            JOptionPane.showMessageDialog(this, "Bạn chưa chọn Vai trò","Error",1);
            return false;
        }else if (!(txtnhanvien.getText()).matches("NV[0-9]{2,3}")) {
            JOptionPane.showMessageDialog(rootPane, "Sai định dạng mã \n VD : NV01","Error",1);
            txtnhanvien.requestFocus();
            return false;
        }
       
        
        
        
       
        return true;
    };
    
    
    
    void init(){ 
        setIconImage(ShareHelper.APP_ICON); 
        
        }
        void load() { 
        DefaultTableModel model = (DefaultTableModel) tblQLNV.getModel(); 
        model.setRowCount(0); 
        try { 
            List<NhanVien> list = dao.select(); 
            for (NhanVien nv : list) { 
                Object[] row = { 
                    nv.getMaNV(), 
                    nv.getMatKhau(), 
                    nv.getHoTen(), 
                    nv.isVaiTro()?"Trưởng phòng":"Nhân viên" 
        }; 
                model.addRow(row); 
            } 
        }  
        catch (Exception e) { 
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!"); 
        } 
    } 
     void insert(){
         NhanVien model = getModel();
         String confirm = new String(txtxacnhanmk.getPassword());
         if(confirm.equals(model.getMatKhau())){
             try{
                 dao.insert(model);
                 this.load();
                 this.clear();
                  DialogHelper.alert(this, "Thêm mới thành công!");
             }catch(Exception e){
                 DialogHelper.alert(this,"Thêm mới thất bại!");
             }
         }else{
             DialogHelper.alert(this, "xác Nhập mật khẩu không đúng!");
         }
     } 
     void update(){
         NhanVien model = getModel();
         String confirm = new String(txtxacnhanmk.getPassword());
         if(!confirm.equals(model.getMatKhau())){
             DialogHelper.alert(this, "xác nhận mật khẩu không đúng");
         }else{
             try{
                 dao.update(model);
                 this.load();
                 DialogHelper.alert(this, "Cập nhập Thành công!");
             }catch(Exception a){
                 DialogHelper.alert(this, "Cập nhập thất bại!");
             }
         }
     }
     void delete(){
         if(DialogHelper.confirm(this, "bạn có thực sự muốn xóa nhân viên này không?")){
         String maNV = txtnhanvien.getText();
         try{
             dao.delete(maNV);
             this.load();
             this.clear();
             DialogHelper.alert(this, "Xóa thành công!");
         }catch(Exception a){
             DialogHelper.alert(this, "Xóa Thất bại!");
       }    
      }
    }
    void edit(){
        try{
            String manv = (String) tblQLNV.getValueAt(this.index,0);
            NhanVien model = dao.findById(manv);
            if(model != null){
              this.setModel(model);
               this.setStatus(false);
              }
            }catch(Exception a){
                DialogHelper.alert(this, "lỗi truy vấn dữ liệu!");
         }
    }  
     void clear(){
            this.setModel(new NhanVien());
            this.setStatus(true);
        }
     void setModel(NhanVien model){
         txtnhanvien.setText(model.getMaNV());
         txthoten.setText(model.getMaNV());
         txtmatkhau.setText(model.getMatKhau()); 
         txtxacnhanmk.setText(model.getMatKhau()); 
         rdotruongphong.setSelected(model.isVaiTro());
         rdonhanvien.setSelected(!model.isVaiTro());
     }
     NhanVien getModel(){ 
         NhanVien model = new NhanVien(); 
         model.setMaNV(txtnhanvien.getText()); 
         model.setHoTen(txthoten.getText()); 
         model.setMatKhau(new String(txtmatkhau.getPassword())); 
         model.setVaiTro(rdotruongphong.isSelected()); 
         return model;
     }
    void setStatus(boolean insertable){ 
        txtnhanvien.setEditable(insertable); 
        btnThem.setEnabled(insertable); 
        btnSua.setEnabled(!insertable); 
        btnXoa.setEnabled(!insertable); 
        boolean first = this.index > 0; 
        boolean last = this.index < tblQLNV.getRowCount() - 1; 
        btnFirst.setEnabled(!insertable && first); 
        btnPrev.setEnabled(!insertable && first); 
        btnnext.setEnabled(!insertable && last); 
        btnLast.setEnabled(!insertable && last); 
    } 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenu1 = new javax.swing.JMenu();
        jFrame1 = new javax.swing.JFrame();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jTextField5 = new javax.swing.JTextField();
        btn = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtnhanvien = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        txthoten = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        rdotruongphong = new javax.swing.JRadioButton();
        rdonhanvien = new javax.swing.JRadioButton();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnMoi = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnnext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        txtmatkhau = new javax.swing.JPasswordField();
        txtxacnhanmk = new javax.swing.JPasswordField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblQLNV = new javax.swing.JTable();

        jMenu1.setText("jMenu1");

        javax.swing.GroupLayout jFrame1Layout = new javax.swing.GroupLayout(jFrame1.getContentPane());
        jFrame1.getContentPane().setLayout(jFrame1Layout);
        jFrame1Layout.setHorizontalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        jFrame1Layout.setVerticalGroup(
            jFrame1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );

        jMenuItem1.setText("jMenuItem1");

        jMenu2.setText("jMenu2");

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jTextField5.setText("jTextField5");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HỆ THỐNG QUẢN LÝ ĐÀO TẠO");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setText("QUẢN LÝ NHÂN VIÊN QUẢN TRỊ");

        jLabel2.setText("Mã nhân viên");

        jLabel3.setText("Mật khẩu");

        jLabel4.setText("Xác nhận mật khẩu");

        jLabel5.setText("Họ và tên");

        jLabel6.setText("Vai trò");

        btn.add(rdotruongphong);
        rdotruongphong.setText("Trưởng phòng");
        rdotruongphong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdotruongphongActionPerformed(evt);
            }
        });

        btn.add(rdonhanvien);
        rdonhanvien.setText("Nhân viên");
        rdonhanvien.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                rdonhanvienActionPerformed(evt);
            }
        });

        btnThem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Save.png"))); // NOI18N
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Sync.png"))); // NOI18N
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Delete.png"))); // NOI18N
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnMoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Add.png"))); // NOI18N
        btnMoi.setText("Mới");
        btnMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMoiActionPerformed(evt);
            }
        });

        btnFirst.setText("|<");
        btnFirst.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFirstActionPerformed(evt);
            }
        });

        btnPrev.setText("<<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnnext.setText(">>");
        btnnext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnnextActionPerformed(evt);
            }
        });

        btnLast.setText(">|");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        txtxacnhanmk.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtxacnhanmkActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(txtxacnhanmk, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtmatkhau, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtnhanvien, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txthoten, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel6, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(rdotruongphong)
                                .addGap(30, 30, 30)
                                .addComponent(rdonhanvien)))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(btnThem)
                        .addGap(2, 2, 2)
                        .addComponent(btnSua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnXoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnMoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFirst)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnnext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLast)))
                .addGap(2, 2, 2))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtnhanvien, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addGap(7, 7, 7)
                .addComponent(txtmatkhau, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel4)
                .addGap(13, 13, 13)
                .addComponent(txtxacnhanmk, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txthoten, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(rdotruongphong)
                    .addComponent(rdonhanvien))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnMoi)
                    .addComponent(btnFirst, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPrev, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnnext, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnLast, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("CẬP NHẬP", jPanel1);

        tblQLNV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ NV", "MẬT KHẨU", "HỌ VÀ TÊN", "VAI TRÒ"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblQLNV.addAncestorListener(new javax.swing.event.AncestorListener() {
            public void ancestorMoved(javax.swing.event.AncestorEvent evt) {
            }
            public void ancestorAdded(javax.swing.event.AncestorEvent evt) {
                tblQLNVAncestorAdded(evt);
            }
            public void ancestorRemoved(javax.swing.event.AncestorEvent evt) {
            }
        });
        tblQLNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQLNVMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblQLNV);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 593, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 361, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabs.addTab("DANH SÁCH", jPanel2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(tabs)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 408, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
    if(check()){
        this.insert();
    }
    
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
          // TODO add your handling code here:
       if(check1()){
           this.update();
       }
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
         // TODO add your handling code here:
       String checkmanv = txtnhanvien.getText();
        if (ShareHelper.USER.getMaNV().equals(checkmanv)) {
            DialogHelper.alert(this, "không xóa chính mình");
        } else {
            this.delete();
        }
    }//GEN-LAST:event_btnXoaActionPerformed

    private void btnMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMoiActionPerformed
        // TODO add your handling code here:
        this.clear();
    }//GEN-LAST:event_btnMoiActionPerformed

    private void tblQLNVAncestorAdded(javax.swing.event.AncestorEvent evt) {//GEN-FIRST:event_tblQLNVAncestorAdded
        // TODO add your handling code here:
    }//GEN-LAST:event_tblQLNVAncestorAdded

    private void tblQLNVMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQLNVMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){ 
            this.index = tblQLNV.rowAtPoint(evt.getPoint()); 
            if (this.index >= 0) { 
                this.edit(); 
                tabs.setSelectedIndex(0); 
            }  
           }
    }//GEN-LAST:event_tblQLNVMouseClicked

    private void txtxacnhanmkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtxacnhanmkActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtxacnhanmkActionPerformed

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
         // TODO add your handling code here:
       this.index=0;
        this.edit();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
         this.index--;
        this.edit();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnnextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnnextActionPerformed
        // TODO add your handling code here:
         this.index++;
        this.edit();
    }//GEN-LAST:event_btnnextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
         // TODO add your handling code here:
         this.index = tblQLNV.getRowCount();
        this.edit();
    }//GEN-LAST:event_btnLastActionPerformed

    private void rdotruongphongActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdotruongphongActionPerformed
        // TODO add your handling code here:
      
    }//GEN-LAST:event_rdotruongphongActionPerformed

    private void rdonhanvienActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_rdonhanvienActionPerformed
         // TODO add your handling code here:
         
    }//GEN-LAST:event_rdonhanvienActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
         // TODO add your handling code here:
         this.load();
        this.setStatus(true);
    }//GEN-LAST:event_formWindowOpened

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
            java.util.logging.Logger.getLogger(Quanlinhanvienquantri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Quanlinhanvienquantri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Quanlinhanvienquantri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Quanlinhanvienquantri.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Quanlinhanvienquantri().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup btn;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnMoi;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnnext;
    private javax.swing.JFrame jFrame1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTable jTable2;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JRadioButton rdonhanvien;
    private javax.swing.JRadioButton rdotruongphong;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblQLNV;
    private javax.swing.JTextField txthoten;
    private javax.swing.JPasswordField txtmatkhau;
    private javax.swing.JTextField txtnhanvien;
    private javax.swing.JPasswordField txtxacnhanmk;
    // End of variables declaration//GEN-END:variables
}
