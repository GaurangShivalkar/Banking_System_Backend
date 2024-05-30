package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.models.Beneficiary;
import com.backendapp.bankingsystem.repositories.AccountRepository;
import com.backendapp.bankingsystem.repositories.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    @Autowired
    private AccountRepository accountRepository;

    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    public List<Beneficiary> getBeneficiariesByCustomerId(long customerId) {
        List<Beneficiary> beneficiaryListCustomer = beneficiaryRepository.findByCustomer_CustomerId(customerId);
        return beneficiaryListCustomer;
    }
    public Beneficiary getBeneficiaryById(Long id) {
        Optional<Beneficiary> beneficiaryOptional = beneficiaryRepository.findById(id);
        return beneficiaryOptional.orElse(null);
    }

    public Beneficiary addBeneficiary(Beneficiary beneficiary) {
        // Fetch the account using the account number from the beneficiary
        Account account = accountRepository.findByAccountNumber(beneficiary.getAccountNumber());
        String beneficiaryType = beneficiary.getBeneficiaryType();
        // Check if the account exists
        if (account == null && beneficiaryType.equals("INTERNAL")) {
            throw new IllegalArgumentException("Account not found for account number: " + beneficiary.getAccountNumber());
        }
        if (beneficiaryType.equals("INTERNAL")) {
            beneficiary.setBranch(account.getBranch());
        }

        return beneficiaryRepository.save(beneficiary);
    }

    public Beneficiary updateBeneficiary(Long id, Beneficiary updatedBeneficiary) {
        Optional<Beneficiary> beneficiaryOptional = beneficiaryRepository.findById(id);
        if (beneficiaryOptional.isPresent()) {
            updatedBeneficiary.setBeneficiaryId(id); // Ensure the ID is set for update
            return beneficiaryRepository.save(updatedBeneficiary);
        }
        return null; // Handle not found case
    }

    public void deleteBeneficiary(Long id) {
        beneficiaryRepository.deleteById(id);
    }
}

