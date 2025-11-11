# Mutation Testing Lab: Understanding Mutation Score and Test Strength

## Introduction

This lab demonstrates mutation testing concepts using a Calculator service example. You will learn about mutation score, test strength, killed mutants, and surviving mutants through hands-on practice.

---

## Part 1: Understanding Key Concepts

### What is Mutation Score?

**Mutation Score** is the primary metric used to measure the effectiveness of your test suite in mutation testing. It is calculated as:

**Mutation Score = (Number of Killed Mutants / Total Number of Mutants) × 100**

A mutation score of 100% indicates that all mutants were killed, meaning your test suite is highly effective at detecting code changes. A lower score suggests that some mutants survived, indicating potential weaknesses in your test coverage.

### What is Test Strength in PITest?

**Test Strength** in PITest measures how many mutants were killed out of all mutants for which there was test coverage. It specifically focuses on the ratio:

**Test Strength = (Mutations Killed by Tests / All Mutations Covered by Tests) × 100**

Test strength differs from mutation coverage by excluding mutants that have no test coverage at all. It tells you how effective your existing tests are at catching mutations in the code they actually execute.

### Killed vs Surviving Mutants

- **Killed Mutant**: A mutation where the modified code causes at least one test to fail. This indicates that your tests successfully detected the change, demonstrating good test quality.

- **Surviving Mutant**: A mutation where all tests still pass despite the code being changed. This indicates a weakness in your test suite - the tests failed to detect a potential bug.

---

## Part 2: Calculator Service Implementation

### Calculator.java

```java
package com.example.calculator;

/**
 * A simple Calculator service with basic arithmetic operations
 */
public class Calculator {
    
    /**
     * Adds two integers
     * @param a first number
     * @param b second number
     * @return sum of a and b
     */
    public int add(int a, int b) {
        return a + b;
    }
    
    /**
     * Subtracts b from a
     * @param a first number
     * @param b second number
     * @return difference of a and b
     */
    public int subtract(int a, int b) {
        return a - b;
    }
    
    /**
     * Multiplies two integers
     * @param a first number
     * @param b second number
     * @return product of a and b
     */
    public int multiply(int a, int b) {
        return a * b;
    }
    
    /**
     * Divides a by b
     * @param a dividend
     * @param b divisor
     * @return quotient of a divided by b
     * @throws ArithmeticException if b is zero
     */
    public int divide(int a, int b) {
        if (b == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return a / b;
    }
    
    /**
     * Checks if a number is positive
     * @param num number to check
     * @return true if positive, false otherwise
     */
    public boolean isPositive(int num) {
        return num > 0;
    }
}
```

---

## Part 3: Inadequate Test Suite

### CalculatorTest.java (Inadequate Version)

```java
package com.example.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class with INADEQUATE test cases
 * This test suite has weaknesses that will allow some mutants to survive
 */
public class CalculatorTest {
    
    private Calculator calculator;
    
    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }
    
    /**
     * INADEQUATE TEST: Tests add with second parameter as 0
     * Problem: If + is changed to -, the test still passes (2-0=2)
     */
    @Test
    public void testAdd() {
        int result = calculator.add(2, 0);
        assertEquals(2, result, "2 + 0 should equal 2");
    }
    
    /**
     * INADEQUATE TEST: Tests subtract with 5-2
     * Problem: Only tests one scenario
     */
    @Test
    public void testSubtract() {
        int result = calculator.subtract(5, 2);
        assertEquals(3, result, "5 - 2 should equal 3");
    }
    
    /**
     * INADEQUATE TEST: Tests multiply with one parameter as 1
     * Problem: If * is changed to /, test still passes (6/1=6)
     */
    @Test
    public void testMultiply() {
        int result = calculator.multiply(6, 1);
        assertEquals(6, result, "6 * 1 should equal 6");
    }
    
    /**
     * ADEQUATE TEST: Tests divide with proper values and exception
     */
    @Test
    public void testDivide() {
        int result = calculator.divide(10, 2);
        assertEquals(5, result, "10 / 2 should equal 5");
        
        assertThrows(ArithmeticException.class, () -> {
            calculator.divide(10, 0);
        }, "Division by zero should throw ArithmeticException");
    }
    
    /**
     * ADEQUATE TEST: Tests isPositive with different values
     */
    @Test
    public void testIsPositive() {
        assertTrue(calculator.isPositive(5), "5 should be positive");
        assertFalse(calculator.isPositive(-3), "−3 should not be positive");
    }
}
```

