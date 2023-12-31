package com.scrum.registrationsystem.dao;

import com.scrum.registrationsystem.entities.User;
import com.scrum.registrationsystem.service.Repository;
import com.scrum.registrationsystem.util.HibernateUtil;
import jakarta.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class UserDao extends Repository<User> {

	private static final Logger logger = (Logger) LoggerFactory.getLogger(UserDao.class);

	public UserDao() {
		super();
	}

	@Override
	public User findById(Long id) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(User.class, id);
		} catch (Exception e) {
			logger.error("Hibernate error: ", e);
			JOptionPane.showMessageDialog(null,
					"Hubo un error en la transacción, por favor intentelo de nuevo.",
					"Error en la base de datos",
					JOptionPane.ERROR_MESSAGE);
		}
		return null;
	}

	@Override
	public List<User> findAll() {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			CriteriaQuery<User> criteriaQuery = session.getCriteriaBuilder().createQuery(User.class);
			criteriaQuery.from(User.class);
			return session.createQuery(criteriaQuery).getResultList();
		} catch (Exception e) {
			logger.error("Hibernate error: ", e);
		}
		return null;
	}

	@Override
	public boolean create(User user) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.save(user);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				try {
					transaction.rollback();
				} catch (RuntimeException re) {
					logger.error("Rollback error: ", re);
				}
			}
			logger.error("Hibernate error: ", e);
			return false;
		}
	}

	@Override
	public boolean update(User user) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			session.update(user);
			transaction.commit();
			return true;
		} catch (Exception e) {
			if (transaction != null) {
				try {
					transaction.rollback();
				} catch (RuntimeException re) {
					logger.error("Rollback error: ", re);
				}
			}
			logger.error("Hibernate error: ", e);
			return false;
		}
	}

	@Override
	public boolean delete(Long id) {
		Transaction transaction = null;
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			transaction = session.beginTransaction();
			User user = session.get(User.class, id);
			if (user != null) {
				session.delete(user);
				transaction.commit();
				return true;
			} else {
				logger.info("User not found with ID: " + id);
				return false;
			}
		} catch (Exception e) {
			if (transaction != null) {
				try {
					transaction.rollback();
				} catch (RuntimeException re) {
					logger.error("Rollback error: ", re);
				}
			}
			logger.error("Hibernate error: ", e);
			return false;
		}
	}

	@Override
	public User returnDataToTextFields(int idRow) {
		try (Session session = HibernateUtil.getSessionFactory().openSession()) {
			return session.get(User.class, (long) idRow);
		} catch (Exception e) {
			logger.error("Hibernate error: ", e);
		}
		return null;
	}

	@Override
	public List<Object[]> findFormattedAll() {
		return new ArrayList<>();
	}
}
