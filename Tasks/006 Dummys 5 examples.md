
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
// Dummy is provided because TransactionValidator requires a logger in constructor,
// but the logger is not used in any assertion or logic here.
class TransactionValidatorTest {
    // Test for negative transaction throws exception.
    @Test
    void testNegativeAmountThrows() {
    }

    // Test for valid transaction returns true.
    @Test
    void testValidAmountReturnsTrue() {
    }
    
   
}

// Dummy implementation for injecting into constructor
class DummyTransactionLogger implements TransactionLogger {
    public void log(String message) {} // No-op
}
```


### Service and Dummy Implementation

```java
// Service Class
public class TransactionValidator {}

// The interface to be dummied
interface TransactionLogger {
    void log(String message);
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
class AccountServiceTest {
    @Test
    void testDepositNegativeThrows() {
    }

    @Test
    void testDepositValid() {
    }
    
}

// DummyNotification is needed as AccountService must have a Notification,
// but it doesn't send any notification in deposit, so we use dummy.
class DummyNotification implements Notification {}
```


### Service and Dummy Implementation

```java
public class AccountService {  
    public AccountService(Notification notification) {        
    }
    public int deposit(int amount) {    
        //some logic for databasing(not to be implemented here)    
        return amount;
    }
}

interface Notification {
    void send(String user, String msg);
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
class TemperatureSensorTest {
    @Test
    void testExtremeLowTemperatureThrows() {
    }

    @Test
    void testValidTemperature() {}
}



// DummyAuthenticator is supplied as dependency but not utilized by record().
class DummyAuthenticator implements Authenticator {}
```




### Service and Dummy Implementation

```java
public class TemperatureSensor {}

interface Authenticator {
    boolean authenticate(String token);
}


```
