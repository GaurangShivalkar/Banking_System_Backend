package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.services.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    public List<Account> getAllAccounts() {
        List<Account> allAccounts = accountService.getAllAccounts();
        return allAccounts;
    }

    @GetMapping("/getAccountsByCustomerId/{id}")
    public Optional<Account> getAccountsByCustomerId(@PathVariable long id) {
        Optional<Account> customerAccount = accountService.getAccountByCustomerId(id);
        return customerAccount;
    }

    @GetMapping("/getAccountByAccountNo/{accNo}")
    public Account getAccountByAccountNo(@PathVariable String accNo) {
        return accountService.getAccountByAccountNo(accNo);
    }

    @PutMapping("/updateBalance/{id}")
    public ResponseEntity<String> updateBalance(@PathVariable Long id, @RequestBody double amount) {
        Account updatedBalance = accountService.updateBalance(id, amount);
        return new ResponseEntity<>("Account balance successfully", HttpStatus.OK);
    }

    @PutMapping("/updateAccount/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(id, account);
        return new ResponseEntity<>("Account status has been changed to: " + updatedAccount.getAccountStatus(), HttpStatus.OK);
    }
}
