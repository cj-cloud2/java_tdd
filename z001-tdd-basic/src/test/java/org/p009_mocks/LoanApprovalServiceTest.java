package org.p009_mocks;
/*
Example 2: Difficulty Level 2 - Banking
Domain: Banking
Task: Test a Loan Approval Service with credit score and income checks

Requirements:
Approve loan if credit score ≥ 700 and annual income ≥ 50,000
Reject loan if either condition fails
Log approval status using an AuditLogger
Throw IllegalArgumentException for invalid inputs (income ≤ 0, credit score ≤ 0)

Mock Explanation:
CreditScoreService and AuditLogger are mocked to avoid real network/database calls.
verify(auditLogger).logApproval(...) confirms the audit logging method was called
with expected parameters.
verifyNoInteractions(...) ensures no unnecessary calls occur after validation failure.
 */


// LoanApprovalServiceTest.java
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LoanApprovalServiceTest {

    @Mock
    CreditScoreService creditScoreService; // External dependency

    @Mock
    AuditLogger auditLogger; // Another dependency to mock

    @InjectMocks
    LoanApprovalService loanApprovalService;

    @Test
    void approveLoan_Approved() {
        // Requirement 1: Conditions met for approval
        when(creditScoreService.getCreditScore(anyString())).thenReturn(750);
        boolean result = loanApprovalService.approveLoan("user123", 60000);
        assertTrue(result);
        verify(auditLogger).logApproval("user123", true); // Requirement 3: Verify audit log
    }

    @Test
    void approveLoan_RejectedDueToLowIncome() {
        // Requirement 2: Reject if income too low
        when(creditScoreService.getCreditScore(anyString())).thenReturn(750);
        boolean result = loanApprovalService.approveLoan("user123", 45000);
        assertFalse(result);
        verify(auditLogger).logApproval("user123", false);
    }

    @Test
    void approveLoan_InvalidInput() {
        // Requirement 4: Validation check
        assertThrows(IllegalArgumentException.class,
                () -> loanApprovalService.approveLoan("user123", -100)
        );
        verifyNoInteractions(creditScoreService, auditLogger); // No calls if validation fails
    }
}
