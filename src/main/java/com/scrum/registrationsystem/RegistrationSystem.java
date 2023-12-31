/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.scrum.registrationsystem;

import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.service.FinesCalculator;
import java.time.LocalTime;

/**
 *
 * @author joses
 */
public class RegistrationSystem {

	public static void main(String[] args) {
<<<<<<< HEAD
		User user = new User();
		user.setFirstName("Adrian");
		user.setLastName("Jurado");
		user.setEmail("test13@gmail.com");
		user.setPassword("test1234");
		user.setUsername("adri4");
                user.setBaseSalary(800);
                user.setSalaryRecived(user.getBaseSalary());
		user.setRole(User.Role.EMPLOYEE);
		UserDao userdao = new UserDao();
		userdao.saveUser(user);

		System.out.println("User id= " + user.getId());
                
                 LocalTime horaEntradaEmpleado = LocalTime.of(8, 15); // Entró a las 8:15 AM
                LocalTime horaSalidaEmpleado = LocalTime.of(16, 45); // Salió a las 4:45 PM
                Long idEmpleado = user.getId(); // Suponiendo que ya tienes un empleado con ID 1 en la base de datos
                 FinesCalculator finesCalculator = new FinesCalculator();

                // Procesar multa de entrada
                finesCalculator.procesarMultaEntrada(idEmpleado, horaEntradaEmpleado);

                // Procesar multa de salida
                finesCalculator.procesarMultaSalida(idEmpleado, horaSalidaEmpleado);
=======
		// User user = new User("Julio", "Julio","Julio", "Julio", User.Role.ADMIN, "Julio", "Julio");
//		user.setFirstName("Jose");
//		user.setLastName("Camino");
//		user.setEmail("test8@gmail.com");
//		user.setPassword("test1234");
//		user.setUsername("scaminom8");
//		user.setRole(User.Role.ADMIN);
//		UserDao userdao = new UserDao();
//		userdao.saveUser(user);
		//User user1 = userdao.getUser(1l);

		// LocalDateTime entryTime = LocalDateTime.now();
		// LocalDateTime exitTime = LocalDateTime.now();
		// Register register = new Register();
		// register.setEntryTime(entryTime);
		// register.setExitTime(exitTime);
		// register.setUser(user1);
		// RegisterDao rd = new RegisterDao();
		// rd.saveRegister(register);
		// System.out.println("RegisterID= " + register.getId());
//		System.out.println("User id= " + user.getId());
		var userDao = new UserDao();
		User user = userDao.getUser(4L);
		var registrations = user.getRegistrations();

		for (var registration : registrations) {
			System.out.println(registration);
		}
>>>>>>> a763b064c0f233d23b8f9b88652292d51f632d45
	}
}
