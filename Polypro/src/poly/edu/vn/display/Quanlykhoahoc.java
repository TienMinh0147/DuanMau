/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package poly.edu.vn.display;

import java.awt.HeadlessException;
import java.util.Date;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import poly.edu.vn.display.other.ChuyenDe;
import poly.edu.vn.display.other.ChuyendeDao;
import poly.edu.vn.display.other.DateHelper;
import poly.edu.vn.display.other.DialogHelper;
import poly.edu.vn.display.other.KhoaHoc;
import poly.edu.vn.display.other.KhoahocDao;
import poly.edu.vn.display.other.ShareHelper;


public class Quanlykhoahoc extends javax.swing.JFrame {
 int index = 0;
 KhoahocDao dao = new KhoahocDao();
 ChuyendeDao cddao = new ChuyendeDao();
    /**
     * Creates new form Quanlykhoahoc
     */
    public Quanlykhoahoc() {
        initComponents();
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
    }
    
    public boolean check() {
    if (txtkhaigiang.getText().equals("") || txthocphi.getText().equals("") || txtthoiluong.getText().equals("")
                || txtngaytao.getText().equals("") || txtnguoitao.getText().equals("") || txtghichu.getText().equals("") ) {
            JOptionPane.showMessageDialog(rootPane, "Hãy nhập đủ dữ liệu sau đó ấn Save","Error",1);
            return false;
        }
    else if (!(txthocphi.getText()).matches("[0-9]{1,9}")) {
            JOptionPane.showMessageDialog(rootPane, "Học phí phải nhập số","Error",1);
            txthocphi.requestFocus();
            return false;
        }
       else if (!(txtthoiluong.getText()).matches("[0-9]{1,8}")) {
            JOptionPane.showMessageDialog(rootPane, "Thời lượng phải nhập số","Error",1);
            txtthoiluong.requestFocus();
            return false;
        }
        
        
        
       
        return true;
    }



     void load() {
        DefaultTableModel model = (DefaultTableModel) tblQLKH.getModel();
        model.setRowCount(0);
        try {
        List<KhoaHoc> list = dao.select();
        for (KhoaHoc kh : list) {
        Object[] row = {
        kh.getMaKH(),
         kh.getMaCD(),
           kh.getThoiLuong(),
        kh.getHocPhi(),
        DateHelper.toString(kh.getNgayKG()),
          kh.getMaNV(),
             DateHelper.toString(kh.getNgayTao())
        };
             model.addRow(row);
             }
          }
             catch (Exception e) {
          DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
           }

         }
     
