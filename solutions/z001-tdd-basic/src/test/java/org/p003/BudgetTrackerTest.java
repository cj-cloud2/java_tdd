package org.p003;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

class BudgetTrackerTest {
    // RED: Expenses within limit.
    @Test
    void testWithinBudget() {
        BudgetTracker tracker = new BudgetTracker(100);
        tracker.addExpense(20);
        tracker.addExpense(40);
        assertFalse(tracker.isOverBudget());
    }
    // RED: Exactly at the limit.
    @Test
    void testAtLimit() {
        BudgetTracker tracker = new BudgetTracker(100);
        tracker.addExpense(100);
        assertFalse(tracker.isOverBudget());
    }
    // RED: Exceeds budget.
    @Test
    void testOverBudget() {
        BudgetTracker tracker = new BudgetTracker(100);
        tracker.addExpense(70);
        tracker.addExpense(40);
        assertTrue(tracker.isOverBudget());
    }
    // RED: Remove expense brings back within budget.
    @Test
    void testRemoveExpense() {
        BudgetTracker tracker = new BudgetTracker(100);
        tracker.addExpense(70);
        tracker.addExpense(40);
        tracker.removeExpense(40);
        assertFalse(tracker.isOverBudget());
    }
}