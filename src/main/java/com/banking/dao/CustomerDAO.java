package com.banking.dao;

import com.banking.entity.Customer;
import com.banking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class CustomerDAO {

    // CREATE
    public Long saveCustomer(Customer customer) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Long id = (Long) session.save(customer);
            
            transaction.commit();
            System.out.println("✓ Customer saved successfully with ID: " + id);
            return id;
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error saving customer: " + e.getMessage());
            e.printStackTrace();
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // READ - Get by ID
    public Customer getCustomerById(Long id) {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            return session.get(Customer.class, id);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // READ - Get all
    public List<Customer> getAllCustomers() {
        Session session = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Customer> query = session.createQuery("FROM Customer", Customer.class);
            return query.list();
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // UPDATE
    public void updateCustomer(Customer customer) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            session.update(customer);
            
            transaction.commit();
            System.out.println("✓ Customer updated successfully");
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error updating customer: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    // DELETE
    public void deleteCustomer(Long id) {
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            Customer customer = session.get(Customer.class, id);
            if (customer != null) {
                session.delete(customer);
                System.out.println("✓ Customer deleted successfully");
            } else {
                System.out.println("Customer not found with ID: " + id);
            }
            
            transaction.commit();
            
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.err.println("Error deleting customer: " + e.getMessage());
            throw e;
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

}
