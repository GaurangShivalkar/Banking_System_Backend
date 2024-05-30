package com.backendapp.bankingsystem.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long beneficiaryId;

    private String name;
    private String accountNumber;
    private String emailId;
    private String beneficiaryType;

    @ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "customerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "branchId", referencedColumnName = "branchId")
    private Branch branch;

}
