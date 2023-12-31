package com.scrum.registrationsystem.service;

import com.scrum.registrationsystem.dao.FinesDAO;
import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.Fines;
import com.scrum.registrationsystem.entities.User;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class FinesCalculator {

	private final FinesDAO finesManage;
	private final UserDao userManage;

	public FinesCalculator() {
		finesManage = new FinesDAO();
		userManage = new UserDao();
	}

	public double calcularMultaMatutina(LocalDateTime horaEntrada) {
		double multa = 0.0;
		LocalTime inicioJornadaMatutina = LocalTime.of(8, 0);
		LocalTime finJornadaMatutina = LocalTime.of(13, 0);
		LocalTime horaEntradaTime = horaEntrada.toLocalTime();

		long minutosTarde = 0;
		if (horaEntradaTime.isAfter(inicioJornadaMatutina) && horaEntradaTime.isBefore(finJornadaMatutina)) {
			minutosTarde = ChronoUnit.MINUTES.between(inicioJornadaMatutina, horaEntradaTime);
		}
		multa = minutosTarde * 0.25;
		return multa;
	}

	public double calcularMultaVespertina(LocalDateTime horaEntrada) {
		double multa = 0.0;
		LocalTime inicioJornadaVespertina = LocalTime.of(14, 0);
		LocalTime finJornada = LocalTime.of(17, 0);
		LocalTime horaEntradaTime = horaEntrada.toLocalTime();

		long minutosTarde = 0;
		if (horaEntradaTime.isAfter(inicioJornadaVespertina) && horaEntradaTime.isBefore(finJornada)) {
			minutosTarde = ChronoUnit.MINUTES.between(inicioJornadaVespertina, horaEntradaTime);
		} else if (horaEntradaTime.isAfter(finJornada)) {
			minutosTarde = 480;
		}
		multa = minutosTarde * 0.25;
		return multa;
	}

	public double calcularMultaSalida(LocalDateTime horaSalida) {
		double multa = 0.0;
		LocalTime finJornadaMatutina = LocalTime.of(13, 0);
		LocalTime inicioJornadaVespertina = LocalTime.of(14, 0);
		LocalTime finJornada = LocalTime.of(17, 0);
		LocalTime horaSalidaTime = horaSalida.toLocalTime();

		if (horaSalidaTime.isBefore(finJornadaMatutina) || (horaSalidaTime.isBefore(finJornada) && horaSalidaTime.isAfter(inicioJornadaVespertina))) {
			LocalTime finJornadaReal = horaSalidaTime.isBefore(finJornadaMatutina) ? finJornadaMatutina : finJornada;
			long minutosTemprano = ChronoUnit.MINUTES.between(horaSalidaTime, finJornadaReal);
			multa = minutosTemprano * 0.25;
		}

		return multa;
	}

	public Fines procesarMultaEntrada(Long idEmpleado, LocalDateTime horaEntrada) {
		// Calcula las multas para cada jornada
		double multaMatutina = calcularMultaMatutina(horaEntrada);
		double multaVespertina = calcularMultaVespertina(horaEntrada);

		// Suma las multas para obtener la multa total
		double multaTotal = multaMatutina + multaVespertina;
		Fines multa = new Fines();

		if (multaTotal > 0) {
			User user = userManage.getUser(idEmpleado);

			// Crear y guardar la multa
			multa.setUser(user);
			multa.setDescripcion("Multa por entrada tardía");
			multa.setValorMulta(multaTotal);
			finesManage.saveMulta(multa);

			// Actualizar la información del usuario
			user.setId(idEmpleado);
			user.setFirstName(user.getFirstName());
			user.setLastName(user.getLastName());
			user.setUsername(user.getUsername());
			user.setPassword(user.getPassword());
			user.setEmail(user.getEmail());
			user.setRole(user.getRole());
			user.setBaseSalary(user.getBaseSalary());
			if (user.getSalaryRecived() > 0) {
				user.setSalaryRecived(user.getSalaryRecived() - multaTotal);
			} else {
				user.setSalaryRecived(0);
			}

			userManage.updateUser(user);
		}
		return multa;
	}

	public Fines procesarMultaSalida(Long idEmpleado, LocalDateTime horaSalida) {
		double multaSalida = calcularMultaSalida(horaSalida);
		User user = null;
		Fines multa = new Fines();
		if (multaSalida > 0) {
			user = userManage.getUser(idEmpleado);
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
			if (user.getSalaryRecived() > 0) {
				user.setSalaryRecived(user.getSalaryRecived() - multaSalida);
			} else {
				user.setSalaryRecived(0);
			}
			userManage.updateUser(user);

		}
		return multa;
	}
}
