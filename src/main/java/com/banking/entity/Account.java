package com.banking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "accounts")
public class Account {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_seq")
    @SequenceGenerator(name = "account_seq", sequenceName = "ACCOUNT_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "account_number", length = 20, unique = true, nullable = false)
    private String accountNumber;
    
    @Column(name = "balance", precision = 19, scale = 2)
    private BigDecimal balance;
    
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    
    @Version
    @Column(name = "version")
    private Long version;
    
    @ManyToMany
    @JoinTable(
        name = "account_subscriptions",
        joinColumns = @JoinColumn(name = "account_id"),
        inverseJoinColumns = @JoinColumn(name = "channel_id")
    )
    private Set<RefNotificationChannel> subscriptions = new HashSet<>();
    
    // Constructors
    public Account() {
    }
    
    public Account(String accountNumber, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public Customer getCustomer() {
        return customer;
    }
    
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
    
    public Long getVersion() {
        return version;
    }
    
    public void setVersion(Long version) {
        this.version = version;
    }
    
    public Set<RefNotificationChannel> getSubscriptions() {
        return subscriptions;
    }
    
    public void setSubscriptions(Set<RefNotificationChannel> subscriptions) {
        this.subscriptions = subscriptions;
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "id=" + id +
                ", accountNumber='" + accountNumber + '\'' +
                ", balance=" + balance +
                ", customer=" + (customer != null ? customer.getFullName() : null) +
                ", version=" + version +
                '}';
    }
}
