package org.p008_dummy;

/*
## Example 1: Finance – Transaction Validator with Logging Dummy

### Task \& Requirements

1. **Task:** Validate bank transactions using a service; the service also receives a logger interface.
2. **Requirements:**
    - If transaction amount is negative, throw an exception.
    - Valid transactions should return true.
    - Logger is required but not used in validation logic (just a dependency).
 */
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// The purpose of these tests is to verify validation logic, not logging.

class TransactionValidatorTest {
    // Test for negative transaction throws exception.
    @Test
    void testNegativeAmountThrows() {
        TransactionLogger dummyLogger = new DummyTransactionLogger();
        TransactionValidator validator = new TransactionValidator(dummyLogger);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(-10));
    }

    // Test for valid transaction returns true.
    @Test
    void testValidAmountReturnsTrue() {
        TransactionLogger dummyLogger = new DummyTransactionLogger();
        TransactionValidator validator = new TransactionValidator(dummyLogger);
        assertTrue(validator.validate(100));
    }

    // Dummy is provided because TransactionValidator requires a logger in constructor,
    // but the logger is not used in any assertion or logic here.
}

// Dummy implementation for injecting into constructor
class DummyTransactionLogger implements TransactionLogger {
    public void log(String message) {} // No-op
}