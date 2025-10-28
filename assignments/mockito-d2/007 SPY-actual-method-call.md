# Mockito Assignment 13: Banking Transaction Processor using Spy

## Introduction to Spy in Mockito

In this assignment, we are learning **Spy** in Mockito. Unlike regular mocks that are created from interfaces, a **Spy** wraps around a **real class instance** and allows us to:

1. **Mock specific methods** while keeping other methods' real implementation
2. **Call actual methods** of the class when no stubbing is defined
3. **Partially mock** an object - useful when you want to test real behavior but control certain dependencies

### Key Difference: Mock vs Spy

- **Mock**: Creates a completely fake object. All methods return default values (null, 0, false) unless stubbed
- **Spy**: Wraps a real object. Calls real methods unless specifically stubbed

### When to Use Spy

- When you want to test a class but override certain method behaviors
- When you need partial mocking of a concrete class
- When testing legacy code with complex dependencies

In this assignment, we will:
- **Pass 1**: Call a **mocked method** (stubbed behavior using when().thenReturn())
- **Pass 2**: Call an **actual method** (real implementation without stubbing)
- **Pass 3**: Mix both - demonstrate partial mocking capability

---

## Business Requirements

**Domain Context:** A bank needs to implement a transaction processing system that calculates fees, validates transactions, and processes payments for customer accounts.

### Primary Business Requirements:

1. **Transaction Fee Calculation**: The system must calculate transaction fees based on transaction amount (2% fee for amounts > $1000, 1% for smaller amounts)
2. **Transaction Validation**: The system must validate transactions by checking if account has sufficient balance before processing
3. **Transaction Processing**: The system must process validated transactions by applying fees and updating account balance

---

## Testable Requirements Derived from Business Requirements

### Requirement 1: Transaction Fee Calculation with Mocked Behavior

- **Given** a transaction amount of $5000
- **When** the fee calculation method is stubbed to return a fixed fee of $50
- **Then** the system should return $50 (demonstrating mocked method call)

### Requirement 2: Transaction Validation with Actual Method Call

- **Given** an account with balance $10,000 and a transaction amount of $2,000
- **When** the validation method is called without stubbing (actual method execution)
- **Then** the system should return true indicating sufficient balance (demonstrating actual method call)

### Requirement 3: Transaction Processing with Partial Mocking

- **Given** an account with balance $5,000 and transaction amount $1,000
- **When** fee calculation is mocked to return $25 but balance update uses real implementation
- **Then** the system should process transaction with final balance of $3,975 (demonstrating mixed behavior)

---

## Purpose

This assignment demonstrates Mockito Spy features:

1. **spy()**: Creating a spy wrapper around a real class instance
2. **when().thenReturn()**: Stubbing specific methods while keeping others real
3. **Actual method calls**: Allowing real implementation to execute
4. **Partial mocking**: Combining mocked and real behaviors in the same test

---

## PASS 1: RED-GREEN-REFACTOR (Requirement 1) 

### Understanding Requirement 1

In this pass, we will **mock a method** of a real class using Spy. The `calculateFee()` method will be stubbed to return a fixed value instead of executing its real implementation. This demonstrates how Spy allows selective method stubbing.

### RED Phase - Write Failing Test

**File:** `src/test/java/edu/m013/TransactionProcessorTest.java`

