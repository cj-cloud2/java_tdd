
# Spy Examples

Here are examples of SPY usage in Java with JUnit5, inspired by tasks from finance, banking, and IoT domains. Each task reflects real-world scenarios where you might want to verify interactions—classic use-cases for the *spy* test double pattern. The examples below explicitly show where and how a SPY is used, by wrapping a real instance to monitor/verify interactions, but not stub behavior.


### **How and Where is SPY Used?**

- In all cases, *spy* is created using Mockito: `Mockito.spy(SomeInterface.class);`
- Service class receives the interface (not actual logic) for collaboration.
- Test methods **verify** the *real* calls happened (using `Mockito.verify(...)`), rather than faking/stubbing behavior.
- This monitors true method calls/interactions, ***not*** stubbing.
- **Purpose:** Verifies that correct business interactions (e.g., logging, notifications) occurred as side effects of operations, central to integrating with observers, loggers, notifiers, or trackers.

Each test is self-contained, includes dummy interfaces as required, and can be run with JUnit5 plus Mockito as test double library for SPY implementation.







## Example 1. Finance: Monthly Budget Tracker

### Task and Requirements

- Implement a budget tracker class to monitor expenses against a monthly limit.
- Users can:

1. Add expenses,
2. Remove expenses,
3. Check if they're over budget.
- Requirements:
    - If total exceeds the limit, `isOverBudget` returns true.
    - Removing enough expense returns the total under the limit (`isOverBudget` returns false).
    - Whenever expenses are added, method `notifyLimitApproaching` is called if total is above 80% of limit.


#### Test Class Using SPY

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;



class BudgetTrackerTest {
    // Requirement 1: Adding expenses below the limit should not trigger over-budget.
    @Test
    void testExpenseWithinLimit() {
        LimitNotifier notifier = Mockito.spy(LimitNotifier.class);
        BudgetTracker tracker = new BudgetTracker(100.0, notifier);

        tracker.addExpense(60.0);
        assertFalse(tracker.isOverBudget());
        // SPY usage: no notification yet.
        Mockito.verify(notifier, Mockito.never()).notifyLimitApproaching();
    }

    // Requirement 2: Adding expense above 80% limit triggers notification.
    @Test
    void testNotifyLimitApproaching() {
        LimitNotifier notifier = Mockito.spy(LimitNotifier.class);
        BudgetTracker tracker = new BudgetTracker(100.0, notifier);

        tracker.addExpense(85.0);
        Mockito.verify(notifier).notifyLimitApproaching(); // SPY: check interaction.
    }

    // Requirement 3: Adding expense exceeding limit triggers over budget.
    @Test
    void testExpenseOverLimit() {
        LimitNotifier notifier = Mockito.spy(LimitNotifier.class);
        BudgetTracker tracker = new BudgetTracker(100.0, notifier);

        tracker.addExpense(120.0);
        assertTrue(tracker.isOverBudget());
        Mockito.verify(notifier).notifyLimitApproaching();
    }

    // SPY Explanation:
    // The notifier spy remembers each call to notifyLimitApproaching, allowing us to verify interaction,
    // rather than stubbing the behavior.
}
```


#### Service and Helper Classes

```java
import java.util.ArrayList;
import java.util.List;

public class BudgetTracker {
    private double limit;
    private List<Double> expenses = new ArrayList<>();
    private final LimitNotifier notifier;

    public BudgetTracker(double limit, LimitNotifier notifier) {
        this.limit = limit;
        this.notifier = notifier;
    }

    public void addExpense(double e) {
        expenses.add(e);
        if(getTotal() > 0.8 * limit) {
            notifier.notifyLimitApproaching();
        }
    }
    public void removeExpense(double e) { expenses.remove(e); }
    public boolean isOverBudget() {
        return getTotal() > limit;
    }
    private double getTotal() {
        return expenses.stream().mapToDouble(Double::doubleValue).sum();
    }
}

