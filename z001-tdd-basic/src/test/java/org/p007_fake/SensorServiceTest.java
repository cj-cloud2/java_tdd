package org.p007_fake;

/*

## 5. IoT: Fake Sensor Data Logger

### Task \& Requirements

- Readings from sensors are logged for analytics.
- Use a Fake logger in tests to verify logging calls and values.


#### Requirements:

- Log is append-only.
- List/verify values logged.
- Service triggers log call for each new value.

 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.util.*;

class FakeSensorLogger implements SensorLogger {
    final List<Double> logged = new ArrayList<>();
    public void log(double v) { logged.add(v); }
}

public class SensorServiceTest {
    @Test
    void testLogCalledOnReading() {
        FakeSensorLogger logger = new FakeSensorLogger();
        SensorService service = new SensorService(logger);
        service.processReading(12.3);
        assertEquals(List.of(12.3), logger.logged);
    }
}

// Fake is used for full inspection of log calls during tests.
