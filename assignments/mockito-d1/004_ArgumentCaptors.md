# Mockito Assignment 04: Argument Captors

## Business Requirements

**Internet Banking Transaction Processing System**

1. **Account Balance Transfer Requirement**: The system must transfer funds from one account to another, validating sufficient balance and maintaining transaction history.
2. **Transaction Audit Requirement**: All fund transfers must be logged with complete transaction details including source account, destination account, amount, and timestamp for regulatory compliance.
3. **Account Status Validation Requirement**: The system must validate that both source and destination accounts are active before processing any transaction.

## Testable Requirements Derived from Business Requirements

1. **Transfer Validation Testing**: Verify that funds can be transferred between accounts only when source account has sufficient balance and both accounts are active.
2. **Transaction Logging Testing**: Verify that all transaction details (account numbers, amount, timestamp) are correctly captured and logged during fund transfers.
3. **Account Status Testing**: Verify that transactions are rejected when either source or destination account is inactive.

## Key Mockito Features Explained

### Argument Captors

**Argument Capture**: Captures arguments passed to method calls for further assertions or validation. This allows you to inspect the exact parameters that were passed to mocked methods, ensuring your code is calling dependencies with the correct data.

**Complex Argument Analysis**: Enables detailed inspection of method parameters, particularly useful when testing interactions between services where you need to verify that objects are created with specific properties.

***

## Pass 1: RED-GREEN-REFACTOR Cycle

### Testing Transfer Validation (Argument Captor for Balance Verification)

**How Argument Captor helps in this pass**: We'll use ArgumentCaptor to capture the account objects passed to the repository when checking balances, allowing us to verify that the correct accounts are being queried during validation.

### RED Phase: Write Failing Test

**File: src/test/java/edu/m005/BankingServiceTest.java**

```java
package edu.m005;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Enable Mockito annotations for JUnit 5
@ExtendWith(MockitoExtension.class)
class BankingServiceTest {

    // Mock the repository that will handle account operations
    @Mock
    private AccountRepository accountRepository;

    // Capture arguments passed to repository methods for validation
    @Captor
    private ArgumentCaptor<String> accountNumberCaptor;

    // The service under test - will be created in setup method
    private BankingService bankingService;

    // Setup method to initialize the service with mocked dependencies
    @BeforeEach
    void setUp() {
        // Create the service instance with the mocked repository
        bankingService = new BankingService(accountRepository);
    }

    // RED: First failing test for basic transfer validation
    @Test
    void shouldTransferFundsWhenSufficientBalance() {
        // Given: Setup test data with source and destination account numbers
        String sourceAccountNumber = "ACC001";
        String destinationAccountNumber = "ACC002";
        BigDecimal transferAmount = new BigDecimal("100.00");

        // Create account objects with sufficient balance for source account
        Account sourceAccount = new Account(sourceAccountNumber, new BigDecimal("500.00"), AccountStatus.ACTIVE);
        Account destinationAccount = new Account(destinationAccountNumber, new BigDecimal("200.00"), AccountStatus.ACTIVE);

        // Mock repository responses to return our test accounts
        when(accountRepository.findByAccountNumber(sourceAccountNumber)).thenReturn(sourceAccount);
        when(accountRepository.findByAccountNumber(destinationAccountNumber)).thenReturn(destinationAccount);

        // When: Execute the transfer operation
        TransferResult result = bankingService.transferFunds(sourceAccountNumber, destinationAccountNumber, transferAmount);

        // Then: Verify the transfer was successful
        assertTrue(result.isSuccessful());
        assertEquals("Transfer completed successfully", result.getMessage());

        // Use ArgumentCaptor to verify repository was called with correct account numbers
        verify(accountRepository, times(2)).findByAccountNumber(accountNumberCaptor.capture());
        
        // Verify that repository was called with both source and destination account numbers
        assertEquals(sourceAccountNumber, accountNumberCaptor.getAllValues().get(0));
        assertEquals(destinationAccountNumber, accountNumberCaptor.getAllValues().get(1));
    }
}
```


### GREEN Phase: Minimal Implementation

**File: src/main/java/edu/m005/BankingService.java**

