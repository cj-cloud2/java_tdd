package org.p007_fake;

public class TransferService {
    private final AccountStore store;
    public TransferService(AccountStore s) { this.store = s; }
    public void transfer(String from, String to, double amount) {
        Double fromBal = store.getBalance(from);
        Double toBal = store.getBalance(to);
        if (fromBal == null || toBal == null) throw new IllegalArgumentException();
        if (fromBal < amount) throw new IllegalArgumentException("Insufficient funds");
        store.setBalance(from, fromBal - amount);
        store.setBalance(to, toBal + amount);
    }
}
interface AccountStore {
    Double getBalance(String id);
    void setBalance(String id, double balance);
}
