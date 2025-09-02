
# Dummys Examples

Note the focus on the use of **dummies** in situations where interfaces or collaborators are required by a class but not actively used or asserted upon. Each example uses JUnit 5 and, for instructional purposes, explains where a dummy object is introduced, why a mock is not used, and how tests are structured. Since dummy objects are not interacted with, they are created as minimal/no-op implementations and provided solely to satisfy constructor or method signatures.



### Summary of Dummy Usage (No Mock)
- **Dummy objects** are needed in these examples to satisfy dependencies where the collaborator is required by API but not involved in the logic under test.
- **No mock** objects are used because their behavior is neither controlled nor asserted in these test scenarios. Dummies may or may not be exercised by the code—if they are called, they perform no-op or return trivial values.
- **Service class** always contains the additional interface and dummy class where required




## Example 1: Finance – Transaction Validator with Logging Dummy

### Task \& Requirements

1. **Task:** Validate bank transactions using a service; the service also receives a logger interface.
2. **Requirements:**
    - If transaction amount is negative, throw an exception.
    - Valid transactions should return true.
    - Logger is required but not used in validation logic (just a dependency).

### JUnit 5 Test Class

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// The purpose of these tests is to verify validation logic, not logging.

class TransactionValidatorTest {
    // Test for negative transaction throws exception.
    @Test
    void testNegativeAmountThrows() {
        TransactionLogger dummyLogger = new DummyTransactionLogger();
        TransactionValidator validator = new TransactionValidator(dummyLogger);
        assertThrows(IllegalArgumentException.class, () -> validator.validate(-10));
    }

    // Test for valid transaction returns true.
    @Test
    void testValidAmountReturnsTrue() {
        TransactionLogger dummyLogger = new DummyTransactionLogger();
        TransactionValidator validator = new TransactionValidator(dummyLogger);
        assertTrue(validator.validate(100));
    }
    
    // Dummy is provided because TransactionValidator requires a logger in constructor,
    // but the logger is not used in any assertion or logic here.
}
```

> **Where Dummy is Used:**
`DummyTransactionLogger` implements `TransactionLogger`, and is injected but not used or asserted upon.

### Service and Dummy Implementation

```java
// Service Class
public class TransactionValidator {
    private final TransactionLogger logger;
    public TransactionValidator(TransactionLogger logger) {
        this.logger = logger; // accepts logger but doesn't use it
    }
    public boolean validate(double amount) {
        if (amount < 0) throw new IllegalArgumentException("Negative transaction");
        return true;
    }
}

// The interface to be dummied
interface TransactionLogger {
    void log(String message);
}

// Dummy implementation for injecting into constructor
class DummyTransactionLogger implements TransactionLogger {
    public void log(String message) {} // No-op
}
```


## Example 2: Banking – AccountService Requires Notification Dummy

### Task \& Requirements

1. **Task:** AccountService manages deposits, requires a notification service.
2. **Requirements:**
    - Depositing a negative amount should throw an exception.
    - Depositing a positive amount succeeds.
    - Notification service is present but not used (dummy).

### JUnit 5 Test Class

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class AccountServiceTest {
    @Test
    void testDepositNegativeThrows() {
        Notification dummyNotif = new DummyNotification();
        AccountService as = new AccountService(dummyNotif);
        assertThrows(IllegalArgumentException.class, () -> as.deposit(-50));
    }

    @Test
    void testDepositValid() {
        Notification dummyNotif = new DummyNotification();
        AccountService as = new AccountService(dummyNotif);
        assertEquals(100, as.deposit(100));
    }
    // DummyNotification is needed as AccountService must have a Notification,
    // but it doesn't send any notification in deposit, so we use dummy.
}
```


### Service and Dummy Implementation

```java
public class AccountService {
    private Notification notification;
    public AccountService(Notification notification) {
        this.notification = notification; // Required but not used
    }
    public int deposit(int amount) {
        if (amount < 0) throw new IllegalArgumentException();
        return amount;
    }
}

interface Notification {
    void send(String user, String msg);
}

class DummyNotification implements Notification {
    public void send(String user, String msg) {} // No-op
}
```


## Example 3: IoT – TemperatureSensor Requires Authenticator Dummy

### Task \& Requirements

1. **Task:** Temperature sensor records readings, requires an authenticator.
2. **Requirements:**
    - Recording temperature below -100 or above 200 throws exception.
    - Valid readings are stored.
    - Authenticator is present but not consulted in these methods.

