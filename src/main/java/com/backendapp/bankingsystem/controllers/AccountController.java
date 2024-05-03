package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {

        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>("Account registered successfully with ID: " + createdAccount.getAccountNumber(), HttpStatus.CREATED);
    }

    @GetMapping("/getAllAccounts")
    public void getAllAccounts() {
    }

    @GetMapping("/getAccountsByCustomerId/{id}")
    public void getAccountsByCustomerId(@PathVariable int id) {
    }

    @GetMapping("/getAccountByAccountNo/{id}")
    public void getAccountByAccountNo(@PathVariable int id) {
    }

    @PutMapping("/updateBalance/{id}")
    public void updateBalance(@PathVariable int id, @RequestBody Account account) {
    }

    @PutMapping("/updateAccount/{id}")
    public void updateAccount(@PathVariable int id, @RequestBody Account account) {
    }
}
