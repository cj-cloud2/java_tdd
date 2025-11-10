# Mockito Assignment 06: Financial Reporting TDD with JUnit 5 and Mockito 5

## Overview

This assignment demonstrates Test-Driven Development (TDD) using JUnit 5 and Mockito 5 in the financial reporting domain for banking applications. The assignment follows the RED-GREEN-REFACTOR cycle across three requirements, each focusing on different Mockito features.

### Mockito Features Covered

**Built-in Argument Matchers**: Mockito provides flexible argument matching capabilities for testing method calls with dynamic or unpredictable inputs.

- `anyString()`: Matches any string value, useful for dynamic text inputs
- `eq()`: Matches exact values when mixing argument matchers
- `endsWith()`: Matches strings ending with specific suffixes

**Type-Based Matching**: The `isA()` matcher performs type checking to ensure objects are of expected types.

**Custom Argument Matching**: Advanced argument validation using custom logic to verify complex object states and business rules.

### TDD Red-Green-Refactor Cycle

The RED-GREEN-REFACTOR approach ensures systematic development with comprehensive test coverage :

- **RED**: Write failing tests that define expected behavior
- **GREEN**: Implement minimal code to make tests pass
- **REFACTOR**: Improve code quality while maintaining functionality


## Business Requirements

### Financial Reporting System for Banking

The system generates regulatory compliance reports for banking institutions, handling account transactions and customer data for audit purposes.

**Business Requirement 1**: Generate account summary reports with formatted account numbers and customer names for regulatory compliance.

**Business Requirement 2**: Process transaction records with type validation to ensure data integrity for audit trails.

**Business Requirement 3**: Create detailed financial reports with complex validation rules for risk assessment and regulatory submission.

## Testable Requirements

**Requirement 1**: Account Summary Report Generation

- Generate formatted account summaries using external data repositories
- Validate account number formatting and customer name processing
- Handle various account types and customer name formats

**Requirement 2**: Transaction Record Processing

- Process different transaction types (DEPOSIT, WITHDRAWAL, TRANSFER)
- Validate transaction objects before processing
- Maintain audit trails for regulatory compliance

**Requirement 3**: Financial Report Validation

- Apply complex business rules for report validation
- Validate account balances, transaction limits, and customer eligibility
- Generate comprehensive reports meeting regulatory standards


## Project Structure

```
src/
├── main/java/edu/m006/
│   ├── model/
│   │   ├── Account.java
│   │   └── Transaction.java
│   ├── repository/
│   │   └── AccountRepository.java
│   └── service/
│       └── FinancialReportService.java
└── test/java/edu/m006/
    └── service/
        └── FinancialReportServiceTest.java
```


## Pass 1: Account Summary Report (RED-GREEN-REFACTOR)

### Features Used: anyString(), eq(), endsWith()

These matchers provide flexibility when testing methods that process string data with varying formats but predictable patterns.

### RED Phase - Write Failing Test

**src/test/java/edu/m006/service/FinancialReportServiceTest.java**

