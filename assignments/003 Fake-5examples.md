# Fake Usage Examples

Here, “Fake” means a simple replacement for an external dependency or resource (such as persistence or remote system) implemented purely for testing—often an in-memory or trivial implementation. Unlike stubs or mocks, Fakes are fully functional substitutes but not production-grade.

**Summary of "Fake" Usage:**

- Each test demonstrates isolation from real-world side effects or persistence.
- Fakes provide fully functional, controllable testing substitutes for real dependencies.
- No mocks/stubs used; Fakes are simple, deterministic implementations ideal for logic and integration-style unit tests.
Each example provides clear separation of concerns, making Fakes easy to swap for real dependencies in production.





## 1. Finance: In-Memory Fake Currency Repository

### Task \& Detailed Requirements

- Implement a service that performs currency conversion using dynamic rates fetched from a repository.
- The repository interface is expected to connect to a remote server in production.
- For testing, use a Fake repository that is in-memory and controllably seeded.


#### Requirements:

- Accept amount and currency codes (from, to).
- Fetch conversion rate from repository.
- Calculate and return the result.
- Throw error if rate not available.


### JUnit5 Test Class (with Fakes)

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// Fake is used instead of real CurrencyRateRepository, enabling test isolation without remote calls.
// Fake for CurrencyRateRepository for testing (all in-memory)
class FakeCurrencyRateRepository implements CurrencyRateRepository {
    private final Map<String, Double> rates = new HashMap<>();
    void seedRate(String pair, double rate) { rates.put(pair, rate); }
    public Double getRate(String from, String to) {
        return rates.get(from + "_" + to);
    }
}

class CurrencyConverterServiceTest {
    // Purpose: Verify conversion using seeded in-memory rates
    @Test
    void testConversionWithAvailableRate() {
        FakeCurrencyRateRepository fakeRepo = new FakeCurrencyRateRepository();
        fakeRepo.seedRate("USD_EUR", 0.9);
        CurrencyConverterService service = new CurrencyConverterService(fakeRepo);
        assertEquals(90, service.convert("USD", "EUR", 100));
    }
    // Purpose: Check error when rate missing
    @Test
    void testMissingRateThrows() {
        CurrencyConverterService service = new CurrencyConverterService(new FakeCurrencyRateRepository());
        assertThrows(IllegalArgumentException.class, () -> service.convert("USD", "JPY", 100));
    }
}

```


### Service Class Example

```java
class CurrencyConverterService {
    private final CurrencyRateRepository repository;
    public CurrencyConverterService(CurrencyRateRepository repo) { this.repository = repo; }
    public double convert(String from, String to, double amount) {
        Double rate = repository.getRate(from, to);
        if (rate == null) throw new IllegalArgumentException("Rate not found");
        return amount * rate;
    }
}
interface CurrencyRateRepository { Double getRate(String from, String to); }
```


## 2. IoT: Fake Notification Dispatcher

### Task \& Requirements

- Create a temperature monitor that sends alerts if a reading exceeds a threshold.
- Actual alerting interface would dispatch notifications to hardware or messaging system.
- In tests, wire up a Fake dispatcher that saves sent notifications to an in-memory list.


#### Requirements:

- Accept temperature readings.
- Dispatch alert on exceed.
- Allow verification of notification content.


### JUnit5 Test Class (with Fakes)

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

// The Fake replaces the real dispatcher, tracking calls in-memory for test verification.

class FakeNotificationDispatcher implements NotificationDispatcher {
    List<String> sent = new ArrayList<>();
    public void dispatch(String message) { sent.add(message); }
    boolean didSend(String msg) { return sent.contains(msg); }
}

class TemperatureMonitorTest {
    @Test
    void testAlertSentOnExceed() {
        FakeNotificationDispatcher fakeDispatcher = new FakeNotificationDispatcher();
        TemperatureMonitor monitor = new TemperatureMonitor(30, fakeDispatcher);
        monitor.addReading(35);
        assertTrue(fakeDispatcher.didSend("ALERT: 35.0 above 30.0"));
    }
}

```


### Service Class Example

```java
class TemperatureMonitor {
    private final double threshold;
    private final NotificationDispatcher dispatcher;
    public TemperatureMonitor(double threshold, NotificationDispatcher d) { this.threshold = threshold; this.dispatcher = d; }
    public void addReading(double value) {
        if (value > threshold) dispatcher.dispatch("ALERT: " + value + " above " + threshold);
    }
}
interface NotificationDispatcher { void dispatch(String message); }
```


## 3. Banking: Fake In-Memory Account Store

### Task \& Requirements

- Implement a funds transfer operation between two accounts, updating balances in a backing store.
- Actual store is persistent; test via a Fake in-memory store.


#### Requirements:

