package com.backendapp.bankingsystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    private long customerId;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private String customerName;
    private String address;
    private String phoneNumber;
    private String aadharNumber;
    private String panNumber;
    private String zipcode;
    private String status;

    @Column(unique = true)
    private String upiId;
}