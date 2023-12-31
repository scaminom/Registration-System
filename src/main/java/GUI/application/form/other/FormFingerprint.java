/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.application.form.other;

import GUI.application.exceptionHandler.ExceptionHandler;
import GUI.application.exceptionHandler.HibernateExceptionHandler;
import GUI.application.exceptionHandler.UserFingerprintValidator;

import com.scrum.registrationsystem.biometrics.FingerprintManager;
import com.scrum.registrationsystem.biometrics.MyFingerprintCallback;
import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author patri
 */
public class FormFingerprint extends javax.swing.JPanel {

        UserDao ud = null;

        User userSelected = null;

        private final ExceptionHandler exceptionHandler;

        FingerprintManager fingerprintManager = null;
        MyFingerprintCallback callback = null;

        public FormFingerprint() {
                initComponents();
                ud = new UserDao();
                exceptionHandler = new HibernateExceptionHandler();
                chargeDataCombobox();
                userSelected = (User) jComboBox1.getSelectedItem();
                fingerprintManager = FingerprintManager.getInstance();
                callback = MyFingerprintCallback.getInstance(jbtnImg);
                fingerprintManager.addFingerprintCallback(callback);
                fingerprintManager.enroll();
        }

        private void chargeDataCombobox() {
                DefaultComboBoxModel<User> model = new DefaultComboBoxModel<>();
                try {
                        var users = ud.findAll();
                        for (User user : users) {
                                model.addElement(user);
                        }
                } catch (Exception e) {
                }
                jComboBox1.setModel(model);

        }

        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // <editor-fold defaultstate="collapsed" desc="Generated
        // Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jbtnImg = new javax.swing.JButton();
                jLabel1 = new javax.swing.JLabel();
                jPanel1 = new javax.swing.JPanel();
                jLabel2 = new javax.swing.JLabel();
                jComboBox1 = new javax.swing.JComboBox<>();
                jbtnIniciar = new javax.swing.JButton();
                jScrollPane1 = new javax.swing.JScrollPane();
                jtxtVer = new javax.swing.JTextArea();
                jbtnCerrar = new javax.swing.JButton();
                jButton1 = new javax.swing.JButton();

                jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
                jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel1.setText("Agregar Huella");
                jLabel1.setToolTipText("");
                jLabel1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);

                jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());

                jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
                jLabel2.setText("Seleccione el empleado:");

                jComboBox1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jComboBox1ActionPerformed(evt);
                        }
                });

                jbtnIniciar.setText("Iniciar");
                jbtnIniciar.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jbtnIniciarActionPerformed(evt);
                        }
                });

                jtxtVer.setColumns(20);
                jtxtVer.setRows(5);
                jScrollPane1.setViewportView(jtxtVer);

                jbtnCerrar.setText("Cerrar");
                jbtnCerrar.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jbtnCerrarActionPerformed(evt);
                        }
                });

                jButton1.setText("Guardar");
                jButton1.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButton1ActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
                jPanel1.setLayout(jPanel1Layout);
                jPanel1Layout.setHorizontalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jLabel2,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap())
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(67, 67, 67)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.TRAILING)
                                                                                .addGroup(jPanel1Layout
                                                                                                .createParallelGroup(
                                                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                                .addGroup(jPanel1Layout
                                                                                                                .createSequentialGroup()
                                                                                                                .addComponent(jbtnIniciar,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                100,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                                                .addPreferredGap(
                                                                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                                                                .addComponent(jbtnCerrar,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                                                102,
                                                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                                                                jPanel1Layout.createSequentialGroup()
                                                                                                                                .addComponent(jButton1)
                                                                                                                                .addGap(70, 70, 70)))
                                                                                .addComponent(jComboBox1,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                                214,
                                                                                                javax.swing.GroupLayout.PREFERRED_SIZE))
                                                                .addGap(0, 0, Short.MAX_VALUE))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout
                                                                .createSequentialGroup()
                                                                .addContainerGap(31, Short.MAX_VALUE)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                294,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(27, 27, 27)));
                jPanel1Layout.setVerticalGroup(
                                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(jPanel1Layout.createSequentialGroup()
                                                                .addGap(12, 12, 12)
                                                                .addComponent(jLabel2,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                24,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jComboBox1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(77, 77, 77)
                                                                .addGroup(jPanel1Layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.BASELINE)
                                                                                .addComponent(jbtnIniciar)
                                                                                .addComponent(jbtnCerrar))
                                                                .addGap(33, 33, 33)
                                                                .addComponent(jButton1)
                                                                .addGap(31, 31, 31)
                                                                .addComponent(jScrollPane1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                338,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addContainerGap(29, Short.MAX_VALUE)));

                jComboBox1.getAccessibleContext().setAccessibleParent(jComboBox1);

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(layout.createSequentialGroup()
                                                                .addGap(17, 17, 17)
                                                                .addComponent(jbtnImg,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                411, Short.MAX_VALUE)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jPanel1,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addGap(22, 22, 22))
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jLabel1,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                Short.MAX_VALUE)
                                                                .addContainerGap()));
                layout.setVerticalGroup(
                                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout
                                                                .createSequentialGroup()
                                                                .addContainerGap()
                                                                .addComponent(jLabel1)
                                                                .addPreferredGap(
                                                                                javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(
                                                                                javax.swing.GroupLayout.Alignment.LEADING)
                                                                                .addComponent(jPanel1,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE)
                                                                                .addComponent(jbtnImg,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                                                Short.MAX_VALUE))
                                                                .addGap(18, 18, 18)));
        }// </editor-fold>//GEN-END:initComponents

        private void jbtnCerrarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jbtnCerrarActionPerformed

        }// GEN-LAST:event_jbtnCerrarActionPerformed

        private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jComboBox1ActionPerformed
                var user = (User) jComboBox1.getSelectedItem();
                userSelected = user;
        }// GEN-LAST:event_jComboBox1ActionPerformed

        private void jbtnIniciarActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jbtnIniciarActionPerformed

        }

        private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
                try {
                        var validate = new UserFingerprintValidator(fingerprintManager.getTemplate(), userSelected);
                        var errors = validate.validate();
                        if (errors.isEmpty()) {
                                userSelected.setFingerprintPattern(fingerprintManager.getTemplate());
                                ud.updateUserFingerprint(userSelected, fingerprintManager.getTemplate());
                                jtxtVer.setText("La huella del usuario " + userSelected.getFirstName() + " "
                                                + userSelected.getLastName() + "\nse guardo correctamente.");
                                fingerprintManager.saveUserFootprints();

                        } else {
                                StringBuilder errorBuilder = new StringBuilder();
                                errors.forEach((k, v) -> {
                                        v.forEach((e) -> {
                                                errorBuilder.append(e).append("\n");
                                        });
                                });
                                String error = errorBuilder.toString();

                                jtxtVer.setText(error);
                        }
                } catch (Exception e) {
                        exceptionHandler.handleException(e);
                }
        }

        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButton1;
        private javax.swing.JComboBox<User> jComboBox1;
        private javax.swing.JLabel jLabel1;
        private javax.swing.JLabel jLabel2;
        private javax.swing.JPanel jPanel1;
        private javax.swing.JScrollPane jScrollPane1;
        private javax.swing.JButton jbtnCerrar;
        private javax.swing.JButton jbtnImg;
        private javax.swing.JButton jbtnIniciar;
        private javax.swing.JTextArea jtxtVer;
        // End of variables declaration//GEN-END:variables
}
