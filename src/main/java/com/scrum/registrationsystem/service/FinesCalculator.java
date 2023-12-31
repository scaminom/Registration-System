package com.scrum.registrationsystem.service;

import com.scrum.registrationsystem.entities.Fines;
import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.dao.FinesDAO;
import com.scrum.registrationsystem.dao.UserDao;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class FinesCalculator {

   private final FinesDAO finesManage;
   private final UserDao userManage;
   
   public FinesCalculator(){
	finesManage = new FinesDAO();
	userManage = new UserDao();
   }

   public double calcularMultaEntrada(LocalTime horaEntrada) {
        double multa = 0.0;
        LocalTime inicioJornada = LocalTime.of(8, 0);

        if (horaEntrada.isAfter(inicioJornada)) {
            long minutosTarde = ChronoUnit.MINUTES.between(inicioJornada, horaEntrada);
            multa = minutosTarde * 0.25;
        }

        return multa;
    }

    public double calcularMultaSalida(LocalTime horaSalida) {
        double multa = 0.0;
        LocalTime finJornadaMatutina = LocalTime.of(13, 0);
        LocalTime inicioJornadaVespertina = LocalTime.of(14, 0);
        LocalTime finJornada = LocalTime.of(17, 0);

        if (horaSalida.isBefore(finJornadaMatutina) || (horaSalida.isBefore(finJornada) && 				    horaSalida.isAfter(inicioJornadaVespertina))) {
            LocalTime finJornadaReal = horaSalida.isBefore(finJornadaMatutina) ? finJornadaMatutina : finJornada;
            long minutosTemprano = ChronoUnit.MINUTES.between(horaSalida, finJornadaReal);
            multa = minutosTemprano * 0.25;
        }

        return multa;

	}

    public void procesarMultaEntrada(Long idEmpleado, LocalTime horaEntrada) {
        double multaEntrada = calcularMultaEntrada(horaEntrada);
	User user = null;
        if (multaEntrada > 0) {
            user = userManage.getUser(idEmpleado);
            Fines multa = new Fines ();
            multa.setUser(user);
            multa.setDescripcion("Multa por entrada tardía");
            multa.setValorMulta(multaEntrada);
            finesManage.saveMulta(multa);
            user.setId(idEmpleado);
	    user.setFirstName(user.getFirstName());
	    user.setLastName(user.getLastName());
	    user.setUsername(user.getUsername());
	    user.setPassword(user.getPassword());
	    user.setEmail(user.getEmail());
	    user.setRole(user.getRole());
	    user.setBaseSalary(user.getBaseSalary());
	    user.setSalaryRecived(user.getSalaryRecived()-multaEntrada);
	    userManage.updateUser(user);
        }
    }

    public void procesarMultaSalida(Long idEmpleado, LocalTime horaSalida) {
        double multaSalida = calcularMultaSalida(horaSalida);
	User user = null;
        if (multaSalida > 0) {
            user = userManage.getUser(idEmpleado);
            Fines multa = new Fines();
            multa.setUser(user);
            multa.setDescripcion("Multa por salida temprana");
            multa.setValorMulta(multaSalida);
            finesManage.saveMulta(multa);
            user.setId(idEmpleado);
	    user.setFirstName(user.getFirstName());
	    user.setLastName(user.getLastName());
	    user.setUsername(user.getUsername());
	    user.setPassword(user.getPassword());
	    user.setEmail(user.getEmail());
	    user.setRole(user.getRole());
	    user.setBaseSalary(user.getBaseSalary());
	    user.setSalaryRecived(user.getSalaryRecived()-multaSalida);
	    userManage.updateUser(user);

        }
    }
}