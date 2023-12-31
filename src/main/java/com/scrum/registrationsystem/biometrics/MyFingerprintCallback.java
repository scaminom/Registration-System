/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scrum.registrationsystem.biometrics;

import com.scrum.registrationsystem.dao.RegisterDao;
import com.scrum.registrationsystem.entities.Register;
import com.scrum.registrationsystem.entities.User;

import GUI.application.exceptionHandler.ExceptionHandler;
import GUI.application.exceptionHandler.HibernateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;

/**
 *
 * @author patri
 */
public class MyFingerprintCallback implements FingerprintCallback {

    private JButton btnImg = null;
    private final RegisterDao registerManage;
    private final ExceptionHandler exceptionHandler;

    public MyFingerprintCallback(JButton btnImage) {
        this.btnImg = btnImage;
        registerManage = new RegisterDao();
        exceptionHandler = new HibernateExceptionHandler();
    }

    public MyFingerprintCallback() {
        registerManage = new RegisterDao();
        exceptionHandler = new HibernateExceptionHandler();
        this.btnImg = new JButton();
    }

    @Override
    public void onFingerprintCaptured(byte[] imgBuf) {
        try {
            btnImg.setIcon(new ImageIcon(ImageIO.read(new File("fingerprint.bmp"))));
        } catch (IOException ex) {
            Logger.getLogger(MyFingerprintCallback.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void onFingerprintSuccess(String message) {
        JOptionPane.showMessageDialog(null, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    @Override
    public void onFingerprintError(String errorMessage) {
        JOptionPane.showMessageDialog(null, errorMessage, "Error", JOptionPane.ERROR_MESSAGE);
    }

    @Override
    public User onUserIdentify(User user) {
        try {
            LocalDateTime entryTime = LocalDateTime.now();
            Register register = new Register(entryTime, null, user);
            user.addRegistration(register);
            registerManage.saveRegister(register);
            JOptionPane.showMessageDialog(null, "Registro guardado exitosamente.");
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return user;
    }

    private static MyFingerprintCallback instance;

    public static MyFingerprintCallback getInstance(JButton btnImage) {
        if (instance == null) {
            if (btnImage == null) {
                instance = new MyFingerprintCallback();
            } else {
                instance = new MyFingerprintCallback(btnImage);
            }
        }

        if (btnImage != null) {
            instance.setBtnImg(btnImage);
        }

        return instance;
    }

    public JButton getBtnImg() {
        return btnImg;
    }

    public void setBtnImg(JButton btnImg) {
        this.btnImg = btnImg;
    }

    

}
