# Mockito Assignment 07: Banking Day End Procedure - TDD with Spy

## Concept of Mockito.spy()

**Mockito.spy()** is a testing technique that creates a **partial mock** of an existing object. Unlike regular mocks that replace all method behaviors with defaults, a spy wraps a real object and allows you to:

- **Execute real methods** by default unless explicitly mocked
- **Mock specific methods** while keeping others functional
- **Verify interactions** with both real and mocked methods
- **Pass actual class instances** instead of interfaces to Mockito

Key characteristics of spies:

- **Partial mocking**: Some methods run real code, others can be stubbed
- **Real object wrapping**: Spies wrap existing objects rather than creating entirely fake ones
- **Selective stubbing**: You can choose which methods to mock and which to leave real


## Business Requirements

### Banking Day End Procedure Domain

**Business Context**: At the end of each banking day, financial institutions must perform critical reconciliation and reporting procedures to ensure data integrity, regulatory compliance, and accurate financial reporting.

**Core Business Requirement**:
Implement an automated day-end processing system that handles account balance reconciliation, transaction validation, and generates end-of-day reports while ensuring data consistency and audit trail compliance.

## Testable Requirements

### Requirement 1: Account Balance Reconciliation

**Description**: The system must reconcile account balances by calculating the final balance based on daily transactions and verify against expected totals.
**Testable Criteria**: Given a list of transactions, calculate final balance and mark reconciliation status as successful or failed.

### Requirement 2: Transaction Validation

**Description**: The system must validate all transactions processed during the day to ensure they meet business rules and regulatory compliance.
**Testable Criteria**: Validate transaction amounts, dates, and account status, returning validation results with error details for failed transactions.

### Requirement 3: End-of-Day Report Generation

**Description**: The system must generate comprehensive end-of-day reports including account summaries, transaction counts, and reconciliation status.
**Testable Criteria**: Generate reports with accurate totals, transaction counts, and status indicators based on processed data.

## Step-by-Step TDD Guide with RED-GREEN-REFACTOR Cycles

### Features Overview

**Mockito.spy() Features Used**:

- **Partial Method Mocking**: Some methods will execute real logic while others are mocked
- **Real Object Verification**: Verify interactions with both real and mocked methods
- **Class-based Spying**: Pass concrete classes instead of interfaces to demonstrate spy capabilities

***

## Pass 1: Account Balance Reconciliation (RED-GREEN-REFACTOR)

### RED Phase - Write Failing Test

**Test Requirements**: Test that account balance reconciliation calculates correct final balance and updates reconciliation status.

**Create Test File**: `src/test/java/edu/m007/DayEndProcessorTest.java`

```java
package edu.m007;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DayEndProcessorTest {

    // Create spy annotation for the service we will test
    @Spy
    private DayEndProcessor dayEndProcessor;

    @BeforeEach
    void setUp() {
        // Initialize the spy with real object for testing
        dayEndProcessor = spy(new DayEndProcessor());
    }

    @Test
    void testAccountBalanceReconciliation_ShouldCalculateCorrectFinalBalance() {
        // Arrange - Set up test data for account reconciliation
        String accountId = "ACC123";
        double initialBalance = 1000.0;
        List<Transaction> transactions = Arrays.asList(
            new Transaction("TXN001", 500.0, "DEPOSIT"),
            new Transaction("TXN002", -200.0, "WITHDRAWAL")
        );
        
        // Mock the external validation method but keep calculation real
        doReturn(true).when(dayEndProcessor).validateAccountStatus(accountId);
        
        // Act - Execute the method under test
        ReconciliationResult result = dayEndProcessor.reconcileAccountBalance(accountId, initialBalance, transactions);
        
        // Assert - Verify the expected behavior
        assertNotNull(result, "Reconciliation result should not be null");
        assertEquals(1300.0, result.getFinalBalance(), "Final balance should be 1300.0");
        assertEquals("SUCCESS", result.getStatus(), "Reconciliation status should be SUCCESS");
        
        // Verify spy interactions - check that validation was called
        verify(dayEndProcessor).validateAccountStatus(accountId);
        verify(dayEndProcessor).reconcileAccountBalance(accountId, initialBalance, transactions);
    }
}
```


