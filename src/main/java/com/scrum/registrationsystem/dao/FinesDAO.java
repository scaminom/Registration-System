package com.scrum.registrationsystem.dao;

import com.scrum.registrationsystem.entities.Fines;
import com.scrum.registrationsystem.util.HibernateUtil;
import java.util.List;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class FinesDAO {

    // Método para obtener todas las multas
    public List<Fines> getMultas() throws HibernateException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Fines", Fines.class).list();
        } catch (HibernateException e) {
            throw e;
        }
    }

    // Método para guardar una multa
   public void saveMulta(Fines multa) throws HibernateException {
    Session session = null;
    Transaction transaction = null;
    try {
        session = HibernateUtil.getSessionFactory().openSession();
        transaction = session.beginTransaction();
        session.save(multa);
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

    // Método para actualizar una multa
    public void updateMulta(Fines multa) throws HibernateException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.update(multa);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    // Método para eliminar una multa
    public void deleteMulta(Fines multa) throws HibernateException {
        Transaction transaction = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.delete(multa);
            transaction.commit();
        } catch (HibernateException e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Fines findTodayLastMultaByUser(Long id) throws HibernateException {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from Fines where user_id = :id order by id desc", Fines.class)
                    .setParameter("id", id)
                    .setMaxResults(1)
                    .uniqueResult();
        } catch (HibernateException e) {
            throw e;
        }
    }
}