package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long id);
    User findByEmailAndPassword(String email, String password);
    User findByUsername(String username);

    User findByCustomer_CustomerId(Long customerId);
    User findByEmail(String email);

    @Query("SELECT Count(userId) FROM User")
    long countUsers();


}
