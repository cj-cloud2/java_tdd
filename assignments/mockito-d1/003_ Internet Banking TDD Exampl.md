# Mockito Assignment 03: Internet Banking TDD Example

**Difficulty Level:** 2
**Domain:** Internet Banking
**Package:** edu.m003
**Framework:** JUnit5 + Mockito-5

## Business Requirements

**Domain:** Internet Banking System

Our internet banking application needs to provide core banking functionalities to customers through a web interface. The system should handle account operations, fund transfers, transaction history, and account validation with proper security and data integrity.

## Testable Requirements Derived from Business Requirements

1. **Balance Inquiry Requirement:** The system must allow customers to check their account balance. The balance inquiry should retrieve account information from the repository at least once to ensure data freshness.
2. **Fund Transfer Requirement:** The system must enable customers to transfer funds between accounts. During a transfer operation, the system should validate both source and destination accounts by checking their existence multiple times (minimum 2 times - once for source, once for destination).
3. **Transaction History Requirement:** The system must provide customers with their transaction history. For performance optimization, the transaction history should not exceed 3 repository calls per request to avoid system overload.
4. **Account Validation Requirement:** The system must validate account status before any operation. Account validation should be an isolated operation that only calls the account status check method without any other repository interactions.

## Summary

This TDD example demonstrates the complete RED-GREEN-REFACTOR cycle with Mockito verification methods:

- **Pass 1:** Balance inquiry using `atLeastOnce()` verification
- **Pass 2:** Fund transfer using `atLeast(2)` verification
- **Pass 3:** Transaction history using `atMost(3)` verification
- **Pass 4:** Account validation using `only()` verification

Each pass follows the TDD methodology:

1. **RED:** Write a failing test
2. **GREEN:** Write minimal code to pass the test
3. **REFACTOR:** Clean up code while keeping tests green





## Step-by-Step TDD Implementation Guide

### Project Structure

```
src/
├── main/java/edu/m003/
│   ├── Account.java
│   ├── AccountRepository.java
│   └── BankingService.java
└── test/java/edu/m003/
    └── BankingServiceTest.java
```


***

## Pass 1: RED-GREEN-REFACTOR (Requirement 1 - Balance Inquiry)

### RED Phase - Write Failing Test

**File:** `src/test/java/edu/m003/BankingServiceTest.java`

```java
package edu.m003;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

// Enable Mockito annotations for this test class
@ExtendWith(MockitoExtension.class)
class BankingServiceTest {

    // Mock the AccountRepository dependency
    @Mock
    private AccountRepository accountRepository;

    // Inject the mock into BankingService
    @InjectMocks
    private BankingService bankingService;

    // Test account instance for testing
    private Account testAccount;

    // Setup method to initialize test data before each test
    @BeforeEach
    void setUp() {
        // Create a test account with sample data
        testAccount = new Account("ACC001", "John Doe", new BigDecimal("1000.00"));
    }

    // RED: Test for balance inquiry - should fail initially
    @Test
    void testGetAccountBalance_ShouldRetrieveBalanceFromRepository() {
        // Arrange: Setup mock behavior for repository
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(testAccount);

        // Act: Call the method under test
        BigDecimal balance = bankingService.getAccountBalance("ACC001");

        // Assert: Verify the returned balance
        assertEquals(new BigDecimal("1000.00"), balance);
        
        // Verify: Ensure repository was called at least once (Requirement 1)
        verify(accountRepository, atLeastOnce()).findByAccountNumber("ACC001");
    }
}
```


### GREEN Phase - Create Minimal Implementation

**File:** `src/main/java/edu/m003/Account.java`

```java
package edu.m003;

import java.math.BigDecimal;

// Bean class representing a bank account
public class Account {
    // Account number field
    private String accountNumber;
    // Account holder name field
    private String accountHolderName;
    // Account balance field
    private BigDecimal balance;

    // Default constructor
    public Account() {
    }

    // Parameterized constructor to initialize account
    public Account(String accountNumber, String accountHolderName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    // Getter method for account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // Setter method for account number
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Getter method for account holder name
    public String getAccountHolderName() {
        return accountHolderName;
    }

    // Setter method for account holder name
    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    // Getter method for balance
    public BigDecimal getBalance() {
        return balance;
    }

    // Setter method for balance
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
```

