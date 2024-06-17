package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.services.AccountService;
import com.backendapp.bankingsystem.services.Generators;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {
    @Autowired
    private AccountService accountService;

    @PostMapping("/createAccount")
    public ResponseEntity<String> createAccount(@RequestBody Account account) {
        account.setAccountNumber(Generators.generateAccountNumber());
        account.setAccountStatus("pending");
        Account createdAccount = accountService.createAccount(account);
        return new ResponseEntity<>("Account registered successfully with ID: " + createdAccount.getAccountNumber(), HttpStatus.CREATED);
    }

    @GetMapping("/getAllAccounts")
    public List<Account> getAllAccounts() {
        List<Account> allAccounts = accountService.getAllAccounts();
        return allAccounts;
    }

    @GetMapping("/getAccountsByCustomerId/{id}")
    public List<Account> getAccountsByCustomerId(@PathVariable long id) {
        List<Account> customerAccount = accountService.getAccountByCustomerId(id);
        return customerAccount;
    }

    @GetMapping("/sumOfAccounts/{customerId}")
    public Double getSumOfBalances(@PathVariable long customerId) {
        return accountService.getSumOfBalancesByCustomerId(customerId);
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


    @CrossOrigin(origins = "http://localhost:5173")
    @PutMapping("/updateAccount/{id}")
    public ResponseEntity<String> updateAccount(@PathVariable Long id, @RequestBody Account account) {
        Account updatedAccount = accountService.updateAccount(id, account);
        return new ResponseEntity<>("Account status has been changed to: " + updatedAccount.getAccountStatus(), HttpStatus.OK);
    }
}