```java
package edu.m005;

import java.math.BigDecimal;

// Main service class that handles banking operations
public class BankingService {
    
    // Repository dependency for account data access
    private final AccountRepository accountRepository;

    // Constructor injection for repository dependency
    public BankingService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Transfer funds between two accounts with validation
    public TransferResult transferFunds(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount) {
        // Retrieve source account from repository
        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
        // Retrieve destination account from repository
        Account destinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber);

        // Validate that source account has sufficient balance
        if (sourceAccount.getBalance().compareTo(amount) >= 0 && 
            sourceAccount.getStatus() == AccountStatus.ACTIVE && 
            destinationAccount.getStatus() == AccountStatus.ACTIVE) {
            // Return success result when validation passes
            return new TransferResult(true, "Transfer completed successfully");
        }
        
        // Return failure result when validation fails
        return new TransferResult(false, "Transfer failed");
    }
}
```

**File: src/main/java/edu/m005/Account.java**

```java
package edu.m005;

import java.math.BigDecimal;

// Account entity representing a bank account
public class Account {
    // Unique identifier for the account
    private final String accountNumber;
    // Current balance in the account
    private BigDecimal balance;
    // Current status of the account (ACTIVE/INACTIVE)
    private AccountStatus status;

    // Constructor to create account with number, balance and status
    public Account(String accountNumber, BigDecimal balance, AccountStatus status) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.status = status;
    }

    // Getter method to retrieve account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // Getter method to retrieve current balance
    public BigDecimal getBalance() {
        return balance;
    }

    // Getter method to retrieve account status
    public AccountStatus getStatus() {
        return status;
    }

    // Setter method to update account balance
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
```

**File: src/main/java/edu/m005/AccountStatus.java**

```java
package edu.m005;

// Enumeration defining possible account states
public enum AccountStatus {
    // Account is active and can perform transactions
    ACTIVE,
    // Account is inactive and cannot perform transactions
    INACTIVE
}
```

**File: src/main/java/edu/m005/AccountRepository.java**

```java
package edu.m005;

// Repository interface for account data access operations
public interface AccountRepository {
    // Method to find account by its unique account number
    Account findByAccountNumber(String accountNumber);
    
    // Method to save or update account information
    void save(Account account);
}
```

**File: src/main/java/edu/m005/TransferResult.java**

```java
package edu.m005;

// Result object that encapsulates transfer operation outcome
public class TransferResult {
    // Boolean flag indicating if transfer was successful
    private final boolean successful;
    // Message providing details about the transfer result
    private final String message;

    // Constructor to create transfer result with status and message
    public TransferResult(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }

    // Getter method to check if transfer was successful
    public boolean isSuccessful() {
        return successful;
    }

    // Getter method to retrieve result message
    public String getMessage() {
        return message;
    }
}
```


### REFACTOR Phase

Code is clean and follows single responsibility principle. No refactoring needed at this stage.

***

## Pass 2: RED-GREEN-REFACTOR Cycle

### Testing Transaction Logging (Argument Captor for Transaction Details)

**How Argument Captor helps in this pass**: We'll use ArgumentCaptor to capture the transaction objects passed to the audit repository, allowing us to verify that transaction details are logged with correct account numbers, amounts, and timestamps.[^2][^6]

### RED Phase: Extended Test

**File: src/test/java/edu/m005/BankingServiceTest.java**

