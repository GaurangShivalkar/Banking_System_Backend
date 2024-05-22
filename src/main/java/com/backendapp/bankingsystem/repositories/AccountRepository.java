package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);

    List<Account> findByCustomer_CustomerId(Long customerId);

}
