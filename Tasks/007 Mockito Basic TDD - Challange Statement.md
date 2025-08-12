
# Mockito Basic Examples: Step-by-Step RED-GREEN-REFACTOR Assignments Guide

# Summary

This step-by-step approach introduces students to:

- Writing failing test cases first (RED).
- Implementing minimal code to pass tests (GREEN).
- Refactoring with edge cases, validation, and full test coverage (REFACTOR).

***

## Assignment 1: Loan Approval Service Testing (Banking Domain)

### Objective:

Test a Loan Approval Service that approves loans based on credit score and annual income using mocks for credit score service and audit logger.

***

### Step 1: RED - Write a failing test

- Create test class `LoanApprovalServiceTest`.
- Mock `CreditScoreService` and `AuditLogger`.
- Inject mocks into `LoanApprovalService`.
- Write a test case `approveLoan_Approved` that expects approval when credit score ≥ 700 and income ≥ 50,000.
- Stub the credit score service to return 750.
- The test should fail if implemented loan approval logic is missing or erroneous.

```java
// LoanApprovalServiceTest.java - Initial test class (RED step)

// LoanApprovalServiceTest.java (RED step)
package org.p009_mocks;

// Import statements for testing libraries
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Enable Mockito framework for JUnit 5 tests
@ExtendWith(MockitoExtension.class)
class LoanApprovalServiceTest {

    // MOCK: Create mock instance of CreditScoreService
    @Mock
    CreditScoreService creditScoreService;

    // MOCK: Create mock instance of AuditLogger
    @Mock
    AuditLogger auditLogger;

    // INJECT: Automatically inject mocks into LoanApprovalService
    @InjectMocks
    LoanApprovalService loanApprovalService;

    @Test
    void approveLoan_Approved() {
        // STUB: Configure creditScoreService mock to return 750 for any user ID
        
        // EXECUTE: Call approveLoan() with userId "user123" and annualIncome 60000
        
        // ASSERT: Verify return value is true (loan approved)
        
        // VERIFY: Confirm auditLogger.logApproval() was called with ("user123", true)
    }
}
```


***

### Step 2: GREEN - Write the minimal code to pass the test

- Implement `LoanApprovalService` to approve when credit score ≥ 700 and income ≥ 50,000.
- Log the approval using `AuditLogger`.

```java
// LoanApprovalService.java - Minimal implementation (GREEN step)
// LoanApprovalService.java (GREEN step)
package org.p009_mocks;

public class LoanApprovalService {

    // DECLARE: Private field for CreditScoreService dependency
    
    // DECLARE: Private field for AuditLogger dependency

    // CONSTRUCTOR: Initialize both dependencies via parameters
    
    public boolean approveLoan(String userId, double annualIncome) {
        // FETCH: Get credit score using userId from creditScoreService
        
        // DECIDE: Set approval status (true if credit ≥700 AND income ≥50000)
        
        // LOG: Record decision using auditLogger.logApproval()
        
        // RETURN: Approval status
    }
}
```


***

### Step 3: REFACTOR - Add edge cases and cleanup code

- Add tests for rejection due to low income and invalid inputs.
- Enhance `LoanApprovalService` to validate inputs and throw exceptions where needed.
- Verify audit logging correctly reflects approval or rejection.
- Use `verifyNoInteractions` to ensure no calls are made on invalid inputs.

```java
// Complete LoanApprovalServiceTest.java with edge cases and validations (REFACTOR step)
// LoanApprovalServiceTest.java (REFACTOR step)
package org.p009_mocks;

// Import testing/Mockito libraries
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanApprovalServiceTest {

    // MOCKS: Same as RED step
    @Mock CreditScoreService creditScoreService;
    @Mock AuditLogger auditLogger;
    @InjectMocks LoanApprovalService loanApprovalService;

    @Test
    void approveLoan_Approved() {
        // TEST: Approval with valid inputs (credit=750, income=60K)
        // STUB creditScoreService to return 750
        // EXECUTE with userId "user123" and income 60000
        // ASSERT true returned
        // VERIFY audit logged approval
    }

    @Test
    void approveLoan_RejectedDueToLowIncome() {
        // TEST: Rejection when income too low (45K)
        // STUB creditScoreService to return 750
        // EXECUTE with income 45000
        // ASSERT false returned
        // VERIFY audit logged rejection
    }

    @Test
    void approveLoan_InvalidInput() {
        // TEST: Exception with negative income
        // EXECUTE with income -100
        // ASSERT IllegalArgumentException thrown
        // VERIFY no interactions with mocks
    }
}
```

