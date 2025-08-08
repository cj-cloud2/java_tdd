package org.p007_fake;

import java.time.LocalDate;
import java.util.*;

public class ExpenseBlocker {
    private final BudgetLimitStore store;
    private final Map<String, List<Double>> monthSums = new HashMap<>();

    public ExpenseBlocker(BudgetLimitStore store) {
        this.store = store;
    }

    public void addExpense(String category, double amt, LocalDate date) {
        monthSums.putIfAbsent(category, new ArrayList<>());
        double sum = monthSums.get(category).stream().mapToDouble(Double::doubleValue).sum() + amt;
        if (sum > store.getLimit(category)) throw new IllegalStateException("Limit exceeded");
        monthSums.get(category).add(amt);
    }
}

interface BudgetLimitStore {
    Double getLimit(String category);
}