package com.banking.dao;

import com.banking.entity.KycProfile;
import com.banking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class KycProfileDAO {

    // CREATE
    public Long saveKycProfile(KycProfile kycProfile) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Long id = (Long) session.save(kycProfile);
            
            transaction.commit();
            System.out.println("✓ KYC Profile saved with ID: " + id);
            return id;
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving KYC Profile: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // READ
    public KycProfile getKycProfileById(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(KycProfile.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // UPDATE
    public void updateKycProfile(KycProfile kycProfile) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(kycProfile);
            
            transaction.commit();
            System.out.println("✓ KYC Profile updated successfully");
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating KYC Profile: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // DELETE
    public void deleteKycProfile(Long id) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            KycProfile kycProfile = session.get(KycProfile.class, id);
            if (kycProfile != null) {
                session.delete(kycProfile);
                System.out.println("✓ KYC Profile deleted successfully");
            }
            
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting KYC Profile: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }
}
