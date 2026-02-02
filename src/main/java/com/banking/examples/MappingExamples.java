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
 * Relationship Mapping Examples
 * Demonstrates different types of mappings in Hibernate
 */
public class MappingExamples {

    public static void main(String[] args) {
        System.out.println("=== Hibernate Relationship Mapping Examples ===\n");
        
        oneToOneExample();
        oneToManyExample();
        manyToOneExample();
        manyToManyExample();
        
        HibernateUtil.shutdown();
        System.out.println("\n=== All mapping examples completed ===");
    }

    /**
     * One-to-One Mapping Example
     * Customer <-> KycProfile (each customer has one KYC profile)
     */
    public static void oneToOneExample() {
        System.out.println("\n--- One-to-One Mapping Example ---");
        System.out.println("Customer <-> KycProfile\n");
        
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // Step 1: Create KYC Profile
            KycProfile kycProfile = new KycProfile();
            kycProfile.setPanNumber("XYZAB5678C");
            kycProfile.setRiskStatus("MEDIUM");
            session.save(kycProfile);
            System.out.println("1. Created KYC Profile: " + kycProfile.getPanNumber());
            
            // Step 2: Create Customer and associate with KYC Profile
            Customer customer = new Customer();
            customer.setFullName("Alice Smith");
            customer.setEmail("alice.smith@example.com");
            customer.setKycProfile(kycProfile);  // ONE-TO-ONE mapping
            session.save(customer);
            System.out.println("2. Created Customer: " + customer.getFullName());
            System.out.println("3. Associated Customer with KYC Profile");
            
            transaction.commit();
            
            // Step 3: Retrieve and verify the relationship
            session = HibernateUtil.getSessionFactory().openSession();
            Customer retrievedCustomer = session.get(Customer.class, customer.getId());
            System.out.println("\n4. Retrieved Customer: " + retrievedCustomer.getFullName());
            System.out.println("5. Customer's KYC Profile: " + retrievedCustomer.getKycProfile().getPanNumber());
            System.out.println("   Risk Status: " + retrievedCustomer.getKycProfile().getRiskStatus());
            
            System.out.println("\n✓ One-to-One mapping demonstrated successfully!");
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * One-to-Many Mapping Example
     * Customer -> Multiple Accounts (one customer can have many accounts)
     */
    public static void oneToManyExample() {
        System.out.println("\n--- One-to-Many Mapping Example ---");
        System.out.println("Customer -> Multiple Accounts\n");
        
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // Step 1: Create Customer
            Customer customer = new Customer();
            customer.setFullName("Bob Johnson");
            customer.setEmail("bob.johnson@example.com");
            session.save(customer);
            System.out.println("1. Created Customer: " + customer.getFullName());
            
            // Step 2: Create multiple Accounts for the same Customer
            Account savingsAccount = new Account();
            savingsAccount.setAccountNumber("SAV" + System.currentTimeMillis());
            savingsAccount.setBalance(new BigDecimal("25000.00"));
            savingsAccount.setCustomer(customer);  // MANY-TO-ONE from Account side
            session.save(savingsAccount);
            System.out.println("2. Created Savings Account: " + savingsAccount.getAccountNumber());
            
            Account currentAccount = new Account();
            currentAccount.setAccountNumber("CUR" + System.currentTimeMillis());
            currentAccount.setBalance(new BigDecimal("15000.00"));
            currentAccount.setCustomer(customer);  // MANY-TO-ONE from Account side
            session.save(currentAccount);
            System.out.println("3. Created Current Account: " + currentAccount.getAccountNumber());
            
            Account creditAccount = new Account();
            creditAccount.setAccountNumber("CRD" + System.currentTimeMillis());
            creditAccount.setBalance(new BigDecimal("5000.00"));
            creditAccount.setCustomer(customer);  // MANY-TO-ONE from Account side
            session.save(creditAccount);
            System.out.println("4. Created Credit Account: " + creditAccount.getAccountNumber());
            
            transaction.commit();
            
            // Step 3: Retrieve Customer and all their Accounts
            session = HibernateUtil.getSessionFactory().openSession();
            Query<Account> accountQuery = session.createQuery(
                "FROM Account WHERE customer.id = :customerId", Account.class);
            accountQuery.setParameter("customerId", customer.getId());
            List<Account> accounts = accountQuery.list();
            
            System.out.println("\n5. Retrieved Customer's Accounts:");
            System.out.println("   Customer: " + customer.getFullName());
            System.out.println("   Total Accounts: " + accounts.size());
            for (Account acc : accounts) {
                System.out.println("   - " + acc.getAccountNumber() + " | Balance: " + acc.getBalance());
            }
            
            System.out.println("\n✓ One-to-Many mapping demonstrated successfully!");
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Many-to-One Mapping Example
     * Multiple Transactions -> One Account (many transactions belong to one account)
     */
    public static void manyToOneExample() {
        System.out.println("\n--- Many-to-One Mapping Example ---");
        System.out.println("Multiple Transactions -> One Account\n");
        
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // Step 1: Get or create an Account
            Account account = new Account();
            account.setAccountNumber("TXN" + System.currentTimeMillis());
            account.setBalance(new BigDecimal("50000.00"));
            session.save(account);
            System.out.println("1. Created Account: " + account.getAccountNumber());
            
            // Step 2: Create multiple Transactions for the same Account
            BankTransaction tx1 = new BankTransaction();
            tx1.setTxDate(new Date());
            tx1.setType("CREDIT");
            tx1.setAmount(new BigDecimal("10000.00"));
            tx1.setStatus("COMPLETED");
            tx1.setAccount(account);  // MANY-TO-ONE mapping
            session.save(tx1);
            System.out.println("2. Created Transaction 1: CREDIT " + tx1.getAmount());
            
            BankTransaction tx2 = new BankTransaction();
            tx2.setTxDate(new Date());
            tx2.setType("DEBIT");
            tx2.setAmount(new BigDecimal("3000.00"));
            tx2.setStatus("COMPLETED");
            tx2.setAccount(account);  // MANY-TO-ONE mapping
            session.save(tx2);
            System.out.println("3. Created Transaction 2: DEBIT " + tx2.getAmount());
            
            BankTransaction tx3 = new BankTransaction();
            tx3.setTxDate(new Date());
            tx3.setType("DEBIT");
            tx3.setAmount(new BigDecimal("2000.00"));
            tx3.setStatus("PENDING");
            tx3.setAccount(account);  // MANY-TO-ONE mapping
            session.save(tx3);
            System.out.println("4. Created Transaction 3: DEBIT " + tx3.getAmount());
            
            transaction.commit();
            
            // Step 3: Retrieve Account and all its Transactions
            session = HibernateUtil.getSessionFactory().openSession();
            Query<BankTransaction> txQuery = session.createQuery(
                "FROM BankTransaction WHERE account.id = :accountId ORDER BY txDate DESC", 
                BankTransaction.class);
            txQuery.setParameter("accountId", account.getId());
            List<BankTransaction> transactions = txQuery.list();
            
            System.out.println("\n5. Retrieved Account's Transactions:");
            System.out.println("   Account: " + account.getAccountNumber());
            System.out.println("   Total Transactions: " + transactions.size());
            for (BankTransaction tx : transactions) {
                System.out.println("   - " + tx.getType() + " | Amount: " + tx.getAmount() + 
                                 " | Status: " + tx.getStatus());
            }
            
            System.out.println("\n✓ Many-to-One mapping demonstrated successfully!");
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }

    /**
     * Many-to-Many Mapping Example
     * Accounts <-> Notification Channels (many accounts can subscribe to many channels)
     */
    public static void manyToManyExample() {
        System.out.println("\n--- Many-to-Many Mapping Example ---");
        System.out.println("Accounts <-> Notification Channels\n");
        
        Session session = null;
        Transaction transaction = null;
        
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            transaction = session.beginTransaction();
            
            // Step 1: Create Notification Channels
            RefNotificationChannel smsChannel = new RefNotificationChannel();
            smsChannel.setChannelName("SMS_BUSINESS");
            smsChannel.setMonthlyCost(new BigDecimal("10.00"));
            session.save(smsChannel);
            System.out.println("1. Created Channel: " + smsChannel.getChannelName());
            
            RefNotificationChannel emailChannel = new RefNotificationChannel();
            emailChannel.setChannelName("EMAIL_BUSINESS");
            emailChannel.setMonthlyCost(new BigDecimal("0.00"));
            session.save(emailChannel);
            System.out.println("2. Created Channel: " + emailChannel.getChannelName());
            
            RefNotificationChannel pushChannel = new RefNotificationChannel();
            pushChannel.setChannelName("PUSH_NOTIFICATION");
            pushChannel.setMonthlyCost(new BigDecimal("3.00"));
            session.save(pushChannel);
            System.out.println("3. Created Channel: " + pushChannel.getChannelName());
            
            // Step 2: Create Accounts
            Account account1 = new Account();
            account1.setAccountNumber("MTM1_" + System.currentTimeMillis());
            account1.setBalance(new BigDecimal("100000.00"));
            session.save(account1);
            System.out.println("\n4. Created Account 1: " + account1.getAccountNumber());
            
            Account account2 = new Account();
            account2.setAccountNumber("MTM2_" + System.currentTimeMillis());
            account2.setBalance(new BigDecimal("75000.00"));
            session.save(account2);
            System.out.println("5. Created Account 2: " + account2.getAccountNumber());
            
            // Step 3: Subscribe Accounts to Channels (MANY-TO-MANY)
            // Account 1 subscribes to SMS and Email
            account1.getSubscriptions().add(smsChannel);
            account1.getSubscriptions().add(emailChannel);
            session.update(account1);
            System.out.println("\n6. Account 1 subscribed to: SMS, Email");
            
            // Account 2 subscribes to all three channels
            account2.getSubscriptions().add(smsChannel);
            account2.getSubscriptions().add(emailChannel);
            account2.getSubscriptions().add(pushChannel);
            session.update(account2);
            System.out.println("7. Account 2 subscribed to: SMS, Email, Push");
            
            transaction.commit();
            
            // Step 4: Retrieve and display the many-to-many relationships
            session = HibernateUtil.getSessionFactory().openSession();
            
            Account retrievedAcc1 = session.get(Account.class, account1.getId());
            System.out.println("\n8. Account 1 (" + retrievedAcc1.getAccountNumber() + ") subscriptions:");
            for (RefNotificationChannel channel : retrievedAcc1.getSubscriptions()) {
                System.out.println("   - " + channel.getChannelName() + " (Cost: " + channel.getMonthlyCost() + ")");
            }
            
            Account retrievedAcc2 = session.get(Account.class, account2.getId());
            System.out.println("\n9. Account 2 (" + retrievedAcc2.getAccountNumber() + ") subscriptions:");
            for (RefNotificationChannel channel : retrievedAcc2.getSubscriptions()) {
                System.out.println("   - " + channel.getChannelName() + " (Cost: " + channel.getMonthlyCost() + ")");
            }
            
            // Show which accounts are subscribed to SMS channel
            Query<Account> accountsWithSms = session.createQuery(
                "SELECT a FROM Account a JOIN a.subscriptions s WHERE s.channelName = :channelName", 
                Account.class);
            accountsWithSms.setParameter("channelName", "SMS_BUSINESS");
            List<Account> smsAccounts = accountsWithSms.list();
            System.out.println("\n10. Accounts subscribed to SMS_BUSINESS:");
            for (Account acc : smsAccounts) {
                System.out.println("   - " + acc.getAccountNumber());
            }
            
            System.out.println("\n✓ Many-to-Many mapping demonstrated successfully!");
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (session != null) session.close();
        }
    }
}
