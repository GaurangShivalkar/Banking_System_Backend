package com.backendapp.bankingsystem.controllers;


import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.repositories.TransactionRepository;
import com.backendapp.bankingsystem.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;
    @Autowired
    private TransactionRepository transactionRepository;

    @PostMapping("/makeTransaction")
    public ResponseEntity<String> insertTransaction(@RequestBody Transaction transaction) {

        Transaction insertedTransaction = transactionService.insertTransaction(transaction);
        return new ResponseEntity<>("Transaction inserted successfully with ID: " + insertedTransaction.getTransactionId(), HttpStatus.CREATED);
    }

    @GetMapping("/showTransaction")
    public List<Transaction> showTransaction() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/getTransactionsById/{id}")
    public Optional<Transaction> getTransactionById(@RequestParam long id) {
        return transactionService.getTransactionById(id);

    }

    @GetMapping("/getTransactionBySourceAccountId/{sourceAccountId}")
    public List<Transaction> getTransactionBySourceAccountId(@RequestParam String sourceAccountId) {
        return transactionService.getTransactionBySoureAccountId(sourceAccountId);

    }

//    @GetMapping("/getByTimestamp/{timestamp}")
//    public List<Transaction> getByTimestamp(@PathVariable String timestamp) {
//        return transactionService.getTransactionByTimestamp(timestamp);
//    }

    @PutMapping("updateTransactionStatus")
    public ResponseEntity<String> updateTransactionStatus(@RequestBody Long id, @RequestBody Transaction transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        return new ResponseEntity<>("Transaction status has been changed to: " + updatedTransaction.getTransactionStatus(), HttpStatus.OK);
    }
}