- Store accounts and balances.
- Transfer should debit and credit correctly.
- Error if account missing or insufficient funds.


### JUnit5 Test Class (with Fakes)

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;
// Fake allows full control of test data and simulates persistence.

class FakeAccountStore implements AccountStore {
    Map<String, Double> accounts = new HashMap<>();
    public Double getBalance(String id) { return accounts.get(id); }
    public void setBalance(String id, double b) { accounts.put(id, b); }
}

class TransferServiceTest {
    @Test
    void testSuccessfulTransfer() {
        FakeAccountStore store = new FakeAccountStore();
        store.setBalance("A", 100);
        store.setBalance("B", 50);
        TransferService service = new TransferService(store);
        service.transfer("A", "B", 40);
        assertEquals(60, store.getBalance("A"));
        assertEquals(90, store.getBalance("B"));
    }
    @Test
    void testInsufficientFunds() {
        FakeAccountStore store = new FakeAccountStore();
        store.setBalance("A", 10);
        store.setBalance("B", 50);
        TransferService service = new TransferService(store);
        assertThrows(IllegalArgumentException.class, () -> service.transfer("A", "B", 40));
    }
}

```


### Service Class Example

```java
class TransferService {
    private final AccountStore store;
    public TransferService(AccountStore s) { this.store = s; }
    public void transfer(String from, String to, double amount) {
        Double fromBal = store.getBalance(from);
        Double toBal = store.getBalance(to);
        if (fromBal == null || toBal == null) throw new IllegalArgumentException();
        if (fromBal < amount) throw new IllegalArgumentException("Insufficient funds");
        store.setBalance(from, fromBal - amount);
        store.setBalance(to, toBal + amount);
    }
}
interface AccountStore {
    Double getBalance(String id);
    void setBalance(String id, double balance);
}
```


## 4. Finance: Fake In-Memory Budget Category Limit Store

### Task \& Requirements

- Service to auto-block expenses if a category’s monthly total would exceed a user’s custom limit.
- Real system queries a remote DB; tests should use an in-memory Fake.


#### Requirements:

- Each expense has amount/category/date.
- Adding expense checks sum for that month/category.
- Block if limit exceeded. Limit is stored in Fake.


### JUnit5 Test Class (with Fakes)

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.*;

// Fake lets you configure and verify limit logic with no external DB.

class FakeBudgetLimitStore implements BudgetLimitStore {
    final Map<String, Double> limits = new HashMap<>();
    public void setLimit(String category, double limit) { limits.put(category, limit); }
    public Double getLimit(String category) { return limits.get(category); }
}

class ExpenseBlockerTest {
    @Test
    void testBlockedWhenOverLimit() {
        FakeBudgetLimitStore store = new FakeBudgetLimitStore();
        store.setLimit("Food", 100);
        ExpenseBlocker blocker = new ExpenseBlocker(store);
        blocker.addExpense("Food", 60, LocalDate.of(2025, 3, 1));
        assertThrows(IllegalStateException.class, () -> blocker.addExpense("Food", 50, LocalDate.of(2025, 3, 10)));
    }
}

```


### Service Class Example

```java
class ExpenseBlocker {
    private final BudgetLimitStore store;
    private final Map<String, List<Double>> monthSums = new HashMap<>();
    public ExpenseBlocker(BudgetLimitStore store) { this.store = store; }
    public void addExpense(String category, double amt, LocalDate date) {
        monthSums.putIfAbsent(category, new ArrayList<>());
        double sum = monthSums.get(category).stream().mapToDouble(Double::doubleValue).sum() + amt;
        if (sum > store.getLimit(category)) throw new IllegalStateException("Limit exceeded");
        monthSums.get(category).add(amt);
    }
}
interface BudgetLimitStore { Double getLimit(String category); }
```


## 5. IoT: Fake Sensor Data Logger

### Task \& Requirements

- Readings from sensors are logged for analytics.
- Use a Fake logger in tests to verify logging calls and values.


#### Requirements:

- Log is append-only.
- List/verify values logged.
- Service triggers log call for each new value.


### JUnit5 Test Class (with Fakes)

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

// Fake is used for full inspection of log calls during tests.

class FakeSensorLogger implements SensorLogger {
    final List<Double> logged = new ArrayList<>();
    public void log(double v) { logged.add(v); }
}

class SensorServiceTest {
    @Test
    void testLogCalledOnReading() {
        FakeSensorLogger logger = new FakeSensorLogger();
        SensorService service = new SensorService(logger);
        service.processReading(12.3);
        assertEquals(List.of(12.3), logger.logged);
    }
}

```


### Service Class Example

```java
class SensorService {
    private final SensorLogger logger;
    public SensorService(SensorLogger l) { this.logger = l; }
    public void processReading(double v) { logger.log(v); }
}
interface SensorLogger { void log(double value); }
```



