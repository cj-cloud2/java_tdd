package org.p003;


import java.time.LocalDate;

public class Transaction {
    public enum Type { DEPOSIT, WITHDRAWAL }
    private double amount;
    private LocalDate date;
    private Type type;
    public Transaction(double amount, LocalDate date, Type type) {
        this.amount = amount;
        this.date = date;
        this.type = type;
    }
    public double getAmount() { return amount; }
    public LocalDate getDate() { return date; }
    public Type getType() { return type; }
}