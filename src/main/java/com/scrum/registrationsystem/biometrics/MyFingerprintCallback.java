/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.scrum.registrationsystem.biometrics;

import com.scrum.registrationsystem.dao.FinesDAO;
import com.scrum.registrationsystem.dao.RegisterDao;
import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.Fines;
import com.scrum.registrationsystem.entities.Register;
import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.service.TimeService;

import GUI.application.exceptionHandler.ExceptionHandler;
import GUI.application.exceptionHandler.HibernateExceptionHandler;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
    private final RegisterDao registerDao = new RegisterDao();
    private final FinesDAO finesDAO = new FinesDAO();
    TimeService timeService = new TimeService();

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
            LocalDateTime now = timeService.getCurrentDateTime();
            Register register = registerDao.findLastRecordByUser(user.getId());

            // Definiendo las horas de inicio y fin de los periodos permitidos
            LocalDateTime morningStartTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(7, 45));
            LocalDateTime morningEndTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(13, 0));
            LocalDateTime afternoonStartTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(14, 45));
            LocalDateTime afternoonEndTime = LocalDateTime.of(now.toLocalDate(), LocalTime.of(17, 0));

            // Determinar si es hora de entrada
            boolean isMorningEntryTime = now.isAfter(morningStartTime) && now.isBefore(morningEndTime);
            boolean isAfternoonEntryTime = now.isAfter(afternoonStartTime);
            boolean isEndTime = now.isBefore(afternoonEndTime);

            if (register == null || (register.getEntryTime() != null && register.getExitTime() != null)) {
                if (isMorningEntryTime || isAfternoonEntryTime) {
                    // Registro de entrada
                    Register newRegister = new Register(now, null, user);
                    user.addRegistration(newRegister);
                    registerManage.saveRegister(newRegister);
                    JOptionPane.showMessageDialog(null,
                            user.getFirstName() + " " + user.getLastName() + " entrada registrada correctamente.");
                } else {
                    JOptionPane.showMessageDialog(null, "No es hora de registro de entrada.");
                }
            } else if (register.getEntryTime() != null) {
                // Registro de salida
                register.setExitTime(now);
                registerManage.updateRegister(register);
                JOptionPane.showMessageDialog(null,
                        user.getFirstName() + " " + user.getLastName() + " salida registrada correctamente.");
            }

            // Calcular y almacenar multas
            if (isEndTime) {
                calculateAndStoreFines();
            }
        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
        return user;
    }

    private void calculateAndStoreFines() {
        try {
            LocalDateTime now = timeService.getCurrentDateTime();
            LocalDate today = now.toLocalDate();

            // Definir las horas de inicio y fin de las jornadas laborales
            LocalTime workMorningStart = LocalTime.of(8, 0);
            LocalTime workMorningEnd = LocalTime.of(13, 0);
            LocalTime workAfternoonStart = LocalTime.of(14, 0);
            LocalTime workAfternoonEnd = LocalTime.of(17, 0);

            // Obtener los registros de hoy
            List<Register> registers = registerDao.findAllRegistersByDate(today);

            // Agrupar registros por usuario
            Map<User, List<Register>> registersByUser = registers.stream()
                    .collect(Collectors.groupingBy(Register::getUser));

            for (Map.Entry<User, List<Register>> entry : registersByUser.entrySet()) {
                User user = entry.getKey();
                List<Register> userRegisters = entry.getValue();

                // Verificar si hay registros para el día
                if (userRegisters.isEmpty()) {
                    // Aplicar multa por día completo de ausencia
                    Fines multa = new Fines(user, "Multa por ausencia", 8 * 60 * 0.25,
                            new Date(now.toLocalDate().toEpochDay()));
                    finesDAO.saveMulta(multa);
                    continue;
                }

                // Calcular las horas trabajadas en los intervalos definidos
                double hoursWorkedMorning = calculateHoursWorkedInInterval(userRegisters, workMorningStart,
                        workMorningEnd, now.toLocalTime());
                double hoursWorkedAfternoon = calculateHoursWorkedInInterval(userRegisters, workAfternoonStart,
                        workAfternoonEnd, now.toLocalTime());

                // Calcular la multa
                double fine = calculateFine(hoursWorkedMorning, hoursWorkedAfternoon);
                Fines multa = new Fines(user, "Multa por ausencia", fine, new Date(now.toLocalDate().toEpochDay()));
                finesDAO.saveMulta(multa);
                ;

            }

        } catch (Exception e) {
            exceptionHandler.handleException(e);
        }
    }

    private double calculateHoursWorkedInInterval(List<Register> registers, LocalTime intervalStart,
            LocalTime intervalEnd, LocalTime currentTime) {
        double totalHours = 0.0;

        for (Register register : registers) {
            LocalTime entryTime = register.getEntryTime() != null ? register.getEntryTime().toLocalTime()
                    : LocalTime.MIN;
            LocalTime exitTime = register.getExitTime() != null ? register.getExitTime().toLocalTime() : currentTime;

            // Ajustar tiempos para que estén dentro del intervalo
            entryTime = entryTime.isBefore(intervalStart) ? intervalStart : entryTime;
            exitTime = exitTime.isAfter(intervalEnd) ? intervalEnd : exitTime;

            // Calcular duración si el registro está dentro del intervalo
            if (!entryTime.isAfter(intervalEnd) && !exitTime.isBefore(intervalStart)) {
                long minutesWorked = Duration.between(entryTime, exitTime).toMinutes();
                totalHours += minutesWorked / 60.0;
            }
        }

        return totalHours;
    }

    private double calculateFine(double hoursWorkedMorning, double hoursWorkedAfternoon) {
        // Horas totales de trabajo esperadas
        double expectedWorkingHours = 8.0;

        // Horas totales trabajadas
        double totalHoursWorked = hoursWorkedMorning + hoursWorkedAfternoon;

        // Calcular la diferencia en minutos
        double differenceInMinutes = (expectedWorkingHours - totalHoursWorked) * 60;

        // Tasa de multa por minuto
        double fineRatePerMinute = 0.25;

        // Calcular la multa
        double fine = differenceInMinutes * fineRatePerMinute;

        // Asegurarse de que la multa no sea negativa
        return Math.max(fine, 0);
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
