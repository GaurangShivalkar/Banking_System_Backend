package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);

    Boolean existsByEmail(String email);

    User findByUsernameOrEmail(String username, String email);

    boolean existsByUsername(String username);

    User findByRole(String role);
}