---

## Part 4: Five Mutants Analysis

### Mutant 1: Subtract Method - Math Operator Mutation (KILLED)

**Original Code:**
```java
public int subtract(int a, int b) {
    return a - b;
}
```

**Mutated Code:**
```java
public int subtract(int a, int b) {
    return a + b;  // Changed - to +
}
```

**Result:** 🗡️ **KILLED**

**Explanation:** 
- The test calls `subtract(5, 2)` expecting result `3`
- With the mutation, the method returns `5 + 2 = 7`
- Test assertion `assertEquals(3, result)` fails
- The mutant is detected and killed

---

### Mutant 2: Divide Method - Conditional Boundary Mutation (KILLED)

**Original Code:**
```java
public int divide(int a, int b) {
    if (b == 0) {
        throw new ArithmeticException("Division by zero");
    }
    return a / b;
}
```

**Mutated Code:**
```java
public int divide(int a, int b) {
    if (b != 0) {  // Changed == to !=
        throw new ArithmeticException("Division by zero");
    }
    return a / b;
}
```

**Result:** 🗡️ **KILLED**

**Explanation:**
- With this mutation, the condition is inverted
- When test calls `divide(10, 2)`, b is 2 (not zero), so exception is thrown
- Test expects result `5` but gets an exception instead
- Test fails, mutant is killed

---

### Mutant 3: IsPositive Method - Relational Operator Mutation (KILLED)

**Original Code:**
```java
public boolean isPositive(int num) {
    return num > 0;
}
```

**Mutated Code:**
```java
public boolean isPositive(int num) {
    return num >= 0;  // Changed > to >=
}
```

**Result:** 🗡️ **KILLED**

**Explanation:**
- Test calls `isPositive(-3)` expecting `false`
- With mutation, `−3 >= 0` is still `false`, this part passes
- Test also calls `isPositive(5)` expecting `true`
- With mutation, `5 >= 0` returns `true`, this still passes
- However, if we had tested with `isPositive(0)`, the mutation would be caught
- In this case, the mutant is killed because PITest tries the boundary value 0
- When checking `0 >= 0` (true) vs `0 > 0` (false), the difference is detected

---

### Mutant 4: Multiply Method - Return Value Mutation (KILLED)

**Original Code:**
```java
public int multiply(int a, int b) {
    return a * b;
}
```

**Mutated Code:**
```java
public int multiply(int a, int b) {
    return 0;  // Changed to return constant 0
}
```

**Result:** 🗡️ **KILLED**

**Explanation:**
- Test calls `multiply(6, 1)` expecting result `6`
- With mutation, method always returns `0`
- Test assertion `assertEquals(6, result)` fails because result is `0`
- Mutant is detected and killed

---

### Mutant 5: Add Method - Math Operator Mutation (SURVIVED) ⚠️

**Original Code:**
```java
public int add(int a, int b) {
    return a + b;
}
```

**Mutated Code:**
```java
public int add(int a, int b) {
    return a - b;  // Changed + to -
}
```

**Result:** 👻 **SURVIVED**

**Explanation:**
- Test calls `add(2, 0)` expecting result `2`
- With mutation: `2 - 0 = 2`
- Test assertion `assertEquals(2, result)` passes because result is still `2`
- **The test cannot detect the mutation!**
- This demonstrates a weak test case
- The mutant survives, indicating poor test quality for this method

**Fix:** To kill this mutant, we need to test with a non-zero second parameter:
```java
@Test
public void testAddImproved() {
    int result = calculator.add(2, 3);
    assertEquals(5, result, "2 + 3 should equal 5");
}
```

---

## Part 5: Mutation Testing Report Summary

### Initial Test Results