```java
package edu.m006.service;

// Import JUnit 5 testing framework for test annotations and assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito framework for mocking dependencies
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

// Import domain model classes
import edu.m006.model.Account;
import edu.m006.repository.AccountRepository;

/**
 * Test class for FinancialReportService using TDD approach with Mockito
 * This class demonstrates RED-GREEN-REFACTOR cycle across 3 requirements
 */
class FinancialReportServiceTest {

    // Create mock object for AccountRepository dependency
    @Mock
    private AccountRepository accountRepository;

    // Inject mocked dependencies into the service under test
    @InjectMocks
    private FinancialReportService financialReportService;

    // Initialize mocks before each test method execution
    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations for dependency injection
        MockitoAnnotations.openMocks(this);
    }

    /**
     * RED PHASE - Requirement 1: Test account summary generation
     * This test will fail initially as no implementation exists
     */
    @Test
    void shouldGenerateAccountSummaryWithFormattedData() {
        // Create test account data for validation
        Account testAccount = new Account("ACC123456", "John Doe", 1500.00);
        
        // Stub repository method using anyString() matcher for flexible account number matching
        // anyString() allows any string value for account number parameter
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(testAccount);
        
        // Stub repository method using eq() for exact customer name matching
        // eq() ensures exact match when mixing with other argument matchers
        when(accountRepository.findByCustomerName(eq("John Doe"))).thenReturn(testAccount);
        
        // Stub repository method using endsWith() for account number suffix validation
        // endsWith() matches account numbers ending with specific patterns
        when(accountRepository.validateAccountNumber(endsWith("123456"))).thenReturn(true);
        
        // Call the method under test - this will fail as method doesn't exist yet
        String summary = financialReportService.generateAccountSummary("ACC123456");
        
        // Verify the expected output format for regulatory compliance
        assertNotNull(summary);
        assertTrue(summary.contains("Account: ACC123456"));
        assertTrue(summary.contains("Customer: John Doe"));
        assertTrue(summary.contains("Balance: $1500.00"));
        
        // Verify that repository methods were called with correct argument matchers
        verify(accountRepository).findByAccountNumber(anyString());
        verify(accountRepository).findByCustomerName(eq("John Doe"));
        verify(accountRepository).validateAccountNumber(endsWith("123456"));
    }
}
```


### GREEN Phase - Implement Minimal Code

**src/main/java/edu/m006/model/Account.java**

```java
package edu.m006.model;

/**
 * Account domain model representing customer bank account
 * Used for financial reporting and regulatory compliance
 */
public class Account {
    // Account identifier for banking operations
    private String accountNumber;
    
    // Customer name for regulatory reporting
    private String customerName;
    
    // Account balance for financial calculations
    private double balance;

    // Constructor to initialize account with required fields
    public Account(String accountNumber, String customerName, double balance) {
        this.accountNumber = accountNumber;
        this.customerName = customerName;
        this.balance = balance;
    }

    // Getter method to retrieve account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // Getter method to retrieve customer name
    public String getCustomerName() {
        return customerName;
    }

    // Getter method to retrieve account balance
    public double getBalance() {
        return balance;
    }
}
```

**src/main/java/edu/m006/repository/AccountRepository.java**

```java
package edu.m006.repository;

// Import domain model
import edu.m006.model.Account;

/**
 * Repository interface for account data operations
 * Provides data access methods for financial reporting
 */
public interface AccountRepository {
    
    // Find account by account number for report generation
    Account findByAccountNumber(String accountNumber);
    
    // Find account by customer name for regulatory compliance
    Account findByCustomerName(String customerName);
    
    // Validate account number format for data integrity
    boolean validateAccountNumber(String accountNumber);
}
```

**src/main/java/edu/m006/service/FinancialReportService.java**

```java
package edu.m006.service;

// Import domain model and repository
import edu.m006.model.Account;
import edu.m006.repository.AccountRepository;

/**
 * Service class for generating financial reports
 * Handles account summary generation for regulatory compliance
 */
public class FinancialReportService {
    
    // Repository dependency for data access operations
    private AccountRepository accountRepository;

    // Constructor for dependency injection
    public FinancialReportService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * Generate account summary report for regulatory compliance
     * @param accountNumber - account identifier for report generation
     * @return formatted summary string for regulatory submission
     */
    public String generateAccountSummary(String accountNumber) {
        // Retrieve account data using repository
        Account account = accountRepository.findByAccountNumber(accountNumber);
        
        // Validate account exists before processing
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        
        // Verify customer name exists in system
        Account customerAccount = accountRepository.findByCustomerName(account.getCustomerName());
        
        // Validate account number format for compliance
        boolean isValidFormat = accountRepository.validateAccountNumber(account.getAccountNumber());
        
        // Ensure account number passes validation checks
        if (!isValidFormat) {
            throw new IllegalArgumentException("Invalid account number format");
        }
        
        // Format account summary for regulatory reporting
        StringBuilder summary = new StringBuilder();
        summary.append("=== Account Summary Report ===\n");
        summary.append("Account: ").append(account.getAccountNumber()).append("\n");
        summary.append("Customer: ").append(account.getCustomerName()).append("\n");
        summary.append("Balance: $").append(String.format("%.2f", account.getBalance()));
        
        // Return formatted summary for compliance submission
        return summary.toString();
    }
}
```


