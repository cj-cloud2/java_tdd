package org.p011_mocks2.v5;

// New interface for audit logging service
public interface AuditService {
    void logOutcome(LoanApplication application, String outcome);
}
