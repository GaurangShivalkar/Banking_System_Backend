package com.backendapp.bankingsystem.services;


import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    public Transaction insertTransaction(Transaction transaction) {
        // Save the user to the database
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionBySourceAccountId(String sourceAccountId) {
        List<Transaction> transactionList = transactionRepository.findBySourceAccountId(sourceAccountId);
        return transactionList;
    }

    //    public List<Transaction> getTransactionByTimestamp(String timestamp) {
//        return (List<Transaction>) transactionRepository.findByTimestamp(timestamp);
//    }
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isPresent()) {
            Transaction transaction = existingTransaction.get();
            if (updatedTransaction.getTransactionStatus() != null) {
                transaction.setTransactionStatus(updatedTransaction.getTransactionStatus());
            }
            return transactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Account not found");
        }

    }
}