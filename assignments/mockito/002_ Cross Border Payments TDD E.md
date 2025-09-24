# Mockito Assignment 02: Cross Border Payments TDD Example

**Difficulty Level:** 2
**Framework:** JUnit 5 + Mockito 5
**Domain:** Cross Border Payments
**Package:** edu.m002

## Business Requirements

### Cross Border Payment Processing System

Our financial institution needs a Cross Border Payment Processing System that handles international money transfers. The system must:

1. **Payment Validation \& Processing**: Validate payment details including currency conversion rates, compliance checks, and process payments through external banking networks
2. **Transaction Monitoring**: Track and log all payment attempts for audit purposes and regulatory compliance
3. **Fraud Prevention**: Implement security measures to prevent fraudulent transactions by blocking suspicious payment patterns

## Testable Requirements

From the above business requirements, we derive the following testable requirements:

### Requirement 1: Payment Processing Validation

**Testable Requirement:** The `CrossBorderPaymentService` must validate payment details and successfully process valid payments by calling the external payment gateway exactly once.

- **Mockito Feature Used:** `verify(T mock)` - Verifies that a method was called on a mock


### Requirement 2: Transaction Audit Logging

**Testable Requirement:** The `CrossBorderPaymentService` must log every payment processing attempt (both successful and failed) exactly twice - once at the start and once at completion.

- **Mockito Feature Used:** `times(int wantedNumberOfInvocations)` - Verifies the exact number of method invocations


### Requirement 3: Fraud Prevention

**Testable Requirement:** The `CrossBorderPaymentService` must never call the payment gateway when a payment is flagged as fraudulent by the fraud detection system.

- **Mockito Feature Used:** `never()` - Verifies that a method was never called

***

## Maven Project Structure

```
src/
├── main/
│   └── java/
│       └── edu/
│           └── m001/
│               ├── CrossBorderPaymentService.java
│               └── PaymentRequest.java
└── test/
    └── java/
        └── edu/
            └── m001/
                └── CrossBorderPaymentServiceTest.java
```


***

# TDD Implementation Guide

## Pass 1: RED-GREEN-REFACTOR (Requirement 1)

### Step 1: RED - Write Failing Test

**File:** `src/test/java/edu/m001/CrossBorderPaymentServiceTest.java`

```java
package edu.m002;

// Import JUnit 5 testing framework
import org.junit.jupiter.api.Test;
// Import JUnit 5 extension support for Mockito
import org.junit.jupiter.api.extension.ExtendWith;
// Import Mockito extension for JUnit 5
import org.mockito.junit.jupiter.MockitoExtension;
// Import Mock annotation for creating mock objects
import org.mockito.Mock;
// Import InjectMocks annotation for dependency injection
import org.mockito.InjectMocks;
// Import static methods for Mockito verification
import static org.mockito.Mockito.*;
// Import JUnit 5 assertions
import static org.junit.jupiter.api.Assertions.*;

// Enable Mockito extension for this test class
@ExtendWith(MockitoExtension.class)
public class CrossBorderPaymentServiceTest {

    // Create a mock object for the payment gateway dependency
    @Mock
    private PaymentGateway paymentGateway;
    
    // Create a mock object for the audit logger dependency
    @Mock
    private AuditLogger auditLogger;
    
    // Create a mock object for the fraud detector dependency
    @Mock
    private FraudDetector fraudDetector;
    
    // Inject the mock dependencies into the service under test
    @InjectMocks
    private CrossBorderPaymentService paymentService;

    // Test case for Requirement 1: Payment Processing Validation
    @Test
    public void shouldCallPaymentGatewayOnceForValidPayment() {
        // Arrange: Create a valid payment request object
        PaymentRequest validRequest = new PaymentRequest("USD", "EUR", 1000.0, "ACC123", "ACC456");
        
        // Arrange: Configure the fraud detector mock to return false (not fraudulent)
        when(fraudDetector.isFraudulent(validRequest)).thenReturn(false);
        
        // Arrange: Configure the payment gateway mock to return success
        when(paymentGateway.processPayment(validRequest)).thenReturn(true);

        // Act: Call the method under test
        boolean result = paymentService.processPayment(validRequest);

        // Assert: Verify the result is successful
        assertTrue(result);
        
        // Assert: Verify that payment gateway was called exactly once (Requirement 1)
        verify(paymentGateway).processPayment(validRequest);
    }
}
```

**Run the test** - It will fail because the classes don't exist yet.

### Step 2: GREEN - Write Minimal Code to Pass

**File:** `src/main/java/edu/m001/PaymentRequest.java`

