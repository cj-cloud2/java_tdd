package org.p010_spy;


// Service and Helper Classes

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TransactionHistory {
    private List<Transaction> transactions = new ArrayList<>();
    private final AccessLogger logger;

    public TransactionHistory(AccessLogger logger) {
        this.logger = logger;
    }

    public void addTransaction(Transaction t) {
        transactions.add(t);
    }

    public List<Transaction> filterByDateRange(LocalDate from, LocalDate to) {
        if (from.isAfter(to)) throw new IllegalArgumentException("Invalid date range");
        List<Transaction> result = transactions.stream()
                .filter(t -> !t.getDate().isBefore(from) && !t.getDate().isAfter(to))
                .collect(Collectors.toList());
        logger.logAccess(result.size());
        return result;
    }
}

class Transaction {
    enum Type {DEPOSIT, WITHDRAWAL}

    private double amount;
    private LocalDate date;
    private Type type;

    public Transaction(double amount, LocalDate date, Type type) {
        this.amount = amount;
        this.date = date;
        this.type = type;
        ;
    }

    public double getAmount() {
        return amount;
    }

    public LocalDate getDate() {
        return date;
    }

    public Type getType() {
        return type;
    }
}


interface AccessLogger {
    void logAccess(int resultCount);
}