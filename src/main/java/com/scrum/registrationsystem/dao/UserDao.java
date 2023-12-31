package com.scrum.registrationsystem.dao;

import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDao {

	public void saveUser(User user) throws HibernateException {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.save(user);
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<User> getUsers() throws HibernateException {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.createQuery("from User").list();
		} catch (HibernateException e) {
			throw e;
		}
	}

	public User getUser(Long id) throws HibernateException {
		if (id == null) {
			throw new IllegalArgumentException("User ID must not be null.");
		}
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return (User) session.get(User.class, id);
		} catch (HibernateException e) {
			throw e;
		}
	}

	public void updateUser(User user) throws HibernateException {
		if (user == null || user.getId() == null) {
			throw new IllegalArgumentException("User and its ID must not be null for update.");
		}

		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			session.update(transaction);;
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void updateUserFingerprint(User user, byte[] fingerprintData) throws HibernateException {
		if (user == null || user.getId() == null) {
			throw new IllegalArgumentException("User and its ID must not be null for update.");
		}
	
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			user.setFingerprintPattern(fingerprintData);
			session.update(user);
	
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void deleteUser(Long id) throws HibernateException {
		if (id == null) {
			throw new IllegalArgumentException("User ID must not be null for deletion.");
		}
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();
			User user = (User) session.get(User.class, id);
			if (user != null) {
				session.delete(user);
			}
			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
			throw e;
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
