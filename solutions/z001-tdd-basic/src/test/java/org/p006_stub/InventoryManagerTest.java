package org.p006_stub;

/*
### Example 3: Item Restocking Logic

**Task Requirement:**
Create an `InventoryManager` that checks if items need to be restocked using
the `SupplierService`. If stock is below threshold, it should request restock.
Use a stub for `SupplierService` to simulate item availability.
 */

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


// Stub Explanation: SupplierServiceStub's constructor sets supplied availability.
// Used directly to inject simulated supplier status.

class InventoryManagerTest {
    // Ensures restocking occurs when supply is available
    @Test
    void restocksWhenLowAndSupplierHasStock() {
        SupplierServiceStub stub = new SupplierServiceStub(true);
        InventoryManager manager = new InventoryManager(stub);
        boolean restocked = manager.restockIfNeeded("itemX", 2, 5);
        assertTrue(restocked, "Should restock if below threshold and supplier has stock");
    }

    // Ensures no restock if supplier is out of stock
    @Test
    void doesNotRestockIfSupplierOut() {
        SupplierServiceStub stub = new SupplierServiceStub(false);
        InventoryManager manager = new InventoryManager(stub);
        boolean restocked = manager.restockIfNeeded("itemX", 2, 5);
        assertFalse(restocked, "Should not restock if supplier has no stock");
    }
}

class SupplierServiceStub implements SupplierService {
    private final boolean inStock;
    public SupplierServiceStub(boolean inStock) { this.inStock = inStock; }
    @Override public boolean hasStock(String item) { return inStock; }
    @Override public void order(String item, int quantity) { /* do nothing */ }
}