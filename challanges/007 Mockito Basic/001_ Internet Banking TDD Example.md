# Mockito Assignment 1: Internet Banking TDD Example

**Difficulty Level:** 2
**Technologies:** JUnit5, Mockito-5, Java
**Domain:** Internet Banking

## Business Requirements

### 1. Account Balance Inquiry

Customers should be able to check their current account balance using their account number. The system must validate the account exists and return the accurate balance.

### 2. Money Transfer Between Accounts

Customers should be able to transfer money from their account to another valid account. The system must verify sufficient funds, validate both accounts exist, and update balances accordingly.

### 3. Transaction History Retrieval

Customers should be able to view their recent transaction history including transaction dates, amounts, and descriptions for their account.

## Testable Requirements

### Derived from Business Requirements:

1. **Balance Inquiry Test:** When a valid account number is provided to the banking service, it should return the current account balance from the repository.
2. **Money Transfer Test:** When transferring money between valid accounts with sufficient funds, the banking service should update both account balances and return a success confirmation.
3. **Transaction History Test:** When requesting transaction history for a valid account, the banking service should return a list of recent transactions from the transaction repository.

***

## Step-by-Step TDD Implementation Guide

### Prerequisites Setup

Create the following dependency classes first (these won't change during the exercise):

**Account.java**

```java
public class Account {
    private String accountNumber;
    private double balance;
    
    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public double getBalance() {
        return balance;
    }
    
    public void setBalance(double balance) {
        this.balance = balance;
    }
}
```

**Transaction.java**

```java
import java.time.LocalDateTime;

public class Transaction {
    private String accountNumber;
    private double amount;
    private String description;
    private LocalDateTime timestamp;
    
    public Transaction(String accountNumber, double amount, String description, LocalDateTime timestamp) {
        this.accountNumber = accountNumber;
        this.amount = amount;
        this.description = description;
        this.timestamp = timestamp;
    }
    
    public String getAccountNumber() {
        return accountNumber;
    }
    
    public double getAmount() {
        return amount;
    }
    
    public String getDescription() {
        return description;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
```

**AccountRepository.java**

```java
public interface AccountRepository {
    Account findByAccountNumber(String accountNumber);
    void updateAccount(Account account);
}
```

**TransactionRepository.java**

```java
import java.util.List;

public interface TransactionRepository {
    List<Transaction> findByAccountNumber(String accountNumber);
    void saveTransaction(Transaction transaction);
}
```


***

## Pass 1: Account Balance Inquiry (RED-GREEN-REFACTOR)

### Step 1: RED - Write Failing Test

Create **BankingServiceTest.java**:

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankingServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private TransactionRepository transactionRepository;
    
    private BankingService bankingService;
    
    @BeforeEach
    void setUp() {
        bankingService = new BankingService(accountRepository, transactionRepository);
    }
    
    @Test
    void shouldReturnAccountBalanceForValidAccount() {
        // Given
        String accountNumber = "12345";
        Account mockAccount = new Account(accountNumber, 1000.0);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(mockAccount);
        
        // When
        double balance = bankingService.getAccountBalance(accountNumber);
        
        // Then
        assertEquals(1000.0, balance);
    }
}
```

**Run Test:** ❌ FAILS (BankingService class doesn't exist)

### Step 2: GREEN - Make Test Pass

Create **BankingService.java**:

```java
public class BankingService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    
    public BankingService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    
    public double getAccountBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        return account.getBalance();
    }
}
```

**Run Test:** ✅ PASSES

### Step 3: REFACTOR - Clean Up Code

No refactoring needed at this stage. Code is simple and clean.

***

## Pass 2: Money Transfer Between Accounts (RED-GREEN-REFACTOR)

### Step 4: RED - Add Failing Test

Update **BankingServiceTest.java** (replace entire content):

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankingServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private TransactionRepository transactionRepository;
    
    private BankingService bankingService;
    
    @BeforeEach
    void setUp() {
        bankingService = new BankingService(accountRepository, transactionRepository);
    }
    
    // EXISTING TEST FROM PASS 1
    @Test
    void shouldReturnAccountBalanceForValidAccount() {
        // Given
        String accountNumber = "12345";
        Account mockAccount = new Account(accountNumber, 1000.0);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(mockAccount);
        
        // When
        double balance = bankingService.getAccountBalance(accountNumber);
        
        // Then
        assertEquals(1000.0, balance);
    }
    
    // NEW TEST FOR PASS 2
    @Test
    void shouldTransferMoneyBetweenValidAccounts() {
        // Given
        String fromAccount = "12345";
        String toAccount = "67890";
        double transferAmount = 500.0;
        
        Account sourceAccount = new Account(fromAccount, 1000.0);
        Account destinationAccount = new Account(toAccount, 500.0);
        
        when(accountRepository.findByAccountNumber(fromAccount)).thenReturn(sourceAccount);
        when(accountRepository.findByAccountNumber(toAccount)).thenReturn(destinationAccount);
        
        // When
        boolean transferResult = bankingService.transferMoney(fromAccount, toAccount, transferAmount);
        
        // Then
        assertTrue(transferResult);
        assertEquals(500.0, sourceAccount.getBalance());
        assertEquals(1000.0, destinationAccount.getBalance());
    }
}
```

