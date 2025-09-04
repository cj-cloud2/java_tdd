# Mockito Assignment 3 - Spy: AML Transaction Risk Analysis

**Difficulty Level:** 3

## Business Requirement

**AML Transaction Risk Monitoring System**

The Anti-Money Laundering (AML) department needs a transaction risk analysis system that evaluates incoming financial transactions and assigns risk scores based on multiple factors. The system must calculate a base risk score using transaction amount and customer risk profile, apply additional penalties for high-risk scenarios, and determine whether a transaction requires manual review by compliance officers.

## Testable Requirements

**TR-1:** The TransactionRiskAnalyzer shall calculate transaction risk scores by:

- Computing a base risk score from transaction amount and customer risk level
- Applying penalty multipliers for suspicious patterns
- Determining if the final risk score exceeds the review threshold (score > 75)


## Understanding Mockito Spy

**What is a Spy?**

A Mockito spy is a wrapper around a real object that allows you to:

- **Keep real behavior**: By default, all methods execute their actual implementation
- **Selective mocking**: You can override specific methods while keeping others real
- **Verification**: You can verify method calls, arguments, and interaction patterns
- **Partial mocking**: Perfect for testing scenarios where you need some real behavior and some controlled behavior

**Key Differences from Mock:**

- **Mock**: All methods return default values unless explicitly stubbed
- **Spy**: All methods execute real code unless explicitly stubbed

**When to use Spy:**

- Testing legacy code where you can't easily inject dependencies
- Verifying method calls on real objects
- Partial mocking scenarios where you need some real behavior

***

## RED-GREEN-REFACTOR Cycle Implementation

### Pass 1: Base Risk Score Calculation (RED → GREEN → REFACTOR)

#### RED Phase - Write the First Test

**Test File: `TransactionRiskAnalyzerTest.java`**

```java
package com.aml.risk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;

// Enable Mockito annotations and extension for JUnit 5
@ExtendWith(MockitoExtension.class)
class TransactionRiskAnalyzerTest {

    // Create a spy instance - this will wrap a real TransactionRiskAnalyzer object
    // Spy allows us to call real methods while still being able to verify interactions
    @Spy
    private TransactionRiskAnalyzer analyzer;

    // Test method to verify base risk score calculation functionality
    @Test
    void shouldCalculateBaseRiskScoreForLowRiskCustomer() {
        // Arrange - Set up test data for a low-risk scenario
        // Transaction amount of $1000 with customer risk level 1 (low risk)
        BigDecimal transactionAmount = new BigDecimal("1000.00");
        int customerRiskLevel = 1;
        
        // Act - Execute the method under test
        // This calls the real method implementation since we're using a spy
        double riskScore = analyzer.calculateBaseRiskScore(transactionAmount, customerRiskLevel);
        
        // Assert - Verify the expected risk score calculation
        // For $1000 transaction with risk level 1, expected score should be 10.0
        // Formula: (amount / 100) * risk level = (1000 / 100) * 1 = 10.0
        assertEquals(10.0, riskScore, 0.01);
        
        // Verify that the calculateBaseRiskScore method was called exactly once
        // This demonstrates spy's ability to verify method interactions on real objects
        verify(analyzer).calculateBaseRiskScore(transactionAmount, customerRiskLevel);
    }
}
```

**Production Class: `TransactionRiskAnalyzer.java`**

```java
package com.aml.risk;

import java.math.BigDecimal;

// Main class responsible for analyzing transaction risk in AML system
public class TransactionRiskAnalyzer {
    
    // Method to calculate base risk score based on transaction amount and customer profile
    // This is the core business logic that will be tested using spy
    public double calculateBaseRiskScore(BigDecimal transactionAmount, int customerRiskLevel) {
           return -1;
    }
}
```

**Run Test:** ❌ **FAILS** - `calculateBaseRiskScore` method doesn't exist yet.

#### GREEN Phase - Make Test Pass

**Updated Production Class: `TransactionRiskAnalyzer.java`**

```java
package com.aml.risk;

import java.math.BigDecimal;

// Main class responsible for analyzing transaction risk in AML system
public class TransactionRiskAnalyzer {
    
    // Method to calculate base risk score based on transaction amount and customer profile
    // This is the core business logic that will be tested using spy
    public double calculateBaseRiskScore(BigDecimal transactionAmount, int customerRiskLevel) {
        // Convert transaction amount to double for calculation
        double amount = transactionAmount.doubleValue();
        
        // Calculate base risk score using formula: (amount / 100) * customer risk level
        // This provides a proportional risk assessment based on transaction size and customer profile
        return (amount / 100.0) * customerRiskLevel;
    }
}
```

