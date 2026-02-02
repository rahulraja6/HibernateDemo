package com.banking.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "account_subscriptions")
public class AccountSubscription implements Serializable {
    
    @Id
    @Column(name = "account_id")
    private Long accountId;
    
    @Id
    @Column(name = "channel_id")
    private Long channelId;
    
    // Constructors
    public AccountSubscription() {
    }
    
    public AccountSubscription(Long accountId, Long channelId) {
        this.accountId = accountId;
        this.channelId = channelId;
    }
    
    // Getters and Setters
    public Long getAccountId() {
        return accountId;
    }
    
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public Long getChannelId() {
        return channelId;
    }
    
    public void setChannelId(Long channelId) {
        this.channelId = channelId;
    }
    
    @Override
    public String toString() {
        return "AccountSubscription{" +
                "accountId=" + accountId +
                ", channelId=" + channelId +
                '}';
    }
}
