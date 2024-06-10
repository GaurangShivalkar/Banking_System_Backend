package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserId(Long id);
    User findByEmailAndPassword(String email, String password);
    User findByUsername(String username);

    User findByEmail(String email);

}