```java
package edu.m013;

// Import JUnit 5 testing framework annotations and assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito framework for spying on real objects
import static org.mockito.Mockito.*;

// FIXED: Test class using programmatic spy creation instead of annotations
// This approach avoids the PowerMock conflict and annotation initialization issues
public class TransactionProcessorTest {
    
    // Declare spy variable without @Spy annotation to avoid initialization conflicts
    private TransactionProcessor transactionProcessor;
    
    // Setup method executed before each test method
    @BeforeEach
    void setUp() {
        // FIXED: Create spy programmatically using Mockito.spy()
        // This avoids MockitoAnnotations.openMocks() which was causing conflicts
        // spy() wraps a real TransactionProcessor instance
        transactionProcessor = spy(new TransactionProcessor());
    }
    
    // Test case for Requirement 1: Mocked Fee Calculation
    // This test demonstrates calling a MOCKED method using spy
    @Test
    void shouldReturnMockedFeeWhenMethodIsStubbed() {
        // Arrange: Define transaction amount
        double transactionAmount = 5000.0;
        
        // Act & Assert: This will fail initially as class doesn't exist
        // Call the fee calculation method which will be stubbed
        double fee = transactionProcessor.calculateFee(transactionAmount);
        assertEquals(50.0, fee);
    }
}
```

**Expected Result:** Compilation errors - `TransactionProcessor` class doesn't exist yet.

---

### GREEN Phase - Make Test Pass

**File:** `src/main/java/edu/m013/TransactionProcessor.java`

```java
package edu.m013;

// Real class for processing banking transactions
// This is a CONCRETE class (not interface) which will be spied on
public class TransactionProcessor {
    
    // Real method to calculate transaction fee based on amount
    // Returns 2% for amounts > $1000, otherwise 1%
    public double calculateFee(double transactionAmount) {
        // Check if transaction amount exceeds threshold
        if (transactionAmount > 1000.0) {
            // Calculate 2% fee for large transactions
            return transactionAmount * 0.02;
        } else {
            // Calculate 1% fee for small transactions
            return transactionAmount * 0.01;
        }
    }
}
```

**Updated Test File with Stubbing:** `src/test/java/edu/m013/TransactionProcessorTest.java`

```java
package edu.m013;

// Import JUnit 5 testing framework annotations and assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito framework for spying on real objects
import static org.mockito.Mockito.*;

// FIXED: Test class using programmatic spy creation instead of annotations
// This approach avoids the PowerMock conflict and annotation initialization issues
public class TransactionProcessorTest {
    
    // Declare spy variable without @Spy annotation to avoid initialization conflicts
    private TransactionProcessor transactionProcessor;
    
    // Setup method executed before each test method
    @BeforeEach
    void setUp() {
        // FIXED: Create spy programmatically using Mockito.spy()
        // This avoids MockitoAnnotations.openMocks() which was causing conflicts
        // spy() wraps a real TransactionProcessor instance
        transactionProcessor = spy(new TransactionProcessor());
    }
    
    // Test case for Requirement 1: Mocked Fee Calculation
    // This test demonstrates calling a MOCKED method using spy
    @Test
    void shouldReturnMockedFeeWhenMethodIsStubbed() {
        // Arrange: Define transaction amount
        double transactionAmount = 5000.0;
        
        // Stub the calculateFee method to return fixed value (MOCKED BEHAVIOR)
        // when() specifies which method call to stub
        // thenReturn() defines the mocked return value
        // Real implementation will NOT execute for this stubbed method
        when(transactionProcessor.calculateFee(transactionAmount)).thenReturn(50.0);
        
        // Act: Call the mocked method on spy object
        double fee = transactionProcessor.calculateFee(transactionAmount);
        
        // Assert: Verify mocked value is returned (not real calculation of $100)
        // Real method would return $100 (5000 * 0.02), but we get mocked $50
        assertEquals(50.0, fee);
        
        // Verify the method was called exactly once
        verify(transactionProcessor, times(1)).calculateFee(transactionAmount);
    }
}
```

**Expected Result:** Test passes ✅

---

### REFACTOR Phase

No refactoring needed at this stage - code is simple and clean.

---

## PASS 2: RED-GREEN-REFACTOR (Requirement 2) - FIXED VERSION

### Understanding Requirement 2

In this pass, we will call an **actual method** without stubbing it. The `validateTransaction()` method will execute its real implementation. This demonstrates that Spy allows real methods to run when no stubbing is applied.