     void insert(){
 KhoaHoc model = getModel();
 model.setNgayTao(new Date());
 try {
 dao.insert(model);
 this.load();
 this.clear();
 DialogHelper.alert(this, "Thêm mới thành công!");
 }
 catch (HeadlessException e) {
 DialogHelper.alert(this, "Thêm mới thất bại!");
 }
 }
 void update(){
 KhoaHoc model = getModel();
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
 if(DialogHelper.confirm(this, "Bạn thực sự muốn xóa khóa học này?")){
 Integer makh = Integer.valueOf(cbochuyende.getToolTipText());
 try {
 dao.delete(makh);
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
 KhoaHoc model = new KhoaHoc();
 ChuyenDe chuyenDe = (ChuyenDe) cbochuyende.getSelectedItem();
 model.setMaCD(chuyenDe.getMaCD());
 model.setMaNV(ShareHelper.USER.getMaNV());
 model.setNgayKG(DateHelper.add(30));
 model.setNgayTao(DateHelper.now());
 this.setModel(model);
 }
 
 void edit() {
 try {
     Integer makh = (Integer) tblQLKH.getValueAt(this.index, 0);
 KhoaHoc model = dao.findById(makh);
 if(model != null){
 this.setModel(model);
 this.setStatus(false);
 }
 }
 catch (Exception e) {
 DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
 }
 }
 void setModel(KhoaHoc model){
 cbochuyende.setToolTipText(String.valueOf(model.getMaKH()));
 cbochuyende.setSelectedItem(cddao.findById(model.getMaCD()));
 txtkhaigiang.setText(DateHelper.toString(model.getNgayKG()));
 txthocphi.setText(String.valueOf(model.getHocPhi()));
 txtthoiluong.setText(String.valueOf(model.getThoiLuong()));
 txtnguoitao.setText(model.getMaNV());
 txtngaytao.setText(DateHelper.toString(model.getNgayTao()));
 txtghichu.setText(model.getGhiChu());
 }

 KhoaHoc getModel(){
 KhoaHoc model = new KhoaHoc();
 ChuyenDe chuyenDe = (ChuyenDe) cbochuyende.getSelectedItem();
 model.setMaCD(chuyenDe.getMaCD());
 model.setNgayKG(DateHelper.toDate(txtkhaigiang.getText()));
 model.setHocPhi(Double.valueOf(txthocphi.getText()));
 model.setThoiLuong(Integer.valueOf(txtthoiluong.getText()));
 model.setGhiChu(txtghichu.getText());
 model.setMaNV(ShareHelper.USER.getMaNV());
 model.setNgayTao(DateHelper.toDate(txtngaytao.getText()));
 model.setMaKH(Integer.valueOf(cbochuyende.getToolTipText()));

 return model;
 }
 void setStatus(boolean insertable){
 btnthem.setEnabled(insertable);
 btnsua.setEnabled(!insertable);
 btnxoa.setEnabled(!insertable);

 boolean first = this.index > 0;
 boolean last = this.index < tblQLKH.getRowCount() - 1;
 btnFirst.setEnabled(!insertable && first);
 btnPrev.setEnabled(!insertable && first);
 btnLast.setEnabled(!insertable && last);
 btnNext.setEnabled(!insertable && last);
 btnStudents.setVisible(!insertable);
 }
 void selectComboBox(){
 ChuyenDe chuyenDe = (ChuyenDe) cbochuyende.getSelectedItem();
 txtthoiluong.setText(String.valueOf(chuyenDe.getThoiLuong()));
 txthocphi.setText(String.valueOf(chuyenDe.getHocPhi()));
 }
 void openHocVien() {
 Integer id = Integer.valueOf(cbochuyende.getToolTipText());
 new Quanlyhocvien(id).setVisible(true);
 }

 void fillComboBox(){
 DefaultComboBoxModel model = (DefaultComboBoxModel) cbochuyende.getModel();
 model.removeAllElements();
 try {
 List<ChuyenDe> list = cddao.select();
 for(ChuyenDe cd : list){
 model.addElement(cd);
 }
 }
 catch (Exception e) {
 DialogHelper.alert(this, "Lỗi truy vấn dữ liệu!");
 }
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
        jLabel2 = new javax.swing.JLabel();
        tabs = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        cbochuyende = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        txtkhaigiang = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txthocphi = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        txtthoiluong = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtnguoitao = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        txtngaytao = new javax.swing.JTextField();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtghichu = new javax.swing.JTextArea();
        btnthem = new javax.swing.JButton();
        btnsua = new javax.swing.JButton();
        btnxoa = new javax.swing.JButton();
        btnmoi = new javax.swing.JButton();
        btnFirst = new javax.swing.JButton();
        btnPrev = new javax.swing.JButton();
        btnLast = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        btnStudents = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblQLKH = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("HỆ THỐNG QUẢN LÝ ĐÀO TẠO");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 1, 14)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(0, 0, 153));
        jLabel2.setText("QUẢN LÝ KHÓA HỌC");

        jLabel1.setText("Chuyên đề");

        cbochuyende.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbochuyendeActionPerformed(evt);
            }
        });

        jLabel3.setText("Ngày khai giảng");

        txtkhaigiang.setEditable(false);

        jLabel4.setText("Học phí");

        txthocphi.setEditable(false);

        jLabel5.setText("Thời lượng (giờ)");

        txtthoiluong.setEditable(false);

        jLabel6.setText("Người tạo");

        txtnguoitao.setEditable(false);
        txtnguoitao.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtnguoitaoActionPerformed(evt);
            }
        });

        jLabel7.setText("Ngày tạo");

        txtngaytao.setEditable(false);

        jLabel8.setText("Ghi chú");

        txtghichu.setColumns(20);
        txtghichu.setRows(5);
        jScrollPane1.setViewportView(txtghichu);

        btnthem.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Save.png"))); // NOI18N
        btnthem.setText("Thêm");
        btnthem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnthemActionPerformed(evt);
            }
        });

        btnsua.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Sync.png"))); // NOI18N
        btnsua.setText("Sửa");
        btnsua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnsuaActionPerformed(evt);
            }
        });

        btnxoa.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Delete.png"))); // NOI18N
        btnxoa.setText("Xóa");
        btnxoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnxoaActionPerformed(evt);
            }
        });

        btnmoi.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Add.png"))); // NOI18N
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

        btnPrev.setText("<<");
        btnPrev.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPrevActionPerformed(evt);
            }
        });

        btnLast.setText(">>");
        btnLast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLastActionPerformed(evt);
            }
        });

        btnNext.setText(">|");
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        btnStudents.setIcon(new javax.swing.ImageIcon(getClass().getResource("/poly/edu/vn/icon/Female.png"))); // NOI18N
        btnStudents.setText("Học Viên");
        btnStudents.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStudentsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(jLabel1)
                        .addGap(240, 240, 240)
                        .addComponent(jLabel3)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(cbochuyende, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel4)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel5)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtkhaigiang)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txthocphi, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel6)))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel7)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addComponent(txtthoiluong)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jLabel8)
                                .addGap(0, 0, Short.MAX_VALUE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(txtnguoitao, javax.swing.GroupLayout.PREFERRED_SIZE, 276, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addComponent(txtngaytao))))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnthem)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnsua)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnxoa)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnmoi)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnStudents)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                        .addComponent(btnFirst)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnPrev)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnLast)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNext)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(cbochuyende, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(txtkhaigiang))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txthocphi, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtthoiluong, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtnguoitao, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE)
                    .addComponent(txtngaytao))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 81, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnthem)
                    .addComponent(btnsua)
                    .addComponent(btnxoa)
                    .addComponent(btnmoi)
                    .addComponent(btnStudents)
                    .addComponent(btnFirst)
                    .addComponent(btnPrev)
                    .addComponent(btnLast)
                    .addComponent(btnNext))
                .addGap(0, 0, Short.MAX_VALUE))
        );

        tabs.addTab("CẬP NHẬP", jPanel2);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 691, Short.MAX_VALUE)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        tblQLKH.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "MÃ KH", "CHUYÊN ĐỀ", "THỜI LƯỢNG", "HỌC PHÍ", "KHAI GIẢNG", "TẠO BỞI", "NGÀY TẠO"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tblQLKH.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tblQLKHMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(tblQLKH);

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane2)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 388, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        tabs.addTab("DANH SÁNH", jPanel3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(tabs, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 29, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(tabs, javax.swing.GroupLayout.PREFERRED_SIZE, 414, Short.MAX_VALUE))
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

    private void btnmoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnmoiActionPerformed
        // TODO add your handling code here:
        this.clear();

    }//GEN-LAST:event_btnmoiActionPerformed

    private void btnPrevActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPrevActionPerformed
        // TODO add your handling code here:
        this.index--;
