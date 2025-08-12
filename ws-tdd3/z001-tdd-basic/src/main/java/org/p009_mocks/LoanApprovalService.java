package org.p009_mocks;
// CreditScoreService.java (External service)
interface CreditScoreService {
    int getCreditScore(String userId); // Might call a REST API
}

// AuditLogger.java
interface AuditLogger {
    void logApproval(String userId, boolean approved);
}

// LoanApprovalService.java
public class LoanApprovalService {

    private CreditScoreService creditScoreService;
    private AuditLogger auditLogger;

    public LoanApprovalService(CreditScoreService creditScoreService, AuditLogger auditLogger) {
        this.creditScoreService = creditScoreService;
        this.auditLogger = auditLogger;
    }

    public boolean approveLoan(String userId, double annualIncome) {
        // Requirement 4: Input validation
        if (annualIncome <= 0) {
            throw new IllegalArgumentException("Income must be positive");
        }

        int creditScore = creditScoreService.getCreditScore(userId);
        if (creditScore <= 0) throw new IllegalArgumentException("Invalid credit score");

        // Requirement 1 & 2: Approval logic
        boolean approved = (creditScore >= 700 && annualIncome >= 50000);

        // Requirement 3: Audit logging
        auditLogger.logApproval(userId, approved);
        return approved;
    }
}