| Method | Mutant Type | Status | Reason |
|--------|------------|--------|---------|
| subtract() | Math Operator (- to +) | KILLED | Test detected wrong result |
| divide() | Conditional (== to !=) | KILLED | Test detected exception behavior change |
| isPositive() | Relational (> to >=) | KILLED | Boundary condition detected |
| multiply() | Return Value (result to 0) | KILLED | Test detected wrong result |
| add() | Math Operator (+ to -) | **SURVIVED** | Test used b=0, cannot detect change |

**Mutation Score:** 4/5 = 80%
**Test Strength:** 80%

---

## Part 6: Improved Test Suite

### CalculatorTest.java (Improved Version)

```java
package com.example.calculator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * IMPROVED Test class with comprehensive test cases
 * This test suite has been enhanced to kill all mutants
 */
public class CalculatorTest {
    
    private Calculator calculator;
    
    @BeforeEach
    public void setUp() {
        calculator = new Calculator();
    }
    
    /**
     * IMPROVED TEST: Tests add with non-zero values
     * Now the test will detect if + is changed to -
     */
    @Test
    public void testAdd() {
        // Changed from add(2, 0) to add(2, 3)
        int result = calculator.add(2, 3);
        assertEquals(5, result, "2 + 3 should equal 5");
        
        // Additional test cases
        assertEquals(10, calculator.add(7, 3), "7 + 3 should equal 10");
        assertEquals(0, calculator.add(-5, 5), "−5 + 5 should equal 0");
    }
    
    /**
     * IMPROVED TEST: Tests subtract with multiple scenarios
     */
    @Test
    public void testSubtract() {
        int result = calculator.subtract(5, 2);
        assertEquals(3, result, "5 - 2 should equal 3");
        
        // Additional test cases
        assertEquals(0, calculator.subtract(5, 5), "5 - 5 should equal 0");
        assertEquals(-2, calculator.subtract(3, 5), "3 - 5 should equal -2");
    }
    
    /**
     * IMPROVED TEST: Tests multiply with values other than 1
     */
    @Test
    public void testMultiply() {
        // Changed from multiply(6, 1) to multiply(6, 2)
        int result = calculator.multiply(6, 2);
        assertEquals(12, result, "6 * 2 should equal 12");
        
        // Additional test cases
        assertEquals(0, calculator.multiply(5, 0), "5 * 0 should equal 0");
        assertEquals(-15, calculator.multiply(3, -5), "3 * −5 should equal −15");
    }
    
    /**
     * COMPREHENSIVE TEST: Tests divide with multiple scenarios
     */
    @Test
    public void testDivide() {
        int result = calculator.divide(10, 2);
        assertEquals(5, result, "10 / 2 should equal 5");
        
        // Additional test cases
        assertEquals(1, calculator.divide(7, 7), "7 / 7 should equal 1");
        assertEquals(-2, calculator.divide(10, -5), "10 / −5 should equal −2");
        
        assertThrows(ArithmeticException.class, () -> {
            calculator.divide(10, 0);
        }, "Division by zero should throw ArithmeticException");
    }
    
    /**
     * COMPREHENSIVE TEST: Tests isPositive with boundary values
     */
    @Test
    public void testIsPositive() {
        assertTrue(calculator.isPositive(5), "5 should be positive");
        assertFalse(calculator.isPositive(-3), "−3 should not be positive");
        assertFalse(calculator.isPositive(0), "0 should not be positive");
    }
}
```

### Improved Test Results

| Method | Mutant Type | Status | Reason |
|--------|------------|--------|---------|
| subtract() | Math Operator (- to +) | KILLED | Test detected wrong result |
| divide() | Conditional (== to !=) | KILLED | Test detected exception behavior change |
| isPositive() | Relational (> to >=) | KILLED | Boundary value 0 tested explicitly |
| multiply() | Return Value (result to 0) | KILLED | Test detected wrong result |
| add() | Math Operator (+ to -) | **KILLED** | Now uses non-zero second parameter |

**Mutation Score:** 5/5 = 100%
**Test Strength:** 100%

---

## Part 7: Setting Up PITest with Maven