### RED Phase - Add Failing Test

**Updated Test File:** `src/test/java/edu/m013/TransactionProcessorTest.java`

```java
package edu.m013;

// Import JUnit 5 testing framework annotations and assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito framework for spying on real objects
import static org.mockito.Mockito.*;

// FIXED: Test class using programmatic spy creation instead of annotations
// This approach avoids the PowerMock conflict and annotation initialization issues
public class TransactionProcessorTest {
    
    // Declare spy variable without @Spy annotation to avoid initialization conflicts
    private TransactionProcessor transactionProcessor;
    
    // Setup method executed before each test method
    @BeforeEach
    void setUp() {
        // FIXED: Create spy programmatically using Mockito.spy()
        // This avoids MockitoAnnotations.openMocks() which was causing conflicts
        // spy() wraps a real TransactionProcessor instance
        transactionProcessor = spy(new TransactionProcessor());
    }
    
    // Test case for Requirement 1: Mocked Fee Calculation
    // This test demonstrates calling a MOCKED method using spy
    @Test
    void shouldReturnMockedFeeWhenMethodIsStubbed() {
        // Arrange: Define transaction amount
        double transactionAmount = 5000.0;
        
        // Stub the calculateFee method to return fixed value (MOCKED BEHAVIOR)
        // when() specifies which method call to stub
        // thenReturn() defines the mocked return value
        // Real implementation will NOT execute for this stubbed method
        when(transactionProcessor.calculateFee(transactionAmount)).thenReturn(50.0);
        
        // Act: Call the mocked method on spy object
        double fee = transactionProcessor.calculateFee(transactionAmount);
        
        // Assert: Verify mocked value is returned (not real calculation of $100)
        // Real method would return $100 (5000 * 0.02), but we get mocked $50
        assertEquals(50.0, fee);
        
        // Verify the method was called exactly once
        verify(transactionProcessor, times(1)).calculateFee(transactionAmount);
    }
    
    // Test case for Requirement 2: Actual Method Call Without Stubbing (NEW TEST)
    // This test demonstrates calling the ACTUAL method implementation
    @Test
    void shouldCallActualValidationMethodWhenNotStubbed() {
        // Arrange: Define account balance and transaction amount
        double accountBalance = 10000.0;
        double transactionAmount = 2000.0;
        
        // Act & Assert: This will fail initially as method doesn't exist
        // NO stubbing here - method will execute real implementation
        boolean isValid = transactionProcessor.validateTransaction(accountBalance, transactionAmount);
        assertTrue(isValid);
    }
}
```

**Expected Result:** Compilation error - `validateTransaction` method doesn't exist.

---

### GREEN Phase - Make Test Pass

**Updated Implementation File:** `src/main/java/edu/m013/TransactionProcessor.java`

```java
package edu.m013;

// Real class for processing banking transactions
// This is a CONCRETE class (not interface) which will be spied on
public class TransactionProcessor {
    
    // Real method to calculate transaction fee based on amount
    // Returns 2% for amounts > $1000, otherwise 1%
    public double calculateFee(double transactionAmount) {
        // Check if transaction amount exceeds threshold
        if (transactionAmount > 1000.0) {
            // Calculate 2% fee for large transactions
            return transactionAmount * 0.02;
        } else {
            // Calculate 1% fee for small transactions
            return transactionAmount * 0.01;
        }
    }
    
    // NEW METHOD: Real method to validate if account has sufficient balance
    // Returns true if balance is sufficient, false otherwise
    public boolean validateTransaction(double accountBalance, double transactionAmount) {
        // Check if account balance is greater than or equal to transaction amount
        return accountBalance >= transactionAmount;
    }
}
```

**Updated Test File:** `src/test/java/edu/m013/TransactionProcessorTest.java`

