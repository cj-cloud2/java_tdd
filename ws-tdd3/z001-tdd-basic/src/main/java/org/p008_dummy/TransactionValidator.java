package org.p008_dummy;

// Service Class
public class TransactionValidator {
    private final TransactionLogger logger;
    public TransactionValidator(TransactionLogger logger) {
        this.logger = logger; // accepts logger but doesn't use it
    }
    public boolean validate(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Negative transaction");
        return true;
    }
}

// The interface to be dummied
interface TransactionLogger {
    void log(String message);
}