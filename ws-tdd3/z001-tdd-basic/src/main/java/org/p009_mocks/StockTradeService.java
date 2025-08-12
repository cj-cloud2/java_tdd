package org.p009_mocks;

// UserAccountService.java
interface UserAccountService {
    double getUserBalance(String userId);
    void deductBalance(String userId, double amount);
}

// StockRepository.java
interface StockRepository {
    void updateStockQuantity(String stockSymbol, int quantityDelta); // Updates DB
}

// Custom Exception
class TradeException extends Exception {
    public TradeException(String message) {
        super(message);
    }
}

// StockTradeService.java
public class StockTradeService {

    private UserAccountService userAccountService;
    private StockRepository stockRepository;

    // Constructor injection
    public StockTradeService(UserAccountService userAccountService, StockRepository stockRepository) {
        this.userAccountService = userAccountService;
        this.stockRepository = stockRepository;
    }

    public void executeTrade(String userId, String stockSymbol, int quantity, double price) throws TradeException {
        // Requirement 3: Validate inputs
        if (quantity <= 0 || price <= 0) {
            throw new TradeException("Invalid trade parameters");
        }

        double totalCost = quantity * price;
        double balance = userAccountService.getUserBalance(userId);

        // Requirement 1: Check sufficient balance
        if (balance < totalCost) {
            throw new TradeException("Insufficient balance");
        }

        // Requirement 2: Deduct balance
        userAccountService.deductBalance(userId, totalCost);

        // Requirement 4: Update stock repository
        stockRepository.updateStockQuantity(stockSymbol, quantity);
    }
}