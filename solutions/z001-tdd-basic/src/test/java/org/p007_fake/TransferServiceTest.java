package org.p007_fake;

/*

## 3. Banking: Fake In-Memory Account Store

### Task \& Requirements

- Implement a funds transfer operation between two accounts, updating balances in a backing store.
- Actual store is persistent; test via a Fake in-memory store.


#### Requirements:

- Store accounts and balances.
- Transfer should debit and credit correctly.
- Error if account missing or insufficient funds.

 */


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.util.*;

class FakeAccountStore implements AccountStore {
    Map<String, Double> accounts = new HashMap<>();

    public Double getBalance(String id) {
        return accounts.get(id);
    }

    public void setBalance(String id, double b) {
        accounts.put(id, b);
    }
}

public class TransferServiceTest {
    @Test
    void testSuccessfulTransfer() {
        FakeAccountStore store = new FakeAccountStore();
        store.setBalance("A", 100);
        store.setBalance("B", 50);
        TransferService service = new TransferService(store);
        service.transfer("A", "B", 40);
        assertEquals(60, store.getBalance("A"));
        assertEquals(90, store.getBalance("B"));
    }

    @Test
    void testInsufficientFunds() {
        FakeAccountStore store = new FakeAccountStore();
        store.setBalance("A", 10);
        store.setBalance("B", 50);
        TransferService service = new TransferService(store);
        assertThrows(IllegalArgumentException.class, () -> service.transfer("A", "B", 40));
    }
}

// Fake allows full control of test data and simulates persistence.