package com.backendapp.bankingsystem.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long beneficiaryId;

    private String name;
    private String accountNumber;
    private String bankName;
    private String beneficiaryType;
    @ManyToOne
    @JoinColumn(name = "branchId", referencedColumnName = "branchId")
    private Branch branch;

}
