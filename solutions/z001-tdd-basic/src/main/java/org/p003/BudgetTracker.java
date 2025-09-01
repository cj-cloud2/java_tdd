package org.p003;

import java.util.ArrayList;
import java.util.List;

public class BudgetTracker {
    private double limit;
    private List<Double> expenses = new ArrayList<>();

    public BudgetTracker(double limit) {
        this.limit = limit;
    }
    public void addExpense(double e) {
        expenses.add(e);
    }
    public void removeExpense(double e) {
        expenses.remove(e);
    }
    public boolean isOverBudget() {
        return expenses.stream().mapToDouble(Double::doubleValue).sum() > limit;
    }
}