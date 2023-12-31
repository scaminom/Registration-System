/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */
package com.scrum.registrationsystem;

import com.scrum.registrationsystem.dao.UserDao;
import com.scrum.registrationsystem.entities.User;

/**
 *
 * @author joses
 */
public class RegistrationSystem {

	public static void main(String[] args) {
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
	}
}
