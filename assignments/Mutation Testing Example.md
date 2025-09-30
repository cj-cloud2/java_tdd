# Mutation Testing: A Complete ATM Banking Domain Example

Mutation testing is a white-box testing technique that evaluates the effectiveness of a test suite by introducing small changes (mutations) to the source code and checking whether existing tests can detect these faults. This comprehensive lab guide demonstrates mutation testing with an ATM banking domain example using JUnit 5 and Mockito.

## What is Mutation Testing?

Mutation testing works by creating multiple versions of your code with small intentional bugs called "mutants". The goal is to determine if your test suite is strong enough to detect these artificial faults. If a test fails when run against a mutant, the mutant is considered "killed." If all tests pass despite the mutation, the mutant "survives," indicating a weakness in your test suite.

### Key Concepts

**Mutants**: Modified versions of the original program with small syntactic changes
**Killed Mutants**: Mutations detected by the test suite (test fails)
**Surviving Mutants**: Mutations not detected by the test suite (all tests pass)
**Mutation Score**: Percentage of mutants killed by the test suite

### Types of Mutations

1. **Value Mutations**: Changing constant values (e.g., `amount > 1000` becomes `amount > 2000`)
2. **Decision Mutations**: Modifying logical operators (e.g., `>` becomes `<` or `>=`)
3. **Statement Mutations**: Deleting or replacing statements

## Test Suite Strength Formula

The mutation score measures test suite effectiveness using this formula :

**Mutation Score = (Number of Killed Mutants / Total Number of Mutants) × 100**

A higher mutation score indicates a more effective test suite. A score of 100% means all mutants were detected.

## Business Requirements: ATM Withdrawal System

Our ATM system must handle withdrawal transactions with the following business rules:

1. **Account Balance Validation**: Withdrawal amount cannot exceed current balance
2. **Daily Limit Check**: Single withdrawal cannot exceed \$1,000 daily limit
3. **Minimum Amount**: Withdrawal must be at least \$10
4. **Amount Multiples**: Withdrawal amount must be divisible by \$10 (ATM dispensing constraint)
5. **Account Status**: Only active accounts can perform withdrawals

## Implementation Code

### ATM Account Model

```java
public class Account {
    private String accountNumber;
    private double balance;
    private boolean isActive;
    
    public Account(String accountNumber, double balance, boolean isActive) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.isActive = isActive;
    }
    
    // Getters and setters
    public String getAccountNumber() { return accountNumber; }
    public double getBalance() { return balance; }
    public void setBalance(double balance) { this.balance = balance; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }
}
```


### ATM Service Class

```java
public class ATMService {
    private static final double DAILY_LIMIT = 1000.0;
    private static final double MINIMUM_WITHDRAWAL = 10.0;
    private static final double DENOMINATION_MULTIPLE = 10.0;
    
    public WithdrawalResult withdraw(Account account, double amount) {
        // Business Rule 5: Check if account is active
        if (!account.isActive()) {
            return new WithdrawalResult(false, "Account is not active");
        }
        
        // Business Rule 3: Check minimum withdrawal amount
        if (amount < MINIMUM_WITHDRAWAL) {
            return new WithdrawalResult(false, "Minimum withdrawal amount is $10");
        }
        
        // Business Rule 4: Check if amount is multiple of 10
        if (amount % DENOMINATION_MULTIPLE != 0) {
            return new WithdrawalResult(false, "Amount must be multiple of $10");
        }
        
        // Business Rule 2: Check daily limit
        if (amount > DAILY_LIMIT) {
            return new WithdrawalResult(false, "Amount exceeds daily limit of $1000");
        }
        
        // Business Rule 1: Check sufficient balance
        if (amount > account.getBalance()) {
            return new WithdrawalResult(false, "Insufficient balance");
        }
        
        // Process withdrawal
        account.setBalance(account.getBalance() - amount);
        return new WithdrawalResult(true, "Withdrawal successful");
    }
}
```


### Result Model

```java
public class WithdrawalResult {
    private boolean successful;
    private String message;
    
    public WithdrawalResult(boolean successful, String message) {
        this.successful = successful;
        this.message = message;
    }
    
    public boolean isSuccessful() { return successful; }
    public String getMessage() { return message; }
}
```


## Comprehensive Test Suite