### GREEN Phase - Minimal Implementation

**Create Main Class**: `src/main/java/edu/m007/DayEndProcessor.java`

```java
package edu.m007;

import java.util.List;

// Main service class for banking day-end processing
public class DayEndProcessor {
    
    // Method for reconciling account balance - real implementation
    public ReconciliationResult reconcileAccountBalance(String accountId, double initialBalance, List<Transaction> transactions) {
        // Calculate final balance by processing all transactions
        double finalBalance = initialBalance;
        
        // Process each transaction to calculate final balance
        for (Transaction transaction : transactions) {
            finalBalance += transaction.getAmount();
        }
        
        // Validate account status before finalizing reconciliation
        boolean isAccountValid = validateAccountStatus(accountId);
        
        // Create result based on validation and calculation
        String status = isAccountValid ? "SUCCESS" : "FAILED";
        
        // Return reconciliation result with calculated balance and status
        return new ReconciliationResult(finalBalance, status);
    }
    
    // Method that will be mocked in tests - external validation
    public boolean validateAccountStatus(String accountId) {
        // This method will be spied and mocked in tests
        // Real implementation would check external systems
        return true;
    }
}
```

**Create Supporting Classes**:

`src/main/java/edu/m007/Transaction.java`

```java
package edu.m007;

// Transaction data model for banking operations
public class Transaction {
    private String transactionId;
    private double amount;
    private String type;
    
    // Constructor to initialize transaction with all required fields
    public Transaction(String transactionId, double amount, String type) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.type = type;
    }
    
    // Getter method for transaction amount
    public double getAmount() {
        return amount;
    }
    
    // Getter method for transaction ID
    public String getTransactionId() {
        return transactionId;
    }
    
    // Getter method for transaction type
    public String getType() {
        return type;
    }
}
```

`src/main/java/edu/m007/ReconciliationResult.java`

```java
package edu.m007;

// Result model for account balance reconciliation
public class ReconciliationResult {
    private double finalBalance;
    private String status;
    
    // Constructor to initialize reconciliation result
    public ReconciliationResult(double finalBalance, String status) {
        this.finalBalance = finalBalance;
        this.status = status;
    }
    
    // Getter method for final calculated balance
    public double getFinalBalance() {
        return finalBalance;
    }
    
    // Getter method for reconciliation status
    public String getStatus() {
        return status;
    }
}
```


### REFACTOR Phase - Improve Code Quality

The code is clean and follows single responsibility principle. The spy allows testing real calculation logic while mocking external dependencies.

***

## Pass 2: Transaction Validation (RED-GREEN-REFACTOR)

### RED Phase - Add Transaction Validation Test

**Update Test File**: `src/test/java/edu/m007/DayEndProcessorTest.java`