```java
package edu.m005;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Enable Mockito annotations for JUnit 5
@ExtendWith(MockitoExtension.class)
class BankingServiceTest {

    // Mock the repository that will handle account operations
    @Mock
    private AccountRepository accountRepository;

    // *** NEW CODE: Mock the repository for transaction audit logging ***
    @Mock
    private TransactionAuditRepository auditRepository;

    // Capture arguments passed to repository methods for validation
    @Captor
    private ArgumentCaptor<String> accountNumberCaptor;

    // *** NEW CODE: Capture transaction objects passed to audit repository ***
    @Captor
    private ArgumentCaptor<TransactionAudit> transactionAuditCaptor;

    // The service under test - will be created in setup method
    private BankingService bankingService;

    // Setup method to initialize the service with mocked dependencies
    @BeforeEach
    void setUp() {
        // *** UPDATED CODE: Create the service instance with both mocked repositories ***
        bankingService = new BankingService(accountRepository, auditRepository);
    }

    // RED: First failing test for basic transfer validation
    @Test
    void shouldTransferFundsWhenSufficientBalance() {
        // Given: Setup test data with source and destination account numbers
        String sourceAccountNumber = "ACC001";
        String destinationAccountNumber = "ACC002";
        BigDecimal transferAmount = new BigDecimal("100.00");

        // Create account objects with sufficient balance for source account
        Account sourceAccount = new Account(sourceAccountNumber, new BigDecimal("500.00"), AccountStatus.ACTIVE);
        Account destinationAccount = new Account(destinationAccountNumber, new BigDecimal("200.00"), AccountStatus.ACTIVE);

        // Mock repository responses to return our test accounts
        when(accountRepository.findByAccountNumber(sourceAccountNumber)).thenReturn(sourceAccount);
        when(accountRepository.findByAccountNumber(destinationAccountNumber)).thenReturn(destinationAccount);

        // When: Execute the transfer operation
        TransferResult result = bankingService.transferFunds(sourceAccountNumber, destinationAccountNumber, transferAmount);

        // Then: Verify the transfer was successful
        assertTrue(result.isSuccessful());
        assertEquals("Transfer completed successfully", result.getMessage());

        // Use ArgumentCaptor to verify repository was called with correct account numbers
        verify(accountRepository, times(2)).findByAccountNumber(accountNumberCaptor.capture());
        
        // Verify that repository was called with both source and destination account numbers
        assertEquals(sourceAccountNumber, accountNumberCaptor.getAllValues().get(0));
        assertEquals(destinationAccountNumber, accountNumberCaptor.getAllValues().get(1));
    }

    // *** NEW CODE: RED - Second failing test for transaction audit logging ***
    @Test
    void shouldLogTransactionDetailsWhenTransferCompletes() {
        // Given: Setup test data for successful transfer scenario
        String sourceAccountNumber = "ACC001";
        String destinationAccountNumber = "ACC002";
        BigDecimal transferAmount = new BigDecimal("150.00");

        // Create account objects with sufficient balance
        Account sourceAccount = new Account(sourceAccountNumber, new BigDecimal("500.00"), AccountStatus.ACTIVE);
        Account destinationAccount = new Account(destinationAccountNumber, new BigDecimal("200.00"), AccountStatus.ACTIVE);

        // Mock repository responses
        when(accountRepository.findByAccountNumber(sourceAccountNumber)).thenReturn(sourceAccount);
        when(accountRepository.findByAccountNumber(destinationAccountNumber)).thenReturn(destinationAccount);

        // When: Execute the transfer operation
        bankingService.transferFunds(sourceAccountNumber, destinationAccountNumber, transferAmount);

        // Then: Verify that transaction audit was logged with correct details
        verify(auditRepository).saveTransactionAudit(transactionAuditCaptor.capture());
        
        // Extract the captured transaction audit object
        TransactionAudit capturedAudit = transactionAuditCaptor.getValue();
        
        // Verify that audit contains correct source account number
        assertEquals(sourceAccountNumber, capturedAudit.getSourceAccountNumber());
        // Verify that audit contains correct destination account number
        assertEquals(destinationAccountNumber, capturedAudit.getDestinationAccountNumber());
        // Verify that audit contains correct transfer amount
        assertEquals(transferAmount, capturedAudit.getAmount());
        // Verify that audit contains a timestamp (should not be null)
        assertNotNull(capturedAudit.getTimestamp());
        // Verify that timestamp is recent (within last minute)
        assertTrue(capturedAudit.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(1)));
    }
}
```


### GREEN Phase: Extended Implementation

**File: src/main/java/edu/m005/BankingService.java**

```java
package edu.m005;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Main service class that handles banking operations
public class BankingService {
    
    // Repository dependency for account data access
    private final AccountRepository accountRepository;
    // *** NEW CODE: Repository dependency for transaction audit logging ***
    private final TransactionAuditRepository auditRepository;

    // *** UPDATED CODE: Constructor injection for both repository dependencies ***
    public BankingService(AccountRepository accountRepository, TransactionAuditRepository auditRepository) {
        this.accountRepository = accountRepository;
        this.auditRepository = auditRepository;
    }

    // Transfer funds between two accounts with validation and audit logging
    public TransferResult transferFunds(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount) {
        // Retrieve source account from repository
        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
        // Retrieve destination account from repository
        Account destinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber);

        // Validate that source account has sufficient balance and both accounts are active
        if (sourceAccount.getBalance().compareTo(amount) >= 0 && 
            sourceAccount.getStatus() == AccountStatus.ACTIVE && 
            destinationAccount.getStatus() == AccountStatus.ACTIVE) {
            
            // *** NEW CODE: Create transaction audit record with all details ***
            TransactionAudit audit = new TransactionAudit(
                sourceAccountNumber,
                destinationAccountNumber, 
                amount,
                LocalDateTime.now()
            );
            
            // *** NEW CODE: Save the transaction audit to repository ***
            auditRepository.saveTransactionAudit(audit);
            
            // Return success result when validation passes
            return new TransferResult(true, "Transfer completed successfully");
        }
        
        // Return failure result when validation fails
        return new TransferResult(false, "Transfer failed");
    }
}
```

