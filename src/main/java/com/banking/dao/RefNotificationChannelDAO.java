package com.banking.dao;

import com.banking.entity.RefNotificationChannel;
import com.banking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RefNotificationChannelDAO {

    // CREATE
    public Long saveChannel(RefNotificationChannel channel) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Long id = (Long) session.save(channel);
            
            transaction.commit();
            System.out.println("✓ Notification Channel saved with ID: " + id);
            return id;
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving channel: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // READ
    public RefNotificationChannel getChannelById(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(RefNotificationChannel.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // READ - Get all
    public List<RefNotificationChannel> getAllChannels() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<RefNotificationChannel> query = session.createQuery(
                "FROM RefNotificationChannel ORDER BY channelName", RefNotificationChannel.class);
            return query.list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // UPDATE
    public void updateChannel(RefNotificationChannel channel) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(channel);
            
            transaction.commit();
            System.out.println("✓ Notification Channel updated successfully");
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating channel: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // DELETE
    public void deleteChannel(Long id) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            RefNotificationChannel channel = session.get(RefNotificationChannel.class, id);
            if (channel != null) {
                session.delete(channel);
                System.out.println("✓ Notification Channel deleted successfully");
            }
            
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting channel: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
