package GUI.application.form.other;

import GUI.application.exceptionHandler.UserValidator;
import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;
import java.util.stream.Collectors;
import javax.swing.JOptionPane;

public class FormManage extends javax.swing.JPanel {

    private UserValidator validator;

    private static final String[] COLUMN_NAMES = { "Id", "Nombre", "Apellido", "Usuario", "Contraseña", "Email",
            "Genero", "Rol" };

    public FormManage() {
        initComponents();
        loadDataToTable();
    }

    public void loadDataToFields() {
        var manager = new PanelManager(new UserDao());
        manager.loadDataToFields(jtblEmployee, rowData -> {
            jtxtName.setText(String.valueOf(rowData[1]));
            jtxtSurname.setText(String.valueOf(rowData[2]));
            jtxtUsername.setText(String.valueOf(rowData[3]));
            jtxtPassword.setText(String.valueOf(rowData[4]));
            jtxtEmail.setText(String.valueOf(rowData[5]));

            String gender = String.valueOf(rowData[6]);
            if ("Hombre".equals(gender)) {
                jrdMale.setSelected(true);
            } else if ("Mujer".equals(gender)) {
                jrdFemale.setSelected(true);
            }

            String role = String.valueOf(rowData[7]);
            if ("Admin".equals(role)) {
                jrdAdmin.setSelected(true);
            } else if ("Empleado".equals(role)) {
                jrdEmploye.setSelected(true);
            }
        });
    }

    public void loadDataToTable() {

        var manager = new PanelManager(new UserDao());
        manager.loadDataToTable(jtblEmployee, (entity, tableModel) -> {
            var users = entity.stream().map(user -> (User) user).collect(Collectors.toList());
            for (int i = 0; i < users.size(); i++) {
                String role = null;
                if (users.get(i).getRole() == User.Role.ADMIN) {
                    role = "Admin";
                } else if (users.get(i).getRole() == User.Role.EMPLOYEE) {
                    role = "Empleado";
                }
                Object[] object = { users.get(i).getId(), users.get(i).getFirstName(), users.get(i).getLastName(),
                        users.get(i).getUsername(), users.get(i).getPassword(), users.get(i).getEmail(),
                        users.get(i).getGender(), role };
                tableModel.addRow(object);
            }
            tableModel.setColumnIdentifiers(COLUMN_NAMES);
        });
    }

    public void saveUser() {
        String firstName = jtxtName.getText();
        String lastName = jtxtSurname.getText();
        String username = jtxtUsername.getText();
        String password = jtxtPassword.getText();
        String email = jtxtEmail.getText();
        String gender = null;
        if (jrdMale.isSelected()) {
            gender = "Hombre";
        }

        if (jrdFemale.isSelected()) {
            gender = "Mujer";
        }

        User.Role role = null;
        if (jrdAdmin.isSelected()) {
            role = User.Role.ADMIN;
        }

        if (jrdEmploye.isSelected()) {
            role = User.Role.EMPLOYEE;
        }

        double initialSalary = 800.0;
        var user = new User(firstName, lastName, username, password, role, email, gender, initialSalary);
        validator = new UserValidator(user);
        var errors = validator.validate();
        if (errors.isEmpty()) {
            var manager = new PanelManager(new UserDao());
            manager.insertData(entity -> user);
            cleanFields();
            loadDataToTable();
        } else {
            errors.forEach((field, errorList) -> {
                errorList.forEach(error -> JOptionPane.showMessageDialog(null, error, "Error in " + field,
                        JOptionPane.ERROR_MESSAGE));
            });
        }
    }