```java
package edu.m007;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DayEndProcessorTest {

    // Create spy annotation for the service we will test
    @Spy
    private DayEndProcessor dayEndProcessor;

    @BeforeEach
    void setUp() {
        // Initialize the spy with real object for testing
        dayEndProcessor = spy(new DayEndProcessor());
    }

    @Test
    void testAccountBalanceReconciliation_ShouldCalculateCorrectFinalBalance() {
        // Arrange - Set up test data for account reconciliation
        String accountId = "ACC123";
        double initialBalance = 1000.0;
        List<Transaction> transactions = Arrays.asList(
            new Transaction("TXN001", 500.0, "DEPOSIT"),
            new Transaction("TXN002", -200.0, "WITHDRAWAL")
        );
        
        // Mock the external validation method but keep calculation real
        doReturn(true).when(dayEndProcessor).validateAccountStatus(accountId);
        
        // Act - Execute the method under test
        ReconciliationResult result = dayEndProcessor.reconcileAccountBalance(accountId, initialBalance, transactions);
        
        // Assert - Verify the expected behavior
        assertNotNull(result, "Reconciliation result should not be null");
        assertEquals(1300.0, result.getFinalBalance(), "Final balance should be 1300.0");
        assertEquals("SUCCESS", result.getStatus(), "Reconciliation status should be SUCCESS");
        
        // Verify spy interactions - check that validation was called
        verify(dayEndProcessor).validateAccountStatus(accountId);
        verify(dayEndProcessor).reconcileAccountBalance(accountId, initialBalance, transactions);
    }

    // NEW TEST FOR PASS 2
    @Test
    void testTransactionValidation_ShouldValidateAllTransactionsCorrectly() {
        // Arrange - Set up test data for transaction validation
        List<Transaction> transactions = Arrays.asList(
            new Transaction("TXN001", 500.0, "DEPOSIT"),
            new Transaction("TXN002", -200.0, "WITHDRAWAL"),
            new Transaction("TXN003", 1000.0, "DEPOSIT")
        );
        
        // Mock external regulatory compliance check but keep business logic real
        doReturn(true).when(dayEndProcessor).checkRegulatoryCompliance(any(Transaction.class));
        // Mock external fraud detection but keep validation logic real
        doReturn(false).when(dayEndProcessor).detectFraud(any(Transaction.class));
        
        // Act - Execute transaction validation method
        ValidationResult result = dayEndProcessor.validateTransactions(transactions);
        
        // Assert - Verify validation results
        assertNotNull(result, "Validation result should not be null");
        assertEquals(3, result.getValidatedCount(), "Should validate all 3 transactions");
        assertEquals(0, result.getFailedCount(), "Should have 0 failed transactions");
        assertEquals("VALID", result.getOverallStatus(), "Overall status should be VALID");
        
        // Verify spy interactions with mocked methods
        verify(dayEndProcessor, times(3)).checkRegulatoryCompliance(any(Transaction.class));
        verify(dayEndProcessor, times(3)).detectFraud(any(Transaction.class));
        // Verify real method was called
        verify(dayEndProcessor).validateTransactions(transactions);
    }
}
```


### GREEN Phase - Add Transaction Validation Implementation

**Update Main Class**: `src/main/java/edu/m007/DayEndProcessor.java`

```java
package edu.m007;

import java.util.List;

// Main service class for banking day-end processing
public class DayEndProcessor {
    
    // Method for reconciling account balance - real implementation
    public ReconciliationResult reconcileAccountBalance(String accountId, double initialBalance, List<Transaction> transactions) {
        // Calculate final balance by processing all transactions
        double finalBalance = initialBalance;
        
        // Process each transaction to calculate final balance
        for (Transaction transaction : transactions) {
            finalBalance += transaction.getAmount();
        }
        
        // Validate account status before finalizing reconciliation
        boolean isAccountValid = validateAccountStatus(accountId);
        
        // Create result based on validation and calculation
        String status = isAccountValid ? "SUCCESS" : "FAILED";
        
        // Return reconciliation result with calculated balance and status
        return new ReconciliationResult(finalBalance, status);
    }
    
    // Method that will be mocked in tests - external validation
    public boolean validateAccountStatus(String accountId) {
        // This method will be spied and mocked in tests
        // Real implementation would check external systems
        return true;
    }
    
    // NEW METHOD FOR PASS 2 - Transaction validation with real business logic
    public ValidationResult validateTransactions(List<Transaction> transactions) {
        // Initialize counters for validation tracking
        int validatedCount = 0;
        int failedCount = 0;
        
        // Process each transaction through validation pipeline
        for (Transaction transaction : transactions) {
            // Check regulatory compliance - this method will be mocked
            boolean isCompliant = checkRegulatoryCompliance(transaction);
            
            // Detect fraud patterns - this method will be mocked
            boolean isFraudulent = detectFraud(transaction);
            
            // Apply real business logic for validation
            if (isCompliant && !isFraudulent && transaction.getAmount() != 0) {
                validatedCount++;
            } else {
                failedCount++;
            }
        }
        
        // Determine overall status based on validation results
        String overallStatus = (failedCount == 0) ? "VALID" : "INVALID";
        
        // Return validation result with counts and status
        return new ValidationResult(validatedCount, failedCount, overallStatus);
    }
    
    // Method to be mocked - external regulatory compliance check
    public boolean checkRegulatoryCompliance(Transaction transaction) {
        // This will be mocked in tests - real implementation would check external regulations
        return true;
    }
    
    // Method to be mocked - external fraud detection
    public boolean detectFraud(Transaction transaction) {
        // This will be mocked in tests - real implementation would use ML models
        return false;
    }
}
```

