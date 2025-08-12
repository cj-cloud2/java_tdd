package org.p008_dummy;
/*

## Example 2: Banking – AccountService Requires Notification Dummy

### Task \& Requirements

1. **Task:** AccountService manages deposits, requires a notification service.
2. **Requirements:**
    - Depositing a negative amount should throw an exception.
    - Depositing a positive amount succeeds.
    - Notification service is present but not used (dummy).
 */
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

// DummyNotification is needed as AccountService must have a Notification,
// but it doesn't send any notification in deposit, so we use dummy.
public class AccountServiceTest {
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

}

class DummyNotification implements Notification {
    public void send(String user, String msg) {} // No-op
}