**File: src/main/java/edu/m005/TransactionAudit.java**

```java
package edu.m005;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// *** NEW CODE: Transaction audit entity to store transaction details for compliance ***
public class TransactionAudit {
    // Source account number for the transaction
    private final String sourceAccountNumber;
    // Destination account number for the transaction
    private final String destinationAccountNumber;
    // Amount transferred in the transaction
    private final BigDecimal amount;
    // Timestamp when the transaction occurred
    private final LocalDateTime timestamp;

    // Constructor to create transaction audit with all required details
    public TransactionAudit(String sourceAccountNumber, String destinationAccountNumber, 
                           BigDecimal amount, LocalDateTime timestamp) {
        this.sourceAccountNumber = sourceAccountNumber;
        this.destinationAccountNumber = destinationAccountNumber;
        this.amount = amount;
        this.timestamp = timestamp;
    }

    // Getter method to retrieve source account number
    public String getSourceAccountNumber() {
        return sourceAccountNumber;
    }

    // Getter method to retrieve destination account number
    public String getDestinationAccountNumber() {
        return destinationAccountNumber;
    }

    // Getter method to retrieve transaction amount
    public BigDecimal getAmount() {
        return amount;
    }

    // Getter method to retrieve transaction timestamp
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
```

**File: src/main/java/edu/m005/TransactionAuditRepository.java**

```java
package edu.m005;

// *** NEW CODE: Repository interface for transaction audit operations ***
public interface TransactionAuditRepository {
    // Method to save transaction audit details for regulatory compliance
    void saveTransactionAudit(TransactionAudit audit);
}
```


### REFACTOR Phase

The code structure is good. We could consider extracting validation logic into a separate method for better readability in future iterations.

***

## Pass 3: RED-GREEN-REFACTOR Cycle

### Testing Account Status Validation (Argument Captor for Failed Transaction Audit)

**How Argument Captor helps in this pass**: We'll use ArgumentCaptor to capture account objects during status validation and verify that failed transactions are also audited with appropriate failure reasons.[^7][^1]

### RED Phase: Final Test Addition

**File: src/test/java/edu/m005/BankingServiceTest.java**