this.edit();

    }//GEN-LAST:event_btnPrevActionPerformed

    private void txtnguoitaoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtnguoitaoActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtnguoitaoActionPerformed

    private void btnthemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnthemActionPerformed
        // TODO add your handling code here:
        try{
        if(check()){
            this.insert();
        }
        }catch(Exception e){
            JOptionPane.showMessageDialog(this, "Lỗi, Vui lòng xem lại");
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

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        
        this.fillComboBox();
        this.load();
        this.clear();
        this.setStatus(true);
    }//GEN-LAST:event_formWindowOpened

    private void tblQLKHMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tblQLKHMouseClicked
        // TODO add your handling code here:
        if(evt.getClickCount() == 2){
 this.index = tblQLKH.rowAtPoint(evt.getPoint());
 if (this.index >= 0) {
 this.edit();
 tabs.setSelectedIndex(0);
 }
}

    }//GEN-LAST:event_tblQLKHMouseClicked

    private void btnFirstActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFirstActionPerformed
        // TODO add your handling code here:
        this.index = 0;
this.edit();

    }//GEN-LAST:event_btnFirstActionPerformed

    private void btnLastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLastActionPerformed
this.index++;
this.edit();

        // TODO add your handling code here:
    }//GEN-LAST:event_btnLastActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        this.index = tblQLKH.getRowCount() - 1;
this.edit();
    }//GEN-LAST:event_btnNextActionPerformed

    private void cbochuyendeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cbochuyendeActionPerformed
         // TODO add your handling code here:
      selectComboBox();
    }//GEN-LAST:event_cbochuyendeActionPerformed

    private void btnStudentsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStudentsActionPerformed
        // TODO add your handling code here:
        this.openHocVien();
    }//GEN-LAST:event_btnStudentsActionPerformed

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
            java.util.logging.Logger.getLogger(Quanlykhoahoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Quanlykhoahoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Quanlykhoahoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Quanlykhoahoc.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Quanlykhoahoc().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnFirst;
    private javax.swing.JButton btnLast;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnPrev;
    private javax.swing.JButton btnStudents;
    private javax.swing.JButton btnmoi;
    private javax.swing.JButton btnsua;
    private javax.swing.JButton btnthem;
    private javax.swing.JButton btnxoa;
    private javax.swing.JComboBox<String> cbochuyende;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTabbedPane tabs;
    private javax.swing.JTable tblQLKH;
    private javax.swing.JTextArea txtghichu;
    private javax.swing.JTextField txthocphi;
    private javax.swing.JTextField txtkhaigiang;
    private javax.swing.JTextField txtngaytao;
    private javax.swing.JTextField txtnguoitao;
    private javax.swing.JTextField txtthoiluong;
    // End of variables declaration//GEN-END:variables
}
