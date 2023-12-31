package com.scrum.registrationsystem.service;

import java.util.List;

public abstract class Repository<T> {

	public abstract T findById(Long id);

	public abstract List<T> findAll();

	public abstract List<Object[]> findFormattedAll();

	public abstract boolean create(T entity);

	public abstract boolean update(T entity);

	public abstract boolean delete(Long id);

	public abstract T returnDataToTextFields(int idRow);
}
