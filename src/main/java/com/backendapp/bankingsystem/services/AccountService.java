package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.repositories.AccountRepository;
import com.backendapp.bankingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    public Account createAccount(Account account) {

        // Save the user to the database
        return accountRepository.save(account);
    }
}