**Run Test:** ❌ FAILS (transferMoney method doesn't exist)

### Step 5: GREEN - Make Test Pass

Update **BankingService.java** (replace entire content):

```java
public class BankingService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    
    public BankingService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    
    // EXISTING METHOD FROM PASS 1
    public double getAccountBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        return account.getBalance();
    }
    
    // NEW METHOD FOR PASS 2
    public boolean transferMoney(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);
        
        if (fromAccount.getBalance() >= amount) {
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
            
            accountRepository.updateAccount(fromAccount);
            accountRepository.updateAccount(toAccount);
            
            return true;
        }
        return false;
    }
}
```

**Run Test:** ✅ PASSES

### Step 6: REFACTOR - Clean Up Code

The code is still clean and readable. No refactoring needed.

***

## Pass 3: Transaction History Retrieval (RED-GREEN-REFACTOR)

### Step 7: RED - Add Failing Test

Update **BankingServiceTest.java** (replace entire content):

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BankingServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private TransactionRepository transactionRepository;
    
    private BankingService bankingService;
    
    @BeforeEach
    void setUp() {
        bankingService = new BankingService(accountRepository, transactionRepository);
    }
    
    // EXISTING TEST FROM PASS 1
    @Test
    void shouldReturnAccountBalanceForValidAccount() {
        // Given
        String accountNumber = "12345";
        Account mockAccount = new Account(accountNumber, 1000.0);
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(mockAccount);
        
        // When
        double balance = bankingService.getAccountBalance(accountNumber);
        
        // Then
        assertEquals(1000.0, balance);
    }
    
    // EXISTING TEST FROM PASS 2
    @Test
    void shouldTransferMoneyBetweenValidAccounts() {
        // Given
        String fromAccount = "12345";
        String toAccount = "67890";
        double transferAmount = 500.0;
        
        Account sourceAccount = new Account(fromAccount, 1000.0);
        Account destinationAccount = new Account(toAccount, 500.0);
        
        when(accountRepository.findByAccountNumber(fromAccount)).thenReturn(sourceAccount);
        when(accountRepository.findByAccountNumber(toAccount)).thenReturn(destinationAccount);
        
        // When
        boolean transferResult = bankingService.transferMoney(fromAccount, toAccount, transferAmount);
        
        // Then
        assertTrue(transferResult);
        assertEquals(500.0, sourceAccount.getBalance());
        assertEquals(1000.0, destinationAccount.getBalance());
    }
    
    // NEW TEST FOR PASS 3
    @Test
    void shouldReturnTransactionHistoryForValidAccount() {
        // Given
        String accountNumber = "12345";
        List<Transaction> mockTransactions = Arrays.asList(
            new Transaction(accountNumber, -200.0, "ATM Withdrawal", LocalDateTime.now().minusDays(1)),
            new Transaction(accountNumber, 1000.0, "Salary Deposit", LocalDateTime.now().minusDays(2))
        );
        
        when(transactionRepository.findByAccountNumber(accountNumber)).thenReturn(mockTransactions);
        
        // When
        List<Transaction> transactions = bankingService.getTransactionHistory(accountNumber);
        
        // Then
        assertEquals(2, transactions.size());
        assertEquals(-200.0, transactions.get(0).getAmount());
        assertEquals("ATM Withdrawal", transactions.get(0).getDescription());
        assertEquals(1000.0, transactions.get(1).getAmount());
        assertEquals("Salary Deposit", transactions.get(1).getDescription());
    }
}
```

**Run Test:** ❌ FAILS (getTransactionHistory method doesn't exist)

### Step 8: GREEN - Make Test Pass

Update **BankingService.java** (replace entire content):

```java
import java.util.List;

public class BankingService {
    
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    
    public BankingService(AccountRepository accountRepository, TransactionRepository transactionRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
    }
    
    // EXISTING METHOD FROM PASS 1
    public double getAccountBalance(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        return account.getBalance();
    }
    
    // EXISTING METHOD FROM PASS 2
    public boolean transferMoney(String fromAccountNumber, String toAccountNumber, double amount) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNumber);
        Account toAccount = accountRepository.findByAccountNumber(toAccountNumber);
        
        if (fromAccount.getBalance() >= amount) {
            fromAccount.setBalance(fromAccount.getBalance() - amount);
            toAccount.setBalance(toAccount.getBalance() + amount);
            
            accountRepository.updateAccount(fromAccount);
            accountRepository.updateAccount(toAccount);
            
            return true;
        }
        return false;
    }
    
    // NEW METHOD FOR PASS 3
    public List<Transaction> getTransactionHistory(String accountNumber) {
        return transactionRepository.findByAccountNumber(accountNumber);
    }
}
```

**Run Test:** ✅ PASSES

### Step 9: REFACTOR - Final Clean Up

The code is clean and follows good practices. All three tests pass successfully.

***

## Key Mockito Features Demonstrated

### 1. `when(T methodCall)` - Method Stubbing

```java
when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(mockAccount);
when(transactionRepository.findByAccountNumber(accountNumber)).thenReturn(mockTransactions);
```


### 2. `thenReturn(T value)` - Return Value Setting

```java
.thenReturn(mockAccount);
.thenReturn(mockTransactions);
```


## Assignment Complete!



**Final Test Results:** All 3 tests should pass ✅

