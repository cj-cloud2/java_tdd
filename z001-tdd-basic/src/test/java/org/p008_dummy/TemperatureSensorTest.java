package org.p008_dummy;
/*

## Example 3: IoT – TemperatureSensor Requires Authenticator Dummy

### Task \& Requirements

1. **Task:** Temperature sensor records readings, requires an authenticator.
2. **Requirements:**
    - Recording temperature below -100 or above 200 throws exception.
    - Valid readings are stored.
    - Authenticator is present but not consulted in these methods.
 */

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

class DummyAuthenticator implements Authenticator {
    public boolean authenticate(String token) { return true; }
}