package com.backendapp.bankingsystem.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long transactionId;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customerId")
    private Customer customer;

    private String transactionMethod;
    private String transactionStatus;
    private String transactionType;

    private String description;

    private String sourceAccountId;

    private String destinationAccountId;

    private double amount;
    private LocalDateTime Timestamp = LocalDateTime.now();
    private double changedBalance;
    private double receiverBalance;

    @ManyToOne
    @JoinColumn(name = "beneficiary_id", referencedColumnName = "beneficiaryId")
    private Beneficiary beneficiary;


}