```java
// Updated LoanApprovalService.java with validations (REFACTOR step)
// LoanApprovalService.java (REFACTOR step)
package org.p009_mocks;

public class LoanApprovalService {

    // FIELDS & CONSTRUCTOR: Same as GREEN step
    
    public boolean approveLoan(String userId, double annualIncome) {
        // VALIDATE: If income ≤0, throw IllegalArgumentException
        
        // FETCH: Get credit score from service
        
        // VALIDATE: If credit score ≤0, throw IllegalArgumentException
        
        // DECIDE: Approval status (credit≥700 AND income≥50000)
        
        // LOG: Decision via auditLogger
        
        // RETURN: Approval status
    }
}
```


***

## Assignment 2: Order Processor Testing (Ecommerce Domain)

Follow the same RED-GREEN-REFACTOR approach for testing an order processor calculating total cost with discount logic and input validations.

***

### Step 1: RED - Write failing tests for calculation

```java
// OrderProcessorTest.java (RED step)
// OrderProcessorTest.java (RED step)
package org.p009_mocks;

// Import necessary testing libraries
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Enable Mockito framework for JUnit 5 tests
@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    // MOCK: Create mock instance of Product
    @Mock
    Product product;

    // INJECT: Automatically inject mock into OrderProcessor
    @InjectMocks
    OrderProcessor orderProcessor;

    @Test
    void calculateTotalCost_NoDiscount() {
        // STUB: Configure product mock to return unit price 100.0
        
        // EXECUTE: Call calculateTotalCost() with quantity 10
        
        // ASSERT: Verify result equals 1000.0 (100 * 10)
        
        // VERIFY: Confirm getUnitPrice() was called
    }
}
```


***

### Step 2: GREEN - Implement minimal calculation logic

```java
// OrderProcessor.java (GREEN step)
// OrderProcessor.java (GREEN step)
package org.p009_mocks;

public class OrderProcessor {

    public double calculateTotalCost(Product product, int quantity) {
        // CALCULATE: Return product unit price multiplied by quantity
    }
}
```


***

### Step 3: REFACTOR - Add discount logic, validations, and more tests

- Add discount for quantity ≥ 20.
- Validate inputs and throw exceptions if invalid.
- Add tests for discounted case and invalid inputs.

```java
// Complete OrderProcessorTest.java (REFACTOR step)
// OrderProcessorTest.java (REFACTOR step)
package org.p009_mocks;

// Import testing/Mockito libraries
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    // MOCKS: Same as RED step
    @Mock Product product;
    @InjectMocks OrderProcessor orderProcessor;

    @Test
    void calculateTotalCost_NoDiscount() {
        // TEST: No discount for quantity=10
        // STUB: product.getUnitPrice returns 100.0
        // EXECUTE: with quantity 10
        // ASSERT: total cost = 1000.0
    }

    @Test
    void calculateTotalCost_WithDiscount() {
        // TEST: 10% discount for quantity=25 (bulk order)
        // STUB: product.getUnitPrice returns 100.0
        // EXECUTE: with quantity 25
        // ASSERT: total cost = 2250.0 (100*25*0.9)
    }

    @Test
    void calculateTotalCost_InvalidPrice() {
        // TEST: Exception for negative unit price
        // STUB: product.getUnitPrice returns -50.0
        // EXECUTE: with quantity 5
        // ASSERT: IllegalArgumentException thrown
    }
}
```

```java
// Updated OrderProcessor.java (REFACTOR step)
// OrderProcessor.java (REFACTOR step)
package org.p009_mocks;

public class OrderProcessor {

    public double calculateTotalCost(Product product, int quantity) {
        // VALIDATE: If unit price ≤0 OR quantity ≤0, throw IllegalArgumentException
        
        // CALCULATE: total = unit price × quantity
        
        // APPLY DISCOUNT: If quantity ≥20, apply 10% discount (multiply total by 0.9)
        
        // RETURN: Final total cost
    }
}
```


***

## Assignment 3: Stock Trade Service Testing (Finance Domain)

