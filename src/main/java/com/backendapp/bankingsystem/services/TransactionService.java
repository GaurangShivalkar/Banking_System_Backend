package com.backendapp.bankingsystem.services;


import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.models.Beneficiary;
import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.repositories.AccountRepository;
import com.backendapp.bankingsystem.repositories.BeneficiaryRepository;
import com.backendapp.bankingsystem.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    public Transaction insertTransaction(Transaction transaction) {
        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(transaction.getBeneficiary().getBeneficiaryId());
        transaction.setTransactionMethod(beneficiary.get().getBeneficiaryType());

        Account sourceAccount = accountRepository.findByAccountNumber(transaction.getSourceAccountId());

        if (transaction.getTransactionMethod().equals("INTERNAL")) {
            transaction.setDestinationAccountId(beneficiary.get().getAccountNumber());
            Account destinationAccount = accountRepository.findByAccountNumber(transaction.getDestinationAccountId());
            double receivedBalance = destinationAccount.getBalance() + transaction.getAmount();
            // Update the destination account balance
            destinationAccount.setBalance(receivedBalance);
            accountRepository.save(destinationAccount);
        }
        // Check if the balance is sufficient
        if (sourceAccount.getBalance() < transaction.getAmount()) {
            System.out.println("Insufficient balance in the source account");
        }

        // Calculate the new balance
        double newBalance = sourceAccount.getBalance() - transaction.getAmount();


        // Update the source account balance
        sourceAccount.setBalance(newBalance);
        accountRepository.save(sourceAccount);


        // Update the transaction with the new balance
        transaction.setChangedBalance(newBalance);
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