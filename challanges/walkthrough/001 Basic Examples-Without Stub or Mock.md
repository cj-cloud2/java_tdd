TDD Examples:
Here are five progressively challenging TDD (Test-Driven Development) examples and tasks from finance, banking, and IoT domains. These focus on typical business logic scenarios and avoid the use of stubs or mocks, ensuring tests directly interact with implemented functionality.These examples cover different domains and introduce a wide range of business logic, data management, and validation concepts essential for TDD mastery.


**How to teach with these:**

- Write each test method (red), see the failure.
- Add/update the implementation for green.
- Refactor if required for readability, reuse, or performance after all tests are green.
- No mocks or stubs used; logic is fully verifiable.




### 1. Finance – Currency Converter (Easy)

**Task:**
Write a function that converts an amount from one currency to another, given a static exchange rate.

- **Test 1:** Converting \$100 to Euros at a rate of 0.9 should return €90.
- **Test 2:** Converting \$0 should return €0.
- **Test 3:** Converting a negative amount should throw an error.

*Goal:* Focus on correct calculation and handling invalid input.

### 2. Banking – Simple Interest Calculator (Moderate)

**Task:**
Implement a function to calculate simple interest.

- **Test 1:** Principal \$1,000, rate 5%, time 3 years → Interest = \$150.
- **Test 2:** Principal \$0 should return \$0 interest.
- **Test 3:** Negative interest rate should throw an error.

*Goal:* Reinforces understanding of input validation and arithmetic logic.

### 3. IoT – Temperature Threshold Alert (Moderate)

**Task:**
Implement a class that stores temperature readings and raises an alert if a threshold is crossed.

- **Test 1:** Adding a reading above the threshold triggers the alert status to true.
- **Test 2:** Adding readings below the threshold does not trigger the alert.
- **Test 3:** Resetting the alert restores alert status to false.

*Goal:* Practice state management in classes and basic event triggering.

### 4. Finance – Monthly Budget Tracker (Hard)

**Task:**
Create a budget tracker that tracks expenses and determines if the spending exceeds a monthly limit.

- **Test 1:** Adding expenses keeps total within the limit – should return isOverBudget as false.
- **Test 2:** Total expenses exactly at the limit – should return isOverBudget as false.
- **Test 3:** Adding an expense that pushes the total over the limit – should return isOverBudget as true.
- **Test 4:** Removing an expense brings the total back within the limit – should return isOverBudget as false.

*Goal:* Manage collections/lists and implement business rule checks.

### 5. Banking – Transaction History Filter (Challenging)

**Task:**
Create a class for maintaining a list of transactions with methods to filter transactions by date range and type (withdrawal/deposit).

- **Test 1:** Adding several transactions, filter by date range returns only those within the range.
- **Test 2:** Filter by type (deposit) returns only deposit transactions.
- **Test 3:** Edge case: No transactions in date range returns an empty list.
- **Test 4:** Filtering with invalid dates throws an error.

*Goal:* Emphasize data filtering, list operations, and error handling.

Use these as coding exercises in your TDD sessions:

- Start each task by writing its simplest (failing) test.
- Write minimal code to pass the test.
- Refactor while continually testing.
- No mocking or stubbing—interact with real code and structures.

This approach reinforces the core TDD cycle and essential business logic skills common in finance, banking, and IoT domains.

---

# Give me solutions with explanatory comments. Also comments should explain the sequence in which test should be written as red, the service class should be modified as green and refactor cycle. Use Java and Junit5 for examples

Below are complete Java solutions for the 5 TDD examples, using JUnit 5. Each example includes step-by-step comments explaining:

- **Red:** The initial failing test.
- **Green:** The minimal service class code to make the test pass.
- **Refactor:** Suggestions for cleanup and improvements.


## 1. Finance – Currency Converter

**CurrencyConverter.java**

```java
public class CurrencyConverter {
    public double convert(double amount, double rate) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        return amount * rate;
    }
}
```

