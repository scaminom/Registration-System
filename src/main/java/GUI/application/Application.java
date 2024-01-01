package GUI.application;

import GUI.application.form.Home;
import GUI.application.form.LoginForm;
import GUI.application.form.MainForm;
import GUI.application.form.other.FormRegister;
import com.formdev.flatlaf.FlatLaf;
import com.formdev.flatlaf.extras.FlatAnimatedLafChange;
import com.formdev.flatlaf.fonts.roboto.FlatRobotoFont;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.scrum.registrationsystem.biometrics.FingerprintManager;
import com.scrum.registrationsystem.biometrics.MyFingerprintCallback;
import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Application extends javax.swing.JFrame {
    private static Application app;
    private static MainForm mainForm;
    private final LoginForm loginForm;
    private FormRegister formRegister;
    private final Home home;
    private final UserDao userDao;
    FingerprintManager fingerprintManager = null;
    MyFingerprintCallback callback = null;
    private static User userLogged = null;
    
    public Application() {
        initComponents();
        setSize(new Dimension(1366, 768));
        setLocationRelativeTo(null);
        
        loginForm = new LoginForm();
        userDao = new UserDao();
        home = new Home();
        setContentPane(home);
        fingerprintManager = FingerprintManager.getInstance();
        callback = MyFingerprintCallback.getInstance(null);
        fingerprintManager.addFingerprintCallback(callback);
    }

    public static void showForm(Component component) {
        component.applyComponentOrientation(app.getComponentOrientation());
        mainForm.showForm(component);
    }

    public static void attendanceRecorder() {
        app.formRegister = new FormRegister();
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.formRegister);
        app.formRegister.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.formRegister);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void login(String usuario, String contraseña) {
        if (usuario.isEmpty() || contraseña.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Ingrese usuario y contraseña", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        User user =  app.userDao.validateUser(usuario, contraseña);
        if (user == null) {
            JOptionPane.showMessageDialog(null, "Usuario o contraseña incorrectos", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        setUserLogged(user);
        mainForm = new MainForm();
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(mainForm);
        mainForm.applyComponentOrientation(app.getComponentOrientation());
        setSelectedMenu(0, 0);
        mainForm.hideMenu();
        SwingUtilities.updateComponentTreeUI(mainForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
    }

    public static void logout() {
        FlatAnimatedLafChange.showSnapshot();
        app.setContentPane(app.loginForm);
        app.loginForm.applyComponentOrientation(app.getComponentOrientation());
        SwingUtilities.updateComponentTreeUI(app.loginForm);
        FlatAnimatedLafChange.hideSnapshotWithAnimation();
        setUserLogged(null);
    }

    public static void setSelectedMenu(int index, int subIndex) {
        mainForm.setSelectedMenu(index, subIndex);
    }

    

    public static User getUserLogged() {
        return userLogged;
    }

    public static void setUserLogged(User userLogged) {
        Application.userLogged = userLogged;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated
    // Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 719, Short.MAX_VALUE));
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 521, Short.MAX_VALUE));

        pack();
    }// </editor-fold>//GEN-END:initComponents

    public static void main(String args[]) {
        System.getProperty("java.classpath");
        FlatRobotoFont.install();
        FlatLaf.registerCustomDefaultsSource("GUI.theme");
        UIManager.put("defaultFont", new Font(FlatRobotoFont.FAMILY, Font.PLAIN, 13));
        FlatMacDarkLaf.setup();
        java.awt.EventQueue.invokeLater(() -> {
            app = new Application();
            // app.applyComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
            app.setVisible(true);
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
