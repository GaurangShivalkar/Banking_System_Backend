package com.backendapp.bankingsystem.models;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "branches")
public class Branch {
    @Id
    private String branchId;

    private String branchName;
    private String branchAddress;
    private String branchCity;
    private String branchZipCode;


}
