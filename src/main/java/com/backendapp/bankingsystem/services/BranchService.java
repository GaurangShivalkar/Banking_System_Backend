package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Branch;
import com.backendapp.bankingsystem.repositories.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BranchService {
    @Autowired
    private BranchRepository branchRepository;

    public Branch saveBranch(Branch branch) {

        // Save the user to the database
        return branchRepository.save(branch);

    }

    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    public Optional<Branch> getBranchById(Long branchId) {
        return branchRepository.findById(branchId);
    }

    public Branch updateBranch(Long id, Branch updateBranch) {
        Optional<Branch> existingBranch = branchRepository.findById(id);
        if (existingBranch.isPresent()) {
            Branch branch = existingBranch.get();
            if (updateBranch.getBranchAddress() != null) {
                branch.setBranchAddress(updateBranch.getBranchAddress());
            } else if (updateBranch.getBranchName() != null) {
                branch.setBranchName(updateBranch.getBranchName());
            }
            return branchRepository.save(branch);
        } else {
            throw new RuntimeException("Branch not found");
        }
    }
}