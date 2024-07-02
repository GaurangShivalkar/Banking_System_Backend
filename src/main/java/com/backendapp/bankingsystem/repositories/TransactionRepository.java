package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountIdOrDestinationAccountId(String sourceAccountId, String destinationAccountId);

    List<Transaction> findBySourceAccountId(String sourceAccountId);

    List<Transaction> findByDestinationAccountId(String destinationAccountId);

    @Query("SELECT Count(transactionId) FROM Transaction ")
    long transactionCount();

    @Query(value = "SELECT EXTRACT(MONTH FROM timestamp) AS month, COUNT(transaction_id) AS transaction_count " +
            "FROM transactions " +
            "WHERE EXTRACT(YEAR FROM timestamp) = 2024 " +
            "GROUP BY EXTRACT(MONTH FROM timestamp) " +
            "ORDER BY month", nativeQuery = true)
    List<Object[]> getTransactionCountsByMonth();

    @Query("SELECT " +
            "SUM(CASE WHEN t.transactionType = 'RTGS' THEN 1 ELSE 0 END) AS rtgsCount, " +
            "SUM(CASE WHEN t.transactionType = 'NEFT' THEN 1 ELSE 0 END) AS neftCount, " +
            "SUM(CASE WHEN t.transactionType = 'IMPS' THEN 1 ELSE 0 END) AS impsCount " +
            "FROM Transaction t")
    List<Object[]> countTransactionTypes();

    @Query("SELECT EXTRACT(DATE FROM Timestamp) AS transactionDate, " +
            "CASE WHEN sourceAccountId =?1 THEN changedBalance ELSE receiverBalance END AS total_changed_balance " +
            "FROM Transaction " +
            "WHERE sourceAccountId  = ?1 OR destinationAccountId = ?1" +

            " ORDER BY EXTRACT(DATE FROM Timestamp)")
    List<Object[]> getTotalChangedBalanceByDate(String sourceAccountId);


    @Query("SELECT t FROM Transaction t WHERE t.beneficiary.name LIKE %?1%")
    List<Transaction> getBeneficiaryByName(String beneficiaryName);

    @Query("SELECT t FROM Transaction t WHERE t.Timestamp BETWEEN ?1 AND ?2")
    List<Transaction> getTransactionByDate(LocalDateTime startDate, LocalDateTime endDate);

    List<Transaction> findByTransactionType(String transactionType);

    @Query("SELECT t FROM Transaction t WHERE t.amount BETWEEN ?1 AND ?2")
    List<Transaction> getTransactionByAmountBetween(Double amount1, Double amount2);

}
