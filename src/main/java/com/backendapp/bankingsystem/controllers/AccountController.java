package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.repositories.AccountRepository;
import com.backendapp.bankingsystem.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/createAccount")
    public ResponseEntity<String> registerUser(@RequestBody Account account) {

        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>("User registered successfully with ID: " + createdAccount.getAccountNumber(), HttpStatus.CREATED);
    }

}
