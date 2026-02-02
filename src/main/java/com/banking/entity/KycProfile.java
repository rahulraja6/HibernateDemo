package com.banking.entity;

import javax.persistence.*;

@Entity
@Table(name = "kyc_profiles")
public class KycProfile {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "kyc_seq")
    @SequenceGenerator(name = "kyc_seq", sequenceName = "KYC_PROFILE_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "pan_number", length = 20, unique = true)
    private String panNumber;
    
    @Column(name = "risk_status", length = 20)
    private String riskStatus;
    
    // Constructors
    public KycProfile() {
    }
    
    public KycProfile(String panNumber, String riskStatus) {
        this.panNumber = panNumber;
        this.riskStatus = riskStatus;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getPanNumber() {
        return panNumber;
    }
    
    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }
    
    public String getRiskStatus() {
        return riskStatus;
    }
    
    public void setRiskStatus(String riskStatus) {
        this.riskStatus = riskStatus;
    }
    
    @Override
    public String toString() {
        return "KycProfile{" +
                "id=" + id +
                ", panNumber='" + panNumber + '\'' +
                ", riskStatus='" + riskStatus + '\'' +
                '}';
    }
}
