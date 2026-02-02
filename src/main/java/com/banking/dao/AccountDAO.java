package com.banking.dao;

import com.banking.entity.Account;
import com.banking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class AccountDAO {

    // CREATE
    public Long saveAccount(Account account) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Long id = (Long) session.save(account);
            
            transaction.commit();
            System.out.println("✓ Account saved with ID: " + id);
            return id;
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving account: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // READ
    public Account getAccountById(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Account.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // READ - Get accounts by customer
    public List<Account> getAccountsByCustomerId(Long customerId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Account> query = session.createQuery(
                "FROM Account WHERE customer.id = :customerId", Account.class);
            query.setParameter("customerId", customerId);
            return query.list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // UPDATE
    public void updateAccount(Account account) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(account);
            
            transaction.commit();
            System.out.println("✓ Account updated successfully");
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating account: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // DELETE
    public void deleteAccount(Long id) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Account account = session.get(Account.class, id);
            if (account != null) {
                session.delete(account);
                System.out.println("✓ Account deleted successfully");
            }
            
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting account: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
