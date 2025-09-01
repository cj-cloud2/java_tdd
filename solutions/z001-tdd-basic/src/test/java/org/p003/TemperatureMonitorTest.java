package org.p003;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class TemperatureMonitorTest {
    // RED: Above threshold triggers alert.
    @Test
    void testAlertTriggered() {
        TemperatureMonitor monitor = new TemperatureMonitor(30);
        monitor.addReading(32);
        assertTrue(monitor.isAlert());
    }
    // RED: Below threshold does not trigger alert.
    @Test
    void testNoAlertBelowThreshold() {
        TemperatureMonitor monitor = new TemperatureMonitor(30);
        monitor.addReading(28);
        assertFalse(monitor.isAlert());
    }
    // RED: Reset alert restores status.
    @Test
    void testResetAlert() {
        TemperatureMonitor monitor = new TemperatureMonitor(30);
        monitor.addReading(32);
        monitor.resetAlert();
        assertFalse(monitor.isAlert());
    }
}