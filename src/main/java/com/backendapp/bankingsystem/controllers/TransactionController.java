package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.models.User;
import com.backendapp.bankingsystem.services.TransactionService;
import com.backendapp.bankingsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping("/makeTransaction")
    public ResponseEntity<String> registerUser(@RequestBody Transaction transaction) {

        Transaction insertedTransaction = transactionService.insertTransaction(transaction);
        return new ResponseEntity<>("User registered successfully with ID: " + insertedTransaction.getTransactionId(), HttpStatus.CREATED);
    }

}
