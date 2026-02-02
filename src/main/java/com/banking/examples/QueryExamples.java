package com.banking.examples;

import com.banking.entity.*;
import com.banking.util.HibernateUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Advanced Query Examples
 * Demonstrates different querying techniques in Hibernate
 */
public class QueryExamples {

    public static void main(String[] args) {
        System.out.println("=== Hibernate Query Examples ===\n");
        
        hqlExamples();
        namedParameterExamples();
        joinQueryExamples();
        aggregateQueryExamples();
        
        HibernateUtil.shutdown();
        System.out.println("\n=== All query examples completed ===");
    }

    /**
     * HQL (Hibernate Query Language) Examples
     */
    public static void hqlExamples() {
        System.out.println("\n--- HQL Query Examples ---");
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            // 1. Simple HQL - Select all
            System.out.println("\n1. Select all Customers:");
            Query<Customer> query1 = session.createQuery("FROM Customer", Customer.class);
            List<Customer> customers = query1.list();
            System.out.println("   Found " + customers.size() + " customer(s)");
            
            // 2. HQL with WHERE clause
            System.out.println("\n2. Customers with specific email domain:");
            Query<Customer> query2 = session.createQuery(
                "FROM Customer c WHERE c.email LIKE '%@example.com'", Customer.class);
            List<Customer> filteredCustomers = query2.list();
            System.out.println("   Found " + filteredCustomers.size() + " customer(s)");
            
            // 3. HQL with ORDER BY
            System.out.println("\n3. Accounts ordered by balance (descending):");
            Query<Account> query3 = session.createQuery(
                "FROM Account ORDER BY balance DESC", Account.class);
            query3.setMaxResults(5);
            List<Account> topAccounts = query3.list();
            for (Account acc : topAccounts) {
                System.out.println("   - " + acc.getAccountNumber() + ": " + acc.getBalance());
            }
            
            // 4. HQL with pagination
            System.out.println("\n4. Paginated results (page 1, size 3):");
            Query<BankTransaction> query4 = session.createQuery(
                "FROM BankTransaction ORDER BY txDate DESC", BankTransaction.class);
            query4.setFirstResult(0);  // Start from record 0
            query4.setMaxResults(3);   // Fetch 3 records
            List<BankTransaction> page1 = query4.list();
            System.out.println("   Found " + page1.size() + " transaction(s) on page 1");
            
            System.out.println("\n✓ HQL examples completed!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Named Parameter Examples
     */
    public static void namedParameterExamples() {
        System.out.println("\n--- Named Parameter Examples ---");
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            // 1. Single named parameter
            System.out.println("\n1. Find transaction by type:");
            Query<BankTransaction> query1 = session.createQuery(
                "FROM BankTransaction WHERE type = :txType", BankTransaction.class);
            query1.setParameter("txType", "CREDIT");
            List<BankTransaction> credits = query1.list();
            System.out.println("   Found " + credits.size() + " CREDIT transaction(s)");
            
            // 2. Multiple named parameters
            System.out.println("\n2. Find transactions by type and status:");
            Query<BankTransaction> query2 = session.createQuery(
                "FROM BankTransaction WHERE type = :type AND status = :status", 
                BankTransaction.class);
            query2.setParameter("type", "DEBIT");
            query2.setParameter("status", "COMPLETED");
            List<BankTransaction> result = query2.list();
            System.out.println("   Found " + result.size() + " DEBIT-COMPLETED transaction(s)");
            
            // 3. Range query with parameters
            System.out.println("\n3. Find accounts with balance in range:");
            Query<Account> query3 = session.createQuery(
                "FROM Account WHERE balance BETWEEN :minBalance AND :maxBalance", 
                Account.class);
            query3.setParameter("minBalance", new BigDecimal("10000.00"));
            query3.setParameter("maxBalance", new BigDecimal("50000.00"));
            List<Account> rangeAccounts = query3.list();
            System.out.println("   Found " + rangeAccounts.size() + " account(s) in range");
            
            System.out.println("\n✓ Named parameter examples completed!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * JOIN Query Examples
     */
    public static void joinQueryExamples() {
        System.out.println("\n--- JOIN Query Examples ---");
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            // 1. Inner Join
            System.out.println("\n1. Inner Join - Accounts with Customers:");
            Query<Object[]> query1 = session.createQuery(
                "SELECT a.accountNumber, a.balance, c.fullName " +
                "FROM Account a INNER JOIN a.customer c", Object[].class);
            List<Object[]> results1 = query1.list();
            System.out.println("   Found " + results1.size() + " account(s) with customer info");
            for (Object[] row : results1) {
                System.out.println("   - Account: " + row[0] + " | Balance: " + row[1] + 
                                 " | Owner: " + row[2]);
            }
            
            // 2. Left Join with FETCH
            System.out.println("\n2. Left Join Fetch - Customers with KYC:");
            Query<Customer> query2 = session.createQuery(
                "SELECT c FROM Customer c LEFT JOIN FETCH c.kycProfile", Customer.class);
            List<Customer> customers = query2.list();
            System.out.println("   Found " + customers.size() + " customer(s)");
            for (Customer c : customers) {
                String kycInfo = (c.getKycProfile() != null) ? 
                    c.getKycProfile().getPanNumber() : "No KYC";
                System.out.println("   - " + c.getFullName() + " | KYC: " + kycInfo);
            }
            
            // 3. Multiple Joins
            System.out.println("\n3. Multiple Joins - Transaction with Account and Customer:");
            Query<Object[]> query3 = session.createQuery(
                "SELECT t.type, t.amount, a.accountNumber, c.fullName " +
                "FROM BankTransaction t " +
                "INNER JOIN t.account a " +
                "INNER JOIN a.customer c", Object[].class);
            query3.setMaxResults(5);
            List<Object[]> results3 = query3.list();
            System.out.println("   Found " + results3.size() + " transaction(s) with full details");
            for (Object[] row : results3) {
                System.out.println("   - " + row[0] + " " + row[1] + " | Account: " + 
                                 row[2] + " | Customer: " + row[3]);
            }
            
            System.out.println("\n✓ JOIN query examples completed!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Aggregate Query Examples
     */
    public static void aggregateQueryExamples() {
        System.out.println("\n--- Aggregate Query Examples ---");
        Session session = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            
            // 1. COUNT
            System.out.println("\n1. Count total customers:");
            Query<Long> query1 = session.createQuery(
                "SELECT COUNT(c) FROM Customer c", Long.class);
            Long customerCount = query1.uniqueResult();
            System.out.println("   Total customers: " + customerCount);
            
            // 2. SUM
            System.out.println("\n2. Sum of all account balances:");
            Query<BigDecimal> query2 = session.createQuery(
                "SELECT SUM(a.balance) FROM Account a", BigDecimal.class);
            BigDecimal totalBalance = query2.uniqueResult();
            System.out.println("   Total balance: " + (totalBalance != null ? totalBalance : "0.00"));
            
            // 3. AVG
            System.out.println("\n3. Average account balance:");
            Query<Double> query3 = session.createQuery(
                "SELECT AVG(a.balance) FROM Account a", Double.class);
            Double avgBalance = query3.uniqueResult();
            System.out.println("   Average balance: " + (avgBalance != null ? avgBalance : "0.00"));
            
            // 4. MIN and MAX
            System.out.println("\n4. Min and Max balances:");
            Query<Object[]> query4 = session.createQuery(
                "SELECT MIN(a.balance), MAX(a.balance) FROM Account a", Object[].class);
            Object[] minMax = query4.uniqueResult();
            System.out.println("   Min balance: " + minMax[0]);
            System.out.println("   Max balance: " + minMax[1]);
            
            // 5. GROUP BY
            System.out.println("\n5. Count transactions by type:");
            Query<Object[]> query5 = session.createQuery(
                "SELECT t.type, COUNT(t) FROM BankTransaction t GROUP BY t.type", 
                Object[].class);
            List<Object[]> groupResults = query5.list();
            for (Object[] row : groupResults) {
                System.out.println("   - " + row[0] + ": " + row[1] + " transaction(s)");
            }
            
            // 6. GROUP BY with HAVING
            System.out.println("\n6. Customers with multiple accounts:");
            Query<Object[]> query6 = session.createQuery(
                "SELECT c.fullName, COUNT(a) FROM Customer c " +
                "LEFT JOIN c.accounts a GROUP BY c.fullName HAVING COUNT(a) > 1", 
                Object[].class);
            List<Object[]> havingResults = query6.list();
            if (havingResults.isEmpty()) {
                System.out.println("   No customers with multiple accounts found");
            } else {
                for (Object[] row : havingResults) {
                    System.out.println("   - " + row[0] + ": " + row[1] + " account(s)");
                }
            }
            
            System.out.println("\n✓ Aggregate query examples completed!");
            
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
