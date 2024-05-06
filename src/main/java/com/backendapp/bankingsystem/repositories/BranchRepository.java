package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.Branch;
import org.springframework.data.jpa.repository.JpaRepository;


public interface BranchRepository extends JpaRepository<Branch, Long> {

}
