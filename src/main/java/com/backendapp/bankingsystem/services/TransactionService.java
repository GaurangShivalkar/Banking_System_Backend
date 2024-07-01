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
            throw new IllegalArgumentException("Beneficiary not found");
        }

        transaction.setTransactionMethod(beneficiary.get().getBeneficiaryType());
        transaction.setDestinationAccountId(beneficiary.get().getAccountNumber());
        Account sourceAccount = accountRepository.findByAccountNumber(transaction.getSourceAccountId());

        if (transaction.getTransactionMethod().equals("INTERNAL")) {
            Account destinationAccount = accountRepository.findByAccountNumber(transaction.getDestinationAccountId());
            double receivedBalance = destinationAccount.getBalance() + transaction.getAmount();
            transaction.setReceiverBalance(receivedBalance);
            destinationAccount.setBalance(receivedBalance);
            accountRepository.save(destinationAccount);
        }

        if (sourceAccount.getBalance() < transaction.getAmount()) {
            throw new IllegalArgumentException("Insufficient balance in the source account");
        }
        if (transaction.getTransactionType().equals("RTGS") && transaction.getAmount() < 200000) {
            throw new IllegalArgumentException("The amount can't be less than 2 lakh rs for RTGS transactions");
        }

        if (transaction.getTransactionType().equals("IMPS") && transaction.getAmount() > 200000) {
            throw new IllegalArgumentException("The amount can't be more than 2 lakh rs for IMPS transactions");
        }

        double newBalance = sourceAccount.getBalance() - transaction.getAmount();
        sourceAccount.setBalance(newBalance);
        accountRepository.save(sourceAccount);

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
