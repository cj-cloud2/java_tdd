package org.p006_stub;

// Stub used where fee depends on account type.
// The stub class returns the type set in its constructor.

public class PaymentProcessor {
    private final AccountTypeProvider accountTypeProvider;

    public PaymentProcessor(AccountTypeProvider accountTypeProvider) {
        this.accountTypeProvider = accountTypeProvider;
    }

    public double processPayment(String userId, double amount) {
        String type = accountTypeProvider.getAccountType(userId);
        if ("premium".equals(type)) {
            return amount * 1.02; // 2% fee
        } else {
            return amount * 1.05; // 5% fee
        }
    }
}

interface AccountTypeProvider {
    String getAccountType(String userId);
}