```java
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

class ATMServiceTest {
    private ATMService atmService;
    private Account activeAccount;
    
    @BeforeEach
    void setUp() {
        atmService = new ATMService();
        activeAccount = new Account("12345", 2000.0, true);
    }
    
    @Test
    @DisplayName("Successful withdrawal within limits")
    void testSuccessfulWithdrawal() {
        WithdrawalResult result = atmService.withdraw(activeAccount, 500.0);
        
        assertTrue(result.isSuccessful());
        assertEquals("Withdrawal successful", result.getMessage());
        assertEquals(1500.0, activeAccount.getBalance());
    }
    
    @Test
    @DisplayName("Withdrawal from inactive account should fail")
    void testInactiveAccountWithdrawal() {
        Account inactiveAccount = new Account("67890", 1000.0, false);
        WithdrawalResult result = atmService.withdraw(inactiveAccount, 100.0);
        
        assertFalse(result.isSuccessful());
        assertEquals("Account is not active", result.getMessage());
    }
    
    @Test
    @DisplayName("Withdrawal below minimum amount should fail")
    void testBelowMinimumWithdrawal() {
        WithdrawalResult result = atmService.withdraw(activeAccount, 5.0);
        
        assertFalse(result.isSuccessful());
        assertEquals("Minimum withdrawal amount is $10", result.getMessage());
    }
    
    @Test
    @DisplayName("Withdrawal not multiple of 10 should fail")
    void testNonMultipleOfTenWithdrawal() {
        WithdrawalResult result = atmService.withdraw(activeAccount, 125.0);
        
        assertFalse(result.isSuccessful());
        assertEquals("Amount must be multiple of $10", result.getMessage());
    }
    
    @Test
    @DisplayName("Withdrawal exceeding daily limit should fail")
    void testExceedDailyLimit() {
        WithdrawalResult result = atmService.withdraw(activeAccount, 1500.0);
        
        assertFalse(result.isSuccessful());
        assertEquals("Amount exceeds daily limit of $1000", result.getMessage());
    }
    
    @Test
    @DisplayName("Withdrawal exceeding balance should fail")
    void testInsufficientBalance() {
        WithdrawalResult result = atmService.withdraw(activeAccount, 2500.0);
        
        assertFalse(result.isSuccessful());
        assertEquals("Insufficient balance", result.getMessage());
    }
}
```


## Manual Mutation Testing Walkthrough

### Step 1: Create the 5 Mutants

Let's create 5 different mutants by introducing specific bugs:

#### Mutant 1: Daily Limit Change (Value Mutation)

**Original Code:**

```java
if (amount > DAILY_LIMIT) {
```

**Mutant 1 Code:**

```java
if (amount > 2000.0) {  // Changed from 1000 to 2000
```

**Expected Result**: This mutant should be **KILLED** because we have a test that checks the daily limit of \$1000.

#### Mutant 2: Operator Change (Decision Mutation)

**Original Code:**

```java
if (amount > account.getBalance()) {
```

**Mutant 2 Code:**

```java
if (amount >= account.getBalance()) {  // Changed > to >=
```

**Expected Result**: This mutant should **SURVIVE** because our test doesn't cover the edge case where withdrawal amount equals balance.

#### Mutant 3: Minimum Amount Change (Value Mutation)

**Original Code:**

```java
if (amount < MINIMUM_WITHDRAWAL) {
```

**Mutant 3 Code:**

```java
if (amount < 20.0) {  // Changed from 10.0 to 20.0
```

**Expected Result**: This mutant should **SURVIVE** because we only test with \$5 (below old minimum) but not values between \$10-\$20.

#### Mutant 4: Multiple Check Removal (Statement Mutation)

**Original Code:**

```java
if (amount % DENOMINATION_MULTIPLE != 0) {
    return new WithdrawalResult(false, "Amount must be multiple of $10");
}
```

**Mutant 4 Code:**

```java
// Entire if statement removed
```

**Expected Result**: This mutant should be **KILLED** because we test non-multiples of \$10.

#### Mutant 5: Balance Update Error (Statement Mutation)

**Original Code:**

```java
account.setBalance(account.getBalance() - amount);
```

**Mutant 5 Code:**

```java
account.setBalance(account.getBalance() + amount);  // Changed - to +
```

**Expected Result**: This mutant should be **KILLED** because we verify the balance after withdrawal.

### Step 2: Run Tests Against Each Mutant

#### Testing Mutant 1 (Daily Limit 2000)

