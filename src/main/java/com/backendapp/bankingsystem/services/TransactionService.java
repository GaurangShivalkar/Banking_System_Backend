package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.repositories.TransactionRepository;
import com.backendapp.bankingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction insertTransaction(Transaction transaction) {

        // Save the user to the database
        return transactionRepository.save(transaction);
    }
}
