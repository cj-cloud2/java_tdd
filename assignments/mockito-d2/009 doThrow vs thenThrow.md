In Mockito, `thenThrow` and `doThrow` are both used to configure mock objects to throw exceptions, but they serve different purposes based on the **type of method being stubbed**. The fundamental difference lies in whether the method returns a value or is void.

### When to Use `thenThrow`

The `when().thenThrow()` syntax is used for **methods that return a value** (non-void methods). This approach reads more naturally and follows the typical "when-then" pattern that resembles English grammar.

**Example for Non-Void Methods:**

```java
// For methods that return a value
@Test
void testThenThrowWithReturnValue() {
    // Arrange
    UserService userService = mock(UserService.class);
    
    // Stub the method to throw an exception
    when(userService.findUserById(1L))
        .thenThrow(new UserNotFoundException("User not found"));
    
    // Act & Assert
    assertThrows(UserNotFoundException.class, () -> {
        userService.findUserById(1L);
    });
}
```


### When to Use `doThrow`

The `doThrow()` method is **mandatory for void methods** because `when().thenThrow()` won't compile without a return value. Additionally, `doThrow` is safer in certain scenarios even for non-void methods.

**Example for Void Methods:**

```java
// For void methods - doThrow is required
@Test
void testDoThrowWithVoidMethod() {
    // Arrange
    EmailService emailService = mock(EmailService.class);
    
    // Stub the void method to throw an exception
    doThrow(new EmailDeliveryException("SMTP server unavailable"))
        .when(emailService).sendEmail("test@example.com", "Subject", "Body");
    
    // Act & Assert
    assertThrows(EmailDeliveryException.class, () -> {
        emailService.sendEmail("test@example.com", "Subject", "Body");
    });
}
```


### Complete Comparative Example

Here's a comprehensive example demonstrating both approaches with working code:

'BankService.java'

```java

package edu.m008.doThrow;

// Service classes for demonstration
class BankService {
    private final AccountRepository accountRepository;
    private final NotificationService notificationService;

    public BankService(AccountRepository accountRepository,
                       NotificationService notificationService) {
        this.accountRepository = accountRepository;
        this.notificationService = notificationService;
    }

    // Non-void method that returns Account
    public Account getAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    // Void method that sends notification
    public void processTransaction(String accountNumber, double amount) {
        notificationService.sendTransactionAlert(accountNumber, amount);
    }
}

class Account {
    private String accountNumber;
    private double balance;

    public Account(String accountNumber, double balance) {
        this.accountNumber = accountNumber;
        this.balance = balance;
    }

    // getters and setters...
}

interface AccountRepository {
    Account findByAccountNumber(String accountNumber);
}

interface NotificationService {
    void sendTransactionAlert(String accountNumber, double amount);
}

```

'BankServiceTest.java'
```java
package edu.m008.doThrow;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;



// Test class demonstrating both approaches
public class BankServiceTest {

    @Test
    void testThenThrow_ForNonVoidMethod() {
        // Arrange
        AccountRepository mockRepository = mock(AccountRepository.class);
        NotificationService mockNotification = mock(NotificationService.class);
        BankService bankService = new BankService(mockRepository, mockNotification);

        // Use thenThrow for non-void method
        when(mockRepository.findByAccountNumber("12345"))
                .thenThrow(new RuntimeException("Database connection failed"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bankService.getAccount("12345");
        });

        assertEquals("Database connection failed", exception.getMessage());
    }

    @Test
    void testDoThrow_ForVoidMethod() {
        // Arrange
        AccountRepository mockRepository = mock(AccountRepository.class);
        NotificationService mockNotification = mock(NotificationService.class);
        BankService bankService = new BankService(mockRepository, mockNotification);

        // Use doThrow for void method - thenThrow won't compile here
        doThrow(new RuntimeException("SMS service unavailable"))
                .when(mockNotification).sendTransactionAlert("12345", 100.0);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bankService.processTransaction("12345", 100.0);
        });

        assertEquals("SMS service unavailable", exception.getMessage());
    }

    @Test
    void testDoThrow_SaferForNonVoidMethods() {
        // Arrange
        AccountRepository mockRepository = mock(AccountRepository.class);
        NotificationService mockNotification = mock(NotificationService.class);
        BankService bankService = new BankService(mockRepository, mockNotification);

        // doThrow can also be used for non-void methods and is safer
        doThrow(new RuntimeException("Database connection failed"))
                .when(mockRepository).findByAccountNumber("12345");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bankService.getAccount("12345");
        });

        assertEquals("Database connection failed", exception.getMessage());
    }
}
```


### Key Safety Considerations

The `doThrow` approach is **safer** because `when().thenThrow()` actually calls the method during stubbing, which can cause issues if :

- The method has existing stubbed behavior that might interfere
- The method has side effects that shouldn't execute during test setup
- The method itself throws exceptions during the stubbing phase

The `doThrow().when()` syntax avoids these issues by configuring the mock without invoking the actual method.

### Summary

- **Use `thenThrow`**: For non-void methods when you want readable, English-like syntax and there are no safety concerns
- **Use `doThrow`**: Mandatory for void methods, and recommended as a safer alternative for non-void methods when dealing with complex stubbing scenarios

Both approaches achieve the same result for exception stubbing, but the choice depends on method return type and safety requirements in your specific testing scenario.