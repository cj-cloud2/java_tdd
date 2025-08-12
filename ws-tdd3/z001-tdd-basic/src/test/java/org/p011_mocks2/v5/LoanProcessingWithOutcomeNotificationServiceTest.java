package org.p011_mocks2.v5;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingWithOutcomeNotificationServiceTest {
    private LoanRepository loanRepository;
    private CreditBureauService creditBureauService;
    private HrVerificationService hrVerificationService;
    private DocumentValidationService documentValidationService;
    private NotificationService notificationService;
    private AuditService auditService;
    private LoanProcessingWithOutcomeNotificationService loanService;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        creditBureauService = mock(CreditBureauService.class);
        hrVerificationService = mock(HrVerificationService.class);
        documentValidationService = mock(DocumentValidationService.class);
        notificationService = mock(NotificationService.class);
        auditService = mock(AuditService.class);

        loanService = new LoanProcessingWithOutcomeNotificationService(
                loanRepository, creditBureauService, hrVerificationService,
                documentValidationService, notificationService, auditService);
    }

    // Test Case 1: Send approval notification on successful approval (mock notification service).
    @Test
    void shouldSendApprovalNotificationOnSuccessfulApproval() {
        LoanApplication application = new LoanApplication("Ellen", "1234512345", 90000);
        when(creditBureauService.fetchCreditScore(application.getApplicantId())).thenReturn(720);
        when(hrVerificationService.verifyEmployment(application.getApplicantId())).thenReturn(EmploymentStatus.VERIFIED);
        when(documentValidationService.validateDocuments(application.getApplicantId())).thenReturn(DocumentValidationStatus.VALID);

        LoanProcessingResult result = loanService.processApplication(application);

        assertTrue(result.isAccepted());
        assertEquals("Application approved with valid documents.", result.getMessage());

        // Verify that application is saved with APPROVED status
        ArgumentCaptor<LoanApplication> captor = ArgumentCaptor.forClass(LoanApplication.class);
        verify(loanRepository).save(captor.capture());
        assertEquals(LoanStatus.APPROVED, captor.getValue().getStatus());

        // Verify notifications sent
        verify(notificationService).sendApprovalNotification(application);

        // Verify audit trail creation
        verify(auditService).logOutcome(application, "Application approved");
    }
}
