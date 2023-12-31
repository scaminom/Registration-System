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
	}
}