```java
package edu.m002;

// Data class to represent a cross-border payment request
public class PaymentRequest {
    // Source currency code (e.g., "USD")
    private String fromCurrency;
    // Target currency code (e.g., "EUR") 
    private String toCurrency;
    // Amount to transfer
    private double amount;
    // Source account identifier
    private String fromAccount;
    // Destination account identifier
    private String toAccount;

    // Constructor to initialize all payment request fields
    public PaymentRequest(String fromCurrency, String toCurrency, double amount, String fromAccount, String toAccount) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    // Getter method for source currency
    public String getFromCurrency() {
        return fromCurrency;
    }

    // Getter method for target currency
    public String getToCurrency() {
        return toCurrency;
    }

    // Getter method for transfer amount
    public double getAmount() {
        return amount;
    }

    // Getter method for source account
    public String getFromAccount() {
        return fromAccount;
    }

    // Getter method for destination account
    public String getToAccount() {
        return toAccount;
    }
}
```

**File:** `src/main/java/edu/m001/CrossBorderPaymentService.java`

```java
package edu.m002;

// Service class for processing cross-border payments
public class CrossBorderPaymentService {
    // Dependency for external payment processing
    private PaymentGateway paymentGateway;
    // Dependency for audit logging
    private AuditLogger auditLogger;
    // Dependency for fraud detection
    private FraudDetector fraudDetector;

    // Constructor for dependency injection
    public CrossBorderPaymentService(PaymentGateway paymentGateway, AuditLogger auditLogger, FraudDetector fraudDetector) {
        this.paymentGateway = paymentGateway;
        this.auditLogger = auditLogger;
        this.fraudDetector = fraudDetector;
    }

    // Main method to process cross-border payments
    public boolean processPayment(PaymentRequest request) {
        // Check if the payment is fraudulent
        boolean isFraudulent = fraudDetector.isFraudulent(request);
        
        // If not fraudulent, process through payment gateway
        if (!isFraudulent) {
            // Call payment gateway to process the payment (satisfies Requirement 1)
            return paymentGateway.processPayment(request);
        }
        
        // Return false if payment is fraudulent
        return false;
    }
}

// Interface for external payment gateway
interface PaymentGateway {
    // Method to process payment through external gateway
    boolean processPayment(PaymentRequest request);
}

// Interface for audit logging functionality  
interface AuditLogger {
    // Method to log payment processing events
    void logPaymentAttempt(PaymentRequest request, String status);
}

// Interface for fraud detection functionality
interface FraudDetector {
    // Method to check if a payment request is fraudulent
    boolean isFraudulent(PaymentRequest request);
}
```

**Run the test** - It should now pass (GREEN).

### Step 3: REFACTOR - Improve Code Quality

**File:** `src/main/java/edu/m001/CrossBorderPaymentService.java` (Updated)

```java
package edu.m002;

// Service class for processing cross-border payments
public class CrossBorderPaymentService {
    // Dependency for external payment processing
    private final PaymentGateway paymentGateway;
    // Dependency for audit logging
    private final AuditLogger auditLogger;
    // Dependency for fraud detection
    private final FraudDetector fraudDetector;

    // Constructor for dependency injection with final fields for immutability
    public CrossBorderPaymentService(PaymentGateway paymentGateway, AuditLogger auditLogger, FraudDetector fraudDetector) {
        // Validate that payment gateway is not null
        if (paymentGateway == null) {
            throw new IllegalArgumentException("PaymentGateway cannot be null");
        }
        // Validate that audit logger is not null
        if (auditLogger == null) {
            throw new IllegalArgumentException("AuditLogger cannot be null");
        }
        // Validate that fraud detector is not null
        if (fraudDetector == null) {
            throw new IllegalArgumentException("FraudDetector cannot be null");
        }
        
        this.paymentGateway = paymentGateway;
        this.auditLogger = auditLogger;
        this.fraudDetector = fraudDetector;
    }

    // Main method to process cross-border payments with improved error handling
    public boolean processPayment(PaymentRequest request) {
        // Validate input parameter
        if (request == null) {
            throw new IllegalArgumentException("PaymentRequest cannot be null");
        }
        
        // Check if the payment is fraudulent using fraud detection service
        boolean isFraudulent = fraudDetector.isFraudulent(request);
        
        // Process payment only if it's not fraudulent
        if (!isFraudulent) {
            // Call payment gateway to process the payment (satisfies Requirement 1)
            return paymentGateway.processPayment(request);
        }
        
        // Return false if payment is fraudulent (gateway not called)
        return false;
    }
}

// Interface for external payment gateway operations
interface PaymentGateway {
    // Method to process payment through external banking network
    boolean processPayment(PaymentRequest request);
}

// Interface for audit logging and compliance tracking
interface AuditLogger {
    // Method to log payment processing events for regulatory compliance
    void logPaymentAttempt(PaymentRequest request, String status);
}

// Interface for fraud detection and security measures
interface FraudDetector {
    // Method to analyze payment request for suspicious patterns
    boolean isFraudulent(PaymentRequest request);
}
```