### REFACTOR Phase - Improve Code Quality

The initial implementation is clean and focused. No refactoring needed for this simple case, but in a real scenario, we might extract formatting logic into separate utility methods.

## Pass 2: Transaction Processing (RED-GREEN-REFACTOR)

### Features Used: isA()

The `isA()` matcher performs type checking to ensure objects are of expected types, crucial for transaction validation.

### RED Phase - Add Failing Test

**Update: src/test/java/edu/m006/service/FinancialReportServiceTest.java**

```java
package edu.m006.service;

// Import JUnit 5 testing framework for test annotations and assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito framework for mocking dependencies
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

// Import domain model classes
import edu.m006.model.Account;
import edu.m006.model.Transaction;
import edu.m006.repository.AccountRepository;

/**
 * Test class for FinancialReportService using TDD approach with Mockito
 * This class demonstrates RED-GREEN-REFACTOR cycle across 3 requirements
 */
class FinancialReportServiceTest {

    // Create mock object for AccountRepository dependency
    @Mock
    private AccountRepository accountRepository;

    // Inject mocked dependencies into the service under test
    @InjectMocks
    private FinancialReportService financialReportService;

    // Initialize mocks before each test method execution
    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations for dependency injection
        MockitoAnnotations.openMocks(this);
    }

    /**
     * EXISTING CODE - Requirement 1: Test account summary generation
     * This test now passes after GREEN phase implementation
     */
    @Test
    void shouldGenerateAccountSummaryWithFormattedData() {
        // Create test account data for validation
        Account testAccount = new Account("ACC123456", "John Doe", 1500.00);
        
        // Stub repository method using anyString() matcher for flexible account number matching
        // anyString() allows any string value for account number parameter
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(testAccount);
        
        // Stub repository method using eq() for exact customer name matching
        // eq() ensures exact match when mixing with other argument matchers
        when(accountRepository.findByCustomerName(eq("John Doe"))).thenReturn(testAccount);
        
        // Stub repository method using endsWith() for account number suffix validation
        // endsWith() matches account numbers ending with specific patterns
        when(accountRepository.validateAccountNumber(endsWith("123456"))).thenReturn(true);
        
        // Call the method under test - this now passes
        String summary = financialReportService.generateAccountSummary("ACC123456");
        
        // Verify the expected output format for regulatory compliance
        assertNotNull(summary);
        assertTrue(summary.contains("Account: ACC123456"));
        assertTrue(summary.contains("Customer: John Doe"));
        assertTrue(summary.contains("Balance: $1500.00"));
        
        // Verify that repository methods were called with correct argument matchers
        verify(accountRepository).findByAccountNumber(anyString());
        verify(accountRepository).findByCustomerName(eq("John Doe"));
        verify(accountRepository).validateAccountNumber(endsWith("123456"));
    }

    /**
     * NEW RED PHASE - Requirement 2: Test transaction processing with type validation
     * This test will fail initially as transaction processing method doesn't exist
     */
    @Test
    void shouldProcessTransactionWithTypeValidation() {
        // Create test transaction data for processing
        Transaction testTransaction = new Transaction("TXN001", "DEPOSIT", 500.00, "ACC123456");
        Account testAccount = new Account("ACC123456", "Jane Smith", 2000.00);
        
        // Stub repository method using isA() matcher for type checking
        // isA() ensures the parameter is of Transaction class type
        when(accountRepository.processTransaction(isA(Transaction.class))).thenReturn(true);
        
        // Stub account lookup for transaction validation
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(testAccount);
        
        // Stub transaction validation using type checking
        when(accountRepository.validateTransactionType(isA(String.class))).thenReturn(true);
        
        // Call the method under test - this will fail as method doesn't exist yet
        String result = financialReportService.processTransactionReport(testTransaction);
        
        // Verify the expected transaction processing result
        assertNotNull(result);
        assertTrue(result.contains("Transaction processed successfully"));
        assertTrue(result.contains("TXN001"));
        assertTrue(result.contains("DEPOSIT"));
        
        // Verify that repository methods were called with type-specific matchers
        verify(accountRepository).processTransaction(isA(Transaction.class));
        verify(accountRepository).validateTransactionType(isA(String.class));
    }
}
```


