package org.p001;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class C002CalculatorTest {

    @Test
    void shouldAddTwoPositiveNumbers() {
        // Given
        C002Calculator calculator = new C002Calculator();
        int a = 2;
        int b = 3;

        // When
        int result = calculator.add(a, b);

        // Then
        assertEquals(5, result);
    }
}