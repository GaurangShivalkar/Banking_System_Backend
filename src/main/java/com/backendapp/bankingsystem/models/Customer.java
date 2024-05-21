package com.backendapp.bankingsystem.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
public class Customer {
    @Id
    private long customerId;

    private String customerName;
    private String address;
    @Column(unique = true)
    private String aadharNumber;
    @Column(unique = true)
    private String panNumber;
    private String phoneNumber;
    private String zipcode;
    private String status;

}
