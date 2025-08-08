package org.p010_spy;

//Service Class

public class CurrencyConverter {
    private final UsageTracker tracker;

    public CurrencyConverter(UsageTracker tracker) {
        this.tracker = tracker;
    }

    public double convert(double amount, double rate) {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        tracker.incrementUsage();
        return amount * rate;
    }
}

interface UsageTracker {
    void incrementUsage();
}