```java
package edu.m013;

// Import JUnit 5 testing framework annotations and assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito framework for spying on real objects
import static org.mockito.Mockito.*;

// FIXED: Test class using programmatic spy creation instead of annotations
// This approach avoids the PowerMock conflict and annotation initialization issues
public class TransactionProcessorTest {
    
    // Declare spy variable without @Spy annotation to avoid initialization conflicts
    private TransactionProcessor transactionProcessor;
    
    // Setup method executed before each test method
    @BeforeEach
    void setUp() {
        // FIXED: Create spy programmatically using Mockito.spy()
        // This avoids MockitoAnnotations.openMocks() which was causing conflicts
        // spy() wraps a real TransactionProcessor instance
        transactionProcessor = spy(new TransactionProcessor());
    }
    
    // Test case for Requirement 1: Mocked Fee Calculation
    // This test demonstrates calling a MOCKED method using spy
    @Test
    void shouldReturnMockedFeeWhenMethodIsStubbed() {
        // Arrange: Define transaction amount
        double transactionAmount = 5000.0;
        
        // Stub the calculateFee method to return fixed value (MOCKED BEHAVIOR)
        // when() specifies which method call to stub
        // thenReturn() defines the mocked return value
        // Real implementation will NOT execute for this stubbed method
        when(transactionProcessor.calculateFee(transactionAmount)).thenReturn(50.0);
        
        // Act: Call the mocked method on spy object
        double fee = transactionProcessor.calculateFee(transactionAmount);
        
        // Assert: Verify mocked value is returned (not real calculation of $100)
        // Real method would return $100 (5000 * 0.02), but we get mocked $50
        assertEquals(50.0, fee);
        
        // Verify the method was called exactly once
        verify(transactionProcessor, times(1)).calculateFee(transactionAmount);
    }
    
    // Test case for Requirement 2: Actual Method Call Without Stubbing
    // This test demonstrates calling the ACTUAL method implementation
    @Test
    void shouldCallActualValidationMethodWhenNotStubbed() {
        // Arrange: Define account balance and transaction amount
        double accountBalance = 10000.0;
        double transactionAmount = 2000.0;
        
        // NO STUBBING - The real validateTransaction method will execute
        // This demonstrates spy calling actual implementation
        
        // Act: Call the real method on spy object (no mocking applied)
        boolean isValid = transactionProcessor.validateTransaction(accountBalance, transactionAmount);
        
        // Assert: Verify actual method logic executes correctly
        // Real method checks if 10000 >= 2000, which returns true
        assertTrue(isValid);
        
        // Verify the actual method was called exactly once
        verify(transactionProcessor, times(1)).validateTransaction(accountBalance, transactionAmount);
    }
}
```

**Expected Result:** Test passes ✅

---

### REFACTOR Phase

No refactoring needed - code remains clean and focused.

---

## PASS 3: RED-GREEN-REFACTOR (Requirement 3) - FIXED VERSION

### Understanding Requirement 3

In this pass, we will demonstrate **partial mocking** by stubbing one method (`calculateFee`) while allowing another method (`processTransaction`) to execute its real implementation. This shows the power of Spy - mixing mocked and real behaviors.

### RED Phase - Add Failing Test

**Updated Test File:** `src/test/java/edu/m013/TransactionProcessorTest.java`

