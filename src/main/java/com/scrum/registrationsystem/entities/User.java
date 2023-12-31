package com.scrum.registrationsystem.entities;

import jakarta.persistence.Basic;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "username", nullable = false, unique = true)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "gender")
    private String gender;

    @Column(name = "base_salary")
    private double baseSalary;

    @Column(name = "salary_received")
    private double salaryRecived;

    @Lob
    @Basic
    @Column(name = "fingerprint_pattern")
    private byte[] fingerprintPattern;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Register> registrations;

	@OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<Fines> fines;

	public User() {
		registrations = new ArrayList<>();
		fines = new ArrayList<>();
	}

	public User(String firstName, String lastName, String username, String password, Role role, String email, String gender, double salaryRecived) {
		registrations = new ArrayList<>();
		fines = new ArrayList<>();
		this.firstName = firstName;
		this.lastName = lastName;
		this.username = username;
		this.password = password;
		this.role = role;
		this.email = email;
		this.gender = gender;
		this.baseSalary = 800.0;
		this.salaryRecived = salaryRecived;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public double getBaseSalary() {
        return baseSalary;
    }

    public void setBaseSalary(double baseSalary) {
        this.baseSalary = baseSalary;
    }

    public double getSalaryRecived() {
        return salaryRecived;
    }

    public void setSalaryRecived(double salaryRecived) {
        this.salaryRecived = salaryRecived;
    }

    public byte[] getFingerprintPattern() {
        return fingerprintPattern;
    }

    public void setFingerprintPattern(byte[] fingerprintPattern) {
        this.fingerprintPattern = fingerprintPattern;
    }

    public enum Role {
        ADMIN, EMPLOYEE
    }
    
    @Override
    public String toString() {
        return getFirstName() + " " + getLastName();
    }
	public List<Register> getRegistrations() {
		return registrations;
	}

	public void setRegistrations(List<Register> registrations) {
		this.registrations = registrations;
	}

	public List<Fines> getFines() {
		return fines;
	}

	public void setFines(List<Fines> fines) {
		this.fines = fines;
	}

	public void addFines(Fines fine) {
		fines.add(fine);
		fine.setUser(this);
	}

	public void addRegistration(Register registration) {
		registrations.add(registration);
		registration.setUser(this);
	}

}
