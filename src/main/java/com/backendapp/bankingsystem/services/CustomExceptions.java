package com.backendapp.bankingsystem.services;

public class CustomExceptions {
    public class InsufficientBalanceException extends RuntimeException {
        public InsufficientBalanceException(String message) {
            super(message);
        }
    }

    public class InvalidTransactionAmountException extends RuntimeException {
        public InvalidTransactionAmountException(String message) {
            super(message);
        }
    }

    public class BeneficiaryNotFoundException extends RuntimeException {
        public BeneficiaryNotFoundException(String message) {
            super(message);
        }
    }

    public class AccountNotFoundException extends RuntimeException {
        public AccountNotFoundException(String message) {
            super(message);
        }
    }

}
