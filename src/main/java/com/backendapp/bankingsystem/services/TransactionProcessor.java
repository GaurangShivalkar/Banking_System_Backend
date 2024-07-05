package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.repositories.AccountRepository;

public class TransactionProcessor {

    private AccountRepository accountRepository;

    public TransactionProcessor(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void processTransaction(Transaction transaction) {
        Account sourceAccount = accountRepository.findByAccountNumber(transaction.getSourceAccountId());
        handleInternalTransaction(transaction, sourceAccount);
        updateSourceAccountBalance(transaction, sourceAccount);
        finalizeTransaction(transaction);
    }

    private void handleInternalTransaction(Transaction transaction, Account sourceAccount) {
        if (transaction.getTransactionMethod().equals("INTERNAL")) {
            Account destinationAccount = accountRepository.findByAccountNumber(transaction.getDestinationAccountId());
            double receivedBalance = destinationAccount.getBalance() + transaction.getAmount();
            transaction.setReceiverBalance(receivedBalance);
            destinationAccount.setBalance(receivedBalance);
            accountRepository.save(destinationAccount);
        }
    }

    private void updateSourceAccountBalance(Transaction transaction, Account sourceAccount) {
        double newBalance = sourceAccount.getBalance() - transaction.getAmount();
        sourceAccount.setBalance(newBalance);
        accountRepository.save(sourceAccount);
    }

    private void finalizeTransaction(Transaction transaction) {
        transaction.setTransactionStatus("completed");
    }
}