**File:** `src/main/java/edu/m003/AccountRepository.java`

```java
package edu.m003;

import java.util.List;

// Repository interface for account data operations
public interface AccountRepository {
    
    // Method to find account by account number
    Account findByAccountNumber(String accountNumber);
    
    // Method to check if account exists
    boolean existsByAccountNumber(String accountNumber);
    
    // Method to get transaction history for an account
    List<String> getTransactionHistory(String accountNumber);
    
    // Method to check account status
    String getAccountStatus(String accountNumber);
}
```

**File:** `src/main/java/edu/m003/BankingService.java`

```java
package edu.m003;

import java.math.BigDecimal;
import java.util.List;

// Service class for banking operations
public class BankingService {
    
    // Repository dependency for data access
    private AccountRepository accountRepository;

    // Constructor for dependency injection
    public BankingService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Method to get account balance (Requirement 1)
    public BigDecimal getAccountBalance(String accountNumber) {
        // Retrieve account from repository
        Account account = accountRepository.findByAccountNumber(accountNumber);
        // Return account balance
        return account.getBalance();
    }
}
```


### REFACTOR Phase

The code is simple and clean. No refactoring needed for this pass.

***

## Pass 2: RED-GREEN-REFACTOR (Requirement 2 - Fund Transfer)

### RED Phase - Add New Failing Test

**File:** `src/test/java/edu/m003/BankingServiceTest.java`

```java
package edu.m003;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

// Enable Mockito annotations for this test class
@ExtendWith(MockitoExtension.class)
class BankingServiceTest {

    // Mock the AccountRepository dependency
    @Mock
    private AccountRepository accountRepository;

    // Inject the mock into BankingService
    @InjectMocks
    private BankingService bankingService;

    // Test account instance for testing
    private Account testAccount;

    // Setup method to initialize test data before each test
    @BeforeEach
    void setUp() {
        // Create a test account with sample data
        testAccount = new Account("ACC001", "John Doe", new BigDecimal("1000.00"));
    }

    // EXISTING TEST - Test for balance inquiry
    @Test
    void testGetAccountBalance_ShouldRetrieveBalanceFromRepository() {
        // Arrange: Setup mock behavior for repository
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(testAccount);

        // Act: Call the method under test
        BigDecimal balance = bankingService.getAccountBalance("ACC001");

        // Assert: Verify the returned balance
        assertEquals(new BigDecimal("1000.00"), balance);
        
        // Verify: Ensure repository was called at least once (Requirement 1)
        verify(accountRepository, atLeastOnce()).findByAccountNumber("ACC001");
    }

    // NEW TEST - RED: Test for fund transfer - should fail initially
    @Test
    void testTransferFunds_ShouldValidateBothAccounts() {
        // Arrange: Setup mock behavior for both accounts
        when(accountRepository.existsByAccountNumber("ACC001")).thenReturn(true);
        when(accountRepository.existsByAccountNumber("ACC002")).thenReturn(true);

        // Act: Call the transfer method
        boolean transferResult = bankingService.transferFunds("ACC001", "ACC002", new BigDecimal("100.00"));

        // Assert: Verify transfer was successful
        assertTrue(transferResult);
        
        // Verify: Ensure both accounts were validated at least 2 times total (Requirement 2)
        verify(accountRepository, atLeast(2)).existsByAccountNumber(anyString());
    }
}
```


### GREEN Phase - Extend Implementation

**File:** `src/main/java/edu/m003/Account.java` (NO CHANGES - Same as Pass 1)

```java
package edu.m003;

import java.math.BigDecimal;

// Bean class representing a bank account
public class Account {
    // Account number field
    private String accountNumber;
    // Account holder name field
    private String accountHolderName;
    // Account balance field
    private BigDecimal balance;

    // Default constructor
    public Account() {
    }

    // Parameterized constructor to initialize account
    public Account(String accountNumber, String accountHolderName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    // Getter method for account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // Setter method for account number
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Getter method for account holder name
    public String getAccountHolderName() {
        return accountHolderName;
    }

    // Setter method for account holder name
    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    // Getter method for balance
    public BigDecimal getBalance() {
        return balance;
    }

    // Setter method for balance
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
```

