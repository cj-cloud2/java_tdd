package org.p010_spy;

import java.util.ArrayList;
import java.util.List;

public class BudgetTracker {
    private double limit;
    private List<Double> expenses = new ArrayList<>();
    private final LimitNotifier notifier;

    public BudgetTracker(double limit, LimitNotifier notifier) {
        this.limit = limit;
        this.notifier = notifier;
    }

    public void addExpense(double e) {
        expenses.add(e);
        if(getTotal() > 0.8 * limit) {
            notifier.notifyLimitApproaching();
        }
    }
    public void removeExpense(double e) { expenses.remove(e); }
    public boolean isOverBudget() {
        return getTotal() > limit;
    }
    private double getTotal() {
        return expenses.stream().mapToDouble(Double::doubleValue).sum();
    }
}

// Dummy interface for notification, to be spied.
interface LimitNotifier {
    void notifyLimitApproaching();
}