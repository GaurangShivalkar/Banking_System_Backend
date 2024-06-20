package com.backendapp.bankingsystem.repositories;

import com.backendapp.bankingsystem.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findBySourceAccountIdOrDestinationAccountId(String sourceAccountId, String destinationAccountId);
    //Transaction findByTimestamp(String timestamp);
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

    @Query("SELECT DATE(timestamp) AS transactionDate, SUM(changedBalance) AS totalChangedBalance " +
            "FROM Transaction " +
            "WHERE sourceAccountId = ?1 " +
            "GROUP BY DATE(timestamp) " +
            "ORDER BY DATE(timestamp)")
    List<Object[]> getTotalChangedBalanceByDate(String sourceAccountId);
}
