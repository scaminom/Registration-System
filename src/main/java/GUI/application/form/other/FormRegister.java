/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.application.form.other;

import GUI.application.exceptionHandler.ExceptionHandler;
import GUI.application.exceptionHandler.HibernateExceptionHandler;
import com.scrum.registrationsystem.biometrics.FingerprintCallback;
import com.scrum.registrationsystem.biometrics.FingerprintManager;
import com.scrum.registrationsystem.biometrics.MyFingerprintCallback;
import com.scrum.registrationsystem.dao.RegisterDao;
import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.Register;
import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.util.HibernateUtil;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import org.hibernate.Session;
import org.hibernate.query.Query;

public class FormRegister extends javax.swing.JPanel implements FingerprintCallback {

    private final ExceptionHandler exceptionHandler;
    private final RegisterDao registerManage;
    private final UserDao userDao;
    Long Long = null;
    FingerprintManager fingerprintManager = null;
    MyFingerprintCallback callback = null;

    public FormRegister() {
        initComponents();
        inicializarHoraLabel();
        exceptionHandler = new HibernateExceptionHandler();
        registerManage = new RegisterDao();
        userDao = new UserDao();
        fingerprintManager = FingerprintManager.getInstance();
        callback = MyFingerprintCallback.getInstance(btnImg);
        fingerprintManager.addFingerprintCallback(callback);
        fingerprintManager.identify();
    }

    private void saveRegister(User user) {
        try {
            LocalDateTime entryTime = LocalDateTime.now();
            Register register = new Register(entryTime, null, user);
            user.addRegistration(register);
            registerManage.saveRegister(register);

            JOptionPane.showMessageDialog(null, "Registro guardado exitosamente.");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    private void updateRegister() {
        try {
            LocalDateTime exitTime = LocalDateTime.now();

            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            Query<Long> query = session.createQuery(
                    "SELECT id FROM Register WHERE user_id = :userId ORDER BY id DESC",
                    Long.class);
            query.setParameter("userId", userDao.getUser(Long.MIN_VALUE));
            query.setMaxResults(1);

            Long ultimoIdRegistro = query.uniqueResult();

            Register register = registerManage.getRegister(Long);

            register.setExitTime(exitTime);
            registerManage.updateRegister(register);
            JOptionPane.showMessageDialog(null, "Registro guardado exitosamente.");

        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    private void inicializarHoraLabel() {
        Timer timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarHoraActual();
            }
        });
        timer.start();

        mostrarHoraActual();
    }

    private void mostrarHoraActual() {
        Date ahora = new Date();
        SimpleDateFormat formatoHora = new SimpleDateFormat("HH:mm:ss");
        String horaFormateada = formatoHora.format(ahora);
        hora.setText("Hora actual: " + horaFormateada);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        hora = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        textArea = new javax.swing.JTextArea();
        btnImg = new javax.swing.JButton();

        setPreferredSize(new java.awt.Dimension(682, 540));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jLabel1.setText("REGISTRO DE ENTRADA Y SALIDA  ");

        hora.setBackground(new java.awt.Color(255, 255, 255));
        hora.setForeground(new java.awt.Color(255, 255, 255));
        hora.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jButton1.setText("REGISTRAR ENTRADA");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("REGISTRAR SALIDA");

        textArea.setColumns(20);
        textArea.setRows(5);
        jScrollPane2.setViewportView(textArea);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                .addComponent(btnImg, javax.swing.GroupLayout.PREFERRED_SIZE, 290,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGroup(layout
                                                        .createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING,
                                                                false)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(100, 100, 100)
                                                                .addComponent(hora,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE, 163,
                                                                        javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(
                                                                        javax.swing.LayoutStyle.ComponentPlacement.RELATED,
                                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                                        Short.MAX_VALUE)
                                                                .addComponent(jLabel1))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(360, 360, 360)
                                                                .addComponent(jButton1)
                                                                .addGap(18, 18, 18)
                                                                .addComponent(jButton2))))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(390, 390, 390)
                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                                        javax.swing.GroupLayout.DEFAULT_SIZE,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(351, Short.MAX_VALUE)));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(45, 45, 45)
                                                .addComponent(hora, javax.swing.GroupLayout.PREFERRED_SIZE, 46,
                                                        javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(58, 58, 58)
                                                .addComponent(jLabel1)))
                                .addGap(20, 20, 20)
                                .addComponent(btnImg, javax.swing.GroupLayout.DEFAULT_SIZE, 263, Short.MAX_VALUE)
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton1)
                                        .addComponent(jButton2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE,
                                        javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)));
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jButton1ActionPerformed

    }// GEN-LAST:event_jButton1ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnImg;
    private javax.swing.JLabel hora;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea textArea;
    // End of variables declaration//GEN-END:variables

    @Override
    public void onFingerprintCaptured(byte[] imgBuf) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onFingerprintSuccess(String message) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void onFingerprintError(String errorMessage) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public User onUserIdentify(User user) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from
                                                                       // nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
