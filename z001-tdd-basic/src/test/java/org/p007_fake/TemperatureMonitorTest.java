package org.p007_fake;
/*

## 2. IoT: Fake Notification Dispatcher

### Task \& Requirements

- Create a temperature monitor that sends alerts if a reading exceeds a threshold.
- Actual alerting interface would dispatch notifications to hardware or messaging system.
- In tests, wire up a Fake dispatcher that saves sent notifications to an in-memory list.


#### Requirements:

- Accept temperature readings.
- Dispatch alert on exceed.
- Allow verification of notification content.

 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

// The Fake replaces the real dispatcher, tracking calls in-memory for test verification.
class FakeNotificationDispatcher implements NotificationDispatcher {
    List<String> sent = new ArrayList<>();

    public void dispatch(String message) {
        sent.add(message);
    }

    boolean didSend(String msg) {
        return sent.contains(msg);
    }
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

