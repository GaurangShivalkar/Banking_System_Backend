package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.ResetToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.Optional;

@Repository
public interface ResetTokenRepository extends JpaRepository<ResetToken, Long> {
    Optional<ResetToken> findByResetPasswordToken(String token);

    void deleteByResetPasswordToken(String token);

    ResetToken findByExpiryDate(Date expiryDate);

}
