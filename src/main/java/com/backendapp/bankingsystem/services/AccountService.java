package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.repositories.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {
        account.setAccountNumber(Generators.generateAccountNumber());
        account.setAccountStatus("restricted");

        return accountRepository.save(account);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getAccountByCustomerId(long id) {
        return accountRepository.findByCustomer_CustomerId(id);
    }

    public Double getSumOfBalancesByCustomerId(long customerId) {
        Double sum = accountRepository.findSumOfBalancesByCustomerId(customerId);
        return sum != null ? sum : 0.0; // return 0.0 if sum is null
    }

    public Account getAccountByAccountNo(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public Account updateBalance(Long id, double amount) {
        Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent()) {
            Account account = existingAccount.get();
            double currentBalance = account.getBalance();
            double newBalance = currentBalance + amount; // Add the deposited amount to the current balance
            account.setBalance(newBalance);
            return accountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found");
        }
    }

    public Account updateAccount(Long id, Account updatedAccount) {
        Optional<Account> existingAccount = accountRepository.findById(id);
        if (existingAccount.isPresent()) {
            Account account = existingAccount.get();
            if (updatedAccount.getAccountStatus() != null) {
                account.setAccountStatus(updatedAccount.getAccountStatus());
            }
            return accountRepository.save(account);
        } else {
            throw new RuntimeException("Account not found");
        }
    }

}
