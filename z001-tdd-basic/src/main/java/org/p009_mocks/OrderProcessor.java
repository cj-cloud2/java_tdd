package org.p009_mocks;

/*
Mock Explanation:

Product is mocked to isolate OrderProcessor from external dependencies (e.g., database).

when(...).thenReturn(...) stubs getUnitPrice() to return fixed values for test scenarios.

verify(...) checks mock interactions (e.g., confirm getUnitPrice() was called twice in the first test).
*/




public class OrderProcessor {

    public double calculateTotalCost(Product product, int quantity) {
        // Requirement 4: Input validation
        if (product.getUnitPrice() <= 0 || quantity <= 0) {
            throw new IllegalArgumentException("Invalid input");
        }

        double total = product.getUnitPrice() * quantity;

        // Requirement 2: Apply discount for bulk orders
        if (quantity >= 20) {
            total *= 0.9; // 10% discount
        }

        return total;
    }
}


class Product {
    private double unitPrice;

    public double getUnitPrice() {
        return unitPrice; // Actual implementation might fetch from DB
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }
}