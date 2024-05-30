package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.Beneficiary;
import com.backendapp.bankingsystem.services.BeneficiaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/beneficiaries")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    @GetMapping("/getAllBeneficiary")
    public ResponseEntity<List<Beneficiary>> getAllBeneficiaries() {
        List<Beneficiary> beneficiaries = beneficiaryService.getAllBeneficiaries();
        return ResponseEntity.ok(beneficiaries);
    }

    @GetMapping("/getBeneficiaryByCustomerId")
    public ResponseEntity<List<Beneficiary>> getBeneficiaryByCustomerId(@RequestParam Long customerId) {
        List<Beneficiary> beneficiariesByCustId = beneficiaryService.getBeneficiariesByCustomerId(customerId);
        return ResponseEntity.ok(beneficiariesByCustId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beneficiary> getBeneficiaryById(@PathVariable Long id) {
        Beneficiary beneficiary = beneficiaryService.getBeneficiaryById(id);
        if (beneficiary != null) {
            return ResponseEntity.ok(beneficiary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/addBeneficiary")
    public ResponseEntity<Beneficiary> addBeneficiary(@RequestBody Beneficiary beneficiary) {
        Beneficiary addedBeneficiary = beneficiaryService.addBeneficiary(beneficiary);
        return ResponseEntity.status(HttpStatus.CREATED).body(addedBeneficiary);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Beneficiary> updateBeneficiary(@PathVariable Long id, @RequestBody Beneficiary updatedBeneficiary) {
        Beneficiary beneficiary = beneficiaryService.updateBeneficiary(id, updatedBeneficiary);
        if (beneficiary != null) {
            return ResponseEntity.ok(beneficiary);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long id) {
        beneficiaryService.deleteBeneficiary(id);
        return ResponseEntity.noContent().build();
    }
}