### GREEN Phase - Add Transaction Processing

**NEW: src/main/java/edu/m006/model/Transaction.java**

```java
package edu.m006.model;

/**
 * Transaction domain model representing banking transactions
 * Used for transaction processing and audit trail generation
 */
public class Transaction {
    // Unique transaction identifier for audit purposes
    private String transactionId;
    
    // Transaction type for regulatory classification
    private String transactionType;
    
    // Transaction amount for financial calculations
    private double amount;
    
    // Associated account number for transaction linking
    private String accountNumber;

    // Constructor to initialize transaction with required fields
    public Transaction(String transactionId, String transactionType, double amount, String accountNumber) {
        this.transactionId = transactionId;
        this.transactionType = transactionType;
        this.amount = amount;
        this.accountNumber = accountNumber;
    }

    // Getter method to retrieve transaction identifier
    public String getTransactionId() {
        return transactionId;
    }

    // Getter method to retrieve transaction type
    public String getTransactionType() {
        return transactionType;
    }

    // Getter method to retrieve transaction amount
    public double getAmount() {
        return amount;
    }

    // Getter method to retrieve associated account number
    public String getAccountNumber() {
        return accountNumber;
    }
}
```

**UPDATE: src/main/java/edu/m006/repository/AccountRepository.java**

```java
package edu.m006.repository;

// Import domain models
import edu.m006.model.Account;
import edu.m006.model.Transaction;

/**
 * Repository interface for account data operations
 * Provides data access methods for financial reporting
 */
public interface AccountRepository {
    
    // EXISTING CODE - Find account by account number for report generation
    Account findByAccountNumber(String accountNumber);
    
    // EXISTING CODE - Find account by customer name for regulatory compliance
    Account findByCustomerName(String customerName);
    
    // EXISTING CODE - Validate account number format for data integrity
    boolean validateAccountNumber(String accountNumber);
    
    // NEW CODE - Process transaction for audit trail creation
    boolean processTransaction(Transaction transaction);
    
    // NEW CODE - Validate transaction type for regulatory compliance
    boolean validateTransactionType(String transactionType);
}
```

**UPDATE: src/main/java/edu/m006/service/FinancialReportService.java**

```java
package edu.m006.service;

// Import domain models and repository
import edu.m006.model.Account;
import edu.m006.model.Transaction;
import edu.m006.repository.AccountRepository;

/**
 * Service class for generating financial reports
 * Handles account summary generation and transaction processing for regulatory compliance
 */
public class FinancialReportService {
    
    // Repository dependency for data access operations
    private AccountRepository accountRepository;

    // Constructor for dependency injection
    public FinancialReportService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * EXISTING CODE - Generate account summary report for regulatory compliance
     * @param accountNumber - account identifier for report generation
     * @return formatted summary string for regulatory submission
     */
    public String generateAccountSummary(String accountNumber) {
        // Retrieve account data using repository
        Account account = accountRepository.findByAccountNumber(accountNumber);
        
        // Validate account exists before processing
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        
        // Verify customer name exists in system
        Account customerAccount = accountRepository.findByCustomerName(account.getCustomerName());
        
        // Validate account number format for compliance
        boolean isValidFormat = accountRepository.validateAccountNumber(account.getAccountNumber());
        
        // Ensure account number passes validation checks
        if (!isValidFormat) {
            throw new IllegalArgumentException("Invalid account number format");
        }
        
        // Format account summary for regulatory reporting
        StringBuilder summary = new StringBuilder();
        summary.append("=== Account Summary Report ===\n");
        summary.append("Account: ").append(account.getAccountNumber()).append("\n");
        summary.append("Customer: ").append(account.getCustomerName()).append("\n");
        summary.append("Balance: $").append(String.format("%.2f", account.getBalance()));
        
        // Return formatted summary for compliance submission
        return summary.toString();
    }

    /**
     * NEW CODE - Process transaction report for audit trail generation
     * @param transaction - transaction object for processing
     * @return formatted transaction result for regulatory compliance
     */
    public String processTransactionReport(Transaction transaction) {
        // Validate transaction object is not null
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        
        // Validate transaction type for regulatory compliance
        boolean isValidType = accountRepository.validateTransactionType(transaction.getTransactionType());
        if (!isValidType) {
            throw new IllegalArgumentException("Invalid transaction type: " + transaction.getTransactionType());
        }
        
        // Verify associated account exists in system
        Account associatedAccount = accountRepository.findByAccountNumber(transaction.getAccountNumber());
        if (associatedAccount == null) {
            throw new IllegalArgumentException("Associated account not found: " + transaction.getAccountNumber());
        }
        
        // Process transaction through repository layer
        boolean processingResult = accountRepository.processTransaction(transaction);
        if (!processingResult) {
            throw new RuntimeException("Transaction processing failed for: " + transaction.getTransactionId());
        }
        
        // Format transaction processing result for audit trail
        StringBuilder result = new StringBuilder();
        result.append("=== Transaction Processing Report ===\n");
        result.append("Transaction processed successfully\n");
        result.append("Transaction ID: ").append(transaction.getTransactionId()).append("\n");
        result.append("Type: ").append(transaction.getTransactionType()).append("\n");
        result.append("Amount: $").append(String.format("%.2f", transaction.getAmount())).append("\n");
        result.append("Account: ").append(transaction.getAccountNumber());
        
        // Return formatted result for regulatory compliance
        return result.toString();
    }
}
```


