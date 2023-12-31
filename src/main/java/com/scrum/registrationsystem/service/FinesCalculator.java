package com.scrum.registrationsystem.service;

import com.scrum.registrationsystem.entities.Fines;
import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.dao.FinesDAO;
import com.scrum.registrationsystem.dao.RegisterDao;
import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.Register;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class FinesCalculator {

   private final FinesDAO finesManage;
   private final UserDao userManage;
   private final RegisterDao registerDao;
   private static final double MULTA_POR_MINUTO = 0.25;
    private static final int HORA_INICIO_JORNADA_MATUTINA = 8; // 8 AM
    private static final int HORA_FIN_JORNADA_MATUTINA = 13; // 1 PM
    private static final int HORA_INICIO_JORNADA_VESPERTINA = 14; // 2 PM
    private static final int HORA_FIN_JORNADA_VESPERTINA = 17; // 5 PM
    private static final int MINUTOS_JORNADA_COMPLETA = 480; // 8 horas
   
   public FinesCalculator(){
	finesManage = new FinesDAO();
	userManage = new UserDao();
        registerDao = new RegisterDao();
   }

    public double calcularMulta(Long userId, LocalDateTime hora, boolean esEntrada) {
        Register ultimoRegistro = registerDao.getLastRegisterForUser(userId);
         if (esEntrada && hora.getHour() >= HORA_INICIO_JORNADA_VESPERTINA) {
            if (ultimoRegistro == null || ultimoRegistro.getEntryTime().getHour() >= HORA_INICIO_JORNADA_VESPERTINA) {
            // Si se registra por primera vez en la tarde, cobrar la jornada matutina completa como multa
            return 300 * MULTA_POR_MINUTO; // la mitad del día
            }
        }
            // Comprobar si es una salida y si falta el registro de entrada en la tarde
        if (!esEntrada && (ultimoRegistro != null && ultimoRegistro.getExitTime() == null)) {
            // Si el último registro es de la mañana y no hay registro de entrada en la tarde
            if (ultimoRegistro.getEntryTime().getHour() < HORA_INICIO_JORNADA_VESPERTINA && hora.getHour() >= HORA_FIN_JORNADA_VESPERTINA) {
            // Aplicar multa por no registrar entrada en la jornada vespertina
            return (HORA_FIN_JORNADA_VESPERTINA - HORA_INICIO_JORNADA_VESPERTINA) * 60 * MULTA_POR_MINUTO;
            }
        }
        // Obtener el último registro para este usuario
        if (esEntrada && hora.getHour() >= HORA_FIN_JORNADA_VESPERTINA) {
            // Si se registra después de las 5 PM, cobrar toda la jornada como multa
            return MINUTOS_JORNADA_COMPLETA * MULTA_POR_MINUTO;
        }
        
        if (ultimoRegistro == null) {
            // Si no hay registros previos, calcular multa normalmente
            return calcularMultaBasica(hora, esEntrada);
        }

        // Determinar si ya se ha registrado en la jornada matutina/vespertina
        boolean yaRegistradoMatutina = ultimoRegistro.getEntryTime().getHour() < HORA_FIN_JORNADA_MATUTINA;
        boolean yaRegistradoVespertina = ultimoRegistro.getEntryTime().getHour() >= HORA_INICIO_JORNADA_VESPERTINA;

        // Calcular multa en base a si ya se ha registrado
        if (esEntrada) {
            if (yaRegistradoMatutina && hora.getHour() >= HORA_INICIO_JORNADA_VESPERTINA) {
                // Caso especial: Ya registrado en matutina, pero llega tarde en vespertina
                return calcularMultaDesdeHasta(hora, HORA_INICIO_JORNADA_VESPERTINA, HORA_FIN_JORNADA_VESPERTINA);
            }
            return calcularMultaBasica(hora, true);
        } else {
            if (yaRegistradoVespertina) {
                // Si ya se registró en la vespertina, no hay multa por salida
                return 0;
            }
            return calcularMultaBasica(hora, false);
        }
    }

private double calcularMultaBasica(LocalDateTime hora, boolean esEntrada) {
    int horaInicio;
    int horaFin;

    if (esEntrada) {
        if (hora.getHour() >=  HORA_INICIO_JORNADA_VESPERTINA) {
            // Entrada vespertina
            horaInicio = HORA_INICIO_JORNADA_VESPERTINA;
            horaFin = HORA_FIN_JORNADA_VESPERTINA;
        } else {
            // Entrada matutina
            horaInicio = HORA_INICIO_JORNADA_MATUTINA;
            horaFin = HORA_FIN_JORNADA_MATUTINA;
        }

        long minutosDiferencia = ChronoUnit.MINUTES.between(LocalDateTime.of(hora.toLocalDate(), hora.toLocalTime().withHour(horaInicio).withMinute(0)), hora);
        return (minutosDiferencia > 0) ? minutosDiferencia * MULTA_POR_MINUTO : 0;
    } else {
        horaInicio = (hora.getHour() < HORA_INICIO_JORNADA_VESPERTINA) ? HORA_INICIO_JORNADA_MATUTINA : HORA_INICIO_JORNADA_VESPERTINA;
        horaFin = (hora.getHour() < HORA_INICIO_JORNADA_VESPERTINA) ? HORA_FIN_JORNADA_MATUTINA : HORA_FIN_JORNADA_VESPERTINA;

        long minutosDiferencia = ChronoUnit.MINUTES.between(hora, LocalDateTime.of(hora.toLocalDate(), hora.toLocalTime().withHour(horaFin).withMinute(0)));
        return (minutosDiferencia > 0) ? minutosDiferencia * MULTA_POR_MINUTO : 0;
    }
}
    

    private double calcularMultaDesdeHasta(LocalDateTime hora, int horaInicio, int horaFin) {
        long minutosDiferencia = ChronoUnit.MINUTES.between(LocalDateTime.of(hora.toLocalDate(), hora.toLocalTime().withHour(horaInicio).withMinute(0)), LocalDateTime.of(hora.toLocalDate(), hora.toLocalTime().withHour(horaFin).withMinute(0)));
        if (minutosDiferencia < 0) {
            return 0;
        }
        return minutosDiferencia * MULTA_POR_MINUTO;
    }


public void procesarMultaEntrada(Long idEmpleado, LocalDateTime horaEntrada) {
        double multaTotal = calcularMulta(idEmpleado, horaEntrada, true);
        User user = null;
        if (multaTotal > 0) {
            user = userManage.getUser(idEmpleado);
             Fines multa = new Fines();
        multa.setUser(user);
        multa.setDescripcion("Multa por entrada tardía");
        multa.setValorMulta(multaTotal);
        finesManage.saveMulta(multa);

            actualizarSalarioUsuario(user, multaTotal);
        }
    }

    public void procesarMultaSalida(Long idEmpleado, LocalDateTime horaSalida) {
        double multaSalida = calcularMulta(idEmpleado, horaSalida, false);
        User user = null;
        if (multaSalida > 0) {
             user = userManage.getUser(idEmpleado);
            Fines multa = new Fines();
            multa.setUser(user);
            multa.setDescripcion("Multa por salida temprana");
            multa.setValorMulta(multaSalida);
            finesManage.saveMulta(multa);

            actualizarSalarioUsuario(user, multaSalida);
        }
    }

    private void actualizarSalarioUsuario(User user, double ajusteSalario) {
            double salarioActual = user.getSalaryRecived();
            user.setId(user.getId());
	    user.setFirstName(user.getFirstName());
	    user.setLastName(user.getLastName());
	    user.setUsername(user.getUsername());
	    user.setPassword(user.getPassword());
	    user.setEmail(user.getEmail());
	    user.setRole(user.getRole());
            user.setSalaryRecived(Math.max(0, salarioActual - ajusteSalario));
            userManage.updateUser(user);
    }
}