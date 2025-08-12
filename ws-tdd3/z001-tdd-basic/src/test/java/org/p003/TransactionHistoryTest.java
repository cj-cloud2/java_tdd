package org.p003;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.List;

class TransactionHistoryTest {
    // RED: Filter by date range.
    @Test
    void testFilterByDateRange() {
        TransactionHistory cut = new TransactionHistory();
        cut.add(new Transaction(100, LocalDate.of(2025,8,1), Transaction.Type.DEPOSIT));
        cut.add(new Transaction(50, LocalDate.of(2025,8,3), Transaction.Type.WITHDRAWAL));
        cut.add(new Transaction(30, LocalDate.of(2025,7,29), Transaction.Type.DEPOSIT));
        List<Transaction> result = cut.filterByDateRange(LocalDate.of(2025,8,1), LocalDate.of(2025,8,4));
        assertEquals(2, result.size());
    }
    // RED: Filter by type.
    @Test
    void testFilterByType() {
        TransactionHistory history = new TransactionHistory();
        history.add(new Transaction(100, LocalDate.of(2025,8,1), Transaction.Type.DEPOSIT));
        history.add(new Transaction(50, LocalDate.of(2025,8,3), Transaction.Type.WITHDRAWAL));
        List<Transaction> deposits = history.filterByType(Transaction.Type.DEPOSIT);
        assertEquals(1, deposits.size());
    }
    // RED: No transactions in range returns empty list.
    @Test
    void testNoTransactionsInRange() {
        TransactionHistory history = new TransactionHistory();
        history.add(new Transaction(100, LocalDate.of(2025,8,1), Transaction.Type.DEPOSIT));
        List<Transaction> result = history.filterByDateRange(LocalDate.of(2025,8,2), LocalDate.of(2025,8,3));
        assertTrue(result.isEmpty());
    }
    // RED: Invalid range throws error.
    @Test
    void testInvalidDateRangeThrows() {
        TransactionHistory history = new TransactionHistory();
        assertThrows(IllegalArgumentException.class,
                () -> history.filterByDateRange(LocalDate.of(2025,8,4), LocalDate.of(2025,8,1)));
    }
}