### REFACTOR Phase - Improve Code Quality

The transaction processing method could be extracted into smaller methods for better readability, but the current implementation is acceptable for the scope of this assignment.

## Pass 3: Complex Financial Validation (RED-GREEN-REFACTOR)

### Features Used: Custom Argument Matching

Custom argument matching allows sophisticated validation of complex business rules and object states.

### RED Phase - Add Complex Validation Test

**UPDATE: src/test/java/edu/m006/service/FinancialReportServiceTest.java**

```java
package edu.m006.service;

// Import JUnit 5 testing framework for test annotations and assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito framework for mocking dependencies
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

// Import domain model classes
import edu.m006.model.Account;
import edu.m006.model.Transaction;
import edu.m006.repository.AccountRepository;

// Import for custom argument matching
import org.mockito.ArgumentMatcher;

/**
 * Test class for FinancialReportService using TDD approach with Mockito
 * This class demonstrates RED-GREEN-REFACTOR cycle across 3 requirements
 */
class FinancialReportServiceTest {

    // Create mock object for AccountRepository dependency
    @Mock
    private AccountRepository accountRepository;

    // Inject mocked dependencies into the service under test
    @InjectMocks
    private FinancialReportService financialReportService;

    // Initialize mocks before each test method execution
    @BeforeEach
    void setUp() {
        // Initialize Mockito annotations for dependency injection
        MockitoAnnotations.openMocks(this);
    }

    /**
     * EXISTING CODE - Requirement 1: Test account summary generation
     * This test passes after previous GREEN phase implementation
     */
    @Test
    void shouldGenerateAccountSummaryWithFormattedData() {
        // Create test account data for validation
        Account testAccount = new Account("ACC123456", "John Doe", 1500.00);
        
        // Stub repository method using anyString() matcher for flexible account number matching
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(testAccount);
        when(accountRepository.findByCustomerName(eq("John Doe"))).thenReturn(testAccount);
        when(accountRepository.validateAccountNumber(endsWith("123456"))).thenReturn(true);
        
        // Call the method under test
        String summary = financialReportService.generateAccountSummary("ACC123456");
        
        // Verify the expected output format for regulatory compliance
        assertNotNull(summary);
        assertTrue(summary.contains("Account: ACC123456"));
        assertTrue(summary.contains("Customer: John Doe"));
        assertTrue(summary.contains("Balance: $1500.00"));
        
        // Verify method calls with argument matchers
        verify(accountRepository).findByAccountNumber(anyString());
        verify(accountRepository).findByCustomerName(eq("John Doe"));
        verify(accountRepository).validateAccountNumber(endsWith("123456"));
    }

    /**
     * EXISTING CODE - Requirement 2: Test transaction processing with type validation
     * This test passes after previous GREEN phase implementation
     */
    @Test
    void shouldProcessTransactionWithTypeValidation() {
        // Create test transaction data for processing
        Transaction testTransaction = new Transaction("TXN001", "DEPOSIT", 500.00, "ACC123456");
        Account testAccount = new Account("ACC123456", "Jane Smith", 2000.00);
        
        // Stub repository methods using type checking matchers
        when(accountRepository.processTransaction(isA(Transaction.class))).thenReturn(true);
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(testAccount);
        when(accountRepository.validateTransactionType(isA(String.class))).thenReturn(true);
        
        // Call the method under test
        String result = financialReportService.processTransactionReport(testTransaction);
        
        // Verify the expected transaction processing result
        assertNotNull(result);
        assertTrue(result.contains("Transaction processed successfully"));
        assertTrue(result.contains("TXN001"));
        assertTrue(result.contains("DEPOSIT"));
        
        // Verify method calls with type-specific matchers
        verify(accountRepository).processTransaction(isA(Transaction.class));
        verify(accountRepository).validateTransactionType(isA(String.class));
    }

    /**
     * NEW RED PHASE - Requirement 3: Test complex financial validation with custom argument matching
     * This test will fail initially as comprehensive validation method doesn't exist
     */
    @Test
    void shouldValidateFinancialReportWithComplexBusinessRules() {
        // Create test data for complex validation scenario
        Account highValueAccount = new Account("ACC999888", "Premium Customer", 50000.00);
        Transaction largeTransaction = new Transaction("TXN999", "TRANSFER", 25000.00, "ACC999888");
        
        // Create custom argument matcher for high-value accounts (balance > 10000)
        ArgumentMatcher<Account> isHighValueAccount = account -> 
            account != null && account.getBalance() > 10000.00;
        
        // Create custom argument matcher for large transactions (amount > 5000)
        ArgumentMatcher<Transaction> isLargeTransaction = transaction -> 
            transaction != null && transaction.getAmount() > 5000.00;
        
        // Create custom argument matcher for premium customer validation
        ArgumentMatcher<String> isPremiumCustomer = customerName -> 
            customerName != null && customerName.toLowerCase().contains("premium");
        
        // Stub repository methods using custom argument matchers for sophisticated validation
        when(accountRepository.findByAccountNumber(anyString())).thenReturn(highValueAccount);
        
        // Use custom matcher to validate high-value accounts receive special processing
        when(accountRepository.validateHighValueAccount(argThat(isHighValueAccount))).thenReturn(true);
        
        // Use custom matcher to validate large transactions require additional checks
        when(accountRepository.validateLargeTransaction(argThat(isLargeTransaction))).thenReturn(true);
        
        // Use custom matcher to validate premium customer status
        when(accountRepository.validatePremiumCustomer(argThat(isPremiumCustomer))).thenReturn(true);
        
        // Call the method under test - this will fail as method doesn't exist yet
        String validationReport = financialReportService.generateComprehensiveValidationReport("ACC999888", largeTransaction);
        
        // Verify the expected comprehensive validation results
        assertNotNull(validationReport);
        assertTrue(validationReport.contains("Comprehensive Financial Validation"));
        assertTrue(validationReport.contains("High Value Account: VALIDATED"));
        assertTrue(validationReport.contains("Large Transaction: VALIDATED"));
        assertTrue(validationReport.contains("Premium Customer: VALIDATED"));
        assertTrue(validationReport.contains("Overall Status: COMPLIANT"));
        
        // Verify that repository methods were called with custom argument matchers
        verify(accountRepository).validateHighValueAccount(argThat(isHighValueAccount));
        verify(accountRepository).validateLargeTransaction(argThat(isLargeTransaction));
        verify(accountRepository).validatePremiumCustomer(argThat(isPremiumCustomer));
    }
}
```


