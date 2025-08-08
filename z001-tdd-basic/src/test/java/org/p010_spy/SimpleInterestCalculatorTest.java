package org.p010_spy;
/*
## Example 3. Banking: Simple Interest Calculator with Auditing

        ### Task and Requirements

        - Calculate simple interest; log every calculation for audit,
        - Requirements:

        1. Normal calculation,
        2. Zero principal,
        3. Negative rate throws error,
        4. Each calculation triggers an audit log call with all arguments.


        #### Test Class Using SPY

*/

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


class SimpleInterestCalculatorTest {
    @Test
    void testNormalCalculationAndAudit() {
        AuditLogger logger = Mockito.spy(AuditLogger.class);
        SimpleInterestCalculator calc = new SimpleInterestCalculator(logger);
        double interest = calc.compute(1000, 5, 3);
        assertEquals(150.0, interest);
        Mockito.verify(logger).logCalculation(1000, 5, 3); // SPY verify
    }

    @Test
    void testZeroPrincipalAndAudit() {
        AuditLogger logger = Mockito.spy(AuditLogger.class);
        SimpleInterestCalculator calc = new SimpleInterestCalculator(logger);
        double interest = calc.compute(0, 5, 2);
        assertEquals(0.0, interest);
        Mockito.verify(logger).logCalculation(0, 5, 2);
    }

    @Test
    void testNegativeRateThrowsAndNoAudit() {
        AuditLogger logger = Mockito.spy(AuditLogger.class);
        SimpleInterestCalculator calc = new SimpleInterestCalculator(logger);
        assertThrows(IllegalArgumentException.class, () -> calc.compute(1000, -1, 3));
        Mockito.verify(logger, Mockito.never()).logCalculation(Mockito.anyDouble(), Mockito.anyDouble(), Mockito.anyInt());
    }
}