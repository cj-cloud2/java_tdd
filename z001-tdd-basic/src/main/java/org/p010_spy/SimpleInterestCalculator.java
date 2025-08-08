package org.p010_spy;

public class SimpleInterestCalculator {
    private final AuditLogger logger;

    public SimpleInterestCalculator(AuditLogger logger) {
        this.logger = logger;
    }

    public double compute(double principal, double rate, int timeYears) {
        if (rate < 0) throw new IllegalArgumentException("Negative rate");
        double result = principal * rate * timeYears / 100.0;
        logger.logCalculation(principal, rate, timeYears);
        return result;
    }
}

interface AuditLogger {
    void logCalculation(double principal, double rate, int years);
}