```java
package edu.m013;

// Import JUnit 5 testing framework annotations and assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito framework for spying on real objects
import static org.mockito.Mockito.*;

// FIXED: Test class using programmatic spy creation instead of annotations
// This approach avoids the PowerMock conflict and annotation initialization issues
public class TransactionProcessorTest {
    
    // Declare spy variable without @Spy annotation to avoid initialization conflicts
    private TransactionProcessor transactionProcessor;
    
    // Setup method executed before each test method
    @BeforeEach
    void setUp() {
        // FIXED: Create spy programmatically using Mockito.spy()
        // This avoids MockitoAnnotations.openMocks() which was causing conflicts
        // spy() wraps a real TransactionProcessor instance
        transactionProcessor = spy(new TransactionProcessor());
    }
    
    // Test case for Requirement 1: Mocked Fee Calculation
    // This test demonstrates calling a MOCKED method using spy
    @Test
    void shouldReturnMockedFeeWhenMethodIsStubbed() {
        // Arrange: Define transaction amount
        double transactionAmount = 5000.0;
        
        // Stub the calculateFee method to return fixed value (MOCKED BEHAVIOR)
        // when() specifies which method call to stub
        // thenReturn() defines the mocked return value
        // Real implementation will NOT execute for this stubbed method
        when(transactionProcessor.calculateFee(transactionAmount)).thenReturn(50.0);
        
        // Act: Call the mocked method on spy object
        double fee = transactionProcessor.calculateFee(transactionAmount);
        
        // Assert: Verify mocked value is returned (not real calculation of $100)
        // Real method would return $100 (5000 * 0.02), but we get mocked $50
        assertEquals(50.0, fee);
        
        // Verify the method was called exactly once
        verify(transactionProcessor, times(1)).calculateFee(transactionAmount);
    }
    
    // Test case for Requirement 2: Actual Method Call Without Stubbing
    // This test demonstrates calling the ACTUAL method implementation
    @Test
    void shouldCallActualValidationMethodWhenNotStubbed() {
        // Arrange: Define account balance and transaction amount
        double accountBalance = 10000.0;
        double transactionAmount = 2000.0;
        
        // NO STUBBING - The real validateTransaction method will execute
        // This demonstrates spy calling actual implementation
        
        // Act: Call the real method on spy object (no mocking applied)
        boolean isValid = transactionProcessor.validateTransaction(accountBalance, transactionAmount);
        
        // Assert: Verify actual method logic executes correctly
        // Real method checks if 10000 >= 2000, which returns true
        assertTrue(isValid);
        
        // Verify the actual method was called exactly once
        verify(transactionProcessor, times(1)).validateTransaction(accountBalance, transactionAmount);
    }
    
    // Test case for Requirement 3: Partial Mocking (NEW TEST)
    // This test demonstrates MIXING mocked and actual method calls
    @Test
    void shouldProcessTransactionWithPartialMocking() {
        // Arrange: Define initial balance and transaction amount
        double initialBalance = 5000.0;
        double transactionAmount = 1000.0;
        
        // Act & Assert: This will fail initially as method doesn't exist
        // We will stub calculateFee but let processTransaction call real implementation
        double finalBalance = transactionProcessor.processTransaction(initialBalance, transactionAmount);
        assertEquals(3975.0, finalBalance, 0.01);
    }
}
```

**Expected Result:** Compilation error - `processTransaction` method doesn't exist.

---

### GREEN Phase - Make Test Pass

**Updated Implementation File:** `src/main/java/edu/m013/TransactionProcessor.java`

```java
package edu.m013;

// Real class for processing banking transactions
// This is a CONCRETE class (not interface) which will be spied on
public class TransactionProcessor {
    
    // Real method to calculate transaction fee based on amount
    // Returns 2% for amounts > $1000, otherwise 1%
    public double calculateFee(double transactionAmount) {
        // Check if transaction amount exceeds threshold
        if (transactionAmount > 1000.0) {
            // Calculate 2% fee for large transactions
            return transactionAmount * 0.02;
        } else {
            // Calculate 1% fee for small transactions
            return transactionAmount * 0.01;
        }
    }
    
    // Real method to validate if account has sufficient balance
    // Returns true if balance is sufficient, false otherwise
    public boolean validateTransaction(double accountBalance, double transactionAmount) {
        // Check if account balance is greater than or equal to transaction amount
        return accountBalance >= transactionAmount;
    }
    
    // NEW METHOD: Real method to process transaction by deducting amount and fee
    // Returns final balance after transaction and fee deduction
    public double processTransaction(double accountBalance, double transactionAmount) {
        // Calculate transaction fee using calculateFee method
        double fee = calculateFee(transactionAmount);
        
        // Calculate final balance: initial balance - transaction amount - fee
        double finalBalance = accountBalance - transactionAmount - fee;
        
        // Return the final balance after transaction
        return finalBalance;
    }
}
```