### GREEN Phase - Add Comprehensive Validation

**UPDATE: src/main/java/edu/m006/repository/AccountRepository.java**

```java
package edu.m006.repository;

// Import domain models
import edu.m006.model.Account;
import edu.m006.model.Transaction;

/**
 * Repository interface for account data operations
 * Provides data access methods for financial reporting
 */
public interface AccountRepository {
    
    // EXISTING CODE - Basic account operations
    Account findByAccountNumber(String accountNumber);
    Account findByCustomerName(String customerName);
    boolean validateAccountNumber(String accountNumber);
    
    // EXISTING CODE - Transaction processing operations
    boolean processTransaction(Transaction transaction);
    boolean validateTransactionType(String transactionType);
    
    // NEW CODE - Advanced validation operations for complex business rules
    boolean validateHighValueAccount(Account account);
    boolean validateLargeTransaction(Transaction transaction);
    boolean validatePremiumCustomer(String customerName);
}
```

**UPDATE: src/main/java/edu/m006/service/FinancialReportService.java**

```java
package edu.m006.service;

// Import domain models and repository
import edu.m006.model.Account;
import edu.m006.model.Transaction;
import edu.m006.repository.AccountRepository;

/**
 * Service class for generating financial reports
 * Handles account summaries, transaction processing, and comprehensive validation for regulatory compliance
 */
public class FinancialReportService {
    
    // Repository dependency for data access operations
    private AccountRepository accountRepository;

    // Constructor for dependency injection
    public FinancialReportService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    /**
     * EXISTING CODE - Generate account summary report for regulatory compliance
     */
    public String generateAccountSummary(String accountNumber) {
        // Retrieve account data using repository
        Account account = accountRepository.findByAccountNumber(accountNumber);
        
        // Validate account exists before processing
        if (account == null) {
            throw new IllegalArgumentException("Account not found: " + accountNumber);
        }
        
        // Verify customer name exists in system
        Account customerAccount = accountRepository.findByCustomerName(account.getCustomerName());
        
        // Validate account number format for compliance
        boolean isValidFormat = accountRepository.validateAccountNumber(account.getAccountNumber());
        
        // Ensure account number passes validation checks
        if (!isValidFormat) {
            throw new IllegalArgumentException("Invalid account number format");
        }
        
        // Format account summary for regulatory reporting
        StringBuilder summary = new StringBuilder();
        summary.append("=== Account Summary Report ===\n");
        summary.append("Account: ").append(account.getAccountNumber()).append("\n");
        summary.append("Customer: ").append(account.getCustomerName()).append("\n");
        summary.append("Balance: $").append(String.format("%.2f", account.getBalance()));
        
        // Return formatted summary for compliance submission
        return summary.toString();
    }

    /**
     * EXISTING CODE - Process transaction report for audit trail generation
     */
    public String processTransactionReport(Transaction transaction) {
        // Validate transaction object is not null
        if (transaction == null) {
            throw new IllegalArgumentException("Transaction cannot be null");
        }
        
        // Validate transaction type for regulatory compliance
        boolean isValidType = accountRepository.validateTransactionType(transaction.getTransactionType());
        if (!isValidType) {
            throw new IllegalArgumentException("Invalid transaction type: " + transaction.getTransactionType());
        }
        
        // Verify associated account exists in system
        Account associatedAccount = accountRepository.findByAccountNumber(transaction.getAccountNumber());
        if (associatedAccount == null) {
            throw new IllegalArgumentException("Associated account not found: " + transaction.getAccountNumber());
        }
        
        // Process transaction through repository layer
        boolean processingResult = accountRepository.processTransaction(transaction);
        if (!processingResult) {
            throw new RuntimeException("Transaction processing failed for: " + transaction.getTransactionId());
        }
        
        // Format transaction processing result for audit trail
        StringBuilder result = new StringBuilder();
        result.append("=== Transaction Processing Report ===\n");
        result.append("Transaction processed successfully\n");
        result.append("Transaction ID: ").append(transaction.getTransactionId()).append("\n");
        result.append("Type: ").append(transaction.getTransactionType()).append("\n");
        result.append("Amount: $").append(String.format("%.2f", transaction.getAmount())).append("\n");
        result.append("Account: ").append(transaction.getAccountNumber());
        
        // Return formatted result for regulatory compliance
        return result.toString();
    }

    /**
     * NEW CODE - Generate comprehensive validation report with complex business rules
     * @param accountNumber - account identifier for validation
     * @param transaction - transaction for comprehensive validation
     * @return detailed validation report for regulatory compliance
     */
    public String generateComprehensiveValidationReport(String accountNumber, Transaction transaction) {
        // Validate input parameters are not null
        if (accountNumber == null || transaction == null) {
            throw new IllegalArgumentException("Account number and transaction are required for validation");
        }
        
        // Retrieve account information for validation
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new IllegalArgumentException("Account not found for validation: " + accountNumber);
        }
        
        // Initialize validation results tracking
        boolean isHighValueValid = false;
        boolean isLargeTransactionValid = false;
        boolean isPremiumCustomerValid = false;
        
        // Perform high-value account validation using custom business rules
        try {
            isHighValueValid = accountRepository.validateHighValueAccount(account);
        } catch (Exception e) {
            // Handle validation exceptions gracefully
            isHighValueValid = false;
        }
        
        // Perform large transaction validation using custom business rules
        try {
            isLargeTransactionValid = accountRepository.validateLargeTransaction(transaction);
        } catch (Exception e) {
            // Handle validation exceptions gracefully
            isLargeTransactionValid = false;
        }
        
        // Perform premium customer validation using custom business rules
        try {
            isPremiumCustomerValid = accountRepository.validatePremiumCustomer(account.getCustomerName());
        } catch (Exception e) {
            // Handle validation exceptions gracefully
            isPremiumCustomerValid = false;
        }
        
        // Determine overall compliance status based on all validation results
        boolean overallCompliant = isHighValueValid && isLargeTransactionValid && isPremiumCustomerValid;
        
        // Format comprehensive validation report for regulatory submission
        StringBuilder validationReport = new StringBuilder();
        validationReport.append("=== Comprehensive Financial Validation Report ===\n");
        validationReport.append("Account Number: ").append(accountNumber).append("\n");
        validationReport.append("Customer: ").append(account.getCustomerName()).append("\n");
        validationReport.append("Transaction ID: ").append(transaction.getTransactionId()).append("\n");
        validationReport.append("--- Validation Results ---\n");
        validationReport.append("High Value Account: ").append(isHighValueValid ? "VALIDATED" : "FAILED").append("\n");
        validationReport.append("Large Transaction: ").append(isLargeTransactionValid ? "VALIDATED" : "FAILED").append("\n");
        validationReport.append("Premium Customer: ").append(isPremiumCustomerValid ? "VALIDATED" : "FAILED").append("\n");
        validationReport.append("--- Overall Assessment ---\n");
        validationReport.append("Overall Status: ").append(overallCompliant ? "COMPLIANT" : "NON-COMPLIANT");
        
        // Return comprehensive validation report for regulatory compliance
        return validationReport.toString();
    }
}
```


