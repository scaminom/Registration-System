package GUI.application.form.other;

import GUI.application.exceptionHandler.ExceptionHandler;
import GUI.application.exceptionHandler.HibernateExceptionHandler;
import GUI.application.exceptionHandler.UserValidator;
import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.table.DefaultTableModel;

public class FormManage extends javax.swing.JPanel {

	private final UserDao userManage;
	private final ExceptionHandler exceptionHandler;
	private UserValidator validator;

	private static final String[] COLUMN_NAMES = {"Id", "Nombre", "Apellido", "Usuario", "Contraseña", "Email", "Genero"};

	public FormManage() {
		exceptionHandler = new HibernateExceptionHandler();
		initComponents();
		userManage = new UserDao();
		chargeDataTable();
		chargeTable();
	}

	private void chargeDataTable() {
		jtblEmployee.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
			int row = jtblEmployee.getSelectedRow();
			if (row != -1) {
				populateFieldsFromTable(row);
			}
		});
	}

	private void chargeTable() {
		DefaultTableModel model = new DefaultTableModel(COLUMN_NAMES, 0);
		try {
			var users = userManage.getUsers();
			for (User user : users) {
				model.addRow(new Object[]{user.getId(), user.getFirstName(), user.getLastName(), user.getUsername(), user.getPassword(), user.getEmail(), user.getGender()});
			}
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
		jtblEmployee.setModel(model);
	}

	private void populateFieldsFromTable(int row) {
		jtxtName.setText(String.valueOf(jtblEmployee.getValueAt(row, 1)));
		jtxtSurname.setText(String.valueOf(jtblEmployee.getValueAt(row, 2)));
		jtxtUsername.setText(String.valueOf(jtblEmployee.getValueAt(row, 3)));
		jtxtPassword.setText(String.valueOf(jtblEmployee.getValueAt(row, 4)));
		jtxtEmail.setText(String.valueOf(jtblEmployee.getValueAt(row, 5)));
		String gender = String.valueOf(jtblEmployee.getValueAt(row, 6));
		if ("Hombre".equals(gender)) {
			jrdMale.setSelected(true);
		} else if ("Mujer".equals(gender)) {
			jrdFemale.setSelected(true);
		}
	}

	private void saveUser() {
		try {
			String firstName = jtxtName.getText();
			String lastName = jtxtSurname.getText();
			String username = jtxtUsername.getText();
			String password = jtxtPassword.getText();
			String email = jtxtEmail.getText();
			var role = User.Role.EMPLOYEE;
			User user = null;
			double initialSalary = 800.0;
			if (jrdMale.isSelected()) {
				String male = "Hombre";
				user = new User(firstName, lastName, username, password, role, email, male, initialSalary);
			} else {
				String male = "Mujer";
				user = new User(firstName, lastName, username, password, role, email, male, initialSalary);
			}
			validator = new UserValidator(user);
			var errors = validator.validate();
			if (errors.isEmpty()) {
				userManage.saveUser(user);
				JOptionPane.showMessageDialog(null, "Empleado guardado exitosamente.");
				cleanFields();
				chargeTable();
			} else {
				errors.forEach((field, errorList) -> {
					errorList.forEach(error
							-> JOptionPane.showMessageDialog(null, error, "Error in " + field, JOptionPane.ERROR_MESSAGE));
				});
			}

		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	private User prepareUpdatedUser(Long id, User updatedUser) {
		User existingUser = userManage.getUser(id);
		if (existingUser == null) {
			throw new IllegalArgumentException("User not found with ID: " + id);
		}

		if (updatedUser.getFirstName() != null && !updatedUser.getFirstName().equals(existingUser.getFirstName())) {
			existingUser.setFirstName(updatedUser.getFirstName());
		}
		if (updatedUser.getLastName() != null && !updatedUser.getLastName().equals(existingUser.getLastName())) {
			existingUser.setLastName(updatedUser.getLastName());
		}
		if (updatedUser.getUsername() != null && !updatedUser.getUsername().equals(existingUser.getUsername())) {
			existingUser.setUsername(updatedUser.getUsername());
		}
		if (updatedUser.getPassword() != null && !updatedUser.getPassword().equals(existingUser.getPassword())) {
			existingUser.setPassword(updatedUser.getPassword());
		}
		if (updatedUser.getRole() != null && !updatedUser.getRole().equals(existingUser.getRole())) {
			existingUser.setRole(updatedUser.getRole());
		}
		if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
			existingUser.setEmail(updatedUser.getEmail());
		}
		if (updatedUser.getGender() != null && !updatedUser.getGender().equals(existingUser.getGender())) {
			existingUser.setGender(updatedUser.getGender());
		}

		if (updatedUser.getFingerprintPattern() != null && !updatedUser.getFingerprintPattern().equals(existingUser.getFingerprintPattern())) {
			existingUser.setFingerprintPattern(updatedUser.getFingerprintPattern());
		}

		return existingUser;
	}

	private User getUpdatedUserFromForm() {
		User updatedUser = new User();

		updatedUser.setFirstName(jtxtName.getText());
		updatedUser.setLastName(jtxtSurname.getText());
		updatedUser.setUsername(jtxtUsername.getText());
		updatedUser.setPassword(jtxtPassword.getText());
		updatedUser.setEmail(jtxtEmail.getText());

		updatedUser.setRole(User.Role.EMPLOYEE);

		updatedUser.setGender(jrdMale.isSelected() ? "Male" : "Female");
		return updatedUser;
	}

	private void updateUser() {
		try {
			int row = jtblEmployee.getSelectedRow();
			if (row != -1) {
				Long id = Long.valueOf(jtblEmployee.getValueAt(row, 0).toString());
				User updatedUser = getUpdatedUserFromForm();
				User user = prepareUpdatedUser(id, updatedUser);

				int response = JOptionPane.showConfirmDialog(null, "Are you sure you want to update the user with ID " + id + "?", "Update User", JOptionPane.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION) {
					performUpdate(user);
				}
			} else {
				JOptionPane.showMessageDialog(null, "Please select a user.", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	private void performUpdate(User user) {
		if (user != null) {
			UserValidator validator = new UserValidator(user);
			var errors = validator.validate();

			if (errors.isEmpty()) {
				userManage.updateUser(user);
				cleanFields();
				chargeTable();
				JOptionPane.showMessageDialog(null, "User updated successfully.", "Update", JOptionPane.INFORMATION_MESSAGE);
			} else {
				errors.forEach((field, errorList) -> {
					errorList.forEach(error -> JOptionPane.showMessageDialog(null, error, "Error in " + field, JOptionPane.ERROR_MESSAGE));
				});
			}
		}
	}

	private void deleteUser() {
		try {
			int row = jtblEmployee.getSelectedRow();
			if (row != -1) {
				Long id = Long.valueOf(jtblEmployee.getValueAt(row, 0).toString());
				int response = JOptionPane.showConfirmDialog(null, "Esta seguro de eliminar el empleado?", "Eliminar Usuario", JOptionPane.YES_NO_OPTION);
				if (response == JOptionPane.YES_OPTION) {
					userManage.deleteUser(id);
					cleanFields();
					chargeTable();
					JOptionPane.showMessageDialog(null, "Empleado eliminado exitosamente.");
				}
			} else {
				JOptionPane.showMessageDialog(null, "Por favor, selecciona un empleado.", "Error", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception e) {
			exceptionHandler.handleException(e);
		}
	}

	public void cleanFields() {
		jtxtName.setText("");
		jtxtSurname.setText("");
		jtxtUsername.setText("");
		jtxtPassword.setText("");
		jtxtEmail.setText("");
		jtblEmployee.clearSelection();
	}

	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jbtgGender = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jlbName = new javax.swing.JLabel();
        jlbSurname = new javax.swing.JLabel();
        jlbUsername = new javax.swing.JLabel();
        jlbPassword = new javax.swing.JLabel();
        jlbGender = new javax.swing.JLabel();
        jtxtName = new javax.swing.JTextField();
        jtxtUsername = new javax.swing.JTextField();
        jtxtSurname = new javax.swing.JTextField();
        jtxtPassword = new javax.swing.JTextField();
        jrdMale = new javax.swing.JRadioButton();
        jrdFemale = new javax.swing.JRadioButton();
        jlbEmail = new javax.swing.JLabel();
        jtxtEmail = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jtblEmployee = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jbtnSave = new javax.swing.JButton();
        jbtnEdit = new javax.swing.JButton();
        jbtnDelete = new javax.swing.JButton();
        jbtnClean = new javax.swing.JButton();

        jPanel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        jlbName.setText("Nombre:");

        jlbSurname.setText("Apellido:");

        jlbUsername.setText("Usuario:");

        jlbPassword.setText("Contrseña:");

        jlbGender.setText("Genero:");

        jbtgGender.add(jrdMale);
        jrdMale.setText("Hombre");

        jbtgGender.add(jrdFemale);
        jrdFemale.setText("Mujer");

        jlbEmail.setText("Email:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jlbName, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jtxtName, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jlbSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jtxtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jlbUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jtxtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addComponent(jlbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(jrdMale))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(144, 144, 144)
                .addComponent(jrdFemale))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(28, 28, 28)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jtxtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlbPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jtxtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jlbName))
                    .addComponent(jtxtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jlbSurname))
                    .addComponent(jtxtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jlbUsername))
                    .addComponent(jtxtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jlbPassword))
                    .addComponent(jtxtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addComponent(jlbEmail))
                    .addComponent(jtxtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(2, 2, 2)
                        .addComponent(jlbGender))
                    .addComponent(jrdMale))
                .addGap(6, 6, 6)
                .addComponent(jrdFemale)
                .addContainerGap(14, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jtblEmployee.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray));
        jtblEmployee.setModel(new javax.swing.table.DefaultTableModel(
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
        jtblEmployee.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jtblEmployee.setShowGrid(true);
        jScrollPane1.setViewportView(jtblEmployee);

        jPanel2.add(jScrollPane1);

        jPanel3.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel3.setLayout(new java.awt.GridBagLayout());

        jbtnSave.setText("Guardar");
        jbtnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnSaveActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.ipadx = 81;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 130, 0, 126);
        jPanel3.add(jbtnSave, gridBagConstraints);

        jbtnEdit.setText("Editar");
        jbtnEdit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnEditActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.ipadx = 81;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 130, 0, 126);
        jPanel3.add(jbtnEdit, gridBagConstraints);

        jbtnDelete.setText("Borrar");
        jbtnDelete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnDeleteActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.ipadx = 81;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 130, 0, 126);
        jPanel3.add(jbtnDelete, gridBagConstraints);

        jbtnClean.setText("Limpiar Campos");
        jbtnClean.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jbtnCleanActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.ipadx = 37;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.NORTHWEST;
        gridBagConstraints.insets = new java.awt.Insets(17, 130, 57, 126);
        jPanel3.add(jbtnClean, gridBagConstraints);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, 371, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 243, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnSaveActionPerformed
		saveUser();
    }//GEN-LAST:event_jbtnSaveActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnDeleteActionPerformed
		deleteUser();
    }//GEN-LAST:event_jbtnDeleteActionPerformed

    private void jbtnEditActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnEditActionPerformed
		updateUser();
    }//GEN-LAST:event_jbtnEditActionPerformed

    private void jbtnCleanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jbtnCleanActionPerformed
		cleanFields();
    }//GEN-LAST:event_jbtnCleanActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup jbtgGender;
    private javax.swing.JButton jbtnClean;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnEdit;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JLabel jlbEmail;
    private javax.swing.JLabel jlbGender;
    private javax.swing.JLabel jlbName;
    private javax.swing.JLabel jlbPassword;
    private javax.swing.JLabel jlbSurname;
    private javax.swing.JLabel jlbUsername;
    private javax.swing.JRadioButton jrdFemale;
    private javax.swing.JRadioButton jrdMale;
    private javax.swing.JTable jtblEmployee;
    private javax.swing.JTextField jtxtEmail;
    private javax.swing.JTextField jtxtName;
    private javax.swing.JTextField jtxtPassword;
    private javax.swing.JTextField jtxtSurname;
    private javax.swing.JTextField jtxtUsername;
    // End of variables declaration//GEN-END:variables
}