**File:** `src/main/java/edu/m003/AccountRepository.java` (NO CHANGES - Same as Pass 1)

```java
package edu.m003;

import java.util.List;

// Repository interface for account data operations
public interface AccountRepository {
    
    // Method to find account by account number
    Account findByAccountNumber(String accountNumber);
    
    // Method to check if account exists
    boolean existsByAccountNumber(String accountNumber);
    
    // Method to get transaction history for an account
    List<String> getTransactionHistory(String accountNumber);
    
    // Method to check account status
    String getAccountStatus(String accountNumber);
}
```

**File:** `src/main/java/edu/m003/BankingService.java`

```java
package edu.m003;

import java.math.BigDecimal;
import java.util.List;

// Service class for banking operations
public class BankingService {
    
    // Repository dependency for data access
    private AccountRepository accountRepository;

    // Constructor for dependency injection
    public BankingService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // EXISTING METHOD - Method to get account balance (Requirement 1)
    public BigDecimal getAccountBalance(String accountNumber) {
        // Retrieve account from repository
        Account account = accountRepository.findByAccountNumber(accountNumber);
        // Return account balance
        return account.getBalance();
    }

    // NEW METHOD - Method to transfer funds between accounts (Requirement 2)
    public boolean transferFunds(String fromAccount, String toAccount, BigDecimal amount) {
        // Validate source account exists
        boolean sourceExists = accountRepository.existsByAccountNumber(fromAccount);
        // Validate destination account exists
        boolean destinationExists = accountRepository.existsByAccountNumber(toAccount);
        
        // Return success if both accounts exist
        return sourceExists && destinationExists;
    }
}
```


### REFACTOR Phase

The code is still simple and readable. No refactoring needed.

***

## Pass 3: RED-GREEN-REFACTOR (Requirement 3 - Transaction History)

### RED Phase - Add New Failing Test

**File:** `src/test/java/edu/m003/BankingServiceTest.java`

```java
package edu.m003;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

// Enable Mockito annotations for this test class
@ExtendWith(MockitoExtension.class)
class BankingServiceTest {

    // Mock the AccountRepository dependency
    @Mock
    private AccountRepository accountRepository;

    // Inject the mock into BankingService
    @InjectMocks
    private BankingService bankingService;

    // Test account instance for testing
    private Account testAccount;

    // Setup method to initialize test data before each test
    @BeforeEach
    void setUp() {
        // Create a test account with sample data
        testAccount = new Account("ACC001", "John Doe", new BigDecimal("1000.00"));
    }

    // EXISTING TEST - Test for balance inquiry
    @Test
    void testGetAccountBalance_ShouldRetrieveBalanceFromRepository() {
        // Arrange: Setup mock behavior for repository
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(testAccount);

        // Act: Call the method under test
        BigDecimal balance = bankingService.getAccountBalance("ACC001");

        // Assert: Verify the returned balance
        assertEquals(new BigDecimal("1000.00"), balance);
        
        // Verify: Ensure repository was called at least once (Requirement 1)
        verify(accountRepository, atLeastOnce()).findByAccountNumber("ACC001");
    }

    // EXISTING TEST - Test for fund transfer
    @Test
    void testTransferFunds_ShouldValidateBothAccounts() {
        // Arrange: Setup mock behavior for both accounts
        when(accountRepository.existsByAccountNumber("ACC001")).thenReturn(true);
        when(accountRepository.existsByAccountNumber("ACC002")).thenReturn(true);

        // Act: Call the transfer method
        boolean transferResult = bankingService.transferFunds("ACC001", "ACC002", new BigDecimal("100.00"));

        // Assert: Verify transfer was successful
        assertTrue(transferResult);
        
        // Verify: Ensure both accounts were validated at least 2 times total (Requirement 2)
        verify(accountRepository, atLeast(2)).existsByAccountNumber(anyString());
    }

    // NEW TEST - RED: Test for transaction history - should fail initially
    @Test
    void testGetTransactionHistory_ShouldNotExceedMaxRepositoryCalls() {
        // Arrange: Setup mock behavior for transaction history
        List<String> expectedTransactions = Arrays.asList("Transaction 1", "Transaction 2", "Transaction 3");
        when(accountRepository.getTransactionHistory("ACC001")).thenReturn(expectedTransactions);

        // Act: Call the transaction history method
        List<String> transactions = bankingService.getTransactionHistory("ACC001");

        // Assert: Verify the returned transactions
        assertEquals(3, transactions.size());
        assertEquals("Transaction 1", transactions.get(0));
        
        // Verify: Ensure repository was called at most 3 times (Requirement 3)
        verify(accountRepository, atMost(3)).getTransactionHistory("ACC001");
    }
}
```


