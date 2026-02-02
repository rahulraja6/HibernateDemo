package com.banking;

import com.banking.dao.*;
import com.banking.entity.*;
import com.banking.util.HibernateUtil;

import org.hibernate.Session;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class App {

    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("   Hibernate CRUD & Mapping Examples   ");
        System.out.println("========================================\n");
        
        try {
            // CRUD Operations Example
            crudExample();
            
            System.out.println("\n========================================\n");
            
            // One-to-One Mapping Example
            oneToOneMappingExample();
            
            System.out.println("\n========================================\n");
            
            // One-to-Many Mapping Example
            oneToManyMappingExample();
            
            System.out.println("\n========================================\n");
            
            // Many-to-Many Mapping Example
            manyToManyMappingExample();
            
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Shutdown Hibernate
            HibernateUtil.shutdown();
            System.out.println("\n========================================");
            System.out.println("   All examples completed successfully! ");
            System.out.println("========================================");
        }
    }

    /**
     * CRUD Operations Example
     * Demonstrates Create, Read, Update, Delete operations
     */
    private static void crudExample() {
        System.out.println("--- CRUD Operations Example ---\n");
        
        CustomerDAO customerDAO = new CustomerDAO();
        
        // CREATE
        System.out.println("1. CREATE Operation:");
        Customer customer = new Customer();
        customer.setFullName("Alice Johnson");
        customer.setEmail("alice.johnson@example.com");
        Long customerId = customerDAO.saveCustomer(customer);
        
        // READ
        System.out.println("\n2. READ Operation:");
        Customer retrievedCustomer = customerDAO.getCustomerById(customerId);
        System.out.println("✓ Retrieved Customer: " + retrievedCustomer.getFullName() + 
                         " (" + retrievedCustomer.getEmail() + ")");
        
        // UPDATE
        System.out.println("\n3. UPDATE Operation:");
        retrievedCustomer.setEmail("alice.j.updated@example.com");
        customerDAO.updateCustomer(retrievedCustomer);
        
        // Verify update
        Customer updatedCustomer = customerDAO.getCustomerById(customerId);
        System.out.println("✓ Verified Email Updated: " + updatedCustomer.getEmail());
        
        // READ ALL
        System.out.println("\n4. READ ALL Operation:");
        List<Customer> allCustomers = customerDAO.getAllCustomers();
        System.out.println("✓ Total Customers in Database: " + allCustomers.size());
        for (Customer c : allCustomers) {
            System.out.println("  - " + c.getFullName() + " (" + c.getEmail() + ")");
        }
        
        // DELETE (commented out to preserve data)
        // System.out.println("\n5. DELETE Operation:");
        // customerDAO.deleteCustomer(customerId);
        // System.out.println("✓ Customer deleted (ID: " + customerId + ")");
    }

    /**
     * One-to-One Mapping Example
     * Customer <-> KycProfile (each customer has one KYC profile)
     */
    private static void oneToOneMappingExample() {
        System.out.println("--- One-to-One Mapping Example ---");
        System.out.println("Customer <-> KycProfile\n");
        
        CustomerDAO customerDAO = new CustomerDAO();
        KycProfileDAO kycDAO = new KycProfileDAO();
        
        // Step 1: Create KYC Profile
        System.out.println("1. Creating KYC Profile...");
        KycProfile kycProfile = new KycProfile();
        kycProfile.setPanNumber("ABCDE" + System.currentTimeMillis());
        kycProfile.setRiskStatus("LOW");
        Long kycId = kycDAO.saveKycProfile(kycProfile);
        
        // Step 2: Create Customer and link to KYC Profile (One-to-One)
        System.out.println("\n2. Creating Customer with KYC Profile...");
        Customer customer = new Customer();
        customer.setFullName("Bob Smith");
        customer.setEmail("bob.smith@example.com");
        customer.setKycProfile(kycProfile);  // ONE-TO-ONE relationship
        Long customerId = customerDAO.saveCustomer(customer);
        
        // Step 3: Retrieve and verify the relationship
        System.out.println("\n3. Verifying One-to-One Relationship:");
        Customer retrievedCustomer = customerDAO.getCustomerById(customerId);
        System.out.println("✓ Customer: " + retrievedCustomer.getFullName());
        if (retrievedCustomer.getKycProfile() != null) {
            System.out.println("✓ Associated KYC Profile: " + 
                             retrievedCustomer.getKycProfile().getPanNumber());
            System.out.println("✓ Risk Status: " + 
                             retrievedCustomer.getKycProfile().getRiskStatus());
            System.out.println("✓ One-to-One relationship verified!");
        }
    }

    /**
     * One-to-Many Mapping Example
     * Customer -> Multiple Accounts (one customer can have many accounts)
     * Account -> Multiple Transactions (one account can have many transactions)
     */
    private static void oneToManyMappingExample() {
        System.out.println("--- One-to-Many Mapping Example ---");
        System.out.println("Customer -> Multiple Accounts");
        System.out.println("Account -> Multiple Transactions\n");
        
        CustomerDAO customerDAO = new CustomerDAO();
        AccountDAO accountDAO = new AccountDAO();
        BankTransactionDAO transactionDAO = new BankTransactionDAO();
        
        // Step 1: Create Customer
        System.out.println("1. Creating Customer...");
        Customer customer = new Customer();
        customer.setFullName("Carol Davis");
        customer.setEmail("carol.davis@example.com");
        Long customerId = customerDAO.saveCustomer(customer);
        
        // Step 2: Create multiple Accounts for the Customer (One-to-Many)
        System.out.println("\n2. Creating Multiple Accounts for Customer...");
        
        Account savingsAccount = new Account();
        savingsAccount.setAccountNumber("SAV" + System.currentTimeMillis());
        savingsAccount.setBalance(new BigDecimal("50000.00"));
        savingsAccount.setCustomer(customer);  // Many-to-One from Account side
        Long savingsId = accountDAO.saveAccount(savingsAccount);
        
        Account currentAccount = new Account();
        currentAccount.setAccountNumber("CUR" + System.currentTimeMillis());
        currentAccount.setBalance(new BigDecimal("25000.00"));
        currentAccount.setCustomer(customer);  // Many-to-One from Account side
        Long currentId = accountDAO.saveAccount(currentAccount);
        
        // Step 3: Create multiple Transactions for an Account (One-to-Many)
        System.out.println("\n3. Creating Multiple Transactions for Savings Account...");
        
        BankTransaction deposit = new BankTransaction();
        deposit.setTxDate(new Date());
        deposit.setType("CREDIT");
        deposit.setAmount(new BigDecimal("10000.00"));
        deposit.setStatus("COMPLETED");
        deposit.setAccount(savingsAccount);  // Many-to-One from Transaction side
        transactionDAO.saveTransaction(deposit);
        
        BankTransaction withdrawal = new BankTransaction();
        withdrawal.setTxDate(new Date());
        withdrawal.setType("DEBIT");
        withdrawal.setAmount(new BigDecimal("5000.00"));
        withdrawal.setStatus("COMPLETED");
        withdrawal.setAccount(savingsAccount);  // Many-to-One from Transaction side
        transactionDAO.saveTransaction(withdrawal);
        
        // Step 4: Verify the relationships
        System.out.println("\n4. Verifying One-to-Many Relationships:");
        
        // Customer -> Accounts
        List<Account> customerAccounts = accountDAO.getAccountsByCustomerId(customerId);
        System.out.println("✓ Customer: " + customer.getFullName());
        System.out.println("✓ Number of Accounts: " + customerAccounts.size());
        for (Account acc : customerAccounts) {
            System.out.println("  - " + acc.getAccountNumber() + 
                             " | Balance: " + acc.getBalance());
        }
        
        // Account -> Transactions
        List<BankTransaction> accountTransactions = 
            transactionDAO.getTransactionsByAccountId(savingsId);
        System.out.println("\n✓ Account: " + savingsAccount.getAccountNumber());
        System.out.println("✓ Number of Transactions: " + accountTransactions.size());
        for (BankTransaction tx : accountTransactions) {
            System.out.println("  - " + tx.getType() + " | Amount: " + tx.getAmount() + 
                             " | Status: " + tx.getStatus());
        }
        
        System.out.println("\n✓ One-to-Many relationships verified!");
    }

    /**
     * Many-to-Many Mapping Example
     * Accounts <-> Notification Channels
     * (many accounts can subscribe to many channels)
     */
    private static void manyToManyMappingExample() {
        System.out.println("--- Many-to-Many Mapping Example ---");
        System.out.println("Accounts <-> Notification Channels\n");
        
        AccountDAO accountDAO = new AccountDAO();
        RefNotificationChannelDAO channelDAO = new RefNotificationChannelDAO();
        
        // Step 1: Get existing Notification Channels (or create if none exist)
        System.out.println("1. Getting Notification Channels...");
        List<RefNotificationChannel> existingChannels = channelDAO.getAllChannels();
        
        RefNotificationChannel smsChannel;
        RefNotificationChannel emailChannel;
        RefNotificationChannel pushChannel;
        
        if (existingChannels.size() >= 3) {
            // Use existing channels
            smsChannel = existingChannels.get(0);
            emailChannel = existingChannels.get(1);
            pushChannel = existingChannels.get(2);
            System.out.println("✓ Using existing channels:");
            System.out.println("  - " + smsChannel.getChannelName());
            System.out.println("  - " + emailChannel.getChannelName());
            System.out.println("  - " + pushChannel.getChannelName());
        } else {
            // Create new channels if not enough exist
            System.out.println("✓ Creating new channels...");
            
            smsChannel = new RefNotificationChannel();
            smsChannel.setChannelName("SMS_PREMIUM_" + System.currentTimeMillis());
            smsChannel.setMonthlyCost(new BigDecimal("15.00"));
            channelDAO.saveChannel(smsChannel);
            
            emailChannel = new RefNotificationChannel();
            emailChannel.setChannelName("EMAIL_PREMIUM_" + System.currentTimeMillis());
            emailChannel.setMonthlyCost(new BigDecimal("0.00"));
            channelDAO.saveChannel(emailChannel);
            
            pushChannel = new RefNotificationChannel();
            pushChannel.setChannelName("PUSH_NOTIFY_" + System.currentTimeMillis());
            pushChannel.setMonthlyCost(new BigDecimal("5.00"));
            channelDAO.saveChannel(pushChannel);
        }
        
        // Step 2: Create Accounts
        System.out.println("\n2. Creating Accounts...");
        
        Account account1 = new Account();
        account1.setAccountNumber("MTM1_" + System.currentTimeMillis());
        account1.setBalance(new BigDecimal("100000.00"));
        Long acc1Id = accountDAO.saveAccount(account1);
        
        Account account2 = new Account();
        account2.setAccountNumber("MTM2_" + System.currentTimeMillis());
        account2.setBalance(new BigDecimal("75000.00"));
        Long acc2Id = accountDAO.saveAccount(account2);
        
        // Step 3: Create Many-to-Many relationships
        System.out.println("\n3. Creating Many-to-Many Subscriptions...");
        
        // Add subscriptions within a single session to avoid lazy initialization exceptions
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            
            // Account 1 subscribes to SMS and Email
            Account acc1 = session.get(Account.class, acc1Id);
            acc1.getSubscriptions().add(smsChannel);
            acc1.getSubscriptions().add(emailChannel);
            session.update(acc1);
            System.out.println("✓ Account 1 subscribed to 2 channels");
            
            // Account 2 subscribes to all three channels
            Account acc2 = session.get(Account.class, acc2Id);
            acc2.getSubscriptions().add(smsChannel);
            acc2.getSubscriptions().add(emailChannel);
            acc2.getSubscriptions().add(pushChannel);
            session.update(acc2);
            System.out.println("✓ Account 2 subscribed to 3 channels");
            
            transaction.commit();
            
            // Step 4: Verify Many-to-Many relationships (within the same session)
            System.out.println("\n4. Verifying Many-to-Many Relationships:");
            
            acc1 = session.get(Account.class, acc1Id);
            System.out.println("\n✓ Account 1 (" + acc1.getAccountNumber() + ") has " + 
                             acc1.getSubscriptions().size() + " subscription(s):");
            for (RefNotificationChannel channel : acc1.getSubscriptions()) {
                System.out.println("  - " + channel.getChannelName() + 
                                 " (Cost: $" + channel.getMonthlyCost() + "/month)");
            }
            
            acc2 = session.get(Account.class, acc2Id);
            System.out.println("\n✓ Account 2 (" + acc2.getAccountNumber() + ") has " + 
                             acc2.getSubscriptions().size() + " subscription(s):");
            for (RefNotificationChannel channel : acc2.getSubscriptions()) {
                System.out.println("  - " + channel.getChannelName() + 
                                 " (Cost: $" + channel.getMonthlyCost() + "/month)");
            }
            
            System.out.println("\n✓ Many-to-Many relationships verified!");
            
        } catch (Exception e) {
            if (transaction != null) transaction.rollback();
            System.err.println("Error in many-to-many mapping: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}