**Run Test:** ✅ **PASSES**

#### REFACTOR Phase

No refactoring needed at this stage - code is simple and clean.

***

### Pass 2: Penalty Application with Spy Verification (RED → GREEN → REFACTOR)

#### RED Phase - Write the Second Test

**Updated Test File: `TransactionRiskAnalyzerTest.java`**

```java
package com.aml.risk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Enable Mockito annotations and extension for JUnit 5
@ExtendWith(MockitoExtension.class)
class TransactionRiskAnalyzerTest {

    // Create a spy instance - this will wrap a real TransactionRiskAnalyzer object
    // Spy allows us to call real methods while still being able to verify interactions
    @Spy
    private TransactionRiskAnalyzer analyzer;

    // Test method to verify base risk score calculation functionality
    @Test
    void shouldCalculateBaseRiskScoreForLowRiskCustomer() {
        // Arrange - Set up test data for a low-risk scenario
        // Transaction amount of $1000 with customer risk level 1 (low risk)
        BigDecimal transactionAmount = new BigDecimal("1000.00");
        int customerRiskLevel = 1;
        
        // Act - Execute the method under test
        // This calls the real method implementation since we're using a spy
        double riskScore = analyzer.calculateBaseRiskScore(transactionAmount, customerRiskLevel);
        
        // Assert - Verify the expected risk score calculation
        // For $1000 transaction with risk level 1, expected score should be 10.0
        // Formula: (amount / 100) * risk level = (1000 / 100) * 1 = 10.0
        assertEquals(10.0, riskScore, 0.01);
        
        // Verify that the calculateBaseRiskScore method was called exactly once
        // This demonstrates spy's ability to verify method interactions on real objects
        verify(analyzer).calculateBaseRiskScore(transactionAmount, customerRiskLevel);
    }

    // Test method to verify penalty application and method interaction verification
    @Test
    void shouldApplyPenaltyAndVerifyMethodCalls() {
        // Arrange - Set up test data for penalty scenario
        double baseScore = 30.0;
        boolean isSuspiciousPattern = true;
        
        // Stub the calculatePenaltyMultiplier method using spy
        // This demonstrates selective mocking - we override one method while keeping others real
        when(analyzer.calculatePenaltyMultiplier(isSuspiciousPattern)).thenReturn(2.5);
        
        // Act - Execute the method that applies penalty
        // This will call the real applyRiskPenalty method, which internally calls calculatePenaltyMultiplier
        double finalScore = analyzer.applyRiskPenalty(baseScore, isSuspiciousPattern);
        
        // Assert - Verify the penalty calculation result
        // Expected: baseScore * penaltyMultiplier = 30.0 * 2.5 = 75.0
        assertEquals(75.0, finalScore, 0.01);
        
        // Verify that the calculatePenaltyMultiplier method was called with correct parameter
        // This shows how spy allows us to verify interactions even when we stub methods
        verify(analyzer).calculatePenaltyMultiplier(isSuspiciousPattern);
        
        // Verify that the applyRiskPenalty method was called with correct parameters
        // This demonstrates verification of the main method call
        verify(analyzer).applyRiskPenalty(baseScore, isSuspiciousPattern);
    }
}
```

**Run Test:** ❌ **FAILS** - Methods `applyRiskPenalty` and `calculatePenaltyMultiplier` don't exist.

#### GREEN Phase - Make Test Pass

**Updated Production Class: `TransactionRiskAnalyzer.java`**

```java
package com.aml.risk;

import java.math.BigDecimal;

// Main class responsible for analyzing transaction risk in AML system
public class TransactionRiskAnalyzer {
    
    // Method to calculate base risk score based on transaction amount and customer profile
    // This is the core business logic that will be tested using spy
    public double calculateBaseRiskScore(BigDecimal transactionAmount, int customerRiskLevel) {
        // Convert transaction amount to double for calculation
        double amount = transactionAmount.doubleValue();
        
        // Calculate base risk score using formula: (amount / 100) * customer risk level
        // This provides a proportional risk assessment based on transaction size and customer profile
        return (amount / 100.0) * customerRiskLevel;
    }
    
    // NEW CODE ADDED IN PASS 2:
    // Method to apply risk penalty based on suspicious patterns
    // This method demonstrates real method execution while allowing verification through spy
    public double applyRiskPenalty(double baseScore, boolean isSuspiciousPattern) {
        // Get the penalty multiplier by calling another method
        // This internal method call can be verified when using spy
        double penaltyMultiplier = calculatePenaltyMultiplier(isSuspiciousPattern);
        
        // Apply the penalty to base score and return result
        // This calculation represents the business rule for penalty application
        return baseScore * penaltyMultiplier;
    }
    
    // Method to calculate penalty multiplier based on suspicious pattern detection
    // This method can be stubbed in tests while keeping applyRiskPenalty method real
    public double calculatePenaltyMultiplier(boolean isSuspiciousPattern) {
        // Return higher multiplier for suspicious patterns, lower for normal patterns
        // This represents the business logic for penalty calculation
        return isSuspiciousPattern ? 2.5 : 1.0;
    }
}
```

