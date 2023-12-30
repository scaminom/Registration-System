package com.scrum.registrationsystem.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "Fines")
public class Fines implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "id_employee", nullable = false)
    private Long idEmployee;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "cost_fine", nullable = false)
    private double costFine;

    public Fines() {
    }

    public Fines(Long idEmployee, String description, double constFine) {
        this.idEmployee = idEmployee;
        this.description = description;
        this.costFine = constFine;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getIdEmpleado() {
        return idEmployee;
    }

    public void setIdEmpleado(Long idEmployee) {
        this.idEmployee = idEmployee;
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