package org.p009_mocks;

/*
Example 3: Difficulty Level 3 - Finance
Domain: Finance
Task: Test a Stock Trade Service with transaction validation and execution

Requirements:
Execute trade if:
        Stock quantity > 0
        Stock price > 0
        User balance ≥ total cost (quantity × price)
Deduct total cost from user balance on successful trade
Throw TradeException for invalid trades (insufficient balance/negative values)
Update stock repository after successful trade

Mock Explanation:
Complex stubbing: getUserBalance() returns predefined balances to simulate user accounts.
Verification chains:
        verify(userAccountService).deductBalance(...) checks correct amount deduction.
        verify(stockRepository).updateStockQuantity(...) confirms stock inventory update.
verifyNoInteractions(...) ensures no side effects during failure scenarios.

 */


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockTradeServiceTest {

    @Mock
    UserAccountService userAccountService; // External dependency

    @Mock
    StockRepository stockRepository; // Another dependency

    @InjectMocks
    StockTradeService stockTradeService;

    @Test
    void executeTrade_Success() throws TradeException {
        // Requirement 1: Valid trade conditions
        when(userAccountService.getUserBalance("user1")).thenReturn(5000.0);
        stockTradeService.executeTrade("user1", "AAPL", 10, 100.0);

        // Requirement 2: Verify balance update
        verify(userAccountService).deductBalance("user1", 1000.0); // 10 * 100

        // Requirement 4: Verify stock repository update
        verify(stockRepository).updateStockQuantity("AAPL", 10);
    }

    @Test
    void executeTrade_InsufficientBalance() {
        // Requirement 3: Reject if balance too low
        when(userAccountService.getUserBalance("user1")).thenReturn(500.0);
        TradeException exception = assertThrows(TradeException.class, () -> stockTradeService.executeTrade("user1", "AAPL", 10, 100.0));
        assertEquals("Insufficient balance", exception.getMessage());
        verifyNoInteractions(stockRepository); // Requirement 4: No update if failed
    }

    @Test
    void executeTrade_InvalidStockPrice() {
        // Requirement 3: Reject negative price
        assertThrows(TradeException.class, () -> stockTradeService.executeTrade("user1", "AAPL", 5, -10.0));
        verifyNoInteractions(userAccountService, stockRepository);
    }
}
