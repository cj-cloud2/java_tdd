package org.p010_spy;
/*
## Example 5. Finance: Currency Converter with API Usage Tracker

        ### Task and Requirements

        - Currency conversion function; every call reports API usage.
        - Requirements:

        1. Normal conversion,
        2. Negative amount errors,
        3. API usage is tracked after every valid attempt (calls incrementUsage).


        #### Test Class Using SPY

*/

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class CurrencyConverterTest {
    @Test
    void testNormalConversionTracksUsage() {
        UsageTracker tracker = Mockito.spy(UsageTracker.class);
        CurrencyConverter converter = new CurrencyConverter(tracker);
        assertEquals(90.0, converter.convert(100.0, 0.9));
        Mockito.verify(tracker).incrementUsage(); // SPY: one call
    }

    @Test
    void testNegativeThrowsNoUsageTracked() {
        UsageTracker tracker = Mockito.spy(UsageTracker.class);
        CurrencyConverter converter = new CurrencyConverter(tracker);
        assertThrows(IllegalArgumentException.class, () -> converter.convert(-5, 0.9));
        Mockito.verify(tracker, Mockito.never()).incrementUsage(); // SPY
    }
}