**CurrencyConverterTest.java**

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CurrencyConverterTest {
    // RED: Start with a failing test for normal conversion.
    @Test
    void testConvertAmount() {
        CurrencyConverter converter = new CurrencyConverter();
        assertEquals(90.0, converter.convert(100.0, 0.9));
    }

    // RED: Test zero amount conversion.
    @Test
    void testConvertZeroAmount() {
        CurrencyConverter converter = new CurrencyConverter();
        assertEquals(0.0, converter.convert(0, 0.9));
    }

    // RED: Negative amount should throw an error.
    @Test
    void testNegativeAmount() {
        CurrencyConverter converter = new CurrencyConverter();
        assertThrows(IllegalArgumentException.class, () -> converter.convert(-5, 0.9));
    }
}
// GREEN: After each test, write minimal code to pass.
// REFACTOR: No complex logic, method is clear.
```


## 2. Banking – Simple Interest Calculator

**SimpleInterestCalculator.java**

```java
public class SimpleInterestCalculator {
    public double compute(double principal, double rate, int timeYears) {
        if (rate < 0) throw new IllegalArgumentException("Negative rate");
        return principal * rate * timeYears / 100;
    }
}
```

**SimpleInterestCalculatorTest.java**

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SimpleInterestCalculatorTest {
    // RED: Normal calculation.
    @Test
    void testCalcSimpleInterest() {
        SimpleInterestCalculator calc = new SimpleInterestCalculator();
        assertEquals(150.0, calc.compute(1000, 5, 3));
    }
    // RED: $0 principal returns $0 interest.
    @Test
    void testZeroPrincipal() {
        SimpleInterestCalculator calc = new SimpleInterestCalculator();
        assertEquals(0.0, calc.compute(0, 5, 3));
    }
    // RED: Negative rate throws error.
    @Test
    void testNegativeRate() {
        SimpleInterestCalculator calc = new SimpleInterestCalculator();
        assertThrows(IllegalArgumentException.class, () -> calc.compute(1000, -1, 3));
    }
}
// GREEN: Write minimal implementation after each test.
// REFACTOR: Here, the formula is clear. No extra logic needed.
```


## 3. IoT – Temperature Threshold Alert

**TemperatureMonitor.java**

```java
public class TemperatureMonitor {
    private double threshold;
    private boolean alert = false;

    public TemperatureMonitor(double threshold) {
        this.threshold = threshold;
    }

    public void addReading(double temp) {
        if (temp > threshold) alert = true;
    }
    public boolean isAlert() {
        return alert;
    }
    public void resetAlert() {
        alert = false;
    }
}
```

**TemperatureMonitorTest.java**

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TemperatureMonitorTest {
    // RED: Above threshold triggers alert.
    @Test
    void testAlertTriggered() {
        TemperatureMonitor monitor = new TemperatureMonitor(30);
        monitor.addReading(32);
        assertTrue(monitor.isAlert());
    }
    // RED: Below threshold does not trigger alert.
    @Test
    void testNoAlertBelowThreshold() {
        TemperatureMonitor monitor = new TemperatureMonitor(30);
        monitor.addReading(28);
        assertFalse(monitor.isAlert());
    }
    // RED: Reset alert restores status.
    @Test
    void testResetAlert() {
        TemperatureMonitor monitor = new TemperatureMonitor(30);
        monitor.addReading(32);
        monitor.resetAlert();
        assertFalse(monitor.isAlert());
    }
}
// GREEN: Code follows each failing test.
// REFACTOR: Code is clear and minimal. Could later add support for multiple readings.
```


## 4. Finance – Monthly Budget Tracker

**BudgetTracker.java**

```java
import java.util.ArrayList;
import java.util.List;

public class BudgetTracker {
    private double limit;
    private List<Double> expenses = new ArrayList<>();

