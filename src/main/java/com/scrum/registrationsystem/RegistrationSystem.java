package com.scrum.registrationsystem;

import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;

public class RegistrationSystem {

	public static void main(String[] args) {
		User user = new User();
		user.setFirstName("Adrian");
		user.setLastName("Jurado");
		user.setEmail("test23@gmail.com");
		user.setPassword("test1234");
		user.setUsername("adri4");
		user.setBaseSalary(800);
		user.setSalaryRecived(user.getBaseSalary());
		user.setRole(User.Role.EMPLOYEE);
		UserDao userdao = new UserDao();
		userdao.create(user);

		System.out.println("User id= " + user.getId());

		// Procesar multa de entrada
		// finesCalculator.procesarMultaEntrada(idEmpleado, horaEntradaEmpleado);
		// Procesar multa de salida
		//finesCalculator.procesarMultaSalida(idEmpleado, horaSalidaEmpleado);
		var userDao = new UserDao();
		user = userDao.findById(4L);
		var registrations = user.getRegistrations();

		for (var registration : registrations) {
			System.out.println(registration);
		}
		user.setUsername("adri14");
		user.setBaseSalary(800);
		user.setSalaryRecived(user.getBaseSalary());
		user.setRole(User.Role.EMPLOYEE);
		userdao.create(user);
                
	}
}