**Run Test:** ✅ **PASSES**

#### REFACTOR Phase

Code is clean and follows single responsibility principle. No refactoring needed.

***

### Pass 3: Complete Risk Analysis with Mixed Spy Behavior (RED → GREEN → REFACTOR)

#### RED Phase - Write the Third Test

**Updated Test File: `TransactionRiskAnalyzerTest.java`**

```java
package com.aml.risk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

// Enable Mockito annotations and extension for JUnit 5
@ExtendWith(MockitoExtension.class)
class TransactionRiskAnalyzerTest {

    // Create a spy instance - this will wrap a real TransactionRiskAnalyzer object
    // Spy allows us to call real methods while still being able to verify interactions
    @Spy
    private TransactionRiskAnalyzer analyzer;

    // Test method to verify base risk score calculation functionality
    @Test
    void shouldCalculateBaseRiskScoreForLowRiskCustomer() {
        // Arrange - Set up test data for a low-risk scenario
        // Transaction amount of $1000 with customer risk level 1 (low risk)
        BigDecimal transactionAmount = new BigDecimal("1000.00");
        int customerRiskLevel = 1;
        
        // Act - Execute the method under test
        // This calls the real method implementation since we're using a spy
        double riskScore = analyzer.calculateBaseRiskScore(transactionAmount, customerRiskLevel);
        
        // Assert - Verify the expected risk score calculation
        // For $1000 transaction with risk level 1, expected score should be 10.0
        // Formula: (amount / 100) * risk level = (1000 / 100) * 1 = 10.0
        assertEquals(10.0, riskScore, 0.01);
        
        // Verify that the calculateBaseRiskScore method was called exactly once
        // This demonstrates spy's ability to verify method interactions on real objects
        verify(analyzer).calculateBaseRiskScore(transactionAmount, customerRiskLevel);
    }

    // Test method to verify penalty application and method interaction verification
    @Test
    void shouldApplyPenaltyAndVerifyMethodCalls() {
        // Arrange - Set up test data for penalty scenario
        double baseScore = 30.0;
        boolean isSuspiciousPattern = true;
        
        // Stub the calculatePenaltyMultiplier method using spy
        // This demonstrates selective mocking - we override one method while keeping others real
        when(analyzer.calculatePenaltyMultiplier(isSuspiciousPattern)).thenReturn(2.5);
        
        // Act - Execute the method that applies penalty
        // This will call the real applyRiskPenalty method, which internally calls calculatePenaltyMultiplier
        double finalScore = analyzer.applyRiskPenalty(baseScore, isSuspiciousPattern);
        
        // Assert - Verify the penalty calculation result
        // Expected: baseScore * penaltyMultiplier = 30.0 * 2.5 = 75.0
        assertEquals(75.0, finalScore, 0.01);
        
        // Verify that the calculatePenaltyMultiplier method was called with correct parameter
        // This shows how spy allows us to verify interactions even when we stub methods
        verify(analyzer).calculatePenaltyMultiplier(isSuspiciousPattern);
        
        // Verify that the applyRiskPenalty method was called with correct parameters
        // This demonstrates verification of the main method call
        verify(analyzer).applyRiskPenalty(baseScore, isSuspiciousPattern);
    }

    // Test method demonstrating complete risk analysis with mixed spy behavior
    @Test
    void shouldPerformCompleteRiskAnalysisWithMixedSpyBehavior() {
        // Arrange - Set up test data for complete analysis scenario
        BigDecimal transactionAmount = new BigDecimal("5000.00");
        int customerRiskLevel = 3;
        boolean isSuspiciousPattern = true;
        
        // Stub specific methods while keeping others real - this shows spy's selective mocking power
        // We stub calculateBaseRiskScore to return a controlled value for testing
        when(analyzer.calculateBaseRiskScore(any(BigDecimal.class), anyInt())).thenReturn(150.0);
        
        // Keep applyRiskPenalty method real, but stub calculatePenaltyMultiplier
        // This creates a chain where some methods are real and others are mocked
        when(analyzer.calculatePenaltyMultiplier(anyBoolean())).thenReturn(1.8);
        
        // Act - Execute the complete risk analysis method
        // This method orchestrates multiple method calls, some real and some stubbed
        boolean requiresReview = analyzer.analyzeTransactionRisk(transactionAmount, customerRiskLevel, isSuspiciousPattern);
        
        // Assert - Verify the analysis result
        // With base score 150.0 and penalty multiplier 1.8, final score = 270.0
        // Since 270.0 > 75.0 (review threshold), requiresReview should be true
        assertTrue(requiresReview);
        
        // Verify all method interactions occurred as expected
        // This demonstrates comprehensive verification of method call chain with spy
        verify(analyzer).calculateBaseRiskScore(transactionAmount, customerRiskLevel);
        verify(analyzer).applyRiskPenalty(150.0, isSuspiciousPattern);
        verify(analyzer).calculatePenaltyMultiplier(isSuspiciousPattern);
        verify(analyzer).analyzeTransactionRisk(transactionAmount, customerRiskLevel, isSuspiciousPattern);
    }
}
```