### GREEN Phase - Extend Implementation

**File:** `src/main/java/edu/m003/Account.java` (NO CHANGES - Same as Pass 2)

```java
package edu.m003;

import java.math.BigDecimal;

// Bean class representing a bank account
public class Account {
    // Account number field
    private String accountNumber;
    // Account holder name field
    private String accountHolderName;
    // Account balance field
    private BigDecimal balance;

    // Default constructor
    public Account() {
    }

    // Parameterized constructor to initialize account
    public Account(String accountNumber, String accountHolderName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    // Getter method for account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // Setter method for account number
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Getter method for account holder name
    public String getAccountHolderName() {
        return accountHolderName;
    }

    // Setter method for account holder name
    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    // Getter method for balance
    public BigDecimal getBalance() {
        return balance;
    }

    // Setter method for balance
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
```

**File:** `src/main/java/edu/m003/AccountRepository.java` (NO CHANGES - Same as Pass 2)

```java
package edu.m003;

import java.util.List;

// Repository interface for account data operations
public interface AccountRepository {
    
    // Method to find account by account number
    Account findByAccountNumber(String accountNumber);
    
    // Method to check if account exists
    boolean existsByAccountNumber(String accountNumber);
    
    // Method to get transaction history for an account
    List<String> getTransactionHistory(String accountNumber);
    
    // Method to check account status
    String getAccountStatus(String accountNumber);
}
```

**File:** `src/main/java/edu/m003/BankingService.java`

```java
package edu.m003;

import java.math.BigDecimal;
import java.util.List;

// Service class for banking operations
public class BankingService {
    
    // Repository dependency for data access
    private AccountRepository accountRepository;

    // Constructor for dependency injection
    public BankingService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // EXISTING METHOD - Method to get account balance (Requirement 1)
    public BigDecimal getAccountBalance(String accountNumber) {
        // Retrieve account from repository
        Account account = accountRepository.findByAccountNumber(accountNumber);
        // Return account balance
        return account.getBalance();
    }

    // EXISTING METHOD - Method to transfer funds between accounts (Requirement 2)
    public boolean transferFunds(String fromAccount, String toAccount, BigDecimal amount) {
        // Validate source account exists
        boolean sourceExists = accountRepository.existsByAccountNumber(fromAccount);
        // Validate destination account exists
        boolean destinationExists = accountRepository.existsByAccountNumber(toAccount);
        
        // Return success if both accounts exist
        return sourceExists && destinationExists;
    }

    // NEW METHOD - Method to get transaction history (Requirement 3)
    public List<String> getTransactionHistory(String accountNumber) {
        // Retrieve transaction history from repository (single call to stay within limit)
        List<String> transactions = accountRepository.getTransactionHistory(accountNumber);
        // Return the transaction list
        return transactions;
    }
}
```


### REFACTOR Phase

The code remains clean and focused. No refactoring needed.

***

## Pass 4: RED-GREEN-REFACTOR (Requirement 4 - Account Validation)

### RED Phase - Add New Failing Test

**File:** `src/test/java/edu/m003/BankingServiceTest.java`

