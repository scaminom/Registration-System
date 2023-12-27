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
		User user = new User();
		user.setFirstName("Jose");
		user.setLastName("Camino");
		user.setEmail("test@gmail.com");
		user.setPassword("test1234");
		user.setUsername("scaminom");
		user.setRole(User.Role.ADMIN);
		UserDao userdao = new UserDao();
		userdao.saveUser(user);

		System.out.println("User id= " + user.getId());

	}
}
