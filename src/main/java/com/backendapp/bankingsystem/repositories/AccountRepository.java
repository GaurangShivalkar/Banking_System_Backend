package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {

}
