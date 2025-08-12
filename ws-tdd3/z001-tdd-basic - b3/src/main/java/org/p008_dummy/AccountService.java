package org.p008_dummy;

public class AccountService {
    private Notification notification;
    public AccountService(Notification notification) {
        this.notification = notification; // Required but not used
    }
    public int deposit(int amount) {
        if (amount < 0) throw new IllegalArgumentException();
        return amount;
    }
}

interface Notification {
    void send(String user, String msg);
}