**Run the test** - It should still pass after refactoring.

***

## Pass 2: RED-GREEN-REFACTOR (Requirement 2)

### Step 1: RED - Add Failing Test for Audit Logging

**File:** `src/test/java/edu/m001/CrossBorderPaymentServiceTest.java` (Updated)

```java
package edu.m002;

// Import JUnit 5 testing framework
import org.junit.jupiter.api.Test;
// Import JUnit 5 extension support for Mockito
import org.junit.jupiter.api.extension.ExtendWith;
// Import Mockito extension for JUnit 5
import org.mockito.junit.jupiter.MockitoExtension;
// Import Mock annotation for creating mock objects
import org.mockito.Mock;
// Import InjectMocks annotation for dependency injection
import org.mockito.InjectMocks;
// Import static methods for Mockito verification
import static org.mockito.Mockito.*;
// Import JUnit 5 assertions
import static org.junit.jupiter.api.Assertions.*;

// Enable Mockito extension for this test class
@ExtendWith(MockitoExtension.class)
public class CrossBorderPaymentServiceTest {

    // Create a mock object for the payment gateway dependency
    @Mock
    private PaymentGateway paymentGateway;
    
    // Create a mock object for the audit logger dependency
    @Mock
    private AuditLogger auditLogger;
    
    // Create a mock object for the fraud detector dependency
    @Mock
    private FraudDetector fraudDetector;
    
    // Inject the mock dependencies into the service under test
    @InjectMocks
    private CrossBorderPaymentService paymentService;

    // Test case for Requirement 1: Payment Processing Validation
    @Test
    public void shouldCallPaymentGatewayOnceForValidPayment() {
        // Arrange: Create a valid payment request object
        PaymentRequest validRequest = new PaymentRequest("USD", "EUR", 1000.0, "ACC123", "ACC456");
        
        // Arrange: Configure the fraud detector mock to return false (not fraudulent)
        when(fraudDetector.isFraudulent(validRequest)).thenReturn(false);
        
        // Arrange: Configure the payment gateway mock to return success
        when(paymentGateway.processPayment(validRequest)).thenReturn(true);

        // Act: Call the method under test
        boolean result = paymentService.processPayment(validRequest);

        // Assert: Verify the result is successful
        assertTrue(result);
        
        // Assert: Verify that payment gateway was called exactly once (Requirement 1)
        verify(paymentGateway).processPayment(validRequest);
    }

    // Test case for Requirement 2: Transaction Audit Logging
    @Test
    public void shouldLogPaymentAttemptTwiceForEveryTransaction() {
        // Arrange: Create a valid payment request object
        PaymentRequest validRequest = new PaymentRequest("USD", "EUR", 1000.0, "ACC123", "ACC456");
        
        // Arrange: Configure the fraud detector mock to return false (not fraudulent)
        when(fraudDetector.isFraudulent(validRequest)).thenReturn(false);
        
        // Arrange: Configure the payment gateway mock to return success
        when(paymentGateway.processPayment(validRequest)).thenReturn(true);

        // Act: Call the method under test
        paymentService.processPayment(validRequest);

        // Assert: Verify that audit logger was called exactly twice (Requirement 2)
        // Once for "STARTED" status and once for "COMPLETED" status
        verify(auditLogger, times(2)).logPaymentAttempt(eq(validRequest), anyString());
    }
}
```

**Run the test** - The new test will fail because audit logging is not implemented.

### Step 2: GREEN - Implement Audit Logging

**File:** `src/main/java/edu/m001/CrossBorderPaymentService.java` (Updated)

