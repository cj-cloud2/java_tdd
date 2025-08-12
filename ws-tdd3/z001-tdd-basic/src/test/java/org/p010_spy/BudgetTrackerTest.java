package org.p010_spy;

/*

## Example 1. Finance: Monthly Budget Tracker

### Task and Requirements

- Implement a budget tracker class to monitor expenses against a monthly limit.
- Users can:

1. Add expenses,
2. Remove expenses,
3. Check if they're over budget.
- Requirements:
    - If total exceeds the limit, `isOverBudget` returns true.
    - Removing enough expense returns the total under the limit (`isOverBudget` returns false).
    - Whenever expenses are added, method `notifyLimitApproaching` is called if total is above 80% of limit.


 */

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;



class BudgetTrackerTest {
    // Requirement 1: Adding expenses below the limit should not trigger over-budget.
    @Test
    void testExpenseWithinLimit() {
        LimitNotifier notifier = Mockito.spy(LimitNotifier.class);
        BudgetTracker tracker = new BudgetTracker(100.0, notifier);

        tracker.addExpense(60.0);
        assertFalse(tracker.isOverBudget());
        // SPY usage: no notification yet.
        Mockito.verify(notifier, Mockito.never()).notifyLimitApproaching();
    }

    // Requirement 2: Adding expense above 80% limit triggers notification.
    @Test
    void testNotifyLimitApproaching() {
        LimitNotifier notifier = Mockito.spy(LimitNotifier.class);
        BudgetTracker tracker = new BudgetTracker(100.0, notifier);

        tracker.addExpense(85.0);
        Mockito.verify(notifier).notifyLimitApproaching(); // SPY: check interaction.
    }

    // Requirement 3: Adding expense exceeding limit triggers over budget.
    @Test
    void testExpenseOverLimit() {
        LimitNotifier notifier = Mockito.spy(LimitNotifier.class);
        BudgetTracker tracker = new BudgetTracker(100.0, notifier);

        tracker.addExpense(120.0);
        assertTrue(tracker.isOverBudget());
        Mockito.verify(notifier).notifyLimitApproaching();
    }

    // SPY Explanation:
    // The notifier spy remembers each call to notifyLimitApproaching, allowing us to verify interaction,
    // rather than stubbing the behavior.
}