// Dummy interface for notification, to be spied.
interface LimitNotifier {
    void notifyLimitApproaching();
}
```


---

## Example 2. IoT: Device Temperature Monitor with Alert

### Task and Requirements

- A temperature monitoring class triggers an alert via notifier if a threshold is crossed.
- Requirements:

1. Add a reading above threshold triggers alert and notifies admin only once,
2. Below the threshold does nothing,
3. Reset restores state and permits a new notification later.


#### Test Class Using SPY

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;



class TemperatureMonitorTest {
    @Test
    void testAboveThresholdTriggersAlertAndNotify() {
        AlertNotifier notifier = Mockito.spy(AlertNotifier.class);
        TemperatureMonitor monitor = new TemperatureMonitor(30.0, notifier);
        monitor.addReading(32.0);
        assertTrue(monitor.isAlert());
        Mockito.verify(notifier).sendAlert(); // SPY: verify called once
    }

    @Test
    void testBelowThresholdNoAlert() {
        AlertNotifier notifier = Mockito.spy(AlertNotifier.class);
        TemperatureMonitor monitor = new TemperatureMonitor(30.0, notifier);
        monitor.addReading(28.0);
        assertFalse(monitor.isAlert());
        Mockito.verify(notifier, Mockito.never()).sendAlert();
    }

    @Test
    void testResetAllowsResend() {
        AlertNotifier notifier = Mockito.spy(AlertNotifier.class);
        TemperatureMonitor monitor = new TemperatureMonitor(30.0, notifier);
        monitor.addReading(32.0);
        monitor.resetAlert();
        monitor.addReading(35.0);
        Mockito.verify(notifier, Mockito.times(2)).sendAlert(); // SPY: called twice
    }
}
```


#### Service Class

```java
public class TemperatureMonitor {
    private double threshold;
    private boolean alert = false;
    private final AlertNotifier notifier;

    public TemperatureMonitor(double threshold, AlertNotifier notifier) {
        this.threshold = threshold;
        this.notifier = notifier;
    }

    public void addReading(double temp) {
        if (temp > threshold && !alert) {
            alert = true;
            notifier.sendAlert();
        }
    }
    public boolean isAlert() { return alert; }
    public void resetAlert() { alert = false; }
}

interface AlertNotifier {
    void sendAlert();
}
```


---

## Example 3. Banking: Simple Interest Calculator with Auditing

### Task and Requirements

- Calculate simple interest; log every calculation for audit,
- Requirements:

1. Normal calculation,
2. Zero principal,
3. Negative rate throws error,
4. Each calculation triggers an audit log call with all arguments.


#### Test Class Using SPY

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;



class SimpleInterestCalculatorTest {
    @Test
    void testNormalCalculationAndAudit() {
        AuditLogger logger = Mockito.spy(AuditLogger.class);
        SimpleInterestCalculator calc = new SimpleInterestCalculator(logger);
        double interest = calc.compute(1000, 5, 3);
        assertEquals(150.0, interest);
        Mockito.verify(logger).logCalculation(1000, 5, 3); // SPY verify
    }

    @Test
    void testZeroPrincipalAndAudit() {
        AuditLogger logger = Mockito.spy(AuditLogger.class);
        SimpleInterestCalculator calc = new SimpleInterestCalculator(logger);
        double interest = calc.compute(0, 5, 2);
        assertEquals(0.0, interest);
        Mockito.verify(logger).logCalculation(0, 5, 2);
    }

    @Test
    void testNegativeRateThrowsAndNoAudit() {
        AuditLogger logger = Mockito.spy(AuditLogger.class);
        SimpleInterestCalculator calc = new SimpleInterestCalculator(logger);
        assertThrows(IllegalArgumentException.class, () -> calc.compute(1000, -1, 3));
        Mockito.verify(logger, Mockito.never()).logCalculation(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt());
    }
}
```


#### Service Class

```java
public class SimpleInterestCalculator {
    private final AuditLogger logger;

    public SimpleInterestCalculator(AuditLogger logger) { this.logger = logger; }
    public double compute(double principal, double rate, int timeYears) {
        if (rate < 0) throw new IllegalArgumentException("Negative rate");
        double result = principal * rate * timeYears / 100.0;
        logger.logCalculation(principal, rate, timeYears);
        return result;
    }
}