```java
package edu.m002;

// Service class for processing cross-border payments
public class CrossBorderPaymentService {
    // Dependency for external payment processing
    private final PaymentGateway paymentGateway;
    // Dependency for audit logging
    private final AuditLogger auditLogger;
    // Dependency for fraud detection
    private final FraudDetector fraudDetector;

    // Constructor for dependency injection with final fields for immutability
    public CrossBorderPaymentService(PaymentGateway paymentGateway, AuditLogger auditLogger, FraudDetector fraudDetector) {
        // Validate that payment gateway is not null
        if (paymentGateway == null) {
            throw new IllegalArgumentException("PaymentGateway cannot be null");
        }
        // Validate that audit logger is not null
        if (auditLogger == null) {
            throw new IllegalArgumentException("AuditLogger cannot be null");
        }
        // Validate that fraud detector is not null
        if (fraudDetector == null) {
            throw new IllegalArgumentException("FraudDetector cannot be null");
        }
        
        this.paymentGateway = paymentGateway;
        this.auditLogger = auditLogger;
        this.fraudDetector = fraudDetector;
    }

    // Main method to process cross-border payments with audit logging
    public boolean processPayment(PaymentRequest request) {
        // Validate input parameter
        if (request == null) {
            throw new IllegalArgumentException("PaymentRequest cannot be null");
        }
        
        // Log the start of payment processing (first log call for Requirement 2)
        auditLogger.logPaymentAttempt(request, "STARTED");
        
        // Check if the payment is fraudulent using fraud detection service
        boolean isFraudulent = fraudDetector.isFraudulent(request);
        
        // Variable to store the final processing result
        boolean result = false;
        
        // Process payment only if it's not fraudulent
        if (!isFraudulent) {
            // Call payment gateway to process the payment (satisfies Requirement 1)
            result = paymentGateway.processPayment(request);
        }
        
        // Log the completion of payment processing (second log call for Requirement 2)
        auditLogger.logPaymentAttempt(request, "COMPLETED");
        
        // Return the final processing result
        return result;
    }
}

// Interface for external payment gateway operations
interface PaymentGateway {
    // Method to process payment through external banking network
    boolean processPayment(PaymentRequest request);
}

// Interface for audit logging and compliance tracking
interface AuditLogger {
    // Method to log payment processing events for regulatory compliance
    void logPaymentAttempt(PaymentRequest request, String status);
}

// Interface for fraud detection and security measures
interface FraudDetector {
    // Method to analyze payment request for suspicious patterns
    boolean isFraudulent(PaymentRequest request);
}
```

**File:** `src/main/java/edu/m001/PaymentRequest.java` (No changes needed)

```java
package edu.m002;

// Data class to represent a cross-border payment request
public class PaymentRequest {
    // Source currency code (e.g., "USD")
    private String fromCurrency;
    // Target currency code (e.g., "EUR") 
    private String toCurrency;
    // Amount to transfer
    private double amount;
    // Source account identifier
    private String fromAccount;
    // Destination account identifier
    private String toAccount;

    // Constructor to initialize all payment request fields
    public PaymentRequest(String fromCurrency, String toCurrency, double amount, String fromAccount, String toAccount) {
        this.fromCurrency = fromCurrency;
        this.toCurrency = toCurrency;
        this.amount = amount;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
    }

    // Getter method for source currency
    public String getFromCurrency() {
        return fromCurrency;
    }

    // Getter method for target currency
    public String getToCurrency() {
        return toCurrency;
    }

    // Getter method for transfer amount
    public double getAmount() {
        return amount;
    }

    // Getter method for source account
    public String getFromAccount() {
        return fromAccount;
    }

    // Getter method for destination account
    public String getToAccount() {
        return toAccount;
    }
}
```

**Run the tests** - Both tests should now pass (GREEN).

### Step 3: REFACTOR - Improve Audit Logging

**File:** `src/main/java/edu/m001/CrossBorderPaymentService.java` (Updated)

```java
package edu.m002;

// Service class for processing cross-border payments
public class CrossBorderPaymentService {
    // Constant for audit log status when payment processing starts
    private static final String AUDIT_STATUS_STARTED = "STARTED";
    // Constant for audit log status when payment processing completes
    private static final String AUDIT_STATUS_COMPLETED = "COMPLETED";
    
    // Dependency for external payment processing
    private final PaymentGateway paymentGateway;
    // Dependency for audit logging
    private final AuditLogger auditLogger;
    // Dependency for fraud detection
    private final FraudDetector fraudDetector;

    // Constructor for dependency injection with final fields for immutability
    public CrossBorderPaymentService(PaymentGateway paymentGateway, AuditLogger auditLogger, FraudDetector fraudDetector) {
        // Validate that payment gateway is not null
        if (paymentGateway == null) {
            throw new IllegalArgumentException("PaymentGateway cannot be null");
        }
        // Validate that audit logger is not null
        if (auditLogger == null) {
            throw new IllegalArgumentException("AuditLogger cannot be null");
        }
        // Validate that fraud detector is not null
        if (fraudDetector == null) {
            throw new IllegalArgumentException("FraudDetector cannot be null");
        }
        
        this.paymentGateway = paymentGateway;
        this.auditLogger = auditLogger;
        this.fraudDetector = fraudDetector;
    }

    // Main method to process cross-border payments with comprehensive audit logging
    public boolean processPayment(PaymentRequest request) {
        // Validate input parameter
        if (request == null) {
            throw new IllegalArgumentException("PaymentRequest cannot be null");
        }
        
        // Log the start of payment processing (first log call for Requirement 2)
        auditLogger.logPaymentAttempt(request, AUDIT_STATUS_STARTED);
        
        // Variable to store the final processing result
        boolean result = false;
        
        try {
            // Check if the payment is fraudulent using fraud detection service
            boolean isFraudulent = fraudDetector.isFraudulent(request);
            
            // Process payment only if it's not fraudulent
            if (!isFraudulent) {
                // Call payment gateway to process the payment (satisfies Requirement 1)
                result = paymentGateway.processPayment(request);
            }
            
        } finally {
            // Log the completion of payment processing (second log call for Requirement 2)
            // This ensures logging happens even if an exception occurs
            auditLogger.logPaymentAttempt(request, AUDIT_STATUS_COMPLETED);
        }
        
        // Return the final processing result
        return result;
    }
}

// Interface for external payment gateway operations
interface PaymentGateway {
    // Method to process payment through external banking network
    boolean processPayment(PaymentRequest request);
}

// Interface for audit logging and compliance tracking
interface AuditLogger {
    // Method to log payment processing events for regulatory compliance
    void logPaymentAttempt(PaymentRequest request, String status);
}

// Interface for fraud detection and security measures
interface FraudDetector {
    // Method to analyze payment request for suspicious patterns
    boolean isFraudulent(PaymentRequest request);
}
```

