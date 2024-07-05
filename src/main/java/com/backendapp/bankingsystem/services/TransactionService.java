package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.models.Beneficiary;
import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.repositories.AccountRepository;
import com.backendapp.bankingsystem.repositories.BeneficiaryRepository;
import com.backendapp.bankingsystem.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Transactional
    public Transaction insertTransaction(Transaction transaction) {
        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(transaction.getBeneficiary().getBeneficiaryId());
        if (beneficiary.isEmpty()) {
            throw new CustomExceptions.BeneficiaryNotFoundException("Beneficiary not found");
        }

        transaction.setTransactionMethod(beneficiary.get().getBeneficiaryType());
        transaction.setDestinationAccountId(beneficiary.get().getAccountNumber());
        Account sourceAccount = accountRepository.findByAccountNumber(transaction.getSourceAccountId());

        validateTransaction(transaction, sourceAccount);

        if (transaction.getTransactionMethod().equals("INTERNAL")) {
            handleInternalTransaction(transaction);
        }

        processTransaction(transaction, sourceAccount);

        transaction.setTransactionStatus("processed");
        return transactionRepository.save(transaction);
    }

    private void validateTransaction(Transaction transaction, Account sourceAccount) {
        if (sourceAccount.getBalance() < transaction.getAmount()) {
            throw new CustomExceptions.InsufficientBalanceException("Insufficient balance in the source account");
        }
        if (transaction.getTransactionType().equals("SELF") && !transaction.getSourceAccountId().equals(transaction.getDestinationAccountId())) {
            throw new CustomExceptions.IllegalTransactionException("The source and destination account should be the same!");
        }
        if (transaction.getTransactionType().equals("OTHER") && transaction.getSourceAccountId().equals(transaction.getDestinationAccountId())) {
            throw new CustomExceptions.IllegalTransactionException("The source and destination account should not be the same!");
        }
        if (transaction.getTransactionType().equals("RTGS") && transaction.getAmount() < 200000) {
            throw new CustomExceptions.IllegalTransactionException("The amount can't be less than 2 lakh Rs for RTGS transactions");
        }
        if (transaction.getTransactionType().equals("IMPS") && transaction.getAmount() > 200000) {
            throw new CustomExceptions.IllegalTransactionException("The amount can't be more than 2 lakh Rs for IMPS transactions");
        }
    }


    private void handleInternalTransaction(Transaction transaction) {
        Account destinationAccount = accountRepository.findByAccountNumber(transaction.getDestinationAccountId());
        double receivedBalance = destinationAccount.getBalance() + transaction.getAmount();
        transaction.setReceiverBalance(receivedBalance);
        destinationAccount.setBalance(receivedBalance);
        accountRepository.save(destinationAccount);
    }

    private void processTransaction(Transaction transaction, Account sourceAccount) {
        double newBalance = sourceAccount.getBalance() - transaction.getAmount();
        sourceAccount.setBalance(newBalance);
        accountRepository.save(sourceAccount);

        transaction.setChangedBalance(newBalance);
        transaction.setTransactionStatus("completed");
    }


    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    public Optional<Transaction> getTransactionById(long id) {
        return transactionRepository.findById(id);
    }

    public List<Transaction> getTransactionBySourceAccountId(String sourceAccountId) {
        List<Transaction> transactionList = transactionRepository.findBySourceAccountIdOrDestinationAccountId(sourceAccountId, sourceAccountId);
        for (Transaction transaction : transactionList) {
            if (transaction.getDestinationAccountId().equals(sourceAccountId)) {
                Account destinationAccount = accountRepository.findByAccountNumber(transaction.getDestinationAccountId());
                transaction.setChangedBalance(destinationAccount.getBalance());
            }
        }
        return transactionList;
    }

    @Transactional
    public Transaction updateTransaction(Long id, Transaction updatedTransaction) {
        Optional<Transaction> existingTransaction = transactionRepository.findById(id);
        if (existingTransaction.isPresent()) {
            Transaction transaction = existingTransaction.get();
            if (updatedTransaction.getTransactionStatus() != null) {
                transaction.setTransactionStatus(updatedTransaction.getTransactionStatus());
            }
            return transactionRepository.save(transaction);
        } else {
            throw new RuntimeException("Transaction not found");
        }
    }

    public double getTotalDebits(String accountNumber) {
        return transactionRepository.findBySourceAccountId(accountNumber)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public double getTotalCredits(String accountNumber) {
        return transactionRepository.findByDestinationAccountId(accountNumber)
                .stream()
                .mapToDouble(Transaction::getAmount)
                .sum();
    }

    public List<Map<String, Object>> getTotalChangedBalanceByDate(String sourceAccountId) {
        List<Object[]> results = transactionRepository.getTotalChangedBalanceByDate(sourceAccountId);
        List<Map<String, Object>> transactionCounts = new ArrayList<>();
        for (Object[] result : results) {
            Map<String, Object> countMap = new HashMap<>();
            countMap.put("date", result[0]);
            countMap.put("balance", result[1]);
            transactionCounts.add(countMap);
        }
        return transactionCounts;
    }
}
