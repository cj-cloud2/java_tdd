package org.p010_spy;

/*
## Example 2. IoT: Device Temperature Monitor with Alert

### Task and Requirements

- A temperature monitoring class triggers an alert via notifier if a threshold is crossed.
        - Requirements:

        1. Add a reading above threshold triggers alert and notifies admin only once,
2. Below the threshold does nothing,
        3. Reset restores state and permits a new notification later.


        #### Test Class Using SPY
*/

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