/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.application.form.other;

import GUI.application.Application;
import GUI.application.exceptionHandler.ExceptionHandler;
import GUI.application.exceptionHandler.HibernateExceptionHandler;

import com.scrum.registrationsystem.dao.RegisterDao;
import com.scrum.registrationsystem.entities.Fines;
import com.scrum.registrationsystem.entities.Register;
import com.scrum.registrationsystem.entities.User;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author patri
 */
public class FormMyRegisters extends javax.swing.JPanel {

    private User user = Application.getUserLogged();
    private static final String[] COLUMN_NAMES = {"id", "Fecha de entrada", "Fecha de salida"};
    private final RegisterDao registerDao;
    private final ExceptionHandler exceptionHandler;
    
    public FormMyRegisters() {
        initComponents();
        this.registerDao = new RegisterDao();
        exceptionHandler = new HibernateExceptionHandler();
        chargeTable();
    }

     private void chargeTable() {
        DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0);
        try {
            var registers = registerDao.findAllRegistersByUser(user.getId());
            for (Register register : registers) {
                model.addRow(new Object[]{register.getId(), register.getEntryTime(), register.getExitTime()});
            }
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        jtblMyRegisters.setModel(model);
    }
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jtblMyRegisters = new javax.swing.JTable();
        jLabel1 = new javax.swing.JLabel();

        jtblMyRegisters.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3"
            }
        ));
        jScrollPane1.setViewportView(jtblMyRegisters);

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Mis registros");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
                .addGap(49, 49, 49))
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(29, 29, 29)
                .addComponent(jScrollPane1)
                .addGap(36, 36, 36))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jtblMyRegisters;
    // End of variables declaration//GEN-END:variables
}
