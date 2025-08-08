package org.p007_fake;

/*


## 1. Finance: In-Memory Fake Currency Repository

### Task \& Detailed Requirements ::

- Implement a service that performs currency conversion using dynamic rates fetched from a repository.
- The repository interface is expected to connect to a remote server in production.
- For testing, use a Fake repository that is in-memory and controllably seeded.


#### Requirements:

- Accept amount and currency codes (from, to).
- Fetch conversion rate from repository.
- Calculate and return the result.
- Throw error if rate not available.
 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import java.util.*;

// Fake for CurrencyRateRepository for testing (all in-memory)
class FakeCurrencyRateRepository implements CurrencyRateRepository {
    private final Map<String, Double> rates = new HashMap<>();
    void seedRate(String pair, double rate) { rates.put(pair, rate); }
    public Double getRate(String from, String to) {
        return rates.get(from + "_" + to);
    }
}

public class CurrencyConverterServiceTest {
    // Purpose: Verify conversion using seeded in-memory rates
    @Test
    void testConversionWithAvailableRate() {
        FakeCurrencyRateRepository fakeRepo = new FakeCurrencyRateRepository();
        fakeRepo.seedRate("USD_EUR", 0.9);
        CurrencyConverterService service = new CurrencyConverterService(fakeRepo);
        assertEquals(90, service.convert("USD", "EUR", 100));
    }
    // Purpose: Check error when rate missing
    @Test
    void testMissingRateThrows() {
        CurrencyConverterService service = new CurrencyConverterService(new FakeCurrencyRateRepository());
        assertThrows(IllegalArgumentException.class, () -> service.convert("USD", "JPY", 100));
    }
}

// Fake is used instead of real CurrencyRateRepository, enabling test isolation without remote calls.