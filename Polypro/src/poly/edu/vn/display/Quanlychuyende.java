/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.edu.vn.display;

import java.awt.Image;
import java.util.ArrayList;

import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import static javax.swing.WindowConstants.DISPOSE_ON_CLOSE;
import javax.swing.table.DefaultTableModel;
import poly.edu.vn.display.other.ChuyenDe;
import poly.edu.vn.display.other.ChuyendeDao;
import poly.edu.vn.display.other.DialogHelper;
import poly.edu.vn.display.other.NhanVien;
import poly.edu.vn.display.other.ShareHelper;



public class Quanlychuyende extends javax.swing.JFrame {
 
    int index = 0;
    ChuyendeDao dao = new ChuyendeDao();
     String imageName = null;
     
    /**
     * Creates new form Quanlychuyende
     */
    public Quanlychuyende() {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
    }
public boolean check() {
    
        if (txtchuyende.getText().equals("") || txttenchuyende.getText().equals("") || txtgio.getText().equals("")
                || txthocphi.getText().equals("") || txtmota.getText().equals("")  ) {
            JOptionPane.showMessageDialog(rootPane, "Hãy nhập đủ dữ liệu sau đó ấn Save","Error",1);
            return false;
        }else if (!(txtchuyende.getText()).matches("CD[0-9]{1,9}")) {
            JOptionPane.showMessageDialog(rootPane, "Sai định dạng mã \n VD : NV01","Error",1);
            txtchuyende.requestFocus();
            return false;
        }else if (!(txtgio.getText()).matches("[0-9]{1,99}")) {
            JOptionPane.showMessageDialog(rootPane, "Thời lượng phải nhập số dương","Error",1);
            txtgio.requestFocus();
            return false;
        }else if (!(txthocphi.getText()).matches("[0-9]{1,99}")) {
            JOptionPane.showMessageDialog(rootPane, "Học phí phải nhập số dương","Error",1);
            txthocphi.requestFocus();
            return false;
        }
        
        
       List<ChuyenDe> list = dao.select(); 
                for(int i=0; i< list.size();i++){
            if(txtchuyende.getText().equalsIgnoreCase(list.get(i).getMaCD())){
                JOptionPane.showMessageDialog(this, "Trùng Mã Chuyên Đề","Error",1);
                return false;
            }
        }
        
        
        
       
        return true;
    }
  public void ResizeImage(String imageName) {
        ImageIcon icon = new ImageIcon("src\\image\\" + imageName);
        Image image = icon.getImage();
        ImageIcon icon1 = new ImageIcon(image.getScaledInstance(lblImage.getWidth(), lblImage.getHeight(), image.SCALE_SMOOTH));
        lblImage.setIcon(icon1);
        
    }
    void load(){
        DefaultTableModel model =(DefaultTableModel)tblQLNV.getModel();
        model.setRowCount(0);
        
        try { 
            List<ChuyenDe> list = dao.select(); 
            for (ChuyenDe cd : list) { 
                Object[] row = { 
                    cd.getMaCD(), 
                    cd.getTenCD(), 
                    cd.getHocPhi(), 
                    cd.getThoiLuong(), 
                    cd.getHinh(), 
                    cd.getMoTa()
                }; 
                model.addRow(row); 
            } 
        }  
        catch (Exception e) { 
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!"); 
        } 

    } 
    void insert(){ 
        ChuyenDe model = getModel(); 
        if (model.getHinh() == null) {
                DialogHelper.alert(this, "Hình Không Được Để Trống");
                return ;
            }
        try { 
            dao.insert(model); 
            this.load(); 
            this.clear(); 
            DialogHelper.alert(this, "Thêm mới thành công!"); 
        }  
        catch (Exception e) { 
            DialogHelper.alert(this, "Thêm mới thất bại!"); 
        } 
    }
    void update(){ 
        ChuyenDe model = getModel(); 
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
        if(DialogHelper.confirm(this, "Bạn có muốn xóa hay không?")){ 
            String macd = txtchuyende.getText(); 
            try { 
                dao.delete(macd); 
                this.load(); 
                this.clear(); 
                DialogHelper.alert(this, "Xóa thành công!"); 
            }  
            catch (Exception e) { 
                DialogHelper.alert(this, "Xóa thất bại!"); 
            } 
        } 
    } 
     void clear(){ 
        this.setModel(new ChuyenDe()); 
        this.setStatus(true); 
    } 
// 
    void edit() { 
        try { 
            String macd = (String) tblQLNV.getValueAt(this.index, 0); 
            ChuyenDe model = dao.findById(macd); 
            if(model != null){ 
                this.setModel(model); 
                this.setStatus(false); 
            } 
        }  
        catch (Exception e) { 
            DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!"); 
        } 
    }
      void setModel(ChuyenDe model){ 
        txtchuyende.setText(model.getMaCD()); 
        txttenchuyende.setText(model.getTenCD()); 
        txthocphi.setText(String.valueOf(model.getHocPhi())); 
        txtgio.setText(String.valueOf(model.getThoiLuong()));     
        lblImage.setToolTipText(model.getHinh());
        if(model.getHinh() != null){ 
        lblImage.setIcon(ShareHelper.readLogo(model.getHinh())); 
        } 
        txtmota.setText(model.getMoTa()); 
    } 
   ChuyenDe getModel(){ 
        ChuyenDe model = new ChuyenDe(); 
        model.setMaCD(txtchuyende.getText()); 
        model.setTenCD(txttenchuyende.getText()); 
        model.setHocPhi(Double.valueOf(txthocphi.getText())); 
        model.setThoiLuong(Integer.valueOf(txtgio.getText())); 
        model.setHinh(lblImage.getToolTipText()); 
        model.setMoTa(txtmota.getText()); 
        return model; 
    } 
// 
    void setStatus(boolean insertable){ 
        txtchuyende.setEditable(insertable); 
        btnInsert.setEnabled(insertable); 
        btnUpdate.setEnabled(!insertable); 
        btnDelete.setEnabled(!insertable); 
        boolean first = this.index > 0; 
        boolean last = this.index < tblQLNV.getRowCount() - 1; 
        btnFirst.setEnabled(!insertable && first); 
        btnPrev.setEnabled(!insertable && first); 
        btnNext.setEnabled(!insertable && last); 
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

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        lblImage = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        txtchuyende = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txttenchuyende = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtgio = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txthocphi = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtmota = new javax.swing.JTextArea();
        btnInsert = new javax.swing.JButton();
        btnUpdate = new javax.swing.JButton();
        btnDelete = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblQLNV = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HỆ THỐNG QUẢN LÝ ĐÀO TẠO");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 153));
        jLabel1.setText("QUẢN LÝ CHUYÊN ĐỀ");

        jLabel2.setText("Hình logo");

        jPanel4.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 204), 2));

        lblImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblImageMouseClicked(evt);
            }
        });
        lblImage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                lblImageKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, 172, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblImage, javax.swing.GroupLayout.DEFAULT_SIZE, 218, Short.MAX_VALUE)
        );

        jLabel4.setText("Mã chuyên đề");

        jLabel5.setText("Tên chuyên đề");

        jLabel6.setText("Thời lượng (giờ)");

        txtgio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtgioActionPerformed(evt);
            }
        });

        jLabel7.setText("Học phí");

        jLabel8.setText("Mô tả chuyên đề");

        txtmota.setColumns(20);
        txtmota.setRows(5);
        jScrollPane2.setViewportView(txtmota);

        btnInsert.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Save.png"))); // NOI18N
        btnInsert.setText("Thêm");
        btnInsert.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInsertActionPerformed(evt);
            }
        });

        btnUpdate.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Sync.png"))); // NOI18N
        btnUpdate.setText("Sửa");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Delete.png"))); // NOI18N
        btnDelete.setText("Xóa");
        btnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteActionPerformed(evt);
            }
        });

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Add.png"))); // NOI18N
        jButton4.setText("Mới");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
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

        btnNext.setText(">>");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnLast.setText(">|");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(txtchuyende)
                                    .addComponent(txttenchuyende)
                                    .addComponent(txtgio)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGap(10, 10, 10)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addComponent(jLabel6)
                                            .addComponent(jLabel5))
                                        .addGap(0, 0, Short.MAX_VALUE))
                                    .addComponent(txthocphi)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(11, 11, 11)
                                .addComponent(jLabel2)
                                .addGap(147, 147, 147)
                                .addComponent(jLabel4)
                                .addGap(0, 0, Short.MAX_VALUE))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(20, 20, 20)
                        .addComponent(jLabel8)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane2))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnInsert)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnUpdate)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnDelete)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnFirst)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnNext)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnLast)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtchuyende, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txttenchuyende, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txtgio, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel7)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txthocphi)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 86, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnInsert)
                    .addComponent(btnUpdate)
                    .addComponent(btnDelete)
                    .addComponent(jButton4)
                    .addComponent(btnFirst)
                    .addComponent(btnPrev)
                    .addComponent(btnNext)
                    .addComponent(btnLast))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        tabs.addTab("CẬP NHẬP", jPanel2);

        tblQLNV.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ CD", "TÊN CD", "HỌC PHÍ", "THỜI LƯỢNG", "HÌNH"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblQLNV.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQLNVMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tblQLNV);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 602, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 22, Short.MAX_VALUE))
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

        tabs.addTab("SANH SÁCH", jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 182, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(tabs)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
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

    private void txtgioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtgioActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtgioActionPerformed

    private void lblImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblImageMouseClicked
        // TODO add your handling code here:
       try{
//        JFileChooser file = new JFileChooser();
//        int kq = file.showOpenDialog(file);
//        imageName = file.getSelectedFile().getName();
//        ResizeImage(imageName);
        JFileChooser file = new JFileChooser("src\\image\\");
        int kq = file.showOpenDialog(file);
        if (kq == JFileChooser.APPROVE_OPTION) {
            imageName = file.getSelectedFile().getName();
            ResizeImage(imageName);
            lblImage.setToolTipText(imageName);
        } else {
            JOptionPane.showMessageDialog(rootPane, "Bạn chưa chọn ảnh...");
        }
        
        }catch(Exception a){
            
        }
    }//GEN-LAST:event_lblImageMouseClicked

    private void btnInsertActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInsertActionPerformed
         // TODO add your handling code here:
         try{
             
         
         if(check()){
             this.insert();
         }
         }catch(Exception e){
             JOptionPane.showMessageDialog(rootPane, "Lỗi, Vui lòng xem lại");
         }
             
    }//GEN-LAST:event_btnInsertActionPerformed

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

    private void lblImageKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_lblImageKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_lblImageKeyPressed

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        // TODO add your handling code here:
        this.update();
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
       this.clear();
       lblImage.setIcon(null);
    }//GEN-LAST:event_jButton4ActionPerformed

    private void btnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteActionPerformed
         // TODO add your handling code here:
         this.delete();
    }//GEN-LAST:event_btnDeleteActionPerformed

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
         // TODO add your handling code here:
         this.load();
        this.setStatus(true);
    }//GEN-LAST:event_formWindowOpened

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
         // TODO add your handling code here:
         this.index =0;
        this.edit();
    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
         // TODO add your handling code here:
         this.index--;
        this.edit();
    }//GEN-LAST:event_btnPrevActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
         // TODO add your handling code here:
          this.index++;
        this.edit();
    }//GEN-LAST:event_btnNextActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
         // TODO add your handling code here:
         this.index =tblQLNV.getRowCount() -1;
        this.edit();
    }//GEN-LAST:event_btnLastActionPerformed

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
            java.util.logging.Logger.getLogger(Quanlychuyende.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Quanlychuyende.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Quanlychuyende.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Quanlychuyende.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Quanlychuyende().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnDelete;
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnInsert;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblImage;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblQLNV;
    private javax.swing.JTextField txtchuyende;
    private javax.swing.JTextField txtgio;
    private javax.swing.JTextField txthocphi;
    private javax.swing.JTextArea txtmota;
    private javax.swing.JTextField txttenchuyende;
    // End of variables declaration//GEN-END:variables
}
