package com.banking.entity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ref_notification_channels")
public class RefNotificationChannel {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "channel_seq")
    @SequenceGenerator(name = "channel_seq", sequenceName = "REF_NOTIFICATION_CHANNEL_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "channel_name", length = 50, nullable = false)
    private String channelName;
    
    @Column(name = "monthly_cost", precision = 10, scale = 2)
    private BigDecimal monthlyCost;
    
    // Constructors
    public RefNotificationChannel() {
    }
    
    public RefNotificationChannel(String channelName, BigDecimal monthlyCost) {
        this.channelName = channelName;
        this.monthlyCost = monthlyCost;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getChannelName() {
        return channelName;
    }
    
    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }
    
    public BigDecimal getMonthlyCost() {
        return monthlyCost;
    }
    
    public void setMonthlyCost(BigDecimal monthlyCost) {
        this.monthlyCost = monthlyCost;
    }
    
    @Override
    public String toString() {
        return "RefNotificationChannel{" +
                "id=" + id +
                ", channelName='" + channelName + '\'' +
                ", monthlyCost=" + monthlyCost +
                '}';
    }
}
