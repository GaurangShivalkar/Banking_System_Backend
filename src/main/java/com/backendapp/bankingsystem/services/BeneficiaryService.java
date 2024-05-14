package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Beneficiary;
import com.backendapp.bankingsystem.repositories.BeneficiaryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BeneficiaryService {

    @Autowired
    private BeneficiaryRepository beneficiaryRepository;

    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    public Beneficiary getBeneficiaryById(Long id) {
        Optional<Beneficiary> beneficiaryOptional = beneficiaryRepository.findById(id);
        return beneficiaryOptional.orElse(null);
    }

    public Beneficiary addBeneficiary(Beneficiary beneficiary) {
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