    public BudgetTracker(double limit) {
        this.limit = limit;
    }
    public void addExpense(double e) {
        expenses.add(e);
    }
    public void removeExpense(double e) {
        expenses.remove(e);
    }
    public boolean isOverBudget() {
        return expenses.stream().mapToDouble(Double::doubleValue).sum() > limit;
    }
}
```

**BudgetTrackerTest.java**

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BudgetTrackerTest {
    // RED: Expenses within limit.
    @Test
    void testWithinBudget() {
        BudgetTracker tracker = new BudgetTracker(100);
        tracker.addExpense(20);
        tracker.addExpense(40);
        assertFalse(tracker.isOverBudget());
    }
    // RED: Exactly at the limit.
    @Test
    void testAtLimit() {
        BudgetTracker tracker = new BudgetTracker(100);
        tracker.addExpense(100);
        assertFalse(tracker.isOverBudget());
    }
    // RED: Exceeds budget.
    @Test
    void testOverBudget() {
        BudgetTracker tracker = new BudgetTracker(100);
        tracker.addExpense(70);
        tracker.addExpense(40);
        assertTrue(tracker.isOverBudget());
    }
    // RED: Remove expense brings back within budget.
    @Test
    void testRemoveExpense() {
        BudgetTracker tracker = new BudgetTracker(100);
        tracker.addExpense(70);
        tracker.addExpense(40);
        tracker.removeExpense(40);
        assertFalse(tracker.isOverBudget());
    }
}
// GREEN: After each test, fix BudgetTracker to pass test.
// REFACTOR: Optimize sum calculation if performance needed.
```


## 5. Banking – Transaction History Filter

**Transaction.java**

```java
import java.time.LocalDate;

public class Transaction {
    public enum Type { DEPOSIT, WITHDRAWAL }
    private double amount;
    private LocalDate date;
    private Type type;
    public Transaction(double amount, LocalDate date, Type type) {
        this.amount = amount;
        this.date = date;
        this.type = type;
    }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public Type getType() { return type; }
}
```

**TransactionHistory.java**

```java
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionHistory {
    private List<Transaction> transactions = new ArrayList<>();

    public void add(Transaction t) { transactions.add(t); }

    public List<Transaction> filterByDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) throw new IllegalArgumentException("Invalid date range");
        return transactions.stream()
                .filter(t -> !t.getDate().isBefore(from) && !t.getDate().isAfter(to))
                .collect(Collectors.toList());
    }

    public List<Transaction> filterByType(Transaction.Type type) {
        return transactions.stream()
                .filter(t -> t.getType() == type)
                .collect(Collectors.toList());
    }
}
```

**TransactionHistoryTest.java**

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

class TransactionHistoryTest {
    // RED: Filter by date range.
    @Test
    void testFilterByDateRange() {
        TransactionHistory history = new TransactionHistory();
        history.add(new Transaction(100, LocalDate.of(2025,8,1), Transaction.Type.DEPOSIT));
        history.add(new Transaction(50, LocalDate.of(2025,8,3), Transaction.Type.WITHDRAWAL));
        history.add(new Transaction(30, LocalDate.of(2025,7,29), Transaction.Type.DEPOSIT));
        List<Transaction> result = history.filterByDateRange(LocalDate.of(2025,8,1), LocalDate.of(2025,8,4));
        assertEquals(2, result.size());
    }
    // RED: Filter by type.
    @Test
    void testFilterByType() {
        TransactionHistory history = new TransactionHistory();
        history.add(new Transaction(100, LocalDate.of(2025,8,1), Transaction.Type.DEPOSIT));
        history.add(new Transaction(50, LocalDate.of(2025,8,3), Transaction.Type.WITHDRAWAL));
        List<Transaction> deposits = history.filterByType(Transaction.Type.DEPOSIT);
        assertEquals(1, deposits.size());
    }
    // RED: No transactions in range returns empty list.
    @Test
    void testNoTransactionsInRange() {
        TransactionHistory history = new TransactionHistory();
        history.add(new Transaction(100, LocalDate.of(2025,8,1), Transaction.Type.DEPOSIT));
        List<Transaction> result = history.filterByDateRange(LocalDate.of(2025,8,2), LocalDate.of(2025,8,3));
        assertTrue(result.isEmpty());
    }
    // RED: Invalid range throws error.
    @Test
    void testInvalidDateRangeThrows() {
        TransactionHistory history = new TransactionHistory();
        assertThrows(IllegalArgumentException.class,
            () -> history.filterByDateRange(LocalDate.of(2025,8,4), LocalDate.of(2025,8,1)));
    }
}
// GREEN: Implement TransactionHistory methods.
// REFACTOR: Use streams and helper methods for cleanliness.
```


