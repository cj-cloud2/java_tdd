package org.p003;

public class SimpleInterestCalculator {
    public double compute(double principal, double rate, int timeYears)  {
        if (rate < 0) throw new IllegalArgumentException("Negative rate");
        return principal * rate * timeYears / 100;
    }
}