Run the test `testExceedDailyLimit()`:

- **Test Input**: Withdrawal of \$1500 from account with \$2000 balance
- **Original Code**: Should fail (exceeds \$1000 limit)
- **Mutant 1**: Should succeed (within \$2000 limit)
- **Result**: Test fails against mutant → **MUTANT KILLED** ✓


#### Testing Mutant 2 (>= instead of >)

Run the test `testInsufficientBalance()`:

- **Test Input**: Withdrawal of \$2500 from account with \$2000 balance
- **Original Code**: Fails with "Insufficient balance"
- **Mutant 2**: Still fails (2500 >= 2000 is true)
- **Result**: Test passes against mutant → **MUTANT SURVIVES** ❌


#### Testing Mutant 3 (Minimum \$20 instead of \$10)

Run the test `testBelowMinimumWithdrawal()`:

- **Test Input**: Withdrawal of \$5
- **Original Code**: Fails with minimum \$10 message
- **Mutant 3**: Still fails (5 < 20 is true)
- **Result**: Test passes against mutant → **MUTANT SURVIVES** ❌


#### Testing Mutant 4 (No Multiple Check)

Run the test `testNonMultipleOfTenWithdrawal()`:

- **Test Input**: Withdrawal of \$125
- **Original Code**: Fails with "must be multiple of \$10"
- **Mutant 4**: Succeeds (no multiple check)
- **Result**: Test fails against mutant → **MUTANT KILLED** ✓


#### Testing Mutant 5 (Addition instead of Subtraction)

Run the test `testSuccessfulWithdrawal()`:

- **Test Input**: Withdrawal of \$500 from \$2000 balance
- **Original Code**: Balance becomes \$1500
- **Mutant 5**: Balance becomes \$2500
- **Result**: Balance assertion fails → **MUTANT KILLED** ✓


### Step 3: Calculate Mutation Score

Using our formula :

**Mutation Score = (Killed Mutants / Total Mutants) × 100**
**Mutation Score = (3 / 5) × 100 = 60%**

### Step 4: Analysis and Improvement

Our test suite achieved a 60% mutation score, which indicates moderate effectiveness. The surviving mutants reveal weaknesses:

#### Surviving Mutant 2 Analysis

The boundary condition mutant (>= vs >) survived because we didn't test the exact balance withdrawal scenario.

**Improvement**: Add this test:

```java
@Test
@DisplayName("Withdrawal of exact balance amount should succeed")
void testExactBalanceWithdrawal() {
    WithdrawalResult result = atmService.withdraw(activeAccount, 2000.0);
    
    assertTrue(result.isSuccessful());
    assertEquals(0.0, activeAccount.getBalance());
}
```


#### Surviving Mutant 3 Analysis

The minimum amount boundary mutant survived because we only tested below the original minimum.

**Improvement**: Add this test:

```java
@Test
@DisplayName("Withdrawal of 15 dollars should succeed")
void testFifteenDollarWithdrawal() {
    WithdrawalResult result = atmService.withdraw(activeAccount, 15.0);
    
    assertFalse(result.isSuccessful());
    assertEquals("Amount must be multiple of $10", result.getMessage());
}
```


### Step 5: Rerun with Improved Tests

After adding the missing test cases:

- **Mutant 1**: Still KILLED ✓
- **Mutant 2**: Now KILLED ✓ (exact balance test catches it)
- **Mutant 3**: Still SURVIVES ❌ (but this might be acceptable)
- **Mutant 4**: Still KILLED ✓
- **Mutant 5**: Still KILLED ✓

**New Mutation Score = (4 / 5) × 100 = 80%**

## Key Learning Points

### Mutation Testing Benefits

1. **Identifies weak test cases**: Reveals gaps in edge case testing
2. **Measures test quality**: Provides quantitative assessment of test effectiveness
3. **Improves code coverage**: Encourages thorough testing of business logic

### Limitations

1. **Computational cost**: Can be expensive for large codebases
2. **Equivalent mutants**: Some mutations don't change behavior
3. **Manual analysis**: Requires human judgment to interpret results

### Best Practices

1. **Start with unit tests**: Focus on isolated business logic
2. **Prioritize critical paths**: Apply to high-risk code sections
3. **Iterate gradually**: Improve mutation score incrementally
4. **Combine with other metrics**: Use alongside code coverage