interface AuditLogger {
    void logCalculation(double principal, double rate, int years);
}
```


---

## Example 4. Banking: Transaction History Filter and Access Log

### Task and Requirements

- Filter a list of transactions by date/type; every filter action must log the number of results.
- Requirements:

1. Filtering by date returns correct list and logs count,
2. Filtering by type returns correct list and logs count,
3. Invalid range throws error, no log.


#### Test Class Using SPY

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.time.LocalDate;
import java.util.List;



class TransactionHistoryTest {
    @Test
    void testFilterByDateLogsAccess() {
        // Dummy logger for SPY
        AccessLogger logger = Mockito.spy(AccessLogger.class);
        TransactionHistory history = new TransactionHistory(logger);
        history.addTransaction(new Transaction(100, LocalDate.of(2025,8,1), Transaction.Type.DEPOSIT));
        history.addTransaction(new Transaction(50, LocalDate.of(2025,8,3), Transaction.Type.WITHDRAWAL));
        List<Transaction> result = history.filterByDateRange(LocalDate.of(2025,8,1), LocalDate.of(2025,8,4));
        assertEquals(2, result.size());
        // SPY: verify correct logging
        Mockito.verify(logger).logAccess(2);
    }

    @Test
    void testInvalidRangeThrowsAndNoLog() {
        AccessLogger logger = Mockito.spy(AccessLogger.class);
        TransactionHistory history = new TransactionHistory(logger);
        assertThrows(IllegalArgumentException.class, () ->
                history.filterByDateRange(LocalDate.of(2025,8,4), LocalDate.of(2025,8,1)));
        Mockito.verify(logger, Mockito.never()).logAccess(Mockito.anyInt());
    }
}
```


#### Service and Helper Classes

```java
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionHistory {
    private List<Transaction> transactions = new ArrayList<>();
    private final AccessLogger logger;

    public TransactionHistory(AccessLogger logger) { this.logger = logger; }
    public void addTransaction(Transaction t) { transactions.add(t); }
    public List<Transaction> filterByDateRange(LocalDate from, LocalDate to) {
        if(from.isAfter(to)) throw new IllegalArgumentException("Invalid date range");
        List<Transaction> result = transactions.stream()
            .filter(t -> !t.getDate().isBefore(from) && !t.getDate().isAfter(to))
            .collect(Collectors.toList());
        logger.logAccess(result.size());
        return result;
    }
}

class Transaction {
    enum Type { DEPOSIT, WITHDRAWAL }
    private double amount;
    private LocalDate date;
    private Type type;
    public Transaction(double amount, LocalDate date, Type type) {
        this.amount = amount; this.date = date; this.type = type;;
    }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public Type getType() { return type; }
}


interface AccessLogger {
    void logAccess(int resultCount);
}
```


---

## Example 5. Finance: Currency Converter with API Usage Tracker

### Task and Requirements

- Currency conversion function; every call reports API usage.
- Requirements:

1. Normal conversion,
2. Negative amount errors,
3. API usage is tracked after every valid attempt (calls incrementUsage).


#### Test Class Using SPY

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class CurrencyConverterTest {
    @Test
    void testNormalConversionTracksUsage() {
        UsageTracker tracker = Mockito.spy(UsageTracker.class);
        CurrencyConverter converter = new CurrencyConverter(tracker);
        assertEquals(90.0, converter.convert(100.0, 0.9));
        Mockito.verify(tracker).incrementUsage(); // SPY: one call
    }

    @Test
    void testNegativeThrowsNoUsageTracked() {
        UsageTracker tracker = Mockito.spy(UsageTracker.class);
        CurrencyConverter converter = new CurrencyConverter(tracker);
        assertThrows(IllegalArgumentException.class, () -> converter.convert(-5, 0.9));
        Mockito.verify(tracker, Mockito.never()).incrementUsage(); // SPY
    }
}
```


#### Service Class

```java
public class CurrencyConverter {
    private final UsageTracker tracker;
    public CurrencyConverter(UsageTracker tracker) { this.tracker = tracker; }
    public double convert(double amount, double rate) {
        if(amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        tracker.incrementUsage();
        return amount * rate;
    }
}

interface UsageTracker {
    void incrementUsage();
}

```


---