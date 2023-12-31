package com.scrum.registrationsystem;

import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.service.FinesCalculator;
import java.time.LocalDateTime;

public class RegistrationSystem {

	public static void main(String[] args) {
		User user = new User();
//		user.setFirstName("Adrian");
//		user.setLastName("Jurado");
//		user.setEmail("test31@gmail.com");
//		user.setPassword("test1234");
//		user.setUsername("adri26");
//		user.setBaseSalary(800);
//		user.setSalaryRecived(user.getBaseSalary());
//		user.setRole(User.Role.EMPLOYEE);
		UserDao userdao = new UserDao();
//		userdao.create(user);
                user = userdao.findById(30L);

//		System.out.println("User id= " + user.getId());
//		LocalDateTime horaEntradaEmpleado;
//		//LocalDateTime horaEntradaEmpleado = LocalTime.of(8, 15); // Entró a las 8:15 AM
//		//LocalTime horaSalidaEmpleado = LocalTime.of(16, 45); // Salió a las 4:45 PM
//		Long idEmpleado = user.getId(); // Suponiendo que ya tienes un empleado con ID 1 en la base de datos
		FinesCalculator finesCalculator = new FinesCalculator();
//
//		// Procesar multa de entrada
//		// finesCalculator.procesarMultaEntrada(idEmpleado, horaEntradaEmpleado);
//		// Procesar multa de salida
//		//finesCalculator.procesarMultaSalida(idEmpleado, horaSalidaEmpleado);
//		var userDao = new UserDao();
//		user = userDao.findById(4L);
		var registrations = user.getRegistrations();
//
		for (var registration : registrations) {
			System.out.println(registration);
		}
//		user.setUsername("adri14");
//		user.setBaseSalary(800);
//		user.setSalaryRecived(user.getBaseSalary());
//		user.setRole(User.Role.EMPLOYEE);
//		userdao.create(user);

		System.out.println("User id= " + user.getId());
		LocalDateTime horaEntrada = LocalDateTime.of(2024, 1, 1, 15, 0);
		LocalDateTime horaSalida = LocalDateTime.of(2024, 1, 1, 16, 0);

		finesCalculator.procesarMultaEntrada(user.getId(), horaEntrada);
		finesCalculator.procesarMultaSalida(user.getId(), horaSalida);
		System.out.println("Usuario: " + user.getUsername());
		System.out.println("Salario recibido después de multas: " + user.getSalaryRecived());
	}
}