```java
package edu.m005;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

// Enable Mockito annotations for JUnit 5
@ExtendWith(MockitoExtension.class)
class BankingServiceTest {

    // Mock the repository that will handle account operations
    @Mock
    private AccountRepository accountRepository;

    // Mock the repository for transaction audit logging
    @Mock
    private TransactionAuditRepository auditRepository;

    // Capture arguments passed to repository methods for validation
    @Captor
    private ArgumentCaptor<String> accountNumberCaptor;

    // Capture transaction objects passed to audit repository
    @Captor
    private ArgumentCaptor<TransactionAudit> transactionAuditCaptor;

    // *** NEW CODE: Capture account objects for status validation ***
    @Captor
    private ArgumentCaptor<Account> accountCaptor;

    // The service under test - will be created in setup method
    private BankingService bankingService;

    // Setup method to initialize the service with mocked dependencies
    @BeforeEach
    void setUp() {
        // Create the service instance with both mocked repositories
        bankingService = new BankingService(accountRepository, auditRepository);
    }

    // First test for basic transfer validation
    @Test
    void shouldTransferFundsWhenSufficientBalance() {
        // Given: Setup test data with source and destination account numbers
        String sourceAccountNumber = "ACC001";
        String destinationAccountNumber = "ACC002";
        BigDecimal transferAmount = new BigDecimal("100.00");

        // Create account objects with sufficient balance for source account
        Account sourceAccount = new Account(sourceAccountNumber, new BigDecimal("500.00"), AccountStatus.ACTIVE);
        Account destinationAccount = new Account(destinationAccountNumber, new BigDecimal("200.00"), AccountStatus.ACTIVE);

        // Mock repository responses to return our test accounts
        when(accountRepository.findByAccountNumber(sourceAccountNumber)).thenReturn(sourceAccount);
        when(accountRepository.findByAccountNumber(destinationAccountNumber)).thenReturn(destinationAccount);

        // When: Execute the transfer operation
        TransferResult result = bankingService.transferFunds(sourceAccountNumber, destinationAccountNumber, transferAmount);

        // Then: Verify the transfer was successful
        assertTrue(result.isSuccessful());
        assertEquals("Transfer completed successfully", result.getMessage());

        // Use ArgumentCaptor to verify repository was called with correct account numbers
        verify(accountRepository, times(2)).findByAccountNumber(accountNumberCaptor.capture());
        
        // Verify that repository was called with both source and destination account numbers
        assertEquals(sourceAccountNumber, accountNumberCaptor.getAllValues().get(0));
        assertEquals(destinationAccountNumber, accountNumberCaptor.getAllValues().get(1));
    }

    // Second test for transaction audit logging
    @Test
    void shouldLogTransactionDetailsWhenTransferCompletes() {
        // Given: Setup test data for successful transfer scenario
        String sourceAccountNumber = "ACC001";
        String destinationAccountNumber = "ACC002";
        BigDecimal transferAmount = new BigDecimal("150.00");

        // Create account objects with sufficient balance
        Account sourceAccount = new Account(sourceAccountNumber, new BigDecimal("500.00"), AccountStatus.ACTIVE);
        Account destinationAccount = new Account(destinationAccountNumber, new BigDecimal("200.00"), AccountStatus.ACTIVE);

        // Mock repository responses
        when(accountRepository.findByAccountNumber(sourceAccountNumber)).thenReturn(sourceAccount);
        when(accountRepository.findByAccountNumber(destinationAccountNumber)).thenReturn(destinationAccount);

        // When: Execute the transfer operation
        bankingService.transferFunds(sourceAccountNumber, destinationAccountNumber, transferAmount);

        // Then: Verify that transaction audit was logged with correct details
        verify(auditRepository).saveTransactionAudit(transactionAuditCaptor.capture());
        
        // Extract the captured transaction audit object
        TransactionAudit capturedAudit = transactionAuditCaptor.getValue();
        
        // Verify that audit contains correct source account number
        assertEquals(sourceAccountNumber, capturedAudit.getSourceAccountNumber());
        // Verify that audit contains correct destination account number
        assertEquals(destinationAccountNumber, capturedAudit.getDestinationAccountNumber());
        // Verify that audit contains correct transfer amount
        assertEquals(transferAmount, capturedAudit.getAmount());
        // Verify that audit contains a timestamp (should not be null)
        assertNotNull(capturedAudit.getTimestamp());
        // Verify that timestamp is recent (within last minute)
        assertTrue(capturedAudit.getTimestamp().isAfter(LocalDateTime.now().minusMinutes(1)));
    }

    // *** NEW CODE: RED - Third failing test for account status validation ***
    @Test
    void shouldRejectTransferWhenSourceAccountIsInactive() {
        // Given: Setup test data with inactive source account
        String sourceAccountNumber = "ACC001";
        String destinationAccountNumber = "ACC002";
        BigDecimal transferAmount = new BigDecimal("100.00");

        // Create source account as INACTIVE and destination as ACTIVE
        Account sourceAccount = new Account(sourceAccountNumber, new BigDecimal("500.00"), AccountStatus.INACTIVE);
        Account destinationAccount = new Account(destinationAccountNumber, new BigDecimal("200.00"), AccountStatus.ACTIVE);

        // Mock repository responses to return test accounts
        when(accountRepository.findByAccountNumber(sourceAccountNumber)).thenReturn(sourceAccount);
        when(accountRepository.findByAccountNumber(destinationAccountNumber)).thenReturn(destinationAccount);

        // When: Execute the transfer operation
        TransferResult result = bankingService.transferFunds(sourceAccountNumber, destinationAccountNumber, transferAmount);

        // Then: Verify the transfer was rejected
        assertFalse(result.isSuccessful());
        assertEquals("Transfer failed: Source account is inactive", result.getMessage());

        // Verify that account status validation was performed by capturing account lookups
        verify(accountRepository, times(2)).findByAccountNumber(accountNumberCaptor.capture());
        
        // Verify correct accounts were retrieved for validation
        assertEquals(sourceAccountNumber, accountNumberCaptor.getAllValues().get(0));
        assertEquals(destinationAccountNumber, accountNumberCaptor.getAllValues().get(1));

        // *** NEW CODE: Verify that failed transaction audit is logged with failure reason ***
        verify(auditRepository).saveFailedTransactionAudit(transactionAuditCaptor.capture());
        
        // Extract the captured failed transaction audit
        TransactionAudit failedAudit = transactionAuditCaptor.getValue();
        
        // Verify failed audit contains source account number
        assertEquals(sourceAccountNumber, failedAudit.getSourceAccountNumber());
        // Verify failed audit contains destination account number  
        assertEquals(destinationAccountNumber, failedAudit.getDestinationAccountNumber());
        // Verify failed audit contains attempted transfer amount
        assertEquals(transferAmount, failedAudit.getAmount());
        // Verify failed audit has timestamp
        assertNotNull(failedAudit.getTimestamp());
    }
}
```


