package org.p002;

import org.junit.jupiter.api.Test;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorServiceTest {

    private final CalculatorService calculator = new CalculatorService();

    // 1. assertEquals
    @Test
    void testAdd() {
        int result = calculator.add(2, 3);
        assertEquals(5, result, "2 + 3 should equal 5");
    }

    // 2. assertNotEquals
    @Test
    void testAddNotEqual() {
        int result = calculator.add(2, 3);
        assertNotEquals(10, result, "2 + 3 should not equal 10");
    }

    // 3. assertTrue
    @Test
    void testIsEven() {
        assertTrue(calculator.isEven(4), "4 should be even");
    }

    // 4. assertFalse
    @Test
    void testIsNotEven() {
        assertFalse(calculator.isEven(7), "7 should not be even");
    }

    // 5. assertNull
    @Test
    void testGetNull() {
        assertNull(calculator.getNull(), "Should return null");
    }

    // 6. assertNotNull
    @Test
    void testGetMessage() {
        assertNotNull(calculator.getMessage(), "Should return non-null value");
    }

    // 7. assertSame (compares object references)
    @Test
    void testSameObject() {
        String message1 = calculator.getMessage();
        String message2 = calculator.getMessage();
        assertSame(message1, message2, "Should be same object");
    }

    // 8. assertNotSame
    @Test
    void testNotSameObject() {
        String message1 = "Hello";
        String message2 = new String("Hello");
        assertNotSame(message1, message2, "Different objects");
    }

    // 9. assertArrayEquals
    @Test
    void testGetNumbers() {
        int[] expected = {1, 2, 3};
        int[] actual = calculator.getNumbers();
        assertArrayEquals(expected, actual, "Arrays should match");
    }

    // 10. assertIterableEquals
    @Test
    void testIterableEquals() {
        List<Integer> expected = Arrays.asList(1, 2, 3);
        List<Integer> actual = Arrays.asList(1, 2, 3);
        assertIterableEquals(expected, actual, "Iterables should match");
    }

    // 11. assertThrows
    @Test
    void testDivideByZero() {
        assertThrows(ArithmeticException.class,
                () -> calculator.divide(10, 0),
                "Should throw ArithmeticException");
    }

    // 12. assertTimeout
    @Test
    void testTimeout() {
        assertTimeout(Duration.ofMillis(100),
                () -> {
                    Thread.sleep(50);
                    calculator.add(1, 1);
                },
                "Should complete within timeout");
    }

    // 13. assertAll (grouped assertions)
    @Test
    void testGroupedAssertions() {
        assertAll("Calculator operations",
                () -> assertEquals(4, calculator.add(2, 2)),
                () -> assertEquals(0, calculator.add(2, -2)),
                () -> assertTrue(calculator.isEven(8))
        );
    }
}