**Run the tests** - Both tests should still pass after refactoring.

***

## Pass 3: RED-GREEN-REFACTOR (Requirement 3)

### Step 1: RED - Add Failing Test for Fraud Prevention

**File:** `src/test/java/edu/m001/CrossBorderPaymentServiceTest.java` (Updated)

```java
package edu.m002;

// Import JUnit 5 testing framework
import org.junit.jupiter.api.Test;
// Import JUnit 5 extension support for Mockito
import org.junit.jupiter.api.extension.ExtendWith;
// Import Mockito extension for JUnit 5
import org.mockito.junit.jupiter.MockitoExtension;
// Import Mock annotation for creating mock objects
import org.mockito.Mock;
// Import InjectMocks annotation for dependency injection
import org.mockito.InjectMocks;
// Import static methods for Mockito verification
import static org.mockito.Mockito.*;
// Import JUnit 5 assertions
import static org.junit.jupiter.api.Assertions.*;

// Enable Mockito extension for this test class
@ExtendWith(MockitoExtension.class)
public class CrossBorderPaymentServiceTest {

    // Create a mock object for the payment gateway dependency
    @Mock
    private PaymentGateway paymentGateway;
    
    // Create a mock object for the audit logger dependency
    @Mock
    private AuditLogger auditLogger;
    
    // Create a mock object for the fraud detector dependency
    @Mock
    private FraudDetector fraudDetector;
    
    // Inject the mock dependencies into the service under test
    @InjectMocks
    private CrossBorderPaymentService paymentService;

    // Test case for Requirement 1: Payment Processing Validation
    @Test
    public void shouldCallPaymentGatewayOnceForValidPayment() {
        // Arrange: Create a valid payment request object
        PaymentRequest validRequest = new PaymentRequest("USD", "EUR", 1000.0, "ACC123", "ACC456");
        
        // Arrange: Configure the fraud detector mock to return false (not fraudulent)
        when(fraudDetector.isFraudulent(validRequest)).thenReturn(false);
        
        // Arrange: Configure the payment gateway mock to return success
        when(paymentGateway.processPayment(validRequest)).thenReturn(true);

        // Act: Call the method under test
        boolean result = paymentService.processPayment(validRequest);

        // Assert: Verify the result is successful
        assertTrue(result);
        
        // Assert: Verify that payment gateway was called exactly once (Requirement 1)
        verify(paymentGateway).processPayment(validRequest);
    }

    // Test case for Requirement 2: Transaction Audit Logging
    @Test
    public void shouldLogPaymentAttemptTwiceForEveryTransaction() {
        // Arrange: Create a valid payment request object
        PaymentRequest validRequest = new PaymentRequest("USD", "EUR", 1000.0, "ACC123", "ACC456");
        
        // Arrange: Configure the fraud detector mock to return false (not fraudulent)
        when(fraudDetector.isFraudulent(validRequest)).thenReturn(false);
        
        // Arrange: Configure the payment gateway mock to return success
        when(paymentGateway.processPayment(validRequest)).thenReturn(true);

        // Act: Call the method under test
        paymentService.processPayment(validRequest);

        // Assert: Verify that audit logger was called exactly twice (Requirement 2)
        // Once for "STARTED" status and once for "COMPLETED" status
        verify(auditLogger, times(2)).logPaymentAttempt(eq(validRequest), anyString());
    }

    // Test case for Requirement 3: Fraud Prevention
    @Test
    public void shouldNeverCallPaymentGatewayForFraudulentPayment() {
        // Arrange: Create a suspicious payment request object
        PaymentRequest fraudulentRequest = new PaymentRequest("USD", "EUR", 50000.0, "SUSP123", "SUSP456");
        
        // Arrange: Configure the fraud detector mock to return true (fraudulent)
        when(fraudDetector.isFraudulent(fraudulentRequest)).thenReturn(true);

        // Act: Call the method under test with fraudulent payment
        boolean result = paymentService.processPayment(fraudulentRequest);

        // Assert: Verify the result is unsuccessful (false)
        assertFalse(result);
        
        // Assert: Verify that payment gateway was never called (Requirement 3)
        verify(paymentGateway, never()).processPayment(fraudulentRequest);
    }
}
```