```java
package edu.m003;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

// Enable Mockito annotations for this test class
@ExtendWith(MockitoExtension.class)
class BankingServiceTest {

    // Mock the AccountRepository dependency
    @Mock
    private AccountRepository accountRepository;

    // Inject the mock into BankingService
    @InjectMocks
    private BankingService bankingService;

    // Test account instance for testing
    private Account testAccount;

    // Setup method to initialize test data before each test
    @BeforeEach
    void setUp() {
        // Create a test account with sample data
        testAccount = new Account("ACC001", "John Doe", new BigDecimal("1000.00"));
    }

    // EXISTING TEST - Test for balance inquiry
    @Test
    void testGetAccountBalance_ShouldRetrieveBalanceFromRepository() {
        // Arrange: Setup mock behavior for repository
        when(accountRepository.findByAccountNumber("ACC001")).thenReturn(testAccount);

        // Act: Call the method under test
        BigDecimal balance = bankingService.getAccountBalance("ACC001");

        // Assert: Verify the returned balance
        assertEquals(new BigDecimal("1000.00"), balance);
        
        // Verify: Ensure repository was called at least once (Requirement 1)
        verify(accountRepository, atLeastOnce()).findByAccountNumber("ACC001");
    }

    // EXISTING TEST - Test for fund transfer
    @Test
    void testTransferFunds_ShouldValidateBothAccounts() {
        // Arrange: Setup mock behavior for both accounts
        when(accountRepository.existsByAccountNumber("ACC001")).thenReturn(true);
        when(accountRepository.existsByAccountNumber("ACC002")).thenReturn(true);

        // Act: Call the transfer method
        boolean transferResult = bankingService.transferFunds("ACC001", "ACC002", new BigDecimal("100.00"));

        // Assert: Verify transfer was successful
        assertTrue(transferResult);
        
        // Verify: Ensure both accounts were validated at least 2 times total (Requirement 2)
        verify(accountRepository, atLeast(2)).existsByAccountNumber(anyString());
    }

    // EXISTING TEST - Test for transaction history
    @Test
    void testGetTransactionHistory_ShouldNotExceedMaxRepositoryCalls() {
        // Arrange: Setup mock behavior for transaction history
        List<String> expectedTransactions = Arrays.asList("Transaction 1", "Transaction 2", "Transaction 3");
        when(accountRepository.getTransactionHistory("ACC001")).thenReturn(expectedTransactions);

        // Act: Call the transaction history method
        List<String> transactions = bankingService.getTransactionHistory("ACC001");

        // Assert: Verify the returned transactions
        assertEquals(3, transactions.size());
        assertEquals("Transaction 1", transactions.get(0));
        
        // Verify: Ensure repository was called at most 3 times (Requirement 3)
        verify(accountRepository, atMost(3)).getTransactionHistory("ACC001");
    }

    // NEW TEST - RED: Test for account validation - should fail initially
    @Test
    void testValidateAccount_ShouldOnlyCallStatusCheck() {
        // Arrange: Setup mock behavior for account status
        when(accountRepository.getAccountStatus("ACC001")).thenReturn("ACTIVE");

        // Act: Call the account validation method
        String status = bankingService.validateAccount("ACC001");

        // Assert: Verify the returned status
        assertEquals("ACTIVE", status);
        
        // Verify: Ensure only the status check method was called (Requirement 4)
        verify(accountRepository, only()).getAccountStatus("ACC001");
    }
}
```


### GREEN Phase - Final Implementation

**File:** `src/main/java/edu/m003/Account.java` (NO CHANGES - Same as Pass 3)

```java
package edu.m003;

import java.math.BigDecimal;

// Bean class representing a bank account
public class Account {
    // Account number field
    private String accountNumber;
    // Account holder name field
    private String accountHolderName;
    // Account balance field
    private BigDecimal balance;

    // Default constructor
    public Account() {
    }

    // Parameterized constructor to initialize account
    public Account(String accountNumber, String accountHolderName, BigDecimal balance) {
        this.accountNumber = accountNumber;
        this.accountHolderName = accountHolderName;
        this.balance = balance;
    }

    // Getter method for account number
    public String getAccountNumber() {
        return accountNumber;
    }

    // Setter method for account number
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    // Getter method for account holder name
    public String getAccountHolderName() {
        return accountHolderName;
    }

    // Setter method for account holder name
    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    // Getter method for balance
    public BigDecimal getBalance() {
        return balance;
    }

    // Setter method for balance
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
}
```

