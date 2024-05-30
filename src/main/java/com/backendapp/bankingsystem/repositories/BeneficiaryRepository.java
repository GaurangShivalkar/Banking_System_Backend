package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.Beneficiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BeneficiaryRepository extends JpaRepository<Beneficiary, Long> {
    List<Beneficiary> findByCustomer_CustomerId(Long customerId);
    // You can add custom query methods here if needed
}