**Run the tests** - The new test should pass immediately because the fraud prevention logic is already implemented, but let's verify it's working correctly.

### Step 2: GREEN - Verify Fraud Prevention Works

The fraud prevention logic is already implemented in our service, so the test should pass. Let's run all tests to ensure everything works.

**Run all tests** - All three tests should pass (GREEN).

### Step 3: REFACTOR - Enhance Fraud Prevention and Final Polish

**File:** `src/main/java/edu/m001/CrossBorderPaymentService.java` (Final Version)

```java
package edu.m002;

// Service class for processing cross-border payments with comprehensive security and audit features
public class CrossBorderPaymentService {
    // Constant for audit log status when payment processing starts
    private static final String AUDIT_STATUS_STARTED = "STARTED";
    // Constant for audit log status when payment processing completes successfully
    private static final String AUDIT_STATUS_COMPLETED = "COMPLETED";
    // Constant for audit log status when payment is blocked due to fraud
    private static final String AUDIT_STATUS_BLOCKED_FRAUD = "BLOCKED_FRAUD";
    
    // Dependency for external payment processing
    private final PaymentGateway paymentGateway;
    // Dependency for audit logging and compliance
    private final AuditLogger auditLogger;
    // Dependency for fraud detection and prevention
    private final FraudDetector fraudDetector;

    // Constructor for dependency injection with validation and final fields for immutability
    public CrossBorderPaymentService(PaymentGateway paymentGateway, AuditLogger auditLogger, FraudDetector fraudDetector) {
        // Validate that payment gateway dependency is not null
        if (paymentGateway == null) {
            throw new IllegalArgumentException("PaymentGateway cannot be null");
        }
        // Validate that audit logger dependency is not null
        if (auditLogger == null) {
            throw new IllegalArgumentException("AuditLogger cannot be null");
        }
        // Validate that fraud detector dependency is not null
        if (fraudDetector == null) {
            throw new IllegalArgumentException("FraudDetector cannot be null");
        }
        
        // Initialize dependencies for cross-border payment processing
        this.paymentGateway = paymentGateway;
        this.auditLogger = auditLogger;
        this.fraudDetector = fraudDetector;
    }

    // Main method to process cross-border payments with full security, audit, and error handling
    public boolean processPayment(PaymentRequest request) {
        // Validate that the payment request is not null
        if (request == null) {
            throw new IllegalArgumentException("PaymentRequest cannot be null");
        }
        
        // Log the initiation of payment processing (first log call for Requirement 2)
        auditLogger.logPaymentAttempt(request, AUDIT_STATUS_STARTED);
        
        // Variable to store the final payment processing result
        boolean paymentResult = false;
        
        try {
            // Perform fraud detection analysis on the payment request
            boolean isFraudulent = fraudDetector.isFraudulent(request);
            
            // Process payment through gateway only if fraud check passes
            if (!isFraudulent) {
                // Execute payment processing through external gateway (satisfies Requirement 1)
                paymentResult = paymentGateway.processPayment(request);
                
                // Log successful completion of payment processing
                auditLogger.logPaymentAttempt(request, AUDIT_STATUS_COMPLETED);
            } else {
                // Log that payment was blocked due to fraud detection
                auditLogger.logPaymentAttempt(request, AUDIT_STATUS_BLOCKED_FRAUD);
                // Note: Payment gateway is never called for fraudulent payments (satisfies Requirement 3)
            }
            
        } catch (Exception e) {
            // Log any unexpected errors during payment processing
            auditLogger.logPaymentAttempt(request, "ERROR: " + e.getMessage());
            // Re-throw the exception to maintain error propagation
            throw new RuntimeException("Payment processing failed", e);
        }
        
        // Return the final result of the payment processing operation
        return paymentResult;
    }
}

// Interface defining contract for external payment gateway operations
interface PaymentGateway {
    // Method to execute payment processing through external banking networks
    // Returns true if payment is successfully processed, false otherwise
    boolean processPayment(PaymentRequest request);
}

// Interface defining contract for audit logging and regulatory compliance
interface AuditLogger {
    // Method to record payment processing events for audit trails and compliance reporting
    // Parameters: payment request details and current processing status
    void logPaymentAttempt(PaymentRequest request, String status);
}

// Interface defining contract for fraud detection and security measures
interface FraudDetector {
    // Method to analyze payment requests for suspicious patterns and potential fraud
    // Returns true if payment appears fraudulent, false if it passes security checks
    boolean isFraudulent(PaymentRequest request);
}
```

