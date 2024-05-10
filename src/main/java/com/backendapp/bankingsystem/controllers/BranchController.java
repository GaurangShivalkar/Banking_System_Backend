package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.models.Branch;
import com.backendapp.bankingsystem.repositories.BranchRepository;
import com.backendapp.bankingsystem.services.BranchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/branch")
public class BranchController {
    @Autowired
    private BranchService branchService;
    @Autowired
    private BranchRepository branchRepository;

    @PostMapping("/saveBranch")
    public ResponseEntity<String> saveBranch(@RequestBody Branch branch) {

        Branch savedBranch = branchService.saveBranch(branch);
        return new ResponseEntity<>("Branch saved successfully with ID: " + savedBranch.getBranchId(), HttpStatus.CREATED);
    }

    @GetMapping("/showAllBranches")
    public List<Branch> getAllUser() {
        List<Branch> allBranches = branchService.getAllBranches();
        return allBranches;
    }

    @GetMapping("/showBranch/{id}")
    public Optional<Branch> getBranchById(@PathVariable String id) {
        Optional<Branch> branchData = branchService.getBranchById(id);
        return branchData;
    }

    @PutMapping("/updateBranch/{id}")
    public ResponseEntity<String> updateBranch(@PathVariable String id, @RequestBody Branch branch) {

        Branch updatedBranch = branchService.updateBranch(id, branch);
        return new ResponseEntity<>("the Branch data is updated successfully" + updatedBranch.getBranchName(), HttpStatus.OK);
    }

}
