/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.scrum.registrationsystem;

import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.service.FinesCalculator;
import java.time.LocalDateTime;
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
		user.setEmail("test23@gmail.com");
		user.setPassword("test1234");
		user.setUsername("adri14");
                user.setBaseSalary(800);
                user.setSalaryRecived(user.getBaseSalary());
		user.setRole(User.Role.EMPLOYEE);
		UserDao userdao = new UserDao();
		userdao.saveUser(user);
              

		System.out.println("User id= " + user.getId());
                LocalDateTime horaEntradaEmpleado;  
                LocalDateTime horaEntrada = LocalDateTime.of(2023, 12, 30, 8, 0);
                 LocalDateTime horaSalida = LocalDateTime.of(2023, 12, 30, 13, 0);
                Long idEmpleado = user.getId(); // Suponiendo que ya tienes un empleado con ID 1 en la base de datos
                 FinesCalculator finesCalculator = new FinesCalculator();

               finesCalculator.procesarMultaEntrada(user.getId(), horaEntrada);
                finesCalculator.procesarMultaSalida(user.getId(), horaSalida);
		System.out.println("Usuario: " + user.getUsername());
        System.out.println("Salario recibido después de multas: " + user.getSalaryRecived());
	}
}