### REFACTOR Phase - Final Code Quality Improvements

The comprehensive validation method demonstrates good separation of concerns and error handling. For production code, we might consider extracting validation logic into separate validator classes, but the current implementation effectively demonstrates the TDD cycle and Mockito features.

## Project Setup Instructions

### Maven Dependencies (pom.xml)

```xml
<dependencies>
    <!-- JUnit 5 for testing framework -->
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter</artifactId>
        <version>5.10.0</version>
        <scope>test</scope>
    </dependency>
    
    <!-- Mockito 5 for mocking framework -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-core</artifactId>
        <version>5.5.0</version>
        <scope>test</scope>
    </dependency>
    
    <!-- Mockito JUnit Jupiter integration -->
    <dependency>
        <groupId>org.mockito</groupId>
        <artifactId>mockito-junit-jupiter</artifactId>
        <version>5.5.0</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```


### Running the Assignment

1. **Pass 1**: Copy the Pass 1 test and implementation code, run tests (should fail initially, then pass after implementation)
2. **Pass 2**: Add Pass 2 code to existing files, run tests (new test fails initially, then passes)
3. **Pass 3**: Add Pass 3 code to existing files, run tests (final test fails initially, then passes)

Each pass demonstrates the complete RED-GREEN-REFACTOR cycle with progressively more complex Mockito features, providing hands-on experience with TDD principles in a financial reporting context.