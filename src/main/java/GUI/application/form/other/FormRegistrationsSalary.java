/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package GUI.application.form.other;

import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.util.HibernateUtil;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

/**
 *
 * @author joses
 */
public class FormRegistrationsSalary extends javax.swing.JPanel {

	/**
	 * Creates new form FormRegistrationsSalary
	 */
	public FormRegistrationsSalary() {
		initComponents();
		chargeUserData();
	}

	private void chargeUserData() {
		var userDao = new UserDao();
		var users = userDao.findAll();
		for (var user : users) {
			jcmbUser.addItem(user.getUsername());
		}
	}

	public static void generateUserReport() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.doWork(connection -> {
				try {
					var username = (String) jcmbUser.getSelectedItem();
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("USERNAME", username);

					JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/Salary.jrxml");
					JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, connection);

					JasperViewer.viewReport(jasperPrint, false);
				} catch (JRException ex) {
					JOptionPane.showMessageDialog(null,
							"There was an error generating the report: " + ex.getMessage(),
							"Report Error",
							JOptionPane.ERROR_MESSAGE);
					ex.printStackTrace();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(null,
							"Please enter a valid month number.",
							"Input Error",
							JOptionPane.ERROR_MESSAGE);
				}
			});
		} finally {
			if (session != null && session.isOpen()) {
				session.close();
			}
		}
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jlblSalaryUsers = new javax.swing.JLabel();
        jlblUsername = new javax.swing.JLabel();
        jcmbUser = new javax.swing.JComboBox<>();
        jbtnGenerate = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jlblSalaryUsers.setText("Reporte del salario de un usuario");

        jlblUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblUsername.setText("Empleado");

        jbtnGenerate.setText("Generar Reporte");
        jbtnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGenerateActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(39, 39, 39)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jlblSalaryUsers)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(57, 57, 57)
                        .addComponent(jcmbUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(43, 43, 43)
                        .addComponent(jbtnGenerate)))
                .addContainerGap(386, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jlblSalaryUsers)
                .addGap(18, 18, 18)
                .addComponent(jlblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jcmbUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jbtnGenerate))
                .addContainerGap(222, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 184, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnGenerateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnGenerateActionPerformed
		generateUserReport();
    }//GEN-LAST:event_jbtnGenerateActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbtnGenerate;
    private static javax.swing.JComboBox<String> jcmbUser;
    private javax.swing.JLabel jlblSalaryUsers;
    private javax.swing.JLabel jlblUsername;
    // End of variables declaration//GEN-END:variables
}
