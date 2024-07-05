package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.models.Beneficiary;
import com.backendapp.bankingsystem.models.Transaction;
import com.backendapp.bankingsystem.repositories.AccountRepository;
import com.backendapp.bankingsystem.repositories.BeneficiaryRepository;

import java.util.Optional;

public class TransactionValidator {

    private BeneficiaryRepository beneficiaryRepository;
    private AccountRepository accountRepository;

    public TransactionValidator(BeneficiaryRepository beneficiaryRepository, AccountRepository accountRepository) {
        this.beneficiaryRepository = beneficiaryRepository;
        this.accountRepository = accountRepository;
    }

    public void validateTransaction(Transaction transaction) {
        validateBeneficiary(transaction);
        validateAmount(transaction);
        validateTransactionType(transaction);
    }

    private void validateBeneficiary(Transaction transaction) {
        Optional<Beneficiary> beneficiary = beneficiaryRepository.findById(transaction.getBeneficiary().getBeneficiaryId());
        if (beneficiary.isEmpty()) {
            throw new CustomExceptions.BeneficiaryNotFoundException("Beneficiary not found");
        }
        transaction.setTransactionMethod(beneficiary.get().getBeneficiaryType());
        transaction.setDestinationAccountId(beneficiary.get().getAccountNumber());
    }

    private void validateAmount(Transaction transaction) {
        Account sourceAccount = accountRepository.findByAccountNumber(transaction.getSourceAccountId());
        if (sourceAccount.getBalance() < transaction.getAmount()) {
            throw new CustomExceptions.InsufficientBalanceException("Insufficient balance in the source account");
        }
    }

    private void validateTransactionType(Transaction transaction) {
        if (transaction.getTransactionType().equals("SELF") && !transaction.getSourceAccountId().equals(transaction.getDestinationAccountId())) {
            throw new CustomExceptions.IllegalTransactionException("The source and destination account should be the same!");
        }
        if (transaction.getTransactionType().equals("OTHER") && transaction.getSourceAccountId().equals(transaction.getDestinationAccountId())) {
            throw new CustomExceptions.IllegalTransactionException("The source and destination account should not be the same!");
        }
        if (transaction.getTransactionType().equals("RTGS") && transaction.getAmount() < 200000) {
            throw new CustomExceptions.IllegalTransactionException("The amount can't be less than 2 lakh Rs for RTGS transactions");
        }
        if (transaction.getTransactionType().equals("IMPS") && transaction.getAmount() > 200000) {
            throw new CustomExceptions.IllegalTransactionException("The amount can't be more than 2 lakh Rs for IMPS transactions");
        }
    }
}

