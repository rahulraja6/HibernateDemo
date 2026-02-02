package com.banking.dao;

import com.banking.entity.BankTransaction;
import com.banking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class BankTransactionDAO {

    // CREATE
    public Long saveTransaction(BankTransaction transaction) {
        Session session = null;
        Transaction tx = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            
            Long id = (Long) session.save(transaction);
            
            tx.commit();
            System.out.println("✓ Bank Transaction saved with ID: " + id);
            return id;
            
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Error saving transaction: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // READ
    public BankTransaction getTransactionById(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(BankTransaction.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // READ - Get transactions by account
    public List<BankTransaction> getTransactionsByAccountId(Long accountId) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<BankTransaction> query = session.createQuery(
                "FROM BankTransaction WHERE account.id = :accountId ORDER BY txDate DESC", 
                BankTransaction.class);
            query.setParameter("accountId", accountId);
            return query.list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // UPDATE
    public void updateTransaction(BankTransaction transaction) {
        Session session = null;
        Transaction tx = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            
            session.update(transaction);
            
            tx.commit();
            System.out.println("✓ Bank Transaction updated successfully");
            
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Error updating transaction: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // DELETE
    public void deleteTransaction(Long id) {
        Session session = null;
        Transaction tx = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            
            BankTransaction transaction = session.get(BankTransaction.class, id);
            if (transaction != null) {
                session.delete(transaction);
                System.out.println("✓ Bank Transaction deleted successfully");
            }
            
            tx.commit();
            
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            System.err.println("Error deleting transaction: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