    public void updateUser() {
        int row = jtblEmployee.getSelectedRow();
        if (row != -1) {
            var user = new User();
            user.setFirstName(jtxtName.getText());
            user.setLastName(jtxtSurname.getText());
            user.setUsername(jtxtUsername.getText());
            user.setPassword(jtxtPassword.getText());
            user.setEmail(jtxtEmail.getText());
            if (jrdMale.isSelected()) {
                user.setGender("Hombre");
            } else if (jrdFemale.isSelected()) {
                user.setGender("Mujer");
            } else {
                user.setGender(null);
            }

            if (jrdAdmin.isSelected()) {
                user.setRole(User.Role.ADMIN);
            } else if (jrdEmploye.isSelected()) {
                user.setRole(User.Role.EMPLOYEE);
            } else {
                user.setRole(null);
            }

            validator = new UserValidator(user);
            var errors = validator.validate();

            if (errors.isEmpty()) {
                var manager = new PanelManager(new UserDao());
                manager.updateData(jtblEmployee, entity -> {
                    var userRecived = (User) entity;
                    userRecived.setFirstName(user.getFirstName());
                    userRecived.setLastName(user.getLastName());
                    userRecived.setUsername(user.getUsername());
                    userRecived.setPassword(user.getPassword());
                    userRecived.setEmail(user.getEmail());
                    userRecived.setGender(user.getGender());
                    return entity;
                });

                cleanFields();
                loadDataToTable();
            } else {
                errors.forEach((field, errorList) -> {
                    errorList.forEach(error -> JOptionPane.showMessageDialog(null, error, "Error in " + field,
                            JOptionPane.ERROR_MESSAGE));
                });
            }
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void deleteUser() {
        int row = jtblEmployee.getSelectedRow();
        if (row != -1) {
            var manager = new PanelManager(new UserDao());
            manager.deleteData(jtblEmployee);

            loadDataToTable();
            cleanFields();
        } else {
            JOptionPane.showMessageDialog(null, "Seleccione un usuario.", "Error", JOptionPane.ERROR_MESSAGE);
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
    // <editor-fold defaultstate="collapsed" desc="Generated
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        jbtgGender = new javax.swing.ButtonGroup();
        jbtgRol = new javax.swing.ButtonGroup();
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
        jlbRol = new javax.swing.JLabel();
        jrdEmploye = new javax.swing.JRadioButton();
        jrdAdmin = new javax.swing.JRadioButton();
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

        jlbRol.setText("Rol:");

        jbtgRol.add(jrdEmploye);
        jrdEmploye.setText("Empleado");

        jbtgRol.add(jrdAdmin);
        jrdAdmin.setText("Admin");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlbName, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jtxtName, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlbSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jtxtSurname, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlbUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jtxtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlbGender, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(34, 34, 34)
                        .addComponent(jrdMale))
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jlbEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addComponent(jtxtEmail, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(jPanel1Layout.createSequentialGroup()
                            .addComponent(jlbPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 82, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(34, 34, 34)
                            .addComponent(jtxtPassword, javax.swing.GroupLayout.PREFERRED_SIZE, 157, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jlbRol)
                        .addGap(96, 96, 96)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jrdEmploye)
                            .addComponent(jrdFemale)
                            .addComponent(jrdAdmin))))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
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
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jlbRol)
                    .addComponent(jrdEmploye))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jrdAdmin)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jPanel2.setLayout(new javax.swing.BoxLayout(jPanel2, javax.swing.BoxLayout.LINE_AXIS));

        jtblEmployee.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray, java.awt.Color.lightGray));
        jtblEmployee.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4", "Title 5", "Title 6", "Title 7", "Title 8"
            }
        ));
        jtblEmployee.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        jtblEmployee.setShowGrid(true);
        jtblEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jtblEmployeeMouseClicked(evt);
            }
        });
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
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void jbtnSaveActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jbtnSaveActionPerformed
        saveUser();
    }// GEN-LAST:event_jbtnSaveActionPerformed

    private void jbtnDeleteActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jbtnDeleteActionPerformed
        deleteUser();
    }// GEN-LAST:event_jbtnDeleteActionPerformed

    private void jbtnEditActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jbtnEditActionPerformed
        updateUser();
    }// GEN-LAST:event_jbtnEditActionPerformed

    private void jbtnCleanActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_jbtnCleanActionPerformed
        cleanFields();
    }// GEN-LAST:event_jbtnCleanActionPerformed

    private void jtblEmployeeMouseClicked(java.awt.event.MouseEvent evt) {// GEN-FIRST:event_jtblEmployeeMouseClicked
        loadDataToFields();
    }// GEN-LAST:event_jtblEmployeeMouseClicked

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.ButtonGroup jbtgGender;
    private javax.swing.ButtonGroup jbtgRol;
    private javax.swing.JButton jbtnClean;
    private javax.swing.JButton jbtnDelete;
    private javax.swing.JButton jbtnEdit;
    private javax.swing.JButton jbtnSave;
    private javax.swing.JLabel jlbEmail;
    private javax.swing.JLabel jlbGender;
    private javax.swing.JLabel jlbName;
    private javax.swing.JLabel jlbPassword;
    private javax.swing.JLabel jlbRol;
    private javax.swing.JLabel jlbSurname;
    private javax.swing.JLabel jlbUsername;
    private javax.swing.JRadioButton jrdAdmin;
    private javax.swing.JRadioButton jrdEmploye;
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
