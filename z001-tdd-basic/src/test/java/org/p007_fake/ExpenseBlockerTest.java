package org.p007_fake;

/*

## 4. Finance: Fake In-Memory Budget Category Limit Store

### Task \& Requirements

- Service to auto-block expenses if a category’s monthly total would exceed a user’s custom limit.
- Real system queries a remote DB; tests should use an in-memory Fake.


#### Requirements:

- Each expense has amount/category/date.
- Adding expense checks sum for that month/category.
- Block if limit exceeded. Limit is stored in Fake.

 */
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.*;

// Fake lets you configure and verify limit logic with no external DB.
class FakeBudgetLimitStore implements BudgetLimitStore {
    final Map<String, Double> limits = new HashMap<>();
    public void setLimit(String category, double limit) { limits.put(category, limit); }
    public Double getLimit(String category) { return limits.get(category); }
}

public class ExpenseBlockerTest {
    @Test
    void testBlockedWhenOverLimit() {
        FakeBudgetLimitStore store = new FakeBudgetLimitStore();
        store.setLimit("Food", 100);
        ExpenseBlocker blocker = new ExpenseBlocker(store);
        blocker.addExpense("Food", 60, LocalDate.of(2025, 3, 1));
        assertThrows(IllegalStateException.class, () -> blocker.addExpense("Food", 50, LocalDate.of(2025, 3, 10)));
    }
}

