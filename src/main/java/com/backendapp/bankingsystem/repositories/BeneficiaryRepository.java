package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    // You can add custom query methods here if needed
}