Repeat RED-GREEN-REFACTOR cycle to test stock trade execution with validations, balance checks, and side effect verifications.

***

### Step 1: RED - Write failing test for successful trade

```java
// StockTradeServiceTest.java (RED step)

// StockTradeServiceTest.java (RED step)
package org.p009_mocks;

// Import necessary testing libraries
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

// Enable Mockito framework for JUnit 5 tests
@ExtendWith(MockitoExtension.class)
class StockTradeServiceTest {

    // MOCK: Create mock instance of UserAccountService
    @Mock
    UserAccountService userAccountService;

    // MOCK: Create mock instance of StockRepository
    @Mock
    StockRepository stockRepository;

    // INJECT: Automatically inject mocks into StockTradeService
    @InjectMocks
    StockTradeService stockTradeService;

    @Test
    void executeTrade_Success() throws TradeException {
        // STUB: Configure userAccountService to return balance 5000.0
        
        // EXECUTE: Call executeTrade() for "user1", "AAPL", 10 shares @ $100
        
        // VERIFY: userAccountService.deductBalance() called with ("user1", 1000.0)
        
        // VERIFY: stockRepository.updateStockQuantity() called with ("AAPL", 10)
    }
}
```


***

### Step 2: GREEN - Minimal implementation to pass the test

```java
// StockTradeService.java (GREEN step)

// StockTradeService.java (GREEN step)
package org.p009_mocks;

public class StockTradeService {

    // DECLARE: Private field for UserAccountService dependency
    
    // DECLARE: Private field for StockRepository dependency

    // CONSTRUCTOR: Initialize both dependencies via parameters
    
    public void executeTrade(String userId, String stockSymbol, int quantity, double price) throws TradeException {
        // CALCULATE: totalCost = quantity × price
        
        // FETCH: Get user balance from userAccountService
        
        // DEDUCT: Call deductBalance() with userId and totalCost
        
        // UPDATE: Call updateStockQuantity() with stockSymbol and quantity
    }
}
```


***

### Step 3: REFACTOR - Add validations, exceptions, negative scenario tests

- Validate inputs and throw `TradeException`.
- Check user balance and throw exception if insufficient.
- Add tests for exceptions and verify no side effects on failure.

```java
// Complete StockTradeServiceTest.java (REFACTOR step)

// StockTradeServiceTest.java (REFACTOR step)
package org.p009_mocks;

// Import testing/Mockito libraries
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockTradeServiceTest {

    // MOCKS: Same as RED step
    @Mock UserAccountService userAccountService;
    @Mock StockRepository stockRepository;
    @InjectMocks StockTradeService stockTradeService;

    @Test
    void executeTrade_Success() throws TradeException {
        // TEST: Successful trade execution
        // STUB: userAccountService.getUserBalance returns 5000.0
        // EXECUTE: ("user1", "AAPL", 10, 100.0)
        // VERIFY: Balance deduction and stock update occurred
    }

    @Test
    void executeTrade_InsufficientBalance() {
        // TEST: Trade with insufficient funds
        // STUB: userAccountService.getUserBalance returns 500.0
        // EXECUTE: ("user1", "AAPL", 10, 100.0)
        // ASSERT: TradeException thrown with message "Insufficient balance"
        // VERIFY: No stock repository interactions
    }

    @Test
    void executeTrade_InvalidStockPrice() {
        // TEST: Trade with negative stock price
        // EXECUTE: ("user1", "AAPL", 5, -10.0)
        // ASSERT: TradeException thrown
        // VERIFY: No interactions with any mocks
    }
}
```

```java
// Updated StockTradeService.java (REFACTOR step)

// StockTradeService.java (REFACTOR step)
package org.p009_mocks;

public class StockTradeService {

    // FIELDS & CONSTRUCTOR: Same as GREEN step
    
    public void executeTrade(String userId, String stockSymbol, int quantity, double price) throws TradeException {
        // VALIDATE: If quantity ≤0 OR price ≤0, throw TradeException("Invalid parameters")
        
        // CALCULATE: totalCost = quantity × price
        
        // FETCH: User balance from userAccountService
        
        // VALIDATE: If balance < totalCost, throw TradeException("Insufficient balance")
        
        // DEDUCT: Call deductBalance() with userId and totalCost
        
        // UPDATE: Call updateStockQuantity() with stockSymbol and quantity
    }
}
```


***


