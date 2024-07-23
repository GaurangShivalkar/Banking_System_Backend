package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
    Optional<RefreshToken> findByToken(String token);

    Boolean existsByToken(String token);

    void deleteByToken(String token);
}
