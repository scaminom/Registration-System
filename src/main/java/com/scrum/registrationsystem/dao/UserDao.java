package com.scrum.registrationsystem.dao;

import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class UserDao {

	public void saveUser(User user) {
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
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public List<User> getUsers() {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			return session.createQuery("from User").list();
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public User getUser(Long id) {
		Session session = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			return (User) session.get(User.class, id);
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void updateUser(User user) {
		Session session = null;
		Transaction transaction = null;
		try {
			session = HibernateUtil.getSessionFactory().openSession();
			transaction = session.beginTransaction();

			session.update(user);

			transaction.commit();
		} catch (HibernateException e) {
			if (transaction != null) {
				transaction.rollback();
			}
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}

	public void deleteUser(Long id) {
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
		} finally {
			if (session != null) {
				session.close();
			}
		}
	}
}