**Run Test:** ❌ **FAILS** - Method `analyzeTransactionRisk` doesn't exist.

#### GREEN Phase - Make Test Pass

**Updated Production Class: `TransactionRiskAnalyzer.java`**

```java
package com.aml.risk;

import java.math.BigDecimal;

// Main class responsible for analyzing transaction risk in AML system
public class TransactionRiskAnalyzer {
    
    // Constant defining the risk threshold above which transactions require manual review
    private static final double REVIEW_THRESHOLD = 75.0;
    
    // Method to calculate base risk score based on transaction amount and customer profile
    // This is the core business logic that will be tested using spy
    public double calculateBaseRiskScore(BigDecimal transactionAmount, int customerRiskLevel) {
        // Convert transaction amount to double for calculation
        double amount = transactionAmount.doubleValue();
        
        // Calculate base risk score using formula: (amount / 100) * customer risk level
        // This provides a proportional risk assessment based on transaction size and customer profile
        return (amount / 100.0) * customerRiskLevel;
    }
    
    // Method to apply risk penalty based on suspicious patterns
    // This method demonstrates real method execution while allowing verification through spy
    public double applyRiskPenalty(double baseScore, boolean isSuspiciousPattern) {
        // Get the penalty multiplier by calling another method
        // This internal method call can be verified when using spy
        double penaltyMultiplier = calculatePenaltyMultiplier(isSuspiciousPattern);
        
        // Apply the penalty to base score and return result
        // This calculation represents the business rule for penalty application
        return baseScore * penaltyMultiplier;
    }
    
    // Method to calculate penalty multiplier based on suspicious pattern detection
    // This method can be stubbed in tests while keeping applyRiskPenalty method real
    public double calculatePenaltyMultiplier(boolean isSuspiciousPattern) {
        // Return higher multiplier for suspicious patterns, lower for normal patterns
        // This represents the business logic for penalty calculation
        return isSuspiciousPattern ? 2.5 : 1.0;
    }
    
    // NEW CODE ADDED IN PASS 3:
    // Main method that orchestrates complete transaction risk analysis
    // This method demonstrates how spy can verify complex method interaction chains
    public boolean analyzeTransactionRisk(BigDecimal transactionAmount, int customerRiskLevel, boolean isSuspiciousPattern) {
        // Calculate the base risk score using transaction details
        // This call can be verified or stubbed when using spy
        double baseScore = calculateBaseRiskScore(transactionAmount, customerRiskLevel);
        
        // Apply penalties for suspicious patterns
        // This demonstrates chaining of method calls that can be individually verified
        double finalScore = applyRiskPenalty(baseScore, isSuspiciousPattern);
        
        // Determine if the transaction requires manual review based on risk threshold
        // Business rule: transactions with risk score > 75 require compliance review
        return finalScore > REVIEW_THRESHOLD;
    }
}
```

**Run Test:** ✅ **PASSES**

#### REFACTOR Phase - Improve Code Structure

**Final Refactored Production Class: `TransactionRiskAnalyzer.java`**

