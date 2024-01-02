package com.scrum.registrationsystem.entities;

import java.sql.Date;

import com.scrum.registrationsystem.service.TimeService;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Fines")
public class Fines {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_user", nullable = false)
	private User user;

	@Column(name = "date", nullable = false)
	private Date date;

	@Column(name = "description", nullable = false)
	private String description;

	@Column(name = "cost_fine", nullable = false)
	private double costFine;

	public Fines() {
	}

	public Fines(User user, String description, double constFine, Date date) {
		this.user = user;
		this.description = description;
		this.costFine = constFine;
		this.date = date;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getDescripcion() {
		return description;
	}

	public void setDescripcion(String description) {
		this.description = description;
	}

	public double getValorMulta() {
		return costFine;
	}

	public void setValorMulta(double costFine) {
		this.costFine = costFine;
	}

}
