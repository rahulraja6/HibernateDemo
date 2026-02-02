package com.banking.entity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "bank_transactions")
public class BankTransaction {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transaction_seq")
    @SequenceGenerator(name = "transaction_seq", sequenceName = "BANK_TRANSACTION_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "tx_date")
    private Date txDate;
    
    @Column(name = "type", length = 10)
    private String type; // CREDIT/DEBIT
    
    @Column(name = "amount", precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(name = "status", length = 20)
    private String status;
    
    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
    
    // Constructors
    public BankTransaction() {
    }
    
    public BankTransaction(Date txDate, String type, BigDecimal amount, String status) {
        this.txDate = txDate;
        this.type = type;
        this.amount = amount;
        this.status = status;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Date getTxDate() {
        return txDate;
    }
    
    public void setTxDate(Date txDate) {
        this.txDate = txDate;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public Account getAccount() {
        return account;
    }
    
    public void setAccount(Account account) {
        this.account = account;
    }
    
    @Override
    public String toString() {
        return "BankTransaction{" +
                "id=" + id +
                ", txDate=" + txDate +
                ", type='" + type + '\'' +
                ", amount=" + amount +
                ", status='" + status + '\'' +
                ", accountId=" + (account != null ? account.getId() : null) +
                '}';
    }
}