```java
package com.aml.risk;

import java.math.BigDecimal;

/**
 * Main class responsible for analyzing transaction risk in AML system.
 * This class demonstrates proper use of Mockito spy for testing scenarios
 * where partial mocking and method verification are required.
 */
public class TransactionRiskAnalyzer {
    
    // Constant defining the risk threshold above which transactions require manual review
    // Extracted as constant for better maintainability and testability
    private static final double REVIEW_THRESHOLD = 75.0;
    
    /**
     * Calculates base risk score based on transaction amount and customer profile.
     * This method implements core business logic for risk assessment.
     * 
     * @param transactionAmount The monetary amount of the transaction
     * @param customerRiskLevel The risk level of the customer (1=low, 2=medium, 3=high)
     * @return The calculated base risk score
     */
    public double calculateBaseRiskScore(BigDecimal transactionAmount, int customerRiskLevel) {
        // Validate input parameters to ensure business rule integrity
        if (transactionAmount == null || transactionAmount.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Transaction amount must be non-negative");
        }
        
        // Convert transaction amount to double for calculation
        double amount = transactionAmount.doubleValue();
        
        // Calculate base risk score using formula: (amount / 100) * customer risk level
        // This provides a proportional risk assessment based on transaction size and customer profile
        return (amount / 100.0) * customerRiskLevel;
    }
    
    /**
     * Applies risk penalty based on suspicious patterns detected in the transaction.
     * This method demonstrates method chaining that can be verified using spy.
     * 
     * @param baseScore The initial risk score before penalty application
     * @param isSuspiciousPattern Whether suspicious patterns were detected
     * @return The risk score after penalty application
     */
    public double applyRiskPenalty(double baseScore, boolean isSuspiciousPattern) {
        // Validate base score input
        if (baseScore < 0) {
            throw new IllegalArgumentException("Base score must be non-negative");
        }
        
        // Get the penalty multiplier by calling another method
        // This internal method call can be verified when using spy
        double penaltyMultiplier = calculatePenaltyMultiplier(isSuspiciousPattern);
        
        // Apply the penalty to base score and return result
        // This calculation represents the business rule for penalty application
        return baseScore * penaltyMultiplier;
    }
    
    /**
     * Calculates penalty multiplier based on suspicious pattern detection.
     * This method can be stubbed in tests while keeping other methods real.
     * 
     * @param isSuspiciousPattern Whether suspicious patterns were detected
     * @return The penalty multiplier to apply to the base score
     */
    public double calculatePenaltyMultiplier(boolean isSuspiciousPattern) {
        // Return higher multiplier for suspicious patterns, lower for normal patterns
        // This represents the business logic for penalty calculation
        return isSuspiciousPattern ? 2.5 : 1.0;
    }
    
    /**
     * Performs complete transaction risk analysis to determine if manual review is required.
     * This method orchestrates the entire risk assessment process and demonstrates
     * how spy can verify complex method interaction chains.
     * 
     * @param transactionAmount The monetary amount of the transaction
     * @param customerRiskLevel The risk level of the customer
     * @param isSuspiciousPattern Whether suspicious patterns were detected
     * @return true if transaction requires manual review, false otherwise
     */
    public boolean analyzeTransactionRisk(BigDecimal transactionAmount, int customerRiskLevel, boolean isSuspiciousPattern) {
        // Calculate the base risk score using transaction details
        // This call can be verified or stubbed when using spy
        double baseScore = calculateBaseRiskScore(transactionAmount, customerRiskLevel);
        
        // Apply penalties for suspicious patterns
        // This demonstrates chaining of method calls that can be individually verified
        double finalScore = applyRiskPenalty(baseScore, isSuspiciousPattern);
        
        // Determine if the transaction requires manual review based on risk threshold
        // Business rule: transactions with risk score > 75 require compliance review
        return finalScore > REVIEW_THRESHOLD;
    }
}
```


***

## Key Learning Points about Mockito Spy

1. **Spy wraps real objects**: Unlike mocks that return default values, spies execute real method implementations by default.
2. **Selective stubbing**: You can override specific methods while keeping others real using `when().thenReturn()`.
3. **Method verification**: All method calls can be verified using `verify()`, even on real implementations.
4. **Partial mocking**: Perfect for testing legacy code or complex objects where you need some real behavior.
5. **Real object interaction**: Spies allow you to test actual business logic while still providing test control and verification.

This assignment demonstrates how Mockito spy enables sophisticated testing scenarios in AML systems where you need to verify method interactions while maintaining real business logic execution.

