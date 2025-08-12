package org.p003;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class SimpleInterestCalculatorTest {
    // RED: Normal calculation.
    @Test
    void testCalcSimpleInterest() {
        SimpleInterestCalculator calc = new SimpleInterestCalculator();
        assertEquals(150.0, calc.compute(1000, 5, 3));
    }
    // RED: $0 principal returns $0 interest.
    @Test
    void testZeroPrincipal() {
        SimpleInterestCalculator calc = new SimpleInterestCalculator();
        assertEquals(0.0, calc.compute(0, 5, 3));
    }
    // RED: Negative rate throws error.
    @Test
    void testNegativeRate() {
        SimpleInterestCalculator calc = new SimpleInterestCalculator();
        assertThrows(IllegalArgumentException.class, () -> calc.compute(1000, -1, 3));
    }
}