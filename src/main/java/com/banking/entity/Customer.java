package com.banking.entity;

import javax.persistence.*;

@Entity
@Table(name = "customers")
public class Customer {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "customer_seq")
    @SequenceGenerator(name = "customer_seq", sequenceName = "CUSTOMER_SEQ", allocationSize = 1)
    @Column(name = "id")
    private Long id;
    
    @Column(name = "full_name", length = 100)
    private String fullName;
    
    @Column(name = "email", length = 100)
    private String email;
    
    @OneToOne
    @JoinColumn(name = "kyc_id", unique = true)
    private KycProfile kycProfile;
    
    // Constructors
    public Customer() {
    }
    
    public Customer(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getFullName() {
        return fullName;
    }
    
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public KycProfile getKycProfile() {
        return kycProfile;
    }
    
    public void setKycProfile(KycProfile kycProfile) {
        this.kycProfile = kycProfile;
    }
    
    @Override
    public String toString() {
        return "Customer{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", kycProfile=" + (kycProfile != null ? kycProfile.getId() : null) +
                '}';
    }
}
