package org.p007_fake;

public class CurrencyConverterService {
    private final CurrencyRateRepository repository;

    public CurrencyConverterService(CurrencyRateRepository repo) {
        this.repository = repo;
    }

    public double convert(String from, String to, double amount) {
        Double rate = repository.getRate(from, to);
        if (rate == null) throw new IllegalArgumentException("Rate not found");
        return amount * rate;
    }
}

interface CurrencyRateRepository {
    Double getRate(String from, String to);
}