**Create Supporting Class**: `src/main/java/edu/m007/ValidationResult.java`

```java
package edu.m007;

// Result model for transaction validation process
public class ValidationResult {
    private int validatedCount;
    private int failedCount;
    private String overallStatus;
    
    // Constructor to initialize validation result with all fields
    public ValidationResult(int validatedCount, int failedCount, String overallStatus) {
        this.validatedCount = validatedCount;
        this.failedCount = failedCount;
        this.overallStatus = overallStatus;
    }
    
    // Getter method for count of successfully validated transactions
    public int getValidatedCount() {
        return validatedCount;
    }
    
    // Getter method for count of failed validations
    public int getFailedCount() {
        return failedCount;
    }
    
    // Getter method for overall validation status
    public String getOverallStatus() {
        return overallStatus;
    }
}
```


### REFACTOR Phase - Clean Up Code

The validation logic is properly separated with external dependencies mocked while keeping core business logic real.

***

## Pass 3: End-of-Day Report Generation (RED-GREEN-REFACTOR)

### RED Phase - Add Report Generation Test

**Update Test File**: `src/test/java/edu/m007/DayEndProcessorTest.java`

```java
package edu.m007;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import java.util.Arrays;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class DayEndProcessorTest {

    // Create spy annotation for the service we will test
    @Spy
    private DayEndProcessor dayEndProcessor;

    @BeforeEach
    void setUp() {
        // Initialize the spy with real object for testing
        dayEndProcessor = spy(new DayEndProcessor());
    }

    @Test
    void testAccountBalanceReconciliation_ShouldCalculateCorrectFinalBalance() {
        // Arrange - Set up test data for account reconciliation
        String accountId = "ACC123";
        double initialBalance = 1000.0;
        List<Transaction> transactions = Arrays.asList(
            new Transaction("TXN001", 500.0, "DEPOSIT"),
            new Transaction("TXN002", -200.0, "WITHDRAWAL")
        );
        
        // Mock the external validation method but keep calculation real
        doReturn(true).when(dayEndProcessor).validateAccountStatus(accountId);
        
        // Act - Execute the method under test
        ReconciliationResult result = dayEndProcessor.reconcileAccountBalance(accountId, initialBalance, transactions);
        
        // Assert - Verify the expected behavior
        assertNotNull(result, "Reconciliation result should not be null");
        assertEquals(1300.0, result.getFinalBalance(), "Final balance should be 1300.0");
        assertEquals("SUCCESS", result.getStatus(), "Reconciliation status should be SUCCESS");
        
        // Verify spy interactions - check that validation was called
        verify(dayEndProcessor).validateAccountStatus(accountId);
        verify(dayEndProcessor).reconcileAccountBalance(accountId, initialBalance, transactions);
    }

    @Test
    void testTransactionValidation_ShouldValidateAllTransactionsCorrectly() {
        // Arrange - Set up test data for transaction validation
        List<Transaction> transactions = Arrays.asList(
            new Transaction("TXN001", 500.0, "DEPOSIT"),
            new Transaction("TXN002", -200.0, "WITHDRAWAL"),
            new Transaction("TXN003", 1000.0, "DEPOSIT")
        );
        
        // Mock external regulatory compliance check but keep business logic real
        doReturn(true).when(dayEndProcessor).checkRegulatoryCompliance(any(Transaction.class));
        // Mock external fraud detection but keep validation logic real
        doReturn(false).when(dayEndProcessor).detectFraud(any(Transaction.class));
        
        // Act - Execute transaction validation method
        ValidationResult result = dayEndProcessor.validateTransactions(transactions);
        
        // Assert - Verify validation results
        assertNotNull(result, "Validation result should not be null");
        assertEquals(3, result.getValidatedCount(), "Should validate all 3 transactions");
        assertEquals(0, result.getFailedCount(), "Should have 0 failed transactions");
        assertEquals("VALID", result.getOverallStatus(), "Overall status should be VALID");
        
        // Verify spy interactions with mocked methods
        verify(dayEndProcessor, times(3)).checkRegulatoryCompliance(any(Transaction.class));
        verify(dayEndProcessor, times(3)).detectFraud(any(Transaction.class));
        // Verify real method was called
        verify(dayEndProcessor).validateTransactions(transactions);
    }

    // NEW TEST FOR PASS 3
    @Test
    void testEndOfDayReportGeneration_ShouldGenerateCompleteReport() {
        // Arrange - Set up test data for report generation
        String accountId = "ACC123";
        double initialBalance = 1000.0;
        List<Transaction> transactions = Arrays.asList(
            new Transaction("TXN001", 500.0, "DEPOSIT"),
            new Transaction("TXN002", -200.0, "WITHDRAWAL"),
            new Transaction("TXN003", 300.0, "DEPOSIT")
        );
        
        // Mock external report formatting but keep calculation logic real
        doReturn("FORMATTED_HEADER").when(dayEndProcessor).formatReportHeader();
        // Mock external report persistence but keep generation logic real
        doReturn(true).when(dayEndProcessor).persistReport(any(DayEndReport.class));
        
        // Act - Execute report generation method
        DayEndReport report = dayEndProcessor.generateDayEndReport(accountId, initialBalance, transactions);
        
        // Assert - Verify report generation results
        assertNotNull(report, "Day end report should not be null");
        assertEquals(accountId, report.getAccountId(), "Account ID should match");
        assertEquals(1600.0, report.getFinalBalance(), "Final balance should be 1600.0");
        assertEquals(3, report.getTransactionCount(), "Should have 3 transactions");
        assertEquals("COMPLETED", report.getReportStatus(), "Report status should be COMPLETED");
        
        // Verify spy interactions with mocked and real methods
        verify(dayEndProcessor).formatReportHeader();
        verify(dayEndProcessor).persistReport(any(DayEndReport.class));
        // Verify real method was called
        verify(dayEndProcessor).generateDayEndReport(accountId, initialBalance, transactions);
    }
}
```