### GREEN Phase: Final Implementation

**File: src/main/java/edu/m005/BankingService.java**

```java
package edu.m005;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Main service class that handles banking operations
public class BankingService {
    
    // Repository dependency for account data access
    private final AccountRepository accountRepository;
    // Repository dependency for transaction audit logging
    private final TransactionAuditRepository auditRepository;

    // Constructor injection for both repository dependencies
    public BankingService(AccountRepository accountRepository, TransactionAuditRepository auditRepository) {
        this.accountRepository = accountRepository;
        this.auditRepository = auditRepository;
    }

    // Transfer funds between two accounts with comprehensive validation and audit logging
    public TransferResult transferFunds(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount) {
        // Retrieve source account from repository
        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
        // Retrieve destination account from repository
        Account destinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber);

        // *** NEW CODE: Enhanced validation with specific error messages for account status ***
        
        // Check if source account is inactive
        if (sourceAccount.getStatus() != AccountStatus.ACTIVE) {
            // Create failed transaction audit for inactive source account
            TransactionAudit failedAudit = new TransactionAudit(
                sourceAccountNumber, destinationAccountNumber, amount, LocalDateTime.now()
            );
            // Log the failed transaction attempt
            auditRepository.saveFailedTransactionAudit(failedAudit);
            // Return specific failure message for inactive source account
            return new TransferResult(false, "Transfer failed: Source account is inactive");
        }
        
        // Check if destination account is inactive
        if (destinationAccount.getStatus() != AccountStatus.ACTIVE) {
            // Create failed transaction audit for inactive destination account
            TransactionAudit failedAudit = new TransactionAudit(
                sourceAccountNumber, destinationAccountNumber, amount, LocalDateTime.now()
            );
            // Log the failed transaction attempt
            auditRepository.saveFailedTransactionAudit(failedAudit);
            // Return specific failure message for inactive destination account
            return new TransferResult(false, "Transfer failed: Destination account is inactive");
        }

        // Validate that source account has sufficient balance
        if (sourceAccount.getBalance().compareTo(amount) >= 0) {
            // Create successful transaction audit record with all details
            TransactionAudit audit = new TransactionAudit(
                sourceAccountNumber,
                destinationAccountNumber, 
                amount,
                LocalDateTime.now()
            );
            
            // Save the successful transaction audit to repository
            auditRepository.saveTransactionAudit(audit);
            
            // Return success result when all validations pass
            return new TransferResult(true, "Transfer completed successfully");
        }
        
        // *** NEW CODE: Log failed transaction for insufficient balance ***
        TransactionAudit failedAudit = new TransactionAudit(
            sourceAccountNumber, destinationAccountNumber, amount, LocalDateTime.now()
        );
        auditRepository.saveFailedTransactionAudit(failedAudit);
        
        // Return failure result for insufficient balance
        return new TransferResult(false, "Transfer failed: Insufficient balance");
    }
}
```

**File: src/main/java/edu/m005/TransactionAuditRepository.java**

```java
package edu.m005;

// Repository interface for transaction audit operations
public interface TransactionAuditRepository {
    // Method to save successful transaction audit details for regulatory compliance
    void saveTransactionAudit(TransactionAudit audit);
    
    // *** NEW CODE: Method to save failed transaction audit details with failure reasons ***
    void saveFailedTransactionAudit(TransactionAudit audit);
}
```