**Updated Test File with Partial Mocking:** `src/test/java/edu/m013/TransactionProcessorTest.java`

```java
package edu.m013;

// Import JUnit 5 testing framework annotations and assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.a pi.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito framework for spying on real objects
import static org.mockito.Mockito.*;

// FIXED: Test class using programmatic spy creation instead of annotations
// This approach avoids the PowerMock conflict and annotation initialization issues
public class TransactionProcessorTest {
    
    // Declare spy variable without @Spy annotation to avoid initialization conflicts
    private TransactionProcessor transactionProcessor;
    
    // Setup method executed before each test method
    @BeforeEach
    void setUp() {
        // FIXED: Create spy programmatically using Mockito.spy()
        // This avoids MockitoAnnotations.openMocks() which was causing conflicts
        // spy() wraps a real TransactionProcessor instance
        transactionProcessor = spy(new TransactionProcessor());
    }
    
    // Test case for Requirement 1: Mocked Fee Calculation
    // This test demonstrates calling a MOCKED method using spy
    @Test
    void shouldReturnMockedFeeWhenMethodIsStubbed() {
        // Arrange: Define transaction amount
        double transactionAmount = 5000.0;
        
        // Stub the calculateFee method to return fixed value (MOCKED BEHAVIOR)
        // when() specifies which method call to stub
        // thenReturn() defines the mocked return value
        // Real implementation will NOT execute for this stubbed method
        when(transactionProcessor.calculateFee(transactionAmount)).thenReturn(50.0);
        
        // Act: Call the mocked method on spy object
        double fee = transactionProcessor.calculateFee(transactionAmount);
        
        // Assert: Verify mocked value is returned (not real calculation of $100)
        // Real method would return $100 (5000 * 0.02), but we get mocked $50
        assertEquals(50.0, fee);
        
        // Verify the method was called exactly once
        verify(transactionProcessor, times(1)).calculateFee(transactionAmount);
    }
    
    // Test case for Requirement 2: Actual Method Call Without Stubbing
    // This test demonstrates calling the ACTUAL method implementation
    @Test
    void shouldCallActualValidationMethodWhenNotStubbed() {
        // Arrange: Define account balance and transaction amount
        double accountBalance = 10000.0;
        double transactionAmount = 2000.0;
        
        // NO STUBBING - The real validateTransaction method will execute
        // This demonstrates spy calling actual implementation
        
        // Act: Call the real method on spy object (no mocking applied)
        boolean isValid = transactionProcessor.validateTransaction(accountBalance, transactionAmount);
        
        // Assert: Verify actual method logic executes correctly
        // Real method checks if 10000 >= 2000, which returns true
        assertTrue(isValid);
        
        // Verify the actual method was called exactly once
        verify(transactionProcessor, times(1)).validateTransaction(accountBalance, transactionAmount);
    }
    
    // Test case for Requirement 3: Partial Mocking
    // This test demonstrates MIXING mocked and actual method calls
    @Test
    void shouldProcessTransactionWithPartialMocking() {
        // Arrange: Define initial balance and transaction amount
        double initialBalance = 5000.0;
        double transactionAmount = 1000.0;
        
        // Stub calculateFee to return fixed value of $25 (MOCKED)
        // Without stubbing, real method would calculate: 1000 * 0.01 = $10
        when(transactionProcessor.calculateFee(transactionAmount)).thenReturn(25.0);
        
        // Act: Call processTransaction which will:
        // 1. Call MOCKED calculateFee (returns $25, not real $10)
        // 2. Execute REAL balance calculation logic
        // This is PARTIAL MOCKING - mixing mocked and real behaviors
        double finalBalance = transactionProcessor.processTransaction(initialBalance, transactionAmount);
        
        // Assert: Verify final balance calculation
        // Expected: 5000 - 1000 - 25 = 3975
        // If calculateFee wasn't mocked: 5000 - 1000 - 10 = 3990
        assertEquals(3975.0, finalBalance, 0.01);
        
        // Verify calculateFee was called once (mocked version)
        verify(transactionProcessor, times(1)).calculateFee(transactionAmount);
        
        // Verify processTransaction was called once (real version)
        verify(transactionProcessor, times(1)).processTransaction(initialBalance, transactionAmount);
    }
}
```

