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
// Fake for CurrencyRateRepository for testing (all in-memory)
// Fake is used instead of real CurrencyRateRepository, enabling test isolation without remote calls.
 FakeCurrencyRateRepository implements CurrencyRateRepository

class CurrencyConverterServiceTest

 


### Service Class Example
```java
class CurrencyConverterService 

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
// The Fake replaces the real dispatcher, tracking calls in-memory for test verification.
class FakeNotificationDispatcher implements NotificationDispatcher 

class TemperatureMonitorTest

```


### Service Class Example
```java
class TemperatureMonitor

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
// Fake allows full control of test data and simulates persistence.

class FakeAccountStore implements AccountStore {}

class TransferServiceTest {}

```


### Service Class Example

```java
class TransferService {}
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
// Fake lets you configure and verify limit logic with no external DB.
class FakeBudgetLimitStore implements BudgetLimitStore {}



class ExpenseBlockerTest {}

```


### Service Class Example

```java
class ExpenseBlocker {}
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
// Fake is used for full inspection of log calls during tests.
class FakeSensorLogger implements SensorLogger {
    final List<Double> logged = new ArrayList<>();
    public void log(double v) { logged.add(v); }
}

class SensorServiceTest {
    @Test
    void testLogCalledOnReading() {}
}

```


### Service Class Example

```java
class SensorService {}
interface SensorLogger { void log(double value); }
```



