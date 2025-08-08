package org.p011_mocks2.v4;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.p011_mocks2.v3.*;
import org.p011_mocks2.v3.CreditBureauService;
import org.p011_mocks2.v3.EmploymentStatus;
import org.p011_mocks2.v3.HrVerificationService;
import org.p011_mocks2.v3.LoanApplication;
import org.p011_mocks2.v3.LoanProcessingResult;
import org.p011_mocks2.v3.LoanProcessingWithEmploymentVerificationService;
import org.p011_mocks2.v3.LoanRepository;
import org.p011_mocks2.v3.LoanStatus;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class LoanProcessingWithEmploymentVerificationServiceTest {
    private org.p011_mocks2.v3.LoanRepository loanRepository;
    private org.p011_mocks2.v3.CreditBureauService creditBureauService;
    private org.p011_mocks2.v3.HrVerificationService hrVerificationService;
    private org.p011_mocks2.v3.LoanProcessingWithEmploymentVerificationService loanService;

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
        org.p011_mocks2.v3.LoanApplication application = new org.p011_mocks2.v3.LoanApplication("Charlie", "222334445", 60000);
        when(creditBureauService.fetchCreditScore(application.getApplicantId())).thenReturn(720);
        when(hrVerificationService.verifyEmployment(application.getApplicantId())).thenReturn(EmploymentStatus.VERIFIED);

        LoanProcessingResult result = loanService.processApplication(application);

        assertTrue(result.isAccepted());
        assertEquals("Application approved with employment verified.", result.getMessage());
        ArgumentCaptor<org.p011_mocks2.v3.LoanApplication> captor = ArgumentCaptor.forClass(LoanApplication.class);
        verify(loanRepository).save(captor.capture());
        assertEquals(LoanStatus.APPROVED, captor.getValue().getStatus());
    }
}
