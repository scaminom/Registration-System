package GUI.application.form.other;

import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.util.HibernateUtil;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.swing.JOptionPane;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.Session;

public class FormRegistrationsReports extends javax.swing.JPanel {

	private static LinkedHashMap<String, Integer> MONTHS;

	public FormRegistrationsReports() {
		MONTHS = new LinkedHashMap();
		initComponents();
		chargeUserData();
		chargeMonthData();
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jbtnGenerate = new javax.swing.JButton();
        jcmbUser = new javax.swing.JComboBox<>();
        jcmbMonth = new javax.swing.JComboBox<>();
        jlblMonth = new javax.swing.JLabel();
        jlblUsername = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jbtnGenerate.setText("Generar Reporte");
        jbtnGenerate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnGenerateActionPerformed(evt);
            }
        });

        jlblMonth.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblMonth.setText("Mes");

        jlblUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jlblUsername.setText("Empleado");

        jLabel1.setText("Reporte de registros por usuario");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(34, 34, 34)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 382, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jlblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(28, 28, 28)
                                .addComponent(jcmbUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jbtnGenerate)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jcmbMonth, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jlblMonth, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(258, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jlblUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jlblMonth, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jcmbUser, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jcmbMonth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addComponent(jbtnGenerate)
                .addContainerGap(65, Short.MAX_VALUE))
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
                .addContainerGap(264, Short.MAX_VALUE))
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

	public static void generateUserReport() {
		Session session = HibernateUtil.getSessionFactory().openSession();

		try {
			session.doWork(connection -> {
				try {
					var username = (String) jcmbUser.getSelectedItem();
					String selectedMonth = (String) jcmbMonth.getSelectedItem();
					int month = MONTHS.get(selectedMonth);
					Map<String, Object> parameters = new HashMap<>();
					parameters.put("USERNAME", username);
					parameters.put("MONTH", month);

					JasperReport jasperReport = JasperCompileManager.compileReport("src/main/resources/Registrations.jrxml");
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

	private void chargeUserData() {
		var userDao = new UserDao();
		var users = userDao.findAll();
		for (var user : users) {
			jcmbUser.addItem(user.getUsername());
		}
	}

	private void chargeMonthData() {
		MONTHS.put("Enero", 1);
		MONTHS.put("Febrero", 2);
		MONTHS.put("Marzo", 3);
		MONTHS.put("Abril", 4);
		MONTHS.put("Mayo", 5);
		MONTHS.put("Junio", 6);
		MONTHS.put("Julio", 7);
		MONTHS.put("Agosto", 8);
		MONTHS.put("Septiembre", 9);
		MONTHS.put("Octubre", 10);
		MONTHS.put("Noviembre", 11);
		MONTHS.put("Diciembre", 12);

		for (var month : MONTHS.keySet()) {
			jcmbMonth.addItem((String) month);
		}
	}

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JButton jbtnGenerate;
    private static javax.swing.JComboBox<String> jcmbMonth;
    private static javax.swing.JComboBox<String> jcmbUser;
    private javax.swing.JLabel jlblMonth;
    private javax.swing.JLabel jlblUsername;
    // End of variables declaration//GEN-END:variables
}
