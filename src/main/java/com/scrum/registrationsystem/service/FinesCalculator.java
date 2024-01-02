package com.scrum.registrationsystem.service;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.scrum.registrationsystem.dao.FinesDAO;
import com.scrum.registrationsystem.dao.RegisterDao;
import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.Fines;
import com.scrum.registrationsystem.entities.Register;
import com.scrum.registrationsystem.entities.User;

import GUI.application.exceptionHandler.ExceptionHandler;
import GUI.application.exceptionHandler.HibernateExceptionHandler;
import java.util.HashSet;
import java.util.Set;

public class FinesCalculator {

	private final RegisterDao registerDao;
	private final FinesDAO finesDAO;
	private final TimeService timeService;
	private final ExceptionHandler exceptionHandler;
	private final UserDao userDao;
	private static boolean isCalculated = false;

	public FinesCalculator() {
		this.registerDao = new RegisterDao();
		this.finesDAO = new FinesDAO();
		this.timeService = new TimeService();
		this.exceptionHandler = new HibernateExceptionHandler();
		this.userDao = new UserDao();
	}

	public void calculateAndStoreFines() {

		try {
			LocalDateTime now = timeService.getCurrentDateTime();
			if (isCalculated) {
				return;
			}

			if (now.toLocalTime().isBefore(LocalTime.of(17, 0))) {
				return;
			}

			System.out.println("Calculando multas");

			LocalDate today = now.toLocalDate();

			// Definir las horas de inicio y fin de las jornadas laborales
			LocalTime workMorningStart = LocalTime.of(8, 0);
			LocalTime workMorningEnd = LocalTime.of(13, 0);
			LocalTime workAfternoonStart = LocalTime.of(14, 0);
			LocalTime workAfternoonEnd = LocalTime.of(17, 0);

			// Obtener los registros de hoy
			List<Register> registers = registerDao.findAllRegistersByDate(today);

			// Obtener los usuarios
			List<User> users = userDao.findAll();

			// Agrupar registros por usuario
			Map<User, List<Register>> registersByUser = registers.stream()
					.collect(Collectors.groupingBy(Register::getUser));

			for (Map.Entry<User, List<Register>> entry : registersByUser.entrySet()) {
				User user = entry.getKey();
				List<Register> userRegisters = entry.getValue();
				
				// Eliminar usuarios que no tenga registros de la lista users por el id
				users.removeIf(u -> u.getId().equals(user.getId()));

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
				if (fine == 0) {
					continue;
				}
				Fines multa = new Fines(user,
						"Multa por no trabajar: " + calculateMinutos(hoursWorkedMorning, hoursWorkedAfternoon)
								+ " minutos",
						fine, new Date(now.toLocalDate().toEpochDay()));
				finesDAO.saveMulta(multa);
				;

			}

			// Aplicar multa por día completo de ausencia a los usuarios que no tengan
			// registros
			for (User user : users) {
				Fines multa = new Fines(user, "Multa por ausencia", 8 * 60 * 0.25,
						new Date(now.toLocalDate().toEpochDay()));
				finesDAO.saveMulta(multa);
			}

			isCalculated = true;

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
			LocalTime exitTime = register.getExitTime() != null ? register.getExitTime().toLocalTime()
					: LocalTime.of(17, 0);

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
		// Tasa de multa por minuto
		double fineRatePerMinute = 0.25;

		// Calcular la multa
		double fine = calculateMinutos(hoursWorkedMorning, hoursWorkedAfternoon) * fineRatePerMinute;

		// Asegurarse de que la multa no sea negativa
		return Math.max(fine, 0);
	}

	private double calculateMinutos(double hoursWorkedMorning, double hoursWorkedAfternoon) {
		// Horas totales de trabajo esperadas
		double expectedWorkingHours = 8.0;

		// Horas totales trabajadas
		double totalHoursWorked = hoursWorkedMorning + hoursWorkedAfternoon;

		// Calcular la diferencia en minutos
		double differenceInMinutes = (expectedWorkingHours - totalHoursWorked) * 60;

		return differenceInMinutes;

	}
}