### GREEN Phase - Add Report Generation Implementation

**Update Main Class**: `src/main/java/edu/m007/DayEndProcessor.java`

```java
package edu.m007;

import java.util.List;

// Main service class for banking day-end processing
public class DayEndProcessor {
    
    // Method for reconciling account balance - real implementation
    public ReconciliationResult reconcileAccountBalance(String accountId, double initialBalance, List<Transaction> transactions) {
        // Calculate final balance by processing all transactions
        double finalBalance = initialBalance;
        
        // Process each transaction to calculate final balance
        for (Transaction transaction : transactions) {
            finalBalance += transaction.getAmount();
        }
        
        // Validate account status before finalizing reconciliation
        boolean isAccountValid = validateAccountStatus(accountId);
        
        // Create result based on validation and calculation
        String status = isAccountValid ? "SUCCESS" : "FAILED";
        
        // Return reconciliation result with calculated balance and status
        return new ReconciliationResult(finalBalance, status);
    }
    
    // Method that will be mocked in tests - external validation
    public boolean validateAccountStatus(String accountId) {
        // This method will be spied and mocked in tests
        // Real implementation would check external systems
        return true;
    }
    
    // Transaction validation with real business logic
    public ValidationResult validateTransactions(List<Transaction> transactions) {
        // Initialize counters for validation tracking
        int validatedCount = 0;
        int failedCount = 0;
        
        // Process each transaction through validation pipeline
        for (Transaction transaction : transactions) {
            // Check regulatory compliance - this method will be mocked
            boolean isCompliant = checkRegulatoryCompliance(transaction);
            
            // Detect fraud patterns - this method will be mocked
            boolean isFraudulent = detectFraud(transaction);
            
            // Apply real business logic for validation
            if (isCompliant && !isFraudulent && transaction.getAmount() != 0) {
                validatedCount++;
            } else {
                failedCount++;
            }
        }
        
        // Determine overall status based on validation results
        String overallStatus = (failedCount == 0) ? "VALID" : "INVALID";
        
        // Return validation result with counts and status
        return new ValidationResult(validatedCount, failedCount, overallStatus);
    }
    
    // Method to be mocked - external regulatory compliance check
    public boolean checkRegulatoryCompliance(Transaction transaction) {
        // This will be mocked in tests - real implementation would check external regulations
        return true;
    }
    
    // Method to be mocked - external fraud detection
    public boolean detectFraud(Transaction transaction) {
        // This will be mocked in tests - real implementation would use ML models
        return false;
    }
    
    // NEW METHOD FOR PASS 3 - End of day report generation with real business logic
    public DayEndReport generateDayEndReport(String accountId, double initialBalance, List<Transaction> transactions) {
        // Calculate final balance using real business logic
        double finalBalance = initialBalance;
        
        // Process all transactions to compute final balance
        for (Transaction transaction : transactions) {
            finalBalance += transaction.getAmount();
        }
        
        // Count total transactions processed
        int transactionCount = transactions.size();
        
        // Format report header - this method will be mocked
        String formattedHeader = formatReportHeader();
        
        // Create day end report with calculated values
        DayEndReport report = new DayEndReport(accountId, finalBalance, transactionCount, "COMPLETED");
        
        // Persist report to external storage - this method will be mocked
        boolean persistSuccess = persistReport(report);
        
        // Update report status based on persistence result
        if (!persistSuccess) {
            report = new DayEndReport(accountId, finalBalance, transactionCount, "FAILED");
        }
        
        // Return completed day end report
        return report;
    }
    
    // Method to be mocked - external report header formatting
    public String formatReportHeader() {
        // This will be mocked in tests - real implementation would format complex headers
        return "DEFAULT_HEADER";
    }
    
    // Method to be mocked - external report persistence
    public boolean persistReport(DayEndReport report) {
        // This will be mocked in tests - real implementation would save to database/file
        return true;
    }
}
```