**Expected Result:** All tests pass ✅

---

### REFACTOR Phase - Final Cleanup

**Final Test File:** `src/test/java/edu/m013/TransactionProcessorTest.java`

```java
package edu.m013;

// Import JUnit 5 testing framework annotations and assertions
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

// Import Mockito framework for spying on real objects
import static org.mockito.Mockito.*;

// FIXED: Test class using programmatic spy creation instead of annotations
// This approach avoids dependency conflicts and works reliably with JDK 24
public class TransactionProcessorTest {
    
    // Declare spy variable without @Spy annotation to avoid initialization conflicts
    private TransactionProcessor transactionProcessor;
    
    // Setup method executed before each test method
    @BeforeEach
    void setUp() {
        // FIXED: Create spy programmatically using Mockito.spy()
        // Benefits of this approach:
        // 1. Avoids MockitoAnnotations.openMocks() dependency conflicts
        // 2. Works reliably with JDK 24 and newer Mockito versions
        // 3. No PowerMock interference
        // 4. Direct control over spy creation
        transactionProcessor = spy(new TransactionProcessor());
    }
    
    // Test case for Requirement 1: Mocked Fee Calculation
    // Demonstrates calling a MOCKED method - stubbed behavior overrides real implementation
    @Test
    void shouldReturnMockedFeeWhenMethodIsStubbed() {
        // Arrange: Define transaction amount
        double transactionAmount = 5000.0;
        
        // Stub the calculateFee method to return fixed value (MOCKED BEHAVIOR)
        // when() specifies which method call to stub
        // thenReturn() defines the mocked return value
        // Real implementation will NOT execute for this stubbed method
        when(transactionProcessor.calculateFee(transactionAmount)).thenReturn(50.0);
        
        // Act: Call the mocked method on spy object
        double fee = transactionProcessor.calculateFee(transactionAmount);
        
        // Assert: Verify mocked value is returned (not real calculation of $100)
        // Real method would return $100 (5000 * 0.02), but we get mocked $50
        // This proves the method is stubbed and not calling real implementation
        assertEquals(50.0, fee, "Mocked fee should be returned instead of calculated value");
        
        // Verify the method was called exactly once with correct parameter
        verify(transactionProcessor, times(1)).calculateFee(transactionAmount);
    }
    
    // Test case for Requirement 2: Actual Method Call Without Stubbing
    // Demonstrates calling the ACTUAL method - real implementation executes
    @Test
    void shouldCallActualValidationMethodWhenNotStubbed() {
        // Arrange: Define account balance and transaction amount
        double accountBalance = 10000.0;
        double transactionAmount = 2000.0;
        
        // NO STUBBING HERE - The real validateTransaction method will execute
        // This demonstrates spy calling actual implementation when not stubbed
        // This is the key difference between spy and mock
        
        // Act: Call the real method on spy object (no mocking applied)
        boolean isValid = transactionProcessor.validateTransaction(accountBalance, transactionAmount);
        
        // Assert: Verify actual method logic executes correctly
        // Real method checks if 10000 >= 2000, which returns true
        // This proves the real implementation ran, not a stubbed version
        assertTrue(isValid, "Transaction should be valid when balance is sufficient");
        
        // Verify the actual method was called exactly once with correct parameters
        verify(transactionProcessor, times(1)).validateTransaction(accountBalance, transactionAmount);
    }
    
    // Test case for Requirement 3: Partial Mocking - Mixing Mocked and Real Methods
    // Demonstrates the power of Spy: some methods stubbed, others call real implementation
    @Test
    void shouldProcessTransactionWithPartialMocking() {
        // Arrange: Define initial balance and transaction amount
        double initialBalance = 5000.0;
        double transactionAmount = 1000.0;
        
        // Stub calculateFee to return fixed value of $25 (MOCKED BEHAVIOR)
        // Without stubbing, real method would calculate: 1000 * 0.01 = $10
        // This demonstrates selective stubbing with spy
        when(transactionProcessor.calculateFee(transactionAmount)).thenReturn(25.0);
        
        // Act: Call processTransaction which demonstrates PARTIAL MOCKING:
        // 1. Internally calls calculateFee() - uses MOCKED version (returns $25)
        // 2. Executes its own calculation logic - uses REAL implementation
        // This mixing of mocked and real behavior is the essence of spying
        double finalBalance = transactionProcessor.processTransaction(initialBalance, transactionAmount);
        
        // Assert: Verify final balance calculation with mocked fee
        // Expected: 5000 (initial) - 1000 (transaction) - 25 (mocked fee) = 3975
        // If calculateFee wasn't mocked: 5000 - 1000 - 10 = 3990
        // This proves partial mocking: mocked fee but real balance calculation
        assertEquals(3975.0, finalBalance, 0.01, 
            "Final balance should reflect mocked fee of $25");
        
        // Verify calculateFee was called once (used mocked version)
        verify(transactionProcessor, times(1)).calculateFee(transactionAmount);
        
        // Verify processTransaction was called once (used real implementation)
        verify(transactionProcessor, times(1)).processTransaction(initialBalance, transactionAmount);
    }
}
```

