package org.p009_mocks;

/*
Example 1: Difficulty Level 1 - Ecommerce
Domain: Ecommerce
Task: Test an Order Processor that calculates total order cost

Requirements:
Calculate total cost: unitPrice × quantity
Apply 10% discount if quantity ≥ 20
Skip discount for quantities < 20
Validate: unitPrice > 0 and quantity > 0 (throw IllegalArgumentException for invalid inputs)

Mock Explanation:
Product is mocked to isolate OrderProcessor from external dependencies (e.g., database).
when(...).thenReturn(...) stubs getUnitPrice() to return fixed values for test scenarios.
verify(...) checks mock interactions (e.g., confirm getUnitPrice() was called twice in the first test).
 */



import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderProcessorTest {

    @Mock
    Product product; // Mock external dependency (e.g., database call)

    @InjectMocks
    OrderProcessor orderProcessor; // Class under test

    @Test
    void calculateTotalCost_NoDiscount() {
        // Requirement 1 & 2: Normal calculation without discount
        when(product.getUnitPrice()).thenReturn(100.0);
        double result = orderProcessor.calculateTotalCost(product, 10);
        assertEquals(1000.0, result); // 100 * 10 = 1000
        verify(product, times(2)).getUnitPrice(); // Verify mock interaction
    }

    @Test
    void calculateTotalCost_WithDiscount() {
        // Requirement 2: Discount applied for bulk orders
        when(product.getUnitPrice()).thenReturn(100.0);
        double result = orderProcessor.calculateTotalCost(product, 25);
        assertEquals(2250.0, result); // (100 * 25) * 0.9 = 2250
    }

    @Test
    void calculateTotalCost_InvalidPrice() {
        // Requirement 4: Validation check
        when(product.getUnitPrice()).thenReturn(-50.0);
        assertThrows(IllegalArgumentException.class,
                () -> orderProcessor.calculateTotalCost(product, 5)
        );
    }
}