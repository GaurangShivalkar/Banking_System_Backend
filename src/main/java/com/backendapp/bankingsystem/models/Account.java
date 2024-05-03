package com.backendapp.bankingsystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long accountId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
    private Customer customer;

    @Column(unique = true)
    private String accountNumber;

    @ManyToOne
    @JoinColumn(name = "branch_id", referencedColumnName = "branchId")
    private Branch branch;

    private String accountType;
    private String accountStatus;
    private LocalDateTime accountTimestamp = LocalDateTime.now();
    private double balance;


}