**Final Implementation File:** `src/main/java/edu/m013/TransactionProcessor.java`

```java
package edu.m013;

// Real concrete class for processing banking transactions
// This class will be SPIED on in tests, not mocked
// Spy allows calling real methods or stubbing specific methods as needed
public class TransactionProcessor {
    
    // Real method to calculate transaction fee based on amount
    // Business logic: 2% fee for amounts > $1000, otherwise 1%
    // This method can be stubbed when spying, or called with real implementation
    public double calculateFee(double transactionAmount) {
        // Check if transaction amount exceeds threshold
        if (transactionAmount > 1000.0) {
            // Calculate 2% fee for large transactions
            return transactionAmount * 0.02;
        } else {
            // Calculate 1% fee for small transactions
            return transactionAmount * 0.01;
        }
    }
    
    // Real method to validate if account has sufficient balance for transaction
    // Business logic: balance must be >= transaction amount
    // When spied without stubbing, this real method executes
    public boolean validateTransaction(double accountBalance, double transactionAmount) {
        // Check if account balance is sufficient for the transaction
        // Returns true if balance >= amount, false otherwise
        return accountBalance >= transactionAmount;
    }
    
    // Real method to process complete transaction
    // Calculates fee using calculateFee() and deducts both amount and fee from balance
    // Demonstrates method chaining - calls another method internally
    // Perfect for demonstrating partial mocking with spy
    public double processTransaction(double accountBalance, double transactionAmount) {
        // Calculate transaction fee by calling calculateFee method
        // In spy tests, this call can be stubbed or use real implementation
        double fee = calculateFee(transactionAmount);
        
        // Calculate final balance: initial balance minus transaction amount minus fee
        // This calculation logic uses real implementation even when spy is used
        double finalBalance = accountBalance - transactionAmount - fee;
        
        // Return the final balance after transaction processing
        return finalBalance;
    }
}
```

**Expected Result:** All tests pass ✅