**File:** `src/main/java/edu/m003/AccountRepository.java` (NO CHANGES - Same as Pass 3)

```java
package edu.m003;

import java.util.List;

// Repository interface for account data operations
public interface AccountRepository {
    
    // Method to find account by account number
    Account findByAccountNumber(String accountNumber);
    
    // Method to check if account exists
    boolean existsByAccountNumber(String accountNumber);
    
    // Method to get transaction history for an account
    List<String> getTransactionHistory(String accountNumber);
    
    // Method to check account status
    String getAccountStatus(String accountNumber);
}
```

**File:** `src/main/java/edu/m003/BankingService.java`

```java
package edu.m003;

import java.math.BigDecimal;
import java.util.List;

// Service class for banking operations
public class BankingService {
    
    // Repository dependency for data access
    private AccountRepository accountRepository;

    // Constructor for dependency injection
    public BankingService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // EXISTING METHOD - Method to get account balance (Requirement 1)
    public BigDecimal getAccountBalance(String accountNumber) {
        // Retrieve account from repository
        Account account = accountRepository.findByAccountNumber(accountNumber);
        // Return account balance
        return account.getBalance();
    }

    // EXISTING METHOD - Method to transfer funds between accounts (Requirement 2)
    public boolean transferFunds(String fromAccount, String toAccount, BigDecimal amount) {
        // Validate source account exists
        boolean sourceExists = accountRepository.existsByAccountNumber(fromAccount);
        // Validate destination account exists
        boolean destinationExists = accountRepository.existsByAccountNumber(toAccount);
        
        // Return success if both accounts exist
        return sourceExists && destinationExists;
    }

    // EXISTING METHOD - Method to get transaction history (Requirement 3)
    public List<String> getTransactionHistory(String accountNumber) {
        // Retrieve transaction history from repository (single call to stay within limit)
        List<String> transactions = accountRepository.getTransactionHistory(accountNumber);
        // Return the transaction list
        return transactions;
    }

    // NEW METHOD - Method to validate account status (Requirement 4)
    public String validateAccount(String accountNumber) {
        // Get account status from repository (isolated operation)
        String status = accountRepository.getAccountStatus(accountNumber);
        // Return the account status
        return status;
    }
}
```


### REFACTOR Phase - Final Cleanup

**File:** `src/main/java/edu/m003/BankingService.java` (REFACTORED VERSION)

```java
package edu.m003;

import java.math.BigDecimal;
import java.util.List;

// Service class for banking operations
public class BankingService {
    
    // Repository dependency for data access
    private final AccountRepository accountRepository;

    // Constructor for dependency injection
    public BankingService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    // Method to get account balance (Requirement 1 - uses atLeastOnce verification)
    public BigDecimal getAccountBalance(String accountNumber) {
        // Retrieve account from repository
        Account account = accountRepository.findByAccountNumber(accountNumber);
        // Return account balance
        return account.getBalance();
    }

    // Method to transfer funds between accounts (Requirement 2 - uses atLeast verification)
    public boolean transferFunds(String fromAccount, String toAccount, BigDecimal amount) {
        // Validate source account exists
        boolean sourceExists = accountRepository.existsByAccountNumber(fromAccount);
        // Validate destination account exists
        boolean destinationExists = accountRepository.existsByAccountNumber(toAccount);
        
        // Return success if both accounts exist
        return sourceExists && destinationExists;
    }

    // Method to get transaction history (Requirement 3 - uses atMost verification)
    public List<String> getTransactionHistory(String accountNumber) {
        // Retrieve transaction history from repository (single call to stay within limit)
        List<String> transactions = accountRepository.getTransactionHistory(accountNumber);
        // Return the transaction list
        return transactions;
    }

    // Method to validate account status (Requirement 4 - uses only verification)
    public String validateAccount(String accountNumber) {
        // Get account status from repository (isolated operation)
        String status = accountRepository.getAccountStatus(accountNumber);
        // Return the account status
        return status;
    }
}
```




The implementation covers core internet banking functionality while demonstrating proper mock stubbing and verification techniques with Mockito-5 and JUnit5.

