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
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long customerId;



    private String customerName;
    private String address;
    private String aadharNumber;
    private String panNumber;
    private String phoneNumber;
    private String zipcode;
    private String status;

    @Column(unique = true)
    private String upiId;
}
