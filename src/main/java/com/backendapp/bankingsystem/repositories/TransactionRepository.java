package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountId(String sourceAccountId);
    //Transaction findByTimestamp(String timestamp);
}
