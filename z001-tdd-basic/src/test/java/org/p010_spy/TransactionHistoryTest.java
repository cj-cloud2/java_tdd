package org.p010_spy;
/*
## Example 4. Banking: Transaction History Filter and Access Log

        ### Task and Requirements

        - Filter a list of transactions by date/type; every filter action must log the number of results.
        - Requirements:

        1. Filtering by date returns correct list and logs count,
        2. Filtering by type returns correct list and logs count,
        3. Invalid range throws error, no log.


        #### Test Class Using SPY

*/

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;


class TransactionHistoryTest {
    @Test
    void testFilterByDateLogsAccess() {
        // Dummy logger for SPY
        AccessLogger logger = Mockito.spy(AccessLogger.class);
        TransactionHistory history = new TransactionHistory(logger);
        history.addTransaction(new Transaction(100, LocalDate.of(2025, 8, 1), Transaction.Type.DEPOSIT));
        history.addTransaction(new Transaction(50, LocalDate.of(2025, 8, 3), Transaction.Type.WITHDRAWAL));
        List<Transaction> result = history.filterByDateRange(LocalDate.of(2025, 8, 1), LocalDate.of(2025, 8, 4));
        assertEquals(2, result.size());
        // SPY: verify correct logging
        Mockito.verify(logger).logAccess(2);
    }

    @Test
    void testInvalidRangeThrowsAndNoLog() {
        AccessLogger logger = Mockito.spy(AccessLogger.class);
        TransactionHistory history = new TransactionHistory(logger);
        assertThrows(IllegalArgumentException.class, () ->
                history.filterByDateRange(LocalDate.of(2025, 8, 4), LocalDate.of(2025, 8, 1)));
        Mockito.verify(logger, Mockito.never()).logAccess(Mockito.anyInt());
    }
}