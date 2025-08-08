package org.p011_mocks2.v3;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LoanProcessingWithEmploymentVerificationServiceTest {
    private LoanRepository loanRepository;
    private CreditBureauService creditBureauService;
    private HrVerificationService hrVerificationService;
    private LoanProcessingWithEmploymentVerificationService loanService;

    @BeforeEach
    void setUp() {
        loanRepository = mock(LoanRepository.class);
        creditBureauService = mock(CreditBureauService.class);
        hrVerificationService = mock(HrVerificationService.class);
        loanService = new LoanProcessingWithEmploymentVerificationService(
                loanRepository, creditBureauService, hrVerificationService);
    }

    // Test Case 1: Approve when employment is confirmed (mock HR service).
    @Test
    void shouldApproveIfEmploymentVerified() {
        LoanApplication application = new LoanApplication("Charlie", "222334445", 60000);
        when(creditBureauService.fetchCreditScore(application.getApplicantId())).thenReturn(720);
        when(hrVerificationService.verifyEmployment(application.getApplicantId())).thenReturn(EmploymentStatus.VERIFIED);

        LoanProcessingResult result = loanService.processApplication(application);

        assertTrue(result.isAccepted());
        assertEquals("Application approved with employment verified.", result.getMessage());
        ArgumentCaptor<LoanApplication> captor = ArgumentCaptor.forClass(LoanApplication.class);
        verify(loanRepository).save(captor.capture());
        assertEquals(LoanStatus.APPROVED, captor.getValue().getStatus());
    }
}