**File:** `src/main/java/edu/m001/PaymentRequest.java` (Final Version with Enhanced Documentation)

```java
package edu.m002;

// Immutable data class representing a cross-border payment request with all necessary details
public class PaymentRequest {
    // ISO currency code for the source currency (e.g., "USD", "EUR", "GBP")
    private final String fromCurrency;
    // ISO currency code for the target currency (e.g., "USD", "EUR", "GBP")
    private final String toCurrency;
    // Monetary amount to be transferred (must be positive)
    private final double amount;
    // Unique identifier for the source bank account
    private final String fromAccount;
    // Unique identifier for the destination bank account
    private final String toAccount;

    // Constructor to create a new cross-border payment request with validation
    public PaymentRequest(String fromCurrency, String toCurrency, double amount, String fromAccount, String toAccount) {
        // Validate that source currency is provided
        if (fromCurrency == null || fromCurrency.trim().isEmpty()) {
            throw new IllegalArgumentException("From currency cannot be null or empty");
        }
        // Validate that target currency is provided
        if (toCurrency == null || toCurrency.trim().isEmpty()) {
            throw new IllegalArgumentException("To currency cannot be null or empty");
        }
        // Validate that transfer amount is positive
        if (amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive");
        }
        // Validate that source account identifier is provided
        if (fromAccount == null || fromAccount.trim().isEmpty()) {
            throw new IllegalArgumentException("From account cannot be null or empty");
        }
        // Validate that destination account identifier is provided
        if (toAccount == null || toAccount.trim().isEmpty()) {
            throw new IllegalArgumentException("To account cannot be null or empty");
        }
        
        // Initialize all payment request fields with validated values
        this.fromCurrency = fromCurrency.trim().toUpperCase();
        this.toCurrency = toCurrency.trim().toUpperCase();
        this.amount = amount;
        this.fromAccount = fromAccount.trim();
        this.toAccount = toAccount.trim();
    }

    // Getter method to retrieve the source currency code
    public String getFromCurrency() {
        return fromCurrency;
    }

    // Getter method to retrieve the target currency code
    public String getToCurrency() {
        return toCurrency;
    }

    // Getter method to retrieve the transfer amount
    public double getAmount() {
        return amount;
    }

    // Getter method to retrieve the source account identifier
    public String getFromAccount() {
        return fromAccount;
    }

    // Getter method to retrieve the destination account identifier
    public String getToAccount() {
        return toAccount;
    }

    // Override toString for better debugging and logging
    @Override
    public String toString() {
        return String.format("PaymentRequest{from='%s', to='%s', amount=%.2f, fromAcc='%s', toAcc='%s'}", 
                           fromCurrency, toCurrency, amount, fromAccount, toAccount);
    }
}
```

**File:** `src/test/java/edu/m001/CrossBorderPaymentServiceTest.java` (Final Version)

