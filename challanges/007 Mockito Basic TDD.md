
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
package org.p009_mocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanApprovalServiceTest {

    @Mock
    CreditScoreService creditScoreService;

    @Mock
    AuditLogger auditLogger;

    @InjectMocks
    LoanApprovalService loanApprovalService;

    @Test
    void approveLoan_Approved() {
        when(creditScoreService.getCreditScore(anyString())).thenReturn(750);
        boolean result = loanApprovalService.approveLoan("user123", 60000);
        assertTrue(result);
        verify(auditLogger).logApproval("user123", true);
    }
}
```


***

### Step 2: GREEN - Write the minimal code to pass the test

- Implement `LoanApprovalService` to approve when credit score ≥ 700 and income ≥ 50,000.
- Log the approval using `AuditLogger`.

```java
// LoanApprovalService.java - Minimal implementation (GREEN step)
package org.p009_mocks;

public class LoanApprovalService {

    private CreditScoreService creditScoreService;
    private AuditLogger auditLogger;

    public LoanApprovalService(CreditScoreService creditScoreService, AuditLogger auditLogger) {
        this.creditScoreService = creditScoreService;
        this.auditLogger = auditLogger;
    }

    public boolean approveLoan(String userId, double annualIncome) {
        int creditScore = creditScoreService.getCreditScore(userId);
        boolean approved = (creditScore >= 700 && annualIncome >= 50000);
        auditLogger.logApproval(userId, approved);
        return approved;
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
package org.p009_mocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanApprovalServiceTest {

    @Mock
    CreditScoreService creditScoreService;

    @Mock
    AuditLogger auditLogger;

    @InjectMocks
    LoanApprovalService loanApprovalService;

    @Test
    void approveLoan_Approved() {
        when(creditScoreService.getCreditScore(anyString())).thenReturn(750);
        boolean result = loanApprovalService.approveLoan("user123", 60000);
        assertTrue(result);
        verify(auditLogger).logApproval("user123", true);
    }

    @Test
    void approveLoan_RejectedDueToLowIncome() {
        when(creditScoreService.getCreditScore(anyString())).thenReturn(750);
        boolean result = loanApprovalService.approveLoan("user123", 45000);
        assertFalse(result);
        verify(auditLogger).logApproval("user123", false);
    }

    @Test
    void approveLoan_InvalidInput() {
        assertThrows(IllegalArgumentException.class,
            () -> loanApprovalService.approveLoan("user123", -100));
        verifyNoInteractions(creditScoreService, auditLogger);
    }
}
```

```java
// Updated LoanApprovalService.java with validations (REFACTOR step)
package org.p009_mocks;

public class LoanApprovalService {

    private CreditScoreService creditScoreService;
    private AuditLogger auditLogger;

    public LoanApprovalService(CreditScoreService creditScoreService, AuditLogger auditLogger) {
        this.creditScoreService = creditScoreService;
        this.auditLogger = auditLogger;
    }

    public boolean approveLoan(String userId, double annualIncome) {
        if (annualIncome <= 0) {
            throw new IllegalArgumentException("Income must be positive");
        }
        int creditScore = creditScoreService.getCreditScore(userId);
        if (creditScore <= 0) {
            throw new IllegalArgumentException("Invalid credit score");
        }
        boolean approved = (creditScore >= 700 && annualIncome >= 50000);
        auditLogger.logApproval(userId, approved);
        return approved;
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
package org.p009_mocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    @Mock
    Product product;

    @InjectMocks
    OrderProcessor orderProcessor;

    @Test
    void calculateTotalCost_NoDiscount() {
        when(product.getUnitPrice()).thenReturn(100.0);
        double result = orderProcessor.calculateTotalCost(product, 10);
        assertEquals(1000.0, result);
        verify(product, times(2)).getUnitPrice();
    }
}
```


***

### Step 2: GREEN - Implement minimal calculation logic

```java
// OrderProcessor.java (GREEN step)
package org.p009_mocks;

public class OrderProcessor {

    public double calculateTotalCost(Product product, int quantity) {
        return product.getUnitPrice() * quantity;
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
package org.p009_mocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    @Mock
    Product product;

    @InjectMocks
    OrderProcessor orderProcessor;

    @Test
    void calculateTotalCost_NoDiscount() {
        when(product.getUnitPrice()).thenReturn(100.0);
        double result = orderProcessor.calculateTotalCost(product, 10);
        assertEquals(1000.0, result);
        verify(product, times(2)).getUnitPrice();
    }

    @Test
    void calculateTotalCost_WithDiscount() {
        when(product.getUnitPrice()).thenReturn(100.0);
        double result = orderProcessor.calculateTotalCost(product, 25);
        assertEquals(2250.0, result);
    }

    @Test
    void calculateTotalCost_InvalidPrice() {
        when(product.getUnitPrice()).thenReturn(-50.0);
        assertThrows(IllegalArgumentException.class,
            () -> orderProcessor.calculateTotalCost(product, 5));
    }
}
```

```java
// Updated OrderProcessor.java (REFACTOR step)
package org.p009_mocks;

public class OrderProcessor {

    public double calculateTotalCost(Product product, int quantity) {
        if (product.getUnitPrice() <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Invalid input");
        }
        double total = product.getUnitPrice() * quantity;
        if (quantity >= 20) {
            total *= 0.9; // 10% discount
        }
        return total;
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
package org.p009_mocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockTradeServiceTest {

    @Mock
    UserAccountService userAccountService;

    @Mock
    StockRepository stockRepository;

    @InjectMocks
    StockTradeService stockTradeService;

    @Test
    void executeTrade_Success() throws TradeException {
        when(userAccountService.getUserBalance("user1")).thenReturn(5000.0);
        stockTradeService.executeTrade("user1", "AAPL", 10, 100.0);
        verify(userAccountService).deductBalance("user1", 1000.0);
        verify(stockRepository).updateStockQuantity("AAPL", 10);
    }
}
```


***

### Step 2: GREEN - Minimal implementation to pass the test

```java
// StockTradeService.java (GREEN step)
package org.p009_mocks;

public class StockTradeService {

    private UserAccountService userAccountService;
    private StockRepository stockRepository;

    public StockTradeService(UserAccountService userAccountService, StockRepository stockRepository) {
        this.userAccountService = userAccountService;
        this.stockRepository = stockRepository;
    }

    public void executeTrade(String userId, String stockSymbol, int quantity, double price) throws TradeException {
        double totalCost = quantity * price;
        double balance = userAccountService.getUserBalance(userId);
        userAccountService.deductBalance(userId, totalCost);
        stockRepository.updateStockQuantity(stockSymbol, quantity);
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
package org.p009_mocks;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockTradeServiceTest {

    @Mock
    UserAccountService userAccountService;

    @Mock
    StockRepository stockRepository;

    @InjectMocks
    StockTradeService stockTradeService;

    @Test
    void executeTrade_Success() throws TradeException {
        when(userAccountService.getUserBalance("user1")).thenReturn(5000.0);
        stockTradeService.executeTrade("user1", "AAPL", 10, 100.0);
        verify(userAccountService).deductBalance("user1", 1000.0);
        verify(stockRepository).updateStockQuantity("AAPL", 10);
    }

    @Test
    void executeTrade_InsufficientBalance() {
        when(userAccountService.getUserBalance("user1")).thenReturn(500.0);
        TradeException exception = assertThrows(TradeException.class,
            () -> stockTradeService.executeTrade("user1", "AAPL", 10, 100.0));
        assertEquals("Insufficient balance", exception.getMessage());
        verifyNoInteractions(stockRepository);
    }

    @Test
    void executeTrade_InvalidStockPrice() {
        assertThrows(TradeException.class,
            () -> stockTradeService.executeTrade("user1", "AAPL", 5, -10.0));
        verifyNoInteractions(userAccountService, stockRepository);
    }
}
```

```java
// Updated StockTradeService.java (REFACTOR step)
package org.p009_mocks;

public class StockTradeService {

    private UserAccountService userAccountService;
    private StockRepository stockRepository;

    public StockTradeService(UserAccountService userAccountService, StockRepository stockRepository) {
        this.userAccountService = userAccountService;
        this.stockRepository = stockRepository;
    }

    public void executeTrade(String userId, String stockSymbol, int quantity, double price) throws TradeException {
        if (quantity <= 0 || price <= 0) {
            throw new TradeException("Invalid trade parameters");
        }
        double totalCost = quantity * price;
        double balance = userAccountService.getUserBalance(userId);
        if (balance < totalCost) {
            throw new TradeException("Insufficient balance");
        }
        userAccountService.deductBalance(userId, totalCost);
        stockRepository.updateStockQuantity(stockSymbol, quantity);
    }
}
```


***


