# Mockito Assignment 11: Inventory Management TDD Example (edu.m011)

**Difficulty Level:** 2

---

## Concept Introduction: Mocking Static Methods (PowerMock)

Mocking static methods is a technique in unit testing where calls to static methods are replaced with controlled, predictable outcomes using a mocking framework. Static methods are tightly bound to their class and cannot be overridden, making them hard to test in isolation. PowerMock (used with Mockito 5 for legacy scenarios, though Mockito can mock statics directly since v3.4) enables you to mock static calls so your tests can focus on business logic rather than system internals or external dependencies, especially for scenarios like utility functions or legacy code where refactoring isn't feasible.

*Key features:*  
- PowerMock allows for mocking static, final, and private methods, which are not directly supported by basic Mockito.  
- The typical workflow includes annotating your test class with `@PrepareForTest(ClassContainingStatic.class)` and then using `PowerMockito.mockStatic(ClassContainingStatic.class)` to enable static method mocking.  
- Mockito 5 with mockito-inline enables static mocking in JUnit5 without extra runners, but PowerMock is still used for advanced cases like mocking constructors or system classes.


---

## Business Requirement

The inventory management system should ensure accurate stock monitoring, allow secure updating of item quantities, and facilitate the transfer of items between two warehouse locations within the enterprise. The solution should support tracking via product codes and enable workflow automation for reliability and efficiency.

**Scenario:**  
A company must manage its warehouse stock, enabling staff to check stock levels, update stock when items arrive or are shipped, and transfer items between locations—all while ensuring system-level updates are correct, regardless of the actual internal implementation of static utility methods (like SKU validation, etc.).


---

## Testable Requirements Derived from Business Requirements

**Requirement 1:** Accurately get the stock level for an item by SKU code from inventory.  
**Requirement 2:** Securely update stock quantity for an item (e.g., on incoming items or sales).  
**Requirement 3:** Enable item transfer between two local warehouses and log the transfer event (using a static method for event logging).

---

## TDD Step-by-Step Guide (RED-GREEN-REFACTOR)

_**Important features covered at every pass:**_  
- **Mocking Static Methods (with PowerMock/Mockito-inline):** At every step, where the business interaction relies on a static method (e.g., utility validation, event logger), you'll see how static mocking is employed to isolate behaviour.

### Pass 1: RED-GREEN-REFACTOR for Stock Retrieval (Requirement 1)
_Before starting, make sure your pom.xml includes Junit5, Mockito 5 (or mockito-inline if mocking statics), and optionally PowerMock._

#### RED Phase (Write Failing Test)
**File:** `src/test/java/edu/m011/InventoryServiceTest.java`

```java
package edu.m011;

// Import statements
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;

// Test class for InventoryService
public class InventoryServiceTest {
    // System under test
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        // Initialize system under test (no dependencies needed yet)
        inventoryService = new InventoryService();
    }

    // Old code starts
    // Test for retrieving stock (RED)
    @Test
    void shouldReturnStockLevelForSku() {
        // Attempt to retrieve stock for a sample SKU
        int stock = inventoryService.getStockLevel("SKU123");
        // The expected stock level for initial development
        assertEquals(100, stock);
    }
    // Old code ends
}
```

_This will fail as InventoryService and required methods do not exist yet._

#### GREEN Phase (Add Implementation to Pass Test)
**File:** `src/main/java/edu/m011/InventoryService.java`

```java
package edu.m011;

// Service class for inventory operations
public class InventoryService {
    // Method to get current stock by SKU
    public int getStockLevel(String sku) {
        // For demo, return a constant value
        return 100;
    }
}
```

#### REFACTOR Phase (Clean up, prepare for next step)
- No changes needed yet. Code is minimal and easy to follow.

---

### Pass 2: RED-GREEN-REFACTOR for Stock Update (Requirement 2)
_This pass will use a static method for SKU validation, mocking its logic._

#### RED Phase (Test for updating stock; now requires SKU validation which is a static utility=mocked)
**File:** `src/test/java/edu/m011/InventoryServiceTest.java`

```java
package edu.m011;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

public class InventoryServiceTest {
    private InventoryService inventoryService;

    @BeforeEach
    void setUp() {
        inventoryService = new InventoryService();
    }

    // Old code starts (from pass 1)
    @Test
    void shouldReturnStockLevelForSku() {
        int stock = inventoryService.getStockLevel("SKU123");
        assertEquals(100, stock);
    }
    // Old code ends

    // New code starts
    // Test for updating stock, with static method validation
    @Test
    void shouldUpdateStockQuantityForValidSku() {
        // Mock the static method for SKU validation
        try (MockedStatic<SkuUtils> mocked = Mockito.mockStatic(SkuUtils.class)) {
            // Setup mock so SkuUtils.isValidSku returns true
            mocked.when(() -> SkuUtils.isValidSku("SKU123")).thenReturn(true);

            // Update stock for the SKU
            boolean result = inventoryService.updateStock("SKU123", 150);

            // Verify - should succeed
            assertTrue(result);
        }
    }
    // New code ends
}
```

#### GREEN Phase (Implement SKU validation check and update logic)
**File:** `src/main/java/edu/m011/SkuUtils.java`

```java
package edu.m011;

// Static utility class for SKU validation
public class SkuUtils {
    // Static method to validate SKU codes
    public static boolean isValidSku(String sku) {
        // Old code: always true
        return true;
    }
}
```

**File:** `src/main/java/edu/m011/InventoryService.java` _(replace old code)_

```java
package edu.m011;

public class InventoryService {
    // Old code starts
    public int getStockLevel(String sku) {
        return 100;
    }
    // Old code ends

    // New code starts
    // Method to update stock if SKU is valid
    public boolean updateStock(String sku, int newQty) {
        // Validate SKU using static method
        if (SkuUtils.isValidSku(sku)) {
            // In real code, would update a database etc.
            return true;
        }
        return false;
    }
    // New code ends
}
```


#### REFACTOR Phase
- All class and test code now supports mocking static method calls for validation and event logging.

**End of Assignment**
