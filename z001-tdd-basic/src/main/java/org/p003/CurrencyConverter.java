package org.p003;

public class CurrencyConverter {
    public double convert(double amount, double rate)  {
        if (amount < 0) throw new IllegalArgumentException("Amount cannot be negative");
        return amount * rate;
    }
}