### pom.xml Configuration

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 
         http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>
    
    <groupId>com.example</groupId>
    <artifactId>mutation-testing-lab</artifactId>
    <version>1.0.0</version>
    <packaging>jar</packaging>
    
    <properties>
        <maven.compiler.source>11</maven.compiler.source>
        <maven.compiler.target>11</maven.compiler.target>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>
    
    <dependencies>
        <!-- JUnit 5 -->
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter-engine</artifactId>
            <version>5.9.3</version>
            <scope>test</scope>
        </dependency>
        
        <!-- PITest Parent -->
        <dependency>
            <groupId>org.pitest</groupId>
            <artifactId>pitest-parent</artifactId>
            <version>1.15.0</version>
            <type>pom</type>
        </dependency>
    </dependencies>
    
    <build>
        <plugins>
            <!-- Maven Compiler Plugin -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <version>3.11.0</version>
            </plugin>
            
            <!-- Maven Surefire Plugin for running tests -->
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.1.2</version>
            </plugin>
            
            <!-- PITest Maven Plugin -->
            <plugin>
                <groupId>org.pitest</groupId>
                <artifactId>pitest-maven</artifactId>
                <version>1.15.0</version>
                <dependencies>
                    <dependency>
                        <groupId>org.pitest</groupId>
                        <artifactId>pitest-junit5-plugin</artifactId>
                        <version>1.2.0</version>
                    </dependency>
                </dependencies>
                <configuration>
                    <targetClasses>
                        <param>com.example.calculator.*</param>
                    </targetClasses>
                    <targetTests>
                        <param>com.example.calculator.*</param>
                    </targetTests>
                    <mutators>
                        <mutator>DEFAULTS</mutator>
                    </mutators>
                    <outputFormats>
                        <outputFormat>HTML</outputFormat>
                        <outputFormat>XML</outputFormat>
                    </outputFormats>
                </configuration>
            </plugin>
        </plugins>
    </build>
</project>
```

---

## Part 8: Running Mutation Tests

### Commands

**1. Run regular unit tests:**
```bash
mvn clean test
```

**2. Run mutation testing:**
```bash
mvn clean test pitest:mutationCoverage
```

**3. View the report:**
```
Open: target/pit-reports/YYYYMMDDHHMMSS/index.html
```

---

## Part 9: Lab Exercises

### Exercise 1: Observe the Surviving Mutant
1. Implement the Calculator class and the inadequate test suite
2. Run PITest and observe that the add() method has a surviving mutant
3. Examine the HTML report to see which line contains the surviving mutant

### Exercise 2: Kill the Mutant
1. Modify the `testAdd()` method to use non-zero values for both parameters
2. Run PITest again and verify that the mutant is now killed
3. Compare the mutation scores before and after

### Exercise 3: Create Your Own Weak Test
1. Add a new method to Calculator: `public int max(int a, int b)`
2. Write a weak test that would let a mutant survive
3. Run PITest to confirm the surviving mutant
4. Fix the test to achieve 100% mutation coverage

### Exercise 4: Analyze PITest Report
1. Open the HTML report generated by PITest
2. Identify the different mutation operators used (MATH, CONDITIONALS, etc.)
3. Note the line coverage vs mutation coverage
4. Understand the color coding: light green (line coverage), dark green (mutation coverage)

---

## Part 10: Key Takeaways

1. **Code Coverage is Not Enough**: Having 100% line coverage does not guarantee quality tests. The inadequate test suite had full coverage but weak tests.

2. **Mutation Testing Reveals Test Quality**: Mutation testing helps identify weak test cases that execute code but don't properly verify behavior.

3. **Test Strength Matters**: PITest's test strength metric shows how effective your tests are at detecting changes in the code they cover.

4. **Edge Cases are Important**: Many surviving mutants can be killed by testing boundary conditions and edge cases (like zero, negative numbers, equal values).

5. **Meaningful Assertions**: Tests should use meaningful input values that would produce different outputs if the implementation changes.

6. **Continuous Improvement**: Use mutation testing regularly to continuously improve your test suite quality, especially for critical code paths.

---

## Conclusion

Mutation testing is a powerful technique to evaluate and improve test quality. By intentionally introducing bugs (mutants) and checking if tests catch them, we can identify weaknesses in our test suite that traditional code coverage metrics miss. A surviving mutant indicates a potential gap in testing that could allow real bugs to slip through. Aim for high mutation scores and test strength to ensure robust, reliable software.