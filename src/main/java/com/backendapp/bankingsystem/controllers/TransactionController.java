package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.repositories.TransactionRepository;
import com.backendapp.bankingsystem.services.PdfService;
import com.backendapp.bankingsystem.services.TransactionService;
import com.backendapp.bankingsystem.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
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
    @Autowired
    private PdfService pdfService;

    @PostMapping("/makeTransaction")
    public ResponseEntity<String> insertTransaction(@RequestBody Transaction transaction) {
        try {
            Transaction insertedTransaction = transactionService.insertTransaction(transaction);
            String msg = "Hi Customer " + insertedTransaction.getCustomer().getCustomerName() + " your transaction with " + insertedTransaction.getBeneficiary().getName() + " has been with successfully processed of Rs " + insertedTransaction.getAmount();
            String subject = "Your transaction has been processed successfully!!";
            Long customerId = insertedTransaction.getCustomer().getCustomerId();
            String email = userService.getEmailByCustomer(customerId);
            userService.sendSimpleMail(email, msg, subject);
            return new ResponseEntity<>("Transaction inserted successfully with ID: " + insertedTransaction.getTransactionId(), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
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

    @GetMapping("/getPdf/{sourceAccountId}")
    public ResponseEntity<byte[]> getTransactionBySourceAccountIdPdf(@PathVariable String sourceAccountId) {
        try {
            List<Transaction> transactionList = transactionService.getTransactionBySourceAccountId(sourceAccountId);
            byte[] pdfContent = pdfService.generateTransactionStatusPdf(transactionList);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=transactions_by_source_account.pdf")
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(pdfContent);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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

    @GetMapping("/getTotalCredits/{accountNumber}")
    public double getTotalCredits(@PathVariable String accountNumber) {
        return transactionService.getTotalCredits(accountNumber);
    }

    @GetMapping("/getTotalDebits/{accountNumber}")
    public double getTotalDebits(@PathVariable String accountNumber) {
        return transactionService.getTotalDebits(accountNumber);
    }

    @GetMapping("/changedBalance/{accountNumber}")
    public List<Map<String, Object>> getTotalChangedBalance(@PathVariable String accountNumber) {
        // Replace '6102056423' with the actual source account ID you want to query

        return transactionService.getTotalChangedBalanceByDate(accountNumber);
    }

//    @GetMapping("/monthly-counts")
//    public Map<String, Long> getTransactionCountsMonthWise() {
//        Map<String, Long> transactionCounts = transactionService.getTransactionCountsMonthWise();
//        return transactionCounts;
//    }
}
