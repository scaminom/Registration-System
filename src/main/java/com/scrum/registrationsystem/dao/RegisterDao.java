package com.scrum.registrationsystem.dao;

import com.scrum.registrationsystem.entities.Register;
import com.scrum.registrationsystem.util.HibernateUtil;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import org.hibernate.query.Query;

public class RegisterDao {

    public void saveRegister(Register register) throws HibernateException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.save(register);
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

    public List<Register> getRegisters() throws HibernateException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Register").list();
        } catch (HibernateException e) {
            throw e;
        }
    }

    public Register getRegister(Long id) throws HibernateException {
        if (id == null) {
            throw new IllegalArgumentException("Register ID must not be null.");
        }
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.get(Register.class, id);
        } catch (HibernateException e) {
            throw e;
        }
    }

    public void updateRegister(Register register) throws HibernateException {
        if (register == null || register.getId() == null) {
            throw new IllegalArgumentException("Register and its ID must not be null for update.");
        }

        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            session.update(register);
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

    public void deleteRegister(Long id) throws HibernateException {
        if (id == null) {
            throw new IllegalArgumentException("Register ID must not be null for deletion.");
        }
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            Register register = session.get(Register.class, id);
            if (register != null) {
                session.delete(register);
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
    
    public Register getLastRegisterForUser(Long userId) throws HibernateException {
    if (userId == null) {
        throw new IllegalArgumentException("User ID must not be null.");
    }
    try (Session session = HibernateUtil.getSessionFactory().getCurrentSession()) {
			session.beginTransaction();
			Query<Long> query = session.createQuery(
					"SELECT r.id FROM Register r WHERE r.user.id = :userId ORDER BY r.id DESC",
					Long.class
			);
			query.setParameter("userId", userId);
			query.setMaxResults(1);

			Long ultimoIdRegistro = query.uniqueResult();

			Register register = this.getRegister(ultimoIdRegistro);
        return register;
    } catch (HibernateException e) {
        throw e;
    }
}
}
