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
    @JoinColumn(name = "user_id", referencedColumnName = "userId")
    private User user;

    private String transactionMethod;
    private String transactionStatus;
    private String description;
    private String sourceAccountId;
    private String destinationAccountId;
    private double amount;
    private LocalDateTime accountTimestamp = LocalDateTime.now();
    private double changedBalance;




}
