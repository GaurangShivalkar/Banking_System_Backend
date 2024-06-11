package com.backendapp.bankingsystem.controllers;


import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.repositories.TransactionRepository;
import com.backendapp.bankingsystem.services.TransactionService;
import com.backendapp.bankingsystem.services.UserService;
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
    @Autowired
    private UserService userService;

    @PostMapping("/makeTransaction")
    public ResponseEntity<String> insertTransaction(@RequestBody Transaction transaction) {

        Transaction insertedTransaction = transactionService.insertTransaction(transaction);
        String msg = "Hi Customer " + insertedTransaction.getCustomer().getCustomerName() + " your transaction with " + insertedTransaction.getBeneficiary().getName() + " has been with successfully processed of Rs " + insertedTransaction.getAmount();
        String subject = "Your transaction has been processed successfully!!";
        Long customerId = insertedTransaction.getCustomer().getCustomerId();
        String email = userService.getEmailByCustomer(customerId);
        userService.sendSimpleMail(email, msg, subject);
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
    public List<Transaction> getTransactionBySourceAccountId(@PathVariable String sourceAccountId) {
        List<Transaction> transactionList = transactionService.getTransactionBySourceAccountId(sourceAccountId);
        return transactionList;
    }

//    @GetMapping("/getByTimestamp/{timestamp}")
//    public List<Transaction> getByTimestamp(@PathVariable String timestamp) {
//        return transactionService.getTransactionByTimestamp(timestamp);
//    }

    @PutMapping("/updateTransactionStatus/{id}")
    public ResponseEntity<String> updateTransactionStatus(@PathVariable Long id, @RequestBody Transaction transaction) {
        Transaction updatedTransaction = transactionService.updateTransaction(id, transaction);
        String msg = "Hi your transaction has been updated with " + transaction.getTransactionStatus() + " status \n Please contact the bank moderator for any queries";
        String subject = "Update on the transaction status";
        Long customerId = updatedTransaction.getCustomer().getCustomerId();
        String email = userService.getEmailByCustomer(customerId);
        userService.sendSimpleMail(email, msg, subject);
        return new ResponseEntity<>("Transaction status has been changed to: " + updatedTransaction.getTransactionStatus(), HttpStatus.OK);
    }

}
