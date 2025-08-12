package org.p006_stub;

public class InventoryManager {
    private final SupplierService supplierService;
    public InventoryManager(SupplierService supplierService) {
        this.supplierService = supplierService;
    }
    // Returns true if restocked
    public boolean restockIfNeeded(String item, int currentStock, int threshold) {
        if (currentStock < threshold && supplierService.hasStock(item)) {
            supplierService.order(item, threshold - currentStock);
            return true;
        }
        return false;
    }
}
interface SupplierService {
    boolean hasStock(String item);
    void order(String item, int quantity);
}
