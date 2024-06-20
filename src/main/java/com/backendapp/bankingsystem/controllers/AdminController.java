package com.backendapp.bankingsystem.controllers;

import com.backendapp.bankingsystem.repositories.TransactionRepository;
import com.backendapp.bankingsystem.services.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private TransactionRepository transactionRepository;


    @GetMapping("/getTotal")
    public ArrayList<Long> getTotalUsers() {
        ArrayList<Long> totalCount = new ArrayList<>();
        totalCount.add(adminService.getUserCount());
        totalCount.add(adminService.getTransactionCount());
        totalCount.add(adminService.getAccountBalance());
        totalCount.add(adminService.getAccountCount());
        return totalCount;
    }

    @GetMapping("/transactionsMonthWise")
    public List<Map<String, Object>> getTransactionCounts() {
        return adminService.getTransactionCountsByMonth();
    }

    @GetMapping("/transactionTypeCount")
    public Map<String, Long> getTransactionTypeCount() {
        Map<String, Long> counts = adminService.getTransactionTypeCounts();
        return counts;
    }
}
