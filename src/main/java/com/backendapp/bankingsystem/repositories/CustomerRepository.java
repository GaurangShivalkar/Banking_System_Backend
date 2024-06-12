package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByCustomerId(Long customerId);
    Customer findByPanNumber(String panNumber);
}
