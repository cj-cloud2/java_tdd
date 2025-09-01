package org.p003;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class CurrencyConverterTest {
    // RED: Start with a failing test for normal conversion.
    @Test
    void testConvertAmount() {
        CurrencyConverter converter = new CurrencyConverter();
        assertEquals(90.0, converter.convert(100.0, 0.9));
    }

    // RED: Test zero amount conversion.
    @Test
    void testConvertZeroAmount() {
        CurrencyConverter converter = new CurrencyConverter();
        assertEquals(0.0, converter.convert(0, 0.9));
    }

    // RED: Negative amount should throw an error.
    @Test
    void testNegativeAmount() {
        CurrencyConverter converter = new CurrencyConverter();
        assertThrows(IllegalArgumentException.class, () -> converter.convert(-5, 0.9));
    }


}
// GREEN: After each test, write minimal code to pass.
// REFACTOR: No complex logic, method is clear.