```java
package edu.m002;

// Import JUnit 5 testing framework core classes
import org.junit.jupiter.api.Test;
// Import JUnit 5 extension support for integrating with Mockito
import org.junit.jupiter.api.extension.ExtendWith;
// Import Mockito extension for JUnit 5 integration
import org.mockito.junit.jupiter.MockitoExtension;
// Import Mock annotation for creating mock objects
import org.mockito.Mock;
// Import InjectMocks annotation for automatic dependency injection
import org.mockito.InjectMocks;
// Import static methods for Mockito stubbing and verification
import static org.mockito.Mockito.*;
// Import JUnit 5 assertion methods
import static org.junit.jupiter.api.Assertions.*;

// Enable Mockito extension to provide mock injection capabilities for this test class
@ExtendWith(MockitoExtension.class)
public class CrossBorderPaymentServiceTest {

    // Create a mock implementation of PaymentGateway for isolated testing
    @Mock
    private PaymentGateway paymentGateway;
    
    // Create a mock implementation of AuditLogger for tracking method calls
    @Mock
    private AuditLogger auditLogger;
    
    // Create a mock implementation of FraudDetector for controlling fraud detection behavior
    @Mock
    private FraudDetector fraudDetector;
    
    // Automatically inject the mock dependencies into the service under test
    @InjectMocks
    private CrossBorderPaymentService paymentService;

    // Test case for Requirement 1: Payment Processing Validation
    // Verifies that the service calls the payment gateway exactly once for valid payments
    @Test
    public void shouldCallPaymentGatewayOnceForValidPayment() {
        // Arrange: Create a legitimate cross-border payment request
        PaymentRequest validRequest = new PaymentRequest("USD", "EUR", 1000.0, "ACC123", "ACC456");
        
        // Arrange: Configure fraud detector to approve the payment (not fraudulent)
        when(fraudDetector.isFraudulent(validRequest)).thenReturn(false);
        
        // Arrange: Configure payment gateway to simulate successful processing
        when(paymentGateway.processPayment(validRequest)).thenReturn(true);

        // Act: Execute the payment processing method with valid request
        boolean result = paymentService.processPayment(validRequest);

        // Assert: Verify that the payment processing returned success
        assertTrue(result, "Payment processing should return true for valid payments");
        
        // Assert: Verify that payment gateway was invoked exactly once (Requirement 1)
        verify(paymentGateway).processPayment(validRequest);
    }

    // Test case for Requirement 2: Transaction Audit Logging
    // Verifies that every payment attempt is logged exactly twice for compliance
    @Test
    public void shouldLogPaymentAttemptTwiceForEveryTransaction() {
        // Arrange: Create a standard cross-border payment request
        PaymentRequest validRequest = new PaymentRequest("USD", "EUR", 1000.0, "ACC123", "ACC456");
        
        // Arrange: Configure fraud detector to allow the payment to proceed
        when(fraudDetector.isFraudulent(validRequest)).thenReturn(false);
        
        // Arrange: Configure payment gateway to return successful processing result
        when(paymentGateway.processPayment(validRequest)).thenReturn(true);

        // Act: Execute the payment processing method
        paymentService.processPayment(validRequest);

        // Assert: Verify that audit logging occurred exactly twice (Requirement 2)
        // First log: when payment processing starts
        // Second log: when payment processing completes
        verify(auditLogger, times(2)).logPaymentAttempt(eq(validRequest), anyString());
    }

    // Test case for Requirement 3: Fraud Prevention
    // Verifies that payment gateway is never called when fraud is detected
    @Test
    public void shouldNeverCallPaymentGatewayForFraudulentPayment() {
        // Arrange: Create a payment request that will be flagged as suspicious
        PaymentRequest fraudulentRequest = new PaymentRequest("USD", "EUR", 50000.0, "SUSP123", "SUSP456");
        
        // Arrange: Configure fraud detector to flag this payment as fraudulent
        when(fraudDetector.isFraudulent(fraudulentRequest)).thenReturn(true);

        // Act: Attempt to process the fraudulent payment request
        boolean result = paymentService.processPayment(fraudulentRequest);

        // Assert: Verify that fraudulent payment processing returns failure
        assertFalse(result, "Fraudulent payments should return false");
        
        // Assert: Verify that payment gateway was never invoked (Requirement 3)
        // This ensures no actual money transfer occurs for fraudulent requests
        verify(paymentGateway, never()).processPayment(fraudulentRequest);
    }

    // Additional test to verify audit logging occurs even for blocked fraudulent payments
    @Test
    public void shouldLogFraudulentPaymentAttempts() {
        // Arrange: Create a payment request that will trigger fraud detection
        PaymentRequest fraudulentRequest = new PaymentRequest("USD", "EUR", 50000.0, "SUSP123", "SUSP456");
        
        // Arrange: Configure fraud detector to identify this as fraudulent
        when(fraudDetector.isFraudulent(fraudulentRequest)).thenReturn(true);

        // Act: Process the fraudulent payment request
        paymentService.processPayment(fraudulentRequest);

        // Assert: Verify that audit logging still occurs for fraudulent payments
        // This ensures regulatory compliance even for blocked transactions
        verify(auditLogger, times(2)).logPaymentAttempt(eq(fraudulentRequest), anyString());
    }
}
```

**Run all tests** - All four tests should pass, completing the TDD assignment.

***

## Assignment Completion Summary

### Requirements Fulfilled:

1. ✅ **Requirement 1**: Payment gateway called exactly once for valid payments - `verify(T mock)`
2. ✅ **Requirement 2**: Audit logging occurs exactly twice per transaction - `times(int wantedNumberOfInvocations)`
3. ✅ **Requirement 3**: Payment gateway never called for fraudulent payments - `never()`

### TDD Cycles Completed:

- **Pass 1**: RED → GREEN → REFACTOR (Payment Processing)
- **Pass 2**: RED → GREEN → REFACTOR (Audit Logging)
- **Pass 3**: RED → GREEN → REFACTOR (Fraud Prevention)


### Mockito Features Demonstrated:

- Method stubbing with `when().thenReturn()`
- Mock verification with `verify()`, `times()`, and `never()`
- Mock object creation with `@Mock` and `@InjectMocks`
- Argument matching with `eq()` and `anyString()`