**Create Supporting Class**: `src/main/java/edu/m007/DayEndReport.java`

```java
package edu.m007;

// Report model for end-of-day banking summary
public class DayEndReport {
    private String accountId;
    private double finalBalance;
    private int transactionCount;
    private String reportStatus;
    
    // Constructor to initialize day end report with all required fields
    public DayEndReport(String accountId, double finalBalance, int transactionCount, String reportStatus) {
        this.accountId = accountId;
        this.finalBalance = finalBalance;
        this.transactionCount = transactionCount;
        this.reportStatus = reportStatus;
    }
    
    // Getter method for account identifier
    public String getAccountId() {
        return accountId;
    }
    
    // Getter method for calculated final balance
    public double getFinalBalance() {
        return finalBalance;
    }
    
    // Getter method for total transaction count
    public int getTransactionCount() {
        return transactionCount;
    }
    
    // Getter method for report generation status
    public String getReportStatus() {
        return reportStatus;
    }
}
```


### REFACTOR Phase - Final Code Review

The implementation demonstrates proper use of Mockito.spy() by:

- **Real business logic execution**: Core calculations and validations run with actual code
- **Selective mocking**: External dependencies like compliance checks and persistence are mocked
- **Verification capabilities**: Both mocked and real method calls are verified
- **Class-based spying**: Uses concrete classes instead of interfaces to show spy flexibility


## Summary

This assignment demonstrates Test-Driven Development using JUnit 5 and Mockito 5 with the spy pattern in a banking day-end procedure context. The implementation showcases how spies allow testing real business logic while mocking external dependencies, providing a balanced approach between unit testing isolation and integration testing realism.