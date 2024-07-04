package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.repositories.AccountRepository;
import com.backendapp.bankingsystem.repositories.TransactionRepository;
import com.backendapp.bankingsystem.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AdminService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    public long getUserCount() {
        return userRepository.countUsers();
    }

    public long getTransactionCount() {
        return transactionRepository.transactionCount();
    }

    public long getAccountBalance() {
        return accountRepository.totalAmountOfBalance();
    }

    public long getAccountCount() {
        return accountRepository.totalAccount();
    }
//    public List<Map<String, Object>> getTransactionCountsByMonth() {
//        return transactionRepository.getTransactionCountsByMonth();
//    }

    public List<Map<String, Object>> getTransactionCountsByMonth() {
        List<Object[]> results = transactionRepository.getTransactionCountsByMonth();
        List<Map<String, Object>> transactionCounts = new ArrayList<>();

        for (Object[] result : results) {
            Map<String, Object> countMap = new HashMap<>();
            countMap.put("month", result[0]);
            countMap.put("transactionCount", result[1]);
            transactionCounts.add(countMap);
        }

        return transactionCounts;
    }

    public Map<String, Long> getTransactionTypeCounts() {
        List<Object[]> counts = transactionRepository.countTransactionTypes();
        Map<String, Long> result = new HashMap<>();

        if (!counts.isEmpty()) {
            Object[] countArray = counts.get(0);
            result.put("RTGS", ((Number) countArray[0]).longValue());
            result.put("NEFT", ((Number) countArray[1]).longValue());
            result.put("IMPS", ((Number) countArray[2]).longValue());
            result.put("SELF", ((Number) countArray[3]).longValue());
            result.put("OTHER", ((Number) countArray[4]).longValue());
        }

        return result;
    }
}
