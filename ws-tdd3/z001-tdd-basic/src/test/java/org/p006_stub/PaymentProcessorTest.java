package org.p006_stub;
/*
### Example 2: Payment Processing Fee Calculation

**Task Requirement:**
Write `PaymentProcessor` that adds a transaction fee to payments
based on user’s account type from an external dependency `AccountTypeProvider`.
Write tests using a stub for `AccountTypeProvider` to simulate different account types.

 */


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

// Stub used where fee depends on account type.
// The stub class returns the type set in its constructor.

public class PaymentProcessorTest {
    // Tests correct fee for premium account
    @Test
    void appliesLowFeeForPremium() {
        AccountTypeProviderStub stub = new AccountTypeProviderStub("premium");
        PaymentProcessor processor = new PaymentProcessor(stub);
        assertEquals(102.0, processor.processPayment("u1", 100.0), 0.01);
    }

    // Tests correct fee for standard account
    @Test
    void appliesStandardFee() {
        AccountTypeProviderStub stub = new AccountTypeProviderStub("standard");
        PaymentProcessor processor = new PaymentProcessor(stub);
        assertEquals(105.0, processor.processPayment("u2", 100.0), 0.01);
    }
}


class AccountTypeProviderStub implements AccountTypeProvider {
    private final String type;

    public AccountTypeProviderStub(String type) {
        this.type = type;
    }

    @Override
    public String getAccountType(String userId) {
        return type;
    }
}

