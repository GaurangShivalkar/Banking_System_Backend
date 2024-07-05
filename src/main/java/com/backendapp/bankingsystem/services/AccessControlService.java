package com.backendapp.bankingsystem.services;

import com.backendapp.bankingsystem.models.Account;
import com.backendapp.bankingsystem.models.Customer;
import org.springframework.stereotype.Service;

@Service
public class AccessControlService {

    public boolean canLogin(Customer customer, Account account) {
        return isKYCVerified(customer) && isAccountActiveOrDormant(account);
    }

    public boolean canCreateAccount(Customer customer) {
        return isKYCVerified(customer);
    }

    public boolean canViewAccount(Customer customer, Account account) {
        return isKYCVerified(customer) && isAccountActiveOrDormant(account);
    }

    public boolean canChangeUpdatePassword(Customer customer, Account account) {
        return isKYCVerified(customer) && isAccountActiveOrDormant(account);
    }

    public boolean canMakeTransactions(Customer customer, Account account) {
        return isKYCVerified(customer) && isAccountActive(account);
    }

    public boolean canAddBeneficiaries(Customer customer, Account account) {
        return isKYCVerified(customer) && isAccountActive(account);
    }

    private boolean isKYCVerified(Customer customer) {
        return "VERIFIED".equals(customer.getStatus());
    }

    private boolean isAccountActive(Account account) {
        return "ACTIVE".equals(account.getAccountStatus());
    }

    private boolean isAccountActiveOrDormant(Account account) {
        return "ACTIVE".equals(account.getAccountStatus()) || "DORMANT".equals(account.getAccountStatus());
    }
}