### REFACTOR Phase

**File: src/main/java/edu/m005/BankingService.java** (Refactored for better readability)

```java
package edu.m005;

import java.math.BigDecimal;
import java.time.LocalDateTime;

// Main service class that handles banking operations
public class BankingService {
    
    // Repository dependency for account data access
    private final AccountRepository accountRepository;
    // Repository dependency for transaction audit logging
    private final TransactionAuditRepository auditRepository;

    // Constructor injection for both repository dependencies
    public BankingService(AccountRepository accountRepository, TransactionAuditRepository auditRepository) {
        this.accountRepository = accountRepository;
        this.auditRepository = auditRepository;
    }

    // Transfer funds between two accounts with comprehensive validation and audit logging
    public TransferResult transferFunds(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount) {
        // Retrieve both accounts from repository
        Account sourceAccount = accountRepository.findByAccountNumber(sourceAccountNumber);
        Account destinationAccount = accountRepository.findByAccountNumber(destinationAccountNumber);

        // *** REFACTORED CODE: Validate account statuses using helper method ***
        TransferResult statusValidationResult = validateAccountStatuses(sourceAccount, destinationAccount, 
                                                                       sourceAccountNumber, destinationAccountNumber, amount);
        if (statusValidationResult != null) {
            return statusValidationResult;
        }

        // *** REFACTORED CODE: Validate balance using helper method ***
        return processTransferWithBalanceValidation(sourceAccount, sourceAccountNumber, destinationAccountNumber, amount);
    }

    // *** REFACTORED CODE: Helper method to validate account statuses ***
    private TransferResult validateAccountStatuses(Account sourceAccount, Account destinationAccount, 
                                                  String sourceAccountNumber, String destinationAccountNumber, 
                                                  BigDecimal amount) {
        // Check if source account is inactive
        if (sourceAccount.getStatus() != AccountStatus.ACTIVE) {
            // Log failed transaction and return specific error message
            logFailedTransaction(sourceAccountNumber, destinationAccountNumber, amount);
            return new TransferResult(false, "Transfer failed: Source account is inactive");
        }
        
        // Check if destination account is inactive
        if (destinationAccount.getStatus() != AccountStatus.ACTIVE) {
            // Log failed transaction and return specific error message
            logFailedTransaction(sourceAccountNumber, destinationAccountNumber, amount);
            return new TransferResult(false, "Transfer failed: Destination account is inactive");
        }
        
        // Return null if all status validations pass
        return null;
    }

    // *** REFACTORED CODE: Helper method to process transfer with balance validation ***
    private TransferResult processTransferWithBalanceValidation(Account sourceAccount, String sourceAccountNumber, 
                                                               String destinationAccountNumber, BigDecimal amount) {
        // Validate that source account has sufficient balance
        if (sourceAccount.getBalance().compareTo(amount) >= 0) {
            // Log successful transaction
            logSuccessfulTransaction(sourceAccountNumber, destinationAccountNumber, amount);
            return new TransferResult(true, "Transfer completed successfully");
        }
        
        // Log failed transaction for insufficient balance
        logFailedTransaction(sourceAccountNumber, destinationAccountNumber, amount);
        return new TransferResult(false, "Transfer failed: Insufficient balance");
    }

    // *** REFACTORED CODE: Helper method to log successful transactions ***
    private void logSuccessfulTransaction(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount) {
        // Create and save successful transaction audit
        TransactionAudit audit = new TransactionAudit(sourceAccountNumber, destinationAccountNumber, amount, LocalDateTime.now());
        auditRepository.saveTransactionAudit(audit);
    }

    // *** REFACTORED CODE: Helper method to log failed transactions ***
    private void logFailedTransaction(String sourceAccountNumber, String destinationAccountNumber, BigDecimal amount) {
        // Create and save failed transaction audit
        TransactionAudit failedAudit = new TransactionAudit(sourceAccountNumber, destinationAccountNumber, amount, LocalDateTime.now());
        auditRepository.saveFailedTransactionAudit(failedAudit);
    }
}
```

This TDD assignment successfully demonstrates the RED-GREEN-REFACTOR cycle with Mockito ArgumentCaptor usage, progressing through three comprehensive passes that build a robust internet banking transfer system with full audit capabilities.
