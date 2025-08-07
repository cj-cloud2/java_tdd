
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
class BudgetTrackerTest {
    // Requirement 1: Adding expenses below the limit should not trigger over-budget.
    @Test
    void testExpenseWithinLimit() {
    }

    // Requirement 2: Adding expense above 80% limit triggers notification.
    @Test
    void testNotifyLimitApproaching() {
    }

    // Requirement 3: Adding expense exceeding limit triggers over budget.
    @Test
    void testExpenseOverLimit() {
    }

    // SPY Explanation:
    // The notifier spy remembers each call to notifyLimitApproaching, allowing us to verify interaction,
    // rather than stubbing the behavior.
}
```


#### Service and Helper Classes

```java

public class BudgetTracker {}

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

class TemperatureMonitorTest {
    @Test
    void testAboveThresholdTriggersAlertAndNotify() {
    }

    @Test
    void testBelowThresholdNoAlert() {
    }

    @Test
    void testResetAllowsResend() {
    }
}
```


#### Service Class

```java
public class TemperatureMonitor {
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

class SimpleInterestCalculatorTest {
    @Test
    void testNormalCalculationAndAudit() {
    }

    @Test
    void testZeroPrincipalAndAudit() {
    }

    @Test
    void testNegativeRateThrowsAndNoAudit() {
    }
}
```


#### Service Class

```java
public class SimpleInterestCalculator {
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
class TransactionHistoryTest {
    @Test
    void testFilterByDateLogsAccess() {
    }

    @Test
    void testInvalidRangeThrowsAndNoLog() {
    }
}
```


#### Service and Helper Classes

```java
public class TransactionHistory {
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
class CurrencyConverterTest {
    @Test
    void testNormalConversionTracksUsage() {
    }

    @Test
    void testNegativeThrowsNoUsageTracked() {
    }
}
```


#### Service Class

```java
public class CurrencyConverter {
}

interface UsageTracker {
    void incrementUsage();
}

```


---