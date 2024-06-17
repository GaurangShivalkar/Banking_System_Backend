package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByAccountNumber(String accountNumber);

    List<Account> findByCustomer_CustomerId(Long customerId);

    @Query("SELECT SUM(a.balance) FROM Account a WHERE a.customer.customerId = :customerId")
    Double findSumOfBalancesByCustomerId(@Param("customerId") long customerId);
}