### JUnit 5 Test Class

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TemperatureSensorTest {
    @Test
    void testExtremeLowTemperatureThrows() {
        Authenticator dummyAuth = new DummyAuthenticator();
        TemperatureSensor sensor = new TemperatureSensor(dummyAuth);
        assertThrows(IllegalArgumentException.class, () -> sensor.record(-150));
    }

    @Test
    void testValidTemperature() {
        Authenticator dummyAuth = new DummyAuthenticator();
        TemperatureSensor sensor = new TemperatureSensor(dummyAuth);
        sensor.record(25);
        assertEquals(1, sensor.getReadingsCount());
    }
    // DummyAuthenticator is supplied as dependency but not utilized by record().
}
```


### Service and Dummy Implementation

```java
import java.util.ArrayList;
import java.util.List;

public class TemperatureSensor {
    private Authenticator authenticator;
    private List<Integer> readings = new ArrayList<>();
    public TemperatureSensor(Authenticator auth) {
        this.authenticator = auth;
    }
    public void record(int temp) {
        if (temp < -100 || temp > 200) throw new IllegalArgumentException();
        readings.add(temp);
    }
    public int getReadingsCount() { return readings.size(); }
}

interface Authenticator {
    boolean authenticate(String token);
}

class DummyAuthenticator implements Authenticator {
    public boolean authenticate(String token) { return true; }
}
```


## Example 4: Finance – Invoice Service with CurrencyProvider Dummy

### Task \& Requirements

1. **Task:** InvoiceService calculates totals, requires a currency provider for possible future extensions.
2. **Requirements:**
    - Adding line with negative price/quantity throws error.
    - Calculating total returns correct sum.
    - CurrencyProvider is unused in calculation logic.

### JUnit 5 Test Class

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class InvoiceServiceTest {
    @Test
    void testAddNegativePriceThrows() {
        CurrencyProvider dummyCP = new DummyCurrencyProvider();
        InvoiceService invoice = new InvoiceService(dummyCP);
        assertThrows(IllegalArgumentException.class, () -> invoice.addLine(-1, 5));
    }
    @Test
    void testGetTotalCorrect() {
        CurrencyProvider dummyCP = new DummyCurrencyProvider();
        InvoiceService invoice = new InvoiceService(dummyCP);
        invoice.addLine(10, 3);
        invoice.addLine(20, 2);
        assertEquals(70, invoice.getTotal());
    }
    // CurrencyProvider is passed as required dependency, not used; dummy supplied.
}
```


### Service and Dummy Implementation

```java
import java.util.*;

public class InvoiceService {
    private CurrencyProvider currencyProvider;
    private List<Integer> lineTotals = new ArrayList<>();
    public InvoiceService(CurrencyProvider cp) { this.currencyProvider = cp; }
    public void addLine(int price, int qty) {
        if (price < 0 || qty < 0) throw new IllegalArgumentException();
        lineTotals.add(price * qty);
    }
    public int getTotal() {
        return lineTotals.stream().mapToInt(Integer::intValue).sum();
    }
}

interface CurrencyProvider {
    String getCurrencyCode();
}

class DummyCurrencyProvider implements CurrencyProvider {
    public String getCurrencyCode() { return "DUMMY"; }
}
```


## Example 5: Banking – CustomerManager Needs Dummy IDGenerator

### Task \& Requirements

1. **Task:** CustomerManager adds customers, needs an IDGenerator (unused directly in test scenario).
2. **Requirements:**
    - Adding null/empty name throws exception.
    - Valid customer is added.
    - IDGenerator is required by constructor but not used here.

### JUnit 5 Test Class

```java
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CustomerManagerTest {
    @Test
    void testAddNullNameThrows() {
        IDGenerator dummyIDGen = new DummyIDGenerator();
        CustomerManager mgr = new CustomerManager(dummyIDGen);
        assertThrows(IllegalArgumentException.class, () -> mgr.addCustomer(null));
    }
    @Test
    void testAddValidCustomer() {
        IDGenerator dummyIDGen = new DummyIDGenerator();
        CustomerManager mgr = new CustomerManager(dummyIDGen);
        mgr.addCustomer("Alice");
        assertEquals(1, mgr.getCustomerCount());
    }
    // DummyIDGenerator only satisfies dependency; not interacting with it.
}
```


### Service and Dummy Implementation

```java
import java.util.*;

public class CustomerManager {
    private List<String> customers = new ArrayList<>();
    private IDGenerator idGenerator;
    public CustomerManager(IDGenerator gen) {
        this.idGenerator = gen;
    }
    public void addCustomer(String name) {
        if (name == null || name.isEmpty()) throw new IllegalArgumentException();
        customers.add(name);
    }
    public int getCustomerCount() { return customers.size(); }
}

interface IDGenerator {
    String generate();
}

class DummyIDGenerator implements IDGenerator {
    public String generate() { return "DUMMY-ID"; }
}
```



